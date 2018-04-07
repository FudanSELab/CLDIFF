package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.main.astdiff.BaseDiffMiner;
import edu.fdu.se.main.astdiff.DiffMiner;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.*;

/**
 * Created by huangkaifeng on 2018/4/6.
 */
public class JGitHelper extends JGitCommand {

    public JGitHelper(String repopath) {
        super(repopath);
    }

    /**
     * 输出output即可
     * @param outDir
     */
    public void walkRepoFromBackwards(String outDir) {
        try {
            FileOutputLog fileOutputLog = new FileOutputLog(outDir, 1);
            Queue<RevCommit> commitQueue = new LinkedList<>();
            Map<String, Boolean> isTraversed = new HashMap<>();
            BaseDiffMiner baseDiffMiner = new DiffMiner();
            List<Ref> mList = this.git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref item : mList) {
                RevCommit commit = revWalk.parseCommit(item.getObjectId());
                commitQueue.offer(commit);
                while (commitQueue.size() != 0) {
                    RevCommit queueCommitItem = commitQueue.poll();
                    RevCommit[] parentCommits = queueCommitItem.getParents();
                    if (isTraversed.containsKey(queueCommitItem.getName()) || parentCommits == null) {
                        continue;
                    }
                    Map<String, Map<String, List<String>>> changedFiles = this.getCommitParentMappedFileList(queueCommitItem.getName());
                    for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
                        String parentCommitId = entry.getKey();
                        Map<String, List<String>> changedFileEntry = entry.getValue();
                        if (changedFileEntry.containsKey("modifiedFiles")) {
                            List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                            for (String file : modifiedFile) {
                                if (!file.endsWith(".java")) {
                                    continue;
                                }
                                byte[] prevFile = this.extract(file, parentCommitId);
                                byte[] currFile = this.extract(file, queueCommitItem.getName());
                                int index = file.lastIndexOf("/");
                                String fileName = file.substring(index + 1, file.length());
                                System.out.println("CommitId: " + queueCommitItem.getName());
                                String dirName = parentCommitId + "-" + commit.getName();
                                baseDiffMiner.doo(fileName.substring(0,fileName.length()-5),prevFile,currFile,outDir+"/"+dirName+"/"+fileName);
//                                fileOutputLog.writeRQ1CommitFile(prevFile, currFile, parentCommitId + "-" + queueCommitItem.getName(), fileName);
                            }
                        }
                    }
                    isTraversed.put(queueCommitItem.getName(), true);
                    for (RevCommit item2 : parentCommits) {
                        RevCommit commit2 = revWalk.parseCommit(item2.getId());
                        commitQueue.offer(commit2);
                    }
                    break;
                }
            }
            System.out.println("CommitSum:" + isTraversed.size());
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 输出debug的文件
     * @param commitString
     * @param outDir
     */
    public void analyzeOneCommit(String commitString, String outDir) {
        try {
            BaseDiffMiner baseDiffMiner = new BaseDiffMiner();
            FileOutputLog fileOutputLog = new FileOutputLog(outDir, 1);
            ObjectId commitId = ObjectId.fromString(commitString);
            RevCommit commit = revWalk.parseCommit(commitId);
            if (commit.getParents() == null) {
                return;
            }
            Map<String, Map<String, List<String>>> changedFiles = this.getCommitParentMappedFileList(commit.getName());
            for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
                String parentCommitId = entry.getKey();
                Map<String, List<String>> changedFileEntry = entry.getValue();
                if (changedFileEntry.containsKey("modifiedFiles")) {
                    List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                    for (String file : modifiedFile) {
                        if (!file.endsWith(".java")) {
                            continue;
                        }
                        byte[] prevFile = this.extract(file, parentCommitId);
                        byte[] currFile = this.extract(file, commit.getName());
                        int index = file.lastIndexOf("/");
                        String fileName = file.substring(index + 1, file.length());
                        String dirName = parentCommitId + "-" + commit.getName();
                        baseDiffMiner.doo(fileName.substring(0,fileName.length()-5),prevFile,currFile,outDir+"/"+dirName+"/"+fileName);
                    }
                }
            }
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

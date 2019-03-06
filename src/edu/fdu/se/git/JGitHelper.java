package edu.fdu.se.git;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.fdu.se.cldiff.CLDiffCore;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import org.eclipse.jgit.treewalk.CanonicalTreeParser;

/**
 * Created by huangkaifeng on 2018/4/6.
 *
 */
public class JGitHelper extends JGitCommand {

    public JGitHelper(String repopath) {
        super(repopath);
    }

    /**
     * 输出output即可
     */
    public void walkRepoFromBackwards(IHandleCommit iHandleCommit) {
        try {
            int commitNum = 0;
            Queue<RevCommit> commitQueue = new LinkedList<>();
            Map<String, Boolean> isTraversed = new HashMap<>();
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
                    Map<String, List<DiffEntry>> changedFiles = this.getCommitParentMappedDiffEntry(queueCommitItem.getName());
                    iHandleCommit.handleCommit(changedFiles, queueCommitItem.getName(),commit);
                    commitNum++;
                    isTraversed.put(queueCommitItem.getName(), true);
                    for (RevCommit item2 : parentCommits) {
                        RevCommit commit2 = revWalk.parseCommit(item2.getId());
                        commitQueue.offer(commit2);
                    }
                }
            }
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
     *
     * @param commitString
     */
    public void analyzeOneCommit(IHandleCommit iHandleCommit, String commitString) {
        try {

            ObjectId commitId = ObjectId.fromString(commitString);
            RevCommit commit = revWalk.parseCommit(commitId);
            if (commit.getParents() == null) {
                return;
            }
            Map<String,List<DiffEntry>> changedFiles = this.getCommitParentMappedDiffEntry(commit.getName());
            iHandleCommit.handleCommit(changedFiles, commitString,commit);

        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出debug的文件
     *
     * @param currentCommitString,nexCommitString
     */
    public void analyzeTwoCommits(IHandleCommit handleDiffCommits, String currentCommitString, String nexCommitString) {
        try {

            ObjectId currCommitId = ObjectId.fromString(currentCommitString);
            RevCommit currCommit = revWalk.parseCommit(currCommitId);
            ObjectId nextCommitId = ObjectId.fromString(nexCommitString);
            RevCommit nextCommit = revWalk.parseCommit(nextCommitId);
            Map<String, List<DiffEntry>> changedFiles = this.getTwoCommitsMappedFileList(currCommit.getName(),nextCommit.getName());
            handleDiffCommits.handleCommit(changedFiles, currentCommitString,currCommit,nexCommitString,nextCommit);

        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出output即可
     */
    public void walkRepoFromBackwardsCountLineNumber(IHandleCommit iHandleCommit) {
        try {
            int commitNum = 0;
            Queue<RevCommit> commitQueue = new LinkedList<>();
            Map<String, Boolean> isTraversed = new HashMap<>();
            long startTime = System.nanoTime();   //获取开始时间
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
                    int temp2 = getCommitFileEditLineNumber(queueCommitItem);
//                    totalChangedLineNumber += temp2;
                    if(temp2==1) {
                        commitNum++;
                    }
                    isTraversed.put(queueCommitItem.getName(), true);
                    for (RevCommit item2 : parentCommits) {
                        RevCommit commit2 = revWalk.parseCommit(item2.getId());
                        commitQueue.offer(commit2);
                    }
                }
            }
            long endTime = System.nanoTime(); //获取结束时间
//            System.out.println("CommitSum:" + isTraversed.size());
//            System.out.println("totalCommitNum: " + commitNum);
            System.out.println("totalChangedLineNumber: " + totalChangedLineNumber);
            System.out.println("----total time:" + (endTime - startTime));
            System.out.println("----commitnum "+commitNum);
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

    private long totalChangedLineNumber;




    public int getCommitFileEditLineNumber(RevCommit commit) {
        try {
            System.out.println("----commit id:" + commit.getName());
            int count = 0;
            long startTime = System.nanoTime();   //获取开始时间
            RevCommit[] parentsCommits = commit.getParents();
            for (RevCommit parent : parentsCommits) {
                ObjectReader reader = git.getRepository().newObjectReader();
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                ObjectId newTree = commit.getTree().getId();
                newTreeIter.reset(reader, newTree);
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                RevCommit pCommit = revWalk.parseCommit(parent.getId());
                ObjectId oldTree = pCommit.getTree().getId();
                oldTreeIter.reset(reader, oldTree);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DiffFormatter diffFormatter = new DiffFormatter(out);
                diffFormatter.setRepository(git.getRepository());
                List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
                diffFormatter.setContext(0);
                for (DiffEntry entry : entries) {
                    switch (entry.getChangeType()) {
                        case MODIFY:
                            String mOldPath = entry.getOldPath();
                            if (CLDiffCore.isFilter(mOldPath)) {
                                continue;
                            }else{
                                //at least one file is changed
                                return 1;
                            }
//                            FileHeader fileHeader = diffFormatter.toFileHeader(entry);
//                            EditList editList = fileHeader.toEditList();
//                            int temp = lineNumber(editList);
//                            count += temp;
//                            System.out.println("----"+mOldPath.substring(mOldPath.lastIndexOf("/") + 1) + " " + count);
//                            diffFormatter.format(entry);
//                            out.reset();
//                            break;
                        case ADD:
                        case DELETE:
                        default:
                            break;
                    }
                }
                diffFormatter.close();
            }
            long endTime = System.nanoTime(); //获取结束时间

            System.out.println("----one commit time:" + (endTime - startTime));
            return 0;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int lineNumber(EditList edits) {
        int cnt = 0;
        for (Edit e : edits) {
            if (e.getBeginA() == e.getEndA()) {
                cnt += e.getEndB() - e.getBeginB();

            } else if (e.getBeginB() == e.getEndB()) {
                cnt += e.getEndA() - e.getBeginA();
            } else if (e.getBeginA() < e.getEndA() && e.getBeginB() < e.getEndB()) {
                cnt += e.getEndB() - e.getBeginB() + e.getEndA() - e.getBeginA();
            }
        }
        return cnt;
    }

}

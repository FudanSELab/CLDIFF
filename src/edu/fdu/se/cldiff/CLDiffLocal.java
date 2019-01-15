package edu.fdu.se.cldiff;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.fileutil.PathUtil;
import edu.fdu.se.git.HandleDiffCommits;
import edu.fdu.se.git.IHandleCommit;
import edu.fdu.se.git.JGitHelper;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.Meta;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/23.
 * cmd entrance for cldiff
 */
public class CLDiffLocal implements IHandleCommit, HandleDiffCommits {

    public JGitHelper jGitHelper;
    public Meta meta;

    public void run(String commitId,String repo,String outputDir){
        jGitHelper = new JGitHelper(repo);
        initMeta(repo,commitId,outputDir);
        jGitHelper.analyzeOneCommit(this,commitId);
        CLDiffAPI clDiffAPI = new CLDiffAPI(outputDir,meta);
        clDiffAPI.generateDiffMinerOutput();
    }

    public void run(String currCommitId, String nextCommitId,String repo,String outputDir){
        jGitHelper = new JGitHelper(repo);
        initMeta(repo,nextCommitId,outputDir);
        meta.addParentCommit(currCommitId);
        jGitHelper.analyzeTwoCommits(this,currCommitId,nextCommitId);
        CLDiffAPI clDiffAPI = new CLDiffAPI(outputDir,meta);
        clDiffAPI.generateDiffMinerOutput();
    }



    public void initMeta(String repo,String commitId,String outputDir){
        meta = new Meta();
        meta.setCommit_hash(commitId);
        meta.setProject_name(PathUtil.getGitProjectNameFromGitFullPath(repo));
        meta.setActions(null);
        meta.setAuthor(null);
        meta.setCommit_log(null);
        meta.setCommitter(null);
        meta.setDate_time(null);
        meta.setOutputDir(outputDir+'/'+PathUtil.getGitProjectNameFromGitFullPath(repo)+'/'+commitId);
        meta.setLinkPath(meta.getOutputDir()+"/link.json");
        Global.mmeta = meta;

    }

    public void loadCommitMeta(String author,int timeSeconds,String committer,String commitLog){
        meta.setAuthor(author);
        meta.setCommitter(committer);
        meta.setCommit_log(commitLog);
        Calendar c=Calendar.getInstance();
        long millions=new Long(timeSeconds).longValue()*1000;
        c.setTimeInMillis(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(c.getTime());
        meta.setDate_time(dateString);
    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String commitId,RevCommit commit){
        loadCommitMeta(commit.getAuthorIdent().getName(),commit.getCommitTime(),commit.getCommitterIdent().getName(),commit.getShortMessage()+"\n\n\n"+commit.getFullMessage());
        int cnt = 0;
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            meta.addParentCommit(parentCommitId);
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                if(modifiedFile !=null && modifiedFile.size()!=0) {
                    for (String file : modifiedFile) {
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        meta.addAction("modified");
                        cnt += 1;
                    }
                }
            }
            if(changedFileEntry.containsKey("addedFiles")){
                List<String> addedFile = changedFileEntry.get("addedFiles");
                if(addedFile!=null && addedFile.size()!=0) {
                    for (String file : addedFile) {
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setAddedCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        meta.addAction("added");
                        cnt += 1;
                    }
                }
            }
            if(changedFileEntry.containsKey("deletedFiles")){
                List<String> deleted = changedFileEntry.get("deletedFiles");
                if(deleted!=null && deleted.size()!=0) {
                    for (String file : deleted) {
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setDeletedCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        cnt += 1;
                        meta.addAction("removed");
                    }
                }
            }

        }
    }

    public  void handleCommit(Map<String, Map<String, List<DiffEntry>>> changedFiles, String currCommitId, RevCommit currCommit, String nextCommitId, RevCommit nextCommit){
        loadCommitMeta(nextCommit.getAuthorIdent().getName(),nextCommit.getCommitTime(),nextCommit.getCommitterIdent().getName(),nextCommit.getShortMessage()+"\n\n\n"+nextCommit.getFullMessage());
        int cnt = 0;
        for (Map.Entry<String, Map<String, List<DiffEntry>>> entry : changedFiles.entrySet()) {
            Map<String, List<DiffEntry>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<DiffEntry> modifiedFile = changedFileEntry.get("modifiedFiles");
                if(modifiedFile !=null && modifiedFile.size()!=0) {

                    for (DiffEntry fileEntry : modifiedFile) {
                        String file = fileEntry.getOldPath();
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setCommitFile(cnt, currCommitId, nextCommitId, file,isFiltered);
                        meta.addAction("modified");
                        cnt += 1;
                    }
                }
            }
            if(changedFileEntry.containsKey("addedFiles")){
                List<DiffEntry> addedFile = changedFileEntry.get("addedFiles");
                if(addedFile!=null && addedFile.size()!=0) {
                    for (DiffEntry fileEntry : addedFile) {
                        String file = fileEntry.getNewPath();
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setAddedCommitFile(cnt, currCommitId, nextCommitId, file,isFiltered);
                        meta.addAction("added");
                        cnt += 1;
                    }
                }
            }
            if(changedFileEntry.containsKey("deletedFiles")){
                List<DiffEntry> deleted = changedFileEntry.get("deletedFiles");
                if(deleted!=null && deleted.size()!=0) {
                    for (DiffEntry fileEntry : deleted) {
                        String file = fileEntry.getOldPath();
                        boolean isFiltered = CLDiffCore.isFilter(file);
                        setDeletedCommitFile(cnt, currCommitId, nextCommitId, file,isFiltered);
                        cnt += 1;
                        meta.addAction("removed");
                    }
                }
            }

        }
    }

    public void setCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            byte[] prevFile = jGitHelper.extract(filePath, parentCommitId);
            byte[] currFile = jGitHelper.extract(filePath, commitId);
            int index = filePath.lastIndexOf("/");
            Path prevFilePath = Paths.get(meta.getOutputDir() + "/prev/" + parentCommitId + "/" + filePath);
            Path currFilePath = Paths.get(meta.getOutputDir() + "/curr/" + parentCommitId + "/" + filePath);
            if(!prevFilePath.toFile().exists() || !currFilePath.toFile().exists()) {
                int index2 = prevFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String prevDir = prevFilePath.toFile().getAbsolutePath().substring(0,index2);
                index2 = currFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String currDir = currFilePath.toFile().getAbsolutePath().substring(0,index2);
                File prev = new File(prevDir);
                if(!prev.exists()){
                    prev.mkdirs();
                }
                File curr = new File(currDir);
                if(!curr.exists()){
                    curr.mkdirs();
                }
            }
            Files.write(prevFilePath, prevFile);
            Files.write(currFilePath, currFile);
            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setPrev_file_path("prev/" + parentCommitId + "/" + filePath);
            commitFile.setCurr_file_path("curr/" + parentCommitId + "/" + filePath);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/" + parentCommitId + "/Diff" + fileName + ".json");
            }
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAddedCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            byte[] currFile = jGitHelper.extract(filePath, commitId);
            int index = filePath.lastIndexOf("/");
            Path currFilePath = Paths.get(meta.getOutputDir() + "/curr/" + parentCommitId + "/" + filePath);
            if(!currFilePath.toFile().exists()) {
                int index2 = currFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String currDir = currFilePath.toFile().getAbsolutePath().substring(0,index2);
                File curr = new File(currDir);
                if(!curr.exists()){
                    curr.mkdirs();
                }
            }
            Files.write(currFilePath, currFile);
            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setCurr_file_path("curr/" + parentCommitId + "/" + filePath);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/"+parentCommitId+"/Diff"+fileName+".json");
            }
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDeletedCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            byte[] prevFile = jGitHelper.extract(filePath, parentCommitId);
            int index = filePath.lastIndexOf("/");
            Path prevFilePath = Paths.get(meta.getOutputDir() + "/prev/" + parentCommitId + "/" + filePath);
            if(!prevFilePath.toFile().exists()) {
                int index2 = prevFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String prevDir = prevFilePath.toFile().getAbsolutePath().substring(0,index2);
                File prev = new File(prevDir);
                if(!prev.exists()){
                    prev.mkdirs();
                }
            }
            Files.write(prevFilePath, prevFile);
            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setPrev_file_path("prev/" + parentCommitId + "/" + filePath);
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/"+parentCommitId+"/Diff"+fileName+".json");
            }
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


package main;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffAPI;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.fileutil.PathUtil;
import edu.fdu.se.git.IHandleCommit;
import edu.fdu.se.git.JGitHelper;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.Meta;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/23.
 *
 */
public class CmdTest implements IHandleCommit {

    JGitHelper jGitHelper;
    Meta meta;

    public static void main(String args[]){
        CmdTest cmdTest = new CmdTest();
        cmdTest.run();

    }

    public void run(){
        String repo = "D:/Workspace/CLDiff-2018-7-12/spring-framework/.git";
        String commitId = "3c1adf7f6af0dff9bda74f40dabe8cf428a62003";
        String outputDir = "C:/Users/huangkaifeng/Desktop/output";
        jGitHelper = new JGitHelper(repo);
        initMeta(repo,commitId,outputDir);
        jGitHelper.analyzeOneCommit(this,commitId);
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



    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String commitId){
        int cnt = 0;
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            meta.addParentCommit(parentCommitId);
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (CLDiffCore.isFilter(file)) {
                        continue;
                    }
                    setCommitFile(cnt,parentCommitId,commitId,file);
                    cnt+=1;
                }
            }
        }
    }

    public void setCommitFile(int cnt,String parentCommitId,String commitId,String filePath){
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
            commitFile.setDiffPath(meta.getOutputDir() + "/gen/"+parentCommitId+"/Diff"+fileName+".json");
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


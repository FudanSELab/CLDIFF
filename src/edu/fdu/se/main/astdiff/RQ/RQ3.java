package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.fileutil.FileWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class RQ3 extends RQ{

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    class FilePairData{
        public FilePairData(byte[] prevv,byte[] currr,String prevPathh,String currPathh,String fileNamee) {
            prev = prevv;
            curr = currr;
            prevPath = prevPathh;
            currPath = currPathh;
            fileName = fileNamee;
        }
        private byte[] prev;
        private byte[] curr;
        private String prevPath;
        private String currPath;
        private String fileName;
    }
    private String repoPath;
    private String projName;
    private String commitId;

    private List<FilePairData> filePairDatas = new ArrayList<>();

    public static void main(String args[]){
        RQ3 rq = new RQ3();
        String projName = "spring-framework";
        rq.repoPath = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\"+projName+"\\.git";
        rq.commitId = "ace6bd2418cba892f793e9e3666ac02a541074c7";
        rq.outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ3\\"+projName;
        rq.jGitHelper = new JGitHelper(rq.repoPath);
        rq.jGitHelper.analyzeOneCommit(rq,rq.commitId);
        rq.generateDiffMinerOutput();

    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String currCommitId){
        String absolutePath = this.outputDir+"\\"+currCommitId;
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                JSONArray ja = new JSONArray();
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if(this.isFilter(file)){
                        continue;
                    }
                    ja.put(file);
                }
                JSONObject jo = new JSONObject();
                jo.put("files",ja);
                File f = new File(absolutePath);
                f.mkdirs();
                FileWriter.writeInAll(absolutePath+"/meta.json",jo.toString());
                for (String file : modifiedFile) {
                    if(this.isFilter(file)){
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());

                    FilePairData fp = new FilePairData(prevFile,currFile,file,file,fileName);
                    filePairDatas.add(fp);
                    String subPathCurr = absolutePath+"\\curr\\";
                    String subPathPrev = absolutePath+"\\prev\\";
                    File f1 = new File(subPathPrev);
                    f1.mkdirs();
                    File f2 = new File(subPathCurr);
                    f2.mkdirs();
                    FileWriter.writeInAll(subPathPrev+fileName,prevFile);
                    FileWriter.writeInAll(subPathCurr+fileName,currFile);
                }
            }
        }
    }

    public void generateDiffMinerOutput(){
        String absolutePath = this.outputDir+"\\" + this.commitId;
        int i =0;
        for(FilePairData fp:filePairDatas){
            System.out.println(fp.fileName);
            runDiffMiner(fp.fileName,fp.prev,fp.curr,absolutePath);
        }
        fileChangeEntityData.clear();
    }

    public void runDiffMiner(String fileName,byte[] prev,byte[] curr,String output){
        Global.fileName = fileName;
        this.baseDiffMiner.doo(fileName,prev,curr,output);
        this.fileChangeEntityData.put(this.baseDiffMiner.mFileName,this.baseDiffMiner.changeEntityData);
    }




    public void handleCommits(Map<String, Map<String, List<String>>> mMap, String currCommitId){
        //pass
    }
}

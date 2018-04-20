package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.main.astdiff.DiffMinerTest;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/4.
 *
 */
public class RQ1 extends RQ{



    public static void main(String args[]){
        RQ1 rq1 = new RQ1();
//        rq1.allCommits();
        rq1.oneCommit();
    }

    // 1.抽取所有的commit filter掉不需要的

    public void allCommits(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\MPAndroidChart\\.git";
        outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ1";
        System.out.println("\n\n----------------------------"+repo);
        baseDiffMiner = new DiffMinerTest();
        String[] data = repo.split("\\\\");
        String projName = data[data.length-2];
        jGitHelper = new JGitHelper(repo);
        baseDiffMiner.mFileOutputLog = new FileOutputLog(outputDir,projName);
        jGitHelper.walkRepoFromBackwards(this);


    }


    // 2. Debug抽取特定的commit
    public void oneCommit(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\okhttp\\.git";
        outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ1";
        //5387e206af84b2dea77ecb1ed552b16dc1aed2c8
        //a267d7efdfe58b5727b5af22070ba3d953fe060a
        //d3455d0c9d57d522c31b5c25af83e8f2b8df12b6
        String commitID = "9f2c435756483bdfc59fecf4837e54eb9bf3d755";
        jGitHelper = new JGitHelper(repo);
        baseDiffMiner = new DiffMinerTest();
        baseDiffMiner.mFileOutputLog = new FileOutputLog(outputDir,"elasticsearch");
        jGitHelper.analyzeOneCommit(this,commitID);
    }

    public void handleCommits(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (this.isFilter(file)) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    System.out.println("CommitId: " + currCommitId);
                    System.out.println("fileName: " + fileName);
                    String dirName = parentCommitId + "-" + currCommitId;
                    baseDiffMiner.doo(fileName,prevFile,currFile,outputDir+"/"+dirName+"/"+fileName);
                }
            }
        }

    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String currCommitId) {
        baseDiffMiner.mFileOutputLog.setCommitId(currCommitId);
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (this.isFilter(file)) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    String dirName = parentCommitId + "-" +currCommitId;
                    System.out.println(fileName);
                    Global.fileName = fileName;
//                    if(fileName.equals("TopDocsCollectorContext.java"))
                    baseDiffMiner.doo(fileName,prevFile,currFile,this.outputDir+"/"+dirName+"/"+fileName);
                    this.baseDiffMiner.mFileOutputLog.writeSourceFile(prevFile,currFile,fileName);
                }
            }
        }
    }


}

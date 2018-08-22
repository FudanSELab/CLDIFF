package RQ;

import edu.fdu.se.base.common.Global;
import edu.fdu.se.base.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.cldiff.CLDiffTest;
import edu.fdu.se.git.IHandleCommit;
import edu.fdu.se.git.JGitHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/4.
 *
 */
public class RQ1 implements IHandleCommit{

    private CLDiffCore cldiff = new CLDiffCore();
    public JGitHelper jGitHelper;

    public static void main(String args[]){
        RQ1 rq1 = new RQ1();
//        rq1.allCommits();
        rq1.oneCommit();
    }

    public void allCommits(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\MPAndroidChart\\.git";
        String outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ1";

        System.out.println("\n\n----------------------------"+repo);
        cldiff = new CLDiffTest();
        String[] data = repo.split("\\\\");
        String projName = data[data.length-2];
        jGitHelper = new JGitHelper(repo);
        cldiff.mFileOutputLog = new FileOutputLog(outputDir,projName);
        jGitHelper.walkRepoFromBackwards(this);
    }


    // 2. Debug抽取特定的commit
    public void oneCommit(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\okhttp\\.git";
        String outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ1";
        String commitID = "9f2c435756483bdfc59fecf4837e54eb9bf3d755";
        jGitHelper = new JGitHelper(repo);
        cldiff = new CLDiffTest();
        cldiff.mFileOutputLog = new FileOutputLog(outputDir,"elasticsearch");
        jGitHelper.analyzeOneCommit(this,commitID);
    }

//    public void handleCommits(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
//        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
//            String parentCommitId = entry.getKey();
//            Map<String, List<String>> changedFileEntry = entry.getValue();
//            if (changedFileEntry.containsKey("modifiedFiles")) {
//                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
//                for (String file : modifiedFile) {
//                    if (CLDiffCore.isFilter(file)) {
//                        continue;
//                    }
//                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
//                    byte[] currFile = jGitHelper.extract(file, currCommitId);
//                    int index = file.lastIndexOf("/");
//                    String fileName = file.substring(index + 1, file.length());
//                    System.out.println("CommitId: " + currCommitId);
//                    System.out.println("fileName: " + fileName);
//                    String dirName = parentCommitId + "-" + currCommitId;
//                    cldiff.doo(fileName,prevFile,currFile,outputDir+"/"+dirName+"/"+fileName);
//                }
//            }
//        }
//
//    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String currCommitId) {
        cldiff.mFileOutputLog.setCommitId(currCommitId);
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (CLDiffCore.isFilter(file)) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    String dirName = parentCommitId + "-" +currCommitId;
                    System.out.println(fileName);
                    Global.fileName = fileName;

                    cldiff.doo(fileName,prevFile,currFile,cldiff.mFileOutputLog.rootPath+"/"+dirName+"/"+fileName);
//                    this.CLDiffCore.mFileOutputLog.writeSourceFile(prevFile,currFile,fileName);
                }
            }
        }
    }


}

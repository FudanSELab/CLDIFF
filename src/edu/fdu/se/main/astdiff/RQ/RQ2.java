package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.main.astdiff.BaseDiffMiner;
import edu.fdu.se.main.astdiff.DiffMinerTest;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 */
public class RQ2 extends RQ {

    private String rq2ProjPath;
    private String argType;
    public static void main(String args[]){
        Global.RQ2 = 1;
        if(!(args[0]!=null && args[1]!=null)){
            return;
        }
        RQ2 rq = new RQ2();
        String projPath = args[1];
        System.out.println("----"+projPath);
        System.out.println("----"+args[0]);
        long startTime = System.nanoTime();
        rq.argType = args[0];
        rq.totalNumber = 0;
        String[] data = projPath.split("\\\\");
        String projName = data[data.length-2];
        if("gt".equals(args[0]) || "df".equals(args[0])){
            rq.outputDir = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ2";
            rq.rq2ProjPath = projPath;
            rq.baseDiffMiner = new DiffMinerTest();
            rq.jGitHelper = new JGitHelper(rq.rq2ProjPath);
            rq.baseDiffMiner.mFileOutputLog = new FileOutputLog(rq.outputDir,projName);
            rq.jGitHelper.walkRepoFromBackwards(rq);

        }else if("lc".equals(args[0])){
            rq.jGitHelper = new JGitHelper(projPath);
            rq.jGitHelper.walkRepoFromBackwardsCountLineNumber(rq);
        }
        long endTime = System.nanoTime();
        System.out.println("----total number:" + (endTime-startTime));
        System.out.println("----total number:"+ rq.totalNumber);

    }


    private long totalNumber;


    public void handleCommits(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
        long start = System.nanoTime();
        System.out.println("----commit id:"+currCommitId);
//        this.baseDiffMiner.mFileOutputLog.setCommitId(currCommitId);
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
//                    System.out.println("CommitId: " + currCommitId);
//                    System.out.println("fileName: " + fileName);
//                    String dirName = parentCommitId + "-" + currCommitId;
                    int num=0;
                    if(this.argType.equals("gt")){
                        num = baseDiffMiner.runGumTree(new String(prevFile), new String(currFile));
                    }else if(this.argType.equals("df")){
                        baseDiffMiner.doo(fileName,prevFile,currFile,outputDir+"/"+fileName);
                        num = baseDiffMiner.changeEntityData.mad.getChangeEntityList().size();
                    }
                    totalNumber += num;
                    System.out.println("----"+fileName+" "+num);
                }
            }
        }
        long end = System.nanoTime();
        System.out.println("----one commit time:" +(end-start));
    }

    public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId){

    }
}

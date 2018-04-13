package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/4.
 *
 */
public class RQ1 extends RQ{



    public static void main(String args[]){
        RQ1 rq1 = new RQ1();
//    	long startTime = System.currentTimeMillis();    //获取开始时间
//    	System.out.println("开始时间：" + startTime + "ms");

//        allCommits();

//        long endTime = System.currentTimeMillis();    //获取结束时间
//        System.out.println("结束时间：" + endTime + "ms");
//
//    	System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

        rq1.oneCommit();
    }

    // 1.抽取所有的commit filter掉不需要的

    public void allCommits(){
        String repo = "E:/projects/RxJava/.git";
        outputDir = "";
        System.out.println("\n\n----------------------------"+repo);
        jGitHelper = new JGitHelper(repo);
        fileOutputLog = new FileOutputLog(outputDir);
        jGitHelper.walkRepoFromBackwards(this);
    }


    // 2. Debug抽取特定的commit
    public void oneCommit(){
        String repo = "E:/projects/RxJava/.git";
        outputDir = "";
        String commitID = "8a6bf14fc9a61f7c1c0016ca217be02ca86211d2";
        jGitHelper = new JGitHelper(repo);
        jGitHelper.analyzeOneCommit(this,commitID);
    }

    public void handleCommits(Map<String, Map<String, List<String>>> changedFiles,String currCommitId){
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (!file.endsWith(".java")) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    System.out.println("CommitId: " + currCommitId);
                    System.out.println("fileName: " + fileName);
                    String dirName = parentCommitId + "-" + currCommitId;

//                                diffMinerTest.runGumTree(new String(prevFile), new String(currFile));
                    if(!file.toLowerCase().contains("test")) {
                        baseDiffMiner.doo(fileName.substring(0,fileName.length()-5),prevFile,currFile,outputDir+"/"+dirName+"/"+fileName);
                        fileOutputLog.writeRQ1CommitFile(prevFile, currFile, parentCommitId + "-" + currCommitId, fileName);
                    }
                }
            }
        }

    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String currCommitId) {
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (!file.endsWith(".java")) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    String dirName = parentCommitId + "-" +currCommitId;
                    System.out.println(fileName);
                    if(!file.toLowerCase().contains("test")) {
                        if(fileName.equals("CompletableToObservable.java"))
                            baseDiffMiner.doo(fileName.substring(0,fileName.length()-5),prevFile,currFile,this.outputDir+"/"+dirName+"/"+fileName);
                    }
                }
            }
        }
    }


}

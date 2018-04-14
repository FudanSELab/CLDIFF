package edu.fdu.se.main.astdiff.RQ;

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

    private String projName;
    public static void main(String args[]){
//        generateCommitCache();
        generateDiffMinerOutput();
    }

    public static void generateDiffMinerOutput(){
        RQ3 rq = new RQ3();
        String projPath  = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ3\\MPAndroidChart";
        File f = new File(projPath);
        File[] files = f.listFiles();
        for(File commit:files){
            if(!commit.getName().equals("34e7f4497962d235d94072d1544e22a7a362ae30")){
                continue;
            }
            File[] commitFiles = commit.listFiles();
            for(File commit2:commitFiles){
                if(commit2.getName().endsWith(".json")){

                }else{
                    File[] prevCurr = commit2.listFiles();
                    File prev;
                    File curr;
                    if(prevCurr[0].getName().endsWith("curr")){
                        prev = prevCurr[1];
                        curr = prevCurr[0];
                    }else{
                        prev = prevCurr[0];
                        curr = prevCurr[1];
                    }
                    File[] prevFiles = prev.listFiles();
                    File[] currFiles = curr.listFiles();
                    Arrays.sort(prevFiles,new Comparator<File>(){
                        @Override
                        public int compare(File a,File b){
                            return a.getName().compareTo(b.getName());
                        }
                    });
                    Arrays.sort(currFiles,new Comparator<File>(){
                        @Override
                        public int compare(File a,File b){
                            return a.getName().compareTo(b.getName());
                        }
                    });
                    for(int i =0;i<prevFiles.length;i++){
                        File a = prevFiles[i];
                        File b = currFiles[i];
                        if(a.isDirectory()){
                            continue;
                        }
                        System.out.println(a.getName());
                        rq.runDiffMiner(a.getAbsolutePath(),b.getAbsolutePath(),commit2.getAbsolutePath());
                        if(a.getName().equals("YAxisRenderer.java")){
                            break;
                        }
                    }
                }
                break; //commit parent json
            }
            break;//commit
        }
    }

    public void runDiffMiner(String prev,String curr,String output){
        this.baseDiffMiner.doo(prev,curr,output);
    }

    public static void generateCommitCache(){
        RQ3 rq = new RQ3();
        Map<String,List<String>> mMap = new HashMap<>();
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\RxJava\\.git";
        List<String> repo1List = new ArrayList<>();
        repo1List.add("e6824c1ab2b649654bebabc3cedb5a15f7605141");
        repo1List.add("0f1542d7d5d2ce2e89118a5701a6d6fd7d5684b0");
        repo1List.add("2a4b18e743f0958551f4e31fdf81e618cc35f238");
        mMap.put(repo,repo1List);
        String repo2 = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\MPAndroidChart\\.git";
        List<String> repo2List = new ArrayList<>();
        repo2List.add("d5e5ec3a924248a3b1d4b977b3b7658a0f3d0a12");
        repo2List.add("34e7f4497962d235d94072d1544e22a7a362ae30");
        repo2List.add("00f36054c79938e2cb6c0276df16ae10b42a6400");
        repo2List.add("1c312e85a9c6f868f76e886386621ebd3555a7d7");
        repo2List.add("922f1a8ab354e3c9408f069bf90366aadc356e6f");
        repo2List.add("9399db9ec77560669dc598379fd88a2b0014ad8e");
        repo2List.add("cd04e50cb42e9de748138dafb8c591b3fa8f3219");
        repo2List.add("3e5d4e6e6ed206a2d1e412f06107facbff384dee");
        mMap.put(repo2,repo2List);
        String repo3 = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\spring-framework\\.git";
        List<String> repo3List = new ArrayList<>();
        repo3List.add("ea9ad4ee9bd6604fe57f73004bf375c7c4cd7be3");
        repo3List.add("6560aed1c85eef68faeb0356c34e12035a2826bf");
        repo3List.add("96da77ef759e8f9623b20d7f28adbb72dc13a946");
        repo3List.add("b1158aa913900b03155712536dee85802624e99f");
        repo3List.add("3f392e32f56993e4cf92e5c61ed227b80fa10b82");
        repo3List.add("090ab4bb6d2588283df3ff95a181c74e72306048");
        repo3List.add("d9cb44527c1f06d6055b805446a38c8817981aaf");
        repo3List.add("90309ab0b5c5cc3b406825fd3f9db730db03ad36");
        repo3List.add("4e1781ae8ce5fe3d6d1ee4607e0ec1bd831bc429");
        repo3List.add("65b17b80ba353d3c21ab392d711d347bcbcce42b");
        repo3List.add("fda9c633c4a9457300cbfcf0780683ae770b1d84");
        repo3List.add("ae942ffdb89ae103b6f9e076ec9548594317e2f9");
        repo3List.add("37679384e82554250c87918ad042e7cfb3825813");
        mMap.put(repo3,repo3List);
        rq.oneCommit(mMap);

    }

    public void oneCommit(Map<String,List<String>> commitList){
        for(Entry<String,List<String>> entry:commitList.entrySet()) {
            String repo = entry.getKey();
            String tmp = repo.substring(0,repo.length()-5);
            int index = tmp.lastIndexOf("\\");
            projName = tmp.substring(index+1);
            List<String> commitIds = entry.getValue();
            outputDir = "";
            jGitHelper = new JGitHelper(repo);
            for(String commit:commitIds) {
//                String commitID = "8a6bf14fc9a61f7c1c0016ca217be02ca86211d2";
                jGitHelper.analyzeOneCommit(this, commit);
            }
        }

    }



    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String currCommitId){
        String absolutePath = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\11-8-GumTree\\RQ3\\"+projName+"\\"+currCommitId;

        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                JSONArray ja = new JSONArray();
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");
                for (String file : modifiedFile) {
                    if (!file.endsWith(".java") || file.toLowerCase().contains("test")) {
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
                    if (!file.endsWith(".java")|| file.toLowerCase().contains("test")) {
                        continue;
                    }
                    byte[] prevFile = jGitHelper.extract(file, parentCommitId);
                    byte[] currFile = jGitHelper.extract(file, currCommitId);
                    int index = file.lastIndexOf("/");
                    String fileName = file.substring(index + 1, file.length());
                    String dirName = parentCommitId + "-" +currCommitId;
                    String subPath = absolutePath + "\\" + dirName;
                    String subPathCurr = subPath+"\\curr\\";
                    String subPathPrev = subPath+"\\prev\\";
                    File f1 = new File(subPathPrev);f1.mkdirs();
                    File f2 = new File(subPathCurr);f2.mkdirs();
                    FileWriter.writeInAll(subPathCurr+fileName,currFile);
                    FileWriter.writeInAll(subPathPrev+fileName,prevFile);
                    System.out.println(fileName);

                }
            }
        }
    }


    public void handleCommits(Map<String, Map<String, List<String>>> mMap, String currCommitId){

    }
}

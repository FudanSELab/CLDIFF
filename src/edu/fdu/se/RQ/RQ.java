package edu.fdu.se.RQ;

import edu.fdu.se.main.astdiff.DiffMinerTest;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/13.
 *
 */
abstract public class RQ {

    public JGitHelper jGitHelper;

    public DiffMinerTest baseDiffMiner;
    public String outputDir;

    abstract public void handleCommits(Map<String, Map<String, List<String>>> mMap,String currCommitId);


    abstract public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId);

    public static boolean isFilter(String filePathName){
        String name = filePathName.toLowerCase();
        if(!name.endsWith(".java")){
            return true;
        }
        if(name.contains("\\test\\")||name.contains("/test/")){
            return true;
        }
        String[] data = filePathName.split("/");
        String fileName = data[data.length-1];
        if(filePathName.endsWith("Test.java")||fileName.startsWith("Test")||filePathName.endsWith("Tests.java")){
            return true;
        }
        return false;
    }
}

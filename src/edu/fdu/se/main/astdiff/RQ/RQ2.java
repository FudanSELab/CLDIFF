package edu.fdu.se.main.astdiff.RQ;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/12.
 */
public class RQ2 extends RQ {

    public static void main(String args[]){
        RQ2 rq = new RQ2();
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\RxJava\\.git";
        rq.jGitHelper = new JGitHelper(repo);
        rq.jGitHelper.walkRepoFromBackwardsCountLineNumber(rq);
    }



    public void changedLineNumber(String commitId,int lineNumber){
        System.out.println(commitId+" "+lineNumber);
    }


    public void handleCommits(Map<String, Map<String, List<String>>> mMap,String currCommitId){

    }

    public void handleCommit(Map<String, Map<String, List<String>>> mMap,String currCommitId){

    }
}

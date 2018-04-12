package edu.fdu.se.main.astdiff.RQ;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.git.RepoConstants;

/**
 * Created by huangkaifeng on 2018/4/4.
 *
 */
public class RQ1Data {

    public static void main(String args[]){
        allCommits();
//        oneCommit();
    }

    // 1.抽取所有的commit filter掉不需要的

    public static void allCommits(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\tomcat\\.git";
        String outputDir = "D:/TomcatCompareFiles";
        JGitHelper cmd = new JGitHelper(repo);
        cmd.walkRepoFromBackwards(outputDir);
    }


    // 2. Debug抽取特定的commit
    public static void oneCommit(){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\tomcat\\.git";
        String outputDir = "D:/aaa";
        String commitID = "";
        JGitHelper cmd = new JGitHelper(repo);
        cmd.analyzeOneCommit(commitID,outputDir);
    }

}

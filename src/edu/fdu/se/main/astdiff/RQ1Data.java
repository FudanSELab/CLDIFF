package edu.fdu.se.main.astdiff;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.git.RepoConstants;

/**
 * Created by huangkaifeng on 2018/4/4.
 */
public class RQ1Data {

    public static void main(String args[]){
        String repo = "D:\\Workspace\\DiffMiner\\November-GT-Extend\\Evaluation\\tomcat";
        JGitRepositoryCommand cmd = new JGitRepositoryCommand(repo);
        cmd.walkRepoFromBackwards("D:/TomcatCompareFiles");
    }


}

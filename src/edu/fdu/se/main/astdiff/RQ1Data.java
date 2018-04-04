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
        JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.JAVA_REPO_RQ1));
        cmd.walkRepoFromBackwards();
    }


}

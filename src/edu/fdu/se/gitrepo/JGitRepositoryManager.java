package edu.fdu.se.gitrepo;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitTagCommand;

public class JGitRepositoryManager {
	
	private static JGitTagCommand baseCmd = new JGitTagCommand(
			ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH) + "/.git");
	private static JGitTagCommand blueToothCmd = new JGitTagCommand(
			ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH) + "/.git");
	
	public static JGitTagCommand getCommand(String key){
		JGitTagCommand jtc;
		switch(key){
		case "/platform/frameworks/base/":jtc =baseCmd;break;
		default:jtc=baseCmd;break;
		}
		return jtc;
	}

}

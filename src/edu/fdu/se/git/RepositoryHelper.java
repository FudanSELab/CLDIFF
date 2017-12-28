package edu.fdu.se.git;


import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.main.gitgui.RepoDataHelper;


public class RepositoryHelper {

    public static Repository openJGitCookbookRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
		try {
			repository = builder.setGitDir(new File(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git")).readEnvironment() // scan
					.findGitDir() // scan up the file system tree
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository;
    }
    
    private static RepositoryHelper repositoryhelper;
	
	public static RepositoryHelper getInstance(){
		if(repositoryhelper == null){
			repositoryhelper = new RepositoryHelper();
		}
		return repositoryhelper;
	}
	public JGitTagCommand myCmd;
	public RepositoryHelper(){
		myCmd = new JGitTagCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
	}

}

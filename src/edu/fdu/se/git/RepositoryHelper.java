package edu.fdu.se.git;


import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;


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
	
	public static RepositoryHelper getInstance1(){
		if(repositoryhelper == null){
			repositoryhelper = new RepositoryHelper(1);
		}
		return repositoryhelper;
	}
	
	public static RepositoryHelper getInstance2(){
		if(repositoryhelper == null){
			repositoryhelper = new RepositoryHelper(2);
		}
		return repositoryhelper;
	}
	public int type;
	public JGitCommand myCmd;
	public RepositoryHelper(int type){
		this.type = type;
		if(type == 1)
			myCmd = new JGitTagCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		else if(type == 2){
			myCmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
					+ RepoConstants.platform_frameworks_base_ + ".git");
		}
		
	}

}

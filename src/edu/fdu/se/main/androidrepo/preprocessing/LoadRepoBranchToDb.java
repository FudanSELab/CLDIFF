package edu.fdu.se.main.androidrepo.preprocessing;

import java.util.List;

import edu.fdu.se.bean.AndroidPlatformFrameworkProject;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidPlatformFrameworkProjectDAO;
import edu.fdu.se.git.JGitTagCommand;

public class LoadRepoBranchToDb {
	public static void main(String args[]){
//		run();
	}
	/**
	 * load to db
	 */
	public static void run(){
		List<AndroidPlatformFrameworkProject> mList = AndroidPlatformFrameworkProjectDAO.selectAll();
		for(AndroidPlatformFrameworkProject item : mList){
			String subPath = item.getProjectSubPath();
			String path = ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2) + subPath +".git";
			JGitTagCommand cmd = new JGitTagCommand(path);
			int size = cmd.listBranchesAndSaveToDb(item);
			System.out.println(subPath +": "+size);
		}
	}

}

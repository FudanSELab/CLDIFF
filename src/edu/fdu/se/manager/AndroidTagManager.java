package edu.fdu.se.manager;

import java.util.List;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.RepoConstants;

public class AndroidTagManager {
	
	
	public static AndroidTag tagEntryOfTagNameOnDefaultProj(String tagName){
		return AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
		
	}
	public static String tagShaIDOfTagNameOnDefaultProj(String tagName){
		return tagEntryOfTagNameOnDefaultProj(tagName).getTagShaId();
	}

}

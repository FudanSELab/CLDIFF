package edu.fdu.se.manager;

import java.util.List;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.RepoConstants;

public class AndroidTagManager {
	
	
	public static AndroidTag tagEntryOfTagNameOnDefaultProj(String tagName){
		List<AndroidTag> mList = AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
		if(mList.size()==1){
			return mList.get(0);
		}else{
			return null;
		}
	}
	public static String tagShaIDOfTagNameOnDefaultProj(String tagName){
		return tagEntryOfTagNameOnDefaultProj(tagName).getTagShaId();
	}

}

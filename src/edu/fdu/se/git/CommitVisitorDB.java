package edu.fdu.se.git;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.dao.AndroidRepoCommitDAO;


public class CommitVisitorDB {
	private List<AndroidRepoCommitWithBLOBs> mList;

	public void visit(RevCommit commit, Map<String, List<String>> mMap) {
		boolean flag = false;
		AndroidRepoCommitWithBLOBs dbCommit = new AndroidRepoCommitWithBLOBs();

		dbCommit.setCommitId(commit.getName());
		dbCommit.setCommitLog(commit.getShortMessage());
		dbCommit.setCommitLogFull(commit.getFullMessage());
		Long l = new Long(commit.getCommitTime());
		Date date = new Date(l);
		dbCommit.setCommitDatetime(date);
		dbCommit.setCommitTimestamp(date);
		System.out.println(commit.getName());
		for (Entry<String, List<String>> item : mMap.entrySet()) {
			// System.out.println(item.getKey());
			for (String filePath : item.getValue()) {
				if (filePath.startsWith("core/java/android")) {
					flag = true;
				}
				// System.out.println(filePath);
			}
		}
		if (flag == true) {
			dbCommit.setIssdkfile(1);
		}else{
			dbCommit.setIssdkfile(0);
		}
		if(mList == null){
			mList = new ArrayList<AndroidRepoCommitWithBLOBs>();
		}
		mList.add(dbCommit);
		if(mList.size()>1000){
			AndroidRepoCommitDAO.insertBatch(mList);
			mList.clear();
		}
	}
}

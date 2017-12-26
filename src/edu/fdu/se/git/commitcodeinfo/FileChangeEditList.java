package edu.fdu.se.git.commitcodeinfo;

import org.eclipse.jgit.diff.EditList;

public class FileChangeEditList {
	String oldFilePath;
	String newFilePath;
	EditList mEditList;
	public FileChangeEditList(String oldPath,String newPath,EditList list,String patch){
		this.oldFilePath = oldPath;
		this.newFilePath = newPath;
		this.mEditList = list;
		this.patchScript = patch;
	}
	public String getOldFilePath() {
		return oldFilePath;
	}
	public String getNewFilePath() {
		return newFilePath;
	}
	public EditList getEditList() {
		return mEditList;
	}
	String patchScript;
	public String getPatchScript() {
		return patchScript;
	}
	
	
}

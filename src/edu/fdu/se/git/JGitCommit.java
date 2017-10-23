package edu.fdu.se.git;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

public class JGitCommit {

	private RevCommit revCommit;
	private List<String> modifiedFiles = new ArrayList<String>();
	private List<String> addedFiles = new ArrayList<String>();
	private List<String> deletedFiles = new ArrayList<String>();
	private List<String> allFiles = new ArrayList<String>();
	private int version;
	
	public JGitCommit(RevCommit commit){
		revCommit=commit;
	}
	public void setAllFields(){
		
	}
	public List<String> getAllFiles() {
		return allFiles;
	}

	public void setAllFiles(List<String> allFiles) {
		this.allFiles = allFiles;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

	public List<String> getModifiedFiles() {
		return modifiedFiles;
	}
	



	public void setModifiedFiles(List<String> modifiedFiles) {
		this.modifiedFiles = modifiedFiles;
	}



	public List<String> getAddedFiles() {
		return addedFiles;
	}



	public void setAddedFiles(List<String> addedFiles) {
		this.addedFiles = addedFiles;
	}



	public List<String> getDeletedFiles() {
		return deletedFiles;
	}


	public void setDeletedFiles(List<String> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}
	
	public String getCommitter(){
		return revCommit.getCommitterIdent().getName();
	}
	public String getEmail(){
		return revCommit.getCommitterIdent().getEmailAddress();
	}
	public Date getDate(){
		return revCommit.getCommitterIdent().getWhen();
	}
	public String getMessage(){
		return revCommit.getFullMessage();
	}
	public String getSha(){
		return revCommit.getName();
	}


	@Override
	public String toString() {
		return "RepositoryCommit [committer=" + getCommitter() + ", email=" + getEmail()
				+ ", date=" + getDate().toString() + ", message=" + getMessage() + ", modifedFiles="
				+ modifiedFiles + ", addedFiles=" + addedFiles
				+ ", deletedFiles=" + deletedFiles 
				+ ",sha= " + getSha() + ", version=" + version
				+ "]";
	}


}

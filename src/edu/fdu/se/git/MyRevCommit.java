package edu.fdu.se.git;

import org.eclipse.jgit.revwalk.RevCommit;

public class MyRevCommit {
	public RevCommit rc;
	public MyRevCommit(RevCommit c){
		this.rc = c;
	}
	public RevCommit[] children;
	
	public void setChildren(RevCommit[] c){
		this.children = c;
	}

}

package edu.fdu.se.git;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.revwalk.RevCommit;

public class CommitVisitor {

	public void visit(RevCommit commit,Map<String,List<String>> mMap){
		
		boolean flag=false;
		for(Entry<String,List<String>> item:mMap.entrySet()){
//			System.out.println(item.getKey());
			for(String filePath:item.getValue()){
				if(filePath.startsWith("core/java/android")){
					flag=true;
				}
//				System.out.println(filePath);
			}
		}
		if(flag==true){
			System.out.println(commit.getName());
			System.out.println(commit.getShortMessage());
		}
	}

}

package edu.fdu.se.git;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.fileutil.FileWriter;

public class CommitVisitor extends AbstractVisitor{
	
	
	public CommitVisitor(String fileName){
		super(fileName);
	}

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
			FileWriter.writeInSegments(this.getFile(), commit.getName(), FileWriter.FILE_APPEND_AND_NOT_CLOSE);
			FileWriter.writeInSegments(this.getFile(), commit.getShortMessage(), FileWriter.FILE_APPEND_AND_NOT_CLOSE);
		}
	}


}

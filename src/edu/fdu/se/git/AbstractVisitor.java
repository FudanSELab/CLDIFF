package edu.fdu.se.git;

import java.io.File;

import edu.fdu.se.fileutil.FileWriter;

public class AbstractVisitor {
	
	public AbstractVisitor(){
		
	}
	public AbstractVisitor(String fileName){
		this.logFile = new File(fileName);
	}
	private File logFile;
	
	public File getFile(){
		return logFile;
	}
	
	public void close(){
		FileWriter.close(logFile);
	}
}

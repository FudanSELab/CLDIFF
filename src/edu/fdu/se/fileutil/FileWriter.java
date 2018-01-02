package edu.fdu.se.fileutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileWriter {

	final public static int FILE_NEW_AND_APPEND=0;
	
	final public static int FILE_APPEND_AND_NOT_CLOSE=1;
	
	final public static int FILE_APPEND_AND_CLOSE=2;
	
	final public static int FILE_WRITE_ONCE = 3;
	
	final public static int FILE_WRITE_MULTIPLE = 4;
	
	public static void writeInAll(String filePath, String content) {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void writeInAll(String filePath,byte[] content){
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  
	
	public static void writeInAll(String filePath,InputStream is){
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			int ch;
			while((ch = is.read())!= -1){
				fos.write(ch);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void close(File mFile){
		if (fileMap.containsKey(mFile)) {
			try {
				fileMap.get(mFile).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileMap.remove(mFile);
		}
	}

	private static Map<File, FileOutputStream> fileMap = new HashMap<File, FileOutputStream>();

	/**
	 * 
	 * @param mFile
	 * @param content
	 * @param flag
	 *            1:normal 2:write end
	 */
	public static void writeInSegments(File mFile, String content, int flag) {
		try {
			if(flag == FileWriter.FILE_NEW_AND_APPEND){
				if (fileMap.containsKey(mFile)) {
					fileMap.get(mFile).close();
					fileMap.remove(mFile);
				}
			}
			if (fileMap.containsKey(mFile)) {
				FileOutputStream fos = fileMap.get(mFile);
				fos.write(content.getBytes());

			} else {
				FileOutputStream fos = new FileOutputStream(mFile);
				fos.write(content.getBytes());
				fileMap.put(mFile, fos);
			}
			if(flag==FileWriter.FILE_APPEND_AND_CLOSE){
				fileMap.get(mFile).close();
				fileMap.remove(mFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

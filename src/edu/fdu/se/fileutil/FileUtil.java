package edu.fdu.se.fileutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

	final public static int FILE_NEW_AND_APPEND=0;
	
	final public static int FILE_APPEND_AND_NOT_CLOSE=1;
	
	final public static int FILE_APPEND_AND_CLOSE=2;
	
	public static void writeInAll(String filePath, String content) {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(content.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			if(flag == FileUtil.FILE_NEW_AND_APPEND){
				if (fileMap.containsKey(mFile)) {
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
			if(flag==FileUtil.FILE_APPEND_AND_CLOSE){
				fileMap.get(mFile).close();
				fileMap.remove(mFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

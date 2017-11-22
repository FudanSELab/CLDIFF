package edu.fdu.se.fileutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

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
			if (fileMap.containsKey(mFile)) {
				FileOutputStream fos = fileMap.get(mFile);
				fos.write(content.getBytes());

			} else {
				FileOutputStream fos = new FileOutputStream(mFile);
				fos.write(content.getBytes());
				fileMap.put(mFile, fos);
			}
			if(flag==2){
				fileMap.get(mFile).close();
				fileMap.remove(mFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

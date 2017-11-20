package edu.fdu.se.fileutil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static void writeInAll(String filePath,String content){
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(content.getBytes());
			fos.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
	}

}

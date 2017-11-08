package edu.fdu.se.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Script {
	public static void  main(String args[]){
		try {
			FileInputStream fis =new FileInputStream("C:/Users/huangkaifeng/Desktop/10-20_Commits/可能的问题java文件.txt");
			FileOutputStream fos = new FileOutputStream("C:/Users/huangkaifeng/Desktop/10-20_Commits/可能的问题java文件sort.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			Map<String,List<String>> mMap =new HashMap<String,List<String>>();
			while((line =  br.readLine()) != null){
				if(line.startsWith("git")){
					String[] data = line.split("--");
					if(mMap.containsKey(data[1])){
						mMap.get(data[1]).add(data[0]);
					}else{
						List<String> mList = new ArrayList<String>();
						mMap.put(data[1], mList);
						mMap.get(data[1]).add(data[0]);
					}
					
				}else{
					fos.write(line.getBytes());
					fos.write("\n".getBytes());
				}
			}
			System.out.println(mMap.size());
			for(Entry<String,List<String>> item:mMap.entrySet()){
				List<String> list = item.getValue();
				for(String s:list){
					fos.write(s.getBytes());
					fos.write(item.getKey().getBytes());
					fos.write("\n".getBytes());
				}
			}
			fos.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

package cn.edu.fudan.se.api;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.fudan.se.db.DB;
import cn.edu.fudan.se.util.JsonFileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileDbDataFlow {
	private static String SAVE_PATH = "C:/Users/yw/Desktop/test/";
	
	
	public static void extractApi(String libPath,String outputPath,String metaPath,int start,int end) {
		String whole = JsonFileUtil.readJsonFile(metaPath);
		JSONArray array = JSONArray.fromObject(whole);
		for (int a = 0; a < array.size(); a++) {
			JSONObject obj = array.getJSONObject(a);
			int index = obj.getInt("id");
			String name = obj.getString("lib");
			name = name.substring(0, name.length()-4)+"_decompile";
			if(index <= end && index>=start) {
				JarAnalyzer ja = new JarAnalyzer();		
				ja.enterSave(libPath, name, outputPath);
			}
		}
	}
	
	public static void meta(String dir) {
		int index = 0;
		JSONArray array = new JSONArray();
		File or = new File(dir);
		File[] files = or.listFiles();
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				if(fileName.endsWith(".jar")) {
					ResultSet rs = DB.query("SELECT * FROM `version_types` where `jar_package_url`= '" + fileName+"'");
					try {
						while (rs.next()) {
							int id = rs.getInt("type_id");
							if(id > 1000) {
								index++;
								JSONObject obj = new JSONObject();
								obj.put("id", index);
								//jackage_url.substring(0, jackage_url.length()-4)+"_decompile"
								obj.put("lib", file.getName());
								array.add(obj);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}				
			}								
		}
		JsonFileUtil.save("meta.txt", array);
	}
	
	public static void readJarApi() {
		for(int versionTypeId =1;versionTypeId<2;versionTypeId++) {
			File file = new File(SAVE_PATH + versionTypeId+".txt");
			if(!file.exists())
				continue;
			String whole = JsonFileUtil.readJsonFile(SAVE_PATH + versionTypeId+".txt");
			JSONArray array = JSONArray.fromObject(whole);			
				
			for (int a = 0; a < array.size(); a++) {
				JSONObject clazz = array.getJSONObject(a);
				String className = clazz.getString("name");
				String sql = "INSERT INTO api_classes(version_type_id,class_name) VALUES ("+ versionTypeId + ",\'" + className + "\')";
				DB.update(sql);
				int classId=-1;
				ResultSet rs = DB.query("select LAST_INSERT_ID()");
				try {
					if (rs.next())
						classId = rs.getInt(1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONArray apis = clazz.getJSONArray("api");
				for (int i = 0; i < apis.size(); i++) {
					JSONObject api = apis.getJSONObject(i);
					String name = api.getString("name");
					String remark = api.getString("remark");
					sql = "INSERT INTO api_interface(class_id,name,remark) VALUES ("+ classId + ",\'" + name + "\', \'"+remark+"\')";
					DB.update(sql);
				}
			}
		}		
	}
	
	public static void main(String[] args) {
//		readJarApi();
//		meta("F:/GP/lib/");
		//"F:/GP/decompile/" "C:/Users/yw/Desktop/test/" "meta.txt" 1 1
		if(args.length == 5) {
			String libPath = args[0];
			String outputPath = args[1];
			String metaPath = args[2];
			int start = Integer.parseInt(args[3]);
			int end = Integer.parseInt(args[4]);
			extractApi(libPath,outputPath,metaPath,start,end);
		}
		else
			System.out.println("Illegal args");		
	}
}

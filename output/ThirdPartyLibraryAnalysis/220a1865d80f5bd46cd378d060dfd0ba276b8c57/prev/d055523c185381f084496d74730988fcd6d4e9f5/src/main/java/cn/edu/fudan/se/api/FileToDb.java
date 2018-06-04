package cn.edu.fudan.se.api;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.fudan.se.db.DB;
import cn.edu.fudan.se.util.JsonFileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileToDb {
	private static String SAVE_PATH = "C:/Users/yw/Desktop/test/";
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
		readJarApi();
	}
}

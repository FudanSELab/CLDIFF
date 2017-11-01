package edu.fdu.se.config;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties; 

public class ProjectProperties {
	public Map<String,String> kvMap;
	private ProjectProperties(){
		kvMap = new HashMap<String,String>();
		Properties prop = new Properties();     
        try{
            InputStream in = new BufferedInputStream (new FileInputStream("resource/config.properties"));
            prop.load(in);  
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
//                System.out.println(key+":"+prop.getProperty(key));
                kvMap.put(key, prop.getProperty(key));
            }
            in.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	private static ProjectProperties instance;
	public static ProjectProperties getInstance(){
		if(instance==null){
			instance = new ProjectProperties();
		}
		return instance;
	}
	public static void main(String args[]){
		ProjectProperties ins = ProjectProperties.getInstance();
		System.out.println("a");
	}

}

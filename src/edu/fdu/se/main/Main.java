package edu.fdu.se.main;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidSDKJavaFile;

public class Main {
	

	public static void main(String args[]){
		String repoPath="D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base";
		String resource = "mybatis-config.xml";
        InputStream is = Main.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        String statement = "edu.fdu.se.mapping.androidSDKJavaFileMapper.getAndroidSDKJavaFile";//Ó³ÉäsqlµÄ±êÊ¶×Ö·û´®
        AndroidSDKJavaFile user = session.selectOne(statement, 1);
        System.out.println(user);
	}
}
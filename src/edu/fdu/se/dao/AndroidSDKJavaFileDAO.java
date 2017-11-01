package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.mapper.AndroidSDKJavaFileMapper;


public class AndroidSDKJavaFileDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidSDKJavaFileMapper androidsdkjavafilemapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidsdkjavafilemapper = sqlSession.getMapper(AndroidSDKJavaFileMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AndroidSDKJavaFile androidsdkjavafile) {
		androidsdkjavafilemapper.insert(androidsdkjavafile);
		sqlSession.commit();
	}

	public static void insertBatch(List<AndroidSDKJavaFile> androidsdkjavafile) {
		androidsdkjavafilemapper.insertBatch(androidsdkjavafile);
		sqlSession.commit();
	}
}

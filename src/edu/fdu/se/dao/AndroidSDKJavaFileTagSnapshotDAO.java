package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidSDKJavaFileTagSnapshot;
import edu.fdu.se.mapper.AndroidSDKJavaFileTagSnapshotMapper;

public class AndroidSDKJavaFileTagSnapshotDAO {
	
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidSDKJavaFileTagSnapshotMapper androidSDKJavaFileTagSnapshotMapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidSDKJavaFileTagSnapshotMapper = sqlSession.getMapper(AndroidSDKJavaFileTagSnapshotMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AndroidSDKJavaFileTagSnapshot a) {
		androidSDKJavaFileTagSnapshotMapper.insert(a);
		sqlSession.commit();
	}
	
	public static List<AndroidSDKJavaFileTagSnapshot> selectAll(){
		return androidSDKJavaFileTagSnapshotMapper.selectAll();
	}
	


}

package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidPlatformFrameworkProject;
import edu.fdu.se.mapper.AndroidPlatformFrameworkProjectMapper;

public class AndroidPlatformFrameworkProjectDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidPlatformFrameworkProjectMapper androidsplatformframeworkprojectmapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidsplatformframeworkprojectmapper = sqlSession.getMapper(AndroidPlatformFrameworkProjectMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static List<AndroidPlatformFrameworkProject> selectAll(){
		return androidsplatformframeworkprojectmapper.selectAll();
	}
}

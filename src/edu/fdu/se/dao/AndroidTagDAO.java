package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.mapper.AndroidTagMapper;

public class AndroidTagDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidTagMapper androidtagmapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidtagmapper = sqlSession.getMapper(AndroidTagMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AndroidTag a) {
		androidtagmapper.insert(a);
		sqlSession.commit();
	}




	public static List<AndroidTag> selectAll(){
		return androidtagmapper.selectAll();
	}
	public static List<AndroidTag> selectTags(String tagShortName){
		return androidtagmapper.selectTags(tagShortName);
	}
	public static AndroidTag selectTagByShortNameAndProjName(String tagShortName,String projectName){
		return androidtagmapper.selectTagByShortNameAndProjName(tagShortName, projectName);
	}
	public static List<AndroidTag> selectTagAll(String projName){
		return androidtagmapper.selectTagAll(projName);
	}
}

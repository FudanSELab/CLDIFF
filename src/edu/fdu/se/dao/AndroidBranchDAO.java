package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.mapper.AndroidBranchMapper;

public class AndroidBranchDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidBranchMapper androidbranchmapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidbranchmapper = sqlSession.getMapper(AndroidBranchMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AndroidBranch a) {
		androidbranchmapper.insert(a);
		sqlSession.commit();
	}




	public static List<AndroidBranch> selectAll(){
		return androidbranchmapper.selectAll();
	}
	public static List<AndroidBranch> selectBranches(String branchShortName){
		return androidbranchmapper.selectBranchByShortName(branchShortName);
	}
	public static AndroidBranch selectBranchByShortNameAndProjName(String branchShortName,String projectName){
		return androidbranchmapper.selectBranchByShortNameAndProjName(branchShortName, projectName);
	}
	public static List<AndroidBranch> selectBranchesAll(String projName){
		return androidbranchmapper.selectBranchAll(projName);
	}
}

package edu.fdu.se.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.mapper.AndroidRepoCommitMapper;

public class AndroidRepoCommitDAO {
	private static SqlSessionFactory sessionFactory;
	private static Reader reader;
	private static SqlSession sqlSession;
	private static AndroidRepoCommitMapper androidrepocommitmapper;

	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sessionFactory.openSession();
			androidrepocommitmapper = sqlSession.getMapper(AndroidRepoCommitMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AndroidRepoCommitWithBLOBs androidRepoCommit) {
		androidrepocommitmapper.insert(androidRepoCommit);
		sqlSession.commit();
	}

	public static void insertBatch(List<AndroidRepoCommitWithBLOBs> androidRepoCommit) {
		androidrepocommitmapper.insertBatch(androidRepoCommit);
		sqlSession.commit();
	}
	public static AndroidRepoCommitWithBLOBs selectWithCommitSHA(String commitId){
		return androidrepocommitmapper.selectByCommitId(commitId);
	}
	
	public static int countByCommitId(String commitId){
		return androidrepocommitmapper.countByCommitId(commitId);
	}
	public static void insertIntoBranchTable(AndroidRepoCommitWithBLOBs item){
		androidrepocommitmapper.insertIntoBranchTable(item);
		sqlSession.commit();
	}
	public static List<AndroidRepoCommit> selectBranchHeads(Date commitTime){
		return androidrepocommitmapper.selectFromBranchHeads(commitTime);
	}
	public static List<AndroidRepoCommit> selectAll(){
		return androidrepocommitmapper.selectAll();
	}
}

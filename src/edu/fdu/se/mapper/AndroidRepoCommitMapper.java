package edu.fdu.se.mapper;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitExample;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;

public interface AndroidRepoCommitMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	long countByExample(AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int deleteByExample(AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int insert(AndroidRepoCommitWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int insertSelective(AndroidRepoCommitWithBLOBs record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	List<AndroidRepoCommitWithBLOBs> selectByExampleWithBLOBs(AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	List<AndroidRepoCommit> selectByExample(AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int updateByExampleSelective(@Param("record") AndroidRepoCommitWithBLOBs record,
			@Param("example") AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int updateByExampleWithBLOBs(@Param("record") AndroidRepoCommitWithBLOBs record,
			@Param("example") AndroidRepoCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_repo_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int updateByExample(@Param("record") AndroidRepoCommit record, @Param("example") AndroidRepoCommitExample example);

	int insertBatch(List<AndroidRepoCommitWithBLOBs> androidRepoCommit);
	
	AndroidRepoCommitWithBLOBs selectByCommitId(@Param("commitId") String commitId);
	
	int countByCommitId(@Param("commitId")String commitId);
	
	int insertIntoBranchTable(AndroidRepoCommitWithBLOBs item);
	
	List<AndroidRepoCommit> selectFromBranchHeads(@Param("commitTime")Date commitTime);
	
	List<AndroidRepoCommit> selectAll();
}
package edu.fdu.se.mapper;

import edu.fdu.se.bean.AndroidCacheCommit;
import edu.fdu.se.bean.AndroidCacheCommitExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AndroidCacheCommitMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	long countByExample(AndroidCacheCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int deleteByExample(AndroidCacheCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int insert(AndroidCacheCommit record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int insertSelective(AndroidCacheCommit record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	List<AndroidCacheCommit> selectByExample(AndroidCacheCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int updateByExampleSelective(@Param("record") AndroidCacheCommit record,
			@Param("example") AndroidCacheCommitExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	int updateByExample(@Param("record") AndroidCacheCommit record,
			@Param("example") AndroidCacheCommitExample example);

	List<AndroidCacheCommit> selectAll();
    
    List<AndroidCacheCommit> selectByKey(@Param("key")String key);
}
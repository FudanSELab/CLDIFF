package edu.fdu.se.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import edu.fdu.se.bean.AndroidSDKJavaFileTagSnapshot;

public interface AndroidSDKJavaFileTagSnapshotMapper {
	
	
	@Insert("INSERT into android_sdk_java_file_tag_snapshot (sdk_java_file_id, tag_name,is_checked) values (#{androidSDKJavaFileId,jdbcType=INTEGER},#{tagStr,jdbcType=VARCHAR},#{isChecked,jdbcType=VARCHAR})")
	int insert(AndroidSDKJavaFileTagSnapshot androidSDKJavaFileTagSnapshot);
	
	
	List<AndroidSDKJavaFileTagSnapshot> selectAll();


}

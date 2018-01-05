package edu.fdu.se.bean;

public class AndroidSDKJavaFileTagSnapshot {
	
	private Integer id;
	
	
	private AndroidSDKJavaFile androidSDKJavaFile;
	
	private String tagStr;
	
	private String isChecked;
	
	public AndroidSDKJavaFileTagSnapshot(Integer idd,AndroidSDKJavaFile asjf,String tagStr,String isChecked){
		this.id = idd;
		this.androidSDKJavaFile = asjf;
		this.tagStr = tagStr;
		this.isChecked = isChecked;
	}

	public AndroidSDKJavaFileTagSnapshot(Integer idd,Integer idd2,String tagName,String isChecked,Integer id3, String fileName, String fileFullPath, Integer sdkVersion, String subCategory,
			String subSubCategory, String subSubCategoryPath) {
		this.id = idd;
		this.androidSDKJavaFile = new AndroidSDKJavaFile(id3,fileName,fileFullPath,sdkVersion,subCategory,subSubCategory,subSubCategoryPath);
		this.tagStr = tagName;
		this.isChecked = isChecked;
	}
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AndroidSDKJavaFile getAndroidSDKJavaFile() {
		return androidSDKJavaFile;
	}

	public void setAndroidSDKJavaFile(AndroidSDKJavaFile androidSDKJavaFile) {
		this.androidSDKJavaFile = androidSDKJavaFile;
	}
	public int getAndroidSDKJavaFileId(){
		return this.androidSDKJavaFile.getId();
	}

	public String getTagStr() {
		return tagStr;
	}

	public void setTagStr(String tagStr) {
		this.tagStr = tagStr;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	
	
	
	

}

package edu.fdu.se.bean;

public class AndroidSDKJavaFileTagSnapshot {
	
	private Integer id;
	
	
	private AndroidSDKJavaFile androidSDKJavaFile;
	
	private String tagStr;
	
	private int isChecked;
	
	public AndroidSDKJavaFileTagSnapshot(Integer idd,AndroidSDKJavaFile asjf,String tagStr,int isChecked){
		this.id = idd;
		this.androidSDKJavaFile = asjf;
		this.tagStr = tagStr;
		this.isChecked = isChecked;
	}
	
	

}

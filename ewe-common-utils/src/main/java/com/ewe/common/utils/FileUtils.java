/**
 * 
 */
package com.ewe.common.utils;

import java.io.File;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * @author Yo-Lee:catreekk@sohu.com
 * @version 2014年11月25日下午2:50:04
 */
public class FileUtils {
	
	public static String File_Path = "";
//	public static final String File_Path = "/opt/tomcat-file/file";
//	public static final String File_Path = "/data/file";
	
	public static String Host_Url = "";
//	public static final String Host_Url = "http://101.95.16.50:8888/cdn";
//	public static final String Host_Url = "http://123.59.71.163:8888/cdn";
	
	public static final String File_Qrcode = "qrcode";
	public static final String File_Exsl = "exsl";
	
	static {
		ResourceBundle rb = ResourceBundle.getBundle("config/file");
		File_Path = rb.getString("file_path");
		Host_Url = rb.getString("host_url");
	}
	

	private static final Random ran = new Random(100);
	/**
	 * use now time to make random file name
	 * @author Lee-Yo
	 * @version 2014年11月25日下午2:53:23
	 * @param oldName
	 * @return
	 */
	public static String randomFileName(String oldName) {
		if(!oldName.contains("."))
			return null;
		String fileName = ran.nextInt()+System.currentTimeMillis()
				+ oldName.substring(
						oldName.lastIndexOf("."));
		return fileName;
	}
	
	public static String getFilePath(String fileTwoDir){
		String filePath = File_Path;
		if(StringUtils.isNotBlank(fileTwoDir)){
			filePath = File_Path+File.separator + fileTwoDir + File.separator+getNowDataPath();
		}
		File fileDir = new File(filePath);
		if(!fileDir.exists())
			fileDir.mkdirs();
		return filePath;
	}
	
	private static String getNowDataPath(){
		String now = DateUtils.getNowTimeString();
		String[] split = now.split(" ")[0].split("-");
		String path = split[0]+File.separator+split[1]+File.separator+split[2];
		return path;
	}
	
	public static String getFileUrl(String fileName, String fileTwoDir){
		String fileNamePath = getFilePath(fileTwoDir) + File.separator + fileName;
		return Host_Url+fileNamePath.substring(File_Path.length()).replace("\\", "/");
	}
}

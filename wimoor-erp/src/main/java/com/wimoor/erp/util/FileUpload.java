package com.wimoor.erp.util;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;


import org.apache.commons.net.ftp.FTPClient;

import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.config.IniConfig;
import com.wimoor.util.FTPServerUtil;
 

public class FileUpload {  

  public static final String FILE_PATH = "/upload/";  

  public static File getFile(String fileName) {  
      return new File(FILE_PATH, fileName);  
  }  
  public static String getPictureImage(String value) {
		if(value!=null&&!value.contains("http")&&value.contains("photo/")) {
			value=value.replace("photo/",IniConfig.photoServerUrl()+"/");
			value=value.replace("%2B","%252B");
		} 
		return value;
  }
  public static List<Map<String, Object>> covertPictureImage(List<Map<String, Object>> list) {
	   for(Map<String, Object> item:list) {
		   item.put("image",FileUpload.getPictureImage(item.get("image")));
		   item.put("location",FileUpload.getPictureImage(item.get("location")));
	   }
	   return list;
  }
  public static String getPictureImage(Object value) {
	  String result=null;
	  if(value!=null)result=value.toString();
	  return getPictureImage(result);
  }
	public static Boolean deletePicture(String path) {
		FTPServerUtil ftputil=new FTPServerUtil();
		try {
			FTPClient ftpClient = ftputil.getFTPClient();
			if(path == null) return false;
			if(path.indexOf("photo/")==0) {
				path=path.substring(6,path.length());
			}
			String[] patharray = path.split("/");
			if(patharray.length>0) {
				path=patharray[0];
				for(int i=1;i<patharray.length-1;i++) {
					path=path+"/"+patharray[i];
				}
				return ftputil.deleteFile(ftpClient, path, patharray[patharray.length-1]);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERPBizException("图片操作失败");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERPBizException("图片操作失败");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ERPBizException("图片操作失败");
		}
		
		return false;
	}
} 
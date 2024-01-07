package com.wimoor.common.mvc;


import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wimoor.common.service.impl.OSSApiService;
import com.wimoor.common.service.util.FTPServerUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUpload   {  

  final OSSApiService ossApiService;
  
  public static final String FILE_PATH = "/upload/";  
 
  @Value("${config.photo-server}")
  public String photoServer;
  @Value("${config.photo-server-url}")
  public String photoServerUrl;
  
  public   File getFile(String fileName) {  
      return new File(FILE_PATH, fileName);  
  }  
  
  public   String getPictureImage(String value) {
	 
		if(value!=null&&!value.contains("http")&&(value.contains("photo/")||value.contains( ossApiService.bucketName+"/"))) {
			value=value.replace("photo/",photoServerUrl+"/");
			value=value.replace("%2B","%252B");
			value=value.replace("+","%2B");
			value=value.replace(ossApiService.bucketName+"/", ossApiService.bucketPath+"/");
		}else if(value==null) {
	    	value=ossApiService.bucketPath+"/sys/photos/noimg.png";
	    } 
		return value;
  }
 
  
  public   List<Map<String, Object>> covertPictureImage(List<Map<String, Object>> list) {
	   for(Map<String, Object> item:list) {
		   item.put("image",getPictureImage(item.get("image")));
		   item.put("location",getPictureImage(item.get("location")));
	   }
	   return list;
  }
  
  public   String getPictureImage(Object value) {
	  String result=null;
	  if(value!=null)result=value.toString();
	  return getPictureImage(result);
  }
  
	public   Boolean deletePicture(String path) {
		FTPServerUtil ftputil=new FTPServerUtil(photoServer);
		try {
		
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
				return ftputil.deleteFile( path, patharray[patharray.length-1]);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("图片操作失败");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("图片操作失败");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("图片操作失败");
		}
		
		return false;
	}


} 
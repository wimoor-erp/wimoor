package com.wimoor.common.mvc;


import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.wimoor.common.service.impl.StorageService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUpload   {  

  final StorageService storageService;
  
  public static final String FILE_PATH = "/upload/";  
 
  @Value("${config.photo-server}")
  public String photoServer;
  @Value("${config.photo-server-url}")
  public String photoServerUrl;
  
  public   File getFile(String fileName) {  
      return new File(FILE_PATH, fileName);  
  }  
  
  public   String getPictureImage(String value) {
		if(value!=null&&!value.contains("http")&&(value.contains("photo/")||value.contains( storageService.getBucketName()+"/"))) {
			value=value.replace("photo/",photoServerUrl+"/");
			value=value.replace("%2B","%252B");
			value=value.replace("+","%2B");
			value=value.replace(storageService.getBucketName()+"/", storageService.getBucketPath()+"/");
		}else if(value==null) {
	    	value=storageService.getBucketPath()+"/sys/photos/noimg.png";
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
   


} 
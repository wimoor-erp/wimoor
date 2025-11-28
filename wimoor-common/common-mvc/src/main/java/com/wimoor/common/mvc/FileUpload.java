package com.wimoor.common.mvc;


import java.io.File;
import java.util.List;
import java.util.Map;

import com.wimoor.common.service.impl.StorageLargeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.wimoor.common.service.impl.StorageService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUpload   {  

  final StorageService storageService;
  final StorageLargeService storageLargeService;
  
  public static final String FILE_PATH = "/upload/";  
 
  @Value("${config.photo-server}")
  public String photoServer;
  @Value("${config.photo-server-url}")
  public String photoServerUrl;
  
  public   File getFile(String fileName) {  
      return new File(FILE_PATH, fileName);  
  }  
  
  public   String getPictureImage(String value) {
	    if(value!=null){
			value=value.replace("%2B","%252B");
			value=value.replace("+","%2B");
			if(value.contains("http")){ return value; }
			else if(value.contains("photo/")){
				value=value.replace("photo/",photoServerUrl+"/");
			}else if(value.contains( storageService.getBucketName()+"/")){
				value=value.replace(storageService.getBucketName()+"/", storageService.getBucketPath()+"/");
			}else if(storageLargeService.getBucketName()!=null&&value.contains( storageLargeService.getBucketName()+"/")){
				value=value.replace(storageLargeService.getBucketName()+"/", storageLargeService.getBucketPath()+"/");
			}
		}else{
			value=storageService.getBucketPath()+"/sys/photos/noimg.png";
		}
		return value;
  }

  public String pathToLocation(String path){
	  if(path!=null){
		  if(path.startsWith("https://")){
			  if(path.startsWith(photoServerUrl+"/")){
				  path=path.replace(photoServerUrl+"/", photoServer+"/");
			  }
			  if(path.startsWith(storageService.getBucketPath()+"/")){
				  path=path.replace(storageService.getBucketPath()+"/", storageService.getBucketName()+"/");
			  }
			  if(path.startsWith(storageLargeService.getBucketPath()+"/")){
				  path=path.replace(storageLargeService.getBucketPath()+"/", storageLargeService.getBucketName()+"/");
			  }

		  }

	  }
	  return path;
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
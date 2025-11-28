package com.wimoor.common.service.impl;

import com.wimoor.common.pojo.entity.StorageType;
import com.wimoor.common.service.ObjectHandler;
import com.wimoor.common.service.util.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Map;

@Component
public class StorageLargeService {

    
    @Resource
	OSSLargeApiUtil oSSLargeApiUtil;
    @Resource
	FTPLargeServerUtil ftpLargeServerUtil;
    @Resource
	MinIOLargeApiUtil minIOLargeApiUtil;


	/**
	 * 上传文件
	 * @param objectName
	 * @param stream 可以是已下方式 
	 * InputStream inputStream = new URL(url).openStream();
	 * ByteArrayInputStream inputStream=new ByteArrayInputStream(content.getBytes())
	 * InputStream inputStream = new FileInputStream(filePath);
	 * @throws Exception 
	 * 
	 */
	 public  Boolean putObject(String bucketName,String objectName,InputStream stream)  {
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
		 if(oSSLargeApiUtil.isRun()) {
			 oSSLargeApiUtil.putObject( bucketName, objectName, stream);
	         return true;
		 }else   if(minIOLargeApiUtil.isRun()){
			 minIOLargeApiUtil.putObject(bucketName, objectName, stream);
	         return true;
		 }else {
			 try {
				 ftpLargeServerUtil.uploadFileOther(bucketName, objectName, stream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return true;
		 }
		
	}

	public void removeObject(String bucketName, String objectName)  {
		// TODO Auto-generated method stub
		 if(oSSLargeApiUtil.isRun()) {
			 oSSLargeApiUtil.removeObject(bucketName, objectName);
		 }else  if(minIOLargeApiUtil.isRun()){
			 minIOLargeApiUtil.removeObject( bucketName, objectName);
		 }else {
			 try {
				 ftpLargeServerUtil.deleteFile(bucketName, objectName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}

	public void getObject(String bucketName, String objectName, ObjectHandler handler, Map<String,Object> param)  {
		// TODO Auto-generated method stub
		if(oSSLargeApiUtil.isRun()) {
			oSSLargeApiUtil.getObject(bucketName, objectName,handler,param);
		}else  if(minIOLargeApiUtil.isRun()){
			minIOLargeApiUtil.getObject(bucketName, objectName,handler,param);
		}else {
			ftpLargeServerUtil.getObject(bucketName, objectName,handler,param);
		}
	}


	public String getBucketName() {
		 if(oSSLargeApiUtil.isRun()) {
			return oSSLargeApiUtil.getBucketName();
		 }else  if(minIOLargeApiUtil.isRun()){
			return minIOLargeApiUtil.getBucketName( );
		 }else {
			return ftpLargeServerUtil.getBucketName();
		 }
		 
	}



	public String getBucketPath() {
		if(oSSLargeApiUtil.isRun()) {
			return oSSLargeApiUtil.getBucketPath();
		 }else  if(minIOLargeApiUtil.isRun()){
			return minIOLargeApiUtil.getBucketPath( );
		 }else {
			return ftpLargeServerUtil.getBucketPath();
		 }
	}


	/**
	 * 上传文件
	 * @param objectName
	 * @param stream 可以是已下方式
	 * InputStream inputStream = new URL(url).openStream();
	 * ByteArrayInputStream inputStream=new ByteArrayInputStream(content.getBytes())
	 * InputStream inputStream = new FileInputStream(filePath);
	 * @throws Exception
	 *
	 */
	public  Boolean putObject(String bucketName, String objectName, InputStream stream, StorageType storageType)  {
		// 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
		if(storageType.equals(StorageType.OSS)) {
			oSSLargeApiUtil.putObject( bucketName, objectName, stream);
			return true;
		}else   if(storageType.equals(StorageType.MinIO)){
			minIOLargeApiUtil.putObject(bucketName, objectName, stream);
			return true;
		}else {
			try {
				ftpLargeServerUtil.uploadFileOther(bucketName, objectName, stream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

	}

	public void removeObject(String bucketName, String objectName, StorageType storageType)  {
		// TODO Auto-generated method stub
		if(storageType.equals(StorageType.OSS)) {
			oSSLargeApiUtil.removeObject(bucketName, objectName);
		}else  if(storageType.equals(StorageType.MinIO)){
			minIOLargeApiUtil.removeObject( bucketName, objectName);
		}else {
			try {
				ftpLargeServerUtil.deleteFile(bucketName, objectName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




	public String getBucketName(StorageType storageType) {
		if(storageType.equals(StorageType.OSS)) {
			return oSSLargeApiUtil.getBucketName();
		}else  if(storageType.equals(StorageType.MinIO)){
			return minIOLargeApiUtil.getBucketName( );
		}else {
			return ftpLargeServerUtil.getBucketName();
		}

	}



	public String getBucketPath(StorageType storageType) {
		if(storageType.equals(StorageType.OSS)) {
			return oSSLargeApiUtil.getBucketPath();
		}else  if(storageType.equals(StorageType.MinIO)){
			return minIOLargeApiUtil.getBucketPath( );
		}else {
			return ftpLargeServerUtil.getBucketPath();
		}
	}


	public void getObject(String bucketName, String objectName, ObjectHandler handler, Map<String,Object> param, StorageType storageType)  {
		// TODO Auto-generated method stub
		if(storageType.equals(StorageType.OSS)) {
			oSSLargeApiUtil.getObject(bucketName, objectName,handler,param);
		}else  if(storageType.equals(StorageType.MinIO)){
			minIOLargeApiUtil.getObject(bucketName, objectName,handler,param);
		}else {
			ftpLargeServerUtil.getObject(bucketName, objectName,handler,param);
		}


	}
	  
}

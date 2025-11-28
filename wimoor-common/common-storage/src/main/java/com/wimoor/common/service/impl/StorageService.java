package com.wimoor.common.service.impl;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import com.wimoor.common.pojo.entity.StorageType;
import com.wimoor.common.service.ObjectHandler;
import org.springframework.stereotype.Component;

import com.wimoor.common.service.util.FTPServerUtil;
import com.wimoor.common.service.util.MinIOApiUtil;
import com.wimoor.common.service.util.OSSApiUtil;

@Component
public class StorageService {

    
    @Resource
    OSSApiUtil oSSApiUtil;
    @Resource
    FTPServerUtil ftpServerUtil;
    @Resource
    MinIOApiUtil minIOApiUtil;


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
		 if(oSSApiUtil.isRun()) {
			 oSSApiUtil.putObject( bucketName, objectName, stream);
	         return true;
		 }else   if(minIOApiUtil.isRun()){
			 minIOApiUtil.putObject(bucketName, objectName, stream);
	         return true;
		 }else {
			 try {
				 ftpServerUtil.uploadFileOther(bucketName, objectName, stream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return true;
		 }
		
	}

	public void removeObject(String bucketName, String objectName)  {
		// TODO Auto-generated method stub
		 if(oSSApiUtil.isRun()) {
			 oSSApiUtil.removeObject(bucketName, objectName);
		 }else  if(minIOApiUtil.isRun()){
			 minIOApiUtil.removeObject( bucketName, objectName);
		 }else {
			 try {
				 ftpServerUtil.deleteFile(bucketName, objectName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}

	public void getObject(String bucketName, String objectName, ObjectHandler handler, Map<String,Object> param)  {
		// TODO Auto-generated method stub
		if(oSSApiUtil.isRun()) {
			oSSApiUtil.getObject(bucketName, objectName,handler,param);
		}else  if(minIOApiUtil.isRun()){
			minIOApiUtil.getObject(bucketName, objectName,handler,param);
		}else {
			ftpServerUtil.getObject(bucketName, objectName,handler,param);
		}
	}


	public String getBucketName() {
		 if(oSSApiUtil.isRun()) {
			return oSSApiUtil.getBucketName();
		 }else  if(minIOApiUtil.isRun()){
			return minIOApiUtil.getBucketName( );
		 }else {
			return ftpServerUtil.getBucketName();
		 }
		 
	}



	public String getBucketPath() {
		if(oSSApiUtil.isRun()) {
			return oSSApiUtil.getBucketPath();
		 }else  if(minIOApiUtil.isRun()){
			return minIOApiUtil.getBucketPath( );
		 }else {
			return ftpServerUtil.getBucketPath();
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
			oSSApiUtil.putObject( bucketName, objectName, stream);
			return true;
		}else   if(storageType.equals(StorageType.MinIO)){
			minIOApiUtil.putObject(bucketName, objectName, stream);
			return true;
		}else {
			try {
				ftpServerUtil.uploadFileOther(bucketName, objectName, stream);
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
			oSSApiUtil.removeObject(bucketName, objectName);
		}else  if(storageType.equals(StorageType.MinIO)){
			minIOApiUtil.removeObject( bucketName, objectName);
		}else {
			try {
				ftpServerUtil.deleteFile(bucketName, objectName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




	public String getBucketName(StorageType storageType) {
		if(storageType.equals(StorageType.OSS)) {
			return oSSApiUtil.getBucketName();
		}else  if(storageType.equals(StorageType.MinIO)){
			return minIOApiUtil.getBucketName( );
		}else {
			return ftpServerUtil.getBucketName();
		}

	}



	public String getBucketPath(StorageType storageType) {
		if(storageType.equals(StorageType.OSS)) {
			return oSSApiUtil.getBucketPath();
		}else  if(storageType.equals(StorageType.MinIO)){
			return minIOApiUtil.getBucketPath( );
		}else {
			return ftpServerUtil.getBucketPath();
		}
	}


	public void getObject(String bucketName, String objectName, ObjectHandler handler, Map<String,Object> param, StorageType storageType)  {
		// TODO Auto-generated method stub
		if(storageType.equals(StorageType.OSS)) {
			oSSApiUtil.getObject(bucketName, objectName,handler,param);
		}else  if(storageType.equals(StorageType.MinIO)){
			minIOApiUtil.getObject(bucketName, objectName,handler,param);
		}else {
			ftpServerUtil.getObject(bucketName, objectName,handler,param);
		}


	}
	  
}

package com.wimoor.common.service.util;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.aliyun.oss.OSSException;
import com.wimoor.common.service.ObjectHandler;
import io.minio.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOApiUtil  {
	    @Setter
	    private String minio_endpoint;
	    
	    @Setter
	    private String accessKeyId;
	    
	    @Setter
	    private String accessKeySecret;
	    
	    @Setter
	    public  String bucketName;
	    
	    @Setter
	    public  String bucketPath;

	    public boolean isRun() {
	    	if(StrUtil.isNotBlank(minio_endpoint)&&StrUtil.isNotBlank(accessKeyId)&&StrUtil.isNotBlank(accessKeySecret)) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    } 
    
	public    MinioClient  getClient() {
		  MinioClient client = new MinioClient.Builder()
                .endpoint(minio_endpoint)
                .credentials(accessKeyId, accessKeySecret)
                .build();
		return client;
	}
	
   public   void createBucketIfAbsent(String bucketName) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException {
	   MinioClient client=getClient();     
	   BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
	                .bucket(bucketName)
	                .build();
	        if (!client.bucketExists(bucketExistsArgs)) {
	            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
	            client.makeBucket(makeBucketArgs);
	        }
	    }

	  
    public   boolean putObject(String bucketName, String objectName, InputStream inputStream)  {
        try {
			 ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			 byte[] buffer = new byte[4096];
			 int bytesRead;
			 while ((bytesRead = inputStream.read(buffer)) != -1) {
				 outputstream.write(buffer, 0, bytesRead);
			}
			outputstream.flush();
			int length = outputstream.size();
			inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputstream).toByteArray());
			MinioClient client=getClient();
			createBucketIfAbsent(bucketName);
			PutObjectArgs putObjectArgs = PutObjectArgs.builder()
		                .bucket(bucketName)
		                .object(objectName)
		                .stream(inputStream, length, -1)
		                .build();
		        client.putObject(putObjectArgs);
		        return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return false;
    }

    public   void removeObject(String bucketName, String objectName) {
    	MinioClient client=getClient();   
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        try {
			client.removeObject(removeObjectArgs);
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public  void getObject(String bucketName, String objectName, ObjectHandler handler, Map<String,Object> param) {
		// 填写Bucket名称，例如examplebucket。
		MinioClient client=getClient();
		try {
			// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
			GetObjectArgs getObjectArgs = GetObjectArgs.builder()
					.bucket(bucketName)
					.object(objectName)
					.build();
			InputStream inputStream = client.getObject(getObjectArgs);

			handler.treatReader(inputStream,param);
			// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
			inputStream.close();

		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message:" + oe.getErrorMessage());
			System.out.println("Error Code:" + oe.getErrorCode());
			System.out.println("Request ID:" + oe.getRequestId());
			System.out.println("Host ID:" + oe.getHostId());
			oe.printStackTrace();
		} catch (Throwable ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			ce.printStackTrace();
			System.out.println("Error Message:" + ce.getMessage());
		}
	}


	public String getBucketName() {
		return bucketName;
	}



	public String getBucketPath() {
		return bucketPath;
	}
}

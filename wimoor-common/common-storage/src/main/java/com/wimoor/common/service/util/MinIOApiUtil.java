package com.wimoor.common.service.util;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.aliyun.oss.ClientException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.Setter;

import static com.aliyun.oss.common.utils.IOUtils.checkFile;
import static com.aliyun.oss.common.utils.LogUtils.getLog;

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
    
	public String getBucketName() {
		return bucketName;
	}



	public String getBucketPath() {
		return bucketPath;
	}
}

package com.wimoor.common.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.wimoor.common.service.OSSObjectHandler;

import cn.hutool.core.lang.Assert;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "aliyun")
public class OSSApiService implements InitializingBean {

	
    @Setter
    private String oss_endpoint;
    
    @Setter
    private String accessKeyId;
    
    @Setter
    private String accessKeySecret;
    
    @Setter
    public  String bucketName;
    
    @Setter
    public  String bucketPath;
    
    
    
    @Override
    public void afterPropertiesSet() {
        Assert.notBlank(oss_endpoint, "aliyun endpoint 为空");
        Assert.notBlank(accessKeyId, "aliyun accessKeyId为空");
        Assert.notBlank(accessKeySecret, "aliyun accessKeySecret为空");
    }
    
 

	private  OSS getOssClient() {
		// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
		// 创建ClientConfiguration实例，您可以根据实际情况修改默认参数。
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
		// 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
		conf.setSupportCname(true);
		OSS ossClient = new OSSClientBuilder().build(oss_endpoint, accessKeyId, accessKeySecret, conf);
		return ossClient;
	}
	
	   void testPutObject() {
		 String content = "Hello OSS";
	     // 创建OSSClient实例。
		 String objectName = "amz/logo/exampleobject.txt";

		 putObject(null,objectName,new ByteArrayInputStream(content.getBytes()));
	   
	}

	/**
	 * 上传文件
	 * @param objectName
	 * @param stream 可以是已下方式 
	 * InputStream inputStream = new URL(url).openStream();
	 * ByteArrayInputStream inputStream=new ByteArrayInputStream(content.getBytes())
	 * InputStream inputStream = new FileInputStream(filePath);
	 * 
	 */
	 public  Boolean putObject(String bucketName,String objectName,InputStream stream){
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        OSS ossClient = getOssClient();
        try {
            ossClient.putObject(bucketName, objectName,stream );
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();	// 关闭OSSClient。
            }
        }
        return true;
	}
	 
	 public   void getObject(String objectName,OSSObjectHandler handler,Map<String,Object> param) {
	        // 填写Bucket名称，例如examplebucket。
	        String bucketName = "wimoor-file";
	        // 创建OSSClient实例。
	        OSS ossClient = getOssClient();
	        try {
	            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
	            OSSObject ossObject = ossClient.getObject(bucketName, objectName);              

	            // 读取文件内容。
	            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
	            
	            handler.treatReader(reader,param);
	            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
	            reader.close();
	            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。            
	            ossObject.close();
	       
	        } catch (OSSException oe) {
	            System.out.println("Caught an OSSException, which means your request made it to OSS, "
	                    + "but was rejected with an error response for some reason.");
	            System.out.println("Error Message:" + oe.getErrorMessage());
	            System.out.println("Error Code:" + oe.getErrorCode());
	            System.out.println("Request ID:" + oe.getRequestId());
	            System.out.println("Host ID:" + oe.getHostId());
	        } catch (Throwable ce) {
	            System.out.println("Caught an ClientException, which means the client encountered "
	                    + "a serious internal problem while trying to communicate with OSS, "
	                    + "such as not being able to access the network.");
	            System.out.println("Error Message:" + ce.getMessage());
	        } finally {
	            if (ossClient != null) {
	                ossClient.shutdown();
	            }
	        }
	    }



	public void removeObject(String bucketName, String objectName) {
		// TODO Auto-generated method stub
        OSS ossClient = getOssClient();
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
	}



	public String getBucketName() {
		return bucketName;
	}



	public String getBucketPath() {
		return bucketPath;
	}
	  
}

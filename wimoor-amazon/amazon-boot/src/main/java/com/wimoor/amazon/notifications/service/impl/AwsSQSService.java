package com.wimoor.amazon.notifications.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.wimoor.amazon.notifications.service.IAwsSQSMessageAdaperService;
import cn.hutool.core.lang.Assert;

@Service("awsSQSService")
public class AwsSQSService  implements InitializingBean {
	@Value("${aws.sqsStandPath}")
	String queueUrl ;
	
	@Value("${aws.accessKeyId}")
	String accessKey ;
	
	@Value("${aws.secretKey}")
	String secretKey ;
	
	@Value("${spring.profiles.active}")
	String profiles;
	
	public String getProfiles() {
		return profiles;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		    Assert.notBlank(queueUrl, "aws.sqsStandPath 为空");
	        Assert.notBlank(accessKey, "aws.accessKeyId为空");
	        Assert.notBlank(secretKey, "aws.secretKey为空");
	}
	
	@Autowired
	IAwsSQSMessageAdaperService iAwsSQSMessageAdaperService;
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	AmazonSQS sqs=null;
	
	public void startSQS() {
		if(sqs==null) {
			AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
			//DefaultAWSCredentialsProviderChain credentialsProvider = DefaultAWSCredentialsProviderChain.getInstance();
			try {
				provider.getCredentials();
			} catch (Exception e) {
				// 处理消息
				e.printStackTrace();
			}
			sqs = AmazonSQSClientBuilder.standard().withCredentials(provider).withRegion(Regions.US_WEST_2).build();
		} 
	}
	
	public void stopTask() {
		if(sqs!=null) {
			sqs.shutdown();
		}
		isrun=false;
	}
	boolean isrun=true;
	public void runTask() {
		// 请求消息
		if(!"prod".equals(profiles)) {
			return;
		}
		startSQS();
		if(sqs==null) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isrun) {
				    try {
							ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
							receiveMessageRequest.setMaxNumberOfMessages(10);//亚马逊队列限制最大值十个
							List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
							// 消息队列为空则推出
							if (!messages.isEmpty()) {
								boolean hasMessage = false;
								for (Message message : messages) {
									// 处理消息
									hasMessage = true;
						            Runnable runnable = new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											boolean result = iAwsSQSMessageAdaperService.handlerMessage(message.getBody());
											// 清除消息
											if(result==true) {
												String messageReceiptHandle = message.getReceiptHandle();
												sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
											}
										}};
									
									threadPoolTaskExecutor.execute(runnable);
								}
							
								if (hasMessage == false) {
									Thread.sleep(20000);
								}
							} else {
								Thread.sleep(60000);
							}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	
	}





}

package com.wimoor.amazon.notifications.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.wimoor.amazon.notifications.service.IAwsSQSService;

@Service("awsSQSService")
public class AwsSQSServiceImpl implements IAwsSQSService {
	public final static String queueUrl = "https://sqs.us-west-2.amazonaws.com/868506290209/wimoorsqs";
	//@Resource
	//ISQSMwsMessageHandlerService productFollowHandlerService;
	private void handlerMessage(String body) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + body;
		//System.out.println(xml);
		handlerXml(xml);
	}

	public void receiveMessage() {
		DefaultAWSCredentialsProviderChain credentialsProvider = DefaultAWSCredentialsProviderChain.getInstance();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			// 处理消息
			e.printStackTrace();
		}
		final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.US_WEST_2).build();
		// 请求消息
		while (true) {
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
			receiveMessageRequest.setMaxNumberOfMessages(10);//亚马逊队列限制最大值十个
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			// 消息队列为空则推出
			if (!messages.isEmpty()) {
				boolean hasMessage = false;
				for (Message message : messages) {
					// 处理消息
					hasMessage = true;
					handlerMessage(message.getBody());
					// 清除消息
					String messageReceiptHandle = message.getReceiptHandle();
					sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
//					if (TomcatStatus.getInstance().isShutdown()) {
//						sqs.shutdown();
//						return;
//					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (hasMessage == false) {
					sqs.shutdown();
					return;
				}
			} else {
				sqs.shutdown();
				return;
			}
		}
	}

	private void handlerXml(String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource input = null;
		ByteArrayInputStream bytea = null;
		try {
			builder = factory.newDocumentBuilder();
			bytea = new ByteArrayInputStream(xml.getBytes("utf-8"));
			input = new InputSource(bytea);
			Document doc = builder.parse(input);
			//productFollowHandlerService.handlerDoc(doc);
			// getStringXPath(doc,
			// "//Notification/NotificationPayload/AnyOfferChangedNotification/OfferChangeTrigger/ASIN");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bytea != null)
				try {
					bytea.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	String getStringXPath(Document doc, String path) throws XPathExpressionException {
		XPathFactory pathFactory = XPathFactory.newInstance();
		// 使用XPathFactory工厂创建 XPath 对象
		XPath xpath = pathFactory.newXPath();
		// 使用XPath对象编译XPath表达式
		XPathExpression pathExpression = xpath.compile(path);
		// 计算 XPath 表达式得到结果
		Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
		// 节点集node-set转化为NodeList
		// 将结果强制转化成 DOM NodeList
		org.w3c.dom.NodeList nodes = (NodeList) result;
		return (nodes.item(0).getTextContent());
	}

}

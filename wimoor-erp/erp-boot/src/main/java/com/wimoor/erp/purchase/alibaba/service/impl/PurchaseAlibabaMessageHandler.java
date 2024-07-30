package com.wimoor.erp.purchase.alibaba.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.tuna.client.api.MessageProcessException;
import com.alibaba.tuna.client.websocket.TunaWebSocketClient;
import com.alibaba.tuna.client.websocket.WebSocketMessage;
import com.alibaba.tuna.client.websocket.WebSocketMessageHandler;
import com.alibaba.tuna.client.websocket.WebSocketMessageType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryLogistics;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "alibaba")
@RequiredArgsConstructor
public class PurchaseAlibabaMessageHandler {
	 
	    TunaWebSocketClient client =null;
	    @Setter
	    String appKey;
	    @Setter
	    String appSecret;
	    
		final IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
		final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
		final IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
		
	    @PostConstruct
	    public  void messageOpenAction() {
		    /*
		     * 开放平台1688环境
		     */
		    String url = "ws://message.1688.com/websocket";
 
		    /**
		     * 您客户端设置的接收线程池大小 默认为虚拟机内核数*40 用户可以自行修改
		     */
		    // int threadNum = 160;    
		    
		    /**
		     * 1. 创建 Client，如果不传入threadNum参数的话，client将使用默认线程数
		     */
		      client = new TunaWebSocketClient(appKey, appSecret, url);
		    /**
		     * 2. 创建 消息处理 Handler
		     */
		    
		    WebSocketMessageHandler tunaMessageHandler = new WebSocketMessageHandler() {
		        /**
		         * 消费消息。
		         * 如果抛异常或返回 false，表明消费失败，如未达重试次数上限，开放平台将会择机重发消息
		         */
		        public boolean onMessage(WebSocketMessage message) throws MessageProcessException {
		            boolean success = true;
		            /**
		             * 服务端推送的消息分为2种，
		             * 业务数据：SERVER_PUSH
		             * 系统消息：SYSTEM，如 appKey 与 secret 不匹配等，一般可忽略
		             */
		            if(WebSocketMessageType.SERVER_PUSH.name().equals(message.getType())) {
		                try {
		                	JSONObject json = GeneralUtil.getJsonObject(message.getContent());
		                	json=json.getJSONObject("message");
		                	String ftype = json.getString("type");
		                	if("LOGISTICS_MAIL_NO_CHANGE".equals(ftype)) {
		                		//物流单修改信息
		                		String memberid=json.getString("userInfo");
		                		PurchaseAlibabaAuth auth=purchaseAlibabaAuthService.selectBymemberId(memberid);
		                		if(auth!=null) {
		                			String authid = auth.getId();
		                			JSONObject data = json.getJSONObject("data");
		                			JSONObject model = data.getJSONObject("MailNoChangeModel");
		                			JSONObject order = model.getJSONObject("orderLogsItems");
		                			String oldmailNo=model.getString("oldMailNo");
		                			String newMailNo=model.getString("newMailNo");
	                				if(order!=null) {
	                					String orderid = order.getString("orderId");
	                					PurchaseFormEntryAlibabaInfo info=purchaseFormEntryAlibabaInfoService.selectByOrderAndAuth(authid,orderid);
	                					if(info!=null) {
	                						String entryid = info.getEntryid();
	                						QueryWrapper<PurchaseFormEntryLogistics> queryWrapper=new QueryWrapper<PurchaseFormEntryLogistics>();
	                						queryWrapper.eq("entryid", entryid);
	                						queryWrapper.eq("logisticsid", oldmailNo);
											List<PurchaseFormEntryLogistics> loglist = purchaseFormEntryLogisticsMapper.selectList(queryWrapper);
											if(loglist!=null && loglist.size()>0) {
												PurchaseFormEntryLogistics logitem = loglist.get(0);
												logitem.setRefreshtime(new Date());
												logitem.setLogisticsid(newMailNo);
												purchaseFormEntryLogisticsMapper.updateById(logitem);
											}else {
												PurchaseFormEntryLogistics item = new PurchaseFormEntryLogistics();
												item.setEntryid(entryid);
												item.setLogisticsid(newMailNo);
												item.setRefreshtime(new Date());
												purchaseFormEntryLogisticsMapper.insert(item);
											}
	                					}
	                				}
		                			 
		                		}
		                	}else if("LOGISTICS_BUYER_VIEW_TRACE".equals(ftype)) {
		                		//物流单跟踪信息
		                		String memberid=json.getString("userInfo");
		                		PurchaseAlibabaAuth auth=purchaseAlibabaAuthService.selectBymemberId(memberid);
		                		if(auth!=null) {
		                			String authid = auth.getId();
		                			JSONObject data = json.getJSONObject("data");
		                			JSONObject model = data.getJSONObject("OrderLogisticsTracingModel");
		                			JSONArray orderArray = model.getJSONArray("orderLogsItems");
		                			String mailNo=model.getString("mailNo");
		                			if(orderArray!=null && orderArray.size()>0) {
		                				JSONObject order = orderArray.getJSONObject(0);
		                				if(order!=null) {
		                					String orderid = order.getString("orderId");
		                					PurchaseFormEntryAlibabaInfo info=purchaseFormEntryAlibabaInfoService.selectByOrderAndAuth(authid,orderid);
		                					if(info!=null) {
		                						String entryid = info.getEntryid();
		                						QueryWrapper<PurchaseFormEntryLogistics> queryWrapper=new QueryWrapper<PurchaseFormEntryLogistics>();
		                						queryWrapper.eq("entryid", entryid);
		                						queryWrapper.eq("logisticsid", mailNo);
												List<PurchaseFormEntryLogistics> loglist = purchaseFormEntryLogisticsMapper.selectList(queryWrapper);
												if(loglist!=null && loglist.size()>0) {
													PurchaseFormEntryLogistics logitem = loglist.get(0);
													logitem.setRefreshtime(new Date());
													purchaseFormEntryLogisticsMapper.updateById(logitem);
												}else {
													PurchaseFormEntryLogistics item = new PurchaseFormEntryLogistics();
													item.setEntryid(entryid);
													item.setLogisticsid(mailNo);
													item.setRefreshtime(new Date());
												}
		                					}
		                				}
		                			}
		                		}
		                	}
		                } catch (Exception e) {
		                    success = false;
		                }
		            }
		            return success;
		        }
		    };
		    client.setTunaMessageHandler(tunaMessageHandler);
		    /**
		     * 3. 启动 Client
		     */
		    //client.connect();
 
		}
 
	    
	    @PreDestroy
	    public  void messageCloseAction() {
	        if(client!=null&&client.isConnect()) {
	        	 client.shutdown();
	        }
		   
		}
 

}

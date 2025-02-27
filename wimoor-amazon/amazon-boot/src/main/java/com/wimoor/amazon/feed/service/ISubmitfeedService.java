package com.wimoor.amazon.feed.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.amazon.spapi.model.feeds.CreateFeedDocumentResponse;
import com.amazon.spapi.model.feeds.CreateFeedResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.feed.pojo.entity.AmazonFeedStatus;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

 
public interface ISubmitfeedService extends IRunAmazonService {
	public Submitfeed  GetFeedSubmissionRequest(String key)throws BizException;
	public AmazonFeedStatus getFeedStatus(String feedstatus) ;
	public AmzSubmitFeedQueue SubmitFeedQueue(ByteArrayOutputStream content,String name, final AmazonAuthority amazonAuthority, String feedType, UserInfo user, String feedoptions);
	public Map<String, Object> selectByFeedTypeAndFileName(String authorityid, String marketplaceid, String feedtype, String shipmentid);
	public AmzSubmitFeedQueue HandlerFeedQueue(ByteArrayOutputStream content, String name, final AmazonAuthority amazonAuthority, String feedType, UserInfo user) ;
	public AmzSubmitFeedQueue selectByPrimaryKey(String id) ;
	public void saveFeed(Submitfeed fd);
	public void handlerCreateFeed(CreateFeedResponse res,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace);
	public void handlerCreateFeedDocument(CreateFeedDocumentResponse response,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace);
	void downloadFeedFile(ServletOutputStream outputStream, String queueid); 
	void callSubmitFeed(AmazonAuthority amazonAuthority, Marketplace marketplace, AmzSubmitFeedQueue queue) ;
 
}

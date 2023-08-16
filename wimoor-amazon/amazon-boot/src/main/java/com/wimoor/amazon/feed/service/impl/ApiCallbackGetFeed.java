package com.wimoor.amazon.feed.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.feeds.Feed;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.util.AmzDateUtils;

public class ApiCallbackGetFeed implements ApiCallback<Feed>{
	ISubmitfeedService submitfeedService;
	AmazonAuthority auth;
	AmzSubmitFeedQueue queue;
	public ApiCallbackGetFeed(ISubmitfeedService submitfeedService, AmazonAuthority auth, AmzSubmitFeedQueue queue) {
		// TODO Auto-generated constructor stub
		this.submitfeedService=submitfeedService;
		this.auth=auth;
		this.queue=queue;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

	@Override
	public void onSuccess(Feed result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		Feed feed = result;
		Submitfeed fd=new Submitfeed();
		fd.setOpttime(new Date());
		fd.setFeedSubmissionid(feed.getFeedId());
		fd.setFeedProcessingStatus(feed.getProcessingStatus().getValue());
		fd.setCompletedProcessiongDate(AmzDateUtils.getUTCToDate(feed.getProcessingEndTime()));
		fd.setSubmittedDate(AmzDateUtils.getUTCToDate(feed.getProcessingStartTime()));
		fd.setFeedType(feed.getFeedType());
		fd.setMarketplaceid(queue.getMarketplaceid());
		fd.setShopid(queue.getShopid());
		fd.setOperator(queue.getOperator());
		fd.setSellerid(auth.getSellerid());
		fd.setQueueid(queue.getId());
		submitfeedService.saveFeed(fd);
	}

	@Override
	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
		// TODO Auto-generated method stub
		
	}
}

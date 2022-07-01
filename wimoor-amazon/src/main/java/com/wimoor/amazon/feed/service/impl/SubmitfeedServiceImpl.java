package com.wimoor.amazon.feed.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.FeedsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.feeds.CreateFeedDocumentResponse;
import com.amazon.spapi.model.feeds.CreateFeedDocumentSpecification;
import com.amazon.spapi.model.feeds.CreateFeedResponse;
import com.amazon.spapi.model.feeds.CreateFeedSpecification;
import com.amazon.spapi.model.feeds.Feed;
import com.amazon.spapi.model.feeds.FeedOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.feed.mapper.AmazonFeedStatusMapper;
import com.wimoor.amazon.feed.mapper.AmzSubmitFeedQueueMapper;
import com.wimoor.amazon.feed.mapper.SubmitfeedMapper;
import com.wimoor.amazon.feed.pojo.entity.AmazonFeedStatus;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;


 

@Service("submitfeedService")
public class SubmitfeedServiceImpl implements ISubmitfeedService {

	@Resource
	IMarketplaceService marketplaceService;

	@Resource
	IAmazonAuthorityService amazonAuthorityService;

	@Resource
	SubmitfeedMapper submitfeedMapper;

	@Resource
	AmzSubmitFeedQueueMapper amzSubmitfeedQueueMapper;

	@Resource
	AmazonFeedStatusMapper amazonFeedStatusMapper;
	
	@Autowired
	ApiBuildService apiBuildService;
	
	@Resource
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
 
	public AmzSubmitFeedQueue SubmitFeedQueue(ByteArrayOutputStream content, String name, final AmazonAuthority amazonAuthority, String feedType, UserInfo user,String feedoptions) {
		if (amazonAuthority.getMarketPlace() == null) {
			return null;
		}
		Marketplace marketplace = amazonAuthority.getMarketPlace();
		AmzSubmitFeedQueue queue = new AmzSubmitFeedQueue();
		queue.getId();
		queue.setShopid(amazonAuthority.getShopId());
		queue.setFilename(name);
		queue.setAmazonauthid(amazonAuthority.getId());
		queue.setMarketplaceid(marketplace.getMarketplaceid());
		queue.setFeedType(feedType);
		queue.setContent(content.toByteArray());
		queue.setFeedoptions(feedoptions);
		queue.setOperator(user.getId());
		queue.setCreatetime(new Date());
		amzSubmitfeedQueueMapper.insert(queue);
		return queue;
	}

	public AmzSubmitFeedQueue HandlerFeedQueue(ByteArrayOutputStream content, String name, final AmazonAuthority amazonAuthority, String feedType, UserInfo user) {
		if (amazonAuthority.getMarketPlace() == null) {
			return null;
		}
		Marketplace marketplace = amazonAuthority.getMarketPlace();
		AmzSubmitFeedQueue queue = new AmzSubmitFeedQueue();
		queue.getId();
		queue.setShopid(user.getCompanyid());
		queue.setFilename(name);
		queue.setAmazonauthid(amazonAuthority.getId());
		queue.setMarketplaceid(marketplace.getMarketplaceid());
		queue.setFeedType(feedType);
		queue.setContent(content.toByteArray());
		queue.setOperator(user.getId());
		queue.setProcessDate(new Date());
		queue.setCreatetime(new Date());
		amzSubmitfeedQueueMapper.insert(queue);
		try {
			callSubmitFeed(amazonAuthority,marketplace,queue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(queue.getSubmitfeedid()==null) {
				amzSubmitfeedQueueMapper.deleteById(queue.getId());
			}
		   throw new BizException("提交失败，请稍等5分钟后重新提交");
		}
		if(queue.getSubmitfeedid()==null) {
			if(queue.getSubmitfeedid()==null) {
				amzSubmitfeedQueueMapper.deleteById(queue.getId());
			}
		   throw new BizException("提交失败，请稍等5分钟后重新提交");
		}
		return queue;
	}
	 
 

	private void callSubmitFeed(AmazonAuthority amazonAuthority, Marketplace marketplace, AmzSubmitFeedQueue queue) {
		// TODO Auto-generated method stub
		 FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		 CreateFeedDocumentSpecification body =new CreateFeedDocumentSpecification();
		 String contentType = String.format("text/xml; charset=%s", StandardCharsets.UTF_8);
		 body.setContentType(contentType);
		 try {
			  CreateFeedDocumentResponse response = api.createFeedDocument(body);
			 
			String docid=response.getFeedDocumentId();
			String url =response.getUrl();
			byte[] content = queue.getContent();
			String mlog = upload(content, url);
			if(mlog==null) {
				CreateFeedSpecification docbody=new CreateFeedSpecification();
				List<String> marketlist=new ArrayList<String>();
				marketlist.add(marketplace.getMarketplaceid());
				docbody.setMarketplaceIds(marketlist);
				docbody.setInputFeedDocumentId(docid);
				docbody.setFeedType(queue.getFeedType());
				String options = queue.getFeedoptions();
				if(StrUtil.isBlank(options)) {
                    String[] optarray = options.split(",");
                    FeedOptions option=new FeedOptions();
                    for(String opt:optarray) {
                        String[] field = opt.split("=");              	
                    	option.put(field[0], field[1]);
                    }
                    docbody.setFeedOptions(option);
				}
				 CreateFeedResponse res = api.createFeed(docbody);
				 queue.setSubmitfeedid(res.getFeedId());
				 queue.setProcessDate(new Date());
				 amzSubmitfeedQueueMapper.updateById(queue);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void handlerCreateFeed(CreateFeedResponse res,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace) {
		 queue.setSubmitfeedid(res.getFeedId());
		 queue.setProcessDate(new Date());
		 amzSubmitfeedQueueMapper.updateById(queue);
	}
	
	public void handlerCreateFeedDocument(CreateFeedDocumentResponse response,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace) {
		 FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		String docid=response.getFeedDocumentId();
		String url =response.getUrl();
		byte[] content = queue.getContent();
		String mlog = upload(content, url);
		if(mlog==null) {
			CreateFeedSpecification docbody=new CreateFeedSpecification();
			List<String> marketlist=new ArrayList<String>();
			marketlist.add(marketplace.getMarketplaceid());
			docbody.setMarketplaceIds(marketlist);
			docbody.setInputFeedDocumentId(docid);
			docbody.setFeedType(queue.getFeedType());
			String options = queue.getFeedoptions();
			if(StrUtil.isBlank(options)) {
                String[] optarray = options.split(",");
                FeedOptions option=new FeedOptions();
                for(String opt:optarray) {
                    String[] field = opt.split("=");              	
                	option.put(field[0], field[1]);
                }
                docbody.setFeedOptions(option);
			}
			 ApiCallback<CreateFeedResponse>  callback=new ApiCallbackCreateFeed(this,amazonAuthority,marketplace,queue);
			 try {
				api.createFeedAsync(docbody,callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	 
	}
	public void callSubmitFeedAsync(AmazonAuthority amazonAuthority, Marketplace marketplace, AmzSubmitFeedQueue queue) {
		// TODO Auto-generated method stub
		 FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		 CreateFeedDocumentSpecification body =new CreateFeedDocumentSpecification();
		 String contentType = String.format("text/xml; charset=%s", StandardCharsets.UTF_8);
		 body.setContentType(contentType);
		 ApiCallback<CreateFeedDocumentResponse> callback=new ApiCallbackCreateFeedDocument(this,amazonAuthority,marketplace,queue);
		 try {
			api.createFeedDocumentAsync(body,callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String upload(byte[] source, String url) {
		    OkHttpClient client = new OkHttpClient();
		    String contentType = String.format("text/xml; charset=%s", StandardCharsets.UTF_8);
		    String mlog=null;
		    try {
			      Request request = new Request.Builder().url(url).put(RequestBody.create(MediaType.parse(contentType), source)).build();
			      Response response = client.newCall(request).execute();
			      if (!response.isSuccessful()) {
			    	  mlog=String.format("Call to upload document failed with response code: %d and message: %s",response.code(), response.message());
			      }
		    } catch (IOException e) {
		          mlog=e.getMessage();
		    }
	        return mlog;
	  }
	
	public AmazonFeedStatus getFeedStatus(String feedstatus) {
		return amazonFeedStatusMapper.selectById(feedstatus);
	}

	public Map<String, Object> selectByFeedTypeAndFileName(String authorityid, String marketplaceid, String feedtype,
			String shipmentid) {
		return amzSubmitfeedQueueMapper.selectByFeedTypeAndFileName(authorityid,marketplaceid,feedtype,shipmentid);
	}

	public     AmzSubmitFeedQueue selectByPrimaryKey(String id) {
		return amzSubmitfeedQueueMapper.selectById(id);
	}

	@Override
	public void GetFeedSubmissionResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Submitfeed GetFeedSubmissionRequest(String queueid) throws BizException {
		// TODO Auto-generated method stub
		AmzSubmitFeedQueue queue = amzSubmitfeedQueueMapper.selectById(queueid);
		String feedid = queue.getSubmitfeedid();
		queue.getAmazonauthid();
		AmazonAuthority auth = amazonAuthorityService.getById(queue.getAmazonauthid());
		FeedsApi api = apiBuildService.getFeedApi(auth);
	 
		try {
			Feed feed = api.getFeed(feedid);
			feed.getCreatedTime();
			Submitfeed fd=new Submitfeed();
			fd.setOpttime(new Date());
			fd.setFeedSubmissionid(feed.getFeedId());
			fd.setFeedProcessingStatus(feed.getProcessingStatus().getValue());
			fd.setCompletedProcessiongDate(AmzDateUtils.getDate(feed.getProcessingEndTime()));
			fd.setSubmittedDate(AmzDateUtils.getDate(feed.getProcessingStartTime()));
			fd.setFeedType(feed.getFeedType());
			fd.setMarketplaceid(queue.getMarketplaceid());
			fd.setShopid(queue.getShopid());
			fd.setOperator(queue.getOperator());
			fd.setSellerid(auth.getSellerid());
			fd.setQueueid(queue.getId());
			submitfeedMapper.insert(fd);
			return fd;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void GetFeedSubmissionRequestAsync(AmazonAuthority auth ,AmzSubmitFeedQueue queue)   {
		// TODO Auto-generated method stub
		FeedsApi api = apiBuildService.getFeedApi(auth);
	    ApiCallback<Feed> callback = new ApiCallbackGetFeed(this,auth,queue) ;
		try {
			api.getFeedAsync(queue.getSubmitfeedid(),callback);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void GetFeedSubmissionTask(List<AmazonAuthority> list, Marketplace market) {
		// TODO Auto-generated method stub
		for(AmazonAuthority auth:list) {
			List<AmzSubmitFeedQueue> queuelist = amzSubmitfeedQueueMapper.findByMarket(auth.getShopId(), auth.getId(), market.getMarketplaceid());
			for(AmzSubmitFeedQueue queue:queuelist) {
				GetFeedSubmissionRequestAsync(auth,queue);
			}
		}
	}

	@Override
	public void saveFeed(Submitfeed fd) {
		// TODO Auto-generated method stub
		Submitfeed oldone = submitfeedMapper.selectById(fd.getFeedSubmissionid());
		if(oldone!=null) {
			submitfeedMapper.updateById(fd);
		}else {
			submitfeedMapper.insert(fd);
		}
	}

	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		for(Marketplace market:marketlist) {
			Calendar c=Calendar.getInstance();
			c.add(Calendar.HOUR, -24);
			List<AmzSubmitFeedQueue> list = amzSubmitfeedQueueMapper.selectList(new LambdaQueryWrapper<AmzSubmitFeedQueue>()
					.eq(AmzSubmitFeedQueue::getAmazonauthid, amazonAuthority.getId())
					.eq(AmzSubmitFeedQueue::getMarketplaceid,market.getMarketplaceid())
					.gt(AmzSubmitFeedQueue::getCreatetime,c.getTime())
					.isNull(AmzSubmitFeedQueue::getProcessDate));
			for(AmzSubmitFeedQueue queue :list) {
				callSubmitFeedAsync(amazonAuthority,market,queue);
			}
		}
	}
}

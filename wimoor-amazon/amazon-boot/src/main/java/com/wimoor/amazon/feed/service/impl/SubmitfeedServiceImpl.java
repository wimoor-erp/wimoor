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
import javax.servlet.ServletOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.api.FeedsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.feeds.CreateFeedDocumentResponse;
import com.amazon.spapi.model.feeds.CreateFeedDocumentSpecification;
import com.amazon.spapi.model.feeds.CreateFeedResponse;
import com.amazon.spapi.model.feeds.CreateFeedSpecification;
import com.amazon.spapi.model.feeds.Feed;
import com.amazon.spapi.model.feeds.FeedOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.wimoor.amazon.notifications.service.IAwsSQSMessageHandlerService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;


 

@Service("submitfeedService")
public class SubmitfeedServiceImpl implements ISubmitfeedService,IAwsSQSMessageHandlerService {

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
		QueryWrapper<AmzSubmitFeedQueue> queryWrapper=new QueryWrapper<AmzSubmitFeedQueue>();
		queryWrapper.eq("shopid", amazonAuthority.getShopId());
		queryWrapper.eq("amazonAuthId", amazonAuthority.getId());
		queryWrapper.eq("marketplaceid", marketplace.getMarketplaceid());
		queryWrapper.eq("feed_type",feedType);
		queryWrapper.eq("filename", name);
		queryWrapper.isNull("process_date");
	    List<AmzSubmitFeedQueue> oldList = amzSubmitfeedQueueMapper.selectList(queryWrapper);
	    if(oldList!=null&&oldList.size()>0) {
	    	throw new BizException("存在正在提交中的任务，请在系统处理完后提交");
	    }
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
		Map<String,Object> map = selectByFeedTypeAndFileName(amazonAuthority.getId(), marketplace.getMarketplaceid(),feedType,name);
		  //判断状态，上一条请求正在处理中，请稍后再提交;如果请求已经完成，可再次提交。
		  if(map==null ||map.get("feedstatus")==null
				||"Error".equals(map.get("feedstatus").toString())
				||"_CANCELLED_".equals(map.get("feedstatus").toString())
				||"_DONE_".equals(map.get("feedstatus").toString())
				||"DONE".equals(map.get("feedstatus").toString())
				||"CANCELLED".equals(map.get("feedstatus").toString())){
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
				}catch(BizException e) {
					if(queue.getSubmitfeedid()==null) {
						amzSubmitfeedQueueMapper.deleteById(queue.getId());
					}
					throw e;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(queue.getSubmitfeedid()==null) {
						amzSubmitfeedQueueMapper.deleteById(queue.getId());
					}
				   throw new BizException("提交失败，请稍等5分钟后重新提交");
				}
				 
				if(queue.getSubmitfeedid()==null) {
					amzSubmitfeedQueueMapper.deleteById(queue.getId());
					throw new BizException("提交失败，请稍等5分钟后重新提交");
				}
	  } else {
		throw new BizException("请求正在处理中，请稍后再试！");
	  }
	  return queue;
	}
	 
 
	private String getContentType(String type) {
	   	 if(type.equals("UPLOAD_VAT_INVOICE")) {
			return  String.format("application/pdf; charset=%s", StandardCharsets.UTF_8);
		 }else if(type.equals("POST_FLAT_FILE_FROM_EXCEL_FBA_CREAT")){
			 return String.format("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=%s", StandardCharsets.UTF_8);
		 }else {
			 return String.format("text/xml; charset=%s", StandardCharsets.UTF_8);
		 }
    }
	
	private FeedOptions getFeedOptions(AmzSubmitFeedQueue queue) {
		String options = queue.getFeedoptions();
		if(StrUtil.isNotBlank(options)) {
            String[] optarray = options.split(";");
            FeedOptions option=new FeedOptions();
            if(optarray.length>0) {
            	 for(String opt:optarray) {
                     String[] field = opt.split("=");              	
                 	 option.put(field[0], field[1]);
                 }
            	 return option;
            } 
		}
		return null;
	}
	public void callSubmitFeed(AmazonAuthority amazonAuthority, Marketplace marketplace, AmzSubmitFeedQueue queue) {
		// TODO Auto-generated method stub
		 FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		 CreateFeedDocumentSpecification body =new CreateFeedDocumentSpecification();
		 String contentType =getContentType(queue.getFeedType());
		 body.setContentType(contentType);
		 try {
			  CreateFeedDocumentResponse response = api.createFeedDocument(body);
			String docid=response.getFeedDocumentId();
			String url =response.getUrl();
			byte[] content = queue.getContent();
			String mlog = upload(content, url,queue);
			if(mlog==null) {
				CreateFeedSpecification docbody=new CreateFeedSpecification();
				List<String> marketlist=new ArrayList<String>();
				marketlist.add(marketplace.getMarketplaceid());
				docbody.setMarketplaceIds(marketlist);
				docbody.setInputFeedDocumentId(docid);
				docbody.setFeedType(queue.getFeedType());
				FeedOptions option=getFeedOptions(queue);
				if(option!=null) {
					docbody.setFeedOptions(option);
				}
				 CreateFeedResponse res = api.createFeed(docbody);
				 queue.setSubmitfeedid(res.getFeedId());
				 queue.setProcessDate(new Date());
				 amzSubmitfeedQueueMapper.updateById(queue);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			if(e!=null&&e.getResponseBody()!=null&&e.getResponseBody().contains("Invalid request parameters")) {
				throw new BizException("参数错误,请求内容中存在错误。请确认您提交的内容正确，或联系管理员");
			}
			e.printStackTrace();
		}
	
	}
	
	public void handlerCreateFeed(CreateFeedResponse res,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace) {
		 queue.setSubmitfeedid(res.getFeedId());
		 queue.setProcessDate(new Date());
		 amzSubmitfeedQueueMapper.updateById(queue);
	}
	
	public void handlerCreateFeedDocument(CreateFeedDocumentResponse response,AmazonAuthority amazonAuthority,AmzSubmitFeedQueue queue,Marketplace marketplace) {
		amazonAuthority.setUseApi("createFeed");
		FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		String docid=response.getFeedDocumentId();
		String url =response.getUrl();
		if(queue==null||queue.getContent()==null) {
			return;
		}
		byte[] content = queue.getContent();
		String mlog = upload(content, url,queue);
		if(mlog==null) {
			CreateFeedSpecification docbody=new CreateFeedSpecification();
			List<String> marketlist=new ArrayList<String>();
			marketlist.add(marketplace.getMarketplaceid());
			docbody.setMarketplaceIds(marketlist);
			docbody.setInputFeedDocumentId(docid);
			docbody.setFeedType(queue.getFeedType());
			FeedOptions option=getFeedOptions(queue);
			if(option!=null) {
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
		 amazonAuthority.setUseApi("createFeedDocument");
		 FeedsApi api = apiBuildService.getFeedApi(amazonAuthority);
		 CreateFeedDocumentSpecification body =new CreateFeedDocumentSpecification();
		 String contentType =getContentType(queue.getFeedType());
		 body.setContentType(contentType);
		 ApiCallback<CreateFeedDocumentResponse> callback=new ApiCallbackCreateFeedDocument(this,amazonAuthority,marketplace,queue);
		 try {
			 if(amazonAuthority.apiNotRateLimit()) {
				 api.createFeedDocumentAsync(body,callback);
			 }
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			amazonAuthority.setApiRateLimit(null, e);
			e.printStackTrace();
		}
	}
	
	public String upload(byte[] source, String url,AmzSubmitFeedQueue queue) {
		    OkHttpClient client = new OkHttpClient();
		    ApiBuildService.initClient(client);
		    String contentType =getContentType(queue.getFeedType());
		    String mlog=null;
		    try {
			      Request request = new Request.Builder().url(url).put(RequestBody.create(MediaType.parse(contentType), source)).build();
			      Response response = client.newCall(request).execute();
			      if (!response.isSuccessful()) {
			    	  mlog=String.format("Call to upload document failed with response code: %d and message: %s",response.code(), response.message());
			      }else {
			    	  if(response!=null&&response.toString()!=null) {
			    		  queue.setProcessLog(response.toString());
			    	  }
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
	public Submitfeed GetFeedSubmissionRequest(String queueid) throws BizException {
		// TODO Auto-generated method stub
		AmzSubmitFeedQueue queue = amzSubmitfeedQueueMapper.selectById(queueid);
		if(queue==null) {
			return null;
		}
		String feedid = queue.getSubmitfeedid();
		if(feedid==null) {
			Submitfeed fd=new Submitfeed();
			fd.setFeedProcessingStatus("waiting");
			return fd;
		}
		queue.getAmazonauthid();
		AmazonAuthority auth = amazonAuthorityService.getById(queue.getAmazonauthid());
		auth.setUseApi("getFeed");
		Submitfeed oldfeed = null;
		QueryWrapper<Submitfeed> queryWrapper=new QueryWrapper<Submitfeed>();
		queryWrapper.eq("feed_submissionid", feedid);
		queryWrapper.eq("sellerid", auth.getSellerid());
		queryWrapper.eq("feed_type", queue.getFeedType());
		if(feedid!=null) {
			oldfeed = submitfeedMapper.selectOne(queryWrapper);
		}
		if(oldfeed!=null&&oldfeed.getFeedProcessingStatus().contains("DONE")) {
			return oldfeed;
		}else if(oldfeed!=null&&GeneralUtil.distanceOfSecond(oldfeed.getOpttime(), new Date())<30) {
			return oldfeed;
		}
		FeedsApi api = apiBuildService.getFeedApi(auth);
		try {
			Feed feed = api.getFeed(feedid);
			feed.getCreatedTime();
			Submitfeed fd=new Submitfeed();
			fd.setOpttime(new Date());
			fd.setFeedSubmissionid(feed.getFeedId());
			fd.setFeedProcessingStatus(feed.getProcessingStatus().getValue());
			if(feed.getProcessingEndTime()!=null) {
				fd.setCompletedProcessiongDate(AmzDateUtils.getUTCToDate(feed.getProcessingEndTime()));
			}
            if(feed.getProcessingStartTime()!=null) {
            	fd.setSubmittedDate(AmzDateUtils.getUTCToDate(feed.getProcessingStartTime()));
            }
            if(feed.getResultFeedDocumentId()!=null) {
            	fd.setDocumentid(feed.getResultFeedDocumentId());
            }
			fd.setFeedType(feed.getFeedType());
			fd.setMarketplaceid(queue.getMarketplaceid());
			fd.setShopid(queue.getShopid());
			fd.setOperator(queue.getOperator());
			fd.setSellerid(auth.getSellerid());
			fd.setQueueid(queue.getId());
			if(oldfeed!=null) {
				submitfeedMapper.update(fd, queryWrapper);
			}else {
				if(submitfeedMapper.exists(queryWrapper)) {
					submitfeedMapper.update(fd, queryWrapper);
				}else {
					try {
						submitfeedMapper.insert(fd);
					}catch(Exception e) {
						submitfeedMapper.update(fd, queryWrapper);
					}
				}
			}
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
	public void downloadFeedFile(ServletOutputStream outputStream, String queueid) {
		AmzSubmitFeedQueue q = selectByPrimaryKey(queueid);
		try {
			outputStream.write(q.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveFeed(Submitfeed fd) {
		// TODO Auto-generated method stub
		QueryWrapper<Submitfeed> queryWrapper=new QueryWrapper<Submitfeed>();
		queryWrapper.eq("feed_submissionid", fd.getFeedSubmissionid());
		queryWrapper.eq("sellerid", fd.getSellerid());
		queryWrapper.eq("feed_type", fd.getFeedType());
		Submitfeed oldone = submitfeedMapper.selectOne(queryWrapper);
		if(oldone!=null) {
			
			submitfeedMapper.update(fd,queryWrapper);
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
			AmzSubmitFeedQueue queue = amzSubmitfeedQueueMapper.findByMarket(amazonAuthority.getShopId(),amazonAuthority.getId(),market.getMarketplaceid());
			if(queue==null||queue.getFeedType().indexOf('_')==0) {
				continue;
			}
			callSubmitFeedAsync(amazonAuthority,market,queue);
			List<AmzSubmitFeedQueue> queueList = amzSubmitfeedQueueMapper.findQueue(amazonAuthority.getId(),market.getMarketplaceid());
			for(AmzSubmitFeedQueue queueFind:queueList) {
				if(queue.getFeedType().indexOf('_')==0) {
					continue;
				}
				GetFeedSubmissionRequestAsync(amazonAuthority,queueFind);
			}
		}
	}

	@Override
	public boolean handlerMessage(JSONObject body) {
		// TODO Auto-generated method stub
		JSONObject feedProcessingFinishedNotification = body.getJSONObject("feedProcessingFinishedNotification");
		String sellerId=feedProcessingFinishedNotification.getString("sellerId");
		String feedId=feedProcessingFinishedNotification.getString("feedId");
		String feedType=feedProcessingFinishedNotification.getString("feedType");
		String processingStatus=feedProcessingFinishedNotification.getString("processingStatus");
		String resultFeedDocumentId=feedProcessingFinishedNotification.getString("resultFeedDocumentId");
		QueryWrapper<Submitfeed> queryWrapper=new QueryWrapper<Submitfeed>();
		queryWrapper.eq("feed_submissionid", feedId);
		queryWrapper.eq("sellerid", sellerId);
		queryWrapper.eq("feed_type", feedType);
		Submitfeed oldone = submitfeedMapper.selectOne(queryWrapper);
		if(oldone!=null) {
			if(!oldone.getFeedProcessingStatus().equals(processingStatus)) {
				oldone.setFeedProcessingStatus(processingStatus);
				oldone.setDocumentid(resultFeedDocumentId);
				submitfeedMapper.update(oldone,queryWrapper);
			}
		}else {
			Submitfeed fd=new Submitfeed();
			AmazonAuthority auth = amazonAuthorityService.selectBySellerId(sellerId);
			fd.setOpttime(new Date());
			fd.setFeedSubmissionid(feedId);
			fd.setFeedProcessingStatus(processingStatus);
			fd.setFeedType(feedType);
			fd.setDocumentid(resultFeedDocumentId);
			fd.setShopid(auth.getShopId());
			fd.setSellerid(sellerId);
			submitfeedMapper.insert(fd);
		}
		return true;
	}
}

package com.wimoor.amazon.auth.pojo.entity;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.SellingPartnerAPIAA.RateLimitConfiguration;
import com.amazon.spapi.client.ApiException;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.wimoor.common.pojo.entity.BaseEntity;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amazon_auth")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmazonAuthority  extends  BaseEntity  implements RateLimitConfiguration{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4872652002637528809L;

	 @TableField(value =  "shop_id")
    private String shopId;

	 @TableField(value =  "sellerid")
    private String sellerid;

	 @TableField(value =  "MWSAuthToken")
    private String mwsauthtoken;

	 @TableField(value =  "name")
	private String name;
	
	 @TableField(value =  "disable")
	private Boolean disable;
    
	 @TableField(value =  "region")
    private String  region;
    
	 @TableField(value =  "pictureId")
    private String pictureId;
	
	 @TableField(value =  "status")
    private String status;
	
	 @TableField(value =  "statusupdate")
    private Date statusupdate;
	
	 @TableField(value =  "productdate")
    private Date productdate;
	
	 @TableField(value =  "opttime")
    private Date opttime;
	
	 @TableField(value =  "groupid")
	private String groupid;
	
	 @TableField(value =  "createtime")
    private Date createtime;
	
	 @TableField(value =  "refreshinvtime")
     private Date refreshinvtime;
	 
	 @TableField(value =  "refresh_token")
	 private String refreshToken;
	 
	 @TableField(value =  "refresh_token_time")
	 private Date refreshTokenTime;
	 
	@TableField(value =  "aws_region")
	private String AWSRegion;
		
	@TableField(value =  "access_key_id")
	private String accessKeyId;
	    
	@TableField(value =  "secret_key")
	private String secretKey;
	    
	@TableField(value =  "role_arn")
	private String roleArn;
	    
	@TableField(value =  "client_id")
	private String clientId;
	    
	@TableField(value =  "client_secret")
	private String clientSecret;
	    
	@TableField(exist = false)
	private Date captureDateTime;
	@TableField(exist = false)
    private int requestOrderReportTime=0;
	@TableField(exist = false)
    Marketplace marketPlace;
	@TableField(exist = false)
    String groupname;
	@TableField(exist = false)
	Date lastupdate;
	@TableField(exist = false)
	String useApi;
	public Date getCaptureDateTime() {
		if(captureDateTime==null){
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			captureDateTime=c.getTime();
		}
		return captureDateTime;
	}


	public AmzAuthApiTimelimit getApiRateLimit() {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), this.getUseApi());
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setApiname(this.getUseApi());
			limit.setLog(null);
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			limit.setLastuptime(c.getTime());
			limit.setNexttoken(null);
			limit.setPages(0);
			limit.setRestore(20.00);
			limit.setAmazonauthid(new BigInteger(this.getId()));
		}
		return limit;
	}
 
	public void setApiRateLimit(Map<String, List<String>> responseHeaders,ApiException e) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(),  this.useApi);
		if(e!=null&&e.getResponseBody()!=null&&e.getResponseBody().contains("Token is not valid")) {
			limit.setNexttoken(null);
		}
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname( this.useApi);
			setRateLimit(limit,responseHeaders,e);
			try {
				amzAuthApiTimelimitService.save(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		}else {
			setRateLimit(limit,responseHeaders,e);
			try {
				amzAuthApiTimelimitService.update(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		}
		
	}
	private void setRateLimit(AmzAuthApiTimelimit limit ,Map<String, List<String>> responseHeaders,ApiException e) {
		String log=null;
		Double rateLimit=null;
		if(responseHeaders!=null) {
			List<String> headerList = responseHeaders.get("x-amzn-RateLimit-Limit");
			if(headerList!=null&&headerList.size()>0) {
				String strRateLimit = headerList.get(0);
				if(StrUtil.isNotBlank(strRateLimit)) {
					rateLimit=Double.parseDouble(strRateLimit);
				}
			}
		}
		if(e!=null&&rateLimit==null) {
			 log=e.getMessage();
			 rateLimit=0.01;
			 if(e!=null&&e.getResponseBody()!=null) {
				 log=e.getResponseBody();
				 if(log!=null&&!log.contains("Unauthorized")) {
					 rateLimit=0.01;
				  }
				}
			 
		}else if(rateLimit==null) {
			 rateLimit=1.00;
		}
	
		limit.setRestore(rateLimit);
		limit.setLog(log);
		if(limit.getPages()!=null) {
			limit.setPages(limit.getPages()+1);
		}else {
			limit.setPages(1);
		}
		limit.setLastuptime(new Date());
	}
	public void setApiRateLimit( Map<String, List<String>> responseHeaders,String token) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(),  this.useApi);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname( this.useApi);
			setRateLimit(limit,responseHeaders,null);
			limit.setNexttoken(token);
			try {
				amzAuthApiTimelimitService.save(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		}else {
			setRateLimit(limit,responseHeaders,null);
			limit.setLog("");
			limit.setNexttoken(token);
			try {
				amzAuthApiTimelimitService.update(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		}
		
	}
	public void setApiRateLimit(Map<String, List<String>> responseHeaders,Date start,String token) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), this.useApi);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname(this.useApi);
			if(start!=null) {
				limit.setStartTime(start);
				limit.setEndTime(new Date());
			}
			limit.setNexttoken("");
			limit.setNexttoken(token);
			limit.setLog("success");
			setRateLimit(limit,responseHeaders,null);
			try {
				amzAuthApiTimelimitService.save(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		}else {
			setRateLimit(limit,responseHeaders,null);
			if(StrUtil.isBlank(token)) {
				if(start!=null) {
					limit.setStartTime(start);
					limit.setEndTime(new Date());
				}
				limit.setNexttoken("");
			}else {
				if(StrUtil.isBlank(limit.getNexttoken())) {
					if(start!=null) {
						limit.setStartTime(start);
						limit.setEndTime(new Date());
					}
				}
				limit.setNexttoken(token);
			}
			limit.setLog("success");
			try {
				amzAuthApiTimelimitService.update(limit);
			}catch(Exception me) {
				me.printStackTrace();
			}
		
		}
		
	}
	public boolean apiNotRateLimit() {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), this.getUseApi());
		if(limit==null||limit.apiNotRateLimit()) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public Double getRateLimitPermit() {
		// TODO Auto-generated method stub
		if(this.useApi!=null) {
			IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
			AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), this.useApi);
            if(limit==null) {
            	return 1.0;
            }else {
            	return limit.getRestore();
            }
		}else {
			return 1.0;
		}
	
	}

	@Override
	public Long getTimeOut() {
		// TODO Auto-generated method stub
		return Long.MAX_VALUE;
	}

 
	
}
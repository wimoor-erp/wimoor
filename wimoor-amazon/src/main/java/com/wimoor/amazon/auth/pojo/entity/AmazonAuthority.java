package com.wimoor.amazon.auth.pojo.entity;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiException;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.pojo.entity.BaseEntity;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amazon_auth")
public class AmazonAuthority  extends  BaseEntity  {
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
	 
	 @TableField(value =  "aws_region")
	 private String AWSRegion;
	 
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
 
	 
	public Date getCaptureDateTime() {
		if(captureDateTime==null){
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			captureDateTime=c.getTime();
		}
		return captureDateTime;
	}


	public boolean apiNotRateLimit(String api) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), api);
		if(limit==null||limit.apiNotRateLimit()) {
			return true;
		}else {
			return false;
		}
	}

	public AmzAuthApiTimelimit getApiRateLimit(String api) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), api);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setApiname(api);
			limit.setLog(null);
			limit.setLastuptime(new Date());
			limit.setNexttoken(null);
			limit.setPages(0);
			limit.setRestore(20.00);
			limit.setAmazonauthid(new BigInteger(this.getId()));
		}
		return limit;
	}
 

	public void setApiRateLimit(String api, Map<String, List<String>> responseHeaders,ApiException e) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), api);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname(api);
			setRateLimit(limit,responseHeaders,e);
			amzAuthApiTimelimitService.save(limit);
		}else {
			setRateLimit(limit,responseHeaders,e);
			amzAuthApiTimelimitService.updateById(limit);
		}
		
	}
	
	private void setRateLimit(AmzAuthApiTimelimit limit ,Map<String, List<String>> responseHeaders,ApiException e) {
		String log=null;
		Double rateLimit=null;
		if(e!=null) {
			 log=e.getMessage();
			 rateLimit=0.0006;
		}else {
			 rateLimit=1.00;
		}
		if(responseHeaders!=null) {
			List<String> headerList = responseHeaders.get("x-amzn-RateLimit-Limit");
			if(headerList!=null&&headerList.size()>0) {
				String strRateLimit = headerList.get(0);
				if(StrUtil.isNotBlank(strRateLimit)) {
					rateLimit=Double.parseDouble(strRateLimit);
				}
			}
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
	
	public void setApiRateLimit(String api, Map<String, List<String>> responseHeaders,String token) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), api);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname(api);
			setRateLimit(limit,responseHeaders,null);
			limit.setNexttoken(token);
			amzAuthApiTimelimitService.save(limit);
		}else {
			setRateLimit(limit,responseHeaders,null);
			limit.setNexttoken(token);
			amzAuthApiTimelimitService.updateById(limit);
		}
		
	}
	
	public void setApiRateLimit(String api, Map<String, List<String>> responseHeaders,Date start,String token) {
		// TODO Auto-generated method stub
		IAmzAuthApiTimelimitService amzAuthApiTimelimitService=SpringUtil.getBean("amzAuthApiTimelimitService");
		AmzAuthApiTimelimit limit = amzAuthApiTimelimitService.getApiLimit(this.getId(), api);
		if(limit==null) {
			limit=new AmzAuthApiTimelimit();
			limit.setAmazonauthid(new BigInteger(this.getId()));
			limit.setApiname(api);
			limit.setStartTime(start);
			limit.setEndTime(new Date());
			setRateLimit(limit,responseHeaders,null);
			limit.setNexttoken(token);
			amzAuthApiTimelimitService.save(limit);
		}else {
			setRateLimit(limit,responseHeaders,null);
			limit.setStartTime(start);
			limit.setEndTime(new Date());
			limit.setNexttoken(token);
			amzAuthApiTimelimitService.updateById(limit);
		}
		
	}
	
}
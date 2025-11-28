package com.wimoor.amazon.feed.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_submitfeed_queue")  
@ApiModel(value="AmzSubmitFeedQueue对象", description="AmzSubmitFeedQueue")
public class AmzSubmitFeedQueue extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1134447705414576481L;

	@TableField(value= "shopid")
    private String shopid;

	@TableField(value= "amazonAuthId")
    private String amazonauthid;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "feed_type")
    private String feedType;

	@TableField(value= "process_date")
    private Date processDate;
	
	@TableField(value= "createtime")
    private Date createtime;
	
	
	@TableField(value= "process_log")
    private String processLog;

	@TableField(value= "submitfeedid")
    private String submitfeedid;

	@TableField(value= "filename")
    private String filename;
	
	@TableField(value= "operator")
    private String operator;
	
	@TableField(value= "content")
    private byte[] content;
	
	@TableField(value= "feedoptions")
	private String feedoptions;
	 
	 
	
    
}
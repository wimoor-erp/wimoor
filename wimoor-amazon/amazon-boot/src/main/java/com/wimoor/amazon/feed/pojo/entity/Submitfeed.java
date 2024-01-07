package com.wimoor.amazon.feed.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
 
@TableName("t_amz_submitfeed")  
@ApiModel(value="AmzSubmitFeedQueue对象", description="AmzSubmitFeedQueue")
public class Submitfeed implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 864049913032652293L;

	@TableField(value= "feed_submissionid")
    private String feedSubmissionid;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "sellerid")
    private String sellerid;

	@TableField(value= "shopid")
    private String shopid;
	
	@TableField(value= "feed_type")
    private String feedType;
	
	@TableField(value= "submitted_date")
    private Date submittedDate;
	
	@TableField(value= "started_processing_date")
    private Date startedProcessingDate;
	
	@TableField(value= "completed_processiong_date")
    private Date completedProcessiongDate;
	
	@TableField(value= "operator")
    private String operator;
	
	@TableField(value= "opttime")
    private Date opttime;
	
	@TableField(value= "feed_processing_status")
    private String feedProcessingStatus;

	@TableField(value= "queueid")
    private String queueid;
	
	@TableField(value= "amzprocesslog")
    private String amzprocesslog;
	
	@TableField(value= "documentid")
    private String documentid;
	
    
}
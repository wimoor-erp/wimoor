package com.wimoor.amazon.report.pojo.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_report_requestrecord")  
@ApiModel(value="ReportRequestRecord对象", description="报表申请记录表")
public class ReportRequestRecord extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6061690161753258008L;


    @ApiModelProperty(value = "卖家ID")
	@TableField(value= "sellerid")
    private String sellerid;
	
    @ApiModelProperty(value = "站点ID")
	@TableField(value= "marketPlaceId")
    private String marketplaceid;

    @ApiModelProperty(value = "报表ID")
	@TableField(value= "reportId")
    private String reportid;

    @ApiModelProperty(value = "报表类型")
	@TableField(value= "reportType")
    private String reporttype;
	
    @ApiModelProperty(value = "报表请求ID")
	@TableField(value= "reportRequestId")
    private String reportrequestid;

    @ApiModelProperty(value = "报表文件ID")
  	@TableField(value= "reportDocumentId")
    private String reportDocumentId;
    
    @ApiModelProperty(value = "报表开始时间")
	@TableField(value= "startDate")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdate;
	 
    @ApiModelProperty(value = "结束时间")
	@TableField(value= "endDate")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    
    @ApiModelProperty(value = "是否最新")
	@TableField(value= "isnewest")
    private Boolean isnewest;

    @ApiModelProperty(value = "可用时间")
	@TableField(value= "availableDate")
    private Date availabledate;
 
    @ApiModelProperty(value = "获取次数")
	@TableField(value= "getnumber")
    private Integer getnumber;
    
    @ApiModelProperty(value = "处理次数")
	@TableField(value= "treatnumber")
    private Integer treatnumber;
	
    @ApiModelProperty(value = "最后更新时间")
	@TableField(value= "lastupdate")
    private Date lastupdate;
	
    @ApiModelProperty(value = "处理log")
	@TableField(value= "log")
	private String log;
	
    @ApiModelProperty(value = "处理状态")
	@TableField(value= "report_processing_status")
	private String reportProcessingStatus;
	
    @ApiModelProperty(value = "报表是否正在处理")
	@TableField(value= "isrun")
    private boolean isrun;
	
    @ApiModelProperty(value = "报表可选参数")
	@TableField(value= "reportOptions")
    private String reportOptions;
	
}
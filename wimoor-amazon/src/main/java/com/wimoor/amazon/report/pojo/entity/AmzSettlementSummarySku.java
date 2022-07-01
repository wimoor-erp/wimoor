package com.wimoor.amazon.report.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.wimoor.amazon.util.UUIDUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@TableName("t_amz_settlement_summary_sku")  
@ApiModel(value="AmzSettlementSummarySku对象", description="SKU账期分析")
public class AmzSettlementSummarySku implements Serializable{
	
	/**
	 * 
	 */
	@ApiModelProperty(value = "ID")
	@MppMultiId
	@TableField(value="id")
    String id;
	
	public String getId() {
		if(id==null) {
			id=UUIDUtil.getUUIDshort();
		}
		return id;
	}
	
 
	private static final long serialVersionUID = -4840675120459455649L;

	@TableField(value="order_amount")
    private Integer orderAmount;

	@TableField(value="sales")
    private Integer sales;

	@TableField(value="principal")
    private BigDecimal principal;

	@TableField(value="commission")
    private BigDecimal commission;

	@TableField(value="refund")
    private BigDecimal refund;

	@TableField(value="fbafee")
    private BigDecimal fbafee;
	
	@TableField(value="otherfee")
	private BigDecimal otherfee;
	
	@TableField(value="tax")
	private BigDecimal tax;
	
	@MppMultiId
	@TableField(value="amazonAuthId")
    private String amazonauthid;
	
	@TableId(value="settlementid")
    private String settlementId;

	@TableField(value="sku")
    private String sku;

	@TableField(value="marketplace_name")
    private String marketplaceName;

	@MppMultiId
	@TableField(value="posted_date")
    private Date postedDate;
 
    
    
}
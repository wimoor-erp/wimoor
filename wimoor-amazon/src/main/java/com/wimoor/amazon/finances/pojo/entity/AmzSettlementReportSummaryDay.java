package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_settlement_summary_day")  
@ApiModel(value="AmzSettlementReportSummaryDay对象", description="账期按日汇总")
public class AmzSettlementReportSummaryDay extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7813928142733228215L;

	@TableField(value= "amount")
    private BigDecimal amount;

	@TableField(value= "currency")
	private String currency;
	
	@TableField(value= "sku")
    private String sku;
	
	@TableField(value= "settlementid")
    private String settlementId;
	

	@TableField(value= "marketplace_name")
    private String marketplaceName;

	@TableField(value= "posted_date")
    private Date postedDate;

	@TableField(value= "transaction_type")
    private String transactionType;

	@TableField(value= "amount_type")
    private String amountType;

	@TableField(value= "amount_description")
    private String amountDescription;

	@TableField(value= "amazonAuthId")
    private String amazonauthid;
	
	@TableField(value= "fulfillment_type")
	private String fulfillmentType;
	
    
    
}
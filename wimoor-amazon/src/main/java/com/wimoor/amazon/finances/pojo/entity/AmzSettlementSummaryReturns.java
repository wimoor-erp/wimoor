package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigInteger;
import java.util.Date;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_settlement_summary_returns")  
@ApiModel(value="AmzSettlementSummaryReturns对象", description="账期退款数量")
public class AmzSettlementSummaryReturns extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1134172178536019587L;

	@TableField(value= "amazonauthid")
    private BigInteger amazonauthid;

    @TableField(value= "sku")
    private String sku;

    @TableField(value= "marketplace_name")
    private String marketplaceName;

    @TableField(value= "posted_date")
    private Date postedDate;

    @TableField(value= "quantity")
    private Integer quantity;

    
    @TableField(value= "settlementid")
    private String settlementId;
    
    @TableField(value= "mfnqty")
    private Integer mfnqty;
     
    
}
package com.wimoor.amazon.report.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 

@Data
@TableName("t_amz_settlement_acc_report")  
@ApiModel(value="AmzSettlementAccReport对象", description="账期回款记录")
public class AmzSettlementAccReport  {
    @ApiModelProperty(value = "账期ID")
	@TableId(value=  "settlement_id")
    private String settlementId;

    @ApiModelProperty(value = "授权ID")
	@TableField(value= "amazonauthid")
    private String amazonauthid;

    @ApiModelProperty(value = "站点名称")
	@TableField(value= "marketplace_name")
    private String marketplaceName;
	
    @ApiModelProperty(value = "账期开始日期")
	@TableField(value= "settlement_start_date")
    private Date settlementStartDate;

    @ApiModelProperty(value = "账期结束日期")
	@TableField(value= "settlement_end_date")
    private Date settlementEndDate;

    @ApiModelProperty(value = "转账日期")
	@TableField(value= "deposit_date")
    private Date depositDate;

    @ApiModelProperty(value = "转账金额")
	@TableField(value= "total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "转账币种")
	@TableField(value= "currency")
    private String currency;
 
}
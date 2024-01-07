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
@TableName("t_amz_settlement_acc_statement")  
@ApiModel(value="AmzSettlementAccStatement对象", description="账期记录结账")
public class AmzSettlementAccStatement extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2502083646736515858L;

	@TableField(value= "shopid")
    private BigInteger shopid;
	
	@TableField(value= "groupid")
    private BigInteger groupid;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "currency")
    private String currency;
	
	@TableField(value= "datetype")
    private String datetype;

	@TableField(value= "startdate")
    private Date startdate;

	@TableField(value= "enddate")
    private Date enddate;

	@TableField(value= "operator")
    private BigInteger operator;
	
	@TableField(value= "opttime")
	private Date opttime;
 
	@TableField(value= "summaryall")
    private byte[] summaryall;

	@TableField(value= "listdata")
    private byte[] listdata;
	

	@TableField(value= "fielddata")
    private String fielddata;
	
    
}
package com.wimoor.amazon.report.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amazon_auth_market_performance")  
@ApiModel(value="AmazonAuthMarketPerformance对象", description="店铺绩效")
public class AmazonAuthMarketPerformance {
	@TableId(value= "amazonauthid")
    private String amazonauthid;

	@TableField(value= "sellerid")
    private String sellerid;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "performance")
    private String performance;

	@TableField(value= "refreshtime")
    private Date refreshtime;
 
}
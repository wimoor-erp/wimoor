package com.wimoor.amazon.report.pojo.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_amazon_auth_market_performance")  
@ApiModel(value="AmazonAuthMarketPerformance对象", description="店铺绩效")
public class AmazonAuthMarketPerformance {
    @ApiModelProperty(value = "授权ID")
	@TableId(value= "amazonauthid")
    private String amazonauthid;
    
    @ApiModelProperty(value = "卖家ID")
	@TableField(value= "sellerid")
    private String sellerid;

    @ApiModelProperty(value = "站点ID")
	@TableField(value= "marketplaceid")
    private String marketplaceid;

    @ApiModelProperty(value = "前端使用json对象")
	@TableField(exist=false)
    private JSONObject performanceJson;
	
    @ApiModelProperty(value = "DB使用String对象")
	@TableField(value= "performance")
    private String performance;
	
    @ApiModelProperty(value = "站点状态")
	@TableField(value= "accountstatus")
    private String accountstatus;
	
    @ApiModelProperty(value = "更新时间")
	@TableField(value= "refreshtime")
    private Date refreshtime;
 
}
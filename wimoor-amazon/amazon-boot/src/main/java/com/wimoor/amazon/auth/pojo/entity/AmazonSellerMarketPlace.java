package com.wimoor.amazon.auth.pojo.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
 
@Data
@TableName("t_amazonseller_market")
public class AmazonSellerMarketPlace {
	
	@TableField(value =  "sellerid")
    private String sellerid;

	@TableField(value =  "marketplace_id")
    private String marketplaceId;
	
	@TableField(value =  "opttime")
    private Date opttime;

	@TableField(value =  "disable")
    private Boolean disable;

    
}
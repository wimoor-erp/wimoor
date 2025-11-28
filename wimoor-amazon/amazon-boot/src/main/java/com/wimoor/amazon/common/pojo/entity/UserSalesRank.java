package com.wimoor.amazon.common.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_usersales_rank")
public class UserSalesRank {
    
	@TableField(value= "userid")
    private String userid;

    
	@TableField(value= "shopid")
    private String shopid;
	
    
	@TableField(value= "daytype")
    private Integer daytype;
    
	@TableField(value= "quantity")
    private Integer quantity;

	@TableField(value= "orderprice")
    private BigDecimal orderprice;


	@TableField(value= "oldorderprice")
    private BigDecimal oldorderprice;

	@TableField(value="createdate")
	private Date createdate;
	 
}
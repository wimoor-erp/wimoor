package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_invoice")
@ApiModel(value="OrdersInvoice对象", description="订单发票信息表")
public class AmzOrdersInvoice extends BaseEntity implements Serializable{
	
	
	private static final long serialVersionUID = -1005876107975620640L;

	@TableField(value = "groupid")
    private String groupid;

	@TableField(value = "logoUrl")
    private String logourl;

	@TableField(value = "company")
    private String company;

	@TableField(value = "country")
    private String country;
	
	@TableField(value = "province")
    private String province;

	@TableField(value = "city")
    private String city;

	@TableField(value = "address")
    private String address;

	@TableField(value = "phone")
    private String phone;

	@TableField(value = "postalcode")
    private String postalcode;
	
	@TableField(value = "email")
    private String email;
	
	@TableField(value = "sign")
    private String sign;
	
	@TableField(value = "image")
    private String image;
	
}

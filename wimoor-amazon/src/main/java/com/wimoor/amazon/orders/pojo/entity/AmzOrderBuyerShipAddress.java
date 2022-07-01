package com.wimoor.amazon.orders.pojo.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.util.UUIDUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_order_buyer_ship_address")
@ApiModel(value="OrdersAddress对象", description="订单对应地址信息")
public class AmzOrderBuyerShipAddress implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "amazon_order_id" )
	@ApiModelProperty(value = "亚马逊订单ID")
    private String amazonOrderid;
	
	@TableField(value="amazonAuthid")
	@ApiModelProperty(value = "系统AuthID")
    private String amazonauthid;
	
	@TableField(value="marketplaceid")
	@ApiModelProperty(value = "亚马逊站点ID")
    private String marketplaceid;
	
	@TableField(value="name")
	@ApiModelProperty(value = "地址名称")
    private String name;
	
	@TableField(value="phone")
	@ApiModelProperty(value = "手机号码")
    private String phone;
	
	@TableField(value="address1")
	@ApiModelProperty(value = "地址行1")
    private String addressLine1;
	
	@TableField(value="address2")
	@ApiModelProperty(value = "地址行2")
    private String addressLine2;
	
	@TableField(value="address3")
	@ApiModelProperty(value = "地址行3")
    private String addressLine3;
	
	@TableField(value="address_type")
	@ApiModelProperty(value = "地址类型")
    private String addressType;
	
	@TableField(value="city")
	@ApiModelProperty(value = "城市")
    private String city;
	
	@TableField(value="countrycode")
	@ApiModelProperty(value = "国家编码")
    private String countryCode;
	
	@TableField(value="county")
	@ApiModelProperty(value = "县")
    private String county;
	
	@TableField(value="district")
	@ApiModelProperty(value = "街道")
    private String district;
	
	@TableField(value="municipality")
	@ApiModelProperty(value = "自治市")
    private String municipality;
	
	@TableField(value="postalcode")
	@ApiModelProperty(value = "邮政编码")
    private String postalCode;
	
	@TableField(value="state")
	@ApiModelProperty(value = "状态或者地区")
    private String stateOrRegion;
	
	@TableField(value="opttime")
	@ApiModelProperty(value = "操作时间")
    private Date opttime;
	
	@TableField(value="id")
    private String id;
    
	public String getId() {
		if (null == id) {
			id = UUIDUtil.getUUIDshort();
		}
		return id;
	}

}

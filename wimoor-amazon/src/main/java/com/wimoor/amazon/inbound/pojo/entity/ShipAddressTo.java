package com.wimoor.amazon.inbound.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.AmazonBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_addressto")
public class ShipAddressTo extends AmazonBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3556222640991967971L;

	@TableField(value="name")
    private String name;

	@TableField(value="isfrom")
    private Boolean isfrom;

	@TableField(value="addressline1")
    private String addressline1;

	@TableField(value="addressline2")
    private String addressline2;

	@TableField(value="city")
    private String city;

	@TableField(value="districtorcounty")
    private String districtorcounty;

	@TableField(value="stateorprovincecode")
    private String stateorprovincecode;

	@TableField(value="countrycode")
    private String countrycode;

	@TableField(value="postalcode")
    private String postalcode;

	@TableField(value="phone")
    private String phone;

	@TableField(value="shopid")
    private String shopid;

	 
 
}
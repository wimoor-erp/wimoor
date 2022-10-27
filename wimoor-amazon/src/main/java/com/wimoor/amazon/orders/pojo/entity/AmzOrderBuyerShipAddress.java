package com.wimoor.amazon.orders.pojo.entity;


import java.io.Serializable;
import java.util.Date;

import com.amazon.spapi.model.orders.Address;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_orders_address")
@ApiModel(value="OrdersAddress对象", description="订单对应地址信息")
public class AmzOrderBuyerShipAddress implements Serializable{
	
	public void setShipAddress(Address address) {
		// TODO Auto-generated constructor stub
		String addressline1 = address.getAddressLine1();
		String addressline2 = address.getAddressLine2();
		String addressline3= address.getAddressLine3();
		String addresstype =null;
		if(address.getAddressType()!=null) {
			addresstype = address.getAddressType().getValue();
		}
		String city = address.getCity();
		String countrycode = address.getCountryCode();
		String county = address.getCounty();
		String district=address.getDistrict();
		String municipality = address.getMunicipality();
		String addressname = address.getName();
		String phone=address.getPhone();
		String postalcode = address.getPostalCode();
		String stateorRegion = address.getStateOrRegion();
		this.setAddressLine1(addressline1);
		this.setAddressLine2(addressline2);
		this.setAddressLine3(addressline3);
		this.setAddressType(addresstype);
		this.setCity(city);
		this.setCountryCode(countrycode);
		this.setCounty(county);
		this.setDistrict(district);
		this.setMunicipality(municipality);
		this.setName(addressname);
		this.setOpttime(new Date());
		this.setPhone(phone);
		this.setPostalCode(postalcode);
		this.setStateOrRegion(stateorRegion);
	}
	
 
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "amazon_order_id" )
	@ApiModelProperty(value = "亚马逊订单ID")
    private String amazonOrderid;
	
	@TableField(value="amazonAuthId")
	@ApiModelProperty(value = "系统AuthID")
    private String amazonauthid;
	
	@TableField(value="marketplaceId")
	@ApiModelProperty(value = "亚马逊站点ID")
    private String marketplaceid;
	
	@TableField(value="name")
	@ApiModelProperty(value = "地址名称")
    private String name;
	
	@TableField(value="phone")
	@ApiModelProperty(value = "手机号码")
    private String phone;
	
	@TableField(value="address_line1")
	@ApiModelProperty(value = "地址行1")
    private String addressLine1;
	
	@TableField(value="address_line2")
	@ApiModelProperty(value = "地址行2")
    private String addressLine2;
	
	@TableField(value="address_line3")
	@ApiModelProperty(value = "地址行3")
    private String addressLine3;
 
	@TableField(value="city")
	@ApiModelProperty(value = "城市")
    private String city;
	
	@TableField(value="municipality")
	@ApiModelProperty(value = "自治区")
    private String municipality;
	
	@TableField(value="country_code")
	@ApiModelProperty(value = "国家编码")
    private String countryCode;
	
	@TableField(value="county")
	@ApiModelProperty(value = "县")
    private String county;
	
	@TableField(value="district")
	@ApiModelProperty(value = "街道")
    private String district;
	
	@TableField(value="postal_code")
	@ApiModelProperty(value = "邮政编码")
    private String postalCode;
	
	@TableField(value="state_or_region")
	@ApiModelProperty(value = "状态或者地区")
    private String stateOrRegion;
	
	@TableField(value="address_type")
	@ApiModelProperty(value = "状态或者地区")
    private String addressType;
	
	@TableField(value="opttime")
	@ApiModelProperty(value = "操作时间")
    private Date opttime;
 
}

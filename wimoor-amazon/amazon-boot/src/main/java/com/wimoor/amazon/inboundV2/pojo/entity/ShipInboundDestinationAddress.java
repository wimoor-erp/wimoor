package com.wimoor.amazon.inboundV2.pojo.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.amazon.spapi.model.fulfillmentinboundV20240320.Address;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ShipmentDestination;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@ApiModel(value="ShipInboundCase对象", description="货件装箱详情")
@TableName("t_erp_ship_v2_destination_address")
public class ShipInboundDestinationAddress {
	public ShipInboundDestinationAddress(){
		
	}
	
    public ShipInboundDestinationAddress(ShipmentDestination dest){
		this.code=dest.getWarehouseId();
		this.destinationType=dest.getDestinationType();
		Address address = dest.getAddress();
		this.name=address.getName();
		this.addressLine1=address.getAddressLine1();
		this.addressLine2=address.getAddressLine2();
		this.city=address.getCity();
		this.countryCode=address.getCountryCode();
		this.companyName=address.getCompanyName();
		this.opttime=new Date();
		this.postalCode=address.getPostalCode();
		this.phoneNumber=address.getPhoneNumber();
		this.email= address.getEmail();
		this.stateOrProvinceCode =address.getStateOrProvinceCode();
	}
	/**
	 * 
	 */
	@ApiModelProperty(value = "CODE")
	@NotNull(message="code不能为空")
    @TableId(value= "code")
    private String code;

	@ApiModelProperty(value = "destinationType")
    @NotNull(message="destinationType不能为空")
    @TableField(value= "destinationType")
    private String destinationType;
	
	@ApiModelProperty(value = "name")
    @TableField(value= "name")
    private String name;
	
	@ApiModelProperty(value = "addressLine1")
    @TableField(value= "addressLine1")
    private String addressLine1;
 
	@ApiModelProperty(value = "addressLine2")
    @TableField(value= "addressLine2")
    private String addressLine2;
	
	@ApiModelProperty(value = "city")
    @TableField(value= "city")
    private String city;
	
	@ApiModelProperty(value = "companyName")
    @TableField(value= "companyName")
    private String companyName;
	
	@ApiModelProperty(value = "phoneNumber")
    @TableField(value= "phoneNumber")
    private String phoneNumber;
	
	@ApiModelProperty(value = "countryCode")
    @TableField(value= "countryCode")
    private String countryCode;
	
	@ApiModelProperty(value = "postalCode")
    @TableField(value= "postalCode")
    private String postalCode;
	
	@ApiModelProperty(value = "email")
    @TableField(value= "email")
    private String email;
	
	@ApiModelProperty(value = "stateOrProvinceCode")
    @TableField(value= "stateOrProvinceCode")
    private String stateOrProvinceCode;
	
	
	@ApiModelProperty(value = "opttime")
    @TableField(value= "opttime")
    private Date opttime;


	@TableField(exist = false)
	private String area;

	@TableField(exist = false)
	private Boolean isfar;
}
package com.wimoor.amazon.inbound.pojo.entity;

 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.AmazonBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="发货地址对象", description="发货地址")
@TableName("t_erp_ship_address")
public class ShipAddress extends AmazonBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 787338653456648856L;
 
 
	@ApiModelProperty(value = "地址名称")
	@NotNull(message="名称不能为空")
	@TableField(value= "name")
    private String name;
	
	@ApiModelProperty(value = "店铺ID")
	@TableField(value= "groupid")
    private String groupid;

	@ApiModelProperty(value = "街道信息")
	@NotNull(message="街道信息不能为空")
	@TableField(value= "addressline1")
    private String addressline1;

	@ApiModelProperty(value = "街道信息2")
	@TableField(value= "addressline2")
    private String addressline2;

	@ApiModelProperty(value = "城市")
	@NotNull(message="城市不能为空")
	@TableField(value= "city")
    private String city;

	@ApiModelProperty(value = "国家或区域")
	@TableField(value= "districtorcounty")
    private String districtorcounty;
  
	@ApiModelProperty(value = "城市编码")
	@TableField(value= "stateorprovincecode")
    private String stateorprovincecode;

	@ApiModelProperty(value = "国家编码")
	@NotNull(message="国家信息不能为空")
	@TableField(value= "countrycode")
    private String countrycode;

	@ApiModelProperty(value = "邮政编码")
	@NotNull(message="邮政编码不能为空")
	@TableField(value= "postalcode")
    private String postalcode;

	@ApiModelProperty(value = "公司ID")
	@TableField(value= "shopid")
    private String shopid;
	
	@ApiModelProperty(value = "是否表单")
	@TableField(value="isfrom")
	private Boolean isfrom;
	
	@ApiModelProperty(value = "是否默认")
	@TableField(value="isdefault")
	private Boolean isdefault;
	
	@ApiModelProperty(value = "是否归档")
	@TableField(value="disable")
	private Boolean disable;
	
	@ApiModelProperty(value = "电话")
    @Size(min=7,max=20,message="请正确输入电话或手机号码格式")
	@TableField(value="phone")
	private String phone;
 

	public Boolean getDisable() {
		if(disable==null) {
           return Boolean.FALSE;			
		}else {
			return disable;
		}
	}

	 
	
    
 
}
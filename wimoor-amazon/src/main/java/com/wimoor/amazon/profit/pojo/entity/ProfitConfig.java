package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_profitcfg")  
@ApiModel(value="Profitconfig对象", description="配置")
public class ProfitConfig extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = 5105172354629277685L;


	@TableField(value= "shop_id")
	private String shopId;

	@TableField(value= "isSystem")
	private Boolean issystem;

	@TableField(value= "name")
	private String name;

	@TableField(value= "sales_channel")
	private String salesChannel;

	@TableField(value= "sellerPlan")
	private String sellerplan;

	@TableField(value= "shipmentStyle")
	private String shipmentstyle;

	@TableField(value= "isDefault")
	private Boolean isDefault;

	@TableField(value= "opttime")
	private Date opttime;

	@TableField(exist=false)
	private ProfitConfigCountry us = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry uk = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry de = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry fr = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry es = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry it = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry nl = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry jp = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry ca = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry au = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry in = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry mx = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry ae = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry sa = new ProfitConfigCountry();

	@TableField(exist=false)
	private ProfitConfigCountry pl = new ProfitConfigCountry();
	
	@TableField(exist=false)
	private ProfitConfigCountry se = new ProfitConfigCountry();
 
	public ProfitConfigCountry getProfitConfigCountry(String country) {
		if ("US".contains(country)) {
			return us;
		}
		if ("UK".contains(country)) {
			return uk;
		}
		if ("DE".contains(country)) {
			return de;
		}
		if ("FR".equalsIgnoreCase(country)) {
			return fr;
		}
		if ("IT".equalsIgnoreCase(country)) {
			return it;
		}
		if ("ES".equalsIgnoreCase(country)) {
			return es;
		}
		if ("NL".equalsIgnoreCase(country)) {
			return nl;
		}
		if ("JP".contains(country)) {
			return jp;
		}
		if ("CA".contains(country)) {
			return ca;
		}
		if ("AU".contains(country)) {
			return au;
		}
		if ("IN".contains(country)) {
			return in;
		}
		if ("MX".contains(country)) {
			return mx;
		}
		if ("AE".contains(country)) {
			return ae;
		}
		if ("SA".contains(country)) {
			return sa;
		}
		if ("PL".contains(country)) {
			return pl;
		}
		if ("SE".contains(country)) {
			return se;
		}
		return null;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

 

}
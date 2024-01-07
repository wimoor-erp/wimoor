package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
	

	@TableField(value= "isdelete")
	private Boolean isdelete;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@TableField(value= "opttime")
	private Date opttime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@TableField(value= "createtime")
	private Date createtime;
	
	@TableField(value= "operator")
	private BigInteger operator;
	
	@TableField(value= "creator")
	private BigInteger creator;
	
	@TableField(exist=false)
	private String operatorName ;
	
	@TableField(exist=false)
	private List<ProfitConfigCountry> countryList ;
	@TableField(exist=false)
	private Map<String,ProfitConfigCountry> countryMap ;
	
	public void setCountryList(List<ProfitConfigCountry> countrys) {
		countryList=countrys;
		countryMap=new HashMap<String,ProfitConfigCountry>();
		for(ProfitConfigCountry item:countryList) {
			countryMap.put(item.getCountry().toUpperCase(), item);
		}
	}
	
	public ProfitConfigCountry getProfitConfigCountry(String country) {
		if(country==null) {
			return null;
		}
		String mycountry=country.toUpperCase().trim();
		if(countryMap==null) {
			if(countryList!=null) {
				countryMap=new HashMap<String,ProfitConfigCountry>();
				for(ProfitConfigCountry item:countryList) {
					countryMap.put(item.getCountry().toUpperCase(), item);
				}
				return countryMap.get(mycountry);
			}else {
				return null;
			}
		}else {
			return countryMap.get(mycountry);
		}
		 
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

 

}
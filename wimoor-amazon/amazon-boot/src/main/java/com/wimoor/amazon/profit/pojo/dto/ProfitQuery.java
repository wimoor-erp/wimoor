package com.wimoor.amazon.profit.pojo.dto;

import java.util.HashMap;
import java.util.Map;

import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="ShowProfitQuery对象", description="查询利润计算详情")
public class ProfitQuery {
	String profittype;
	String profitCfgId ;// 利润计算方案
	String shipment;
	Integer typeId; 
	String cost;// 高
    String currency;

    
    String weight;// 重量
	String wunit;// 重量单位
	String length;// 长
	String lunit;// 长度单位
	String width;// 宽
	String height;// 高
	
	String categories;
	
	String shipmentType;//计算印度利润，local,regional,national
	String declaredValue;//申报价值
	 
	String declaredValueCur;//申报价值单位
	String taxrate;//印度进口税率
	String gstrate;//印度进口GST税率
	String sellingGSTRate;//印度销售GST税率
	String referralrate;//印度佣金比率
	String isSmlAndLightStr;//是否轻小
	@ApiModelProperty(value = "国家详情例如【UK,CA,US,FR,IT。。。】")
	Map<String,CostDetail> country=new HashMap<String,CostDetail>();
	ProfitConfig profitCfgAll;
}

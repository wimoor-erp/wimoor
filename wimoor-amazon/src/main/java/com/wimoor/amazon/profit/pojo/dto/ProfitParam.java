package com.wimoor.amazon.profit.pojo.dto;

import java.math.BigDecimal;
import java.util.List;

import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="ProfitParam对象", description="利润计算页面显示参数")
public class ProfitParam {
	@ApiModelProperty(value = "产品类型列表")
	List<ReferralFee> typeList ;//产品类型列表
	@ApiModelProperty(value = "为了计算prep service fee")
    List<String> categoryList ;//为了计算prep service fee
	@ApiModelProperty(value = "固定利率列表")
	List<BigDecimal> marginList;//固定利率列表
	@ApiModelProperty(value = "countryList")
    List<String> countryList;
	List<String> currencyUnitList ;
	@ApiModelProperty(value = "利润计算方案")
	List<ProfitConfig> profitCfgList ;//利润计算方案
	String defaultPlanId;
	List<Marketplace> marketlist ;
	List<Marketplace> countryMarketlist ;
}

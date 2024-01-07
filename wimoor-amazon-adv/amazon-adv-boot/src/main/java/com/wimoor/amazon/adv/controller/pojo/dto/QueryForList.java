package com.wimoor.amazon.adv.controller.pojo.dto;

import com.wimoor.amazon.adv.common.pojo.BasePageQuery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryForList extends BasePageQuery{
	@ApiModelProperty(value = "授权站点profile_ID", example = "41564561")
	String profileid;
	@ApiModelProperty(value = "广告活动ID", example = "41564561")
	String campaignid ;
	@ApiModelProperty(value = "广告组ID", example = "41564561")
	String adGroupid;
	@ApiModelProperty(value = "站点marketplaceid", example = "41564561")
	String marketplaceid ;
	@ApiModelProperty(value = "店铺ID", example = "41564561")
	String groupid ;
	@ApiModelProperty(value = "开始日期", example = "2022-01-01")
	String fromDate ;
	@ApiModelProperty(value = "结束日期", example = "2022-01-01")
	String endDate ;
	@ApiModelProperty(value = "结束日期", example = "2022-01-01")
	String paralist ;
	@ApiModelProperty(value = "广告活动状态", example = "enabled,paused,archived")
	String campaignStatus;
	@ApiModelProperty(value = "投放类型")
	String targetingType ;
	@ApiModelProperty(value = "广告活动类型",example="sp,sb,sd")
	String campaignType ;
	@ApiModelProperty(value = "广告活动名称")
	String campaignName ;
	@ApiModelProperty(value = "广告活动名称")
	String searchlist;
	@ApiModelProperty(value = "广告组名称")
	String adGroupsName ;
	@ApiModelProperty(value = "广告组状态")
	String adGroupsStatus;
	@ApiModelProperty(value = "portfolios ID")
	String portfolios ;
	@ApiModelProperty(value = "是否第1页")
	Boolean isZeroPage = false;
	@ApiModelProperty(value = "图表按周按月按天查询")
	String bytime;
	
	@ApiModelProperty(value = "关键词状态")
	String state;
	@ApiModelProperty(value = "关键词matchType")
	String matchType;
	@ApiModelProperty(value = "关键词name|产品名称")
	String name;
	@ApiModelProperty(value = "关键词ID")
	String keywordid;
	@ApiModelProperty(value = "产品SKU")
	String sku ;
	@ApiModelProperty(value = "产品ASIN")
	String asin ;
	@ApiModelProperty(value = "产品Color")
	String color ;
	@ApiModelProperty(value = "产品创建人ID")
	String creator ;
	@ApiModelProperty(value = "产品ChangeRate")
	String changeRate ;
	@ApiModelProperty(value = "产品备注")
	String remark ;
	@ApiModelProperty(value = "产品分类")
	String categoryid ;
	@ApiModelProperty(value = "产品SKUStr")
	String skuStr;
	@ApiModelProperty(value = "广告投放search")
	String search;
	
	@ApiModelProperty(value = "系统内置")
	String shopid;
	public Boolean getIsZeroPage() {
		if (super.getCurrentpage() ==0) {
				isZeroPage = true;
		}
		return isZeroPage;
	}
	public String getCampaignName() {
		return campaignName ;
	}
	
	public String getAdGroupsName() {
		return adGroupsName;
	}
 
	

 
	public String getProfileid() {
		return profileid;
	}

 
	public String getCampaignType() {
		if (campaignType == null) {
			campaignType = "all";
		}
		return campaignType;
	}

   
	public String getFromDate() {
		if(fromDate!=null) {
			fromDate=fromDate.replaceAll("/","-");
		}
		return fromDate;
	}

 

	public String getEndDate() {
		if(endDate!=null) {
			endDate=endDate.replaceAll("/","-");
		}
		return endDate;
	}

}

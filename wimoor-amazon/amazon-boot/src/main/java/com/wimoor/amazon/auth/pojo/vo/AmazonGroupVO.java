package com.wimoor.amazon.auth.pojo.vo;



import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="AmazonGroupVO对象", description="店铺")
public class AmazonGroupVO {
	   @ApiModelProperty(value = "店铺ID")
	   String groupid;
	   @ApiModelProperty(value = "店铺名称")
	   String gname;
	   
	   @ApiModelProperty(value = "店铺授权区域")
	   Map<String,AmazonRegionVO>  regions;
	   @ApiModelProperty(value = "店铺授权区域与国家")
	   List<AmazonRegionVO>  regionslist;
	    public void convertRegionsToItem() {
		// TODO Auto-generated method stub
		    if(regions!=null&&regions.size()>0) {
		    	for(Entry<String, AmazonRegionVO> entry:regions.entrySet()) {
		    		AmazonRegionVO region=entry.getValue();
		    		if(region.getCountrys()!=null&&region.getCountrys().size()>0) {
		    			region.countrysList=Lists.newArrayList(region.countrys.values());
		    		}
		    	}
		    	regionslist=Lists.newArrayList(this.regions.values());
		    }
	    }
}

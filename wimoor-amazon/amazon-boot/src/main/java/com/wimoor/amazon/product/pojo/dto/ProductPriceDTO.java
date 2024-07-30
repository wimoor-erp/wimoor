package com.wimoor.amazon.product.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductPriceDTO extends BasePageQuery{

	private String groupid;
	
	private String shopid;
	
	private String marketplaceid;
	
	private String searchtype;
	
	private String search;
	
	private String sku;
	
	private String status;
	
	private String owner;
	
	private String startDate;
	
	private String endDate;
	
	private String operator;
	
	private List<String> groupList;
	
}

package com.wimoor.erp.material.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MaterialDTO对象", description="material本地产品")

public class MaterialDTO extends BasePageQuery{
	String color;
	
	String search;
	
	String searchtype;
	
	String ftype;
	
	String isDelete;
	
	String searchlist;
	
	String materialid;
	
	String issfg;
	
	String warehouseid;
	
	String addressid;
	
	String categoryid;
	
	String supplier;
	
	String owner;
	
	String name;
	
	String remark;
	
	String downtype;
	String mtype;
	Boolean withoutTags;
	
	Boolean withPriceHis;
	Boolean isplan;
	Boolean iswarning;
	List<String> taglist;
	List<String> materialList;
	String productname;
}

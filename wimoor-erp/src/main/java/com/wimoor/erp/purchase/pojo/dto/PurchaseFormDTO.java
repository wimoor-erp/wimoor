package com.wimoor.erp.purchase.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseFormDTO对象", description="获取采购列表")
public class PurchaseFormDTO extends BasePageQuery{

	 @ApiModelProperty(value = "按搜索类型搜索SKU，订单，运单", example = "sku" )
	 String search ;
	
    @ApiModelProperty( value = "开始日期")
    String fromDate;
   
    @ApiModelProperty(value = "结束日期" )
    String toDate;
    
    @ApiModelProperty(value = "ftype" )
    String ftype;
    
    @ApiModelProperty(value = "状态与订单状态不同，属于自定义状态[0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成]",required = true )
	String auditstatus;
	
    @ApiModelProperty(value = "供应商ID" )
	String supplierid;
	    
    @ApiModelProperty(value = "日期类型[创建日期，审核日期，预计到货日期]",required = true )
    String datetype;
    
    @ApiModelProperty(value = "产品名称" )
    String name;
    
    @ApiModelProperty(value = "供应商名称")
    String supplier;
    
    @ApiModelProperty(value = "是否组装")
    String issfg;
    
    @ApiModelProperty(value = "订单SKU上的备注")
    String remark;
    
    @ApiModelProperty(value = "产品负责人ID")
    String owner;
    
    @ApiModelProperty(value = "仓库ID")
    String warehouseid;
    
    @ApiModelProperty(value = "分类ID")
    String categoryid;
    
    @ApiModelProperty(value = "单据ID")
    String formid;
    
    @ApiModelProperty(value = "审核状态['in','notin','outdate']",required = true )
    String auditstatusparam;
    
    List<String> taglist;
    
    String hasStatus;
    
    String groupid;
    
    
}

package com.wimoor.erp.assembly.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AssemblyVO对象", description="组装产品对应关系")
public class AssemblyVO {
	@ApiModelProperty(value = "组装产品对应关系ID")
	String id;
	
	@ApiModelProperty(value = "主产品ID")
	String mainmid;
	
	@ApiModelProperty(value = "子产品ID")
	String submid;
	
	@ApiModelProperty(value = "子产品需求数量[1个主产品需要的子产品量]")
	Integer subnumber;
	
	
	@ApiModelProperty(value = "子产品总需求数量")
	Integer subamount;
	
	@ApiModelProperty(value = "备注")
	String remark;
	
	@ApiModelProperty(value = "操作人")
	String operator;
	
	@ApiModelProperty(value = "子产品图片")
	String image;
	
	@ApiModelProperty(value = "子产品名称")
	String mname;
	
	@ApiModelProperty(value = "子产品颜色标签")
	String mcolor;
	
	@ApiModelProperty(value = "子产品单个箱子数量")
	Integer boxnum;
	
	@ApiModelProperty(value = "子产品SKU")
	String sku;
	
	@ApiModelProperty(value = "子产品采购价格")
	BigDecimal subprice;
	
	@ApiModelProperty(value = "子产品供应商ID")
	String supplierid;
	
	@ApiModelProperty(value = "子产品供应商名称")
	String supplier;
	
	@ApiModelProperty(value = "子产品供货周期")
	Integer deliverycycle;
	
	@ApiModelProperty(value = "子产品供货周期日期")
	Date deliverycycledate;
	
	@ApiModelProperty(value = "历史价格")
	String historyprice;
	
	@ApiModelProperty(value = "操作时间")
	Date opttime;
	
	@ApiModelProperty(value = "子产品-待入库库存")
	Integer inbound;
	
	@ApiModelProperty(value = "子产品-待出库库存")
	Integer outbound;
	
	@ApiModelProperty(value = "子产品-可用库存")
	Integer fulfillable;
	
	@ApiModelProperty(value = "组装信息子产品Map")
	Map<String,Object> submap;
	
	@ApiModelProperty(value = "货架库存")
	List<WarehouseShelfInventoryVo> shelfInvList;
	List<WarehouseShelfInventoryOptRecordVo> shelfInvRecordList;
}

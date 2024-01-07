package com.wimoor.amazon.inbound.pojo.dto;

 

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundShipmenSummaryDTO对象", description="申请货件列表条件")
public class ShipInboundShipmenSummaryDTO extends BasePageQuery{
	@ApiModelProperty(value = "订单状态[0:已取消,6:已完成,5:已发货,55:正在接受,7:(2,3,4)处理中，空：全部，3:配货]", example = "5")
	String auditstatus ;
	
	@ApiModelProperty(value = "货件ID", example = "FBA2X65XXX")
	String shipmentid ;
	
	@ApiModelProperty(value = "开始日期", example = "2022-01-01")
	String fromdate ;
	
	@ApiModelProperty(value = "结束日期", example = "2022-01-01")
	String enddate ;
	
	@ApiModelProperty(value = "站点ID", example = "A1AM78C64UM0Y8")
	String marketplaceid ;
	
	@ApiModelProperty(value = "店铺ID", example = "123456789")
	String groupid ;
	
	@ApiModelProperty(value = "授权店铺ID【系统填充】")
	List<String> groupList ;
	
	@ApiModelProperty(value = "仓库ID", example = "123456789")
	String warehouseid ;
	 
	@ApiModelProperty(value = "物流公司ID", example = "系统填写")
    String company ;
	
	@ApiModelProperty(value = "物流公司ID", example = "1556748645")
    String companyid ;
	
	@ApiModelProperty(value = "渠道ID", example = "123456789")
	String channel ;
	
	@ApiModelProperty(value = "运输方式", example = "123456789")
	String transtype ;
	
	@ApiModelProperty(value = "查询类型【number表示货件ID，remark，formnumber，sku】", example = "remark")
	String searchtype ;
	
	@ApiModelProperty(value = "查找内容", example = "SK0015")
	String search ;
	@ApiModelProperty(value = "异常单据", example = "true")
	String hasexceptionnum ;
	
	@ApiModelProperty(value = "公司ID", example = "系统填写")
    String shopid ;
	String downloadType;
	String datetype;
	String isShip;
}

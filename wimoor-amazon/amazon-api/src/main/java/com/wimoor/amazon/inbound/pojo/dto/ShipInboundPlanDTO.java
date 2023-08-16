package com.wimoor.amazon.inbound.pojo.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.common.pojo.entity.BizBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundPlan对象", description="货件")
@TableName("t_erp_ship_inboundplan")
public class ShipInboundPlanDTO  extends BizBaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -230459464810130543L;
 
	 
	@ApiModelProperty(value = "计划名称")
	@NotNull(message="名称不能为空")
	@Size(max=200,message="名称不能超过200个字符")
	@TableField(value="name")
    private String name;

	@ApiModelProperty(value = "计划编码")
	@TableField(value="number")
    private String number;

	@ApiModelProperty(value = "发货地址ID")
	@TableField(value="shipfromaddressid")
    private String shipfromaddressid;

	@ApiModelProperty(value = "是否贴标【系统填写】")
	@TableField(value="labelpreptype")
    private String labelpreptype;
	
	@ApiModelProperty(value = "是否整箱")
	@TableField(value="arecasesrequired")
	private Boolean arecasesrequired;

	@ApiModelProperty(value = "店铺ID")
	@TableField(value="amazongroupid")
    private String amazongroupid;

	@ApiModelProperty(value = "站点ID")
	@TableField(value="marketplaceid")
    private String marketplaceid;

	@ApiModelProperty(value = "仓库ID")
	@TableField(value="warehouseid")
    private String warehouseid;

	@ApiModelProperty(value = "公司ID【系统填写】")
	@TableField(value="shopid")
    private String shopid;

	@ApiModelProperty(value = "计划ID")
	@TableField(value="planid")
    private String planid;

	@ApiModelProperty(value = "创建时间【系统填写】")
	@TableField(value="createdate")
    private Date createdate;

	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value="creator")
    private String creator;
	
	@ApiModelProperty(value = "SKU数量【系统填写】")
	@TableField(value="skunum")
    private Integer skunum;
	
	@ApiModelProperty(value = "备注")
	@Size(max=200,message="备注不能超过200个字符")
	@TableField(value="remark")
	private String remark;

	@ApiModelProperty(value = "审核状态【系统填写】")
	@TableField(value="auditstatus")
	private Integer auditstatus;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "产品列表")
    private List<ShipInboundItemVo> planitemlist=new LinkedList<ShipInboundItemVo>();
 
	@TableField(exist = false)
	@ApiModelProperty(value = "物流信息")
	ShipInboundTransDTO transinfo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "是否欧洲分割")
	Boolean issplit;
   
	@TableField(exist = false)
	@ApiModelProperty(value = "发货批次号")
	String batchnumber;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "计划的站点")
	String planmarketplaceid;
}
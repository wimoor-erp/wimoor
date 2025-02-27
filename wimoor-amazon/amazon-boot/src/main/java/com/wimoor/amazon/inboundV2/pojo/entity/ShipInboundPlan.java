package com.wimoor.amazon.inboundV2.pojo.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.AmazonBaseEntity;

import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundPlan对象", description="货件")
@TableName("t_erp_ship_v2_inboundplan")
public class ShipInboundPlan  extends AmazonBaseEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8888551589740532165L;
	@ApiModelProperty(value = "计划名称")
	@NotNull(message="名称不能为空")
	@Size(max=200,message="名称不能超过200个字符")
	@TableField(value="name")
    private String name;

	@ApiModelProperty(value = "计划编码")
	@TableField(value="number")
    private String number;

	@ApiModelProperty(value = "发货地址ID")
	@TableField(value="source_address")
    private String sourceAddress;

	@ApiModelProperty(value = "店铺ID")
	@TableField(value="groupid")
    private String groupid;

	@ApiModelProperty(value = "站点ID")
	@TableField(value="marketplaceid")
    private String marketplaceid;
	
	@ApiModelProperty(value = "授权ID")
	@TableField(value="amazonauthid")
    private String amazonauthid;

	@ApiModelProperty(value = "仓库ID")
	@TableField(value="warehouseid")
    private String warehouseid;

	@ApiModelProperty(value = "审核人")
	@TableField(value="auditor")
    private String auditor;

	@ApiModelProperty(value = "审核状态[0：未处理，1：被审核，2：驳回，3：创建，4：取消]")
	@TableField(value="auditstatus")
    private Integer auditstatus;
	
	@ApiModelProperty(value = "审核时间")
	@TableField(value="auditime")
    private Date auditime;
	
	@ApiModelProperty(value = "公司ID【系统填写】")
	@TableField(value="shopid")
    private String shopid;
	
	@ApiModelProperty(value = "计划ID")
	@TableField(value="batchnumber")
    private String batchnumber;

	@ApiModelProperty(value = "创建时间【系统填写】")
	@TableField(value="createtime")
    private Date createtime;

	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value="creator")
    private String creator;
	
	@ApiModelProperty(value = "备注")
	@Size(max=200,message="备注不能超过200个字符")
	@TableField(value="remark")
	private String remark;
	
	@ApiModelProperty(value = "单据库存类型")
	@TableField(value="invtype")
	private Integer invtype;
	
	@ApiModelProperty(value = "库存状态")
	@TableField(value="invstatus")
	private Integer invstatus;
	
	@ApiModelProperty(value = "发货日期")
	@TableField(value="shipping_date")
	private Date shippingDate;

	@ApiModelProperty(value = "transtype")
	@TableField(value="transtype")
    private String transtype;
	
	@ApiModelProperty(value = "发货类型")
	@TableField(value="shipping_solution")
	private String shippingSolution;
	
	@ApiModelProperty(value = "发货类型")
	@TableField(value="transtyle")
	private String transtyle;
	
	@ApiModelProperty(value = "配货批次")
	@TableField(value="check_inv")
	private BigInteger checkInv;
	
	@ApiModelProperty(value = "提交箱子信息")
	@TableField(value="submitbox")
	private Boolean submitbox;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "产品列表")
    private List<ShipInboundItem> planitemlist=new LinkedList<ShipInboundItem>();
	
	@ApiModelProperty(value = "亚马逊计划id")
	@TableField(value="inbound_pland_id")
	String inboundPlanId;
	 
	@ApiModelProperty(value = "收货地址方案id")
	@TableField(value="placement_option_id")
	String placementOptionId;

	@ApiModelProperty(value = "Transportation Token")
	@TableField(value="transportation_token")
	String transportationToken;
	
	@TableField(exist = false)
	List<String> shipmentids;

	@TableField(exist = false)
	ShipAddress source;
    public void addPlanitem(ShipInboundItem item) {
			  planitemlist.add(item);
		}
    
    
    public   Boolean NeedInv() {
		   return this.getInvtype()==null||this.getInvtype()==0||this.getInvtype()==1;
	   }
    
    public   Boolean NeedAmz() {
		   return this.getInvtype()==null||this.getInvtype()==0||this.getInvtype()==2;
	 }
}
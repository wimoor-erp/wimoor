package com.wimoor.amazon.inboundV2.pojo.entity;

 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value="ShipInboundShipment对象", description="货件")
@TableName("t_erp_ship_v2_inboundshipment")
public class ShipInboundShipment {
	@TableField(exist = false)
	public static final String ShipmentStatus_WORKING ="WORKING"; 
	@TableField(exist = false)
	public static final String ShipmentStatus_CANCELLED  ="CANCELLED"; 
	@TableField(exist = false)
	public static final String ShipmentStatus_SHIPPED  ="SHIPPED";
	@TableField(exist = false)
	public static final String ShipmentStatus_CLOSED  ="CLOSED";
	@TableField(exist = false)
	public static final String ShipmentStatus_ERROR  ="ERROR";
	@TableField(exist = false)
	public static final String ShipmentStatus_DELETED  ="DELETED";
	
	
	@ApiModelProperty(value = "货件ID【系统填写】")
	@TableId(value="shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "货件ID【系统填写】")
	@TableField(value="shipment_confirmation_id")
    private String shipmentConfirmationId;

	
	@ApiModelProperty(value = "亚马逊仓库中心代码【系统填写】")
	@TableField(value="destination")
    private String destination;

	@ApiModelProperty(value = "发货地址ID【系统填写】")
	@TableField(value="addressid")
    private String addressid;
	
	@ApiModelProperty(value = "货件状态【平台】【系统填写】")
	@TableField(value="shipmentstatus")
    private String shipmentstatus;
	
	@ApiModelProperty(value = "订单ID【系统填写】")
	@TableField(value="inboundplanid")
	private String inboundplanid;
	
	@ApiModelProperty(value = "订单ID【系统填写】")
	@TableField(value="formid")
	private String formid;
	
	@ApiModelProperty(value = "placementOptionId")
	@TableField(value="placement_option_id")
	private String placementOptionId;

	@ApiModelProperty(value = "transactionName")
	@TableField(value="transaction_name")
	private String transactionName;

	@ApiModelProperty(value = "transportationOptionId")
	@TableField(value="transportation_option_id")
	private String transportationOptionId;
	
	@ApiModelProperty(value = "deliveryWindowOptionId")
	@TableField(value="delivery_window_option_id")
	private String deliveryWindowOptionId;
	
	@ApiModelProperty(value = "货件名称")
	@TableField(value="name")
	private String name;

	@ApiModelProperty(value = "货件状态【本地】-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货")
	@TableField(value="status")
	private Integer status;
	
	@ApiModelProperty(value = "海外物流【来自表：t_erp_ship_config_carrier】")
	@TableField(value="carrier")
	private String carrier;
	
	@ApiModelProperty(value = "备注")
	@Size(max=500,message="备注不能超过500个字符")
	@TableField(value="remark")
	private String remark;
	
	@ApiModelProperty(value = "配送方式")
	@TableField(value="transtyle")
	private String  transtyle;
	
	@ApiModelProperty(value = "同步时间【系统填写】")
	@TableField(value="refreshtime")
	private Date refreshtime;
	
	@ApiModelProperty(value = "创建日期【系统填写】")
	@TableField(value="createdate")
	private Date createdate;
	
	@TableField(value="boxtime")
	private Date boxtime;
	
	@ApiModelProperty(value = "操作日期【系统填写】")
	@TableField(value="opttime")
	private Date opttime;
	
	@ApiModelProperty(value = "操作人【系统填写】")
	@TableField(value="operator")
	private String  operator;
	
	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value="creator")
	private String  creator;
	
	@ApiModelProperty(value = "发货日期【系统填写】")
	@TableField(value="shiped_date")
	private Date shipedDate;
	
	@ApiModelProperty(value = "关闭日期【系统填写】")
	@TableField(value="closed_date")
	private Date closedDate;
	
	@ApiModelProperty(value = "开始接受日期【系统填写】")
	@TableField(value="start_receive_date")
	private Date start_receive_date;
	
	@TableField(value="referenceid")
	private String referenceid;
	
	@ApiModelProperty(value = "transportStatus")
	@TableField(value="transport_status")
	private String transportStatus;
	
	@ApiModelProperty(value = "同步货件：1代表没有扣库存，2代表已经扣库存")
	@TableField(value="sync_inv")
	private Integer syncInv;

	@ApiModelProperty(value = "装运单位数")
	@TableField(value="totalunits")
	private Integer totalunits;
	
	@ApiModelProperty(value = "单位手工加工费")
	@TableField(value="feeunit")
	private BigDecimal feeunit;
	
	@ApiModelProperty(value = "装运的总手工加工费")
	@TableField(value="totalfee")
	private BigDecimal totalfee;
	
	@ApiModelProperty(value = "币种")
	@TableField(value="currency")
	private String currency;
	
	@TableField(value="check_inv")
    private BigInteger checkInv;
	
	@ApiModelProperty(value = "忽略异常")
	@TableField(value="ignorerec")
	private Boolean ignorerec;

	@ApiModelProperty(value = "是否报价")
	@TableField(value="isquote")
	private Boolean isquote;

    @TableField(exist = false)
	private List<ShipInboundItem> itemList=new ArrayList<ShipInboundItem>();
    
    @TableField(exist = false)
    private ShipInboundPlan inboundplan;
     
	public void addItem(ShipInboundItem item){
		itemList.add(item);
	}
	public int getItemSize(){
		return itemList.size();
	}
	public ShipInboundItem getItem(int index){
		return itemList.get(index);
	}
 
	public Integer getSyncInv() {
		if(syncInv==null)return 0;
		return syncInv;
	}
	 
	

  }
package com.wimoor.amazon.inbound.pojo.entity;

 

import java.math.BigDecimal;
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
@TableName("t_erp_ship_inboundshipment")
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

	@ApiModelProperty(value = "亚马逊仓库中心代码【系统填写】")
	@TableField(value="destinationfulfillmentcenterid")
    private String destinationfulfillmentcenterid;

	@ApiModelProperty(value = "发货地址ID【系统填写】")
	@TableField(value="shiptoaddressid")
    private String shiptoaddressid;
	
	@ApiModelProperty(value = "是否打标【系统填写】")
	@TableField(value="labelpreptype")
    private String labelpreptype;

	@ApiModelProperty(value = "货件状态【平台】【系统填写】")
	@TableField(value="shipmentstatus")
    private String shipmentstatus;
	
	@ApiModelProperty(value = "订单ID【系统填写】")
	@TableField(value="inboundplanid")
	private String inboundplanid;

	@ApiModelProperty(value = "SKU数量【系统填写】")
	@TableField(value="totalunits")
	private Integer  totalunits;
	
	@ApiModelProperty(value = "SKU数量【系统填写】")
	@TableField(value="feeperunit")
	private BigDecimal  feeperunit;
	
	@ApiModelProperty(value = "总费用")
	@TableField(value="totalfee")
	private BigDecimal  totalfee;
	
	@ApiModelProperty(value = "币种【系统填写】")
	@TableField(value="currency")
	private String  currency;
	
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
	
	@ApiModelProperty(value = "箱子数量")
	@TableField(value="boxnum")
	private Integer boxnum;
	
	@ApiModelProperty(value = "上一次箱子数量，判断箱子是否发生变化")
	@TableField(value="oldboxnum")
	private Integer oldboxnum;
	
	@ApiModelProperty(value = "配送方式")
	@TableField(value="transtyle")
	private String  transtyle;
	
	@ApiModelProperty(value = "同步时间【系统填写】")
	@TableField(value="refreshtime")
	private Date refreshtime;
	
	@ApiModelProperty(value = "创建日期【系统填写】")
	@TableField(value="createdate")
	private Date createdate;
	
	@ApiModelProperty(value = "操作日期【系统填写】")
	@TableField(value="opttime")
	private Date opttime;
	
	@ApiModelProperty(value = "操作人【系统填写】")
	@TableField(value="operator")
	private String  operator;
	
	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value="creator")
	private String  creator;
	
	@ApiModelProperty(value = "装箱提交ID【系统填写】")
	@TableField(value="submissionid_excel")
	private String  submissionidExcel;
	
	@ApiModelProperty(value = "装箱提交ID2【系统填写】")
	@TableField(value="submissionid")
	private String  submissionid;
	
	@ApiModelProperty(value = "装箱提交状态已经废弃【系统填写】")
	@TableField(value="feedstatus")
	private String   feedstatus;
	
	@ApiModelProperty(value = "发货日期【系统填写】")
	@TableField(value="shiped_date")
	private Date shipedDate;
	
	@ApiModelProperty(value = "开始接受日期【系统填写】")
	@TableField(value="start_receive_date")
	private Date start_receive_date;
	
	@ApiModelProperty(value = "驳回日期【系统填写】")
	@TableField(value="status0date")
	private Date status0date;
	
	@ApiModelProperty(value = "创建日期【系统填写】")
	@TableField(value="status1date")
	private Date status1date;
	
	@ApiModelProperty(value = "审核日期【系统填写】")
	@TableField(value="status2date")
	private Date status2date;
	
	@ApiModelProperty(value = "配货日期【系统填写】")
	@TableField(value="status3date")
	private Date status3date;
	
	@ApiModelProperty(value = "装箱日期【系统填写】")
	@TableField(value="status4date")
	private Date status4date;
	
	@ApiModelProperty(value = "发货日期")
	@TableField(value="status5date")
	private Date status5date;
	
	@ApiModelProperty(value = "完成日期【系统填写】")
	@TableField(value="status6date")
	private Date status6date;
	
	@TableField(value="pro_number")
	private String proNumber;
	
	@TableField(value="box_contents_source")
	private String intendedBoxContentsSource;
	
	@ApiModelProperty(value = "transportStatus")
	@TableField(value="transport_status")
	private String transportStatus;
	
	@ApiModelProperty(value = "同步货件：1代表没有扣库存，2代表已经扣库存")
	@TableField(value="sync_inv")
	private Integer syncInv;
	

	@ApiModelProperty(value = "忽略异常")
	@TableField(value="ignorerec")
	private Boolean ignorerec;
	
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
package com.wimoor.erp.ship.pojo.dto;

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
public class ShipAmazonShipmentDTO {
	 
	@ApiModelProperty(value = "货件ID[必填]")
	@TableId(value="shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "货件名称[非必填，审核货件时需要]")
	@TableField(value="name")
	private String name;
	
 
	@ApiModelProperty(value = "备注[非必填，审核货件时需要]")
	@Size(max=500,message="备注不能超过500个字符")
	@TableField(value="remark")
	private String remark;
	
	@ApiModelProperty(value = "箱子数量[非必填，仅在装箱时需要]")
	@TableField(value="boxnum")
	private Integer boxnum;
	
 	@ApiModelProperty(value = "配送方式[非必填，仅在装箱时需要]")
	@TableField(value="transtyle")
	private String  transtyle;
	
	@ApiModelProperty(value = "发货日期[非必填，仅在确认发货时]")
	@TableField(value="status5date")
	private Date status5date;
 
	@TableField(value="box_contents_source[非必填，仅在配货时需要]")
	private String intendedBoxContentsSource;
	
	@ApiModelProperty(value = "同步货件：1代表没有扣库存，2代表已经扣库存[非必填，仅在同步货件时需要]")
	@TableField(value="sync_inv")
	private Integer syncInv;

	@ApiModelProperty(value = "忽略异常[非必填，仅在忽略异常操作时需要]")
	@TableField(value="ignorerec")
	private Boolean ignorerec;
	
    @TableField(exist = false)
	private List<ShipInboundItemDTO> itemList=new ArrayList<ShipInboundItemDTO>();
 
	

  }
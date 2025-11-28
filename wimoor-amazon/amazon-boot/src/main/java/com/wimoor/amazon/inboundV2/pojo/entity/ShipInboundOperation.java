package com.wimoor.amazon.inboundV2.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ShipInboundOperation对象", description="操作结果同步")
@TableName("t_erp_ship_v2_inboundoperation")
public class ShipInboundOperation implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2429906861856659950L;
	@ApiModelProperty(value = "计划ID")
	@TableField(value= "formid")
    private String formid;
	
	@ApiModelProperty(value = "操作ID用于获取状态")
	@TableId(value = "operationid" )
    private String operationid;

	@ApiModelProperty(value = "操作类型")
    @TableField(value= "operation")
    private String operation;

	@ApiModelProperty(value = "操作状态")
    @TableField(value= "operation_status")
    private String operationStatus;

	@ApiModelProperty(value = "操作失败log")
    @TableField(value= "operation_problem")
    private String operationProblem;
 
	@ApiModelProperty(value = "操作时间")
    @TableField(value= "opttime")
	private Date opttime;


	public Boolean isRunOrSucces(){
		return "IN_PROGRESS".equals(operationStatus)||"SUCCESS".equals(operationStatus);
	}
}
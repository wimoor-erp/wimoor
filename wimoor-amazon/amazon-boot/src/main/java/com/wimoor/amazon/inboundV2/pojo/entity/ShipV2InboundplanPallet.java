package com.wimoor.amazon.inboundV2.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @TableName t_erp_ship_v2_inboundplan_pallet
 */
@Data
@TableName(value ="t_erp_ship_v2_inboundplan_pallet")
public class ShipV2InboundplanPallet extends BaseEntity {
    /**
     * 
     */
    @TableField(value= "shipmentid")
    private String shipmentid;
    @TableField(value= "formid")
    private String formid;

    /**
     * 
     */
    @TableField(value= "length")
    private BigDecimal length;

    /**
     * 
     */
    @TableField(value= "width")
    private BigDecimal width;

    /**
     * 
     */
    @TableField(value= "height")
    private BigDecimal height;

    /**
     * 
     */
    @TableField(value= "dim_unit")
    private String dimUnit;

    /**
     * 
     */
    @TableField(value= "weight")
    private BigDecimal weight;

    /**
     * 
     */
    @TableField(value= "weight_unit")
    private String weightUnit;

    /**
     * 
     */
    @TableField(value= "num")
    private Integer num;


}
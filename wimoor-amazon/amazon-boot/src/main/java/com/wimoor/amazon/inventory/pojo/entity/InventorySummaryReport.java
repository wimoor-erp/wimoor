package com.wimoor.amazon.inventory.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_rpt_inventory_summary")
@ApiModel(value="InventorySummaryReport对象", description="")
public class InventorySummaryReport extends BaseEntity {

    private static final long serialVersionUID=1L;

    private Date byday;

    private String fnsku;

    private String asin;

    private String msku;

    private String disposition;

    @TableField("startingWarehouseBalance")
    private Integer startingWarehouseBalance;

    @TableField("inTransitBetweenWarehouses")
    private String inTransitBetweenWarehouses;

    private Integer receipts;

    @TableField("customerShipments")
    private Integer customerShipments;

    @TableField("customerReturns")
    private Integer customerReturns;

    @TableField("vendorReturns")
    private Integer vendorReturns;

    @TableField("warehouseTransferInOut")
    private Integer warehouseTransferInOut;

    private Integer found;

    private Integer lost;

    private Integer damaged;

    private Integer disposed;

    @TableField("otherEvents")
    private String otherEvents;

    @TableField("endingWarehouseBalance")
    private Integer endingWarehouseBalance;

    @TableField("unknownEvents")
    private String unknownEvents;

    private String location;
    
    @TableField("authid")
    private String authid;

}

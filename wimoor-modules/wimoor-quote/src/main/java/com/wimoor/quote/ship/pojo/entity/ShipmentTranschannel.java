package com.wimoor.quote.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName t_shipment_transchannel
 */
@TableName(value ="t_shipment_transchannel")
@Data
public class ShipmentTranschannel {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Date opttime;

    /**
     * 
     */
    private String buyerid;

    /**
     * 
     */
    private Boolean disable;

    @ApiModelProperty(value = "shipmentTranschannel")
    @TableField(exist = false)
    private List<ShipmentSupplierTranschannel> supplierTranschannelList;
}
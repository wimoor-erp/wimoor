package com.wimoor.quote.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_shipment_supplier_transchannel
 */
@TableName(value ="t_shipment_supplier_transchannel")
@Data
public class ShipmentSupplierTranschannel {
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
    private String supplierid;

    /**
     *
     */
    private String buyerid;

    /**
     * 
     */
    private String channelid;

    /**
     * 
     */
    private Boolean disable;
}
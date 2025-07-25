package com.wimoor.quote.ship.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BasePageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName t_shipment_supplier_transchannel
 */
@TableName(value ="t_shipment_supplier_transchannel")
@Data
public class ShipmentSupplierTranschannelDTO extends BasePageQuery {
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
package com.wimoor.amazon.inbound.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_ship_inboundshipment_traceupload")
@ApiModel(value="TErpShipInboundshipmentTraceupload对象", description="")
public class ShipInboundshipmentTraceupload implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId
    private String shipmentid;
    
    private String shopid;

    private String creator;
 
    private String errormsg;
    @ApiModelProperty(value = "1,已处理，0，处理失败")
    private Integer status;

    private Date opttime;

    private Date createtime;


}

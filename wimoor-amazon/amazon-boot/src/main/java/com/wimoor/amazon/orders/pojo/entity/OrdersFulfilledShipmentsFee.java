package com.wimoor.amazon.orders.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_rpt_orders_fulfilled_shipments_fee")
@ApiModel(value="AmzRptOrdersFulfilledShipmentsFee对象", description="")
public class OrdersFulfilledShipmentsFee implements Serializable {

    private static final long serialVersionUID=1L;

    private String amazonauthid;

    private String marketplaceid;

      private String amazonOrderId;

    private String shipmentid;
    
    private String itemid;

    private String sku;

    private Date shipmentDate;

    private Date reportingDate;

    private Date paymentsDate;

    private Date purchaseDate;

    private Integer quantity;

    private BigDecimal unitcost;

    private BigDecimal unittransfee;


}

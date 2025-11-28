package com.wimoor.quote.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="QuoteOrderShipment", description="报价订单货件")
@TableName("t_order_shipment")
public class QuoteOrderShipment {
    private String orderid;
    private String shipmentid;
}

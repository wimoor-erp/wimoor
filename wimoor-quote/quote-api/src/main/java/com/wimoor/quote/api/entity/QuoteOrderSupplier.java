package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="QuoteOrderShipment", description="报价订单货件")
@TableName("t_order_supplier")
public class QuoteOrderSupplier {
    private String orderid;
    private String supplierid;
    private BigDecimal base;
    private Integer status;
    private Date opttime;
}

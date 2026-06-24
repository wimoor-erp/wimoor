package com.wimoor.manager.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 客户订单列表DTO（包含联表查询字段）
 */
@Data
public class CustomerOrderListDTO {

    private BigInteger id;

    private BigInteger shopid;

    private String shopName;

    private String ftype;

    private String outTradeNo;

    private String subject;

    private String tradeNo;

    private BigDecimal totalAmount;

    private String discountnumber;

    private LocalDateTime paytime;

    private String paystatus;

    private String ivcstatus;

    private Integer months;

    private Integer tariffpackage;

    private String packageName;

    private Integer pcs;

    private String paytype;

    private Boolean disable;

    private Boolean hasInvoiceRequest;

    private String location;
}
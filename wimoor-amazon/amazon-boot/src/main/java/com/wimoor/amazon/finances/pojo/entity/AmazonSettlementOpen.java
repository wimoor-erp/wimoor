package com.wimoor.amazon.finances.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amz_settlement_open")
@ApiModel(value="AmazonSettlementOpen对象", description="")
public class AmazonSettlementOpen implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private BigInteger id;
    String amazonauthid;
    private String groupId;
    private String settlementId;
    private Date postedDate;
    private String amazonOrderId;
    private String orderItemId;
    private String marketplaceName;
    private String accountType;
    private String fulfillment;
    private String eventType;
    private String sku;
    private String ftype;
    private String currency;
    private BigDecimal amount;
    private Integer quantity;
    private Date createTime; 
}
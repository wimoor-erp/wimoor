package com.wimoor.erp.purchase.alibaba.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 还款明细列表
 * </p>
 *
 * @author wimoor team
 * @since 2023-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_alibaba_settlement_pay")
@ApiModel(value="PurchaseAlibabaSettlementPay对象", description="还款明细列表")
public class PurchaseAlibabaSettlementPay implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String settlementid;

    private Date paytime;

    private String paymethod;

    private String paytype;

    private BigDecimal amount;


}

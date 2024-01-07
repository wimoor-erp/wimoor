package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_alibaba_settlement_order_return")
@ApiModel(value="PurchaseAlibabaSettlementDetail对象", description="")
public class PurchaseAlibabaSettlementOrderReturn implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId
    private String orderid;

    private String settlementid;
    
    private Date returntime;

    private String name;
    
    private BigDecimal payamount;
    
    private BigDecimal returnamount;
    
    private String returntype;
    
    private String returnto;

}

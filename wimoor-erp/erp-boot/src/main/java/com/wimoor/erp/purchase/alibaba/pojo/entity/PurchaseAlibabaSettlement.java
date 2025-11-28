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
@TableName("t_erp_purchase_alibaba_settlement")
@ApiModel(value="PurchaseAlibabaSettlement对象", description="")
public class PurchaseAlibabaSettlement implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId
    private String id;

    private String shopid;
    private String acct;

    private String alibabaAccount;

    private BigDecimal amount;

    private Integer quantity;

    private Date postdate;

    private Date paydate;

    private BigDecimal payamount;
    private BigDecimal returnamount;
    private BigDecimal payreturnamount;
    
    private Integer paytimes;
    private Integer returntimes;

    private Date loaddate;

    private String remark;

    private Date opttime;


}

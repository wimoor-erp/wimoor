package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_form_entry_alibabainfo")
@ApiModel(value="ErpPurchaseFormEntryAlibabainfo对象", description="")
public class ErpPurchaseFormEntryAlibabainfo implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger entryid;

    private BigInteger alibabaAuth;

    private BigInteger alibabaOrderid;

    private String logisticsInfo;

    private String logisticsTraceInfo;

    private String orderInfo;

    private Boolean logisticsStatus;

    private Boolean logisticsTraceStatus;

    private String orderStatus;

    private LocalDateTime orderRefreshTime;

    private LocalDateTime logisticsRefreshTime;

    private LocalDateTime logisticsTraceRefreshTime;


}

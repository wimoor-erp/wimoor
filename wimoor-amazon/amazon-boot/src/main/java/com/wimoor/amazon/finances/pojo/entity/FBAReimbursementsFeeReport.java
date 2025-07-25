package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
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
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_fba_reimbursements_fee_report")
@ApiModel(value="FBAReimbursementsFeeReport对象", description="")
public class FBAReimbursementsFeeReport extends BaseEntity{

    private static final long serialVersionUID=1L;

    private Date approvalDate;

    private String reimbursementId;

    private String caseId;

    private String amazonOrderId;

    @ApiModelProperty(value = "原因")
    private String reason;

    private String sku;

    private String fnsku;

    private String asin;

    private String conditions;

    private String currencyUnit;

    private BigDecimal amountPerUnit;

    private BigDecimal amountTotal;

    private Integer quantityReimbursedCash;

    private Integer quantityReimbursedInventory;

    private Integer quantityReimbursedTotal;

    private String originalReimbursementId;

    private String originalReimbursementType;

    private String amazonauthid;
    
    private String marketplaceid;

    private Date lastupdate;


}

package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
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
 * @since 2022-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_fin_account")
@ApiModel(value="AmzFinAccount对象", description="")
public class AmzFinAccount implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField(value="amazonAuthid")
    private BigInteger amazonAuthid;

    @TableField(value="currency")
    private String currency;

    @TableField(value="groupid")
    private String groupid;

    @TableField(value="financial_event_group_start")
    private LocalDateTime financialEventGroupStart;

    @TableField(value="financial_event_group_end")
    private LocalDateTime financialEventGroupEnd;

    @TableField(value="fund_transfer_date")
    private LocalDateTime fundTransferDate;

    @TableField(value="processing_status")
    private String processingStatus;

    @TableField(value="trace_id")
    private String traceId;

    @TableField(value="account_tail")
    private String accountTail;

    @TableField(value="converted_total")
    private BigDecimal convertedTotal;

    @TableField(value="beginning_balance")
    private BigDecimal beginningBalance;

    @TableField(value="original_total")
    private BigDecimal originalTotal;

    @TableField(value="fund_transfer_status")
    private String fundTransferStatus;
    
    @TableField(value="refreshtime")
    private Date refreshtime;
    
    @TableField(value="nexttoken")
    private String nexttoken;
}

package com.wimoor.finance.voucher.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 凭证修改日志对象 fin_voucher_modify_log
 * 
 * @author wimoor
 * @date 2025-11-22
 */
@Data
public class FinVoucherModifyLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String logId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String groupid;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String voucherId;

    /** 修改类型：1-新增，2-修改，3-删除 */
    @Excel(name = "修改类型：1-新增，2-修改，3-删除")
    private Integer modifyType;

    /** 修改前数据 */
    @Excel(name = "修改前数据")
    private String beforeData;

    /** 修改后数据 */
    @Excel(name = "修改后数据")
    private String afterData;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String modifyBy;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date modifyTime;

    /** 修改原因 */
    @Excel(name = "修改原因")
    private String reason;



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("groupid", getGroupid())
            .append("voucherId", getVoucherId())
            .append("modifyType", getModifyType())
            .append("beforeData", getBeforeData())
            .append("afterData", getAfterData())
            .append("modifyBy", getModifyBy())
            .append("modifyTime", getModifyTime())
            .append("reason", getReason())
            .toString();
    }
}

package com.wimoor.finance.closing.domain;

import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class TransferSummary {

    /**
     * 结转科目数量
     */
    private Integer subjectCount = 0;

    /**
     * 结转总金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 结转详情列表
     */
    private List<TransferDetail> details = new ArrayList<>();

    // 业务方法

    public void addTransferDetail(TransferDetail detail) {
        this.details.add(detail);
        this.subjectCount++;
        this.totalAmount = this.totalAmount.add(detail.getTransferAmount());
    }

    public void addSubject(FinAccountingSubjects subject, BigDecimal amount) {
        TransferDetail detail = new TransferDetail();
        detail.setSubjectId(subject.getSubjectId());
        detail.setSubjectCode(subject.getSubjectCode());
        detail.setSubjectName(subject.getSubjectName());
        detail.setSubjectType(SubjectType.valueOf(subject));
        detail.setTransferAmount(amount);
        detail.setTransferTime(new Date());

        addTransferDetail(detail);
    }

    /**
     * 获取平均结转金额
     */
    public BigDecimal getAverageAmount() {
        if (subjectCount == 0) {
            return BigDecimal.ZERO;
        }
        return totalAmount.divide(BigDecimal.valueOf(subjectCount), 2, RoundingMode.HALF_UP);
    }
}
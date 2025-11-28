package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RestoredSubjectInfo {
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private BigDecimal restoredBalance;
}
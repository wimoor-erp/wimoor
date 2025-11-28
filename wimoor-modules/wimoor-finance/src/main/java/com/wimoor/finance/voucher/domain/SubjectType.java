package com.wimoor.finance.voucher.domain;

import com.wimoor.finance.setting.domain.FinAccountingSubjects;

/**
 * 科目类型枚举
 */
public enum SubjectType {
    ASSET("资产类", 1),           // 默认借方余额
    LIABILITY("负债类", 2),      // 默认贷方余额
    EQUITY("权益类", 2),         // 默认贷方余额
    COST("成本类", 1),           // 默认借方余额
    INCOME("收入类", 2),         // 默认贷方余额
    EXPENSE("费用类", 1);        // 默认借方余额

    private final String name;
    private final Integer defaultDirection; // 1-借, 2-贷
    SubjectType(String name, Integer defaultDirection ) {
        this.name = name;
        this.defaultDirection = defaultDirection;
    }
    public Integer getDefaultDirection() {
        return defaultDirection;
    }
    public  static SubjectType valueOf(FinAccountingSubjects subject) {
        if(subject.getSubjectId()==1){
            return SubjectType.ASSET;
        }
        if(subject.getSubjectId()==2){
            return SubjectType.LIABILITY;
        }
        if(subject.getSubjectId()==3){
            return SubjectType.EQUITY;
        }
        if(subject.getSubjectId()==4){
            return SubjectType.COST;
        }
        if(subject.getSubjectId()==5){
            if(subject.getDirection()==1){
                return SubjectType.EXPENSE;
            }else{
                return SubjectType.INCOME;
            }
        }
        return null;
    }
    public boolean isDebitBalance() {
        return defaultDirection == 1;
    }
}
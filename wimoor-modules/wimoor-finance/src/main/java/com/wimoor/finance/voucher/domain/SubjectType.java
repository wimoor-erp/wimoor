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
        if(subject.getSubjectType()==null){
            return null;
        }
        int type = subject.getSubjectType();
        // 一级类别：1-资产，2-负债，3-权益，4-成本，5-损益
        if(type==1){
            return SubjectType.ASSET;
        }
        if(type==2){
            return SubjectType.LIABILITY;
        }
        if(type==3){
            return SubjectType.EQUITY;
        }
        if(type==4){
            return SubjectType.COST;
        }
        if(type==5){
            // 损益类根据方向区分收入和费用
            if(subject.getDirection()==1){
                return SubjectType.EXPENSE;
            }else{
                return SubjectType.INCOME;
            }
        }
        // 二级类别：6-7资产，8-9负债，10权益，11成本，12-18损益
        if(type==6 || type==7){
            return SubjectType.ASSET;
        }
        if(type==8 || type==9){
            return SubjectType.LIABILITY;
        }
        if(type==10){
            return SubjectType.EQUITY;
        }
        if(type==11){
            return SubjectType.COST;
        }
        if(type>=12 && type<=18){
            // 12-营业收入(贷方)，13-其他收益(贷方) -> INCOME
            // 14-期间费用，15-其他损失，16-营业成本及税金，17-以前年度损益调整，18-所得税 -> EXPENSE
            if(type==12 || type==13){
                return SubjectType.INCOME;
            }else{
                return SubjectType.EXPENSE;
            }
        }
        return null;
    }
    public boolean isDebitBalance() {
        return defaultDirection == 1;
    }
}
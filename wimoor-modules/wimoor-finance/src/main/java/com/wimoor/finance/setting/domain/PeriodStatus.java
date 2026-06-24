package com.wimoor.finance.setting.domain;

public enum PeriodStatus {
    NOT_OPEN(1,"未开启"),
    OPEN(2,"已开启"),
    CLOSED(3,"已关闭");

    private final Integer value;
    private final String name;
    PeriodStatus(Integer value, String info) {
        this.value = value;
        this.name = info;
    }
    public Integer getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
}

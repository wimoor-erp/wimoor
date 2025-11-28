package com.wimoor.finance.setting.domain;

public enum PeriodStatus {
    OPEN(1,"开启"),
    CLOSED(2,"关闭");

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

package com.wimoor.finance.closing.domain;

public enum CheckLevel {

    /**
     * 信息级别 - 仅用于展示
     */
    INFO(1, "信息"),

    /**
     * 警告级别 - 不影响结账，但需要注意
     */
    WARNING(2, "警告"),

    /**
     * 错误级别 - 阻止结账进行
     */
    ERROR(3, "错误");

    private final Integer level;
    private final String description;

    CheckLevel(Integer level, String description) {
        this.level = level;
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判断是否比指定级别严重
     */
    public boolean isMoreSevereThan(CheckLevel other) {
        return this.level > other.level;
    }
}

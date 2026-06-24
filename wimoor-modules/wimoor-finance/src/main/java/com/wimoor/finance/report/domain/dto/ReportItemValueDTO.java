package com.wimoor.finance.report.domain.dto;

import java.math.BigDecimal;

public class ReportItemValueDTO {
    private String itemCode;
    private String itemName;
    private Integer itemLevel;
    private Integer lineNumber;
    private String itemType;
    private String displayFormat;
    private BigDecimal amount;
    private BigDecimal yearBeginAmount;
    private BigDecimal comparisonAmount;
    private BigDecimal changeAmount;
    private BigDecimal changeRate;
    private Boolean isLeaf;
    /** 本年累计金额 */
    private BigDecimal yearAmount;
    /** 本月金额 */
    private BigDecimal monthAmount;

    // Getters and Setters
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getItemLevel() { return itemLevel; }
    public void setItemLevel(Integer itemLevel) { this.itemLevel = itemLevel; }

    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getDisplayFormat() { return displayFormat; }
    public void setDisplayFormat(String displayFormat) { this.displayFormat = displayFormat; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getYearBeginAmount() { return yearBeginAmount; }
    public void setYearBeginAmount(BigDecimal yearBeginAmount) { this.yearBeginAmount = yearBeginAmount; }

    public BigDecimal getComparisonAmount() { return comparisonAmount; }
    public void setComparisonAmount(BigDecimal comparisonAmount) { this.comparisonAmount = comparisonAmount; }

    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }

    public BigDecimal getChangeRate() { return changeRate; }
    public void setChangeRate(BigDecimal changeRate) { this.changeRate = changeRate; }

    public Boolean getIsLeaf() { return isLeaf; }
    public void setIsLeaf(Boolean isLeaf) { this.isLeaf = isLeaf; }

    public BigDecimal getYearAmount() { return yearAmount; }
    public void setYearAmount(BigDecimal yearAmount) { this.yearAmount = yearAmount; }

    public BigDecimal getMonthAmount() { return monthAmount; }
    public void setMonthAmount(BigDecimal monthAmount) { this.monthAmount = monthAmount; }
}
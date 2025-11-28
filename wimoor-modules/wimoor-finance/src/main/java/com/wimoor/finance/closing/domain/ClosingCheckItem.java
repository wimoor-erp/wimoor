package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ClosingCheckItem {
    private String checkItem;
    private Boolean passed;
    private String message;
    private CheckLevel checkLevel;
    private Map<String,Object> relatedData;
    private String suggestion;
    private Date checkTime;



    public ClosingCheckItem(String checkItem, Boolean passed, String message, CheckLevel checkLevel) {
        this.checkItem = checkItem;
        this.passed = passed;
        this.message = message;
    }

    public ClosingCheckItem(String checkItem, Boolean passed, String message) {
        this.checkItem = checkItem;
        this.passed = passed;
        this.message = message;
    }

    public ClosingCheckItem() {

    }

    public void addRelatedData(String key, Object issues) {
        if (relatedData == null) {
            relatedData = new HashMap<>();
        }
        relatedData.put(key, issues);
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestion() {
        return suggestion;
    }
}

package com.wimoor.data.pojo.entity;

import lombok.Data;

import java.util.List;

@Data
public class DataQuery {
    private List<String> field;
    private String database;
    private String tableName;
}

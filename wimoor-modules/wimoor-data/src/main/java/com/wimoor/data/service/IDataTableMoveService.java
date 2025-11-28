package com.wimoor.data.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IDataTableMoveService {
    void moveSettlement();
    public void moveTableByDay(String database,String table, String datefield, Date enddate,String fieldwhere,Map<String,Integer> sumlineMap);
    void addTask(Map<String, Object> map);
    void move(String database);
    void clear(String database);
    void addSettlement(List<Map<String, Object>> needMove);
}

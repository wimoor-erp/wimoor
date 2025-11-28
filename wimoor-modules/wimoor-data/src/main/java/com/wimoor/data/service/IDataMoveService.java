package com.wimoor.data.service;

import java.util.Map;

public interface IDataMoveService {
    void clear(String database);
    void move(String database);
    void addTask(Map<String, Object> map);
}

package com.wimoor.data.service;

import java.util.List;
import java.util.Map;

public interface IDataService {
    public List<Map<String, Object>> queryLocal(String sql);
    public List<Map<String, Object>> queryRemote(String sql);
    public void insertLog(String logs);
    public void updateLocal(String sql);
}

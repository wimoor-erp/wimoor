package com.wimoor.data.service.impl;
import com.wimoor.data.config.DataSource;
import com.wimoor.data.config.DataSourceNames;
import com.wimoor.data.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements IDataService {
    @Autowired
    JdbcTemplate jdbcTemplate;



    @Override
    @DataSource(name = DataSourceNames.slave)
    public List<Map<String, Object>> queryLocal(String sql) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @Override
    @DataSource(name = DataSourceNames.master)
    public List<Map<String, Object>> queryRemote(String sql) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @Override
    @DataSource(name = DataSourceNames.slave)
    public void updateLocal(String sql) {
         jdbcTemplate.update(sql);
    }

    @DataSource(name = DataSourceNames.slave)
    public void insertLog(String logs) {
        String sql = "INSERT INTO db_logs.t_logs ( `logs`)  VALUES('"+logs+"')";
        jdbcTemplate.update(sql);
    }

}

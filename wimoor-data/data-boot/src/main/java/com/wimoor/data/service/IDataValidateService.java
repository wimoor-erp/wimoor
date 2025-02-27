package com.wimoor.data.service;

import com.wimoor.data.pojo.entity.DataQuery;

public interface IDataValidateService {
    DataQuery validateField(String database, String tableName);
}

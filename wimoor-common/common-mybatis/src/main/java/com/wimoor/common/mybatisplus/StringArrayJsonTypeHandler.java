package com.wimoor.common.mybatisplus;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
 
@Component
@MappedTypes(value = {String[].class})
@MappedJdbcTypes(value = {JdbcType.OTHER}, includeNullJdbcType = true)
public class StringArrayJsonTypeHandler extends ArrayObjectJsonTypeHandler<String> {
    public StringArrayJsonTypeHandler() {
        super(String[].class);
    }
}

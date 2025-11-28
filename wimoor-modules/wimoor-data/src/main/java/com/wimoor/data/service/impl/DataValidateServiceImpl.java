package com.wimoor.data.service.impl;

import com.wimoor.data.pojo.entity.DataQuery;
import com.wimoor.data.service.IDataService;
import com.wimoor.data.service.IDataValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataValidateServiceImpl implements IDataValidateService {
    @Autowired
    IDataService iDataService;

   String getFieldSql(String database, String tableName){
       String sql= "SELECT COLUMN_NAME FROM information_schema.COLUMNS ";
       sql=sql+"WHERE TABLE_SCHEMA = '"+database+"' AND TABLE_NAME = '"+tableName+"'";
       return sql;
    }
    String getCountSql(String database, String tableName){
        String sql= "SELECT count(0) num FROM  "+database+"."+tableName;
        return sql;
    }
   Boolean validateField(List<Map<String, Object>> fieldList, List<Map<String, Object>> field2List){
       Set<String> fieldSet = fieldList.stream().map(map -> map.get("COLUMN_NAME").toString()).collect(Collectors.toSet());
       Set<String> field2Set = field2List.stream().map(map -> map.get("COLUMN_NAME").toString()).collect(Collectors.toSet());
       return fieldSet.containsAll(field2Set)&&field2Set.containsAll(fieldSet);
   }
    @Override
    public DataQuery validateField(String database, String tableName) {
        if (database != null && tableName != null) {
            String fieldNameSql = getFieldSql(database, tableName);
            List<Map<String, Object>> fieldList = iDataService.queryRemote(fieldNameSql);
            List<Map<String, Object>> field2List = iDataService.queryLocal(fieldNameSql);
            List<Map<String, Object>> remoteCount = iDataService.queryRemote(getCountSql(database, tableName));
            List<Map<String, Object>> localCount = iDataService.queryLocal(getCountSql(database, tableName));
            if(remoteCount!=null
                    &&localCount!=null
                    &&remoteCount.size()==1
                    &&localCount.size()==1
                    &&remoteCount.get(0).get("num")!=null
                    &&localCount.get(0).get("num")!=null
                    &&Integer.parseInt(remoteCount.get(0).get("num").toString())==Integer.parseInt(localCount.get(0).get("num").toString())){
                return null;
            }
            if(!validateField(fieldList, field2List)){
                iDataService.insertLog("数据表："+database+"."+tableName+"字段不匹配"+fieldList.toString()+"--"+field2List.toString());
                return null;
            }
            if(remoteCount!=null
                    &&localCount!=null
                    &&remoteCount.size()==1
                    &&localCount.size()==1
                    &&remoteCount.get(0).get("num")!=null
                    &&localCount.get(0).get("num")!=null
                    &&Integer.parseInt(remoteCount.get(0).get("num").toString())<=Integer.parseInt(localCount.get(0).get("num").toString())){
                iDataService.updateLocal("truncate table "+database+"."+tableName);
            }
            DataQuery dataQuery = new DataQuery();
            dataQuery.setDatabase(database);
            dataQuery.setTableName(tableName);
            List<String> fields = fieldList.stream().map(map -> map.get("COLUMN_NAME").toString()).collect(Collectors.toList());
            dataQuery.setField(fields);
            return dataQuery;
        }
        return null;
    }
}

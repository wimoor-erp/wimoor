package com.wimoor.data.service.impl;

import com.wimoor.data.pojo.entity.DataQuery;
import com.wimoor.data.service.IDataMoveService;
import com.wimoor.data.service.IDataService;
import com.wimoor.data.service.IDataValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataMoveServiceImpl implements IDataMoveService {
    @Autowired
    IDataService iDataService;
    @Autowired
    IDataValidateService iDataValidateService;

    public void clear(String database){
        String sql = " delete  FROM db_datamove.t_database_table where database_name = '"+database+"'";
        iDataService.updateLocal(sql);
    }
    @Override
    public void move(String database) {
           String sql = " SELECT database_name `database_name`,table_name `table_name`,download_size `download_size` FROM db_datamove.t_database_table WHERE database_name = '"+database+"' and opttime>DATE_SUB(NOW(),INTERVAL  7 DAY) and enddate is null   ORDER BY table_size asc";
           List<Map<String, Object>> list = iDataService.queryLocal(sql);
           AtomicInteger i= new AtomicInteger(1);
           for(Map<String, Object> map:list){
               String tablename=map.get("table_name").toString();
               Integer download_size=map.get("download_size")!=null?Integer.parseInt(map.get("download_size").toString()):0;
               try{
                       System.out.println(tablename+"---------------index:"+ i.getAndIncrement());
                       iDataService.updateLocal("update db_datamove.t_database_table set opttime=now(),startdate=now(),enddate=null where database_name = '"+database+"' and table_name='"+tablename+"'");
                       int number=moveTable(database,tablename,download_size);
                       iDataService.updateLocal("update db_datamove.t_database_table set  enddate=now()  where database_name = '"+database+"' and table_name='"+tablename+"'");
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
    }

    @Override
    public void addTask(Map<String, Object> map) {
        String database=map.get("database_name").toString();
        String tablename=map.get("table_name").toString();
        String table_size=map.get("table_size").toString();
        Integer download_size=map.get("download_size")!=null?Integer.parseInt(map.get("download_size").toString()):0;
        iDataService.updateLocal("insert ignore into db_datamove.t_database_table (database_name,table_name,table_size,download_size,opttime) values ('"+database+"','"+tablename+"',"+table_size+","+download_size+",now())");
    }

    private Integer moveTable(String database, String tableName,Integer download_size){
        DataQuery query= iDataValidateService.validateField(database,tableName);
        int number=0;
        if(query!=null){
            Integer page=download_size;
            iDataService.updateLocal("TRUNCATE  TABLE "+database+"."+tableName);
            while(true) {
                String sql = "select * from " + database + "." + tableName + " limit " + (page * 1000) + ",1000";
                page = page + 1;
                try {
                    List<Map<String, Object>> list = iDataService.queryRemote(sql);
                    System.out.println("Table:" + tableName + "  pagesize:" + page + "  success");
                    if (list == null || list.size() == 0) {
                        break;
                    } else {
                        iDataService.updateLocal("update db_datamove.t_database_table set download_size=" + number + " where database_name = '" + database + "' and table_name='" + tableName + "'");
                    }
                    number = number + list.size();
                    String insertSql = "insert ignore into " + database + "." + tableName + " ";
                    insertSql = insertSql + " (";
                    for (String key : list.get(0).keySet()) {
                        insertSql += "`" + key + "`,";
                    }
                    insertSql = insertSql.substring(0, insertSql.length() - 1);
                    insertSql += ") values ";
                    for (Map<String, Object> map : list) {
                        insertSql = insertSql + " (";
                        for (String key : map.keySet()) {
                            if (map.get(key) != null) {
                                if (map.get(key) instanceof String) {
                                    String value = map.get(key).toString();
                                    if (value.contains("'")) {
                                        value = value.replaceAll("'", "\\\\'");
                                    }
                                    if (value.contains("\\")) {
                                        value = value.replaceAll("\\\\", "\\\\");
                                    }
                                    insertSql += "'" + value + "',";
                                    if (value.contains("\\',")) {
                                        value = value.replaceAll("\\',", "',");
                                    }
                                } else if (map.get(key) instanceof Integer) {
                                    insertSql += Integer.parseInt(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof Long) {
                                    insertSql += Long.parseLong(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof Double) {
                                    insertSql += Double.parseDouble(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof Float) {
                                    insertSql += Float.parseFloat(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof Boolean) {
                                    insertSql += Boolean.parseBoolean(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof BigDecimal) {
                                    insertSql += new BigDecimal(map.get(key).toString()) + ",";
                                } else if (map.get(key) instanceof java.util.Date) {
                                    insertSql += "'" + map.get(key) + "',";
                                } else if (map.get(key) instanceof BigInteger) {
                                    insertSql += new BigInteger(map.get(key).toString()) + ",";
                                } else {
                                    insertSql += "'" + map.get(key) + "',";
                                }
                            } else {
                                insertSql += "null,";
                            }

                        }
                        insertSql = insertSql.substring(0, insertSql.length() - 1);
                        insertSql += "),";
                    }

                    iDataService.updateLocal(insertSql.substring(0, insertSql.length() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return number;
    }
}

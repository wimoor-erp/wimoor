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

@Service
public class DataMoveServiceImpl implements IDataMoveService {
    @Autowired
    IDataService iDataService;
    @Autowired
    IDataValidateService iDataValidateService;
    public Set<String> getExcludeTable(String database){
        Set<String> excloudKeys=new HashSet<String>();
        List<Map<String, Object>> list = iDataService.queryLocal("select * from db_logs.t_large_table where `database`='" + database + "'");
        for(Map<String, Object> map:list){
                excloudKeys.add(map.get("name").toString());
        }
        return excloudKeys;
    }
    @Override
    public void move(String database, String tableName) {
        if(tableName==null){
           String sql=" SELECT TABLE_NAME FROM db_logs.t_database_table WHERE database_name = '"+database+"' and opttime<DATE_SUB(NOW(),INTERVAL  7 DAY)  ORDER BY table_size asc";
           Set<String>  excloudKeys=getExcludeTable(database);
           List<Map<String, Object>> list = iDataService.queryLocal(sql);
           int i=1;
            System.out.println("Table---------------number:"+list.size());
           for(Map<String, Object> map:list){
               String tablename=map.get("TABLE_NAME").toString();
               if(excloudKeys.contains(tablename)){
                   continue;
               }
               System.out.println(tablename+"---------------index:"+i++);
               moveTable(database,tablename);
               iDataService.updateLocal("update db_logs.t_database_table set opttime=now() where database_name = '"+database+"' and table_name='"+tablename+"'");
           }

        }else{
            moveTable(database,tableName);
        }

    }
    private void moveTable(String database, String tableName){
        DataQuery query= iDataValidateService.validateField(database,tableName);
        if(query!=null){
            Integer page=0;
            while(true){
                String sql = "select * from "+database+"."+tableName +" limit "+(page*2000)+",2000";
                page=page+1;
                List<Map<String, Object>> list = iDataService.queryRemote(sql);
                if(list==null||list.size()==0){
                    System.out.println("Table:"+tableName+"  pagesize:"+page*2000+"  success");
                    break;
                }
                String insertSql = "replace into "+database+"."+tableName+" ";
                insertSql=insertSql +" (";
                for(String key:list.get(0).keySet()){
                    insertSql+="`"+key+"`,";
                }
                insertSql= insertSql.substring(0,insertSql.length()-1);
                insertSql+= ") values ";
                for(Map<String, Object> map:list){
                    insertSql=insertSql +" (";
                    for(String key:map.keySet()){
                        if(map.get(key)!=null){
                            if(map.get(key) instanceof String){
                                String value = map.get(key).toString();
                                if(value.contains("'")){
                                    value = value.replaceAll("'","\\\\'");
                                }
                                insertSql+="'"+value+"',";
                            }else if(map.get(key) instanceof Integer){
                                insertSql+=Integer.parseInt(map.get(key).toString())+",";
                            }else if(map.get(key) instanceof Long){
                                insertSql+=Long.parseLong(map.get(key).toString())+",";
                            }else if(map.get(key) instanceof Double){
                                insertSql+=Double.parseDouble(map.get(key).toString())+",";
                            }else if(map.get(key) instanceof Float){
                                insertSql+=Float.parseFloat(map.get(key).toString())+",";
                            }else if(map.get(key) instanceof Boolean){
                                insertSql+=Boolean.parseBoolean(map.get(key).toString())+",";
                            }else if (map.get(key) instanceof BigDecimal){
                                insertSql+=new BigDecimal(map.get(key).toString())+",";
                            }else if (map.get(key) instanceof java.util.Date){
                                insertSql+="'"+map.get(key)+"',";
                            }else if(map.get(key) instanceof BigInteger){
                                insertSql+=new BigInteger(map.get(key).toString())+",";
                            }else {
                                insertSql+="'"+map.get(key)+"',";
                            }
                        }else{
                            insertSql+="null,";
                        }

                    }
                    insertSql= insertSql.substring(0,insertSql.length()-1);
                    insertSql+="),";
                }
                iDataService.updateLocal(insertSql.substring(0,insertSql.length()-1));
            }

        }
    }
}

package com.wimoor.data.controller;


import cn.hutool.core.util.StrUtil;
import com.wimoor.data.controller.result.Result;
import com.wimoor.data.service.IDataMoveService;
import com.wimoor.data.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {
    @Autowired
    IDataMoveService iDataMoveService;
    @Autowired
    IDataService iDataService;

    @GetMapping("/showDataBase")
    public Result<?> showDataBase() {
        List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> list = iDataService.queryRemote("SHOW DATABASES;");
        for(Map<String, Object> map:list){
            if(map.get("Database")!=null&&map.get("Database").toString().indexOf("mysql")!=-1){
                continue;
            }
            if(map.get("Database")!=null&&map.get("Database").toString().indexOf("__recycle_bin__")!=-1){
                continue;
            }
            if(map.get("Database")!=null&&map.get("Database").toString().indexOf("sys")!=-1){
                continue;
            }
            if(map.get("Database")!=null&&map.get("Database").toString().indexOf("information_schema")!=-1){
                continue;
            }
            if(map.get("Database")!=null&&map.get("Database").toString().indexOf("performance_schema")!=-1){
                continue;
            }
            result.add(map);
        }
        return Result.success(result);
    }

    @GetMapping("/showTable")
    public Result<?> showTables(String database,String tableSearchName ) {
        String query="";
        if(StrUtil.isNotBlank(tableSearchName)){
             query=" and t.TABLE_NAME like '%"+tableSearchName.trim()+"%'";
        }
        return Result.success(iDataService.queryRemote(
                 "    SELECT `TABLE_NAME` `name`,`table_rows` `rows`, " +
                     "    ROUND(data_length / 1024 / 1024, 2) AS data_length_mb, " +
                     "    ROUND(data_length / 1024 / 1024 /1024, 2) AS data_length_gb," +
                     "    create_time createtime,update_time updatetime," +
                     "    table_comment tablecomment  " +
                     "    FROM information_schema.tables t  " +
                     "    WHERE table_schema = '"+database+"' "+query+" order by data_length desc;"));
    }


    @PostMapping("/taskAdd/{database}")
    public Result<?> remote(@PathVariable("database") String database, @RequestBody List<Map<String,Object>> request) throws InterruptedException {
        iDataMoveService.clear(database);
        for(Map<String,Object> map : request) {
            iDataMoveService.addTask(map);
        }
        new Thread(()->{
             iDataMoveService.move(database);
        }).start();
        return Result.success("add task success");
    }

    @GetMapping("/showtask")
    public Result<?> showtask(String database) throws InterruptedException {
        return Result.success(iDataService.queryLocal("select * from db_datamove.t_database_table where database_name = '"+database+"'"));
    }


    @GetMapping("/startTask")
    public Result<?> startTask(String database) throws InterruptedException {
        new Thread(()->{
            iDataMoveService.move(database);
        }).start();
        return Result.success("startTask success");
    }
    @GetMapping("/showTableColumn")
    public Result<?> showTableColumn(String database,String tableName) throws InterruptedException {
        List<Map<String, Object>> field = iDataService.queryRemote("SELECT COLUMN_NAME,ordinal_position,column_type,column_key " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "' AND table_schema='" + database + "';");
        List<Map<String, Object>> index = iDataService.queryRemote("SELECT index_name,group_concat(COLUMN_NAME ORDER BY  SEQ_IN_INDEX) fieldname " +
                " FROM INFORMATION_SCHEMA.STATISTICS s WHERE TABLE_SCHEMA = '" + database + "' AND TABLE_NAME = '" + tableName + "' " +
                "        GROUP BY index_name ;" );
        Map<String,Object> result=new HashMap<>();
        result.put("field",field);
        result.put("index",index);
        return Result.success(result);
    }


    @GetMapping("/clear")
    public Result<?> clear(String database) throws InterruptedException {
        iDataMoveService.clear(database);
        return Result.success("clear success");
    }

    @GetMapping("/showlog")
    public Result<?> showlog() throws InterruptedException {
        return Result.success(iDataService.queryLocal("select * from db_datamove.t_logs order by opttime desc limit 100;"));
    }
}
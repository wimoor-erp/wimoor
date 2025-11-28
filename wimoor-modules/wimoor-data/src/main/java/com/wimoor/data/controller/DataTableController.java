package com.wimoor.data.controller;


import com.wimoor.data.controller.result.Result;
import com.wimoor.data.service.IDataService;
import com.wimoor.data.service.IDataTableMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/datamove")
public class DataTableController {
    @Autowired
    IDataTableMoveService iDataTableMoveService;
    @Autowired
    IDataService iDataService;

    @GetMapping("/querySettlement")
    public Result<?> querySettlement(String savedays){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1*Integer.parseInt(savedays));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> result=new HashMap<String,Object>();
        String sql1 = " SELECT * FROM  db_amazon.t_amz_settlement_acc_report WHERE deposit_date<'"+fmt.format(calendar.getTime())+"' and ismoved=0" +
                " ORDER BY deposit_date ASC limit 100";
        List<Map<String, Object>> needMove = iDataService.queryRemote(sql1);
        String sql2 = " SELECT sum(case when  ismoved=1 then 1 else 0 end) hasmove,sum(case when ismoved=0 then 1 else 0 end) needmove FROM  db_amazon.t_amz_settlement_acc_report WHERE deposit_date<'"+fmt.format(calendar.getTime())+"' " ;
        List<Map<String, Object>> beforeSummary = iDataService.queryRemote(sql2);
        result.put("needMove",needMove);
        if(beforeSummary!=null&&beforeSummary.size()>0){
            result.put("beforeSummary",beforeSummary.get(0));
        }
        String sql4= " SELECT sum(case when status=0 then 1 else 0 end ) needtreat,max(case when status=1 then settlementid else null end ) treatsettlement," +
                "sum(case when status=2 then 1 else 0 end) hastreat FROM  db_datamove.t_database_table_move_settlement   " ;
        List<Map<String, Object>> treatSummary = iDataService.queryLocal(sql4);
        if(treatSummary!=null&&treatSummary.size()>0){
            result.put("treatSummary",treatSummary.get(0));
        }
        String sql5= " SELECT * FROM  db_datamove.t_database_table_move_settlement order by startdate desc  limit 100;" ;
        List<Map<String, Object>> treatlist = iDataService.queryLocal(sql5);
        result.put("treatlist",treatlist);
        return Result.success(result);
    }

    @GetMapping("/moveSettlement")
    public Result<?> moveSettlement(String savedays){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1*Integer.parseInt(savedays));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sql1 = " SELECT * FROM  db_amazon.t_amz_settlement_acc_report WHERE deposit_date<'"+fmt.format(calendar.getTime())+"' and ismoved=0" ;
        List<Map<String, Object>> needMove = iDataService.queryRemote(sql1);
        iDataTableMoveService.addSettlement(needMove);
        new Thread(()->{
           // iDataTableMoveService.moveSettlement();
        }).start();
        return Result.success("treated settlement successfully");
    }

    @GetMapping("/startmoveSettlement")
    public Result<?> startmoveSettlement(String database) throws InterruptedException {
        new Thread(()->{
            iDataTableMoveService.moveSettlement();
        }).start();
        return Result.success("startTask success");
    }
    @GetMapping("/clearSettlement")
    public Result<?> clearSettlement() throws InterruptedException {
        iDataService.updateLocal("delete from db_datamove.t_database_table_move_settlement");
        return Result.success("clear success");
    }

    @PostMapping("/taskAdd/{database}")
    public Result<?> remote(@PathVariable("database") String database, @RequestBody List<Map<String,Object>> request) throws InterruptedException {
        for(Map<String,Object> map : request) {
            iDataTableMoveService.addTask(map);
        }
        new Thread(()->{
            iDataTableMoveService.move(database);
        }).start();
        return Result.success("add task success");
    }

    @GetMapping("/showtask")
    public Result<?> showtask(String database) throws InterruptedException {
        return Result.success(iDataService.queryLocal("select * from db_datamove.t_database_table_move where database_name = '"+database+"'"));
    }


    @GetMapping("/startTask")
    public Result<?> startTask(String database) throws InterruptedException {
        new Thread(()->{
            iDataTableMoveService.move(database);
        }).start();
        return Result.success("startTask success");
    }

    @GetMapping("/clear")
    public Result<?> clear(String database) throws InterruptedException {
        iDataTableMoveService.clear(database);
        return Result.success("clear success");
    }
}
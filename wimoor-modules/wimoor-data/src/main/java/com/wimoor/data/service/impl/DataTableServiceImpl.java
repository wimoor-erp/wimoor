package com.wimoor.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wimoor.data.service.IDataService;
import com.wimoor.data.service.IDataTableMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataTableServiceImpl   implements IDataTableMoveService {
    @Autowired
    IDataService iDataService;

    @Override
    public void addTask(Map<String, Object> map) {
        String database=map.get("database_name").toString();
        String tableName=map.get("table_name").toString();
        String dateField=map.get("datefield").toString();
        String saveDays=map.get("savedays").toString();
        String authfield=map.get("authfield")!=null?map.get("authfield").toString():null;
        String marketfield=map.get("marketfield")!=null?map.get("marketfield").toString():null;
        String countryfield=map.get("countryfield")!=null?map.get("countryfield").toString():null;
        String sellerfield=map.get("sellerfield")!=null?map.get("sellerfield").toString():null;
        authfield=StrUtil.isNotBlank(authfield)&&!authfield.equals("null")?"'"+authfield+"'":"null";
        marketfield=StrUtil.isNotBlank(marketfield)&&!marketfield.equals("null")?"'"+marketfield+"'":"null";
        countryfield=StrUtil.isNotBlank(countryfield)&&!countryfield.equals("null")?"'"+countryfield+"'":"null";
        sellerfield=StrUtil.isNotBlank(sellerfield)&&!sellerfield.equals("null")?"'"+sellerfield+"'":"null";

        Integer table_size=map.get("table_size")!=null?Integer.parseInt(map.get("table_size").toString()):0;
        Integer download_size=map.get("download_size")!=null?Integer.parseInt(map.get("download_size").toString()):0;
        iDataService.updateLocal("insert ignore into db_datamove.t_database_table_move " +
                "(database_name,table_name,datefield,savedays,table_size,download_size,opttime,authfield,marketfield,countryfield,sellerfield) " +
                "values ('"+database+"','"+tableName+"','"+dateField+"',"+saveDays+","+table_size+","+download_size+",now(),"+authfield+","+marketfield+","+countryfield+","+sellerfield+")");
    }

    @Override
    public void move(String database) {
        String sql = " SELECT database_name `database_name`,table_name `table_name`,datefield `datefield`," +
                "savedays `savedays`,download_size `download_size`, " +
                "authfield `authfield`,marketfield `marketfield`, " +
                "countryfield `countryfield`,sellerfield `sellerfield` " +
                "FROM db_datamove.t_database_table_move WHERE database_name = '"+database+"' and opttime>DATE_SUB(NOW(),INTERVAL  7 DAY) and enddate is null   ORDER BY table_size asc";
        List<Map<String, Object>> list = iDataService.queryLocal(sql);
        AtomicInteger i= new AtomicInteger(1);
        Map<String,Integer> sumlineMap=new HashMap<String,Integer>();
        for(Map<String, Object> map:list){
            String tablename=map.get("table_name").toString();
            String datefield=map.get("datefield").toString();
            String savedays=map.get("savedays").toString();
            String authfield=map.get("authfield")!=null?map.get("authfield").toString():null;
            String marketfield=map.get("marketfield")!=null?map.get("marketfield").toString():null;
            String countryfield=map.get("countryfield")!=null?map.get("countryfield").toString():null;
            String sellerfield=map.get("sellerfield")!=null?map.get("sellerfield").toString():null;
            authfield=StrUtil.isNotBlank(authfield)&&!authfield.equals("null")?authfield:null;
            marketfield=StrUtil.isNotBlank(marketfield)&&!marketfield.equals("null")?marketfield:null;
            countryfield=StrUtil.isNotBlank(countryfield)&&!countryfield.equals("null")?countryfield:null;
            sellerfield=StrUtil.isNotBlank(sellerfield)&&!sellerfield.equals("null")?sellerfield:null;
            Integer download_size=map.get("download_size")!=null?Integer.parseInt(map.get("download_size").toString()):0;
            sumlineMap.put("sumline", 0);
            try{
                iDataService.updateLocal("update db_datamove.t_database_table_move set opttime=now(),startdate=now(),enddate=null where database_name = '"+database+"' and table_name='"+tablename+"'");
                Calendar c=Calendar.getInstance();
                c.add(Calendar.DATE, Integer.parseInt(savedays)*-1);
                if(StrUtil.isNotBlank(authfield)||StrUtil.isNotBlank(marketfield)||StrUtil.isNotBlank(countryfield)||StrUtil.isNotBlank(sellerfield)){
                    this.moveAuthMakretTableByDay(database,tablename,datefield,authfield,marketfield,countryfield,sellerfield,c.getTime(),sumlineMap);
                }else{
                    this.moveTableByDay(database,tablename,datefield,c.getTime(),null,sumlineMap);
                }
                iDataService.updateLocal("update db_datamove.t_database_table_move set  enddate=now()  where database_name = '"+database+"' and table_name='"+tablename+"'");
            }catch(Exception e){
                 e.printStackTrace();
            }
        }
    }

    @Override
    public void clear(String database) {
        String sql = " delete  FROM db_datamove.t_database_table_move where database_name = '"+database+"'";
        iDataService.updateLocal(sql);
    }

    @Override
    public void addSettlement(List<Map<String, Object>> needMove) {
        for(Map<String, Object> map:needMove){
            String settlement_id=map.get("settlement_id").toString();
            String deposit_date=map.get("deposit_date").toString();
            iDataService.updateLocal("insert ignore into db_datamove.t_database_table_move_settlement (settlementid,deposit_date,status,startdate,enddate) values ("+settlement_id+",'"+deposit_date+"','0',null,null)");
        }
    }


    public String getMinDateByday(String database,String table,String bydayfield,Date enddate ,String fieldwhere){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sqlmin = "SELECT DATE_FORMAT(min("+bydayfield+"),'%Y-%m-%d') byday FROM  "+database+"."+table+(StrUtil.isNotBlank(fieldwhere)?" where "+fieldwhere:"") ;
        List<Map<String, Object>> minRemote = iDataService.queryRemote(sqlmin);
        if (minRemote == null || minRemote.size() == 0 || minRemote.get(0).get("byday") == null) {
            return null;
        }
        String minDateStr = minRemote.get(0).get("byday").toString();
        Date minDate = null;
        try {
            minDate = fmt.parse(minDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(minDate==null){
            return null;
        }
        if (minDate.after(enddate)) {
            return null;
        }
        return minDateStr;
    }

    public void moveTableByDay(String amazonauthid,String marketplaceid,
                               String table,String amazonauthidfield,
                               String marketplaceidfield,String datefield,
                               String byday){
            String  where=" where  "+datefield+"='" + byday + "' and "+amazonauthidfield+"="+amazonauthid+" and "+marketplaceidfield+"='"+marketplaceid+"'";
            String sql1 = "SELECT * FROM "+ table+where ;
            insertTable(sql1,"db_amazon",table);
            String count_sql = " SELECT count("+datefield+") amount FROM "+table+ where ;
            List<Map<String, Object>> countRemote = iDataService.queryRemote(count_sql);
            List<Map<String, Object>> countLocal = iDataService.queryLocal(count_sql);
            if (countRemote.size() == 1
                    && countLocal.size() == 1
                    && countRemote.get(0).get("amount") != null
                    && countLocal.get(0).get("amount") != null
                    && new BigDecimal(countRemote.get(0).get("amount").toString()).intValue()!=0
                    && new BigDecimal(countRemote.get(0).get("amount").toString()).compareTo(new BigDecimal(countLocal.get(0).get("amount").toString()))<=0) {
                String sqldel = "delete  FROM "+table+ where ;
                iDataService.updateRemote(sqldel);
            }

    }
    public void moveAuthMakretTableByDay(String database,String table,String datefield,String amazonauthidfield,String marketplaceidfield,String countryfield,String selleridField,Date enddate,Map<String,Integer> sumlineMap){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT * FROM db_amazon.t_amazon_auth where refresh_token is not null ";
        List<Map<String, Object>> list = iDataService.queryRemote(sql);
        String marketsql = "SELECT * FROM db_amazon.t_marketplace ";
        List<Map<String, Object>> marketlist = iDataService.queryRemote(marketsql);
        Map<String,List<Map<String,Object>>> maplist=new HashMap<String,List<Map<String,Object>>>();
        for(Map<String, Object> mark:marketlist){
                String aws_region=mark.get("aws_region")!=null?mark.get("aws_region").toString():null;
                if(StrUtil.isBlankOrUndefined(aws_region)){
                    continue;
                }
                if(maplist.containsKey(aws_region)){
                    maplist.get(aws_region).add(mark);
                }else{
                    List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
                    list1.add(mark);
                    maplist.put(aws_region, list1);
                }
        }
        if(StrUtil.isNotBlank(amazonauthidfield)||StrUtil.isNotBlank(selleridField)) {
            for (Map<String, Object> map : list) {
                String id = map.get("id").toString();
                String sellerid = map.get("sellerid").toString();
                String aws_region=map.get("aws_region").toString();
                String fieldwhere = null;
                if (StrUtil.isNotBlank(amazonauthidfield)) {
                    fieldwhere = (fieldwhere == null ? " " : fieldwhere+ " and ") + amazonauthidfield + "=" + id + " ";
                }
                if (StrUtil.isNotBlank(selleridField)) {
                    fieldwhere = (fieldwhere == null ? " " : fieldwhere+ " and ") + selleridField + "='" + sellerid + "' ";
                }
                if ((StrUtil.isNotBlank(marketplaceidfield) || StrUtil.isNotBlank(countryfield)) && maplist.containsKey(aws_region)) {
                    for (Map<String, Object> market : maplist.get(aws_region)) {
                        String marketid = market.get("marketplaceid").toString();
                        String country = market.get("market").toString();
                        if (StrUtil.isNotBlank(marketplaceidfield)) {
                            fieldwhere =(fieldwhere == null ? " " :fieldwhere+ " and ") + marketplaceidfield + "='" + marketid + "' ";
                        }
                        if (StrUtil.isNotBlank(countryfield)) {
                            fieldwhere = (fieldwhere == null ? " " :fieldwhere+ " and ") + countryfield + "='" + country + "' ";
                        }
                        moveTableByDay(database, table, datefield, enddate, fieldwhere,sumlineMap);
                    }
                } else {
                    moveTableByDay(database, table, datefield, enddate, fieldwhere,sumlineMap);
                }

            }
        }else{
            String fieldwhere = null;
            if (StrUtil.isNotBlank(marketplaceidfield) || StrUtil.isNotBlank(countryfield)) {
                for (Map<String, Object> market : marketlist) {
                    String marketid = market.get("marketplaceid").toString();
                    String country = market.get("market").toString();
                    if (StrUtil.isNotBlank(marketplaceidfield)) {
                        fieldwhere = (fieldwhere == null ? " " : " and ") + marketplaceidfield + "=" + marketid + " ";
                    }
                    if (StrUtil.isNotBlank(countryfield)) {
                        fieldwhere = (fieldwhere == null ? " " : " and ") + countryfield + "=" + country + " ";
                    }
                    moveTableByDay(database, table, datefield, enddate, fieldwhere,sumlineMap);
                }
            } else {
                moveTableByDay(database, table, datefield, enddate, fieldwhere,sumlineMap);
            }
        }
    }



    public void moveTableByDay(String database, String table, String datefield, Date enddate,String fieldwhere,Map<String,Integer> sumlineMap ){
        database=database.trim();
        table=table.trim();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String bydaystr = null;
        String minDateStr=getMinDateByday(database,table,datefield,enddate,fieldwhere);
        if(minDateStr==null){return;}
        try {
            c.setTime(fmt.parse(minDateStr));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int sumline=sumlineMap.get("sumline");
        while (c.getTime().before(enddate)) {
            bydaystr = fmt.format(c.getTime());
            String  where=" where  "+datefield+">='" + bydaystr + "' and "+datefield+"<DATE_ADD('" + bydaystr + "',INTERVAL  1  Day ) "+(fieldwhere!=null ?" and "+fieldwhere:"");
            String sql1 = "SELECT * FROM "+database+"."+ table+where ;
             Integer download_size=insertTable(sql1,database,table);
            sumline=sumline+download_size;
            String count_sql = " SELECT count("+datefield+") amount FROM "+database+"."+ table+ where ;
            List<Map<String, Object>> countRemote = iDataService.queryRemote(count_sql);
            List<Map<String, Object>> countLocal = iDataService.queryLocal(count_sql);
            if (countRemote.size() == 1
                    && countLocal.size() == 1
                    && countRemote.get(0).get("amount") != null
                    && countLocal.get(0).get("amount") != null
                    && new BigDecimal(countRemote.get(0).get("amount").toString()).intValue()!=0
                    && new BigDecimal(countRemote.get(0).get("amount").toString()).compareTo(new BigDecimal(countLocal.get(0).get("amount").toString()))<=0) {
                String sqldel = "delete  FROM "+database+"."+ table+ where ;
                iDataService.updateRemote(sqldel);
            }
            c.add(Calendar.DATE, 1);
            if(sumline>0){
                iDataService.updateLocal("update db_datamove.t_database_table_move set download_size=" + sumline+ " where database_name = '" + database + "' and table_name='" + table + "'");
            }
            sumlineMap.put("sumline", sumline);
        }
    }

    @Override
    public void moveSettlement() {
            String sql = " SELECT * FROM  db_datamove.t_database_table_move_settlement WHERE   status=0  ORDER BY deposit_date ASC ";
            List<Map<String, Object>> list = iDataService.queryLocal(sql);
            for(Map<String, Object> map:list) {
                String settlementid = map.get("settlementid").toString();
                String sql1 = " SELECT * FROM  db_amazon.t_amz_settlement_acc_report WHERE  settlement_id=" +settlementid;
                List<Map<String, Object>> list1 = iDataService.queryRemote(sql1);
                for(Map<String, Object> map1:list1){
                    try {
                        iDataService.updateLocal("update db_datamove.t_database_table_move_settlement set status=1,startdate=now() where settlementid=" + map1.get("settlement_id").toString());
                        String t_amz_settlement_summary_sku_sql = "    SELECT * FROM db_amazon.t_amz_settlement_summary_sku     WHERE settlementid=" + map1.get("settlement_id").toString();
                        String t_amz_settlement_summary_month_sql = "  SELECT * FROM db_amazon.t_amz_settlement_summary_month   WHERE settlementid=" + map1.get("settlement_id").toString();
                        String t_amz_settlement_summary_day_sql = "    SELECT * FROM db_amazon.t_amz_settlement_summary_day     WHERE settlementid=" + map1.get("settlement_id").toString();
                        String t_amz_settlement_summary_returns_sql = "SELECT * FROM db_amazon.t_amz_settlement_summary_returns WHERE settlementid=" + map1.get("settlement_id").toString();
                        String t_amz_settlement_report_sql = "         SELECT * FROM db_amazon.t_amz_settlement_report          WHERE settlement_id=" + map1.get("settlement_id").toString();
                        System.out.println("开始迁移结算单" + map1.get("settlement_id").toString() + " amazonauthid:" + map1.get("amazonauthid").toString());
                        insertTable(t_amz_settlement_summary_sku_sql, "db_amazon","t_amz_settlement_summary_sku");
                        insertTable(t_amz_settlement_summary_month_sql, "db_amazon","t_amz_settlement_summary_month");
                        insertTable(t_amz_settlement_summary_day_sql, "db_amazon","t_amz_settlement_summary_day");
                        insertTable(t_amz_settlement_summary_returns_sql, "db_amazon","t_amz_settlement_summary_returns");
                        insertTable(t_amz_settlement_report_sql, "db_amazon","t_amz_settlement_report");
                        String count_sql = "         SELECT ifnull(sum(amount),0) amount FROM db_amazon.t_amz_settlement_report  WHERE settlement_id=" + map1.get("settlement_id").toString();
                        List<Map<String, Object>> countRemote = iDataService.queryRemote(count_sql);
                        List<Map<String, Object>> countLocal = iDataService.queryLocal(count_sql);
                        if (countRemote.size() == 1
                                && countLocal.size() == 1
                                && countRemote.get(0).get("amount") != null
                                && countLocal.get(0).get("amount") != null
                                && new BigDecimal(countRemote.get(0).get("amount").toString()).equals(new BigDecimal(countLocal.get(0).get("amount").toString()))) {
                            String t_amz_settlement_summary_sku_del = "    delete FROM db_amazon.t_amz_settlement_summary_sku     WHERE settlementid=" + map1.get("settlement_id").toString();
                            String t_amz_settlement_summary_month_del = "  delete FROM db_amazon.t_amz_settlement_summary_month   WHERE settlementid=" + map1.get("settlement_id").toString();
                            String t_amz_settlement_summary_day_del = "    delete FROM db_amazon.t_amz_settlement_summary_day     WHERE settlementid=" + map1.get("settlement_id").toString();
                            String t_amz_settlement_summary_returns_del = "delete FROM db_amazon.t_amz_settlement_summary_returns WHERE settlementid=" + map1.get("settlement_id").toString();
                            String t_amz_settlement_report_del = "         delete FROM db_amazon.t_amz_settlement_report          WHERE settlement_id=" + map1.get("settlement_id").toString();
                            iDataService.updateRemote(t_amz_settlement_summary_sku_del);
                            iDataService.updateRemote(t_amz_settlement_summary_month_del);
                            iDataService.updateRemote(t_amz_settlement_summary_day_del);
                            iDataService.updateRemote(t_amz_settlement_summary_returns_del);
                            iDataService.updateRemote(t_amz_settlement_report_del);
                            String update = "UPDATE db_amazon.t_amz_settlement_acc_report SET ismoved=1 WHERE settlement_id=" + map1.get("settlement_id").toString();
                            iDataService.updateRemote(update);
                        }
                        iDataService.updateLocal("update db_datamove.t_database_table_move_settlement set status=2,enddate=now() where settlementid=" + map1.get("settlement_id").toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

    }
    public void moveRow(String database, String tableName, List<Map<String, Object>> list) {
        if(list==null||list.size()==0)return;
        System.out.println("database:" + database + " tableName:" + tableName+"  list:"+list.size()+" movetime:"+new Date());
        String insertSql = "insert ignore into "+database+"."+tableName+" ";
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
                            value = value.replaceAll("'","");
                        }
                        if(value.contains("\\")){
                            value = value.replaceAll("\\\\","");
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


    public Integer insertTable(String sql,String database,String tableName) {
        int page=0;
        int number=0;
        while(true){
            List<Map<String, Object>> listrpt = iDataService.queryRemote(sql+" limit "+(page*1000)+",1000");
            page++;
            if(listrpt==null|| listrpt.isEmpty()){
                break;
            }
            number=number+listrpt.size();
            this.moveRow(  database,  tableName,  listrpt);
            listrpt.clear();
        }
        return number;

    }

}

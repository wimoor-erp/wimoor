package com.wimoor.erp.stock.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaTrans;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaTransMapper;
import com.wimoor.erp.stock.service.IErpDispatchOverseaTransService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Service
public class ErpDispatchOverseaTransServiceImpl extends ServiceImpl<ErpDispatchOverseaTransMapper, ErpDispatchOverseaTrans> implements IErpDispatchOverseaTransService {

    @Override
    public Map<String,Object> getInfo(String id) {
        return this.baseMapper.getInfo(id);
    }


    @Override
    public void setShipFeeDetailReport(SXSSFWorkbook workbook, Map<String, Object> params) {
        Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
        titlemap.put("sku", "本地sku");
        titlemap.put("number", "编码");
        titlemap.put("ownername", "产品负责人");
        titlemap.put("optname", "发货人");
        titlemap.put("warehouse", "发货仓库");
        titlemap.put("towarehousename", "收货仓库");
        titlemap.put("opttime", "发货日期");
        titlemap.put("shipcompany", "发货承运商");
        titlemap.put("channame", "发货渠道");
        titlemap.put("name", "产品名称");
        titlemap.put("qty", "发货数量");
        titlemap.put("transweight", "实际结算重量");
        titlemap.put("skuweight", "SKU预估重量");
        titlemap.put("shipmentfee", "货件运费");
        titlemap.put("skufee", "SKU分摊费用");
        titlemap.put("skufeeavg", "SKU单件费用");
        List<Map<String, Object>> list  =this.baseMapper.transFeeSharedDetail(params);
        Sheet sheet = workbook.createSheet("sheet1");
        // 在索引0的位置创建行（最顶端的行）
        Row trow = sheet.createRow(0);
        Object[] titlearray = titlemap.keySet().toArray();
        for (int i = 0; i < titlearray.length; i++) {
            Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
            Object value = titlemap.get(titlearray[i].toString());
            cell.setCellValue(value.toString());
        }
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Map<String, Object> map = list.get(i);
            for (int j = 0; j < titlearray.length; j++) {
                Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
                Object value = map.get(titlearray[j].toString());
                if (value != null||"skufeeavg".equals(titlearray[j].toString())) {
                    if("skufeeavg".equals(titlearray[j].toString())) {
                        if(map.get("skufee")!=null && map.get("qty")!=null) {
                            String skufeeStr=map.get("skufee").toString();
                            BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
                            BigDecimal skufee=new BigDecimal(skufeeStr);
                            Double rate = 0.0;
                            if(skufee!=null&&qty!=null&&qty.intValue()>0) {
                                rate=skufee.doubleValue()/qty.doubleValue();
                            }
                            cell.setCellValue(GeneralUtil.formatterNum(rate,2));
                        }else {
                            cell.setCellValue("--");
                        }
                    }else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
    }

    @Override
    public IPage<Map<String, Object>> transSKUFeeShared(Page<Object> page, Map<String, Object> param) {
        IPage<Map<String, Object>> pagelist = this.baseMapper.transFeeSharedWeek(page,param);
        HashMap<String,Object> result=new HashMap<String,Object>();
        HashMap<String,Object> temp=new HashMap<String,Object>();
        LinkedList<String> labels=new LinkedList<String>();
        LinkedList<String> series=new LinkedList<String>();
        LinkedList<String> seriesfee=new LinkedList<String>();
        LinkedList<String> seriesavgfee=new LinkedList<String>();

        if(pagelist!=null && pagelist.getRecords().size()>0) {
            String fromdateStr = param.get("fromDate").toString();
            String enddateStr = param.get("endDate").toString();
            Date fromdate = GeneralUtil.getDatez(fromdateStr);
            Date firtday = getFirstDayofWeek(fromdate);
            Calendar c=Calendar.getInstance();
            c.setTime(firtday);
            Date enddate = GeneralUtil.getDatez(enddateStr);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Map<String, Object> item:pagelist.getRecords()) {
                if(item.get("opttime2")!=null){
                    temp.put(item.get("opttime2").toString().substring(0,10), item);
                }
            }
            while (c.getTime().before(enddate)) {
                String key = sdf.format(c.getTime());
                if(temp.get(key)==null) {
                    series.add("0");
                    seriesfee.add("0");
                    seriesavgfee.add("0");
                }else {
                    Map<String, Object> myItem = (Map<String, Object>)temp.get(key);
                    if(myItem==null||myItem.get("qty")==null) {
                        series.add("0");
                    }else {
                        series.add(myItem.get("qty").toString());
                    }
                    if(myItem==null||myItem.get("skufee")==null) {
                        seriesfee.add("0");
                    }else {
                        seriesfee.add(myItem.get("skufee").toString());
                    }
                    if(myItem==null||myItem.get("avgfee")==null) {
                        seriesavgfee.add("0");
                    }else {
                        seriesavgfee.add(myItem.get("avgfee").toString());
                    }
                }
                labels.add(key);
                c.add(Calendar.DATE, 7);
            }

        }
        result.put("labels", labels);
        result.put("series", series);
        result.put("seriesfee", seriesfee);
        result.put("seriesavgfee", seriesavgfee);
        if(pagelist!=null&&pagelist.getRecords().size()>0) {
            pagelist.getRecords().get(0).put("chartdata", result);
        }
        return pagelist;
    }

    public static  Date getFirstDayofWeek(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return  cal.getTime();
    }


    @Override
    public void setShipFeeReport(SXSSFWorkbook workbook, Map<String, Object> params) {
        Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
        titlemap.put("sku", "SKU");
        titlemap.put("name", "产品名称");
        titlemap.put("ownername", "负责人");
        titlemap.put("qty", "发货数量");
        titlemap.put("skufee", "运费");
        titlemap.put("msku", "本地SKU");
        titlemap.put("avgfee", "单件平均费用");

        List<Map<String, Object>> list = this.baseMapper.transFeeShared(params);
        Sheet sheet = workbook.createSheet("sheet1");
        // 在索引0的位置创建行（最顶端的行）
        Row trow = sheet.createRow(0);
        Object[] titlearray = titlemap.keySet().toArray();
        for (int i = 0; i < titlearray.length; i++) {
            Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
            Object value = titlemap.get(titlearray[i].toString());
            cell.setCellValue(value.toString());
        }
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Map<String, Object> map = list.get(i);
            for (int j = 0; j < titlearray.length; j++) {
                Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
                Object value = map.get(titlearray[j].toString());
                if (value != null) {
                    if(value instanceof BigDecimal) {
                        cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
                    }else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
		}
	}

    @Override
    public IPage<Map<String, Object>> getShipFeeReport(Page<Object> page, Map<String, Object> param) {
        return this.baseMapper.transFeeShared(page,param);
    }


    public List<Map<String, Object>> getShipFeeReport( Map<String, Object> param) {
        return this.baseMapper.transFeeShared(param);
    }



    @Override
    public IPage<Map<String, Object>> getShipFeeDetailReport(Page<Object> page, Map<String, Object> param) {
        IPage<Map<String, Object>> list =null;

        list=this.baseMapper.transFeeSharedDetail(page,param);

        if(list!=null && list.getRecords().size()>0) {
            for(int i=0;i<list.getRecords().size();i++) {
                Map<String, Object> map = list.getRecords().get(i);
                if(map.get("skufee")!=null && map.get("qty")!=null) {
                    String skufeeStr=map.get("skufee").toString();
                    BigDecimal qty=new BigDecimal(map.get("qty").toString()) ;
                    BigDecimal skufee=new BigDecimal(skufeeStr);
                    //应该找出表t_profitcfg中的shipmentStyle 再去判断使用谁
                    if(skufee!=null&&qty!=null&&qty.intValue()>0) {
                        Double rate = skufee.doubleValue()/qty.doubleValue();
                        map.put("skufeeavg", rate );
                    }else {
                        map.put("skufeeavg", 0);
                    }
                }else {
                    map.put("skufeeavg", null);
                }
            }
        }
        return list;
    }

}

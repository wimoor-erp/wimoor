package com.wimoor.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysLocalHolidayMapper;
import com.wimoor.admin.pojo.entity.SysLocalHoliday;
import com.wimoor.admin.service.ISysLocalHolidayService;
import com.wimoor.admin.util.CalendarUtils;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysLocalHolidayServiceImpl extends ServiceImpl<SysLocalHolidayMapper, SysLocalHoliday> implements ISysLocalHolidayService {
    /**
     * 生成年度日期
     *
     * @return 结果
     */
    public List<SysLocalHoliday> generateYearData(UserInfo user,SysLocalHoliday param){

        Integer year = param.getYear()==null?Integer.valueOf(dateTimeNow("yyyy")):param.getYear();
        deleteHolidayInfoByYear(user.getCompanyid(),year);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.DAY_OF_YEAR, 1);
        calendar.set(GregorianCalendar.YEAR, year);
        // 循环输出一年的日期
        List<SysLocalHoliday> list = new ArrayList<>();
        while (calendar.get(GregorianCalendar.YEAR) == year) {
            Date date = calendar.getTime();
            SysLocalHoliday holidayInfo = new SysLocalHoliday();
            holidayInfo.setYear(calendar.get(GregorianCalendar.YEAR));
            holidayInfo.setMonth(calendar.get(GregorianCalendar.MONTH)+1);
            holidayInfo.setDay(calendar.get(GregorianCalendar.DATE));
            //周名称
            holidayInfo.setWeekName(new DateFormatSymbols().getWeekdays()[calendar.get(GregorianCalendar.DAY_OF_WEEK)]);
            int weekDay = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            int boo = (weekDay==1||weekDay==7)?1:0;
            //判断日期类型 0工作日 1周末 2节假日 3调休
            holidayInfo.setType(boo);
            holidayInfo.setKeyDate(DateFormatUtils.format(date, "MM-dd"));
            holidayInfo.setCurDate(date);
            CalendarUtils as = new CalendarUtils(calendar);
            //农历日 初一~三十
            holidayInfo.setLunars(as.getSimpleDay());
            //农历日 例如: 二零三年九月初一
            holidayInfo.setLunarsDate(as.getDay());
            holidayInfo.setOperator(user.getId());
            holidayInfo.setOpttime(new Date());
            holidayInfo.setShopid(user.getCompanyid());
            holidayInfo.setCreatetime(new Date());
            holidayInfo.setCreator(user.getId());
            list.add(holidayInfo);
            calendar.add(GregorianCalendar.DAY_OF_YEAR, 1);
        }
        this.saveBatch(list);
        return list;
    }

    private void deleteHolidayInfoByYear(String shopid,Integer year) {
        this.baseMapper.delete(new LambdaQueryWrapper<SysLocalHoliday>().eq(SysLocalHoliday::getYear, year).eq(SysLocalHoliday::getShopid, shopid));
    }


    /**
     * 日历查询
     *
     * @param holidayInfo
     * @return AjaxResult
     */
    public List<SysLocalHoliday> selectHolidayInfoList(SysLocalHoliday holidayInfo ) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //处理日期
        Integer year = holidayInfo .getYear();
        Integer month = holidayInfo .getMonth();
        GregorianCalendar calendar = new GregorianCalendar();
        // 重置时间为当月月初  例: 2023-09-01
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month-1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        // 获取当月月初的week 需要注意的是  1代表着周末
        int week = calendar.get(GregorianCalendar.DAY_OF_WEEK);
        week = week==1?7:week-1;
        // 本月日历展示的起始日期 例: 2023-08-27
        calendar.add(GregorianCalendar.DAY_OF_YEAR, -week);
        holidayInfo.setStartDate(df.parse(df.format(calendar.getTime())));
        // 本月日历展示的结束日期 例: 2023-10-07
        calendar.add(GregorianCalendar.DAY_OF_YEAR, 41);
        holidayInfo .setEndDate(df.parse(df.format(calendar.getTime())));
        List<SysLocalHoliday> list = this.baseMapper.selectHolidayInfoList(holidayInfo );
        list.stream().forEach(item->{
            item.setIsGray(item.getMonth()==month?0:1);
        });
        return  list ;

    }

    /**
     * 请求获取节假日信息
     */
    public static String getHoliday(Integer year) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try{
            URL realUrl = new URL("https://timor.tech/api/holiday/year/" + year);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
        }catch (Exception e) {
            System.out.println("调用HttpUtils.sendGet ConnectException: " + e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception ex) {
                System.out.println("调用in.close Exception: "+ ex);
            }
        }
        return result.toString();
    }

    /**
     * 同步节假日
     *
     * @param holidayInfo
     * @return 结果
     */
    public void syncHoliday(SysLocalHoliday holidayInfo){
        Integer year = holidayInfo.getYear()==null?Integer.valueOf(dateTimeNow("yyyy")):holidayInfo.getYear();
        String result = getHoliday(year);
        JSONObject parse = (JSONObject)JSONObject.parse(result);
        JSONObject holidayJson = (JSONObject)parse.get("holiday");
        Map<String, Object> map = holidayJson.toJavaObject(Map.class);
        List<SysLocalHoliday > holidayList =  selectHolidayInfoListByYear(year);
        for (SysLocalHoliday holiday: holidayList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if(holiday.getKeyDate().equals(key)){
                    JSONObject valueJson = (JSONObject)JSONObject.parse(String.valueOf(entry.getValue()));
                    Boolean holidayFlag = (Boolean)valueJson.get("holiday");
                    String name = (String)valueJson.get("name");
                    holiday.setType(holidayFlag?2:3);
                    holiday.setName(name);
                    this.baseMapper.updateById(holiday);
                }
            }
        }

    }

    public List<SysLocalHoliday> selectHolidayInfoListByYear(Integer year) {
        return  this.baseMapper.selectList(new LambdaQueryWrapper<SysLocalHoliday>().eq(SysLocalHoliday::getYear, year));
    }

    private int dateTimeNow(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        return Integer.valueOf(df.format(calendar.getTime()));
    }
}

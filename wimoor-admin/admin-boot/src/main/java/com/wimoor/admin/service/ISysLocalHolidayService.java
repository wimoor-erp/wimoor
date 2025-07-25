package com.wimoor.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.SysLocalHoliday;
import com.wimoor.admin.util.CalendarUtils;
import com.wimoor.common.user.UserInfo;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public interface ISysLocalHolidayService  extends IService<SysLocalHoliday> {

    public List<SysLocalHoliday> generateYearData(UserInfo user, SysLocalHoliday param);
    /**
     * 日历查询
     *
     * @param holidayInfo
     * @return AjaxResult
     */
    public List<SysLocalHoliday> selectHolidayInfoList(SysLocalHoliday holidayInfo ) throws ParseException ;

    public List<SysLocalHoliday> selectHolidayInfoListByYear(Integer year);
    /**
     * 同步节假日
     *
     * @param holidayInfo
     * @return 结果
     */
    public void syncHoliday(SysLocalHoliday holidayInfo);
}

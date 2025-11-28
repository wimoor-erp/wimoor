package com.wimoor.admin.controller;

import com.wimoor.admin.common.exception.BizException;
import com.wimoor.admin.pojo.entity.SysLocalHoliday;
import com.wimoor.admin.service.ISysLocalHolidayService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/api/v1/holiday")
@RequiredArgsConstructor
public class SysLocalHolidayController {
    final ISysLocalHolidayService iSysLocalHolidayService;

    @PostMapping("/listHolid")
    public Result<List<SysLocalHoliday>> syncHoliday(@RequestBody SysLocalHoliday holidayInfo) throws ParseException {
        UserInfo userinfo = UserInfoContext.get();
        holidayInfo.setShopid(userinfo.getCompanyid());
        List<SysLocalHoliday> result=new LinkedList<SysLocalHoliday>();
        if(holidayInfo.getMonth()== null){
            for(int i=1;i<=12;i++){
                holidayInfo.setMonth(i);
                List<SysLocalHoliday> list = iSysLocalHolidayService.selectHolidayInfoList(holidayInfo);
                result.addAll(list);
            }
            return Result.success(result);
        }
    	return Result.success(iSysLocalHolidayService.selectHolidayInfoList(holidayInfo));
    }

    @PostMapping("/generateYearData")
    public Result<List<SysLocalHoliday>> generateYearData(@RequestBody SysLocalHoliday holidayInfo) throws ParseException {
        UserInfo userinfo = UserInfoContext.get();
        List<SysLocalHoliday> list = iSysLocalHolidayService.generateYearData(userinfo,holidayInfo);
        iSysLocalHolidayService.syncHoliday(holidayInfo);
        return Result.success(list);
    }

    @PostMapping("/changeDateType")
    public Result<?> changeDateType(@RequestBody SysLocalHoliday holidayInfo) throws ParseException {
        SysLocalHoliday one = iSysLocalHolidayService.getById(holidayInfo.getId());
        if(holidayInfo==null||one==null||holidayInfo.getType()==null){
            throw new BizException("参数错误");
        }
        one.setType(holidayInfo.getType());
        return Result.success(iSysLocalHolidayService.updateById(one));
    }


}

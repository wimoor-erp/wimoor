package com.wimoor.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SysLocalHoliday;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  SysLocalHolidayMapper extends BaseMapper<SysLocalHoliday> {
    List<SysLocalHoliday> selectHolidayInfoList(SysLocalHoliday holidayInfo);
}
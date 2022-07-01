package com.wimoor.schedule.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.schedule.pojo.entity.QuartzTask;
@Mapper
public interface QuartzTaskMapper extends BaseMapper<QuartzTask> {

}

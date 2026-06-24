package com.wimoor.feishu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.feishu.pojo.entity.SysFeishuTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysFeishuTableMapper extends BaseMapper<SysFeishuTable> {
    
    List<String> getTypeList(@Param("appId") String appId);
    
}

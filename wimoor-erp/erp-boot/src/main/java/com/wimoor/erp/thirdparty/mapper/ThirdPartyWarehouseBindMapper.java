package com.wimoor.erp.thirdparty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ThirdPartyWarehouseBindMapper extends BaseMapper<ThirdPartyWarehouseBind> {
    List<Map<String,Object>> findByConditon(Map<String,Object> param);
}

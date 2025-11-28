package com.wimoor.amazon.inboundV2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ProductCustomsSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductCustomsSettingMapper extends BaseMapper<ProductCustomsSetting> {


    List<Map<String, Object>> getSettingRate(@Param("shopid")   String shopid);
}

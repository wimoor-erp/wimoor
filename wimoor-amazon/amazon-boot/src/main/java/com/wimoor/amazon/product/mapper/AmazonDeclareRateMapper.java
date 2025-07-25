package com.wimoor.amazon.product.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.AmazonDeclareRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AmazonDeclareRateMapper extends BaseMapper<AmazonDeclareRate> {

    List<Map<String, Object>> findFeeRateList(@Param("shopid")String shopid,@Param("groupid")String groupid,@Param("marketplaceid") String marketplaceid, @Param("authid")String authid);
}

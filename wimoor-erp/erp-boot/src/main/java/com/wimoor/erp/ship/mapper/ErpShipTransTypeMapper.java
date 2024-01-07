package com.wimoor.erp.ship.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
@Mapper
public interface ErpShipTransTypeMapper extends BaseMapper<ErpShipTransType>{
    public List<ErpShipTransType> selectTransTypeList(@Param("shopid") String shopid,@Param("name")String name);
}
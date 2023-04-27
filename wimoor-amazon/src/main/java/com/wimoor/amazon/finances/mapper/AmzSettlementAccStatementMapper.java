package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccStatement;
 

@Mapper
public interface AmzSettlementAccStatementMapper extends BaseMapper<AmzSettlementAccStatement>{
    
    List<Map<String,Object>> existByKey(Map<String,Object> param);
    
    List<Map<String,Object>> findAll(String shopid);
}
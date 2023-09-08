package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccStatement;
 

@Mapper
public interface AmzSettlementAccStatementMapper extends BaseMapper<AmzSettlementAccStatement>{
    
    List<Map<String,Object>> existByKey(Map<String,Object> param);
    
    List<Map<String,Object>> findAll(String shopid);
    
    Page<Map<String, Object>> selectSettlementOpen(IPage<?> page,@Param("param") Map<String, Object> map);

	List<Map<String, Object>> selectSettlementOpen(@Param("param") Map<String, Object> map);
   
}
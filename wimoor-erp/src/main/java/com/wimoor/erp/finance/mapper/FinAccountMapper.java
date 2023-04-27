package com.wimoor.erp.finance.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;

@Mapper
public interface FinAccountMapper extends BaseMapper<FinAccount> {

	FinAccount findAcBanlance(String shopid);
    
}
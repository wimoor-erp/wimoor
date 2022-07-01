package com.wimoor.erp.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;

public interface FinAccountMapper extends BaseMapper<FinAccount> {

	FinAccount findAcBanlance(String shopid);
    
}
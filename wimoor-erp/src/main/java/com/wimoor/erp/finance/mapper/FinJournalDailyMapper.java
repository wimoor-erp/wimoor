package com.wimoor.erp.finance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.finance.pojo.entity.FinJournalDaily;
@Mapper
public interface FinJournalDailyMapper extends BaseMapper<FinJournalDaily> {
	FinJournalDaily selectByday(@Param("day")String day,@Param("acct")String acct);
}
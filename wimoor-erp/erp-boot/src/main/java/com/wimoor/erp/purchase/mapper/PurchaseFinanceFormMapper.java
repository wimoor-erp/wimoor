package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFinanceForm;
@Mapper
public interface PurchaseFinanceFormMapper  extends BaseMapper<PurchaseFinanceForm> {
 
	IPage<Map<String, Object>> findByCondition(Page<?> page,@Param("param") Map<String, Object> param);

	Map<String, Object> findFormId(@Param("formid")String formid);

	List<Map<String, Object>> getDataList(@Param("param") Map<String, Object> param);
	
}
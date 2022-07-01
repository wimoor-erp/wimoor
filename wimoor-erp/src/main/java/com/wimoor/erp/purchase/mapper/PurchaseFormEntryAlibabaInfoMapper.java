package com.wimoor.erp.purchase.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryAlibabaInfo;
@Mapper
public interface PurchaseFormEntryAlibabaInfoMapper extends BaseMapper<PurchaseFormEntryAlibabaInfo> {

	List<PurchaseFormEntryAlibabaInfo> findNeedRefresh();
	List<String>  getEntryIdByLogisticsId(@Param("logisticsId")String logisticsId,@Param("shopid")String shopid);
 
}
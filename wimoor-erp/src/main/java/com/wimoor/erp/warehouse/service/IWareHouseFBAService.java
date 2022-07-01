package com.wimoor.erp.warehouse.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.warehouse.pojo.entity.WareHouseFBA;

public interface IWareHouseFBAService extends IService<WareHouseFBA> {
	
	List<Map<String,Object>> findByCondition(String shopid, String search);
	
	WareHouseFBA findByMarket(String shopid, String marketplaceid);
	
	IPage<Map<String,Object>> findBySerch(Page<?> page,String shopid, String search);

	boolean saverun(WareHouseFBA entity) throws ERPBizException;
	
	int updaterun(WareHouseFBA shiprun);
	
	List<Map<String,Object>> getWarehouseFBA(String shopid, String marketplaceid);
}

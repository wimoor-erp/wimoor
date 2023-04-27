package com.wimoor.amazon.auth.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.WareHouseFBA;
import com.wimoor.common.mvc.BizException;

public interface IWareHouseFBAService extends IService<WareHouseFBA> {
	
	List<Map<String,Object>> findByCondition(String shopid, String search);
	
	WareHouseFBA findByMarket(String shopid, String marketplaceid);
	
	
	List<WareHouseFBA> findFbaMarket(String shopid,String groupid);
	
	IPage<Map<String,Object>> findBySerch(Page<?> page,String shopid, String search);

	boolean saverun(WareHouseFBA entity) throws BizException;
	
	int updaterun(WareHouseFBA shiprun);
	
	List<Map<String,Object>> getWarehouseFBA(String shopid, String marketplaceid);
	
	
    public List<WareHouseFBA> findFbaWarehouseByShop(String shopid); 

	
	//默认添加FBA仓库
	Integer saveFBA(String shopid,String operator)throws BizException;

	int refreshFBA(String shopid, String operator) throws BizException;
	//删除FBA仓库
	Integer deleteFBA(String shopid)throws BizException;
	List<WareHouseFBA> selectFBAAllByShopId(String shopid);
}

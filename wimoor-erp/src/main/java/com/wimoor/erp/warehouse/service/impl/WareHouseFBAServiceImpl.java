package com.wimoor.erp.warehouse.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.warehouse.mapper.WareHouseFBAMapper;
import com.wimoor.erp.warehouse.pojo.entity.WareHouseFBA;
import com.wimoor.erp.warehouse.service.IWareHouseFBAService;


@Service("wareHouseFBAService")
public class WareHouseFBAServiceImpl extends  ServiceImpl<WareHouseFBAMapper,WareHouseFBA> implements IWareHouseFBAService {
 
	public List<Map<String,Object>> findByCondition(String shopid, String search) {
		if(GeneralUtil.isEmpty(search)) {
			search=null;
		}else {
			search=search.trim()+"%";
		}
		List<Map<String,Object>> list = this.baseMapper.findByCondition(shopid, search);
		return list;
	}
	
	public IPage<Map<String,Object>> findBySerch(Page<?> page,String shopid, String search) {
		if(GeneralUtil.isEmpty(search)) {
			search=null;
		}else {
			search=search.trim()+"%";
		}
		return this.baseMapper.findBySerch(page,shopid, search);
	}

	public WareHouseFBA findByMarket(String shopid, String marketplaceid) {
	 
		QueryWrapper<WareHouseFBA> queryWrapper = new QueryWrapper<WareHouseFBA>();
		queryWrapper.eq("shopid",shopid);
		queryWrapper.eq("marketplaceid",marketplaceid);
		List<WareHouseFBA> shiplist = this.baseMapper.selectList(queryWrapper);
	    if(shiplist.size()>0)return shiplist.get(0);
		return null;
	}
	
	public List<Map<String,Object>> getWarehouseFBA(String shopid, String marketplaceid) {
		return this.baseMapper.getWarehouseFBA(shopid, marketplaceid);
	}
	
	public boolean saverun(WareHouseFBA entity) throws ERPBizException {
		 return save(entity);
	}

	public int updaterun(WareHouseFBA shiprun) {
		WareHouseFBA old = this.baseMapper.selectById(shiprun.getId());
		if(old != null){
			return this.baseMapper.updateById(shiprun);
		}else{
			return 0;
		}
		
	}
}

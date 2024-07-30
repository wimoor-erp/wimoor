package com.wimoor.amazon.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.WareHouseFBAMapper;
import com.wimoor.amazon.auth.pojo.entity.WareHouseFBA;
import com.wimoor.amazon.auth.service.IWareHouseFBAService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import lombok.RequiredArgsConstructor;


@Service("wareHouseFBAService")
@RequiredArgsConstructor
public class WareHouseFBAServiceImpl extends  ServiceImpl<WareHouseFBAMapper,WareHouseFBA> implements IWareHouseFBAService {
	final ISerialNumService serialnumService;
	public List<Map<String,Object>> findByCondition(String shopid, String search) {
		if(GeneralUtil.isEmpty(search)) {
			search=null;
		}else {
			search=search.trim()+"%";
		}
		List<Map<String,Object>> list = this.baseMapper.findByCondition(shopid, search);
		return list;
	}
	

	public int refreshFBA(String shopid, String operator)   {
		//this.deleteFBA(shopid);
		int result = this.saveFBA(shopid, operator);
		return result;
	}

	// 删除没有绑定地区的FBA仓库
	public Integer deleteFBA(String shopid)     {
		if (GeneralUtil.isEmpty(shopid)) {
			shopid = null;
		}
		int result = this.baseMapper.deleteFBA(shopid);
		return result;
	}
	

	// 添加默认FBA仓库
	public Integer saveFBA(String shopid, String operator)     {
		if (GeneralUtil.isEmpty(shopid)) {
			shopid = null;
		}
		String sernum="";
		try {
			sernum = serialnumService.readSerialNumber(shopid, "FBA");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int result = this.baseMapper.saveFBA(shopid, operator, sernum);
		return result;
	}
	public List<WareHouseFBA> selectFBAAllByShopId(String shopid) {
		QueryWrapper<WareHouseFBA> queryWrapper = new QueryWrapper<WareHouseFBA>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.orderByDesc("opttime");
		List<WareHouseFBA> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}

	public List<WareHouseFBA> findFbaMarket(String shopid,String groupid) {
		return this.baseMapper.findFbaMarket(shopid,groupid);
	}
	public List<WareHouseFBA> findFbaWarehouseByShop(String shopid) {
		return this.baseMapper.findFbaWarehouseByShop(shopid);
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
	
	public boolean saverun(WareHouseFBA entity) throws BizException {
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

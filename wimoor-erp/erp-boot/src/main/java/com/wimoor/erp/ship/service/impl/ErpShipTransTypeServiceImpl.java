package com.wimoor.erp.ship.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.mapper.ErpShipTransTypeDayMapper;
import com.wimoor.erp.ship.mapper.ErpShipTransTypeMapper;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransTypeDay;
import com.wimoor.erp.ship.service.IErpShipTransTypeService;
@Service("erpShipTransTypeService")
public class ErpShipTransTypeServiceImpl extends  ServiceImpl<ErpShipTransTypeMapper,ErpShipTransType> implements IErpShipTransTypeService {
 
	@Autowired
	ErpShipTransTypeDayMapper erpShipTransTypeDayMapper;
    void saveDay(UserInfo user, ErpShipTransType oldone){
    	LambdaQueryWrapper<ErpShipTransTypeDay> query=new LambdaQueryWrapper<ErpShipTransTypeDay>();
    	query.eq(ErpShipTransTypeDay::getShopid, user.getCompanyid());
    	query.eq(ErpShipTransTypeDay::getTranstypeid,oldone.getId());
		ErpShipTransTypeDay tday = erpShipTransTypeDayMapper.selectOne(query);
		if(tday!=null) {
			tday.setDay(oldone.getDay());
			tday.setOperator(user.getId());
			tday.setOpttime(new Date());
			tday.setShopid(user.getCompanyid());
			erpShipTransTypeDayMapper.update(tday, query);
		}else {
			tday=new ErpShipTransTypeDay();
			tday.setDay(oldone.getDay());
			tday.setOperator(user.getId());
			tday.setOpttime(new Date());
			tday.setTranstypeid(oldone.getId());
			tday.setShopid(user.getCompanyid());
			erpShipTransTypeDayMapper.insert(tday);
		}
    }
	@Override
	public boolean updateById(UserInfo user, ErpShipTransType oldone) {
		if(oldone.getIssystem()==false) {
			boolean result = this.baseMapper.updateById(oldone)>0;
			saveDay(user,oldone);
			return result;
		}else {
			saveDay(user,oldone);
			return true;
		}
		
	}

	@Override
	public boolean save(UserInfo user, ErpShipTransType item) {
		// TODO Auto-generated method stub
		if(item.getIssystem()) {
			saveDay(user,item);
			return true;
		}else {
			boolean result = this.baseMapper.insert(item)>0;
			saveDay(user,item);
			return result;
		}
		
	}

	@Override
	public boolean removeById(UserInfo user, String typeid) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<ErpShipTransTypeDay> query=new LambdaQueryWrapper<ErpShipTransTypeDay>();
    	query.eq(ErpShipTransTypeDay::getShopid, user.getCompanyid());
    	query.eq(ErpShipTransTypeDay::getTranstypeid,typeid);
    	erpShipTransTypeDayMapper.delete(query);
    	ErpShipTransType old = this.getById(typeid);
		if(old.getShopid()!=null) {
		   return this.removeById(typeid);
		}else {
		   return true;
		}
	}

	@Override
	public List<ErpShipTransType> list(UserInfo user, QueryWrapper<ErpShipTransType> query) {
		// TODO Auto-generated method stub
		 List<ErpShipTransType> list = this.list(query);
		 if(list!=null&&list.size()>0) {
			 for(ErpShipTransType item:list) {
				 if(item.getShopid()==null) {
					 item.setIssystem(true);
				 }else {
					 item.setIssystem(false);
				 }
				 LambdaQueryWrapper<ErpShipTransTypeDay> mquery=new LambdaQueryWrapper<ErpShipTransTypeDay>();
				 mquery.eq(ErpShipTransTypeDay::getShopid, user.getCompanyid());
				 mquery.eq(ErpShipTransTypeDay::getTranstypeid,item.getId());
				 ErpShipTransTypeDay tday = erpShipTransTypeDayMapper.selectOne(mquery);
				 if(tday!=null) {
					 item.setDay(tday.getDay());
				 }
			 }
		 }
		 return list;
	}
	
 

}

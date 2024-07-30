package com.wimoor.erp.material.service.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.material.mapper.MaterialMarkHisMapper;
import com.wimoor.erp.material.mapper.MaterialMarkMapper;
import com.wimoor.erp.material.pojo.entity.MaterialMark;
import com.wimoor.erp.material.pojo.entity.MaterialMarkHis;
import com.wimoor.erp.material.service.IMaterialMarkService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("materialMarkService")
@RequiredArgsConstructor
public class MaterialMarkServiceImpl extends ServiceImpl<MaterialMarkMapper,MaterialMark> implements IMaterialMarkService {
  final MaterialMarkHisMapper materialMarkHisMapper;
  final AdminClientOneFeignManager adminClientOneFeign;
  @CacheEvict(value = {"inventoryByMskuCache" }, allEntries = true)
   public boolean save(MaterialMark mark){
	   MaterialMarkHis markhis = new MaterialMarkHis();
	   BeanUtil.copyProperties(mark, markhis);
	   LambdaQueryWrapper<MaterialMark> query=new LambdaQueryWrapper<MaterialMark>();
	   query.eq(MaterialMark::getFtype, mark.getFtype());
	   query.eq(MaterialMark::getMaterialid, mark.getMaterialid());
	   MaterialMark old = this.baseMapper.selectOne(query);
	   int result=0;
	   if(old==null) {
		   result= this.baseMapper.insert(mark);
	   }else {
		   result= this.baseMapper.update(mark, query);
	   }
	   materialMarkHisMapper.insert(markhis);
	   return result>0;
  }
   @CacheEvict(value = {"inventoryByMskuCache" }, allEntries = true)
  public boolean remove(MaterialMark mark) {
	   MaterialMarkHis markhis = new MaterialMarkHis();
	   BeanUtil.copyProperties(mark, markhis);
	   LambdaQueryWrapper<MaterialMark> query=new LambdaQueryWrapper<MaterialMark>();
	   query.eq(MaterialMark::getFtype, mark.getFtype());
	   query.eq(MaterialMark::getMaterialid, mark.getMaterialid());
	   MaterialMark old = this.baseMapper.selectOne(query);
	   int result=0;
	   if(old!=null) {
		   result= this.baseMapper.delete(query);
	   }else {
		   return true;
	   }
	   materialMarkHisMapper.insert(markhis);
	   return result>0;
  }
  @CacheEvict(value = {"inventoryByMskuCache" }, allEntries = true)
  public boolean saveNotice(UserInfo user,String materialid,String notice) {
	  MaterialMark mark=new MaterialMark();
	  mark.setFtype(MaterialMark.Type_Notice);
	  mark.setMark(notice);
	  mark.setMaterialid(materialid);
	  mark.setOperator(user.getId());
	  mark.setOpttime(new Date());
	  return save(mark);
  }
  
  @CacheEvict(value = {"inventoryByMskuCache" }, allEntries = true)
  public boolean hide(UserInfo user,String materialid) {
	  MaterialMark mark=new MaterialMark();
	  mark.setFtype(MaterialMark.Type_Hide);
	  mark.setMark(null);
	  mark.setMaterialid(materialid);
	  mark.setOperator(user.getId());
	  mark.setOpttime(new Date());
	  return save(mark);
  }
  @CacheEvict(value = {"inventoryByMskuCache" }, allEntries = true)
  public boolean show(UserInfo user,String materialid) {
	  MaterialMark mark=new MaterialMark();
	  mark.setFtype(MaterialMark.Type_Hide);
	  mark.setMark(null);
	  mark.setMaterialid(materialid);
	  mark.setOperator(user.getId());
	  mark.setOpttime(new Date());
	  return remove(mark);
  }

@SuppressWarnings("unchecked")
@Override
public MaterialMark showNotice(String materialid) {
	// TODO Auto-generated method stub
	   LambdaQueryWrapper<MaterialMark> query=new LambdaQueryWrapper<MaterialMark>();
	   query.eq(MaterialMark::getFtype,MaterialMark.Type_Notice);
	   query.eq(MaterialMark::getMaterialid, materialid);
	   MaterialMark old = this.baseMapper.selectOne(query);
	   if(old!=null) {
		   LambdaQueryWrapper<MaterialMarkHis> queryhis=new LambdaQueryWrapper<MaterialMarkHis>();
		   queryhis.eq(MaterialMarkHis::getFtype,MaterialMark.Type_Notice);
		   queryhis.eq(MaterialMarkHis::getMaterialid, materialid);
		   queryhis.orderByDesc(MaterialMarkHis::getOpttime);
		   List<MaterialMarkHis> list = this.materialMarkHisMapper.selectList(queryhis);
		   for(MaterialMarkHis item:list) {
			   if(StrUtil.isNotBlank(item.getOperator())) {
				   try {
					   Result<Map<String, Object>> result = adminClientOneFeign.getUserByUserId(item.getOperator());
					   if(Result.isSuccess(result)&&result.getData()!=null) {
						   if(result.getData().get("userinfo")!=null) {
							   Map<String, Object> infomap = (Map<String, Object>) result.getData().get("userinfo");
							   item.setOperator(infomap.get("name").toString()); 
						   }
					   }else {
						   item.setOperator(""); 
					   }
				   }catch(Exception e) {
					  item.setOperator("");  
				   }
				   
			   }
			   
		   }
		   if(StrUtil.isNotBlank(old.getOperator())) {
			   try {
				   Result<Map<String, Object>> result = adminClientOneFeign.getUserByUserId(old.getOperator());
				   if(Result.isSuccess(result)&&result.getData()!=null) {
					   if(result.getData().get("userinfo")!=null) {
						   Map<String, Object> infomap = (Map<String, Object>) result.getData().get("userinfo");
						   old.setOperator(infomap.get("name").toString()); 
					   }
				   }else {
					   old.setOperator(""); 
				   }
			   }catch(Exception e) {
				   old.setOperator("");  
			   }
			   
		   }
		   old.setHisList(list);
	   }
	return old;
}
}

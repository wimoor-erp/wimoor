package com.wimoor.erp.stock.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.erp.stock.mapper.DispatchFormEntryMapper;
import com.wimoor.erp.stock.pojo.entity.DispatchFormEntry;
import com.wimoor.erp.stock.service.IDispatchFormEntryService;

import lombok.RequiredArgsConstructor;

@Service("dispatchFormEntryService")
@RequiredArgsConstructor
public class DispatchFormEntryServiceImpl extends  ServiceImpl<DispatchFormEntryMapper,DispatchFormEntry> implements IDispatchFormEntryService {
	final FileUpload fileUpload;
	public List<Map<String, Object>> selectByFormid(String formid) {
		return this.baseMapper.selectByFormid(formid);
	}
	public List<DispatchFormEntry> findByFormid(String formid) {
		QueryWrapper<DispatchFormEntry> queryWrapper=new QueryWrapper<DispatchFormEntry>();
		queryWrapper.eq("formid", formid);
		return this.baseMapper.selectList(queryWrapper);
	}
	
	public List<Map<String, Object>> findFormDetailByFormid(String formid, String warehouseid,String warehouseid2,String shopid) {
		List<Map<String, Object>> list = this.baseMapper.findFormDetailByFormid(formid, warehouseid,warehouseid2,shopid);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				 String image=null; 
				Map<String, Object> map = list.get(i);
				if(map.get("image")!=null)image=map.get("image").toString();
				if(map.get("image")!=null)list.get(i).put("image",fileUpload.getPictureImage(image));
				else list.get(i).put("image","images/systempicture/noimage40.png");
			}
		}
		return list;
	}

	public void deleteByFormid(Object formid) {
		QueryWrapper<DispatchFormEntry> queryWrapper=new QueryWrapper<DispatchFormEntry>();
		queryWrapper.eq("formid",formid);
		this.baseMapper.delete(queryWrapper);
	}
 
}

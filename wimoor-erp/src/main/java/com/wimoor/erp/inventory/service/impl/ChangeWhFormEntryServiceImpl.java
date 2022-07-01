package com.wimoor.erp.inventory.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.inventory.mapper.ChangeWhFormEntryMapper;
import com.wimoor.erp.inventory.pojo.entity.ChangeWhFormEntry;
import com.wimoor.erp.inventory.service.IChangeWhFormEntryService;
import com.wimoor.erp.util.FileUpload;

import lombok.RequiredArgsConstructor;

 

@Service("changeWhFormEntryService")
@RequiredArgsConstructor
public class ChangeWhFormEntryServiceImpl extends  ServiceImpl<ChangeWhFormEntryMapper,ChangeWhFormEntry> implements IChangeWhFormEntryService {
 
	public void deleteByFormid(String formid) {
		QueryWrapper<ChangeWhFormEntry> queryWrapper=new QueryWrapper<ChangeWhFormEntry>();
		queryWrapper.eq("formid", formid);
		this.baseMapper.delete(queryWrapper);
	}

	public List<Map<String, Object>> findFormDetailByFormid(String formid) {
		List<Map<String, Object>> list = this.baseMapper.findFormDetailByFormid(formid);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				 String image=null; 
				Map<String, Object> map = list.get(i);
				if(map.get("image")!=null)image=map.get("image").toString();
				if(map.get("image")!=null)list.get(i).put("image",FileUpload.getPictureImage(image));
				else list.get(i).put("image","images/systempicture/noimage40.png");
			}
		}
		return list;
	}

	public List<ChangeWhFormEntry> selectByFormid(String formid) {
		return this.baseMapper.selectByFormid(formid);
	}


}

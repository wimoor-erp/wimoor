package com.wimoor.erp.stock.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaFormEntryMapper;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaFormEntry;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormEntryService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Service
@RequiredArgsConstructor
public class ErpDispatchOverseaFormEntryServiceImpl extends ServiceImpl<ErpDispatchOverseaFormEntryMapper, ErpDispatchOverseaFormEntry> implements IErpDispatchOverseaFormEntryService {
	final FileUpload fileUpload;
	@Override
	public List<Map<String, Object>> findFormDetailByFormid(String formid, String warehouseid, String warehouseid2, String shopid) {
		// TODO Auto-generated method stub
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

	@Override
	public List<Map<String, Object>> selectByFormid(String formid) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectByFormid(formid);
	}

	@Override
	public List<ErpDispatchOverseaFormEntry> findByFormid(String formid) {
		// TODO Auto-generated method stub
		QueryWrapper<ErpDispatchOverseaFormEntry> queryWrapper=new QueryWrapper<ErpDispatchOverseaFormEntry>();
		queryWrapper.eq("formid", formid);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public void deleteByFormid(String formid) {
		// TODO Auto-generated method stub
		QueryWrapper<ErpDispatchOverseaFormEntry> queryWrapper=new QueryWrapper<ErpDispatchOverseaFormEntry>();
		queryWrapper.eq("formid",formid);
		this.baseMapper.delete(queryWrapper);
	}

}

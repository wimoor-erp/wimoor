package com.wimoor.erp.stock.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.mapper.MaterialCustomsMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
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
	final MaterialCustomsMapper materialCustomsMapper;
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
				else list.get(i).put("image",null);
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

	@Override
	public List<Map<String, Object>> getPrintLabel(UserInfo user, String id) {
		return this.baseMapper.getPrintLabel(id);
	}

	@Override
	public void saveEname(UserInfo user, List<Map<String, Object>> skulist) {
		for(Map<String,Object> item:skulist){
			if(item.get("materialid")==null)continue;
			if(item.get("country")==null)continue;
			if(item.get("ename")==null)continue;
			String materialid=(String)item.get("materialid");
			String country=(String)item.get("country");
			String ename=(String)item.get("ename");
			LambdaQueryWrapper<MaterialCustoms> query=new LambdaQueryWrapper<MaterialCustoms>();
			query.eq(MaterialCustoms::getMaterialid, materialid);
			query.eq(MaterialCustoms::getCountry,country);
			MaterialCustoms old = materialCustomsMapper.selectOne(query);
			if(old!=null) {
				if( StrUtil.isBlank(old.getEname())||!old.getEname().equals(ename)){
					old.setOperator(user.getId());
					old.setOpttime(new Date());
					old.setCreatetime(old.getCreatetime());
					old.setCreator(old.getCreator());
					old.setEname(ename);
					materialCustomsMapper.update(old, query);
				}
			}else {
				MaterialCustoms customs = new MaterialCustoms();
				customs.setOperator(user.getId());
				customs.setOpttime(new Date());
				customs.setCreatetime(new Date());
				customs.setCreator(user.getId());
				customs.setMaterialid(materialid);
				customs.setCountry(country);
				customs.setEname(ename);
				materialCustomsMapper.insert(customs);
			}
		}

	}

}

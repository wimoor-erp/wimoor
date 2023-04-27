package com.wimoor.erp.material.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.ERPGroupMapper;
import com.wimoor.erp.material.mapper.MaterialGroupMapper;
import com.wimoor.erp.material.pojo.entity.ERPGroup;
import com.wimoor.erp.material.pojo.entity.MaterialGroup;
import com.wimoor.erp.material.service.IMaterialGroupService;

import lombok.RequiredArgsConstructor;
 

@Service("materialGroup")
@RequiredArgsConstructor
public class MaterialGroupServiceImpl  extends ServiceImpl<MaterialGroupMapper,MaterialGroup> implements IMaterialGroupService {
	 
	ERPGroupMapper eRPCategoryMapper;
	 
	MaterialGroupMapper materialGroupMapper;
	
	public List<ERPGroup> initfindAllGroup(UserInfo user) {
		QueryWrapper<ERPGroup> queryWrapper = new QueryWrapper<ERPGroup>();
		queryWrapper.eq("ftype", 1);
		queryWrapper.eq("shopid", user.getCompanyid());
		QueryWrapper<ERPGroup> queryor = queryWrapper.or();
		queryor.eq("issys", 1);
		queryor.eq("ftype", 1);
		queryWrapper.orderByDesc("opttime");
		List<ERPGroup> list = eRPCategoryMapper.selectList(queryWrapper);
		if(list.size()==0){
			initDefault(user);
		}
	 
		return  list;
	}
	
	private int  initDefault(UserInfo user){
		int count=0;
		count+=newGroup("红色分类","red",  user);
		count+=newGroup("绿色分类","green",  user);
		count+=newGroup("蓝色分类","blue",  user);
		count+=newGroup("水绿类别","aqua",  user);
		count+=newGroup("黄色类别","yellow",  user);
	 return count;
	}
	
	 public int changeMaterialGroup(String materialid,List<String> categorylist,String userid) throws ERPBizException{
		 QueryWrapper<MaterialGroup> queryWrapper=new QueryWrapper<MaterialGroup>();
		 queryWrapper.eq("materialid", materialid);
		 materialGroupMapper.delete(queryWrapper);
		   int count = 0;
	       for(int i=0;i<categorylist.size();i++){
	    	    MaterialGroup mm = new MaterialGroup();
			    mm.setMaterialid(materialid);
			    mm.setGroupid(categorylist.get(i));
			    mm.setOperator(userid);
				mm.setOpttime(new Date());
				count+=super.baseMapper.insert(mm);
	       }
	       return	count;
		}
	 public List<ERPGroup> getMaterialGroup(UserInfo user,String materialid) {
			// TODO Auto-generated method stub
		   QueryWrapper<MaterialGroup> queryWrapper=new QueryWrapper<MaterialGroup>();
		   queryWrapper.eq("materialid", materialid);
		   List<ERPGroup> alist = initfindAllGroup(user);
		   List<MaterialGroup> list = materialGroupMapper.selectList(queryWrapper);
		   Set<String> set=new HashSet<String>();
		   for(int i=0;i<list.size();i++){
			   set.add(list.get(i).getGroupid());
		   }
		   for(int i=0;i<alist.size();i++){
			   if(set.contains(alist.get(i).getId())){
				   alist.get(i).setChecked(true);
			   }
		   }
			return alist;
		}

 public int deleteGroup(String categorykey) {
			// TODO Auto-generated method stub
			return eRPCategoryMapper.deleteById(categorykey);
		}
		
	public int newGroup(String categoryname, String cateogrycolor, UserInfo user) {
		// TODO Auto-generated method stub
		ERPGroup eRPCategory = new ERPGroup();
		eRPCategory.setColor(cateogrycolor);
		eRPCategory.setFtype(1);
		eRPCategory.setName(categoryname);
		eRPCategory.setShopid(user.getCompanyid());
		eRPCategory.setOperator(user.getId());
		eRPCategory.setOpttime(new Date());
		eRPCategory.setIssys(false);
		return eRPCategoryMapper.insert(eRPCategory);
		
	}
}

package com.wimoor.admin.service.impl;

import com.wimoor.admin.pojo.entity.SysQueryField;
import com.wimoor.admin.pojo.entity.SysQueryUserVersion;
import com.wimoor.admin.pojo.entity.SysQueryVersionFeild;
import com.wimoor.admin.mapper.SysQueryFieldMapper;
import com.wimoor.admin.mapper.SysQueryVersionFeildMapper;
import com.wimoor.admin.service.ISysQueryFieldService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Service
public class SysQueryFieldServiceImpl extends ServiceImpl<SysQueryFieldMapper, SysQueryField> implements ISysQueryFieldService {
 
		@Resource
		com.wimoor.admin.mapper.SysQueryUserVersionMapper SysQueryUserVersionMapper;
		
		@Resource
		SysQueryVersionFeildMapper SysQueryVersionFeildMapper;
		public List<SysQueryField> findByQueryName(UserInfo user, String queryname) {
			// TODO Auto-generated method stub
			List<SysQueryField> list = this.baseMapper.findByUserUsed(user.getId(),queryname);
		    if(list==null||list.size()==0) {
		    	return findByQueryName(queryname);
		    }
		    return list;
		}
		
		public List<SysQueryField> findByQueryName(String queryname) {
			LambdaQueryWrapper<SysQueryField> query=new LambdaQueryWrapper<SysQueryField>();
			query.eq(SysQueryField::getFquery,queryname);
			query.orderByAsc(SysQueryField::getFindex);
			return this.baseMapper.selectList(query);
		}
		
		public  Map<String, ArrayList<Map<String, Object>>>  findAllVersionByUser(UserInfo user, String queryname) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> list = this.baseMapper.findAllVersionByUser(user.getId(),queryname);
		    Map<String, ArrayList<Map<String, Object>>> fversion = GeneralUtil.groupListMapBy(list, "fversionid");
		    return fversion;
		}

		public SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, List<String> fieldlist) {
			// TODO Auto-generated method stub
			SysQueryUserVersion version=new SysQueryUserVersion();
			version.setCreatetime(new Date());
			version.setOpttime(new Date());
			version.setIsused(Boolean.FALSE);
			version.setFquery(queryname);
			version.setUserid(new BigInteger(user.getId()));
			SysQueryUserVersionMapper.insert(version);
			int i=1;
	        for(String field:fieldlist) {
				SysQueryVersionFeild fieldobj=new SysQueryVersionFeild();
				fieldobj.setFfield(field.trim());
				fieldobj.setFversionid(version.getId());
				fieldobj.setFindex(i++);
				SysQueryVersionFeildMapper.insert(fieldobj);
			}		
			return version;
		}
		
		public SysQueryUserVersion updateUserVersion(UserInfo user, SysQueryUserVersion version, List<String> fieldlist) {
			// TODO Auto-generated method stub
			version.setOpttime(new Date());
			version.setIsused(Boolean.TRUE);
			version.setUserid(new BigInteger(user.getId()));
			if(version.getIsused()) {
				LambdaQueryWrapper<SysQueryUserVersion> query=new LambdaQueryWrapper<SysQueryUserVersion>();
				SysQueryUserVersion record=new SysQueryUserVersion();
				record.setIsused(Boolean.FALSE);
				query.eq(SysQueryUserVersion::getFquery,version.getFquery());
				query.eq(SysQueryUserVersion::getUserid,version.getUserid());
				SysQueryUserVersionMapper.update(record, query);
			}
			SysQueryUserVersionMapper.updateById(version);
			int i=1;
			LambdaQueryWrapper<SysQueryVersionFeild> querydelete=new LambdaQueryWrapper<SysQueryVersionFeild>();
			querydelete.eq(SysQueryVersionFeild::getFversionid,version.getId());
			SysQueryVersionFeildMapper.delete(querydelete);
	        for(String field:fieldlist) {
				SysQueryVersionFeild fieldobj=new SysQueryVersionFeild();
				fieldobj.setFfield(field.trim());
				fieldobj.setFversionid(version.getId());
				fieldobj.setFindex(i++);
				SysQueryVersionFeildMapper.insert(fieldobj);
			}		
	        
			return version;
		}
		
		public int deleteUserVersion(String versionid) {
			// TODO Auto-generated method stub
			LambdaQueryWrapper<SysQueryVersionFeild> query=new LambdaQueryWrapper<SysQueryVersionFeild>();
			query.eq(SysQueryVersionFeild::getFversionid,versionid);
			SysQueryVersionFeildMapper.delete(query);
			return SysQueryUserVersionMapper.deleteById(versionid);
		}

		@Override
		public SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, String versionname,
				List<String> fieldlist) {
			// TODO Auto-generated method stub
			SysQueryUserVersion version=new SysQueryUserVersion();
			version.setCreatetime(new Date());
			version.setOpttime(new Date());
			version.setIsused(Boolean.FALSE);
			version.setFquery(queryname);
			version.setName(versionname);
			version.setUserid(new BigInteger(user.getId()));
			SysQueryUserVersionMapper.insert(version);
			int i=1;
	        for(String field:fieldlist) {
				SysQueryVersionFeild fieldobj=new SysQueryVersionFeild();
				fieldobj.setFfield(field.trim());
				fieldobj.setFversionid(version.getId());
				fieldobj.setFindex(i++);
				SysQueryVersionFeildMapper.insert(fieldobj);
			}		
			return version;
		}
}

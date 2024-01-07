package com.wimoor.sys.tool.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.mapper.SysQueryFieldMapper;
import com.wimoor.sys.tool.mapper.SysQueryVersionFeildMapper;
import com.wimoor.sys.tool.pojo.entity.SysQueryField;
import com.wimoor.sys.tool.pojo.entity.SysQueryUserVersion;
import com.wimoor.sys.tool.pojo.entity.SysQueryVersionFeild;
import com.wimoor.sys.tool.service.ISysQueryFieldService;

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
		com.wimoor.sys.tool.mapper.SysQueryUserVersionMapper SysQueryUserVersionMapper;
		@Resource
		SysQueryVersionFeildMapper SysQueryVersionFeildMapper;
		@Resource
		SysQueryFieldMapper sysQueryFieldMapper;
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

		@Override
		public List<SysQueryUserVersion> getMyVersionFieldByUser(String userid, String queryname) {
			LambdaQueryWrapper<SysQueryUserVersion> queryWrapper=new LambdaQueryWrapper<SysQueryUserVersion>();
			queryWrapper.eq(SysQueryUserVersion::getUserid,userid);
			queryWrapper.eq(SysQueryUserVersion::getFquery,queryname);
			List<SysQueryUserVersion> list = SysQueryUserVersionMapper.selectList(queryWrapper);
			if(list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					SysQueryUserVersion item = list.get(i);
					String versionid=item.getId().toString();
					List<Map<String, Object>> fieldlist = SysQueryUserVersionMapper.selectByQueryAndField(versionid);
					String titleStr="";
					String queryfieldStr="";
					if(fieldlist!=null && fieldlist.size()>0) {
						for (int j = 0; j < fieldlist.size(); j++) {
							Map<String, Object> field = fieldlist.get(j);
							titleStr+=(field.get("title")+",");
							queryfieldStr+=(field.get("ffield")+",");
						}
						if(titleStr.contains(",")) {
							titleStr=titleStr.substring(0, titleStr.length()-1);
						}
						if(queryfieldStr.contains(",")) {
							queryfieldStr=queryfieldStr.substring(0, queryfieldStr.length()-1);
						}
					}
					item.setFquery(titleStr);
					item.setQueryfield(queryfieldStr);
				}
			}
			return list;
		}
}

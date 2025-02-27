package com.wimoor.sys.tool.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
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
		
		@CacheEvict(value = { "VersionField"}, allEntries = true)
		public SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, List<SysQueryVersionFeild> fieldlist) {
			if(queryname.contains("adv")) {
				LambdaQueryWrapper<SysQueryUserVersion> queryWrapper=new LambdaQueryWrapper<SysQueryUserVersion>();
				queryWrapper.eq(SysQueryUserVersion::getUserid,new BigInteger(user.getId()));
				queryWrapper.eq(SysQueryUserVersion::getFquery, queryname);
				SysQueryUserVersion old=SysQueryUserVersionMapper.selectOne(queryWrapper);
				if(old!=null) {
					old.setOpttime(new Date());
					old.setIsused(Boolean.TRUE);
					old.setFquery(queryname);
					old.setUserid(new BigInteger(user.getId()));
					SysQueryUserVersionMapper.updateById(old);
					int i=1;
					LambdaQueryWrapper<SysQueryVersionFeild> fqueryWrapper=new LambdaQueryWrapper<SysQueryVersionFeild>();
					fqueryWrapper.eq(SysQueryVersionFeild::getFversionid, old.getId());
					SysQueryVersionFeildMapper.delete(fqueryWrapper);
			        for(SysQueryVersionFeild fieldobj:fieldlist) {
						fieldobj.setFversionid(old.getId());
						if(fieldobj.getFindex()==null) {
							fieldobj.setFindex(i++);
						}
						SysQueryVersionFeildMapper.insert(fieldobj);
					}		
					return old;
				}else {
					SysQueryUserVersion version=new SysQueryUserVersion();
					version.setCreatetime(new Date());
					version.setOpttime(new Date());
					version.setIsused(Boolean.TRUE);
					version.setFquery(queryname);
					version.setUserid(new BigInteger(user.getId()));
					SysQueryUserVersionMapper.insert(version);
					int i=1;
			        for(SysQueryVersionFeild fieldobj:fieldlist) {
						fieldobj.setFversionid(version.getId());
						if(fieldobj.getFindex()==null) {
							fieldobj.setFindex(i++);
						}
						SysQueryVersionFeildMapper.insert(fieldobj);
					}		
					return version;
				}
				
			}else {
				SysQueryUserVersion version=new SysQueryUserVersion();
				version.setCreatetime(new Date());
				version.setOpttime(new Date());
				version.setIsused(Boolean.TRUE);
				version.setFquery(queryname);
				version.setUserid(new BigInteger(user.getId()));
				SysQueryUserVersionMapper.insert(version);
				int i=1;
		        for(SysQueryVersionFeild fieldobj:fieldlist) {
					fieldobj.setFversionid(version.getId());
					if(fieldobj.getFindex()==null) {
						fieldobj.setFindex(i++);
					}
					SysQueryVersionFeildMapper.insert(fieldobj);
				}		
				return version;
			}
		}
		@CacheEvict(value = { "VersionField"}, allEntries = true)
		public SysQueryUserVersion updateUserVersion(UserInfo user, SysQueryUserVersion version, List<SysQueryVersionFeild> fieldlist) {
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
	        for(SysQueryVersionFeild fieldobj:fieldlist) {
				fieldobj.setFversionid(version.getId());
				if(fieldobj.getFindex()==null) {
					fieldobj.setFindex(i++);
				}
				SysQueryVersionFeildMapper.insert(fieldobj);
			}		
	        
			return version;
		}
		
		@CacheEvict(value = { "VersionField"}, allEntries = true)
		public int deleteUserVersion(String versionid) {
			// TODO Auto-generated method stub
			LambdaQueryWrapper<SysQueryVersionFeild> query=new LambdaQueryWrapper<SysQueryVersionFeild>();
			query.eq(SysQueryVersionFeild::getFversionid,versionid);
			SysQueryVersionFeildMapper.delete(query);
			return SysQueryUserVersionMapper.deleteById(versionid);
		}

		@Override
		@CacheEvict(value = { "VersionField"}, allEntries = true)
		public SysQueryUserVersion saveUserVersion(UserInfo user, String queryname, String versionname,
				List<SysQueryVersionFeild> fieldlist) {
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
	        for(SysQueryVersionFeild fieldobj:fieldlist) {
				fieldobj.setFversionid(version.getId());
				if(fieldobj.getFindex()==null) {
					fieldobj.setFindex(i++);
				}
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

		@Override
		public List<SysQueryVersionFeild> getVersionFieldByUserQueryName(String userid, String queryname) {
			LambdaQueryWrapper<SysQueryUserVersion> queryWrapper=new LambdaQueryWrapper<SysQueryUserVersion>();
			queryWrapper.eq(SysQueryUserVersion::getUserid, userid);
			queryWrapper.eq(SysQueryUserVersion::getFquery, queryname);
			List<SysQueryUserVersion> vlist = SysQueryUserVersionMapper.selectList(queryWrapper);
			if(vlist!=null && vlist.size()>0) {
				 SysQueryUserVersion version = vlist.get(0);
				 LambdaQueryWrapper<SysQueryVersionFeild> queryWrapper2=new LambdaQueryWrapper<SysQueryVersionFeild>();
				 queryWrapper2.eq(SysQueryVersionFeild::getFversionid, version.getId());
				 return SysQueryVersionFeildMapper.selectList(queryWrapper2);
			}else {
				return null;
			}
		}

		@Override
		public List<SysQueryVersionFeild> getVersionFieldById(String id) {
			LambdaQueryWrapper<SysQueryVersionFeild> queryWrapper=new LambdaQueryWrapper<SysQueryVersionFeild>();
			queryWrapper.eq(SysQueryVersionFeild::getFversionid, new BigInteger(id));
			return SysQueryVersionFeildMapper.selectList(queryWrapper);
		}
}

package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.common.util.PasswordHelper;
import com.wimoor.admin.mapper.SysUserDatalimitMapper;
import com.wimoor.admin.mapper.SysUserGroupMapper;
import com.wimoor.admin.mapper.SysUserInfoMapper;
import com.wimoor.admin.mapper.SysUserMapper;
import com.wimoor.admin.mapper.SysUserShopMapper;
import com.wimoor.admin.pojo.dto.UserDTO;
import com.wimoor.admin.pojo.dto.UserInsertDTO;
import com.wimoor.admin.pojo.entity.SysDictItem;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.entity.SysUserDatalimit;
import com.wimoor.admin.pojo.entity.SysUserGroup;
import com.wimoor.admin.pojo.entity.SysUserInfo;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.pojo.vo.UserVO;
import com.wimoor.admin.service.ISysDictItemService;
import com.wimoor.admin.service.ISysRoleService;
import com.wimoor.admin.service.ISysUserDatalimitService;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.admin.service.ISysUserWechatMPService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserType;
import com.wimoor.util.SpringUtil;
import com.wimoor.util.UUIDUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * 用户业务类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	private final ISysUserWechatMPService iSysUserWechatMPService;
	
	private final ISysUserRoleService iSysUserRoleService;
 
	private final ISysRoleService iSysRoleService;
	
	private final SysUserShopMapper sysUserShopMapper;
	
	private final SysUserInfoMapper sysUserInfoMapper;
	
	private final SysUserGroupMapper sysUserGroupMapper;
	
	private final SysUserDatalimitMapper sysUserDatalimitMapper;
	
	private final ISysUserDatalimitService iSysUserDatalimitService;
	
	private final ISysDictItemService iSysDictItemService;
	public BigInteger getShortUUID() {
		// TODO Auto-generated method stub
		return this.baseMapper.getShortUUID();
	}
 
	public SysUser findOneByAccountOrEmail(String account) {
		List<SysUser> list = this.baseMapper.findByAccountOrEmail(account);
		if (list != null && list.size() > 0) {
			SysUser user = list.get(0);
			return user;
		} else {
			return null;
		}
	}
	
	public String getUserShopByUser(SysUser user) {
		SysUser leader = user;
		while (!leader.getId().equals(leader.getLeaderId())) {
			user = leader;
			leader = this.getById(leader.getLeaderId());
		}
		String shopid =this.baseMapper.findShopIdByUserId(user.getId());
		if (shopid == null) {
			QueryWrapper<SysUserRole> query=new QueryWrapper<SysUserRole>();
			query.eq("userId", user.getId());
			List<SysUserRole> rolelist = iSysUserRoleService.list(query);
			for (SysUserRole role : rolelist) {
				SysRole mrole = iSysRoleService.getById(role.getRoleId());
				if (mrole.getType().equals("support")) {
					return this.baseMapper.findShopIdByUserId(leader.getId());
				}
			}
		}
		return shopid;
	}
	
 
	public SysUser getUserAllByAccount(String account) {
		//account="18824232880";
		SysUser m_user = this.findOneByAccountOrEmail(account);
		String shopid = getUserShopByUser(m_user);
		m_user.setShopid(shopid);
		m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
		return m_user;
	}
	
	public SysUser getUserAllByOpenid(String openid) {
		//account="18824232880";
		SysUserWechatMP user_wechatMp=iSysUserWechatMPService.getById(openid);
		if(user_wechatMp==null) {
			return null;
		}
		SysUser m_user = this.baseMapper.selectById(user_wechatMp.getUserid());
		if(m_user==null) {
			return null;
		}
		String shopid = getUserShopByUser(m_user);
		m_user.setShopid(shopid);
		m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
		return m_user;
	}

	public SysUser getUserAllById(String id) {
		SysUser m_user = this.baseMapper.selectById(id);
		if(m_user==null) {
			return null;
		}
		String shopid = getUserShopByUser(m_user);
		if(shopid==null) {
			return null;
		}
		m_user.setShopid(shopid);
		m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
		return m_user;
	}
	
	public Map<String, Object> getUserInfoById(String id) {
		return this.baseMapper.findUserInfoById(id);
	}
	
	@Override
	public SysUser bindOpenId(String openid, String account, String password) {
		// TODO Auto-generated method stub
		SysUser m_user = this.findOneByAccountOrEmail(account);
		if(m_user==null) {
			throw new BizException("账号不存在");
		}
		if(m_user.getPassword().equals(PasswordHelper.encryptPassword(account, password, m_user.getSalt()))) {
			
			this.addUserWechatMPInfo(m_user,null,null,openid,"app");
			String shopid = getUserShopByUser(m_user);
			m_user.setShopid(shopid);
			m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
			return m_user;
		}else{
			throw new BizException("账号或密码有误");
		}
		
	}
	
	@Override
	public SysUser verifyAccount(String account, String password) {
		// TODO Auto-generated method stub
		SysUser m_user = this.findOneByAccountOrEmail(account);
		if(m_user==null) {
			throw new BizException("账号不存在");
		}
		if(m_user.getPassword().equals(PasswordHelper.encryptPassword(account, password, m_user.getSalt()))) {
			
			String shopid = getUserShopByUser(m_user);
			m_user.setShopid(shopid);
			m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
			return m_user;
		}else{
			throw new BizException("账号或密码有误");
		}
		
	}
	
	@Override
	public UserInfo convertToUserInfo(SysUser user) {
		// TODO Auto-generated method stub
		 if(user==null)return null;
		 else {
			 UserInfo info=new UserInfo();
			 info.setAccount(user.getAccount());
			 info.setCreateDate(user.getCreateDate());
			 info.setLastloginip(user.getLastloginip());
			 info.setLastlogintime(user.getLastlogintime());
			 info.setLastloginsession(user.getLastloginsession());
			 info.setHasEmail(user.getHasEmail());
			 info.setCompanyid(user.getShopid());
			 info.setFtype(user.getFtype());
			 info.setIsActive(user.getIsActive());
			 info.setId(user.getId());
			 info.setUserinfo(user.getUserinfo());
			 info.setMember(user.getMember());
			 info.setPasswordkey(user.getPasswordkey());
			 info.setLogicDelete(user.getLogicDelete());
			 info.setLosingeffect(user.getLosingeffect());
			 info.setDisable(user.getDisable());
			 info.setUserinfo(user.getUserinfo());
			 info.setDeptid(user.getDeptid());
			 LambdaQueryWrapper<SysUserGroup> query=new LambdaQueryWrapper<SysUserGroup>();
    		 query.eq(SysUserGroup::getUserid, user.getId());
    		 List<SysUserGroup> grouplist = sysUserGroupMapper.selectList(query);
             List<String> groups = grouplist.stream().map(item -> item.getGroupid().toString()).collect(Collectors.toList());
             info.setGroups(groups);
	    		
	    	 LambdaQueryWrapper<SysUserRole> queryRole=new LambdaQueryWrapper<SysUserRole>();
	    	 queryRole.eq(SysUserRole::getUserId, user.getId());
			 List<SysUserRole> rolelist = iSysUserRoleService.list(queryRole);
			 boolean isadmin=false;
	         boolean ismanager=false;
	         List<String> roles =new LinkedList<String>();
	         for(SysUserRole item:rolelist) {
	        	SysRole mrole = iSysRoleService.getById(item.getRoleId());
	        	if(mrole.getType().equals("admin")&&mrole.getIssystem()) {
	    			isadmin=true;break;
	    		}else if(mrole.getType().equals("manager")&&mrole.getIssystem()) {
	    			ismanager=true;
	    		}
	        	roles.add(item.getRoleId().toString());
	        }
			 info.setRoles(roles);
			 
			 LambdaQueryWrapper<SysUserDatalimit> queryLimit=new LambdaQueryWrapper<SysUserDatalimit>();
			 queryLimit.eq(SysUserDatalimit::getUserid, user.getId());
    		 List<SysUserDatalimit> datalimitList = sysUserDatalimitMapper.selectList(queryLimit);
    		 List<String> datalimits = datalimitList.stream().filter(item -> item.getIslimit()).map(item -> item.getDatatype().toString()).collect(Collectors.toList());
    		 info.setDatalimits(datalimits);
    		 
    		if(isadmin) {
    			info.setUsertype(UserType.admin.getCode());
    		}else if(ismanager) {
    			info.setUsertype(UserType.manager.getCode());
    		}else {
    			info.setUsertype(UserType.actor.getCode());
    		}
    		    
    		return info;
		 }
		
	}

	private boolean addUserWechatMPInfo(SysUser m_user,  String accesstoken, String refreshtoken, String openid, String ftype) {
		// TODO Auto-generated method stub
		SysUserWechatMP record=new SysUserWechatMP();
		record.setAccessToken(accesstoken);
		if(StrUtil.isBlank(openid)) {
			throw new BizException("openid无法获取");
		}
		record.setOpenid(openid);
		record.setRefreshToken(refreshtoken);
		record.setUserid(m_user.getId());
		record.setFtype(ftype);
		return iSysUserWechatMPService.save(record);
	}

	@Override
	public IPage<UserVO> listQuery(Page<?> page, UserDTO dto) {
		// TODO Auto-generated method stub
		if(GeneralUtil.isNotEmpty(dto.getName())) {
			if(!dto.getName().contains("%")) {
				dto.setName("%"+dto.getName().trim()+"%");
			}
		}else {
			dto.setName(null);
		}
		IPage<UserVO> result=this.baseMapper.listQuery(page,dto);
		for(int i=0;i<result.getRecords().size();i++) {
			UserVO uv=result.getRecords().get(i);
			List<String> roles=iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, uv.getId()))
            .stream().map(item -> {
            	SysRole role = iSysRoleService.getById(item.getRoleId());
            	if(role!=null) {
            		return role.getName();
            	}
            	return item.getRoleId().toString();}).collect(Collectors.toList());
			
			  List<SysUserGroup> groups = sysUserGroupMapper.selectList(new LambdaQueryWrapper<SysUserGroup>().eq(SysUserGroup::getUserid, uv.getId()));
			  if(groups!=null&groups.size()>0) {
				  RestTemplate restTemplate=SpringUtil.getBean("restTemplateApi");
				  List<String> groupnamelist=new LinkedList<String>();
				  for(SysUserGroup item:groups) {
					  try {
						  Result<?> resultv = restTemplate.getForObject("http://wimoor-amazon/amazon/api/v1/amzgroup/id/"+item.getGroupid(),Result.class);
					      if(Result.isSuccess(resultv)&&resultv.getData()!=null) {
					    	 Map<String, Object> group = BeanUtil.beanToMap(resultv.getData());
					    	 if(group.get("name")!=null) {
					    		 groupnamelist.add(group.get("name").toString());
					    	 }
					      }
					  }catch(Exception e) {
						  e.printStackTrace();
					  }
					 
				  }
				  uv.setGroups(groupnamelist);
			  }
			   
			  List<SysUserDatalimit> limits = iSysUserDatalimitService.list(new LambdaQueryWrapper<SysUserDatalimit>().eq(SysUserDatalimit::getUserid, uv.getId()));
			  if(limits!=null&&limits.size()>0) {
				  List<String> limit=new LinkedList<String>();
				  for(SysUserDatalimit item:limits) {
					  if(item.getIslimit()) {
						  SysDictItem dict = iSysDictItemService.getOne(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictCode, "limit_data_type")
								                                                          .eq(SysDictItem::getValue, item.getDatatype()));
						  limit.add(dict.getName());
					  }
				  }
				  uv.setPerms(limit);
			  }
			  uv.setRoles(roles);
		}
		return result;
	}

	public int unbindWechat(UserInfo userInfo, String ftype) {
		QueryWrapper<SysUserWechatMP> queryWrapper=new QueryWrapper<SysUserWechatMP>();
		queryWrapper.eq("userid", userInfo.getId());
		queryWrapper.eq("ftype", ftype);
		// TODO Auto-generated method stub
		return iSysUserWechatMPService.remove(queryWrapper)?1:0;
	}

	@Override
	public int unbindAccount(UserInfo userInfo) {
		// TODO Auto-generated method stub
		SysUser user = this.baseMapper.selectById(userInfo.getId());
		user.setPasswordkey(userInfo.getAccount());
		user.setLogicDelete(true);
		user.setAccount(user.getId());
		user.setDisable(true);
		return this.baseMapper.updateById(user);
	}

	private  List<SysUser> findUserByOpenid(String openid,String type) {
		// TODO Auto-generated method stub
		QueryWrapper<SysUserWechatMP> queryWrapper=new QueryWrapper<SysUserWechatMP>();
		queryWrapper.eq("openid", openid);
		queryWrapper.eq("ftype",type);
		List<SysUser> result=new ArrayList<SysUser>();
		List<SysUserWechatMP> userList=iSysUserWechatMPService.list(queryWrapper);
		if(userList!=null&&userList.size()>0) {
			for(SysUserWechatMP usermp:userList) {
				SysUser m_user = this.baseMapper.selectById(usermp.getUserid());
				if(m_user==null) {
					continue;
				}
				String shopid = getUserShopByUser(m_user);
				m_user.setShopid(shopid);
				m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
				result.add(m_user);
			}
		}
	   return result;
	} 
	
	@Override
	public List<SysUser> findAppUserByOpenid(String openid) {
		// TODO Auto-generated method stub
		return findUserByOpenid(openid,"app");
	}

	@Override
	public List<SysUser> findMpUserByOpenid(String openid) {
		// TODO Auto-generated method stub
		return findUserByOpenid(openid,"mp");
	}

	@Override
	public boolean saveUser(UserInsertDTO userDTO, UserInfo operatorUserInfo) {
		// TODO Auto-generated method stub
		BigInteger leaderid = sysUserShopMapper.findByCompanyId(new BigInteger(operatorUserInfo.getCompanyid()));
		if(leaderid==null) {
			throw new BizException("未找到对应公司");
		}
		LambdaQueryWrapper<SysUser> query=new LambdaQueryWrapper<SysUser>();
		query.eq(SysUser::getAccount, userDTO.getAccount());
		if(this.baseMapper.selectCount(query)>0) {
			throw new BizException("账号已存在，使用其他账号或者联系客服注销此账号");
		}
		SysUser user=new SysUser();
		user.setAccount(userDTO.getAccount());
    	user.setCreateDate(new Date());
    	user.setLeaderId(leaderid.toString());
    	user.setIsActive(true);
    	user.setDisable(userDTO.getDisable());
    	user.setLogicDelete(false);
    	user.setLosingeffect(userDTO.getLosingEffect());
    	user.setHasEmail(false);
    	user.setOpttime(new Date());
    	user.setSalt(PasswordHelper.initRoundomSalt());
    	String password=PasswordHelper.encryptPassword(userDTO.getAccount(), userDTO.getPassword(), user.getSalt());
    	user.setPassword(password);
    	user.setId(UUIDUtil.getUUIDshort());
    	this.baseMapper.insert(user);
    	
    	SysUserInfo userinfo=new SysUserInfo();
    	userinfo.setId(user.getId());
    	userinfo.setName(userDTO.getName());
    	sysUserInfoMapper.insert(userinfo);
    	
    	if(userDTO.getRoles()!=null&&userDTO.getRoles().size()>0) {
    		List<SysUserRole> list=new ArrayList<SysUserRole>();
    		for(String roleid:userDTO.getRoles()) {
    			SysUserRole ur=new SysUserRole();
    			ur.setRoleId(new BigInteger(roleid));
    			ur.setUserId(new BigInteger(user.getId()));
    		    list.add(ur);
    		}
    		iSysUserRoleService.saveBatch(list);
    	}
    	
    	if(userDTO.getGroups()!=null&&userDTO.getGroups().size()>0) {
    		for(String groupid:userDTO.getGroups()) {
    				SysUserGroup ug=new SysUserGroup();
        			ug.setGroupid(new BigInteger(groupid));
        			ug.setUserid(new BigInteger(user.getId()));
        			sysUserGroupMapper.insert(ug);
    		}
    	}
    	
    	if(userDTO.getDatalimits()!=null&&userDTO.getDatalimits().size()>0) {
    		for(String dataType:userDTO.getDatalimits()) {
    			    SysUserDatalimit ug=new SysUserDatalimit();
        			ug.setDatatype(dataType);
        			ug.setUserid(new BigInteger(user.getId()));
        			ug.setIslimit(true);
        			sysUserDatalimitMapper.insert(ug);
    		}
    	}
		return true;
	}

	@Override
	public boolean updateUser(UserInsertDTO userDTO, UserInfo operatorUserInfo) {
		// TODO Auto-generated method stub
		BigInteger leaderid = sysUserShopMapper.findByCompanyId(new BigInteger(operatorUserInfo.getCompanyid()));
		if(leaderid==null) {
			throw new BizException("未找到对应公司");
		}
		SysUser user=this.baseMapper.selectById(userDTO.getId());
		user.setAccount(userDTO.getAccount());
    	user.setCreateDate(new Date());
    	user.setLeaderId(leaderid.toString());
    	user.setIsActive(true);
    	user.setDisable(userDTO.getDisable());
    	user.setLogicDelete(false);
    	user.setOpttime(new Date());
    	user.setDeptid(userDTO.getDeptid());
    	user.setLosingeffect(userDTO.getLosingEffect());
    	if(StrUtil.isNotBlank(userDTO.getPassword())) {
    		user.setSalt(PasswordHelper.initRoundomSalt());
        	String password=PasswordHelper.encryptPassword(userDTO.getAccount(), userDTO.getPassword(), user.getSalt());
        	user.setPassword(password);
    	}
    	this.baseMapper.updateById(user);
    	
    	SysUserInfo userinfo=sysUserInfoMapper.selectById(user.getId());
    	userinfo.setId(user.getId());
    	userinfo.setName(userDTO.getName());
    	sysUserInfoMapper.updateById(userinfo);
		List<SysUserRole> list=new ArrayList<SysUserRole>();
		
		LambdaQueryWrapper<SysUserRole> queryRole=new LambdaQueryWrapper<SysUserRole>();
		queryRole.eq(SysUserRole::getUserId, user.getId());
		iSysUserRoleService.remove(queryRole);
    	if(userDTO.getRoles()!=null&&userDTO.getRoles().size()>0) {
    		for(String roleid:userDTO.getRoles()) {
    			SysUserRole ur=new SysUserRole();
    			ur.setRoleId(new BigInteger(roleid));
    			ur.setUserId(new BigInteger(user.getId()));
    		    list.add(ur);
    		}
    		iSysUserRoleService.saveBatch(list);
    	}
    	LambdaQueryWrapper<SysUserGroup> queryGroup=new LambdaQueryWrapper<SysUserGroup>();
    	queryGroup.eq(SysUserGroup::getUserid, user.getId());
		sysUserGroupMapper.delete(queryGroup);
    	if(userDTO.getGroups()!=null&&userDTO.getGroups().size()>0) {
    		for(String groupid:userDTO.getGroups()) {
    				SysUserGroup ug=new SysUserGroup();
        			ug.setGroupid(new BigInteger(groupid));
        			ug.setUserid(new BigInteger(user.getId()));
        			sysUserGroupMapper.insert(ug);
    		}
    	}
  		LambdaQueryWrapper<SysUserDatalimit> querylimit=new LambdaQueryWrapper<SysUserDatalimit>();
  		querylimit.eq(SysUserDatalimit::getUserid, user.getId());
    	sysUserDatalimitMapper.delete(querylimit);
    	if(userDTO.getDatalimits()!=null&&userDTO.getDatalimits().size()>0) {
    		for(String dataType:userDTO.getDatalimits()) {
    			    SysUserDatalimit ug=new SysUserDatalimit();
        			ug.setDatatype(dataType);
        			ug.setUserid(new BigInteger(user.getId()));
        			ug.setIslimit(true);
        			sysUserDatalimitMapper.insert(ug);
    		}
    	}
		return true;
	}
 
	
}
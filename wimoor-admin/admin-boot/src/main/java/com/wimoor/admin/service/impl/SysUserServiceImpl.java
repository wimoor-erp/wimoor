package com.wimoor.admin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.common.util.PasswordHelper;
import com.wimoor.admin.mapper.SysCompanyMapper;
import com.wimoor.admin.mapper.SysUserDatalimitMapper;
import com.wimoor.admin.mapper.SysUserGroupMapper;
import com.wimoor.admin.mapper.SysUserInfoMapper;
import com.wimoor.admin.mapper.SysUserMapper;
import com.wimoor.admin.mapper.SysUserShopMapper;
import com.wimoor.admin.pojo.dto.UserDTO;
import com.wimoor.admin.pojo.dto.UserInsertDTO;
import com.wimoor.admin.pojo.dto.UserRegisterInfoDTO;
import com.wimoor.admin.pojo.entity.SysCompany;
import com.wimoor.admin.pojo.entity.SysDictItem;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.entity.SysUserBind;
import com.wimoor.admin.pojo.entity.SysUserDatalimit;
import com.wimoor.admin.pojo.entity.SysUserGroup;
import com.wimoor.admin.pojo.entity.SysUserInfo;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.pojo.entity.SysUserShop;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.pojo.vo.UserVO;
import com.wimoor.admin.service.ISysDictItemService;
import com.wimoor.admin.service.ISysRoleService;
import com.wimoor.admin.service.ISysUserBindService;
import com.wimoor.admin.service.ISysUserDatalimitService;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.admin.service.ISysUserWechatMPService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserType;
import com.wimoor.manager.pojo.entity.ManagerLimit;
import com.wimoor.manager.pojo.entity.SysTariffPackages;
import com.wimoor.manager.service.IManagerLimitService;
import com.wimoor.manager.service.ISysTariffPackagesService;
import com.wimoor.sys.email.service.ISysMailManagerService;
import com.wimoor.util.SpringUtil;
import com.wimoor.util.UUIDUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.URLEncoder;
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
	
	private final SysCompanyMapper sysCompanyMapper ;
	
	private final ISysTariffPackagesService iSysTariffPackagesService;
	
	private final IManagerLimitService iManagerLimitService;
	
	private final IPictureService pictureService;
	
	private final ISysUserBindService iSysUserBindService;
	
	private final ISysMailManagerService iSysMailManagerService;
	
	@Value("${sys.admin.id}")
	String adminid;
	public String getAdminid() {
		return adminid;
	}
 
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
			LambdaQueryWrapper<SysUserRole> query=new LambdaQueryWrapper<SysUserRole>();
			query.eq(SysUserRole::getUserId, user.getId());
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
	
    public void setUserInfoMap(SysUser m_user) {
    	Map<String, Object> map = this.baseMapper.findUserInfoById(m_user.getId());
		String companyname=getCompanyName(m_user.getShopid());
		if(map!=null) {
			if(companyname!=null) {
				map.put("companyname", URLEncoder.DEFAULT.encode(companyname,Charset.defaultCharset()));
			}
		}else {
			map=new HashMap<String,Object>();
			if(companyname!=null) {
				map.put("companyname", URLEncoder.DEFAULT.encode(companyname,Charset.defaultCharset()));
			}
		}
		m_user.setUserinfo(map);
    }
    
    public String getCompanyName(String companyid) {
		SysCompany company = sysCompanyMapper.selectById(companyid);
		String companyname=null;
		if(company.getName()!=null) {
			companyname=company.getName();
		}
		return companyname;
    }
    
	public SysUser getUserAllByAccount(String account) {
		SysUser m_user = this.findOneByAccountOrEmail(account);
		if(m_user==null) {
			throw new BizException("无法找到对应用户,已被删除或注销");
		}
		String shopid = getUserShopByUser(m_user);
		m_user.setShopid(shopid);
		setUserInfoMap(m_user);
		return m_user;
	}
	
	public SysUser getUserAllByOpenid(String openid) {
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
		setUserInfoMap(m_user);
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
		setUserInfoMap(m_user);
		return m_user;
	}
	
	public Map<String, Object> getUserInfoById(String id) {
		return this.baseMapper.findUserInfoById(id);
	}
	
	@Override
	public SysUser bindOpenId(String openid,String appType, String account, String password) {
		// TODO Auto-generated method stub
		SysUser m_user = this.findOneByAccountOrEmail(account);
		if(m_user==null) {
			throw new BizException("账号不存在");
		}
		if(m_user.getPassword().equals(PasswordHelper.encryptPassword(account, password, m_user.getSalt()))) {
			
			if(this.addUserWechatMPInfo(m_user,null,null,openid,appType)) {
				String shopid = getUserShopByUser(m_user);
				m_user.setShopid(shopid);
				m_user.setUserinfo( this.baseMapper.findUserInfoById(m_user.getId()));
				return m_user;
			}else {
				throw new BizException("绑定失败");
			}
			
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
		if(m_user.getLosingeffect().before(new Date())) {
			throw new BizException("账号已过期,"+m_user.getLastlogintime());
		}
		if(m_user.getDisable()) {
			throw new BizException("账号已停用");
		}
		if(m_user.getLogicDelete()) {
			throw new BizException("账号已删除");
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
	         boolean issupport=false;
	         List<String> roles =new LinkedList<String>();
	         for(SysUserRole item:rolelist) {
	        	SysRole mrole = iSysRoleService.getById(item.getRoleId());
	        	if(mrole.getType().equals("admin")&&mrole.getIssystem()) {
	    			isadmin=true;break;
	    		}else if(mrole.getType().equals("manager")&&mrole.getIssystem()) {
	    			ismanager=true;
	    		}else if(mrole.getType().equals("support")&&mrole.getIssystem()) {
	    			issupport=true;
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
    		}else if(issupport) {
    			info.setUsertype(UserType.support.getCode());
    		}else {
    			info.setUsertype(UserType.actor.getCode());
    		}
    		    
    		return info;
		 }
		
	}
    
	private boolean addUserWechatMPInfo(SysUser m_user,  String accesstoken, String refreshtoken, String openid, String ftype) {
		// TODO Auto-generated method stub
		if(ftype.equals("website")) {
			return iSysUserBindService.save(m_user.getId(),openid);
		}else {
			SysUserWechatMP record=new SysUserWechatMP();
			record.setAccessToken(accesstoken);
			if(StrUtil.isBlank(openid)) {
				throw new BizException("openid无法获取");
			}
			record.setOpenid(openid);
			record.setRefreshToken(refreshtoken);
			record.setUserid(m_user.getId());
			record.setFtype(ftype);
			
			LambdaQueryWrapper<SysUserWechatMP> query=new LambdaQueryWrapper<SysUserWechatMP>();
			query.eq(SysUserWechatMP::getFtype, ftype);
			query.eq(SysUserWechatMP::getUserid, m_user.getId());
			SysUserWechatMP old = iSysUserWechatMPService.getOne(query);
			Boolean result=false;
			if(old!=null) {
				result= iSysUserWechatMPService.update(record, query);
			}else {
				result= iSysUserWechatMPService.save(record);
			}
			iSysUserBindService.bindMpUser( openid, m_user.getId());
			return result;
		}
		
		
	
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
	public List<SysUser> findAppUserByOpenid(String openid,String appType) {
		// TODO Auto-generated method stub
		return findUserByOpenid(openid,appType);
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
		if(operatorUserInfo.getId().equals(adminid)) {
			UserRegisterInfoDTO dto=new UserRegisterInfoDTO();
			dto.setAccount(userDTO.getAccount());
			dto.setPassword(userDTO.getPassword());
			dto.setName(userDTO.getName());
			if(StrUtil.isEmpty(userDTO.getCompany())) {
				dto.setCompany(userDTO.getName());
			}else {
				dto.setCompany(userDTO.getCompany());
			}
			dto.setFtype("mobile");
			saveRegister( dto);
			return true;
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
	public boolean updateUserSelf(UserInsertDTO userDTO, UserInfo operatorUserInfo) {
		    int result=0;
			SysUserInfo userinfo=sysUserInfoMapper.selectById(operatorUserInfo.getId());
	    	userinfo.setName(userDTO.getName());
	    	result=sysUserInfoMapper.updateById(userinfo);
			if(operatorUserInfo.getUsertype()!=null&&operatorUserInfo.getUsertype().equals("manager")) {
				if(StrUtil.isNotEmpty(userDTO.getCompany())) {
					SysCompany comp = sysCompanyMapper.selectById(operatorUserInfo.getCompanyid());
					if(comp==null) {
						 throw new BizException("账户异常，请联系管理员");
					}else {
						comp.setName(userDTO.getCompany());
						 sysCompanyMapper.updateById(comp);
					}
					
				}
			}
			return result>0;
	}
	@Override
	public boolean updateUser(UserInsertDTO userDTO, UserInfo operatorUserInfo) {
		// TODO Auto-generated method stub
		BigInteger leaderid = sysUserShopMapper.findByCompanyId(new BigInteger(operatorUserInfo.getCompanyid()));
		if(leaderid==null) {
			throw new BizException("未找到对应公司");
		}
		SysUser user=this.baseMapper.selectById(userDTO.getId());
    	user.setLeaderId(leaderid.toString());
    	user.setIsActive(true);
    	user.setDisable(userDTO.getDisable());
    	user.setLogicDelete(false);
    	user.setOpttime(new Date());
		user.setAccount(userDTO.getAccount());
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

	@Override
	public SysUser saveRegister(UserRegisterInfoDTO dto) {
		// TODO Auto-generated method stub
		SysUser existUser =this.findOneByAccountOrEmail(dto.getAccount());
		if (existUser != null) {
			boolean logicDelete = existUser.getLogicDelete();
			if (!logicDelete && existUser.getIsActive()) {// 如果账号没有被删除,提示账号已经被注册使用。如果已经被删除，允许再次注册。
				 throw new BizException("账号已存在，请重新注册！");
			} else  {
				existUser.setPasswordkey(existUser.getAccount());
				existUser.setAccount(existUser.getId());
				existUser.setLogicDelete(true);
				this.baseMapper.updateById(existUser);
				SysUserInfo existinfo = sysUserInfoMapper.selectById(existUser.getId());
				existinfo.setEmail(existUser.getId());
				existinfo.setRemark("账号已被重新注册，自动删除旧账号");
				sysUserInfoMapper.updateById(existinfo);
			}
		}
	 
		SysUser user = new SysUser();
		user.setFtype(dto.getFtype());
		user.setAccount(dto.getAccount());
		user.setSalt(UUID.randomUUID().toString());
		user.setPassword(PasswordHelper.encryptPassword(dto.getAccount(),dto.getPassword(),user.getSalt()));
		user.setLeaderId(adminid);
		Calendar c = Calendar.getInstance();
		user.setCreateDate(c.getTime());
		c.add(Calendar.MONTH, 1200);
		user.setLosingeffect(c.getTime());
		user.setLogicDelete(false);
		user.setDisable(false);
		if (dto.getFtype().equals("email")) {
			user.setIsActive(false);// 修改：注册的账号先开始不可用，激活之后才可用，2019-06-18
		} else {
			user.setIsActive(true);
		}
		user.setHasEmail(false);
	    this.save(user);
		SysUserInfo userInfo = new SysUserInfo();
		userInfo.setName(dto.getName());
		userInfo.setCompany(dto.getCompany());
		userInfo.setEmail(dto.getEmail());
		userInfo.setId(user.getId());
		sysUserInfoMapper.insert(userInfo);
		
		SysCompany company = new SysCompany();
		company.setName(dto.getCompany());
		company.setFromcode(dto.getSalekey());
		company.setInvitecode(dto.getInvitecode());
		sysCompanyMapper.insert(company);
		
		LambdaQueryWrapper<SysTariffPackages> query=new LambdaQueryWrapper<SysTariffPackages>();
		query.eq(SysTariffPackages::getIsdefault, Boolean.TRUE);
		SysTariffPackages tariffPackage = iSysTariffPackagesService.getOne(query);
		SysRole role = iSysRoleService.getById(tariffPackage.getRoleId());
		SysUserRole userRole = new SysUserRole();
		userRole.setUserId(new BigInteger(user.getId()));
		userRole.setRoleId(role.getId());
		iSysUserRoleService.save(userRole);
		
		SysUserShop userShop = new SysUserShop();
		userShop.setUserId(user.getId());
		userShop.setShopId(company.getId());
		sysUserShopMapper.insert(userShop);
		// 添加管理员权限
		ManagerLimit managerLimit = new ManagerLimit();
		managerLimit.setTariffpackage(tariffPackage.getId());
		managerLimit.setMaxOrderCount(tariffPackage.getMaxOrderCount());
		managerLimit.setMaxMember(tariffPackage.getMaxMember());
		managerLimit.setMaxProfitPlanCount(tariffPackage.getMaxProfitPlanCount());
		managerLimit.setMaxdayOpenAdvCount(tariffPackage.getDayOpenAdvCount());
		managerLimit.setMaxShopCount(tariffPackage.getMaxShopCount());
		managerLimit.setMaxProductCount(tariffPackage.getMaxProductCount());
		managerLimit.setMaxMarketCount(tariffPackage.getMaxMarketCount());
		managerLimit.setExistdayOpenAdvCount(0);
		managerLimit.setExistMarketCount(0);
		managerLimit.setExistMember(0);
		managerLimit.setExistOrderCount(0);
		managerLimit.setExistProductCount(0);
		managerLimit.setExistProfitPlanCount(0);
		managerLimit.setExistShopCount(0);
		managerLimit.setShopId(new BigInteger(company.getId()));
		managerLimit.setOpratetime(LocalDateTime.now());
		managerLimit.setCreatetime(LocalDateTime.now());
		managerLimit.setLosingEffect(LocalDate.parse("2099-01-01"));
		iManagerLimitService.save(managerLimit);
		if(StrUtil.isNotBlank(dto.getEmail())) {
			this.sendWelcomeEmail(dto.getEmail());
		}
		return user;
	}

	@Override
	public void  changePasswordSelf(UserInfo info, UserRegisterInfoDTO dto){
		// TODO Auto-generated method stub
		SysUser user = this.verifyAccount(dto.getAccount(), dto.getOldpassword());
		if(user!=null&&user.getId().equals(info.getId())) {
			user.setSalt(UUID.randomUUID().toString());
			user.setPassword(PasswordHelper.encryptPassword(dto.getAccount(),dto.getPassword(),user.getSalt()));
		    this.updateById(user);
		}else {
			throw new BizException("未找到对应账号");
		}
	}
	
	@Override
	public SysUser changePassword(UserRegisterInfoDTO dto) {
		// TODO Auto-generated method stub
		SysUser existUser =this.findOneByAccountOrEmail(dto.getAccount());
		if(existUser!=null) {
			existUser.setSalt(UUID.randomUUID().toString());
			existUser.setPassword(PasswordHelper.encryptPassword(dto.getAccount(),dto.getPassword(),existUser.getSalt()));
		    this.updateById(existUser);
		}else {
			throw new BizException("未找到对应账号");
		}
		return existUser;
	}

	@Override
	public List<Map<String, Object>> findOwnerAll(String shopid, String search) {
		return this.baseMapper.findOwnerAll(shopid, search);
	}

	@Override
	public Map<String, Object> detail(UserInfo userInfo) {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<String,Object>();
		SysCompany company = sysCompanyMapper.selectById(userInfo);
		result.put("company", company);
		result.put("user", userInfo);
		return result;
	}

	@Override
	public void saveImage(MultipartFile file, UserInfo userinfo) {
		// TODO Auto-generated method stub
		try {
			this.uploadImg(userinfo,file.getInputStream(), file.getOriginalFilename());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int uploadImg(UserInfo userinfo, InputStream inputStream, String filename) {
		 int result=0;
		 String pictureid =null;
		 SysUserInfo m_user = sysUserInfoMapper.selectById(userinfo.getId());
		 if(m_user!=null&&m_user.getPicture()!=null) {
			 pictureid=m_user.getPicture();
		 }
		try {
			if(StrUtil.isNotEmpty(filename)) {
				String filePath = PictureServiceImpl.userImgPath + userinfo.getCompanyid();
				int len = filename.lastIndexOf(".");
				String imgtype=filename.substring(len, filename.length());
				filename=userinfo.getId()+"-"+System.currentTimeMillis()+imgtype;
				Picture picture = pictureService.uploadPicture(inputStream, null, filePath, filename, pictureid);
				if(picture!=null) {
                    if(m_user!=null) {
                    	m_user.setPicture(picture.getId());
						sysUserInfoMapper.updateById(m_user);
                    }else {
                    	m_user=new SysUserInfo();
                    	m_user.setId(userinfo.getId());
                    	m_user.setPicture(picture.getId());
						sysUserInfoMapper.insert(m_user);
                    }
					
					result=1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<SysUser> findBindList(UserInfo userInfo) {
		// TODO Auto-generated method stub
		  List<SysUserBind> bindlist = this.iSysUserBindService.getBindListUser(userInfo.getId());
		  List<SysUser> result=new ArrayList<SysUser>();
		  for(SysUserBind item:bindlist) {
			  SysUser user = this.getUserAllById(item.getUserid().toString());
			  user.setPassword("***");
			  user.setSalt("***");
			  result.add(user);
		  }
		  return result;
	}

	@Override
	public void changeAccountSelf(UserInfo info, UserRegisterInfoDTO dto) {
		// TODO Auto-generated method stub
		SysUser existUser =this.findOneByAccountOrEmail(dto.getAccount());
		if(existUser!=null) {
			throw new BizException(dto.getAccount()+"在系统中已存在");
		}
		 SysUser sysuser = this.baseMapper.selectById(info.getId());
		if(sysuser!=null&&sysuser.getId().equals(info.getId())) {
			sysuser.setAccount(dto.getAccount());
			sysuser.setSalt(UUID.randomUUID().toString());
			sysuser.setPassword(PasswordHelper.encryptPassword(dto.getAccount(),dto.getPassword(),sysuser.getSalt()));
		    this.updateById(sysuser);
		}else {
			throw new BizException("未找到对应账号");
		}
	}

	public void sendEmail(String account, String code,String type) {
	 
		// 获取当前服务IP
		String subject="";
		if (type != null && type.contains("losepassword")) {
			subject="重设密码";
		} else if(type != null && type.contains("changephone")) {
			subject="修改账户";
		}else {
			subject="激活邮箱";
		}
		 
		iSysMailManagerService.sendCode(subject, account, code);
		
	}

	public void sendWelcomeEmail(String email) {
		iSysMailManagerService.sendWelcome(email);
	}

	@Override
	public void changeEmailSelf(UserInfo info, UserRegisterInfoDTO dto) {
		// TODO Auto-generated method stub
		SysUserInfo extinfo = this.sysUserInfoMapper.selectById(info.getId());
		extinfo.setEmail(dto.getEmail());
		SysUser user = this.baseMapper.selectById(info.getId());
		user.setHasEmail(true);
		this.baseMapper.updateById(user);
		sysUserInfoMapper.updateById(extinfo);
	}

	@Override
	public void mergeData(UserInfo fromuser, SysUser touser) {
		// TODO Auto-generated method stub
		if(touser.getLeaderId().equals(adminid)) {
			this.baseMapper.mergeAccountData(fromuser.getAccount(), touser.getAccount());
		}else {
			throw new BizException("必须迁移到主账号，请确认您输入的账号类型");
		}
		
	}


}
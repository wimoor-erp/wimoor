package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.common.util.PasswordHelper;
import com.wimoor.admin.mapper.SysUserMapper;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.pojo.vo.UserVO;
import com.wimoor.admin.service.ISysRoleService;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.admin.service.ISysUserService;
import com.wimoor.admin.service.ISysUserWechatMPService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

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
	public IPage<UserVO> listQuery(Page<?> page, String name) {
		// TODO Auto-generated method stub
		if(GeneralUtil.isNotEmpty(name)) {
			if(!name.contains("%")) {
				name="%"+name.trim()+"%";
			}
		}else {
			name=null; 
		}
		UserInfo userInfo = UserInfoContext.get();
		IPage<UserVO> result=this.baseMapper.listQuery(page,name,userInfo.getCompanyid());
		for(int i=0;i<result.getRecords().size();i++) {
			UserVO uv=result.getRecords().get(i);
			List<String> roles=iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, uv.getId()))
            .stream().map(item -> item.getRoleId().toString()).collect(Collectors.toList());
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
	
}
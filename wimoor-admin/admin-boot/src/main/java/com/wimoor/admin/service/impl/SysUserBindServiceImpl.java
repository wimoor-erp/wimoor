package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SysUserBindMapper;
import com.wimoor.admin.mapper.SysUserMapper;
import com.wimoor.admin.pojo.entity.SysUserBind;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.service.ISysUserBindService;
import com.wimoor.admin.service.ISysUserWechatMPService;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-25
 */
@Service
public class SysUserBindServiceImpl extends ServiceImpl<SysUserBindMapper, SysUserBind> implements ISysUserBindService {
    @Autowired 
    ISysUserWechatMPService iSysUserWechatMPService;
    @Autowired 
    SysUserMapper sysUserMapper; 
    
	@Override
	public String getBindId(String userid) {
		// TODO Auto-generated method stub
		if(StrUtil.isBlankOrUndefined(userid)) {
			throw new BizException("用户ID不能为空");
		}
		SysUserBind bind = this.lambdaQuery().eq(SysUserBind::getUserid, userid).one();
		 if(bind!=null) {
			 return bind.getBindid().toString();
		 }else {
			 SysUserBind userbind=new SysUserBind();
			 userbind.setUserid(new BigInteger(userid));
			 userbind.setBindid(sysUserMapper.getShortUUID());
			 this.save(userbind);
			 LambdaQueryWrapper<SysUserWechatMP> query=new LambdaQueryWrapper<SysUserWechatMP>();
			 query.eq(SysUserWechatMP::getUserid,userid);
			 List<SysUserWechatMP> listmp = iSysUserWechatMPService.list(query);
			 if(listmp!=null&&listmp.size()>0) {
				 bindMpUser(listmp.get(0).getOpenid(),userid);
			 }
			 return userbind.getBindid().toString();
		 }
		
	}

	@Override
	public List<SysUserBind> getBindListUser(String userid) {
		// TODO Auto-generated method stub
		if(StrUtil.isBlankOrUndefined(userid)) {
			throw new BizException("用户ID不能为空");
		}
		 String bindid = getBindId(  userid) ;
		 if(bindid!=null ) {
			 return   this.lambdaQuery().eq(SysUserBind::getBindid,bindid).list();
		 } 
		return null;
	}
	
	@Override
	public List<SysUserBind> getBindList(String bindid) {
		// TODO Auto-generated method stub
		 if(bindid!=null ) {
			 return   this.lambdaQuery().eq(SysUserBind::getBindid,bindid).list();
		 } 
		return null;
	}
    public void bindMpUser(String openid,String userid) {
		LambdaQueryWrapper<SysUserWechatMP> query=new LambdaQueryWrapper<SysUserWechatMP>();
		query.eq(SysUserWechatMP::getOpenid,openid);
		List<SysUserWechatMP> list = iSysUserWechatMPService.list(query);
		String bindid=getBindId(userid);
		for(SysUserWechatMP item:list) {
			if(item.getUserid().equals(userid)) {
				save(item.getUserid(),bindid);
			}
		}
    }
    
	@Override
	public Boolean save(String userid, String bindid) {
		// TODO Auto-generated method stub
		SysUserBind old = this.lambdaQuery().eq(SysUserBind::getUserid, userid).one();
		if(old==null) {
			 SysUserBind userbind=new SysUserBind();
			 userbind.setUserid(new BigInteger(userid));
			 userbind.setBindid(new BigInteger(bindid));
			return this.save(userbind);
		}else {
			return this.lambdaUpdate().eq(SysUserBind::getUserid, userid).set(SysUserBind::getBindid, bindid).update();
		}
	}

}

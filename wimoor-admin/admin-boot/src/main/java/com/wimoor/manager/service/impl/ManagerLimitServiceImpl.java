package com.wimoor.manager.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.admin.mapper.SysUserRoleMapper;
import com.wimoor.admin.mapper.SysUserShopMapper;
import com.wimoor.admin.pojo.entity.SysUserRole;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.manager.mapper.ManagerLimitMapper;
import com.wimoor.manager.pojo.entity.ManagerLimit;
import com.wimoor.manager.pojo.entity.SysTariffPackages;
import com.wimoor.manager.service.IManagerLimitService;
import com.wimoor.manager.service.ISysTariffPackagesService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Service
@RequiredArgsConstructor
public class ManagerLimitServiceImpl extends ServiceImpl<ManagerLimitMapper, ManagerLimit> implements IManagerLimitService {
   final ISysTariffPackagesService iSysTariffPackagesService;
   final SysUserShopMapper sysUserShopMapper;
   final SysUserRoleMapper sysUserRoleMapper;
   public String getCompanyRole(String companyid) {
	   QueryWrapper<ManagerLimit> query=new QueryWrapper<ManagerLimit>();
	   query.eq("shopid", new BigInteger(companyid));
	   ManagerLimit ml = this.baseMapper.selectOne(query);
	   SysTariffPackages pkg = iSysTariffPackagesService.getById(ml.getTariffpackage());
	   if(pkg==null) {
		   throw new BizException("无法找到套餐，请联系管理员");
	   }
	   if(pkg.getId()==5){
		   BigInteger userid = sysUserShopMapper.findByCompanyId(new BigInteger(companyid));
		   LambdaQueryWrapper<SysUserRole> mquery=new LambdaQueryWrapper<SysUserRole>();
		   mquery.eq(SysUserRole::getUserId, userid);
		   List<SysUserRole> roles = sysUserRoleMapper.selectList(mquery);
		   if(roles!=null && roles.size()>0) {
			   return roles.get(0).getRoleId().toString();
		   }
	   }
	   return pkg.getRoleId().toString();
   }

@Override
public List<Map<String, Object>> summary() {
	// TODO Auto-generated method stub
	return this.baseMapper.summary();
}

@Override
public IPage<Map<String, Object>> getCompanyList(Page<Object> page, Map<String, Object> param) {
	// TODO Auto-generated method stub
	return this.baseMapper.getCompanyList(page,param);
}

@Override
public Map<String, Object> findManagerLimitByShopId(String companyid) {
	return this.baseMapper.findManagerLimitByShopId(companyid);
}
   
}

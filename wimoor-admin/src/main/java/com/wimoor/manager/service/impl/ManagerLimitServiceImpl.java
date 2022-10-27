package com.wimoor.manager.service.impl;

import com.wimoor.manager.pojo.entity.ManagerLimit;
import com.wimoor.manager.pojo.entity.SysTariffPackages;
import com.wimoor.common.mvc.BizException;
import com.wimoor.manager.mapper.ManagerLimitMapper;
import com.wimoor.manager.service.IManagerLimitService;
import com.wimoor.manager.service.ISysTariffPackagesService;

import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

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
   
   public String getCompanyRole(String companyid) {
	   QueryWrapper<ManagerLimit> query=new QueryWrapper<ManagerLimit>();
	   query.eq("shopid", new BigInteger(companyid));
	   ManagerLimit ml = this.baseMapper.selectOne(query);
	   SysTariffPackages pkg = iSysTariffPackagesService.getById(ml.getTariffpackage());
	   if(pkg==null) {
		   throw new BizException("无法找到套餐，请联系管理员");
	   }
	   return pkg.getRoleId().toString();
   }
   
}

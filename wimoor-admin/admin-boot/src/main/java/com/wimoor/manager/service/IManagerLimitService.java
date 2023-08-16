package com.wimoor.manager.service;

import com.wimoor.manager.pojo.entity.ManagerLimit;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
public interface IManagerLimitService extends IService<ManagerLimit> {
	   public String getCompanyRole(String companyid) ;
	   public List<Map<String,Object>> summary();
	   public IPage<Map<String, Object>> getCompanyList(Page<Object> page, Map<String, Object> param);
}

package com.wimoor.manager.service;

import com.wimoor.manager.pojo.entity.ManagerLimit;
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
}

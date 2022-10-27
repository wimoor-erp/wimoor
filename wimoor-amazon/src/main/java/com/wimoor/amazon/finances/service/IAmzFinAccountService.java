package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
public interface IAmzFinAccountService extends IService<AmzFinAccount> ,IRunAmazonService{

	void handler(AmazonAuthority amazonAuthority, ListFinancialEventGroupsResponse result);

}

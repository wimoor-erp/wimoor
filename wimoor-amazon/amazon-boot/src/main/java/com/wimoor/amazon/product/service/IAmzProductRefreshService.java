package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
public interface IAmzProductRefreshService extends IService<AmzProductRefresh> {
	void insert();
	
	public AmzProductRefresh findForDetailRefresh(String amazonauthid);

	public List<AmzProductRefresh> findForCatalogRefresh(String amazonauthid);
	
	public List<AmzProductRefresh> findForPriceRefresh(String amazonauthid) ;
	
}

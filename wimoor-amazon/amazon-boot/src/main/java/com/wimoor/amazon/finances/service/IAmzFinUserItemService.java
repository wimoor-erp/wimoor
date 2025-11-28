package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;

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
 * @since 2022-10-27
 */
public interface IAmzFinUserItemService extends IService<AmzFinUserItem> {
	public List<Map<String, Object>> findFinListByShopid(String shopid) ;
	public List<AmzFinUserItem> getFinItemList(String shopid);
	public IPage<Map<String, Object>> findFinListByShopid(Page<?>page,String shopid);
	public List<Map<String, Object>> findFinListByShopidNoPage(String shopid) ;
}

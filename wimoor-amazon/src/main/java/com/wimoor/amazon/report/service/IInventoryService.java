package com.wimoor.amazon.report.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
public interface IInventoryService  {
	public IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page, Map<String, Object> parameter);
}

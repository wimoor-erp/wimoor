package com.wimoor.admin.service;

import com.wimoor.admin.pojo.entity.SysTags;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */
public interface ISysTagsService extends IService<SysTags> {

	IPage<SysTags> list(Page<SysTags> page, SysTags dict);

	List<SysTags> listbyshop(String shopid);

	void disableList(List<String> idslist);
}

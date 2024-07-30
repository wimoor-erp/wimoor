package com.wimoor.sys.tool.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.sys.tool.pojo.entity.SysTags;

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

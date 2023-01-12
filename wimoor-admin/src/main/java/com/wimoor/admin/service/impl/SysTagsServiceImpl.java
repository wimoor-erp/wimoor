package com.wimoor.admin.service.impl;

import com.wimoor.admin.pojo.entity.SysTags;
import com.wimoor.admin.mapper.SysTagsMapper;
import com.wimoor.admin.service.ISysTagsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */
@Service
public class SysTagsServiceImpl extends ServiceImpl<SysTagsMapper, SysTags> implements ISysTagsService {

	@Override
	public IPage<SysTags> list(Page<SysTags> page, SysTags dict) {
		List<SysTags> list = this.baseMapper.list(page,dict);
		page.setRecords(list);
		return page;
	}

}

package com.wimoor.sys.tool.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.sys.tool.mapper.SysTagsMapper;
import com.wimoor.sys.tool.pojo.entity.SysTags;
import com.wimoor.sys.tool.service.ISysTagsService;

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

	@Override
	public List<SysTags> listbyshop(String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.listbyshop(shopid);
	}

	@Override
	public void disableList(List<String> idslist) {
		if(idslist!=null && idslist.size()>0) {
			for (int i = 0; i < idslist.size(); i++) {
				String tagid = idslist.get(i);
				SysTags tags = this.baseMapper.selectById(tagid);
				if(tags!=null && tags.getStatus()==1) {
					tags.setStatus(0);
					this.baseMapper.updateById(tags);
				}
			}
		}
	}

}

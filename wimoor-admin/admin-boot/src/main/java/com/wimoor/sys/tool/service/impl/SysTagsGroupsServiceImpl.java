package com.wimoor.sys.tool.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.sys.tool.mapper.SysTagsGroupsMapper;
import com.wimoor.sys.tool.mapper.SysTagsMapper;
import com.wimoor.sys.tool.pojo.entity.SysTagsGroups;
import com.wimoor.sys.tool.service.ISysTagsGroupsService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */
@Service
@RequiredArgsConstructor
public class SysTagsGroupsServiceImpl extends ServiceImpl<SysTagsGroupsMapper, SysTagsGroups> implements ISysTagsGroupsService {

	final SysTagsMapper sysTagsMapper;
	
	
	@Override
	public void disableList(String groupid) {
    	SysTagsGroups groups = this.baseMapper.selectById(groupid);
//    	LambdaQueryWrapper<SysTags> queryWrapper=new LambdaQueryWrapper<SysTags>();
//    	queryWrapper.eq(SysTags::getTaggroupid,groupid);
//		List<SysTags> tagslist = sysTagsMapper.selectList(queryWrapper);
//		if(tagslist!=null && tagslist.size()>0) {
//			for (int i = 0; i < tagslist.size(); i++) {
//				SysTags tags = tagslist.get(i);
//				if(tags!=null && tags.getStatus()==1) {
//					tags.setStatus(0);
//					sysTagsMapper.updateById(tags);
//				}
//			}
//		}
    	groups.setStatus(0);
    	this.baseMapper.updateById(groups);
	}

}

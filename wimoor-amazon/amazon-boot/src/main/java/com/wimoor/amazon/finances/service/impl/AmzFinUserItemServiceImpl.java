package com.wimoor.amazon.finances.service.impl;

import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.mapper.AmzFinUserItemMapper;
import com.wimoor.amazon.finances.service.IAmzFinUserItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Service
public class AmzFinUserItemServiceImpl extends ServiceImpl<AmzFinUserItemMapper, AmzFinUserItem> implements IAmzFinUserItemService {
	
 
	public List<Map<String, Object>> findFinListByShopid(String shopid) {
		return this.baseMapper.findFinListByShopid(shopid);
	}

	public IPage<Map<String, Object>> findFinListByShopid(Page<?>page,String shopid) {
		return this.baseMapper.findFinListByShopid(page,shopid);
	}

	public List<Map<String, Object>> findFinListByShopidNoPage(String shopid) {
		return this.baseMapper.findFinListByShopid(shopid);
	}


	@Override
	public List<AmzFinUserItem> getFinItemList(String shopid) {
		LambdaQueryWrapper<AmzFinUserItem> queryWrapper=new LambdaQueryWrapper<AmzFinUserItem>();
		queryWrapper.eq(AmzFinUserItem::getShopid,shopid);
		queryWrapper.eq(AmzFinUserItem::getDisable, false);
		List<AmzFinUserItem> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}
}

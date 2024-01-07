package com.wimoor.amazon.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.FollowOfferMapper;
import com.wimoor.amazon.auth.pojo.entity.FollowOffer;
import com.wimoor.amazon.auth.service.IFollowOfferService;


@Service("followOfferService")
public class FollowOfferServiceImpl extends ServiceImpl<FollowOfferMapper, FollowOffer> implements IFollowOfferService{
	
	public List<Map<String,Object>> getNeedUpdate() {
		return this.baseMapper.getNeedUpdate();
	}

	public int updateSellerName(String sellerid, String sellerName, int refreshnum) {
		return this.baseMapper.updateSellerName(sellerid, sellerName, refreshnum);
		
	}

}

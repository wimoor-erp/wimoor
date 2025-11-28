package com.wimoor.amazon.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.FollowOfferChangeMapper;
import com.wimoor.amazon.product.pojo.entity.FollowOfferChange;
import com.wimoor.amazon.product.service.IFollowOfferChangeService;



@Service("followOfferChangeService")
public class FollowOfferChangeServiceImpl extends ServiceImpl<FollowOfferChangeMapper, FollowOfferChange> implements IFollowOfferChangeService{
	@Resource
	FollowOfferChangeMapper followOfferChangeMapper;

	public FollowOfferChange selectOfferChangeBySellerAsin(String sellerid, String asin, String marketplaceid) {
		// TODO Auto-generated method stub
		return followOfferChangeMapper.selectOfferChangeBySellerAsin(sellerid, asin, marketplaceid);
	}
	
	public FollowOfferChange selectOfferChangeByLost(String sellerid, String asin, String marketplaceid) {
		// TODO Auto-generated method stub
		return followOfferChangeMapper.selectOfferChangeByLost(sellerid, asin, marketplaceid);
	}
	
	public List<FollowOfferChange> selectAllForLostOffer(String asin, String marketplaceid){
		return followOfferChangeMapper.selectAllForLostOffer(asin, marketplaceid);
	}

	public void updateForLostOfferForlostTime(String sellerid, String asin, String marketplaceid){
		followOfferChangeMapper.updateForLostOfferForlostTime(sellerid, asin, marketplaceid);
	}
}

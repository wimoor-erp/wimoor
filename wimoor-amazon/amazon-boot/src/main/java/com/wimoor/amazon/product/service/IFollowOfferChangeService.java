package com.wimoor.amazon.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.entity.FollowOfferChange;

public interface IFollowOfferChangeService extends IService<FollowOfferChange>{

	FollowOfferChange selectOfferChangeBySellerAsin(String sellerid, String asin, String marketplaceid );
	
	FollowOfferChange selectOfferChangeByLost(String sellerid, String asin, String marketplaceid);
	
	List<FollowOfferChange> selectAllForLostOffer(String asin, String marketplaceid);
	
	void updateForLostOfferForlostTime(String sellerid, String asin, String marketplaceid);
}

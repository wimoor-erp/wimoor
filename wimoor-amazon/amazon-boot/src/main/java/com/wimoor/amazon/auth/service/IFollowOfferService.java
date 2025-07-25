package com.wimoor.amazon.auth.service;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.pojo.entity.FollowOffer;

public interface IFollowOfferService extends IService<FollowOffer>{
	
	List<Map<String,Object>> getNeedUpdate();

	int updateSellerName(String sellerid, String sellerName, int refreshnum);
}

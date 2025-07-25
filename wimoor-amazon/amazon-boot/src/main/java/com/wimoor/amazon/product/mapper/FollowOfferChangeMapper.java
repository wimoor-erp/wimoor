package com.wimoor.amazon.product.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.FollowOfferChange;
 
@Mapper
public interface FollowOfferChangeMapper extends BaseMapper<FollowOfferChange>{
	
	FollowOfferChange selectOfferChangeBySellerAsin(@Param("sellerid") String sellerid,@Param("asin") String asin,@Param("marketplaceid") String marketplaceid );
	
	public FollowOfferChange selectOfferChangeByLost(@Param("sellerid")String sellerid, @Param("asin")String asin, @Param("marketplaceid")String marketplaceid);
 
	List<FollowOfferChange> selectAllForLostOffer(@Param("asin") String asin, @Param("marketplaceid") String marketplaceid);
	
	void updateForLostOfferForlostTime(@Param("sellerid") String sellerid,@Param("asin") String asin,@Param("marketplaceid") String marketplaceid );

	List<Map<String, Object>> findByFollowID(@Param("followid")String followid,@Param("mylimit")Integer mylimit);
}
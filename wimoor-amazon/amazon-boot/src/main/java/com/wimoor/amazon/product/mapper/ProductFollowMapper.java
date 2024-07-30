package com.wimoor.amazon.product.mapper;

 
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductFollow;

@Mapper
public interface ProductFollowMapper   extends BaseMapper<ProductFollow> {
	   
	   ProductFollow selectByMarketplaceAsin(@Param("asin")String asin, 
			                                 @Param("marketplaceid")String marketplaceid,
			                                 @Param("amazonAuthId")String amazonAuthId);
	   
}
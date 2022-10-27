package com.wimoor.amazon.feed.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
@Mapper
public interface AmzSubmitFeedQueueMapper extends BaseMapper<AmzSubmitFeedQueue> {

   

	AmzSubmitFeedQueue findByMarket(@Param("shopid")String shopId,@Param("authid") String authid,@Param("marketplaceid") String marketplaceid);

	AmzSubmitFeedQueue selectByKey(String id);

	Map<String, Object> selectByFeedTypeAndFileName(@Param("authorityid") String authorityid,
			@Param("marketplaceid") String marketplaceid, @Param("feedtype") String feedtype,
			@Param("shipmentid") String shipmentid);
	
	List<AmzSubmitFeedQueue> findQueue(@Param("authorityid") String authid,@Param("marketplaceid") String marketplaceid);
	 
}
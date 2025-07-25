package com.wimoor.amazon.follow.mapper;

import com.wimoor.amazon.follow.pojo.dto.ProductFollowListDTO;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollow;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-06
 */
@Mapper
public interface ProductInfoFollowMapper extends BaseMapper<ProductInfoFollow> {

	
	IPage<Map<String,Object>> findByCondition(Page<?> page,@Param("param") ProductFollowListDTO param);

	List<Map<String, Object>> findFollowOfferlist(@Param("asin")String asin,@Param("marketplaceid")String marketplaceid);
	List<Map<String, Object>> findFollowOffers(@Param("pid") String pid);
	
	List<ProductInfoFollow> selectstartTimeList(@Param("fromtime")String starttime,@Param("totime")String endtime, @Param("daytime")String daytime);
	List<ProductInfoFollow> selectendTimeList(@Param("fromtime")String starttime,@Param("totime")String endtime, @Param("daytime")String daytime);
	List<ProductInfoFollow> selectDeleteList();
	List<ProductInfoFollow> selectPriceList();
	
	List<ProductInfoFollow>  selectNoUploadList();
	List<Map<String,Object>> findByCondition(@Param("param") ProductFollowListDTO param);
	
	List<Map<String, Object>> findRecordList(@Param("pid")String pid,@Param("opttype")String opttype);
	
	IPage<Map<String, Object>> findWarningList(Page<?> page,@Param("shopid")String shopid,@Param("authid")String authid,
			@Param("marketplaceid")String marketplaceid);

	Map<String, Object> findInfo(@Param("pid")String pid);

	Integer findWarningNunmber(@Param("shopid")String shopid);

	List<Map<String, Object>> findProductInfo(@Param("shopid")String shopid, @Param("asin")String asin,@Param("marketplaceid") String marketplaceid);

	Map<String, Object> findOrderSummary(String pid);

	Date findPriceTime(String pid);

	List<ProductInfoFollow>  syncProductInfo(@Param("shopid")String shopid, @Param("userid")String userid);

	List<ProductInfoFollow> needInvRefresh();

	void invalidProduct(String id);



}

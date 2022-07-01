package com.wimoor.amazon.auth.mapper;

import com.wimoor.amazon.auth.pojo.entity.AmazonSellerMarket;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 授权对应区域客户所有绑定的站点 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-25
 */
@Mapper
public interface AmazonSellerMarketMapper extends BaseMapper<AmazonSellerMarket> {

}

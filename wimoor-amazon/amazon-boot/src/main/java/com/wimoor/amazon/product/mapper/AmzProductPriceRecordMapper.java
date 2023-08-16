package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用于记录调价 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-28
 */
@Mapper
public interface AmzProductPriceRecordMapper extends BaseMapper<AmzProductPriceRecord> {

}

package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用于记录调价 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-28
 */
public interface IAmzProductPriceRecordService extends IService<AmzProductPriceRecord> {

	List<AmzProductPriceRecord> findPriceListByPid(String pid);
}

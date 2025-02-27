package com.wimoor.amazon.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.AmzProductPriceRecordMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;
import com.wimoor.amazon.product.service.IAmzProductPriceRecordService;

/**
 * <p>
 * 用于记录调价 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-28
 */
@Service
public class AmzProductPriceRecordServiceImpl extends ServiceImpl<AmzProductPriceRecordMapper, AmzProductPriceRecord> implements IAmzProductPriceRecordService {

	@Override
	public List<AmzProductPriceRecord> findPriceListByPid(String pid) {
		LambdaQueryWrapper<AmzProductPriceRecord> queryWrapper=new LambdaQueryWrapper<AmzProductPriceRecord>();
		queryWrapper.eq(AmzProductPriceRecord::getPid, pid);
		return this.baseMapper.selectList(queryWrapper);
	}

	
}

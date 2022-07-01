package com.wimoor.amazon.auth.service.impl;

import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.mapper.AmzAuthApiTimelimitMapper;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新版本SPI-API使用 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-24
 */
@Service("amzAuthApiTimelimitService")
public class AmzAuthApiTimelimitServiceImpl extends ServiceImpl<AmzAuthApiTimelimitMapper, AmzAuthApiTimelimit> implements IAmzAuthApiTimelimitService {

	@Override
	public AmzAuthApiTimelimit getApiLimit(String amazonauthid, String apiname) {
		// TODO Auto-generated method stub
		   QueryWrapper<AmzAuthApiTimelimit> queryWrapperlimit=new QueryWrapper<AmzAuthApiTimelimit>();
		   queryWrapperlimit.eq("amazonauthid",amazonauthid);
		   queryWrapperlimit.eq("apiname",apiname);
		return this.getOne(queryWrapperlimit);
	}

}

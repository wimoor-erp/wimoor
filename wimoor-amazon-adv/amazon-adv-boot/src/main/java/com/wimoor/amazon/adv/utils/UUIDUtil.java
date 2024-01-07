package com.wimoor.amazon.adv.utils;

import java.math.BigInteger;


import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;


public class UUIDUtil {
public static String getUUIDshort() {
	AmzAdvProfileMapper mapper=SpringUtil.getBean(AmzAdvProfileMapper.class);
	String result = mapper.getUUID();
	if(StrUtil.isNotBlank(result)) {
		return result;
	}else {
		return "0";
	}
}
public static BigInteger getBigIntUUIDshort() {
	AmzAdvProfileMapper mapper=SpringUtil.getBean(AmzAdvProfileMapper.class);
	String result = mapper.getUUID();
	if(StrUtil.isNotBlank(result)) {
		return new BigInteger(result);
	}
	return new BigInteger("0");
}
}

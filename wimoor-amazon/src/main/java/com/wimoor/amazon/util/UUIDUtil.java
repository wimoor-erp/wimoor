package com.wimoor.amazon.util;

import java.math.BigInteger;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wimoor.amazon.auth.mapper.AmazonAuthorityMapper;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;


public class UUIDUtil extends com.wimoor.util.UUIDUtil{
public static String getUUIDshort()  {
	AmazonAuthorityMapper mapper=SpringUtil.getBean(AmazonAuthorityMapper.class);
	String result = mapper.uuid();
	if(StrUtil.isNotBlank(result)) {
		return result;
	}else {
		return "0";
	}
}
public static BigInteger getBigIntUUIDshort() {
	JdbcTemplate us=SpringUtil.getBean("jdbcTemplate");
	List<String> bigid = us.query("select uuid_short()",new BeanPropertyRowMapper<String>(String.class));
	if(bigid.size()>0) {
		return new BigInteger(bigid.get(0));
	}
	return new BigInteger("0");
}
}

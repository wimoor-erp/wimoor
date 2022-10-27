package com.wimoor.amazon.adv.sb.dao;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvSBMedia;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvSBMediaMapper extends BaseMapper<AmzAdvSBMedia>{

	AmzAdvSBMedia loadOldMedia(String profileId);
 
}
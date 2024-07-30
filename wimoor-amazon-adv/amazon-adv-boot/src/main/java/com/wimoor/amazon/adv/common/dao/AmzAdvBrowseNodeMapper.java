package com.wimoor.amazon.adv.common.dao;

import java.math.BigInteger;
import java.util.List;

import com.wimoor.amazon.adv.common.pojo.AmzAdvBrowseNode;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvBrowseNodeMapper  extends BaseMapper<AmzAdvBrowseNode>{

	void insertBatch(List<AmzAdvBrowseNode> list);

	List<AmzAdvBrowseNode> selcetByParentId(BigInteger id);
}
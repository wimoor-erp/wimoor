package com.wimoor.amazon.finances.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.finances.pojo.entity.AmazonSettlementOpen;
@Mapper
public interface AmazonSettlementOpenMapper  extends BaseMapper<AmazonSettlementOpen>{

	void insertBatch(List<AmazonSettlementOpen> list);

}

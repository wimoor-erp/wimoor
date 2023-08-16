package com.wimoor.amazon.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;

@Mapper
public interface ProductInfoStatusDefineMapper extends BaseMapper<ProductInfoStatusDefine>{

	List<ProductInfoStatusDefine> getProStatusByShop(@Param("shopid")String shopid);

	int selectStatusTotalCount(@Param("shopid")String shopid,@Param("id")String id);

	ProductInfoStatusDefine selectStatusDefineByPid(@Param("shopid")String shopid,@Param("pid") String pid);
    
	
	
	
}
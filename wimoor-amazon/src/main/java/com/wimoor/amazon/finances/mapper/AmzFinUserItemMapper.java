package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
 
@Mapper
public interface AmzFinUserItemMapper  extends BaseMapper<AmzFinUserItem>{

	IPage<Map<String, Object>> findFinListByShopid(Page<?>page,@Param("shopid")String shopid);

	List<Map<String, Object>> findFinListByShopid(String shopid);
 
}
package com.wimoor.amazon.finances.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Mapper
public interface AmzFinAccountMapper extends BaseMapper<AmzFinAccount> {
	
	List<Map<String,Object>> selectUnclosedFinByShopid(String shopid);
	
	List<Map<String,Object>> selectFinByShopid(@Param("shopid")String shopid, @Param("beginDate")String beginDate, @Param("endDate")String endDate);

	IPage<Map<String, Object>> getFinancial(Page<Map<String, Object>> page,@Param("param") Map<String, Object> maps);

	List<Map<String, Object>> getFinancialSum(Map<String, Object> map);

	Date getlastUpdate(String amazonAuthid);
}

package com.wimoor.amazon.orders.mapper;

import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoiceReport;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-08-20
 */
@Mapper
public interface AmzOrdersInvoiceReportMapper extends BaseMapper<AmzOrdersInvoiceReport> {

	IPage<AmazonOrdersVo> selectOrderList(Page<?> page,@Param("dto") AmazonOrdersDTO dto);
 
}

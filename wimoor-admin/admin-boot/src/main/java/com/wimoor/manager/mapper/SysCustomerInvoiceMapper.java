package com.wimoor.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.manager.pojo.entity.SysCustomerInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

/**
 * <p>
 * 客户发票表 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Mapper
public interface SysCustomerInvoiceMapper extends BaseMapper<SysCustomerInvoice> {

    SysCustomerInvoice selectByOrderId(@Param("orderid") BigInteger orderid);

}
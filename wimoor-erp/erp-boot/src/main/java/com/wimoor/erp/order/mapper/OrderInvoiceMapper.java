package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderInvoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Mapper
public interface OrderInvoiceMapper extends BaseMapper<OrderInvoice> {

}



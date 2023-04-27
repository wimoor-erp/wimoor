package com.wimoor.amazon.orders.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsReport;

@Mapper
public interface OrdersFulfilledShipmentsReportMapper  extends BaseMapper<OrdersFulfilledShipmentsReport> {
	int insertBatch(List<OrdersFulfilledShipmentsReport> list);
}

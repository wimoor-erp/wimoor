package com.wimoor.amazon.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-08-20
 */
public interface IReportAmzOrderInvoiceService  {

	IPage<AmazonOrdersVo> selectOrderList(AmazonOrdersDTO condition);

}

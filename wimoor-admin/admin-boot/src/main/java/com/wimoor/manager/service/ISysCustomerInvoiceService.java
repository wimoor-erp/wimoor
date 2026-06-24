package com.wimoor.manager.service;

import com.wimoor.manager.pojo.entity.SysCustomerInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigInteger;

/**
 * <p>
 * 客户发票表 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
public interface ISysCustomerInvoiceService extends IService<SysCustomerInvoice> {

    SysCustomerInvoice findByOrderId(BigInteger orderid);
}
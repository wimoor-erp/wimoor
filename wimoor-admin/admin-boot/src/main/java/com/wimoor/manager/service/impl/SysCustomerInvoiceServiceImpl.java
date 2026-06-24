package com.wimoor.manager.service.impl;

import com.wimoor.manager.pojo.entity.SysCustomerInvoice;
import com.wimoor.manager.mapper.SysCustomerInvoiceMapper;
import com.wimoor.manager.service.ISysCustomerInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * <p>
 * 客户发票表 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Service
public class SysCustomerInvoiceServiceImpl extends ServiceImpl<SysCustomerInvoiceMapper, SysCustomerInvoice> implements ISysCustomerInvoiceService {

    @Override
    public SysCustomerInvoice findByOrderId(BigInteger orderid) {
        return baseMapper.selectByOrderId(orderid);
    }
}
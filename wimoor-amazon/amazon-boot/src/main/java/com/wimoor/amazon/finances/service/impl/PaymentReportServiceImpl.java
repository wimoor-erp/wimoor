package com.wimoor.amazon.finances.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.finances.pojo.entity.PaymentReport;
import com.wimoor.amazon.finances.service.PaymentReportService;
import com.wimoor.amazon.finances.mapper.PaymentReportMapper;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_amz_payment_report(亚马逊支付报告表)】的数据库操作Service实现
* @createDate 2026-02-07 09:42:36
*/
@Service
public class PaymentReportServiceImpl extends ServiceImpl<PaymentReportMapper, PaymentReport>
    implements PaymentReportService{

}





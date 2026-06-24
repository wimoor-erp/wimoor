package com.wimoor.finance.closing.service.impl;

import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.service.IFinClosingTemplateHandlerService;
import com.wimoor.finance.closing.service.strategy.FinClosingTemplateStrategyFactory;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FinClosingTemplateHandlerServiceImpl implements IFinClosingTemplateHandlerService {

    @Resource
    private FinClosingTemplateStrategyFactory strategyFactory;

    @Override
    public void handleAmazonPaymentTemplate(String templateid,String period) {
        IFinClosingTemplateStrategy strategy = strategyFactory.getStrategy("amzpayment");
        strategy.generateVoucher(UserInfoContext.get(),templateid,period);
    }

    @Override
    public void generateVoucher(String templateid) {
        IFinClosingTemplateStrategy strategy = strategyFactory.getStrategy("amzpayment");
        strategy.generateVoucher(UserInfoContext.get(),templateid,null);
    }
}

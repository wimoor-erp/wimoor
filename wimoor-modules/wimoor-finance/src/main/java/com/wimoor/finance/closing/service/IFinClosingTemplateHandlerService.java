package com.wimoor.finance.closing.service;

public interface IFinClosingTemplateHandlerService {
    void handleAmazonPaymentTemplate(String templateid,String period);
    void generateVoucher(String templateid);
}

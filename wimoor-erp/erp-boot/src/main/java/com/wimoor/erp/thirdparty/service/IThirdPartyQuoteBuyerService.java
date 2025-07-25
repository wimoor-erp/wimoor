package com.wimoor.erp.thirdparty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyQuoteBuyer;

public interface IThirdPartyQuoteBuyerService  extends IService<ThirdPartyQuoteBuyer> {
    ThirdPartyQuoteBuyer getQuoteToken(String shopid);
    ThirdPartyQuoteBuyer saveQuoteToken(ThirdPartyQuoteBuyer quote);
    Boolean  removeQuoteToken(String shopid);
}

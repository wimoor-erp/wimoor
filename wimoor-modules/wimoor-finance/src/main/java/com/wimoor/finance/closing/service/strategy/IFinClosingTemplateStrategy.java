package com.wimoor.finance.closing.service.strategy;

import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.closing.domain.FinClosingTemplate;

import java.util.List;
import java.util.Map;

public interface IFinClosingTemplateStrategy {
    String getFtype();
    void generateVoucher(UserInfo userInfo, String templateId, String periodCode);
    void initTemplateItem(FinClosingTemplate template);
    
    /**
     * 获取金额计算逻辑明细（只读，用于展示）
     * @param templateId 模板ID
     * @param periodCode 期间编码
     * @return 计算逻辑明细数据
     */
    default Map<String, Object> getCalculationDetail(String templateId, String periodCode) {
        return null;
    }
}

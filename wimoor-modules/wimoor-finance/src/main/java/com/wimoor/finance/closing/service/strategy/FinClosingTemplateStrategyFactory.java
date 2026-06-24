package com.wimoor.finance.closing.service.strategy;

import com.wimoor.finance.closing.domain.FinClosingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinClosingTemplateStrategyFactory {

    @Autowired
    private List<IFinClosingTemplateStrategy> strategyList;

    private Map<String, IFinClosingTemplateStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IFinClosingTemplateStrategy strategy : strategyList) {
            strategyMap.put(strategy.getFtype(), strategy);
        }
    }

    public IFinClosingTemplateStrategy getStrategy(String ftype) {
        IFinClosingTemplateStrategy strategy = strategyMap.get(ftype);
        if (strategy == null) {
            throw new IllegalArgumentException("未找到模板类型为 [" + ftype + "] 的处理策略");
        }
        return strategy;
    }

    public void initTemplateItem(FinClosingTemplate finClosingTemplate){
        for (IFinClosingTemplateStrategy strategy : strategyList) {
            strategy.initTemplateItem(finClosingTemplate);
        }
    }
    public boolean hasStrategy(String ftype) {
        return strategyMap.containsKey(ftype);
    }
}

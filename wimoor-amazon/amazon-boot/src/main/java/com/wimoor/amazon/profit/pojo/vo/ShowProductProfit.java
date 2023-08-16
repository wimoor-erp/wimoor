package com.wimoor.amazon.profit.pojo.vo;

import java.util.List;

import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;

import lombok.Data;

@Data
public class ShowProductProfit {
String msg;
CostDetail costDetail;
String profitCfgId;
List<ProfitConfig> profitCfgList;
InputDimensions inputDimensions;
}

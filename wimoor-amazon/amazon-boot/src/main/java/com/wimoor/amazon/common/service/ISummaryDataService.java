package com.wimoor.amazon.common.service;
 
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.common.pojo.entity.SummaryData;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.orders.pojo.vo.ProductSalesRankVo;
import com.wimoor.common.user.UserInfo;


public interface ISummaryDataService  extends IService<SummaryData>{
	public List<Map<String, Object>> marketSummary(SummaryMutilParameterQueryDTO parameter);
	public  Map<String,Object> selectSumByMutilParameter(SummaryMutilParameterQueryDTO parameter);
    public  Map<String,Object> selectByMutilParameter(SummaryMutilParameterQueryDTO parameter);
    public Map<String, Object> getReturnOrderSummary(SummaryMutilParameterQueryDTO parameter);
    public Map<String, Object> getReturnOrder(SummaryMutilParameterQueryDTO parameter);
	List<ProductSalesRankVo> top5(SummaryMutilParameterQueryDTO parameter);
	public ChartLine findOrderSummaryBySku(String groupid,String amazonAuthId, String sku, String marketplaceid,Integer daysize ,String linetype, UserInfo user);
}

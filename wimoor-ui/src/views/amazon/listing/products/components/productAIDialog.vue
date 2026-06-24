<template>
  <el-dialog
    v-model="dialogVisible"
    title="AI产品分析助手"
    width="90%"
    top="5vh"
    :close-on-click-modal="true"
    class="ai-dialog"
  >
    <div class="ai-dialog-content">
      <div class="ai-main-area">
        <DeepSeekInfo inner-type="product" ref="deepseekRef" @change="onAiResponse" />
      </div>
      <div class="ai-sidebar">
        <div class="sidebar-title">数据扩展</div>
        <el-button type="primary" @click="handleSalesInfo" :disabled="isLoading" class="sidebar-btn">
          <el-icon><TrendCharts /></el-icon>
          <span>获取销量信息</span>
        </el-button>
        <el-button type="success" @click="handleReviewInfo" :disabled="isLoading" class="sidebar-btn">
          <el-icon><ChatDotRound /></el-icon>
          <span>获取评论信息</span>
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref,reactive ,toRefs,nextTick} from 'vue';
import DeepSeekInfo from "@/views/sys/deepseek/index.vue";
import saleschartApi from '@/api/amazon/order/saleschartApi.js';
import listingApi from '@/api/amazon/listing/listingApi.js';

const state = reactive({
  dialogVisible: false,
  isLoading: false,
  dataRow: {}
});

const { dialogVisible,dataRow,isLoading } = toRefs(state);

const deepseekRef = ref();

// AI 开始响应时关闭 loading
function onAiResponse() {
  state.isLoading = false;
}

function show(row){
  state.dataRow = row;
  state.isLoading = true;
  //弹框展示row里的数据
  const costDetailMap = row.param?.costDetailMap || {};
  const totalCost = costDetailMap.totalCost || 0;
  const profit = costDetailMap.profit || row.profits || 0;
  const profitRate = costDetailMap.margin || row.profitrate || '0%';
  let analysisPrompt = `请分析以下产品运营数据，并提供专业的运营建议：

产品基本信息：
- ASIN: ${row.asin || '无'}
- SKU: ${row.sku || '无'}
- 产品名称: ${row.name || '无'}
- 品牌: ${row.info?.brand || '无'}
- 制造商: ${row.info?.manufacturer || '无'}
- 站点: ${row.marketplacename || '无'}
- 市场: ${row.groupname || '无'}
- 负责人: ${row.ownername || '无'}

销售数据：
- 30日销量: ${row.summonth || 0}件
- 30日订单数: ${row.ordermonth || 0}单
- 7日销量: ${row.sumweek || 0}件
- 日均销量: ${row.averageSalesDay || 0}件
- 30日退货数量: ${row.salesreturnmonth || 0}件
- 30日退货订单数: ${row.orderReturnMonth || 0}单
- 销售排名: ${row.rank || '无'}

价格和利润：
- 当前售价: ${row.landedCurrency || '$'}${row.landedAmount || 0}
- 利润: ${row.landedCurrency || '$'}${profit}
- 利润率: ${profitRate}
- 总成本: ${row.landedCurrency || '$'}${totalCost}

库存数据：
- 可售库存: ${row.afnFulfillableQuantity || 0}件
- 可售天数: ${row.dayfulfilla || 0}天
- 预计到货天数: ${row.dayinbound || 0}天
- 购物车比例: ${row.buybox || 0}%

流量和转化：
- 7日访问量: ${row.sessions || 0}
- 转化率: ${row.sessionrate || 0}%

广告数据（近7天）：
- 展示次数: ${row.advimpr || 0}
- 点击次数: ${row.advclick || 0}
- 点击率(CTR): ${row.advctr || 0}%
- 点击成本(CPC): ${row.advcpc || 0}
- 广告花费: ${row.landedCurrency || '$'}${row.advspend || 0}
- 广告订单数: ${row.advsales || 0}
- 转化率(CR): ${row.advspc || 0}%
- 广告销售成本比(ACOS): ${row.advacos || 0}%

成本明细：
- 采购成本: ${row.landedCurrency || '$'}${costDetailMap.purchase || 0}
- 运费: ${row.landedCurrency || '$'}${costDetailMap.shipment || 0}
- 佣金: ${row.landedCurrency || '$'}${costDetailMap.referralFee || 0}
- 仓储费: ${row.landedCurrency || '$'}${costDetailMap.storageFee || 0}
- FBA费用: ${row.landedCurrency || '$'}${costDetailMap.fba || 0}
- 税费: ${row.landedCurrency || '$'}${costDetailMap.tax || 0}
- 营销费用: ${row.landedCurrency || '$'}${costDetailMap.marketing || 0}
- 其他费用: ${row.landedCurrency || '$'}${costDetailMap.othersFee || 0}
- 促销费用: ${row.landedCurrency || '$'}${costDetailMap.promotionFee || 0}
- 手动处理费: ${row.landedCurrency || '$'}${costDetailMap.manualProcessingFee || 0}
- 标签服务费: ${row.landedCurrency || '$'}${costDetailMap.labelServiceFee || 0}
- 预处理费: ${row.landedCurrency || '$'}${costDetailMap.prepServiceFee || 0}
- 现场费: ${row.landedCurrency || '$'}${costDetailMap.inPlaceFee || 0}
- 货币转换费: ${row.landedCurrency || '$'}${costDetailMap.currencyTransportFee || 0}

成本率分析：
- 税率: ${costDetailMap.taxRate || 0}%
- 营销费率: ${costDetailMap.marketingRate || 0}%
- 促销费率: ${costDetailMap.promotionRate || 0}%
- 其他费率: ${costDetailMap.otherRate || 0}%

产品信息：
- 产品等级: ${costDetailMap.productTier || '无'}
- 产品等级ID: ${costDetailMap.productTierId || '无'}
- 出库重量: ${costDetailMap.outboundWeight || 0}

标签: ${row.tagNameList?.map(tag => tag.name).join(', ') || '无'}

请基于以上数据，提供以下分析：
1. 产品整体运营状况评估
2. 销售趋势分析
3. 利润优化建议
4. 库存管理建议
5. 广告投放优化建议
6. 价格调整建议
7. 风险预警和应对策略`;
  let currentRowData={};
  currentRowData.content = analysisPrompt;
  currentRowData.title = "产品运营分析"+row.sku;
  state.dialogVisible = true;
  nextTick(()=>{
    deepseekRef.value.show(currentRowData,"initMsg");
  })

}


function handleSalesInfo(){
  let productData={
    msku:state.dataRow.msku,
    sku:state.dataRow.sku,
    marketplaceid:state.dataRow.marketplaceid,
    groupid:state.dataRow.groupid,
    daysize:30,
  }
  productData.lineType="sales";
  productData.amazonAuthId=state.dataRow.amazonAuthId;
  state.isLoading = true;
  saleschartApi.salesLine(productData).then((res)=>{
      if(res.data){
          let salesData=res.data;
          let salesRowData="";
          
          // 提取数据
          let productName = salesData.legends?.[0] || '未知产品';
          let labels = salesData.labels || [];
          let salesDataArray = salesData.lines?.[0]?.data || [];
          let totalSales = salesData.lines?.[0]?.summary || 0;
          
          // 计算日均销量
          let avgSales = salesDataArray.length > 0 ? (totalSales / salesDataArray.length).toFixed(2) : 0;
          
          // 计算最高销量和最低销量
          let maxSales = Math.max(...salesDataArray) || 0;
          let minSales = Math.min(...salesDataArray) || 0;
          
          // 生成销量趋势描述
          let trendDescription = "";
          if(salesDataArray.length >= 2){
              let firstHalf = salesDataArray.slice(0, Math.floor(salesDataArray.length/2));
              let secondHalf = salesDataArray.slice(Math.floor(salesDataArray.length/2));
              let firstHalfAvg = firstHalf.reduce((a, b) => a + b, 0) / firstHalf.length;
              let secondHalfAvg = secondHalf.reduce((a, b) => a + b, 0) / secondHalf.length;
              
              if(secondHalfAvg > firstHalfAvg * 1.1){
                  trendDescription = "销量呈上升趋势";
              } else if(secondHalfAvg < firstHalfAvg * 0.9){
                  trendDescription = "销量呈下降趋势";
              } else {
                  trendDescription = "销量保持稳定";
              }
          }
          
          // 构建中文问答格式
          salesRowData = `请继续根据以下的产品近30日销量信息，继续提供分析：

产品近30日销量信息：
- 产品名称: ${productName}
- 统计周期: ${labels[0]} 至 ${labels[labels.length-1]}
- 总销量: ${totalSales} 件
- 日均销量: ${avgSales} 件
- 最高销量: ${maxSales} 件
- 最低销量: ${minSales} 件
- 销量趋势: ${trendDescription}

每日销量详情：
`;
          
          // 添加每日销量数据
          for(let i = 0; i < labels.length; i++){
              salesRowData += `- ${labels[i]}: ${salesDataArray[i] || 0} 件
`;
          }
          
          // 添加分析请求
          salesRowData += `
请基于以上销量数据，提供以下分析：
1. 销量趋势的具体分析
2. 销量波动的可能原因
3. 基于销量数据的库存管理建议
4. 销售策略优化建议
5. 未来销量预测`;
          
          nextTick(()=>{
            deepseekRef.value.show(salesRowData,"addMsg");
          });
      }
	});
}

function handleReviewInfo(){
  let reviewData={
    amazonauthid:state.dataRow.amazonAuthId,
    asin:state.dataRow.asin,
    marketplaceid:state.dataRow.marketplaceid,
    sortby:"STAR_RATING_IMPACT"
  };
  state.isLoading = true;
  listingApi.getItemReviewTopics(reviewData).then((res)=>{
      if(res.data){
          let reviewTopics=res.data;
          let reviewRowData="";
          
          // 构建中文问答格式
          reviewRowData = `请继续根据以下的产品评论信息，继续提供分析：

产品评论信息：
- 产品名称: ${state.dataRow.name || '未知产品'}
- SKU: ${state.dataRow.sku || '未知'}
- ASIN: ${state.dataRow.asin || '未知'}
- 站点: ${state.dataRow.marketplacename || '未知'}

评论分析：
`;
          
          // 添加肯定评论
          if(reviewTopics.topics && reviewTopics.topics.positiveTopics && reviewTopics.topics.positiveTopics.length > 0){
              reviewRowData += `
【肯定评论】
`;
              reviewTopics.topics.positiveTopics.forEach((topic, index) => {
                  reviewRowData += `${index + 1}. ${topic.topic}
`;
                  reviewRowData += `   - 评论人数: ${topic.asinMetrics?.numberOfMentions || 0}
`;
                  reviewRowData += `   - 发生率: ${topic.asinMetrics?.occurrencePercentage || '0%'}
`;
                  reviewRowData += `   - 星级影响: ${topic.asinMetrics?.starRatingImpact || '0'}
`;
                  if(topic.reviewSnippets && topic.reviewSnippets.length > 0){
                      reviewRowData += `   - 评论片段: ${topic.reviewSnippets.slice(0, 2).join('; ')}
`;
                  }
              });
          }
          
          // 添加否定评论
          if(reviewTopics.topics && reviewTopics.topics.negativeTopics && reviewTopics.topics.negativeTopics.length > 0){
              reviewRowData += `
【否定评论】
`;
              reviewTopics.topics.negativeTopics.forEach((topic, index) => {
                  reviewRowData += `${index + 1}. ${topic.topic}
`;
                  reviewRowData += `   - 评论人数: ${topic.asinMetrics?.numberOfMentions || 0}
`;
                  reviewRowData += `   - 发生率: ${topic.asinMetrics?.occurrencePercentage || '0%'}
`;
                  reviewRowData += `   - 星级影响: ${topic.asinMetrics?.starRatingImpact || '0'}
`;
                  if(topic.reviewSnippets && topic.reviewSnippets.length > 0){
                      reviewRowData += `   - 评论片段: ${topic.reviewSnippets.slice(0, 2).join('; ')}
`;
                  }
              });
          }
          
          // 添加分析请求
          reviewRowData += `
请基于以上评论数据，提供以下分析：
1. 产品优势和劣势分析
2. 基于评论的产品改进建议
3. 客户关注点分析
4. 与竞争对手的潜在差异
5. 如何利用评论优势进行营销`;
          
          nextTick(()=>{
            deepseekRef.value.show(reviewRowData,"addMsg");
          });
      }
	});
}



defineExpose({
		show,
	})

</script>

<style scoped>
.ai-dialog :deep(.el-dialog) {
  margin: 0;
  padding: 0;
  max-height: calc(100vh - 40px);
  display: flex;
  flex-direction: column;
}

.ai-dialog :deep(.el-dialog__header) {
  margin: 0;
  padding: 16px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 8px 8px 0 0;
}

.ai-dialog :deep(.el-dialog__title) {
  color: #fff;
  font-weight: 600;
}

.ai-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
}

.ai-dialog :deep(.el-dialog) {
  height: 600px;
  display: flex;
  flex-direction: column;
}

.ai-dialog :deep(.el-dialog__body) {
  padding: 0;
  flex: 1;
  overflow: hidden;
  display: flex;
}

.ai-dialog-content {
  display: flex;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.ai-main-area {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.ai-sidebar {
  width: 180px;
  padding: 16px;
  background: #f5f7fa;
  border-left: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.sidebar-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-btn {
  width: 100%;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.sidebar-btn .el-icon {
  font-size: 16px;
}

/* 覆盖 DeepSeekInfo 组件的样式 */
.ai-main-area :deep(.deepseek-app) {
  height: 100%;
  background: #fff;
  display: flex;
  flex-direction: column;
}

.ai-main-area :deep(.messages-wrapper) {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

.ai-main-area :deep(.product-messages-wrapper) {
  flex: 1;
  border: none;
  border-radius: 0;
  height: auto;
  min-height: 0;
  overflow-y: auto;
}

.ai-main-area :deep(.product-input-area) {
  border: none;
  border-top: 1px solid #e4e7ed;
  border-radius: 0;
  flex-shrink: 0;
}
</style>
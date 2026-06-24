<template>
  <el-col :span="6">
    <el-space style="margin-bottom:8px;">
      <span class="pag-title">店铺绩效</span>
      <span class="pag-small-Extra">更新时间 {{refreshtime}}</span>
    </el-space>
    <div class="pag-radius-bor">
        <div class="st-list" :class='s.state' v-for="s in storeData" :key="s.id">
          <div class="st-label">
            <div class="title">{{s.label}}</div>
            <div class="pag-small-Extra">目标:{{s.subdata}}</div>
          </div>
          <div class="pag-data-num">{{s.data}}</div>
        </div>
    </div>
  </el-col>
</template>
<script>
	import { ref,reactive,onMounted,watch } from 'vue'
	import summaryDataApi from '@/api/amazon/summary/summaryDataApi.js'
	import {formatFloat} from '@/utils/index.js';
export default {
  name: "storeshow",
  components: {},
  props:["parameter"],
  setup(prop,context){
    let storeData = ref([
      { label: "订单缺陷率", data: "0.0%", subdata: "低于0%" },
      { label: "发票缺陷率", data: "0.0%", subdata: "低于0%" },
      { label: "商品政策合规性", data: "0", subdata: "0" ,state:'danger'},
      { label: "有效跟踪率", data: "100%", subdata: "高于0%" },
	  { label: "迟发率", data: "0", subdata: "低于0%" },
	  { label: "订单取消率", data: "0.0%", subdata: "低于0%" }
    ]);
	let refreshtime=ref("");
	watch(prop.parameter,(val)=>{
	 
	        summaryDataApi.sumPerformance(prop.parameter).then((res)=>{
				refreshtime.value=new Date(res.data.refreshTime).format("yyyy-MM-dd hh:mm:ss"); 
				storeData.value[0].data=formatFloat(res.data.orderDefectRate*100)+"%";
				storeData.value[0].subdata="低于"+formatFloat(res.data.orderDefectRateTargetValue*100)+"%";
				storeData.value[1].data=formatFloat(res.data.invoiceDefectRate*100)+"%";
				storeData.value[1].subdata="低于"+formatFloat(res.data.invoiceDefectRateTargetValue*100)+"%";
				storeData.value[2].data=res.data.policyDefectsCount;
				storeData.value[2].subdata=res.data.policyTargetValue;
				storeData.value[3].data=formatFloat(res.data.validTrackingRate*100)+"%";
				storeData.value[3].subdata="高于"+formatFloat(res.data.validTrackingRateTargetValue*100)+"%";
				storeData.value[4].data=formatFloat(res.data.lateShipmentRate*100)+"%";
				storeData.value[4].subdata="低于"+formatFloat(res.data.lateShipmentRateTargetValue*100)+"%";
				storeData.value[5].data=formatFloat(res.data.cancelRate*100)+"%";
				storeData.value[5].subdata="低于"+formatFloat(res.data.cancelRateTargetValue*100)+"%";
			});
	});
    //返回数据
    return {
		storeData,refreshtime
	};
  }
};
</script>
<style>

.st-list{display:flex;padding:7px 16px;border: 1px solid var(--el-color-info-light-8);margin:8px 0;
           border-radius: var(--el-border-radius-base); 
		   align-items: center;
}
.st-list .st-label .title{font-size:14px;color:var(--el-text-color-primary)}
.st-list .pag-data-num{ margin-left:auto;margin-bottom:0;font-size:14px;}
.danger{
	background-color:var(--el-color-danger-light-9);
    border-color:var(--el-color-danger-light-5);
    color:#e23f22
	}
</style>

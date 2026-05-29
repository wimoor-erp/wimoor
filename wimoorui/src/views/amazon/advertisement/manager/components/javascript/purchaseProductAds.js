	import advProductsApi from '@/api/amazon/advertisement/report/advProductsApi.js';
	import adgroupsStatus from '@/model/amazon/advertisement/status.json';
	import {ElMessage,ElDivider} from 'element-plus';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import advSBAdsApi from '@/api/amazon/advertisement/report/advSBAdsApi.js';
	import {nextTick} from 'vue';
	
	export function loadList(state){
		if(state.queryParams.campaignType.toUpperCase()=="SB"){
			advSBAdsApi.getPurchaseAdsList(state.queryParams).then((res)=>{
				state.tableData.records =  res.data.records;
				state.tableData.total = res.data.total;
			})
		}else if(state.queryParams.campaignType.toUpperCase()=="SP"){
			advProductsApi.getPurchaseProductAdList(state.queryParams).then((res)=>{
				state.tableData.records =  res.data.records;
				state.tableData.total = res.data.total;
			})
		}else{
			advProductsApi.getSDPurchaseProductAdList(state.queryParams).then((res)=>{
				state.tableData.records =  res.data.records;
				state.tableData.total = res.data.total;
			})
		}
	}
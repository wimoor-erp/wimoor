<template>
	<el-table :show-header="false" :data='tableData' >
		<el-table-column fixed label="周期" prop="cycle" width="260"/>
		<el-table-column label="状态"/>
		<el-table-column label="广告组合" width="120 "/>
		<el-table-column label="分时策略" width="100 "/>
		<el-table-column label="类型" width="60"/>
		<el-table-column label="开始日期" width="120 " />
		<el-table-column label="结束日期" width="120 " />
		<el-table-column label="预算"  width="80" />
		<el-table-column label="竞价策略" width="190 " />
		<el-table-column label="曝光量" width="120 " />
		<el-table-column label="点击次数" width="120 " />
		<el-table-column label="点击率" width="120 " />
		<el-table-column label="每次点击费用" width="120 " />
		<el-table-column label="广告费"  width="120 "/>
		<el-table-column label="广告销售额" prop="attributedSalesSameSKU" width="120 " />
		<el-table-column label="销量" prop="sumUnits" width="120 "  />
		<el-table-column label="订单量" width="120 " prop="attributedUnitsOrdered" />
		<el-table-column label="ACOS"  width="120 " prop="ACOS" />
		<el-table-column label="ROAS" prop="ROAS" width="120 "/>
		<el-table-column label="转化率" prop="CSRT"  width="120 " />
		<el-table-column label="广告贡献销售额-目标商品" width="180 " prop="attributedSalesSameSKU" />
		<el-table-column label="广告贡献销售额-其它商品" width="180 "  prop="attributedSales" />
		<el-table-column label="广告转化量-目标商品" width="160 "  prop="attributedConversionsSameSKU" />
		<el-table-column label="广告转化量-其它商品" width="160 "  prop="attributedConversions"/>
		<el-table-column label="展开"/>
	</el-table>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue';
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	
	let props = defineProps({expandData:{},expandType:"" });
	const { expandData,expandType} = toRefs(props);
	const state = reactive({
		tableData:[
			
		],
	})
	const{
		tableData,
	}=toRefs(state);
	 
	function handleData(){
		if(props.expandType=="days"){
			//处理广告周期数据
			var advData=[];
			advData.push({
				cycle:'1天内成交',
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU1d,
				sumUnits:props.expandData.sumUnits1d,
				attributedUnitsOrdered:props.expandData.attributedUnitsOrdered1d,
				ACOS:formatPercent(props.expandData.ACOS1d)+"%",
				ROAS:formatPercent(props.expandData.ROAS1d)+"%",
				CSRT:formatPercent(props.expandData.CSRT1d)+"%",
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU1d,
				attributedSales:props.expandData.attributedSales1d,
				attributedConversionsSameSKU:props.expandData.attributedConversionsSameSKU1d,
				attributedConversions:props.expandData.attributedConversions1d
			});
			advData.push({
				cycle:'7天内成交',
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU7d,
				sumUnits:props.expandData.sumUnits7d,
				attributedUnitsOrdered:props.expandData.attributedUnitsOrdered7d,
				ACOS:formatPercent(props.expandData.ACOS7d)+"%",
				ROAS:formatPercent(props.expandData.ROAS7d)+"%",
				CSRT:formatPercent(props.expandData.CSRT7d)+"%",
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU7d,
				attributedSales:props.expandData.attributedSales7d,
				attributedConversionsSameSKU:props.expandData.attributedConversionsSameSKU7d,
				attributedConversions:props.expandData.attributedConversions7d
			});
			advData.push({
				cycle:'14天内成交',
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU14d,
				sumUnits:props.expandData.sumUnits14d,
				attributedUnitsOrdered:props.expandData.attributedUnitsOrdered14d,
				ACOS:props.expandData.ACOS14d,
				ROAS:props.expandData.ROAS14d,
				CSRT:props.expandData.CSRT14d,
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU14d,
				attributedSales:props.expandData.attributedSales14d,
				attributedConversionsSameSKU:props.expandData.attributedConversionsSameSKU14d,
				attributedConversions:props.expandData.attributedConversions14d
			});
			advData.push({
				cycle:'30天内成交',
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU30d,
				sumUnits:props.expandData.sumUnits30d,
				attributedUnitsOrdered:props.expandData.attributedUnitsOrdered30d,
				ACOS:props.expandData.ACOS30d,
				ROAS:props.expandData.ROAS30d,
				CSRT:props.expandData.CSRT30d,
				attributedSalesSameSKU:props.expandData.attributedSalesSameSKU30d,
				attributedSales:props.expandData.attributedSales30d,
				attributedConversionsSameSKU:props.expandData.attributedConversionsSameSKU30d,
				attributedConversions:props.expandData.attributedConversions30d
			});
			 
			state.tableData=advData;
		}
		
	}
	onMounted(() => {
		handleData();
	
	});
</script>

<style>
	 .el-table__expanded-cell{
		padding:0px!important;
	}
	 .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
</style>
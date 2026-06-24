<template>
	<div  >
	<div class="con-header">
    <Group ref="groups" @change="changeGroup" defaultValue="only"/>
    <Currency ref="currencyRef" defaultValue="MARKET"   @change="changeCurrency"/>
		  <el-row  style="margin-top:20px">
			  <el-col :span="5">
				   <el-card :shadow="false">
					   <div class="flex-top-between">
						   <div>
						   <el-space direction="vertical" alignment="normal">
							   <span class="font-small">销售额
							   {{queryParams.Currency}}($)
							   </span>
							   <span class="font-large font-bold">{{outputmoney(quarterData.amount)}}</span>
						   </el-space>
						</div>
						   <div>
							    <QuarterPicker
							      ref="quarterPickerRef"
							      v-model="queryParams.quarterValue"
							      format="YYYY年Q季度"
								  :disabled-date="disabledQuarter"
								   @change="getQuarter"
							    />
						   </div>
					   </div>
					   <p class="font-extraSmall">
					    <span >环比上季度 {{outputmoney(quarterData.oldamount)}}</span>
					    <span style="padding-left:10px" class="text-green" v-if="quarterData.rate>0"> {{quarterData.rate}}%
						  <el-icon ><CaretTop /></el-icon>
						</span>
						<span style="padding-left:10px" class="text-red" v-else> {{quarterData.rate*-1}}%
						  <el-icon ><CaretBottom /></el-icon>
						</span>
					  
					    </p>
				   </el-card>
			  </el-col>
		  </el-row>
		  <el-row>
			  <el-col :span="24">
				  <div class="flex-center-between">

				   <el-button 
				   @click="exchangeRateSet"
				   type="primary"  >汇率设置</el-button>
				   <div>
				   <el-space  >
					      <Datepicker type="monthrange" :shortIndex="1" ref="datepickers"   unlink-panels @changedate="changedaterange" />
				   </el-space>
				   <el-button style="margin-left:10px" type="primary" @click="downloadData">导出</el-button>
				   </div>
				   </div>
			  </el-col>
		  </el-row>
	</div>

	</div>

		<Month ref="monthRef" />

	<el-popover
	  ref="popoverRef"
	  placement="top"
	  :virtual-ref="editPriceRef"
	  trigger="click"
	  virtual-triggering
	  width="200px"
	>
	<el-input v-model="skuprice"></el-input>
	</el-popover>
	 
	<OtherFeeDialog ref="otherFeeDiaRef"></OtherFeeDialog>
	<AccCalcDialog   ref="calcDialogRef"></AccCalcDialog>
	<RefreshDataDialog ref="refreshDataDialogRef"></RefreshDataDialog>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs,} from 'vue'
	import {Search,ArrowDownBold,Download,Upload,InfoFilled,HomeFilled,Switch,CaretTop,CaretBottom,Edit} from '@element-plus/icons-vue';
	import Group from '@/components/header/group.vue';
	import {Plus,Formula,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import {formatFloat,formatPercent,formatInteger,decodeText,getCurrencyMark,outputmoney} from '@/utils/index.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import Currency from"@/components/header/currency.vue";
	import OtherFeeDialog from"./components/other_fee_dialog.vue";
	import QuarterPicker from "@/components/header/quarter_picker.vue";
	import AccCalcDialog from '@/views/amazon/payment/profitReport/components/accCalc_dialog.vue';
	import RefreshDataDialog from '@/views/amazon/payment/common/refresh_data_dialog.vue';
  import financesApi from '@/api/amazon/finances/monthFinance.js';
	import Month from"./components/month.vue"
	import { useRoute,useRouter } from 'vue-router'
	const editPriceRef = ref();
	const calcDialogRef=ref();
	const refreshDataDialogRef=ref();
	const otherFeeDiaRef = ref();
	const globalTable=ref();
	const poporef = ref();
	const monthRef = ref();
	const currencyRef=ref();
	const router = useRouter();
	const state = reactive({
		queryParams:{currency:'MARKET',charttype:"sku",fatype:"sku","monthDate":new Date(new Date().getFullYear(), new Date().getMonth() - 1, 1),"quarterValue":new Date()},
		orderState:[
			{label:'SKU',name:'0',},
			{label:'月度',name:'1',},
		],
		quarterData:{},
		tableData:{
			records:[],
			total:1,
		}
	})
	const {
		orderState,
		quarterData,
		tableData,
		queryParams,
	} = toRefs(state)
	
	function recalculation(){
		refreshDataDialogRef.value.show();
	}

function getQuarter(){
	var myparams=state.queryParams;
	myparams.fromDate=state.queryParams.quarterValue;
	financesApi.getQuarter(myparams).then((res)=>{
		state.quarterData=res.data;
		var rate=0;
		if(state.quarterData.oldamount){
			rate=(parseFloat(state.quarterData.amount)-parseFloat(state.quarterData.oldamount))/parseFloat(state.quarterData.oldamount)*100
		}
		state.quarterData.rate=formatFloat(rate);
	});
}
function getMonthData(){
	var params=state.queryParams;
	params.fromDate=state.queryParams.monthfromDate;
	params.endDate=state.queryParams.monthendDate;
	params.currency=state.queryParams.currency;
	financesApi.getMonth(params).then((res)=>{
		monthRef.value.show(res.data);
	});
}
function handleChange(val){
	 handleQuery();
}
function formulaConfig(){
	calcDialogRef.value.show();
}

 
 function getFBARef(el){
	 if(el){
		 poporef.value.buttonRef = el;
	 }
 }
 function changeCurrency(value){
	  state.queryParams.currency=value;
	  handleQuery();
 }
 function editPrice(row){
	 state.skuprice = row.price;
 }
 function handleOtherFee(){
	 otherFeeDiaRef.value.show();
 }
 function handleQuery(){
	 	getQuarter();
	 	getMonthData();

	 
 }

 function changeGroup(obj){
 	state.queryParams.groupid=obj.groupid;
 	state.queryParams.marketplaceid=obj.marketplaceid;
 	handleQuery();
 }
 //日期改变
 function changedate(dates){
 	 state.queryParams.monthDate=dates;
 	// state.queryParams.endDate=dates.end; 
 	handleQuery();
 }
 function changedaterange(dates){
	state.queryParams.monthfromDate=dates.start; 
	state.queryParams.monthendDate=dates.end; 
	getMonthData();
 }
 function exchangeRateSet(){
				router.push({
					path:"/finance/exchangeRate",
					query:{
						title:'汇率管理',
						path:'/finance/exchangeRate',
						
					}
				})
 }
 function cellClassName(data){
	 if(data.column.property=="setincome"){
		 return "cell-setincome";
	 }
 }
 function downloadData(){

		 var params=state.queryParams;
		  params.fromDate=state.queryParams.monthfromDate;
		  params.endDate=state.queryParams.monthendDate;
		  financesApi.downloadMonth(params);
 }
 
 
</script>
<style  >
.cell-setincome {
	background-color: #dedede;
}
.cell-setincome:hover {
	background-color: #dedede;
}
</style>
<style scoped>
	.flex-top-between{
		display: flex;
		justify-content: space-between;
	}
</style>
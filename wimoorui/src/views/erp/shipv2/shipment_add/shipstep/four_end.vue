<template>
	<div class="box-margin">
		<div class="pag-radius-bor mar-bot" >
			<div class="con-body lylStepTwo" > 
			  <Header ref="headerRef" ></Header>
			  <Table  ref="tableRef" :nopage="true" @change="handleTableChange"/>
			</div>
		</div>
		
		<Footer  ref="footerRef"></Footer>
	</div>
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue';
	import { useRoute,useRouter } from 'vue-router';
	import {Location,Van,ShoppingCart,Printer,Document} from '@element-plus/icons-vue';
	import Footer from "./components/footer.vue";
	import Header from "./components/header.vue";
	import Operation from "./components/operation.vue";
	import { ElMessage,ElMessageBox } from 'element-plus';
	import addressApi from '@/api/amazon/inbound/addressApi.js';
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	import shipmentBoxApi from '@/api/erp/shipv2/shipmentBoxApi.js';
	import shipmentTransportationApi from '@/api/erp/shipv2/shipmentTransportationApi.js';
	import Table from "@/views/erp/shipv2/shipment_handing/list/components/table.vue";
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	let router = useRouter();
	const planid=router.currentRoute.value.query.id;
	const footerRef=ref();
	const headerRef=ref();
	const globalTable=ref();
	const tableRef=ref();
	const operationRef=ref();
	let state =reactive({
		planData:{}, 
		step:0,
		itemcount:0,
		addressVisiable:false,
		operatorVisiable:false,
		operatorRow:null,
		btnStatus:{
			"submitForm":false,
			"submitStep":false,
			"number":1,
		},
		groupList:[],
		
	})
	let{planData,step,itemcount,addressVisiable,operatorVisiable,operatorRow,btnStatus,groupList}=toRefs(state);
	
	function stepChange(val){
		state.step=val;
		//loading.value=true;
		//shipmentRef.value.getBaseInfo(shipmentid);
	}

	function loadTable(){
		var obj={pagesize:1000};
		obj.formid = planid;
		tableRef.value.getshipmentData(obj);
	}
	
	function loadData(){
		shipmentplanApi.getPlanInfo({"formid":planid}).then((res)=>{
			state.planData=res.data;
			state.itemcount=state.planData.itemlist.length;
			headerRef.value.show(state.planData,4);
			footerRef.value.show(state.planData);
			loadTable();
		});
	}
	 
	
	onMounted(()=>{
		loadData();
	})
</script>

<style>
	.lylStepTwo .el-card__header {

	}
	.pull-right{
		float: right;
	}
	.mar-bot{
		margin-bottom:16px;
	}
	.box-margin{
		padding:16px;
		min-height:calc(100vh - 36px)
	}
</style>
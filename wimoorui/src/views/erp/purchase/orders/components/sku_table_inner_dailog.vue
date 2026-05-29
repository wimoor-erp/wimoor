<template>
  <GlobalTable ref="globalTable"   
    @selectionChange='selectChange' 
     :tableData="tableData" 
     :stripe="true" 
	 highlight-current-row
	 height="calc(100vh - 350px)"
	 @current-change="handleCurrentChange"
    @loadTable="loadTableData"  >
	<template #field>
	   <el-table-column  prop="sku" label="产品信息" max-width="300"   sortable="custom">
	      <template #default="scope">
			  <el-space>
				  <div @click.stop="handleToMaterial(scope.row)" >
				  	  					<el-image v-if="scope.row.image" :src="scope.row.image"    class="pointer img40" ></el-image>
				  	  					<el-image   v-else :src="$require('empty/noimage40.png')"   class="pointer img40"  ></el-image>
				  </div>
				  <div>
					  <div  ><span class='font-bold '>{{scope.row.sku}}</span>
					   <copy style="padding:3px;" @click.stop="CopyText(scope.row.sku)" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
					   <el-tag class="pointer" v-if="scope.row.issfg=='2'" size="small" effect="plain" type="warning" @click.stop="showAssembly(scope.row)">组装</el-tag>
					  </div>
					   <div class='name font-extraSmall text-omit-1'>{{scope.row.mname}}</div>
					   <div>
						   <span  class='tag-mr' v-if='scope.row.TagNameList'  v-for='(s,index) in scope.row.TagNameList' :key='index'>
						       <el-tag size="small"  effect="plain" :title="s.name" :type="s.color">  {{s.name}}</el-tag>
						   </span>
					   </div>
				  </div>
			  </el-space>
	     </template>
	   </el-table-column>
		<el-table-column  label="状态"  prop="auditstatus" >
		   <template #default="scope">
			   <el-space>
			   <el-tag  v-if="scope.row.auditstatus==0" type="danger">已作废</el-tag>
			   <el-tag  v-if="scope.row.auditstatus==1" type="warning">待审核
			   <span v-if="scope.row.ischange==1" style="color: #ff0f47;">
			        <el-icon @click="handlePurchase(scope.row)"><Warning /></el-icon>
			   </span>
			   </el-tag>
			   <el-tag  v-if="scope.row.auditstatus==2 && scope.row.paystatus!=3" type="success">执行中</el-tag>
			   <el-tag  v-if="scope.row.auditstatus==3" type="info">已完成</el-tag>
			   <el-tag  v-if="scope.row.paystatus==3" type="warning">请款中</el-tag>
			   <div v-if="scope.row['orderstatus']">
							  <span v-if="orderStatus[scope.row.orderstatus]" class="tag-group"><span class="before">1688</span>
							     <span :class="orderStatus[scope.row.orderstatus].color" style="padding-left:2px">   {{orderStatus[scope.row.orderstatus].simple}} </span> 
							  </span> 
							  <span v-else class="tag-group"><span class="before">1688</span> 待收货<!-- {{sub.orderstatus}} --></span> 
			   		 </div>
			   </el-space>
			  		<div class="table-edit-flex font-small" >
						<span v-if="scope.row.remark!=''&&scope.row.remark!=undefined">
						<span class='font-extraSmall word-break'>备注:</span>
						{{scope.row.remark}}</span>
						<span v-else >-</span>
			  		</div>
		  </template>
		</el-table-column>
		<el-table-column  label="供应商信息" prop="suppliername"     sortable="custom" >
		   <template #default="scope">
			   <p></p>
			   <p class="pointer" >
			   <el-link :underline="false"  @click.stop="handleToSupplier(scope.row.purchaseUrl)"><span class="text-gray word-break">供应商：</span>{{scope.row.suppliername}}</el-link>
			    </p>
			  <p  ><span class=" text-gray">供货周期(天)：</span>
								{{scope.row.delivery_cycle}}
			  </p> 
               <p><span class="text-gray">起订量：</span>{{scope.row.moq}}</p>
		  </template>
		</el-table-column>
        <el-table-column  label="订单信息"  prop="number" show-overflow-tooltip  sortable="custom"  >
		   <template #default="scope">
			<p><span class="text-gray ">订单编码：</span>{{scope.row.number}} <span class="text-gray"> ({{scope.row.creatorname}})</span></p>
			<p class="font-base"><span class="text-gray">入库仓库：</span>{{scope.row.warehousename}}</p>
			<p v-if="tableState=='4' || tableState=='5' || tableState=='6'"> <span class="text-gray">审核日期:</span>{{dateFormat(scope.row.audittime)}}</p>
			<p v-else><span class="text-gray ">创建日期：</span>{{dateFormat(scope.row.createdate)}} </p>
		  </template>
		</el-table-column>

		<el-table-column  label="采购信息"  width="140" >
		   <template #default="scope">
			   <div>
			   <span class="word-break text-gray">单价：</span>
			   <span >￥{{scope.row.itemprice}}</span>
			   </div>
			   <div>
			   <span class="word-break text-gray">数量：</span>
			   <span >{{scope.row.amount}}</span>
			   </div>
			   <div >
			   <span class="word-break text-gray">金额：</span>
			   <span >￥{{scope.row.orderprice}} </span>
			   </div>
		  </template>
		</el-table-column>
		
		<el-table-column v-if="tableState=='4' || tableState=='5' || tableState=='6'" label="到货数" prop="auditstatus"  width="200">
			 <template #default="scope">
				 <!-- <div> {{scope.row.totalin}} </div> -->
				  <div class="flex-center" :title="'已入库:'+scope.row.totalin">
					   <span class="text-gray">入库进度:</span>
					  <el-progress style="flex-grow:1" :percentage="(scope.row.totalin/scope.row.amount)*100"  :status="scope.row.inwhstatus==0?'':'success'">
					       <el-tag v-if="scope.row.inwhstatus==0&&scope.row.totalin>0" effect="plain" type="info"  round size="small">入库中</el-tag>
						   <el-tag v-else-if="scope.row.inwhstatus==0" effect="plain" type="warning"  round size="small">未入库</el-tag>
					       <el-tag v-else effect="plain" type="success"  round size="small">已入库</el-tag>
					  </el-progress>
				  </div>
				  <div class="flex-center" :title="'已付款:'+scope.row.totalpay">
					  <span class="text-gray">付款进度:</span>
				  	  <el-progress style="flex-grow:1" :percentage="(scope.row.totalpay/scope.row.orderprice)*100"  :status="scope.row.paystatus==0?'':'success'">
				  		<el-tag v-if="scope.row && scope.row.paystatus==1" effect="plain" type="success"  round size="small">已结清</el-tag>
				  		<el-tag v-else effect="plain" type="warning"  round size="small">未结清</el-tag> 
				  	  </el-progress>
				  </div>
				<p>  <span class="text-gray">预计到货:</span><DeliveryDate :sub="scope.row"></DeliveryDate></p>
			 </template>
		</el-table-column>
		 <el-table-column  label="操作" width="140" fixed="right" >
		    <template #default="scope">
		 	   <el-space>
		 	  	  	<el-button  link type="primary" @click="handleDetails(scope.row)">详情</el-button>
		 	   	</el-space>
		   </template>
		 </el-table-column>
		</template>
  </GlobalTable>
  <Records ref="RecordsRef"/>
  <ChangeRecords ref="changeRecordsRef"/>
  <Assembly ref="AssemblyRef"/>
  <Receipt ref="ReceiptRef" @change="changeRec" />
  <Payment ref="PaymentRef" @change="changePay"/>
  <Details ref="detailsRef"/>
</template>

<script setup> 
	import {h,ref,reactive,toRefs,getCurrentInstance,inject	} from 'vue'
	import {Download,Edit,DeleteFilled,ArrowDown,Warning,ArrowLeft,ArrowRight} from '@element-plus/icons-vue';
	import {MoreOne,Copy} from '@icon-park/vue-next';
	import CopyText from"@/utils/copy_text.js";
	import {useRouter } from 'vue-router'
	import {ElDivider} from 'element-plus';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,formatFloat,dateTimesFormat,CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import NullTransform from"@/utils/null-transform";
	import Assembly from "./assembly.vue"; 
	import Records from "../records.vue";
	import ChangeRecords from "./change_records.vue"; 
	import DeliveryDate from "./deliverydate.vue"; 
	import Receipt from "./receipt.vue";
	import Payment from "./payment.vue"; 
	import Details from "./details.vue"; 
	import orderStatus from '@/model/erp/purchase/open1688/order_status.json';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import {updateItem,handleChangesub,changeItemPrice,changeItemOrderprice,changeItemAmount,handleChanges,updatenotice,handleDelete,
	updateCycleDate} from  '@/hooks/erp/purchase/form.js';	const router = useRouter()
	const changeRecordsRef=ref();
	const AssemblyRef= ref();
	const RecordsRef = ref();
	const detailsRef = ref();
	const PaymentRef = ref();
	const ReceiptRef = ref();
	const emit = defineEmits(['change',]);
	let globalTable=ref();
	 const   proxy  = getCurrentInstance();
	const spacer = h(ElDivider, {
		direction: 'vertical'
	})
	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		checkedRowsData:[],
	})
	const {
		tableData,queryParams,
		checkedRowsData,
	} = toRefs(state)
	const props = defineProps({
	             tableState:String,
	        })
	const {
		tableState,
	} =toRefs(props);
	function checkedRowClass({row,rowIndex}){
		var a = '';
		state.checkedRowsData.filter(item=>{
			if(item==row){
				a='height-currnet-row'
			}
		})
		return a;
	}
	
	function showRemarkPopover(sub){
	    sub.remark2=sub.remark;
	}
	function loadTableData(params){
		state.queryParams=params;
		params.needinventory=true;
		purchaselistApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
 
	function handleToSupplier(link){
		 window.open(link,"_blank");
	}
	function handleToMaterial(row){
		 var type=null;
		 if(row.mtype==1 || row.mtype=="1"){
		 	type='consumable';
		 }
		 if(row.mtype==2 || row.mtype=="2"){
		 	type='package';
		 }
		 if(row.mtype==0 || row.mtype=="0"){
		 	type='product';
		 }
		  router.push({
		 	path:'/material/details',
		 	query:{
		 	  title:"产品详情",
		 	  path:'/material/details',
		 	  details:row.materialid,
		 	  type:type
		 	}
		  })
	}
	function cancelChangesub(row){
		row.withoutEdit=true;
	}
	function showDatePopover(sub){
	    sub.delivery_cycle2=sub.delivery_cycle;
	}	 
	//展示组装信息
	function showAssembly(sub){
		AssemblyRef.value.show(sub,"ass");
	}
	//审核通过
	function handlePass(sub){
		purchaselistApi.approval({"ids":sub.id}).then((res)=>{
			if(res.data){
				sub.auditstatus=2;
				sub.paystatus=0;
				sub.inwhstatus=0;
				ElMessage.success('审核通过 ');
			}
		});
	}
	function handleReturn(sub){
		AssemblyRef.value.show(sub,"return");
	}
	//采购记录
	function handleRecords(row){
		RecordsRef.value.show(row.id);
	}
	//变更记录
	function handlePurchase(row){
		changeRecordsRef.value.show(row.id);
	}
 
	function changePay(){
		emit("changepay");
	}
	function changeRec(){
		emit("changepay");
	}
	//收货
	function handleReceipt(sub,type){
		ReceiptRef.value.show(type,sub)
	}
	
	//换货 
	function gotoChange(row){
		emitter.emit("removeCache", "请款单");
		router.push({
			path:"/finance/paymentRequest",
			query:{
				title:'请款单',
				path:"/finance/paymentRequest",
				number:row.number,
			},
		})
	}
	
	//付款
	function handlePay(sub,type){
		PaymentRef.value.show(type,sub)
	}
	//采购单详情
	function handleDetails(row){
		detailsRef.value.show(row)
	}
   const emitter = inject("emitter");
   function gotoApprovePay(row){
		emitter.emit("removeCache", "请款单");
		router.push({
			path:"/finance/paymentRequest",
			query:{
				title:'请款单',
				path:"/finance/paymentRequest",
				number:row.number,
			},
		})
	}
	function load(params){
		globalTable.value.loadTable(params);
	}
	defineExpose({
	   load,
	})
	function handleCurrentChange(row){
		emit("change",row);
	}
	function pageUp(){
		if(parseInt(state.queryParams.currentpage)>1){
			state.queryParams.currentpage=parseInt(state.queryParams.currentpage)-1;
			globalTable.value.handleCurrentChange(state.queryParams.currentpage);
		}
	}
	function pageDown(){
		if(parseInt(state.queryParams.currentpage)<(parseInt(state.tableData.total)/parseInt(state.queryParams.pagesize)+1)){
			state.queryParams.currentpage=parseInt(state.queryParams.currentpage)+1;
			globalTable.value.handleCurrentChange(state.queryParams.currentpage);
		}
	}
</script>

<style scoped>
.tag-mr{margin-left:4px;margin-bottom:4px;display:inline-block}
.word-break{
	white-space:nowrap;
}
.img40{
	width:60px;
	height:60px;
}
.text-gray{
	color:#999;
}
.pull-right{
	float:right;
	margin-top:3px;
}
.tag-group{
	font-size: 12px;
	border:1px solid #999;
	color: #999;
	padding-right: 2px;
	padding-top: 2px;
	padding-bottom: 2px;
	border-radius: 2px;
}
.tag-group .before{
	color: #fff;
	background-color: #999;
	padding: 2px;
}
</style>
<style>
	.height-currnet-row td{
		background:#fbebe0!important;
	}
</style>
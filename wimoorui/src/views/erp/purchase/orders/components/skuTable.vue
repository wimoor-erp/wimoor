<template>
  <GlobalTable ref="globalTable"   
    @selectionChange='selectChange' 
     :tableData="tableData" 
     :stripe="true" 
	 :rowClassName="checkedRowClass"
	 rowKey="id"
    @loadTable="loadTableData"  >
	<template #field>
	   <el-table-column type="selection" width="40"></el-table-column>
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
			    <el-tag type="danger" v-if="scope.row.changenumber" class="pointer" :title="'换货单编号:'+scope.row.changenumber" style="margin-left:10px;" size="small" @click="handleChangeForm(scope.row.changenumber)" effect="dark">换货中</el-tag>
			   <div v-if="scope.row['orderstatus']">
							  <span v-if="orderStatus[scope.row.orderstatus]" class="tag-group"><span class="before">1688</span>
							     <span :class="orderStatus[scope.row.orderstatus].color" style="padding-left:2px">   {{orderStatus[scope.row.orderstatus].simple}} </span> 
							  </span> 
							  <span v-else class="tag-group"><span class="before">1688</span> 待收货<!-- {{sub.orderstatus}} --></span> 
			   		 </div>
			   </el-space>
			   <div class="table-edit-flex font-small" 
			   @click="e=>getRemarks(e,scope.row)">
				<span v-if="scope.row.remark!=''&&scope.row.remark!=undefined">
				<span class='font-extraSmall word-break'>备注:</span>
				{{scope.row.remark}}</span>
				<span v-else >-</span>
			    <el-icon><Edit /></el-icon>
			   	</div>

		  </template>
		</el-table-column>
		<el-table-column  label="供应商信息" prop="suppliername"     sortable="custom" >
		   <template #default="scope">
			   <p></p>
			   <p class="pointer" >
			   <el-link :underline="false"  @click.stop="handleToSupplier(scope.row.purchaseUrl)"><span class="text-gray word-break">供应商：</span>{{scope.row.suppliername}}</el-link>
			    </p>
			   <el-popover
			     v-if="scope.row.auditstatus==1"
			     placement="top-start"
			     title="供货周期"
			     :width="200"
			     trigger="click"
			   	@show="showDatePopover(scope.row)"
			   >
			   	<el-input
			   	    v-model="scope.row.delivery_cycle2"
			   	    :rows="2"
			   	    type="textarea"
			   	    placeholder="请输入"
			   	  />
			   		<el-button type="primary" class='m-t-8' @click="updateCycleDate(scope.row.materialid,scope.row.delivery_cycle2,scope.row)">确定</el-button>
			     <template #reference>
			   		<div class="table-edit-flex">
						<span class="text-gray ">供货周期(天)：</span>
			       <span v-if="scope.row.delivery_cycle!=''&&scope.row.delivery_cycle!=undefined">{{scope.row.delivery_cycle}}</span>
			   							   <span v-else >-</span>
			       <el-icon><Edit /></el-icon>
			   		</div>
			     </template>
			   </el-popover>	
			  <p v-else><span class=" text-gray">供货周期(天)：</span>
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
			   <span v-if="scope.row.withoutEdit==true">￥{{scope.row.itemprice}}
			   <div class="table-edit-flex pull-right">
			    <el-icon v-if="scope.row.auditstatus==1" @click="handleChanges(scope.row)"><Edit /></el-icon>
			   </div>
			   </span>
			   <el-input  v-model="scope.row.edit_itemprice" @input="changeItemPrice(scope.row)" v-else></el-input>
			   </div>
			   <div >
			   <span class="word-break text-gray">数量：</span>
			   <span v-if="scope.row.withoutEdit==true">{{scope.row.amount}}
			   <div class="table-edit-flex pull-right">
			    <el-icon v-if="scope.row.auditstatus==1" @click="handleChanges(scope.row)"><Edit /></el-icon>
			   </div>
			   </span>
			   <el-input  v-model="scope.row.edit_amount"   @input="changeItemAmount(scope.row)" v-else></el-input>
			   </div>
			   <div >
			   <span class="word-break text-gray">金额：</span>
			   <span v-if="scope.row.withoutEdit==true">￥{{scope.row.orderprice}}
			   <div class="table-edit-flex pull-right">
			    <el-icon v-if="scope.row.auditstatus==1" @click="handleChanges(scope.row)"><Edit /></el-icon>
			   </div>
			   </span>
			   <el-input  v-model="scope.row.edit_orderprice"  @input="changeItemOrderprice(scope.row)" v-else></el-input>
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
			<template #header>
				<el-space>
				<div>操作</div>
				<div>
					 <el-tooltip  placement="top"  content="上一页">
						 <span style="margin-right:3px;" v-if="parseInt(state.queryParams.currentpage)>1"  @click.stop="pageUp()"><el-icon class="pointer" ><ArrowLeft/></el-icon> </span>
						 <span   v-else  style="color:#dedede;margin-right:3px;"><el-icon ><ArrowLeft/></el-icon> </span>
					 </el-tooltip>

				     <el-tooltip   placement="top" content="下一页">
					 <span v-if="parseInt(state.queryParams.currentpage)<(parseInt(state.tableData.total)/parseInt(state.queryParams.pagesize))" 
					  @click.stop="pageDown()"><el-icon class="pointer" ><ArrowRight /></el-icon></span>
						<span v-else  style="color:#dedede"><el-icon ><ArrowRight/></el-icon> </span>
					 </el-tooltip>
				</div>
				</el-space>
			</template>
		   <template #default="scope">
			   <el-space>
			   <template v-if="scope.row.auditstatus==1 || scope.row.auditstatus==0">
			   	   <template v-if="scope.row.auditstatus==1  && scope.row.withoutEdit==true">
					<el-button link type="primary" @click="handlePass(scope.row)">通过</el-button>
					<el-button link type="primary" @click="handleReturn(scope.row)">驳回 </el-button>
			   	   </template>
			     <template v-if="scope.row.auditstatus==1">
					<el-space v-if="scope.row.withoutEdit==false" >
					    <el-button link  type="primary" @click="updateItem(scope.row)">保存</el-button>
						<el-button link  type="primary" @click="cancelChangesub(scope.row)">取消</el-button>
					</el-space>
				</template>
			   	<el-dropdown :hide-on-click="false">
			   	  <span class="el-dropdown-link">
					  <el-button type="primary" link>更多
					  <el-icon class="ic-cen"><ArrowDown /></el-icon>
					  </el-button>
			   	  </span>
			   	   <template #dropdown>
			   	    <el-dropdown-menu>
			   	      <el-dropdown-item @click="handleRecords(scope.row)">采购记录</el-dropdown-item>
					  <el-dropdown-item v-if="scope.row.auditstatus==0" @click="reSubmit(scope.row)">重新提交</el-dropdown-item>
			   	    </el-dropdown-menu>
			   	</template>
			   	</el-dropdown>
			   	
			   </template>
			   <el-space v-else>
			   	<el-button v-show="scope.row.inwhstatus==0&&tableState=='4' " link type="primary" @click="handleReceipt(scope.row,'rec')">收货</el-button>
			   	<el-button v-show="scope.row.paystatus==0&&tableState=='5'" link type="primary" @click="handlePay(scope.row,'pay')">付款</el-button>
				<el-button v-show="scope.row.paystatus==3" link type="primary" @click.stop="gotoApprovePay(scope.row)" >审核请款</el-button>
			   	<el-button v-show="scope.row.auditstatus==3||(scope.row.inwhstatus==1&&scope.row.paystatus==1)" link type="primary" @click="handleDetails(scope.row)">详情</el-button>
			   	<el-dropdown :hide-on-click="false" >
			   	  <span class="el-dropdown-link">
					  <el-button type="primary" link>更多
					  <el-icon class="ic-cen"><ArrowDown /></el-icon>
					  </el-button>
			   	  </span>
			   	   <template #dropdown>
			   	    <el-dropdown-menu>
			   		  <el-dropdown-item v-if="scope.row.inwhstatus==0&&scope.row.auditstatus==2&&tableState!='4'"   @click="handleReceipt(scope.row,'rec')">收货</el-dropdown-item>
			   	      <el-dropdown-item v-if="scope.row.auditstatus>=2" :class="tableState=='4'?'text-orange':''"  @click="handleReceipt(scope.row,'ret')">退货</el-dropdown-item>
			   		  <el-dropdown-item v-if="scope.row.paystatus==0&&scope.row.auditstatus==2&&tableState!='5'"    @click="handlePay(scope.row,'pay')">付款</el-dropdown-item>
			   		  <el-dropdown-item v-if="scope.row.auditstatus>=2"  :class="tableState=='5'?'text-orange':''"  @click="handlePay(scope.row,'refund')">退款</el-dropdown-item>
			   		  <el-dropdown-item v-if="scope.row.totalin==0 && (scope.row.totalpay==0 || scope.row.totalpay==0.0) && scope.row.withoutEdit==true" @click="handleChangesub(scope.row)">撤回</el-dropdown-item>
			   		  <el-dropdown-item v-if="scope.row.totalin==0 && (scope.row.totalpay==0 || scope.row.totalpay==0.0)" @click="handleDelete(scope.row)">作废</el-dropdown-item>
					  <el-dropdown-item v-if="scope.row.auditstatus>=2" :class="tableState=='4'?'text-orange':''"  @click="gotoChange(scope.row)">换货</el-dropdown-item>
					  <el-dropdown-item v-if="scope.row.auditstatus==2" @click="handleDetails(scope.row)">详情</el-dropdown-item>
			   	    </el-dropdown-menu>
			   	</template>
			   	</el-dropdown>
			   </el-space>
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
  <!-- 备注popover -->
  <el-popover
    placement="top-start"
    title="备注"
	:width="300"
	virtual-triggering
    trigger="click"
	:virtual-ref="remarkRef"
	:visible="remarkRows.visible"
	@show="showRemarkPopover(remarkRows)"
  >
  	<el-input
  	    v-model="remarkRows.remark2"
  	    :rows="2"
  	    type="textarea"
  	    placeholder="请输入"
  	  />
	  <div class="m-t-8">
  	<el-button type="primary" 
	 @click="updatenotice(remarkRows.id,remarkRows.remark2,remarkRows,'popover-'+remarkRows.id,proxy);remarkRows.visible=false">确定</el-button>
    <el-button type="default" plain @click="remarkRows.visible=false">关闭</el-button>
	</div>
  </el-popover>
  
</template>

<script setup> 
	import {h,ref,reactive,toRefs,getCurrentInstance,inject,} from 'vue'
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
	updateCycleDate,reSubmit} from  '@/hooks/erp/purchase/form.js';	const router = useRouter()
	const changeRecordsRef=ref();
	const remarkRef = ref();
	const AssemblyRef= ref();
	const RecordsRef = ref();
	const detailsRef = ref();
	const PaymentRef = ref();
	const ReceiptRef = ref();
	const emitter = inject("emitter");
	const emit = defineEmits(['selectrow','changepay',]);
	let globalTable=ref();
	 const   proxy  = getCurrentInstance();
	const spacer = h(ElDivider, {
		direction: 'vertical'
	})
	const state = reactive({
		tableData:{records:[],total:0},
		queryParams:{},
		checkedRowsData:[],
		remarkRows:[],
	})
	const {
		tableData,queryParams,
		checkedRowsData,
		remarkRows,
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
	function getRemarks(e,row){
		const evt = e || window.e || window.event;
		state.remarkRows= row;
		remarkRef.value = evt.currentTarget;
		state.remarkRows.visible = true;		
 	}
	function showRemarkPopover(sub){
	    sub.remark2=sub.remark;
	}
	function loadTableData(params){
		state.queryParams=params;
		purchaselistApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	function selectChange(selection) {
		state.checkedRowsData = selection
		 emit('selectrow',selection);
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
	function handleChangeForm(changenumber){
		emitter.emit("removeCache", "换货单");
		router.push({
			path:"/erp/purchase/change",
			query:{
				title:'换货单',
				path:"/erp/purchase/change",
				number:changenumber,
			},
		})
	}
	//换货 
	//换货
	function gotoChange(row){
		router.push({
			path:"/e/p/c/c",
			query:{
				title:'添加换货单',
				path:"/e/p/c/c",
				purchaseentryid:row.id,
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
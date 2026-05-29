<template>
	<div class="height-screen">
		<div class="con-header ">
			
		<el-button type="primary" v-if="planData.overseaid&&planData.overseaid!='0'&&planData.overseaid!=0"  @click="submitOverseaForm">海外仓发货</el-button>
		<div v-else>
		<el-button type="primary"  @click="submitFormV2">FBA发货</el-button>
		</div>
		<el-button   class="pull-right" @click="downloadItem">下载</el-button>
	<!-- 	<el-button @click="marketSplit" v-if="summary.marketplaceid=='EU'">分配站点</el-button> -->
		</div>
		<el-row :gutter="16" class="m-t-16">
			<el-col :span="16">
				<el-card shadow="never">
					<el-space >
						<data-sheet class="ic-cen" theme="filled" size="20" fill="#FF6700"/>
						 <h5> 发货合计
						 <span v-if="summary.transtype">-{{summary.transtype}}</span>
						 <span v-if="summary.oversea">-{{summary.oversea}}</span>
						 </h5>
					</el-space>
				<div class="con-body">
					<el-row >
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">SKU数量</span>
								<span class="data">{{summary.sumNum}}</span>
							</div>
						</el-col>	
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">发货数量</span>
								<span class="data">{{summary.sumAmount}}</span>
							</div>
						</el-col>
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">货值</span>
								<span class="data" v-if="summary.sumPrice">￥{{formatFloat(summary.sumPrice)}}</span>
								<span class="data" v-else>￥0</span>
							</div>
						</el-col>
					</el-row>
					<el-row class="m-t-16">
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">预估箱子体积</span>
								<span class="data" v-if="summary.sumBoxcm3">{{formatFloat(summary.sumBoxcm3/1000000)}} m³</span>
								<span class="data" v-else>0 m³</span>
							</div>
						</el-col>	
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">预估发货重量</span>
								<span class="data" v-if="summary.sumWeight">{{formatFloat(summary.sumWeight)}} kg</span>
								<span class="data" v-else>0 kg</span>
							</div>
						</el-col>
						<el-col :span="8">
							<div class="flex-v-bet">
								<span class="title">预估材积重量</span>
								<span class="data" v-if="summary.sumVolume">{{formatFloat(summary.sumVolume)}} kg</span>
								<span class="data" v-else>0 kg</span>
							</div>
						</el-col>
					</el-row>
				</div>
				</el-card>
			</el-col>
		<el-col :span="8">
				<el-card shadow="never">
					<el-space >
					<bookmark-one class="ic-cen"  theme="filled" size="18" fill="#e6a23c"/>
				 <h5>历史发货</h5>
				 </el-space>
				 <div class="">
				 	<ul class="font-small ul-list">
				 		<li v-for="shipment in shiplist"><span>{{shipment.createdate}} </span><span>  
						 <span style="margin-left:10px;"  class="font-extraSmall"> 发货数量：</span>
						 {{shipment.Quantity}} </span>
						   <el-tag  style="margin-left:10px;" :type="tranStatusType(shipment)" v-if="shipment.status"> {{tranStatus(shipment)}}</el-tag>
						 </li>
			 
				 	</ul>
				 </div>
				</el-card>
			</el-col>
		</el-row>
		<p class="font-extraSmall m-t-16 m-b-8">去掉勾选的产品，可分批次或部分发货</p>
		<el-table :data="planData.list" height="calc(100vh - 410px)" @selection-change="handleSummary"  border ref="tableRef">
			<el-table-column type="selection"></el-table-column>
			<el-table-column label="图片"  width="65">
				<template #default="scope">
					<el-image :src="scope.row.image"></el-image>
				</template>
			</el-table-column>
			<el-table-column label="名称/msku"   show-overflow-tooltip>
				<template #default="scope">
					<div class="name">{{scope.row.name}}</div>
					<div class="sku">{{scope.row.msku}}</div>
					<el-space wrap>
					<span v-if="scope.row.issfg=='1'"   @click.stop="handleEmtpy">
						<el-tag
						 title="组合产品"
						 @click.stop="e=>handleAssemblyShow(e,scope.row)"
						 @mouseenter="e=>handleAssemblyShow(e,scope.row)"
						 type="warning" class="pointer" v-if="scope.row.issfg=='1'"
						 size="small" 
						>组合</el-tag>
					</span>
					  <el-tag v-if="scope.row.tagNameList" effect="plain" :type="item.color"  v-for="item in scope.row.tagNameList" size="small" >
					  	{{item.name}}
					  </el-tag>
					  </el-space>
				</template>
			</el-table-column>
			<el-table-column label="备注" >
				<template #default="scope">
					<span  v-html="scope.row.htmlnotice">  </span>
				</template>
			</el-table-column>
			<el-table-column label="平台sku" prop="sku" width="180">
			</el-table-column>
			<el-table-column label="采购成本" prop="price" width="80">
				<template #default="scope">
					<span >￥{{scope.row.price}}</span>
				</template>
			</el-table-column>
			<el-table-column label="库存" prop="quantity" width="80"></el-table-column>
			<el-table-column label="产品附加费" prop="addfee" width="100">-</el-table-column>
			<el-table-column label="产品材质" prop="material" width="100"></el-table-column>
			<el-table-column label="实际发货总量"  prop="amount" width="100"></el-table-column>
		</el-table>
	</div>
</template>

<script setup>
import {Local,Help,DataSheet,BookmarkOne} from '@icon-park/vue-next';
import { ref ,nextTick ,reactive,onMounted,toRefs,watch,inject} from 'vue'
import {tranStatus,tranStatusType} from "@/hooks/erp/shipment/shipment_status.js"
import shipmentApi from "@/api/amazon/inbound/shipmentApi.js";
import { useRouter } from 'vue-router'
import {decodeText,formatFloat} from '@/utils/index.js';
import planApi from '@/api/erp/ship/planApi.js';
import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
let tableRef = ref();
const emitter = inject("emitter"); // Inject `emitter`
let props = defineProps({
  	 	                       planData:{list:[]},
		                       groupid:"",
							   transtypeOptions:[],
							   warehouseid:"",
							   warehousename:"",
  	                       });
	const { planData,groupid,warehouseid,warehousename} = toRefs(props);
	let state = reactive({     transtype:"",
		                       summary:{sumNum:0,sumAmount:0,sumPrice:0,
							            sumBoxcm3:0,sumWeight:0,sumVolume:0},
	 	                       shiplist:[]
	                       });
						    
						 
	const { shiplist,summary} = toRefs(state);
let router = useRouter() ;
function marketSplit(){
		router.push({
			path:"/e/s/p/e",
			query:{
				title:'站点分配 ',
				path:"/e/s/p/e",
			}
		})
	}
	watch(props.planData,()=>{
		handleSummary(props.planData.list);
	})
	async function handleSummary(rows){
		state.summary.sumNum=0;
		state.summary.sumAmount=0;
		state.summary.sumPrice=0;
		state.summary.sumBoxcm3=0;
		state.summary.sumWeight=0;
		state.summary.sumVolume=0;
		props.transtypeOptions.forEach(row=>{
			if(props.planData.transtype==row.id){
				state.summary.transtype=row.name;
			}
		});
		rows.forEach(row=>{
			state.summary.sumNum+=1 ;
			var amount=parseInt(row.amount);
			state.summary.sumAmount+=amount;
			if(row.price){
				state.summary.sumPrice+=parseFloat(row.price)*amount;
			}
			if(row.boxcm3){
			  state.summary.sumBoxcm3+=parseFloat(row.boxcm3)*(amount/parseInt(row.boxnum));
			}
			if(row.pkgweight){
				 state.summary.sumWeight+=parseFloat(row.pkgweight)*amount;
			}
			if(row.pkgvolume){
				 state.summary.sumVolume+=parseFloat(row.pkgvolume)*amount;
			}
			 row.htmlnotice=decodeText(row.notice);
		});
		if(props.planData.overseaid){
			await warehouseApi.detail(props.planData.overseaid).then(res=>{
				if(res.data){
					state.summary.oversea=res.data.name;
				}
			});
		}
	}
	function submitFormV2(){
		var param={};
		var rows=tableRef.value.getSelectionRows();
		planApi.batch(rows).then(res=>{
			emitter.emit("removeCache", "添加新版货件");
			router.push({
				path:'/invoice/new/addshipment',
				query:{
					batchnumber:res.data,
					warehouse:props.warehouseid,
					group:props.groupid,
					marketplaceid:props.planData.marketplaceid,
					title:'添加新版货件',
					path:'/invoice/new/addshipment',
				}
			}) 
		})
	}
	function submitForm(){
		var param={};
	    var rows=tableRef.value.getSelectionRows();
		planApi.batch(rows).then(res=>{
			emitter.emit("removeCache", "添加货件");
			router.push({
				path:'/invoice/addshipment',
				query:{
					batchnumber:res.data,
					warehouse:props.warehouseid,
					group:props.groupid,
					marketplaceid:props.planData.marketplaceid,
					title:'添加货件',
					path:'/invoice/addshipment',
				}
			}) 
		})
	}
	function submitOverseaForm(){
		var param={};
	    var rows=tableRef.value.getSelectionRows();
		planApi.batch(rows).then(res=>{
			router.push({
				path:'/e/w/os/s',
				query:{
					batchnumber:res.data,
					warehouse:props.warehouseid,
					group:props.groupid,
					marketplaceid:props.planData.marketplaceid,
					overseaid:props.planData.overseaid,
					title:'海外仓备货单',
					path:'/e/w/os/s',
				}
			}) 
		})
	}
	
	function downloadItem(){
		var wname=props.warehousename;
		var list=[];
		props.planData.list.forEach(item=>{
			var row=item;
			row.warehousename=wname;
			list.push(row);
		});
		planApi.downloadlist(list);
	}
	onMounted(async()=>{
		await shipmentApi.getShipRecordByMarket(
		             {groupid:props.groupid,marketplaceid:props.planData.marketplaceid}
				 ).then(res=>{
					 state.shiplist=res.data;
				});
		nextTick(()=>{
			 tableRef.value.toggleAllSelection();
		});
		   
		 
	})
  
</script>

<style scoped>
	.text-gray{
		color:var(--el-text-color-secondary)
	}
	.height-screen{
		height: calc(100vh - 100px);
		width: 100%;
		padding:16px ;
	}
	.flex-warp{
		display: flex;
	}
	.m-t-16{
		margin-top:16px;
	}
	.border-left{
		border-left: 1px solid var(--el-border-color-base);
	}
	.el-menu{border-right: 0;}
	.flex-v-bet{
		display: flex;
		flex-direction: column;
	}
	.flex-v-bet .title{
		font-size:12px;
		color: var(--el-text-color-secondary);
	}
	.flex-v-bet .data{
		font-size:16px;
		font-weight: 600;
	}
	.con-body{
		margin-top:16px;
		margin-bottom: 16px;
	}
	.ul-list{
		margin-left: 16px;
		margin-top:10px;
	}
	.ul-list li::marker {
	    color: #FF6700;
	    line-height: 32px;
	}
	.m-b-8{
		margin-bottom:8px;
	}
	.pull-right{
		float: right;
	}
</style>
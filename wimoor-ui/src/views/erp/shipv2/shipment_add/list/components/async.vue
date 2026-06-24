<template>
	  <el-dialog
	     v-model="dialogVisible"
	     title="计划同步"
	     width="80%"
		 top="3vh"
	  >
	<el-row style="padding-bottom:10px">
				<el-space >
				  <Group ref="groups" @change="getData" defaultValue="only"/>
				  <el-select v-model="myparams.status" @change="searchConfirm">
					  <el-option label="SHIPPED" value="SHIPPED"></el-option>
					  <el-option label="ACTIVE" value="ACTIVE"></el-option>
					 
				  </el-select>
				  <el-input  v-model="searchKeywords" @change="searchConfirm" 
				  placeholder="请输入货件编码"   >
				  <template #append>
				    <el-button >
				       <el-icon style="font-size: 16px;align-itmes:center">
				        <search @click="searchConfirm" />
				     </el-icon>
				    </el-button>
				  </template>
				  </el-input>
				</el-space>
			    <div class='rt-btn-group'> 
			        <el-space >
					<Warehouse ref="warehouses" @changeware="getWarehouse" defaultValue="only" />
					<el-button type="primary" plain @click="shipmentBatchfollow()">批量出库</el-button>
					</el-space>
				</div>
	 </el-row>
	  <el-table   :data="tableData" 
				   height="calc(100vh - 266px)"
				   border 
				   v-loading="loading"
				   style="width: 100%;margin-bottom:16px;">
		   <el-table-column   :selectable='selectEnable' type="selection" width="38" />
		   <el-table-column prop="name"  label="计划编码" width='200'>
			   <template #default="scope">
			   	    {{scope.row.name}} 
			     </template>
		   </el-table-column>
		   <el-table-column prop="number" label="货件状态" width='150'>
			   <template #default="scope">
				   {{scope.row.number}}
				   <div >{{Marketplace[scope.row.marketplaceid]}}</div>
			   </template>
			   </el-table-column>
		   <el-table-column prop="remark" label="地址"  >
			   <template #default="scope">
				   <div v-if="scope.row.source">
			        <div>
					      <span>{{scope.row.source.addressline1}}</span>
					      <span v-if="scope.row.source.addressline2">,{{scope.row.source.addressline2}}</span>
					</div>
					<div>
						  <span v-if="scope.row.source.city">{{scope.row.source.city}}</span>
						  <span v-if="scope.row.source.stateorprovincecode">,{{scope.row.source.stateorprovincecode}}</span>
						  <span v-if="scope.row.source.countrycode">,{{scope.row.source.countrycode}}</span>
						  <span v-if="scope.row.source.postalcode">,{{scope.row.source.postalcode}}</span>
					</div>
					<div>
						  <span v-if="scope.row.source.name">{{scope.row.source.name}}</span>
						  <span v-if="scope.row.source.phone">,{{scope.row.source.phone}}</span>
						  <span v-if="scope.row.source.email">,{{scope.row.source.email}}</span>
					 </div>
				  </div>
			   </template>
			</el-table-column>
			<el-table-column prop="createtime" label="创建时间" width='150'>
						   <template #default="scope">
							   {{dateFormat(scope.row.createtime)}}
						   </template>
						   </el-table-column>
	       <el-table-column prop="operate" label="操作"  width="240"   >
			   <template #header >
			     	<el-button size="small" :disabled="myparams.paginationToken?false:true" @click="showMore()">显示更多</el-button>
			   </template>
	        <template #default="scope">
	          <el-space>
				<el-button v-if="scope.row.invtype==5" v-loading="scope.row.loading" type="success" plain @click="shipmentfollow(scope.row)">同步货件</el-button>
	            <el-button v-else  type="success" disabled plain  >已同步货件</el-button>
				<el-button v-if="scope.row.invstatus==0 && scope.row.invtype==2" type="primary" @click="inventorySync(scope.row)" >本地出库</el-button>
				<el-button v-if="scope.row.invstatus==2" disabled type="success" >已出库</el-button>
	          </el-space>
	        </template>
	    </el-table-column>
	  </el-table>
   
 </el-dialog>
 
	  <AsyncDailog ref="asyncDailog"  @change="searchConfirm"></AsyncDailog>
</template>


<script setup>
	import { ref,reactive,onMounted,toRefs } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router'
	import {dateFormat} from '@/utils/index.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {Refresh,CircleClose,WarningFilled,Search} from '@element-plus/icons-vue'
	import Group from '@/components/header/group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import shipmentApi from '@/api/amazon/inbound/shipmentApi.js';
	import AsyncDailog from './asyncdailog.vue'
	import Warehouse from '@/components/header/warehouse.vue';
	import Marketplace from "@/model/amazon/ship/marketplace.json";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	let router = useRouter()
	let state =reactive({
		tableData:[],
		orderStatus:"",
		searchKeywords:"",
		parmashead:{},
		dialogVisible:false,
		optShipmentid:"",
		loading:false,
		myparams:{pageSize:30,paginationToken:null,
		status:"SHIPPED"
		},
		dataList:[],
		selected:null,
		warehouseid:null
	})
	let{
		tableData,orderStatus,searchKeywords,parmashead,
		dialogVisible,optShipmentid,myparams,dataList,loading,
		selected,warehouseid
	}=toRefs(state);
 
	const asyncDailog=ref(AsyncDailog);
	const globalTable=ref(GlobalTable);
	const groups=ref(Group)
	const datepickers=ref(Datepicker)
	 
	function showMore(){
		if(state.myparams.paginationToken){
			state.loading=true;
			shipmentplanApi.getPlanListSync(state.myparams).then(res=>{
				state.loading=false;
				res.data.list.forEach(item=>{
					state.tableData.push(item);
				})
				if(res.data&&res.data.pagination){
					state.myparams.paginationToken=res.data.pagination.nextToken;
				}else{
					state.myparams.paginationToken=null;
				}
			});
		}
		
	}
	function loadtableData(){
		state.myparams.search=state.searchKeywords;
		state.tableData=[];
		state.myparams.paginationToken=null;
		state.loading=true;
		shipmentplanApi.getPlanListSync(state.myparams).then(res=>{
			if(res.data && res.data.list){
				state.tableData=res.data.list;
			}
			state.loading=false;
			if(res.data&&res.data.paginationToken){
			  state.myparams.paginationToken=res.data.paginationToken;
			}
		});
	}
	function getData(param){
		state.myparams.groupid=param.groupid;
		state.myparams.marketplaceid=param.marketplaceid;
		loadtableData();
	}
	function searchConfirm(){
		loadtableData();
	}
	function shipmentfollow(row){
		row.groupid=state.myparams.groupid;
		row.warehouseid=state.warehouseid;
		// shipmentplanApi.syncPlan(row).then(res=>{
		// 	console.log(res);
		// })
		asyncDailog.value.showDailog(row);
	}
	function sltChange(select){
		state.selected=select;
	}
	function getWarehouse(id){
		state.warehouseid=id;
	}
	function selectEnable(row, rowIndex){
		if(row.syncinv!=1){
			return false;
		}else{
			return true;
		}
	}
	function shipmentBatchfollow(){
		if(state.selected&&state.selected.length>0){
			state.selected.forEach(async function(item){
				 let params={};
				 params.needsubinv="true";
				 params.shipmentid=item.shipmentid;
				 params.groupid=item.groupid;
				 params.marketplaceid=item.marketplaceid;
				 params.warehouseid=warehouseid;
				 item.syncinv=4;
				 await shipmenthandlingApi.saveShipmentItemAndPlanBath(params).then(res=>{
					 item.syncinv=2;
				 }).catch(e=>{
					 item.syncinv=1;
				 });
				
			});
		}else{
			ElMessage.error('请选择记录');	
		}
	 
	}
	function inventorySync(row){
		ElMessageBox.confirm('确认出库操作?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
		}).then(() => {
			  getInvParam(row.id);
			}).catch(() => ElMessage.info('已取消'));
		
	}
	function getInvParam(id){
		shipmentplanApi.getInvParam({"id":id}).then((res)=>{
			    state.invparam=res.data;
				outShipInventory(state.invparam,id);
		});
	}
	function outShipInventory(params,id){
		shipmentplanApi.subFulfillable(params).then((res)=>{
				setInvStatus(id);
		});
	}
	function setInvStatus(id){
		shipmentplanApi.setInvStatus({"id":id,"status":2}).then((res)=>{
			 ElMessage.success('操作成功');
			 state.myparams.invstatus=2;
		});
		}
	function show(){
		state.dialogVisible=true;
	}
	 defineExpose({show});
</script>

<style scoped>
	.sku{
		cursor:pointer;
	}
</style>
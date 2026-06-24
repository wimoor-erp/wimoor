<template>
	  <div class="main-sty">
		<div class="con-header" >
			<el-row>
				<el-space >
				  <Group ref="groups" @change="getData" defaultValue="only"/>
				  <Datepicker ref="datepickers" @changedate="changedate" />
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
			</el-row>
			<el-row>
				<el-space >
				   <Warehouse ref="warehouses" @changeware="getWarehouse" defaultValue="only" />
			       <el-button type="primary" plain @click="shipmentBatchfollow()">批量出库</el-button>
				</el-space>
			</el-row>
	<el-row>
	  <GlobalTable ref="globalTable" :tableData="tableData" 
	  @selectionChange="sltChange"  
	  @loadTable="loadtableData" 
	  height="calc(100vh - 266px)"
	  border style="width: 100%;margin-bottom:16px;">
	    <template #field>
		   <el-table-column   :selectable='selectEnable' type="selection" width="38" />
		   
		   <el-table-column prop="shipmentid"  label="货件编码" width='180'>
			   <template #default="scope">
			        <span >{{scope.row.shipmentid}}</span>
			   	     <p class="font-extraSmall">{{scope.row.name}}</p>
			     </template>
		   </el-table-column>
		   <el-table-column prop="groupname" label="店铺-站点" width='150'>
			   <template #default="scope">
			        <span >{{scope.row.groupname}}-{{scope.row.country}}</span>
			     </template>
		   </el-table-column>
		   <el-table-column prop="center" label="配送中心" width='150'>	
		     <template #default="scope">
		        <span >{{scope.row.centerId}}-{{scope.row.countryCode}}</span>
		     </template>
		   </el-table-column>
		   <el-table-column prop="shipmentstatus" label="货件状态" width='150'>	</el-table-column>
		   <el-table-column prop="remark" label="地址"  >	</el-table-column>
		   <el-table-column prop="areCasesRequired" label="发货方式" width='150' >
			   <template #default="scope">
			      <span  v-if="areCasesRequired">原装</span>
				  <span v-else>混装</span>
			   </template>
		 </el-table-column> 
	       <el-table-column prop="operate" label="操作"  width="100"   >
	        <template #default="scope">
	          <el-space>
				<el-button v-if="scope.row.syncinv==-1" type="success" plain @click="shipmentfollow(scope.row)">同步货件</el-button>
	            <el-button v-else-if="scope.row.syncinv==1" type="primary" plain @click="shipmentfollow(scope.row)">同步库存</el-button>
				<span v-else-if="scope.row.syncinv==4"   >同步中...</span>
				<span v-else  >已同步库存</span>
	          </el-space>
	        </template>
	    </el-table-column>
		</template>
	  </GlobalTable>
    </el-row>
	  </div>
	  </div>
	  <AsyncDailog ref="asyncDailog"  @change="searchConfirm"></AsyncDailog>
</template>


<script>
	import { ref,reactive,onMounted } from 'vue'
	import {ElMessage } from 'element-plus'
	import { useRoute,useRouter } from 'vue-router'
	import {dateFormat} from '@/utils/index.js';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import {Refresh,CircleClose,WarningFilled,Search} from '@element-plus/icons-vue'
	import Group from '@/components/header/group.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import shipmentApi from '@/api/amazon/inbound/shipmentApi.js';
	import AsyncDailog from './asyncdailog.vue'
	import Warehouse from '@/components/header/warehouse.vue';
	import shipmenthandlingApi from '@/api/erp/ship/shipmenthandlingApi.js';
	export default{
		name:'Table',
		components:{
			GlobalTable,Refresh,ElMessage,
			CircleClose,WarningFilled,Group,Datepicker,AsyncDailog,
			Warehouse,Search
		},
		setup(props){
			let router = useRouter()
			let tableData=reactive({records:[],total:0})
			let orderStatus =ref("")
			let searchKeywords=ref("");
			let parmashead = ref({})
			let dialogVisible=ref(false);
			let asyncDailog=ref(AsyncDailog);
			let globalTable=ref(GlobalTable);
			let groups=ref(Group)
			let datepickers=ref(Datepicker)
			let optShipmentid="";
			let myparams={};
			let dataList=[];
			let selected=null;
			let warehouseid=null;
			function loadtableData(params){
				if(params.loadType=="loadTable"){
					myparams.search=searchKeywords.value;
					shipmentApi.getSyncList(myparams).then(res=>{
						dataList=res.data;
						tableData.records=dataList.slice(0,params.pagesize);
						tableData.total=res.data.length;
					});
				}else{
					let start=(params.currentpage-1)*params.pagesize;
					let end=(params.currentpage)*params.pagesize;
					if(end>=tableData.total){
						end=tableData.total;
					}
					tableData.records=dataList.slice(start,end);
				}
			}
			function getData(param){
				myparams.groupid=param.groupid;
				myparams.marketplaceid=param.marketplaceid;
				globalTable.value.loadTable();
			}
			function searchConfirm(){
				globalTable.value.loadTable();
			}
			function changedate(param){
				myparams.fromDate=param.start;
				myparams.endDate=param.end;
				if(myparams.groupid){
				   globalTable.value.loadTable();
				}
			}
			function shipmentfollow(row){
				let params={};
				params.groupid=myparams.groupid;
				params.marketplaceid=row.marketplaceid;
				params.shipmentid=row.shipmentid;
				asyncDailog.value.showDailog(params);
			}
			function sltChange(select){
				selected=select;
			}
			function getWarehouse(id){
				warehouseid=id;
			}
			function selectEnable(row, rowIndex){
				if(row.syncinv!=1){
					return false;
				}else{
					return true;
				}
			}
		    function shipmentBatchfollow(){
				if(selected&&selected.length>0){
					selected.forEach(async function(item){
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
			return{ loadtableData,getData,changedate,searchConfirm,shipmentfollow,
			        shipmentBatchfollow,sltChange,getWarehouse,selectEnable,
			        //function
			        groups,datepickers,globalTable,asyncDailog,  
					//ref
					tableData,searchKeywords,			
					//value
								
			 } 
		}
	}
</script>

<style scoped>
	.sku{
		cursor:pointer;
	}
</style>
<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
		<el-space>
			<Warehouse @changeware="getWarehouse" />
			<Datepicker ref="datepickers" @changedate="changedate" />
			<el-select
			      v-model="queryParam.formtype"
			      multiple
			      placeholder="请选择单据类型"
			      style="width: 240px"
				  @change="changeFormType"
			    >
			      <el-option
			        v-for="item in formtypelist"
			        :key="item.id"
			        :label="item.name"
			        :value="item.id"
			      />
			    </el-select>
			<OwnerAll @owner="changeOwner" notAll="isAll"  alltitle="全部操作人"></OwnerAll>
			<el-input  v-model="searchKeywords" placeholder="请输入" clearable @input="handleQuery" class="input-with-select" >
			   <template #prepend>
			     <el-select v-model="searchtype" @change='handleQuery' placeholder="SKU" style="width:90px">
			       <el-option label="SKU" value="sku"></el-option>
			       <el-option label="单据编码" value="number"></el-option>
			     </el-select>
			   </template>
			   <template #append>
			     <el-button @click="handleQuery">
			        <el-icon style="font-size: 16px;align-itmes:center">
			         <search />
			      </el-icon>
			     </el-button>
			   </template>
			 </el-input>
		 <el-button @click.stop="download">导出</el-button>
		 <el-button @click.stop="downloadConsumable">导出耗材</el-button>
		 </el-space>
		 <div class='rt-btn-group'>
		 	  <el-checkbox v-model="queryParam.notlike" @change="handleQuery">精确查询</el-checkbox>	
		 </div>
		 </el-row>
		 </div>
		  <el-row :gutter="12">
			  <el-space>
				
		 	 </el-space>
		  </el-row>
		 <el-row :gutter="12" v-if="searchKeywords!='' && searchtype=='sku'" style="margin-bottom: 15px;">
			  <el-col :span="24">
				  <div style="padding-bottom: 5px;">
					  <span style="font-weight: bolder;">
						<span>{{searchKeywords}}</span>  数据汇总
					  </span>
				      <span class="font-extraSmall" style="margin-left: 10px;">{{queryParam.fromDate}}至{{queryParam.toDate}}</span>
				  </div>
			  </el-col>
		     <el-col :span="12">
		       <el-card shadow="always">
				    <el-row :gutter="12">
						 <el-col :span="12" style="text-align: center;"> 
						 <p style="font-weight: bolder;">{{startmap.fulfillable}}</p>
						  <p>期初库存</p>
						 </el-col>
						 <el-col :span="12" style="text-align: center;"> 
						 <p style="font-weight: bolder;">{{endmap.fulfillable}}</p>
						  <p>期末库存</p>
						 </el-col>
					</el-row>
			  </el-card>
		     </el-col>
		     <el-col :span="12">
		        <el-card shadow="always"> 
					<el-row :gutter="12">
						 <el-col :span="8" style="text-align: center;"> 
						 <p style="font-weight: bolder;">{{inmap.fulfillable}}</p>
						  <p>期间入库</p>
						 </el-col>
						 <el-col :span="8" style="text-align: center;">
						 <p style="font-weight: bolder;">{{outmap.fulfillable}}</p>
						  <p>期间出库</p>
						 </el-col>
						 <el-col :span="8" style="text-align: center;"> 
						 <p style="font-weight: bolder;">{{exepNum}}</p>
						  <p>差异数量</p>
						 </el-col>
					</el-row>
				</el-card>
		     </el-col>
		    <!-- <el-col :span="8">
		        <el-card shadow="always"> 
					<el-row :gutter="12">
						 <el-col :span="12"> 
						 <p>2000</p>
						  <p>已上架数量</p>
						 </el-col>
						 <el-col :span="12"> 
						 <p>0</p>
						  <p>与货架差异数量</p>
						 </el-col>
					</el-row>
				 </el-card>
		     </el-col> -->
		 </el-row>
		 <div class="con-body" >
			 <Table ref="tableRef" />
		 </div>
	</div>
</template>
<script>
    export default{ name:"出入库明细" };
</script>
<script setup>
    import Warehouse from '@/components/header/warehouse.vue';
	import Datepicker from '@/components/header/datepicker.vue';
	import {Search,ArrowDown,UploadFilled} from '@element-plus/icons-vue';
	import {Plus,Drag} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,toRefs} from 'vue';
	import OwnerAll from '@/components/header/ownerAll.vue';
	import Table from"./components/table.vue";
	import {useRouter } from 'vue-router';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import inventoryRecordApi from '@/api/erp/inventory/inventoryRecordApi.js';
	const router = useRouter()
	let tableRef=ref();
    const state = reactive({
		queryParam:{"formtype":null},
		searchKeywords:"",
		isload:true,
		searchtype:'sku',
		formtypelist:[],
		startmap:{
			fulfillable:0
		},
		endmap:{
			fulfillable:0
		},
		inmap:{
			fulfillable:0
		},
		outmap:{
			fulfillable:0
		},
		exepNum:0,
    })
    const {
    	queryParam,searchKeywords,isload,searchtype,formtypelist,startmap,endmap,inmap,outmap,exepNum
    } = toRefs(state)
	
		function handleQuery(){
			state.queryParam.searchtype=state.searchtype;
			state.queryParam.search=state.searchKeywords;
			state.queryParam.sku=state.queryParam.search;
			if(!state.queryParam.warehouseid){
				state.queryParam.warehouseid="";
			}
			tableRef.value.load(state.queryParam);
			if(state.searchKeywords!="" && state.searchtype=="sku"){
				//查询汇总数据
				inventoryRecordApi.findInvDayList(state.queryParam).then((res)=>{
					   if(res.data && res.data.list &&res.data.list.length>0){
							state.startmap=res.data.list[0];
							state.endmap=res.data.list[1];
						}else{
							state.startmap={fulfillable:0};
							state.endmap={fulfillable:0};
						}
						
						if(res.data.record){
							state.inmap=res.data.record.inmap;
							state.outmap=res.data.record.outmap;
							state.exepNum=(state.endmap.fulfillable)-(state.inmap.fulfillable+state.outmap.fulfillable+state.startmap.fulfillable);
						}else{
							state.inmap={fulfillable:0};
							state.outmap={fulfillable:0};
							state.exepNum=0;
						}
					});
			}
			if(state.isload==true){
				state.isload=false;
			}
		}
		function getWarehouse(wid){
			state.queryParam.warehouseid=wid;
			if(state.isload==true){
				loadFormType();
			}
			handleQuery();
		}
		//日期改变
		function changedate(dates){
			state.queryParam.fromDate=dates.start;
			state.queryParam.toDate=dates.end;
			if(state.isload==false){
				handleQuery();
			}
		}
		function changeOwner(id){
			state.queryParam.operator=id;
			if(state.isload==false){
				handleQuery();
			}
		}
		function loadFormType(){
			inventoryRecordApi.getFormTypeList().then((res)=>{
				res.data.push({"id":"","name":"全部单据类型"});
				state.formtypelist=res.data;
				state.queryParam.formtype=null;
			})
		}
		function changeFormType(){
			if(state.isload==false){
				handleQuery();
			}
		}
		 
		function download(){
			//下载表格数据
			inventoryRecordApi.downloadRecordsExcel(state.queryParam);
		}	
		
		function downloadConsumable(){
			//下载表格数据
			inventoryRecordApi.downloadConsumableExcel(state.queryParam);
		}
	    
</script>

<style scoped="scoped">
 .font-large{
	 font-size:32px;
	 color:#999;
 }
</style>

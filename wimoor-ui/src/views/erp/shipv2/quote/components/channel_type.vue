<template>
	<el-card  >
		<template #header>
		      <div class="card-header">
				  <div class="flex-between"  style="padding-bottom:10px;">
					  <el-space>
					      <div>渠道类型</div>
						  <el-input size="small" clearable v-model="name" placeholder="请输入名称">
							<template #append>
							    <el-button :icon="Search" @click="handleQuery"/>
							</template>
						  </el-input>
					  </el-space>
					  <el-button @click="handleAdd">新增</el-button>
				  </div>
				  
		      <GlobalTable
		           ref="globalTableRef"
		      	   @loadTable="loadTableData" 
		      	   :tableData="tableData"  
		      	   :stripe="true"  
				   :inDialog="true"
		      	    height="calc(100vh - 243px)"
		      	   :show-header="false"
		      	  >
		      	  <template #field>
		      			 <el-table-column label=""    >
		      			 	<template #default="scope">
		      			 		{{scope.row.name}}
		      			 	</template>
		      			 </el-table-column>
						 <el-table-column label=""  width="120"   >
						 	<template #default="scope">
								<el-space>
						 		<el-button @click="delSupplier(scope.row)" link type="danger">删除</el-button>
								<el-button @click="editSupplier(scope.row)" link type="primary">更新</el-button>
								</el-space>
						 	</template>
						 </el-table-column>
		      	  </template>
		      </GlobalTable>
		      </div>
		    </template>
	</el-card>	
	<el-dialog
	    v-model="visible"
	    title="渠道类型"
	    width="500"
	    :before-close="handleClose"
	  >
	    <el-form :model="form" label-width="auto" style="max-width: 600px">
	       <el-form-item label="名称">
	         <el-input v-model="form.name" />
	       </el-form-item>
	     </el-form>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="visible = false">取消</el-button>
	        <el-button type="primary" @click="handleSubmit"> 确认 </el-button>
	      </div>
	    </template>
	  </el-dialog>
</template>

<script setup>
import {Search,ArrowDown,Link} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import shipmentplanApi from '@/api/erp/shipv2/shipmentplanApi.js';
	import shipmentPlacementApi from '@/api/erp/shipv2/shipmentPlacementApi.js';
	import CopyText from"@/utils/copy_text.js";
	import orderApi from '@/api/quote/orderApi.js';
	import supplierApi from '@/api/quote/supplierApi.js';
	import transchannelApi from '@/api/quote/transchannelApi.js';
	import Datepicker from '@/components/header/datepicker.vue';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	import {ElMessage,ElMessageBox } from 'element-plus';
	const emit = defineEmits(['change']);
	const globalTableRef=ref();
	const state = reactive({
		edittoken:"",
		editname:"",
		supplier:{},
		tableData:{total:0,records:[]},
		token:"",
		form:{},
		name:"",
		tokenname:"",
		title:'新增',
		isowner:false,
		visible:false,
		suppliers:[],
		buyer:{'edit':true},
	})
	const{
		token,buyer,edittoken,editname,supplier,tableData,title,isowner,tokenname,visible,form,suppliers,name
		}=toRefs(state);
	 
	function loadTableData(param){
		if(state.token){
			transchannelApi.list(state.token,param).then(res=>{
				state.tableData.records=res.data.records;
				state.tableData.total=res.data.total;
			})
		}
		
	}
	function editSupplier(row){
		state.form=row;
		state.visible=true;
	}
	function handleQuery(){
		globalTableRef.value.loadTable({"name":state.name});
	}
	 
    function handleAdd(){
		state.visible=true;
	}
	function handleSubmit(){
		state.form.opttime=null;
		if(state.form.id){
			transchannelApi.update(state.token,state.form).then(res=>{
					 ElMessage.success("操作成功!");
					 state.visible=false;
					 handleQuery();
			})
		}else{
			transchannelApi.save(state.token,state.form).then(res=>{
					 ElMessage.success("操作成功!");
					 state.visible=false;
					 handleQuery();
			})
		}
	}
	function loadSupplier(){
			 var datas={};
			 datas.token=state.token;
			 supplierApi.listsupplier(datas).then((res)=>{
				 state.suppliers=res.data;
			 });
	}
	function delSupplier(row){
		ElMessageBox.confirm(
			     '您确定要删除此记录?',
			    '删除渠道类型',
			    {
			      confirmButtonText: '确定',
			      cancelButtonText: '取消',
			      type: 'warning',
			    }
			  ).then( () => {
				  row.disable=true;
				  row.opttime=null;
				  transchannelApi.update(state.token,row).then(res=>{
				  		 ElMessage.success("操作成功!");
				  		 handleQuery();
				  })
			  })
		
	}
	 
	 
	 
	  function show(data){
			state.token=data.buyertoken;
			state.tokenname=data.name;
			state.edittoken=data.buyertoken;
			state.editname=data.name;
			state.isowner=data.isowner;
			handleQuery();
	}
 defineExpose({show});
</script>

<style>
</style>
<!-- 添加供应商时，共用的弹窗 -->

<template>
	<div >
		<el-dialog
		    v-model="suplierVisible"
		    title="选择供应商"
			top="2vh"
		    :before-close="handleClose"
		  >
		  <div class="con-header">
			  <el-row>
			<el-input   v-model="search"  @input="loadData" placeholder="搜索供应商名称或编码">
				<template #suffix >
					<el-icon :size="16" @click="loadData"><Search/></el-icon>
				</template>
			</el-input>
			</el-row>
			</div>
		   <GlobalTable ref="globalTable" :tableData="tableData" 
		   :inDialog="true" 
		   @row-click="rowClick" @loadTable="loadTableData" 
		   @currentChange="selectRows" 	 :highlightCurrentRow="true" :stripe="false"  >
			    <template #field>
		   	<el-table-column prop="number" label="编码" width="140" show-overflow-tooltip/> 
		   	<el-table-column prop="product" label="名称"   show-overflow-tooltip>
		   		<template #default="scope">
		   			<div class='name'>{{scope.row.name}}
					<el-tag v-if="scope.row.isdefault">默认供应商</el-tag>
					<el-tag v-else-if="scope.row.isbackup" type="success">备选供应商</el-tag>
					</div>
		   		</template>
		   	</el-table-column>
		   	<el-table-column prop="operator2" label="操作人"   width="120"  />
			</template>
		   </GlobalTable>
		    <template #footer>
		      <span class="dialog-footer">
		        <el-button @click="suplierVisible = false">取消</el-button>
		        <el-button type="primary" @click="submit"
		          >提交</el-button >
		      </span>
		    </template>
		  </el-dialog>
	</div>
</template>

<script setup>
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,watch,onMounted,toRefs } from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
    import customerApi from '@/api/erp/material/customerApi.js';
	import {ElMessage } from 'element-plus';
	const globalTable=ref();	
	const emit = defineEmits(['getdata']);
	 
	let state = reactive({
		search:'',
		materialid:"",
		tableData:{records:[],total:0},
		suplierVisible:false,
		selectRow:null,
	})    
	 let {search,tableData,suplierVisible}=toRefs(state)
     function rowClick(row){
		  globalTable.value.toggleRowSelection(row,true);
	 }

	function show(materialid){
		if(materialid){
			state.materialid=materialid;
		}else{
			state.materialid="";
		}
		
		state.suplierVisible=true;
		setTimeout(()=>{
			loadData();
		},100);
	}
	
	function loadData(){
		globalTable.value.loadTable({"search":state.search,"materialid":state.materialid});
	}
	function submit(){
		if(!state.selectRow){
			ElMessage.error('请提交1个供应商！');
		}else{
			state.suplierVisible=false;
			emit('getdata',state.selectRow);
		}
	}
	function selectRows(rows){
		 state.selectRow=rows;
	}
	function loadTableData(data){
		 //根据 params去查
		 customerApi.list(data).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total
		 })
		 
	}
	//方法
	defineExpose({
	  show
	});
	onMounted(()=>{
		
	});
</script>

<style>
	.el-input__suffix{
		display: flex;
		align-items: center;
		font-size: 16px;
		padding-right:8px;
	}
</style>

<template>
	<GlobalTable ref="globalTable" :tableData="tableData"  height="calc(100vh - 198px)" @selectionChange='handleSelect' :defaultSort="{ prop: 'opttime', order: 'descending' }"  @loadTable="loadTableData" :stripe="false"  style="width: 100%;margin-bottom:16px;">
		<template #field>
		<el-table-column type="selection"></el-table-column>
		<el-table-column label="供应商名称" prop="name"   width="230" sortable="custom" ></el-table-column>
		<el-table-column label="供应商编码" prop="number" width="130"  sortable="custom" ></el-table-column>
		<el-table-column label="联系人" prop="contacts"   sortable="custom" >
			<template #default="scope">
				<div>{{scope.row.contacts}}</div>
			    <div><span class="font-extraSmall">联系电话:</span>{{scope.row.phone_num}}</div>
			</template>
		</el-table-column>
		<el-table-column label="总采购金额" prop="totalpay"  width="120">
			<template #default="scope">
				<div>￥<span v-if="scope.row.totalpay">{{scope.row.totalpay}}</span><span v-else>0</span></div>
			    <div><span class="font-extraSmall">入库数量:</span><span v-if="scope.row.totalin">{{scope.row.totalin}}</span>
				<span v-else>0</span></div>
			</template>
		</el-table-column>
		<el-table-column label="联系地址" prop="address"></el-table-column>
		<el-table-column label="其它信息" show-overflow-tooltip   width="100" prop="contact_info"></el-table-column>
		<el-table-column label="创建人" width="100" prop="operator2"></el-table-column>
		<el-table-column label="修改时间" prop="opttime"  width="100" sortable="custom" >
			<template #default="scope">
				<span>{{dateTimesFormat(scope.row.opttime)}}</span>
			</template>
		</el-table-column>
		<el-table-column label="操作"  width="100"  fixed="right" >
			<template #default='scope'>
			<el-space>
				<el-button @click="handleDetails(scope.row)" type="primary" link>详情</el-button>
				<el-dropdown :hide-on-click="false">
				  <span class="el-dropdown-link">
				    <more-one class="ic-cen" theme="outline" size="16" fill="#333" :strokeWidth="3"/>
				  </span>
				   <template #dropdown>
				    <el-dropdown-menu>
				      <el-dropdown-item @click="rowRemove(scope.row)">删除</el-dropdown-item>
				    </el-dropdown-menu>
				</template>
				</el-dropdown>
			  </el-space>	
			</template>
		</el-table-column>
		</template>
	</GlobalTable>
	<el-dialog
	   v-model="dialogVisible"
	   title="查看/编辑供应商"
	   width="600px"
	 >
	 <el-form
	   ref="dataFormRef"
	   :model="formData"
	   :rules="rules"
	   label-width="100px"
	 >
	   
	   <el-form-item label="供应商名称" prop="name">
	     <el-input
	       v-model="formData.name"
	       placeholder="请输入供应商名称"
	     />
	   </el-form-item>
	   <el-form-item label="供应商编码" prop="number">
	   		   <el-input
	   		     v-model="formData.number"
	   		     placeholder="输入供应商编码,可不填,由系统自动生成"
	   		   />
	   </el-form-item>
	   <el-form-item label="联系人" prop="contacts">
	     <el-input v-model="formData.contacts" placeholder="请输入联系人" />
	   </el-form-item>
	   <el-form-item label="联系电话" prop="phone_num">
	 		  <el-input v-model="formData.phone_num" placeholder="请输入联系电话" />
	 	</el-form-item>
	   <el-form-item label="地址" prop="address">
	     <el-input v-model="formData.address"  placeholder="请输入地址" type="textarea"></el-input>
	   </el-form-item>
	   <el-form-item label="其它信息" prop="contact_info">
	     <el-input v-model="formData.contact_info"  :rows="5" placeholder="微信，QQ或者网址，工商信息，银行信息..." type="textarea"></el-input>
	   </el-form-item>
	 </el-form>
	   <template #footer>
	     <span class="dialog-footer">
	       <el-button type="primary" @click="submitForm">提 交</el-button>
		   <el-button @click="cancel">取 消</el-button>
	     </span>
	   </template>
	 </el-dialog>
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {MoreOne} from '@icon-park/vue-next';
	import { ElMessage, ElMessageBox,ElForm } from 'element-plus'
	import customerApi from '@/api/erp/material/customerApi.js';
	import {dateFormat,formatFloat,dateTimesFormat} from '@/utils/index.js';
	let detailsRef =ref()
	let globalTable=ref()
	const dataFormRef = ref(ElForm);
	let state = reactive({
		tableData: {records:[],total:0},
		selectRows:[],
		dialogVisible:false,
		formData: {
			name: '',
			number: 1,
		}, 
		rules: {
			name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
			address: [{ required: true, message: '请输入供应商地址', trigger: 'blur' }],
			/*contacts: [{ required: true, message: '请输入供应商联系人', trigger: 'blur' }], */
		},
	})
	let {
		tableData,formData,rules,selectRows,dialogVisible
		 } =toRefs(state);
	defineExpose({
		state,
		loadData,
	})
	let emits = defineEmits(['handleEdit'])
	function handleSelect(rows){
		state.selectRows =rows;
	}
	function handleDetails(row){
		state.dialogVisible = true;
		state.formData=row;
	}
	function rowEdit(row){
		emits('handleEdit',row)
	}
	function rowRemove(rows){
		 ElMessageBox.confirm(
		    '确定要删除该条供应商信息吗',
		    '删除供应商',
		    {
		      confirmButtonText: '确认',
		      cancelButtonText: '取消',
		      type: 'warning',
		    }
		  )
		    .then(() => {
			customerApi.deletecust({"ids":rows.id}).then((res)=>{
				 if(res.data=="OK"){
					 ElMessage.success('删除成功');
					 loadData('');
				 }else{
					 ElMessage.error('删除失败');
				 }
			});	
			 
		      
		    })
		    .catch(() => {
		      ElMessage.info('取消删除');
		    })
		
		
	}
	function loadData(searchs){
		var data={};
		var search="";
		if(searchs=="" || searchs==undefined || searchs==null){
			search="";
		}else{
			search=searchs;
		}
		data.search=search;
		globalTable.value.loadTable(data);
	}
	function loadTableData(data){
		customerApi.list(data).then((res)=>{
			state.tableData.records = res.data.records
			state.tableData.total =res.data.total
		});
	}
	
	function submitForm() {
	  dataFormRef.value.validate((isValid) => {
		if (isValid) {
		  if (state.formData.id) {
			 customerApi.saveData(state.formData).then((res)=>{
			  ElMessage.success('修改成功');
			  loadData('');
			});
		  } else {
			customerApi.saveData(state.formData).then((res)=>{
			  ElMessage.success('新增成功');
			  loadData('');
			});
		  }
		}
	  });
	}
	function cancel() {
	  state.dialogVisible = false;
	}
</script>

<style>
</style>

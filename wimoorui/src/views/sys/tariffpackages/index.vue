<template>
     <div class="main-sty">
		 <div class="con-header" style="padding-bottom:20px">
			  <div class="flex-between">
				   <el-button @click="handleAdd">添加新套餐</el-button>
			  </div>
		 </div>
		 <div class="con-body">
	       <el-row>
			 <GlobalTable ref="globalTable" :nopage="true" :tableData="tableData"     @loadTable="loadTableData" :stripe="true"  style="width: 100%;margin-bottom:16px;">
			   <template #field>
			 	 <el-table-column prop="name" label="名称"  sortable='custom'  width="110">
					 <template #default="scope">
					 						   <div>{{scope.row.name}}</div>
					 </template>
			 	  </el-table-column>
			  <el-table-column label="数量上限" >
				  <el-table-column prop="maxShopCount" label="绑定店铺"  sortable='custom'  width="110">
				     </el-table-column>
				     <el-table-column prop="maxProductCount" label="商品列表"  sortable='custom'  width="110">
				  	   
				  </el-table-column>
				  <el-table-column prop="maxOrderCount" label="处理订单"  sortable='custom'  width="110">
				   </el-table-column>
				   <el-table-column prop="maxMember" label="子用户"  sortable='custom'  width="110">
				    </el-table-column>
				    <el-table-column prop="maxProfitPlanCount" label="利润计算方案数量"  sortable='custom'  width="150">
				     </el-table-column>
				  <el-table-column prop="maxMarketCount" label="站点数量"  sortable='custom'  width="110">
				   </el-table-column>
				  <el-table-column prop="orderMemoryCount" label="订单存储月份"  sortable='custom'  width="130">
				   </el-table-column>
				    <el-table-column prop="dayOpenAdvCount" label="广告组数量"  sortable='custom'  width="120">
				     </el-table-column>
				     <el-table-column prop="controlProductCount" label="跟卖监控数量"  sortable='custom'  width="140">
				      </el-table-column>
				  	<el-table-column prop="anysisProductCount" label="商品分析数量"  sortable='custom'  width="140">
				  	 </el-table-column>
			  </el-table-column>
				
					 <el-table-column prop="yearprice" label="年费"  sortable='custom'  width="140">
					  </el-table-column>
					 <el-table-column prop="monthprice" label="月费"  sortable='custom'  width="140">
					  </el-table-column>
			 	 <el-table-column prop="turndays" label="操作"    width="160"  >
			 		 <template #default="scope">
						  <el-button   @click="handleEdit(scope.row)"   link type="primary">编辑</el-button>
			 			  <el-button   @click="handleDelete(scope.row)"   link type="danger">删除</el-button>
			 		 </template>
			 	 </el-table-column>
			 	 </template>
			  </GlobalTable>
  </el-row>
  </div>
  </div>
   <el-dialog v-model="dialogFormVisible" title="任务信息" width="60%">
      <el-form :model="editForm"  label-width="auto">
        <el-form-item label="套餐名称" >
          <el-input v-model="editForm.name"  />
        </el-form-item>
		<el-form-item label="角色" >
		   <el-select v-model="editForm.roleId">
			   <el-option   :key="0" value="0" label="定制角色"></el-option>
			   <el-option v-for="item in roles" :key="item.id" :value="item.id" :label="item.name"></el-option>
		   </el-select>
		</el-form-item>
		<el-form-item label="绑定店铺数量上限" >
		  <el-input v-model="editForm.maxShopCount"  />
		</el-form-item>
		<el-form-item label="商品列表数量上限" >
		  <el-input v-model="editForm.maxProductCount"  />
		</el-form-item>
		<el-form-item label="处理订单数量上限" >
		  <el-input v-model="editForm.maxOrderCount"  />
		</el-form-item>
		<el-form-item label="子用户数量上限"  >
		  <el-input v-model="editForm.maxMember"  />
		</el-form-item>
		<el-form-item label="利润计算方案数量数量上限" >
		  <el-input v-model="editForm.maxProfitPlanCount"  />
		</el-form-item>
		<el-form-item label="站点数量上限" >
		  <el-input v-model="editForm.maxMarketCount"  />
		</el-form-item>
		<el-form-item label="订单存储月份(月)" >
		  <el-input v-model="editForm.orderMemoryCount"  />
		</el-form-item>
		<el-form-item label="广告组数量" >
		  <el-input v-model="editForm.dayOpenAdvCount"  />
		</el-form-item>
		<el-form-item label="跟卖监控数量" >
		  <el-input v-model="editForm.controlProductCount"  />
		</el-form-item>
		<el-form-item label="商品分析数量" >
		  <el-input v-model="editForm.anysisProductCount"  />
		</el-form-item>
		<el-form-item label="年费" >
		  <el-input v-model="editForm.yearprice"  />
		</el-form-item>
		<el-form-item label="月费" >
		  <el-input v-model="editForm.monthprice"  />
		</el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script setup>
	import {h, ref ,watch,reactive,onMounted,toRefs} from 'vue'
	import {Search,ArrowDown,} from '@element-plus/icons-vue';
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
    import { ElMessage ,ElMessageBox} from 'element-plus';
	import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,ChartPie} from '@icon-park/vue-next';
	import {dateFormat,getCurrencyMark} from '@/utils/index.js';
	import MarketCountry from '@/components/header/market_country.vue';
	import tariffpackagesApi from '@/api/sys/admin/tariffpackagesApi.js';
	import {listRole} from "@/api/sys/admin/roles"
	const globalTable=ref();
	const formLabelWidth = '140px'
	const marketCountryRef =ref();
	let state=reactive({
		 downLoading:false,
		 dialogFormVisible:false,
		 roles:[],
		 queryParam:{
			 groupid:'',
			 search:"",
			 marketplaceid:"",
		 },
		 editForm:{},
		 isload:true,
		 tableData:{records:[],total:0},
	});
	let {
	   queryParam,
	   isload,
	   roles,
	   editForm,
	   dialogFormVisible,
	   tableData,
	   downLoading,
	} =toRefs(state);
 
  
	 
	function handleQuery(){
	     globalTable.value.loadTable(state.queryParam);
	}
	
	function loadTableData(param){
		tariffpackagesApi.list().then((res)=>{
			state.tableData.records=res.data;
			state.tableData.total=res.data.length;
		}); 
	}
	
	function disableApi(row){
		taskApi.list(row).then((res)=>{
			ElMessage.success("操作成功");
			handleQuery();
		});
	}
	function handleEdit(row){
		tariffpackagesApi.detail({"id":row.id}).then(res=>{
			state.editForm=res.data;
			state.dialogFormVisible=true;
		})
		
	}
	function handleAdd(){
		state.editTask={};
		state.dialogFormVisible=true;
	}
	function handleSubmit(){
		if(state.editForm&&state.editForm.id){
			tariffpackagesApi.updateItem(state.editForm).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
				state.dialogFormVisible = false;
			})
		}else{
			tariffpackagesApi.save(state.editForm).then(res=>{
				ElMessage.success("操作成功");
				handleQuery();
				state.dialogFormVisible = false;
			})
		}
		
		
	}
	function enableApi(row){
		taskApi.enableApi(row).then((res)=>{
			ElMessage.success("操作成功");
			handleQuery();
		});
	}
	
	function runApi(row){
		taskApi.runApi(row).then((res)=>{
			ElMessage.success("执行成功");
		});
	}
    function refreshTask(){
		taskApi.refreshTask().then((res)=>{
			ElMessage.success("操作成功");
		});
	}
    function handleDelete(row){
		ElMessageBox.confirm(`确认删除已选中的数据项?`, '警告', {
		  confirmButtonText: '确定',
		  cancelButtonText: '取消',
		  type: 'warning',
		})
		  .then(() => {
		    tariffpackagesApi.deleteItem(row)
		      .then(() => {
		        handleQuery();
		        ElMessage.success('删除成功');
		      })
		      .catch(() => {
		        console.log(`删除失败`);
		      });
		  })
		  .catch(() => ElMessage.info('已取消删除'));
	}
 
	 onMounted(()=>{
		 listRole().then(res=>{
			 state.roles=res.data;
		 })
		 handleQuery();
	 })
	 
	 
</script>

<style>
</style>

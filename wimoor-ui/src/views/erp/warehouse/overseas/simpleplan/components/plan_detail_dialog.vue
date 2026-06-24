<template>
	<el-dialog v-model="visible" class="invheader" title="订单详情" top="8vh" width="80%">
		   <el-space style="padding:20px" wrap  :size="80">
			 <div>订单号：{{row.number}}
					 <el-tag type="danger"  v-if="row.auditstatus==0">已删除</el-tag>
					 <el-tag type="warning" v-if="row.auditstatus==1">备货中</el-tag>
					 <el-tag type="success" v-if="row.auditstatus==2">已完成</el-tag>
		     </div>  
			 <div>海外仓库：{{row.warehousename}}</div>  
			 <div>		  
			    下单日期：{{row.createtime}}
			</div>
		   </el-space>
	       <el-table :data="row.entrys" >
			   <el-table-column label="图片" prop="image" width="70">
			   	<template #default="scope">
			   		<el-image :src="scope.row.image" style="width:45px;height:45px"></el-image>
			   	</template>
			   </el-table-column>
			  <el-table-column label="名称/SKU" prop="name" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>{{scope.row.name}}</div>
			  			<div class="font-extraSmall">{{scope.row.sku}}</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="仓库" prop="warehousename" show-overflow-tooltip>
			          <template #default="scope">
			  			<div>{{scope.row.warehousename}}</div>
			  		</template>
			  </el-table-column>
			 <el-table-column label="库存" prop="qty" width="120"> </el-table-column>
			 <el-table-column label="箱规" prop="boxnum" width="120">  </el-table-column>
			 <el-table-column label="单箱尺寸" prop="quantity" width="120">
			 	<template #default="scope">
			 		 <div v-if="scope.row.plength">
			 			 {{scope.row.plength}}*{{scope.row.pwidth}}*{{scope.row.pweight}}
			 		 </div>
			 		 <div v-else>--</div> 
			 	</template>
			 </el-table-column>
			 <el-table-column label="单箱体积cm³" prop="quantity" width="120">
			 	<template #default="scope">
			 		 <div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight}}</div>
			 		 <div v-else>--</div> 
			 	 </template>
			 </el-table-column>
			 <el-table-column label="单箱体积m³" prop="quantity" width="120">
			 	<template #default="scope">
			 		<div v-if="scope.row.plength">{{scope.row.plength*scope.row.pwidth*scope.row.pweight/1000000}}</div> 
			 		<div v-else>--</div> 
			 	 </template>
			 </el-table-column>
			  <el-table-column label="计划备货数量" prop="price" show-overflow-tooltip>
			          <template #default="scope">
					      {{scope.row.quantity}}
			  		</template>
			  </el-table-column>
			  <el-table-column label="总体积" prop="price" show-overflow-tooltip>
			          <template #default="scope">
						  <div v-if="scope.row.planamount&&scope.row.plength">
						     {{parseFloat(scope.row.planamount)*parseFloat(scope.row.plength)*parseFloat(scope.row.pwidth)*parseFloat(scope.row.pweight)/1000000}}
						  </div>
			  			  <div v-else>--</div>
			  		</template>
			  </el-table-column>
			  <el-table-column label="操作" prop="price" show-overflow-tooltip>
			          <template #default="scope">
			  			<el-button link type="primary"   @click="handleDelete(scope.$index)" >删除</el-button>
			  		</template>
			  </el-table-column>
		   </el-table>
		   <div style="float:right;font-weight: 600;padding:20px;margin-bottom:20px" v-if="summaryVolumn">总体积： {{summaryVolumn}}</div>
		   <template #footer>
			   <div style="float:left">
				   <el-button type="success" v-loading="downloadLoading" @click="downloadForm"><el-icon><Download /></el-icon>导出备货单</el-button>
			   </div>
		   	<span class="dialog-footer">
				<el-space>
		   		<el-button @click="visible=false">取消</el-button>
				<el-dropdown split-button  type="primary" @click="submitForm">
				  <span>完成订单</span>
				  <template #dropdown>
				    <el-dropdown-menu>
				      <el-dropdown-item  @click="submitFormOut">完成订单并出库</el-dropdown-item>
				    </el-dropdown-menu>
				  </template>
				</el-dropdown>
				</el-space>
		   	</span>
		   </template>
	</el-dialog>
</template>

<script setup>
	import {Download} from '@element-plus/icons-vue'
	import {ref,reactive,onMounted,watch,h,toRefs,computed} from 'vue'
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ElMessage,ElDivider} from 'element-plus';
	import orderApi from "@/api/erp/order/orderApi.js";
	import materialApi from "@/api/erp/material/materialApi.js";
	import planFormApi from "@/api/erp/order/planFormApi.js";
	import {dateFormat,dateTimesFormat,formatFloat} from '@/utils/index.js';
	const emit = defineEmits(['change']);
	const state =reactive({
		visible:false,
		downloadLoading:false,
		row:{},
	})
	const {
		visible,
		downloadLoading,
		row,
	}=toRefs(state)
	 
	const summaryVolumn = computed(() => {
		var summary=0;
		state.row.entrys.forEach(row=>{
			 summary=summary+parseFloat(row.planamount)*parseFloat(row.plength)*parseFloat(row.pwidth)*parseFloat(row.pweight)/1000000
		});
		return summary;
	    }
	)
	function submitForm(){
		var ids=[];
		ids.push(state.row.id);
		planFormApi.done(ids).then((res)=>{
			ElMessage.success("操作成功");
			state.visible=false;
			emit('change');
		})
	}
	function submitFormOut(){
		var ids=[];
		ids.push(state.row.id);
		planFormApi.doneOut(ids).then((res)=>{
			ElMessage.success("操作成功");
			state.visible=false;
			emit('change');
		})
	}
	
	function show(row){
		state.visible=true;
		state.row=row;
	}
	function downloadForm(){
		state.downloadLoading=true;
		planFormApi.downloadOrderPlanForm({id:state.row.id},()=>{state.downloadLoading=false});
	}
	defineExpose({
		show,
	})
</script>

<style>
	.invheader .el-dialog__body{
		padding-top:0px;
	}
</style>
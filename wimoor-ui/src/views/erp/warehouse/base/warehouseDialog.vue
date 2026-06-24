<!-- 添加仓库时，共用的仓库弹窗 -->

<template>
	<div class="warehousedailog">
		<el-dialog v-model="visible" title="选择仓库" width="60%"  :before-close="handleClose">
			<div class="con-header">
				<el-row>
					<el-select v-model="queryParams.ftype"     placeholder="全部店铺" @change="loadData">
					      <el-option   v-for="item in warehouseTypeOptions"   
						              :key="item.value"  
									  :label="item.label" 
									  :value="item.value" > 
							</el-option>
					</el-select>
				</el-row>
			</div>
			 <el-table
			   highlight-current-row
			   :data="tableData"
			   v-loading="loading"
			    height="calc(100vh - 370px)" 
			   @selection-change="handleSelectionChange"
			   @current-change="currentChange"
			   border
			 >
			   <el-table-column v-if="!single" type="selection" width="55" align="center" />
			   <el-table-column label="名称" prop="name" width="220" />
			   <el-table-column label="编码" prop="number" width="220" />
			   <el-table-column label="备注" align="center"  prop="remark" show-overflow-tooltip  > 
			   </el-table-column>
			 </el-table>
			 
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="handleClose">取消</el-button>
					<el-button type="primary" @click="submitFunc">确认</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	// Vue依赖
	import { onMounted, reactive, ref, toRefs } from 'vue';
	// API依赖
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	// 组件依赖
	import { Search, Plus, Edit, Refresh, Delete ,ArrowDown} from '@element-plus/icons-vue';
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
    const state= reactive({dialog: {visible: false }  ,
			                        search:'',
								    tableData:[],
								    ids:[],
									warehouse:null,
								    queryParams:{ftype:"self_usable"},
								    warehouseTypeOptions:[{label:"正品仓",value:"self_usable"},
								                  {label:"测试仓",value:"self_test"},
												  {label:"废品仓",value:"self_unusable"},
												  ],
								    loading:false,
			                     });
								 
    const { 
			loading,
			tableData,
			queryParams,
			search,
			ids,
			warehouse,
			dialog,
			warehouseTypeOptions,
		  } = toRefs(state);
	let props = defineProps({
	    visible:false,
		single:false,
	})
	 const { visible,single } = toRefs(props);
	 const emit = defineEmits(['dataChange']);
	 
			function loadData() {
				state.loading=true;
			    warehouseApi.getWarehouseNameList(state.queryParams).then(res=>{
					    state.loading=false;
						state.tableData=res.data;
				}) 
			}
			function currentChange(row){
				state.warehouse =row;
			}
            function handleClose(){
				props.visible = false;
				emit("handleClose");
			}
			
			function submitFunc() {
				if(props.single){
					if (state.warehouse!=null) {
						props.visible = false;
						emit("dataChange",state.warehouse);
					} else {
						ElMessage.error('提交的行数不能小于1！')
						props.visible = true;
					}
				}else{
					if (state.ids.length > 0) {
						props.visible = false;
						emit("dataChange",state.ids);
					} else {
						ElMessage.error('提交的行数不能小于1！')
						props.visible = true;
					}
				}
				
			}
		 
		 function handleSelectionChange(selection) {
		     state.ids = selection.map((item) => item.id);
		 }
		 onMounted(()=>{
			 loadData();
		 })
</script>

<style>
	.el-input__suffix {
		display: flex;
		align-items: center;
		font-size: 16px;
		padding-right: 8px;
	}
	.warehousedailog .el-dialog__body{
		padding-top:0px;
	}
</style>

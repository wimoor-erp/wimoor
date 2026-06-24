<template>
	<div class="pag-radius-bor m-t-16">
		<el-row>
		<el-space>
		<el-button type="primary" v-if="queryParams.acc"  @click="showAccDialog">
		  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
		  <span>记账</span>
		</el-button>
		 

		<el-tooltip  v-else placement="top" content="必须选择具体账户后才能操作">
			<el-button type="primary"  >
			  <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
			  <span>记账</span>
			</el-button>
		</el-tooltip>
	
		<el-button @click="handleFeeItem">费用项管理</el-button>
		<el-button @click="handleDownload">导出</el-button>
		<el-button @click="openUpload" v-if="finAcct?.paymeth==3||finAcct?.paymeth==7">
		    <span title="导入后可以搜索买家账单确认收货时间 已保证系统付款与1688账单对应">账单管理</span>
		</el-button>
		</el-space>
		<div class="rt-btn-group">
			<el-space>
			<Datepicker ref="datepickers" :shortIndex="1"  @changedate="changedate" />
			<el-select v-model="queryParams.project" placeholder="请选择费用项" class="with-select"  @change="handleQuery">
				<el-option  key=""  label="全部费用项" value="" ></el-option>
				<el-option v-for="item in feeTypeList" :key="item.id"  :label="item.name" :value="item.id" ></el-option>
			</el-select>
			<el-input  @input="handleQuery"
			  placeholder="请输入费用项,SKU,单据号"
			  v-model="queryParams.search">
			  <template #suffix>
				   <el-icon class="ic-cen font-medium">
					<search />
				 </el-icon>
			  </template>
			</el-input>
			</el-space>
			  
		</div>
		</el-row>
		<el-row class="m-t-16 expand-table-bg">
			<GlobalTable
			ref="globalTableRef"
			 :tableData="tableData"
			 @expandChange="handleExpandChange"
			  @row-click="tableRowClick"
			 rowKey="createtime"
			  :height="height"
			 :scrollbarAlwaysOn="true"
			 @loadTable="loadTableData" >
				<template #field>
					<el-table-column type="expand" width="65" >
						 <template #default="props">
							<el-table
							scrollbar-always-on
							 :data="props.row.expandTable">
								<el-table-column label="时间"  prop="createtime" />
								<el-table-column label="收支项" prop="name" />
								<el-table-column label="关联单号" prop="number" />
								<el-table-column label="关联SKU" prop="sku" />
								<el-table-column label="金额(￥)" prop="amount">
									<template #default="scope">
										<span v-if="scope.row.ftype=='in'">+</span>
										<span v-if="scope.row.ftype=='out'">-</span>{{scope.row.amount}}
										<span v-if="scope.row.postdate" class="font-extraSmall"> 结算日:{{dateFormat(scope.row.postdate)}}</span>
										<el-button link size="mini" type="primary" v-if="!scope.row.number" @click.stop="cancelAccItem(scope.row)">撤销</el-button> 
									</template>	 
								 </el-table-column>
								<el-table-column label="备注" prop="remark"/>
							</el-table>
						 </template>	 
				</el-table-column>
				<el-table-column label="日期" prop="createtime"/>
				<el-table-column label="收入(￥)" prop="insum"/>
				<el-table-column label="支出(￥)" prop="outsum"/>
				<el-table-column label="结余" prop="balance"/>
				</template>
			</GlobalTable>
		</el-row>
	</div>
	
<FinItem ref="finItemRef" @change="loadTypeList()"></FinItem>
<FinRecord ref="finRecordRef" @change="handleFinChange()"></FinRecord>
</template>

<script setup>
	import {Plus,} from '@icon-park/vue-next';
	import {Search} from '@element-plus/icons-vue'
	import { ref,reactive,onMounted,watch,toRefs,computed,nextTick} from 'vue'
	import Datepicker from '@/components/header/datepicker.vue';
	import FinItem from './finItem.vue';
	import FinRecord from './finRecord.vue';
	import journalApi from '@/api/erp/finances/journalApi.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
	import purchaseAlibabaSettlementApi from '@/api/erp/finances/purchaseAlibabaSettlementApi.js';
	import projectApi from '@/api/erp/finances/projectApi.js';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {useRouter } from 'vue-router';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	let router = useRouter();
	const emit = defineEmits(['changeData']);
	const globalTableRef=ref();
	const uploadDialogRef=ref();
	const finItemRef =ref();
	const finRecordRef =ref();
	const state = reactive({
		height:'calc(100vh - 220px)',
		feeTypeList:[],
		selectLabel:'0',
		expendRows:[],
		searchWord:'',
		finAcct:{},
		queryParams:{search:'',project:""},
		tableData:{records:[],total:0},
	})
	const {
		feeTypeList,
		selectLabel,
		height,
		searchWord,
		finAcct,
		queryParams,
		tableData,
	} = toRefs(state)
	function cancelAccItem(row){
		ElMessageBox.confirm(
			'你确定要撤销该笔记账吗?',
			{
			  confirmButtonText: '确认',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
						journalApi.cancel({"id":row.id}).then(() => {
						  ElMessage.success('删除成功');
						  handleQuery();
						  emit("changeData");
						})  
				 }
			  }
			}
		  )
	}
	function openUpload(){
		 router.push({
		 	path:'/e/f/s',
		 	query:{
		 	  title:"账期管理",
		 	  path:'/e/f/s',
			  acct:state.finAcct.id
		 	}
		 });
	}

	function showAccDialog(){
		if(state.queryParams.acc){
		   finRecordRef.value.show(state.queryParams.acc);
		}else{
			ElMessage.error( '请选择一个具体的账户！' );
		}
	}
	function handleFinChange(){
		  emit("changeData");
		handleQuery();
	}
	function handleFeeItem(){
		finItemRef.value.show();
	}
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.toDate=dates.end;
		handleQuery();
	}
	function handleDownload(){
		journalApi.downExcelDate(state.queryParams);
	}
	function handleExpandChange(row,expandedRows){
		//查询detail
		
		state.expendRows=expandedRows;
		if(row.expandTable&&row.expandTable.length==0){
			var data={};
			data.createdate=row.createtime;
			data.project=state.queryParams.project;
			data.search=state.queryParams.search;
			data.acc=state.queryParams.acc;
			journalApi.findDetialByCondition(data).then((res)=>{
				res.data.forEach(item=>{
					row.expandTable.push(item);
				})
				if(state.height==='calc(100vh - 220px)'){
					state.height = 'calc(100vh - 221px)'
				}else{
					state.height = 'calc(100vh - 220px)'
				}
			});
		}
		
	}
	function tableRowClick(row){
		   globalTableRef.value.toggleRowExpansion(row);
	}
	function handleQuery(){
		state.expendRows.forEach(item=>{
			  globalTableRef.value.toggleRowExpansion(item,false);
		});
		globalTableRef.value.loadTable(state.queryParams);
	}
	function loadTableData(param){
		journalApi.list(param).then(res=>{
			state.tableData.records=res.data.records;
			state.tableData.total=res.data.total;
			if(res.data.total>0){
				state.tableData.records.forEach(item=>{
					item.expandTable=[];
				});
			}
		});
	}
	function loadTypeList(){
		faccountApi.getProject().then((res)=>{
			state.feeTypeList=res.data;
		});
	}
	
	function show(item){
		state.queryParams.acc=item.id;
		state.finAcct=item;
		handleQuery();
	}
	defineExpose({ show })
	onMounted(()=>{
		loadTypeList();
	})
	
	</script>

<style scoped="scoped">
	.with-select{
		width: 120px;
	}


</style>
<style>
	.expand-table-bg .el-table__expanded-cell{
		padding:0px;
	}
	.expand-table-bg .el-table__expanded-cell .el-table__cell{
		background-color:var(--el-fill-color-lighter);
	}
	.expand-table-bg .el-table__expanded-cell .el-table__inner-wrapper::before{background-color:var(--el-fill-color-lighter)}
</style>
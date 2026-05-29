<template>
	<div class="main-sty">
		<div class="con-header">
			<el-row>
			<el-space>
	   <el-radio-group v-model="queryParams.fatype" @change="handleQuery">
		  <el-radio-button label="finsett" >已结算</el-radio-button>
		  <el-radio-button label="other"  >未结算</el-radio-button>
		</el-radio-group>
		<Group @change="groupChange"  />
		<el-select style="width:80px;" v-model="queryParams.currency" @change="handleQuery">
			<el-option v-for="item in currentyOptions" :label="item.label" :value="item.value"></el-option>
		</el-select>
		<div class="date-picker-group">
			<el-select style="width:120px;" placeholder="结算日期" v-model="queryParams.datetype" @change="handleQuery">
				<el-option v-for="item in dateOptions" :label="item.label" :value="item.value"></el-option>
			</el-select>
			<Datepicker ref="datepickers" :shortIndex="1" @changedate="changedate" />
		</div>
		</el-space>
        <div class='rt-btn-group'>
			<el-space>
			    <el-dropdown  v-if="queryParams.fatype=='finsett'" >
			      <el-button type="primary" :loading="checkSummaryLoading|doSummaryLoading">
			        汇总<el-icon class="el-icon--right"><arrow-down /></el-icon>
			      </el-button>
			      <template #dropdown>
			        <el-dropdown-menu>
			          <el-dropdown-item @click="handleCheckSummary">校验账期汇总</el-dropdown-item>
			          <el-dropdown-item  @click="handleDoSummary">强制重算</el-dropdown-item>
			        </el-dropdown-menu>
			      </template>
			    </el-dropdown>
			<el-button @click="handleAllDownload" :loading="downloadAllLoading">导出所有账期</el-button>
					<el-button class='ic-btn' title='帮助文档'>
						<help theme="outline" size="16" :strokeWidth="3" />
					</el-button>
					</el-space>
				</div>
				</el-row>
		</div>
		
		<div class="con-body">
			<el-row :gutter="16" class="m-b-16">
				<el-col :span="6">
					<el-card shadow="never">
						<div class="flex-center">
							<div class="r-icon">
								<bank-transfer class="ic-cen" theme="filled" size="20" fill="var(--el-color-primary)"/>
							</div>
							<div class="l-data">
								<div class="title">未结算金额</div>
								<div class="data">{{summary.finacc}}</div>
							</div>
						</div>
					</el-card>
				</el-col><el-col :span="6">
					<el-card shadow="never">
						<div class="flex-center">
							<div class="r-icon">
								<bank-transfer class="ic-cen" theme="filled" size="20" fill="var(--el-color-success)"/>
							</div>
							<div class="l-data">
								<div class="title">已结算金额</div>
								<div class="data">{{summary.finsett}}</div>
							</div>
						</div>
					</el-card>
				</el-col><el-col :span="6">
					<el-card shadow="never">
						<div class="flex-center">
							<div class="r-icon">
								<bank-transfer class="ic-cen" theme="filled" size="20" fill="var(--el-color-blue)"/>
							</div>
							<div class="l-data">
								<div class="title">总金额</div>
								<div class="data">{{summary.finsum}}</div>
							</div>
						</div>
					</el-card>
				</el-col>
				<el-col :span="6">
					<el-card shadow="never">
						<div class="flex-center">
							<div class="r-icon">
								<bank-transfer class="ic-cen" theme="filled" size="20" fill="var(--el-color-info)"/>
							</div>
							<div class="l-data">
								<div class="title">转账失败金额</div>
								<div class="data">¥0.00</div>
							</div>
						</div>
					</el-card>
				</el-col>
			</el-row>
			<GlobalTable ref="globalTable"
			  :tableData="tableData"  height="calc(100vh - 300px)"  
			  :defaultSort="{ prop: 'startDate', order: 'descending' }"  @loadTable="loadTableData" :stripe="true"  
			  style="width: 100%;margin-bottom:16px;">
			 	<template #field>
					 <el-table-column  label="店铺" prop="groupname"></el-table-column>
					 <el-table-column label="国家" prop="marketName"></el-table-column>
					 <el-table-column label="结算周期" prop="startDate">
						 <template #default="scope">
						 	 <span>{{dateFormat(scope.row.startDate)}}-{{dateFormat(scope.row.endDate)}}</span>
						 </template>
					 </el-table-column>
					 <el-table-column label="状态" >
					 	<template #default="scope">
							<el-tag  v-if="scope.row.pstatus=='Open'" type="warning">未转账</el-tag>
							<el-tag  v-else-if="scope.row.pstatus=='Fail'" type="danger" >转账失败</el-tag>
							<el-tag  v-else-if="scope.row.pstatus=='Pending'" type="warning" >转账中</el-tag>
							<el-tag  v-else-if="scope.row.pstatus=='Closed'" type="success" >已转账</el-tag>
							<el-tag  v-else  type="success" >已转账</el-tag>
							
					 		
					 	</template>
					 </el-table-column>
					 <el-table-column label="存款总计" prop="totalAmount">
					 	<template #default="scope">
					 	   <p>{{scope.row.currency}} {{scope.row.totalAmount}}</p>
						   <p>{{queryParams.currency}} {{scope.row.totalAmount_to}}</p>
					 	</template>
					 </el-table-column>
					 <el-table-column label="转账日期" prop="deposit_date">
						 <template #default="scope">
						 	 <span>{{scope.row.deposit_date}}</span>
						 </template>
					 </el-table-column>
					 <el-table-column label="转账状态">
					 	<template #default="scope">
					 		<el-tag  v-if="scope.row.tstatus=='Succeeded'" type="success">已完成</el-tag>
					 		<el-tag  v-else-if="scope.row.tstatus=='Fail'||scope.row.tstatus=='Failed'" type="danger" >已失败</el-tag>
					 	</template>
					</el-table-column>
					<el-table-column label="汇总状态">
					 	<template #default="scope">
					 		<el-tag  v-if="scope.row.sumtime" type="success">已完成</el-tag>
					 		<el-tag  v-else type="danger" >待计算</el-tag>
					 	</template>
					</el-table-column>
					 <el-table-column label="操作">
					 	<template #default="scope">
					 		<el-button type="primary" v-if="scope.row.settlement_id" @click.stop="downloadList(scope.row)" :loading="scope.row.downloading" link>下载明细</el-button>
					 	    <el-button type="primary" v-if="scope.row.settlement_id" @click.stop="showDetail(scope.row)" link>查看详情</el-button>
						</template>
					 </el-table-column>
			 	</template>
			 </GlobalTable>
		</div>
	</div>
	<Detail ref="myDetailRef"></Detail>
</template>
<script>
    export default{ name:"回款记录" };
</script>
<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {Help,BankTransfer} from '@icon-park/vue-next';
	import Datepicker from '@/components/header/datepicker.vue';
	import Group from '@/components/header/group.vue';
	import Detail from './detail.vue';
	import {dateFormat,outputmoney} from '@/utils/index.js';
	import { ElMessage, ElMessageBox} from 'element-plus';
		import { Search, ArrowDown, } from '@element-plus/icons-vue'
	import settlementAccRptApi from '@/api/amazon/finances/settlementAccRptApi.js';
	const globalTable=ref();
	const myDetailRef=ref();
	const state = reactive({
		radio:'全部',
		downloadAllLoading:false,
		checkSummaryLoading:false,
		doSummaryLoading:false,
		dateOptions:[{label:'结算日期',value:''},{label:'转账日期',value:'acc'}],
		currentyOptions:[{label:'原币种',value:'market'},{label:'CNY',value:'CNY'},{label:'USD',value:'USD'}],
		tableData: {records:[],total:0},
		queryParams:{
			"fatype":"finsett",
			"currency":"CNY",
			"datetype":'',
		},
		isload:true,
		summary:{},
	})
	const {radio,currentyOptions,downloadAllLoading,dateOptions,queryParams,isload,tableData,summary,checkSummaryLoading,doSummaryLoading
	}=toRefs(state)
	function groupChange(obj){
			state.queryParams.groupid=obj.groupid;
			state.queryParams.marketplaceid=obj.marketplaceid;
			state.queryParams.marketplace_name=obj.market.pointName;
			state.queryParams.marketcurrency=obj.market.currency;
			handleQuery();
	}
	//日期改变
	function changedate(dates){
		state.queryParams.fromDate=dates.start;
		state.queryParams.endDate=dates.end;
		if(state.isload==false){
			handleQuery();
		}
	}
	function handleAllDownload(){
		ElMessageBox.confirm(
			   '您要下载查询的所有账期吗，此过程将很漫长（建议不要导出超过三个的账期），请耐心等待？',
			   '请确认',
			   {
			     confirmButtonText: '确认',
			     cancelButtonText: '取消',
			     type: 'warning',
			   }
			 )
			   .then(() => {
									state.downloadAllLoading=true;
									settlementAccRptApi.downloadAllSettlementExcel(state.queryParams,()=>{
										state.downloadAllLoading=false;
									})
				 });
				 
			   }
    function handleCheckSummary(){
		state.checkSummaryLoading=true;
		settlementAccRptApi.checkSettlementSummary(state.queryParams).then(res=>{
			state.checkSummaryLoading=false;
			ElMessage.success('完成操作');
			handleQuery();
		}).catch(e=>{
			state.checkSummaryLoading=false;
		})
		
	}
	function handleDoSummary(){
		state.doSummaryLoading=true;
		settlementAccRptApi.doSettlementSummary(state.queryParams).then(res=>{
			ElMessage.success('完成操作');
			state.doSummaryLoading=false;
			handleQuery();
		}).catch(e=>{
			state.doSummaryLoading=false;
		})
		
	}
	function handleQuery(){
		if(state.queryParams.currency=="market"){
			if(state.queryParams.marketplaceid===''){
				ElMessage({
				message: '请选择对应的国家！',
				type: 'warning',
				})
				return
			}
			state.queryParams.currency=state.queryParams.marketcurrency;
		}
		globalTable.value.loadTable(state.queryParams);
	}
	function loadTableData(params){
		 state.isload=false;
		 settlementAccRptApi.getSettlementAccReport(params).then(res=>{
			 if(res.data&&res.data.records){
				 res.data.records.forEach(item=>{
					 if(item.deposit_date){
					      item.deposit_date= dateFormat(item.deposit_date);
					 }
					 if(item.totalAmount){
						 item.totalAmount=outputmoney(item.totalAmount);
					 }
					if(item.totalAmount_to){
							 item.totalAmount_to=outputmoney(item.totalAmount_to);
					}
				 })
			 }
			 state.tableData.records=res.data.records;
			 state.tableData.total=res.data.total;
		 })
		 //同时加载summary
		 settlementAccRptApi.getSettlementAccReportSum(params).then(res=>{
		 	 state.summary=res.data;
		 })
	}
	
	function downloadList(row){
		var data={};
		data.amazonauthid=row.amazonauthid;
		data.settlementId=row.settlement_id
		data.marketplaceName=row.marketplace_name;
		row.downloading=true;
		settlementAccRptApi.downSettlementExcel(data,()=>{
			row.downloading=false;
		});
	}
	function showDetail(row){
		 myDetailRef.value.show(row,state.queryParams);
	}
</script>

<style scoped="scoped">
	.r-icon{
		background-color:var(--el-color-info-lighter) ;
		width: 40px;
		height: 40px;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 50%;
	}
	.l-data{
		margin-left:16px;
	}
	.l-data .title{
		color:#666;
		font-size:12px;
		margin-bottom: 4px;
	}
	.l-data .data{
		font-size: 18px;
		font-weight: 700;
	}
	.m-b-16{
		margin-bottom: 16px;
	}
</style>
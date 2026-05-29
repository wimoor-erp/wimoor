<template>
	<div class="account"> 
	<div class="account-card el-white-bg" ref="leftdiv" :style="{minHeight: leftheight}">
		 
		<h4>我的账户
			<el-button style="float:right;" @click.stop="showDeleteDailog" type="info" link size="mini">查看删除列表</el-button>
		</h4>
			<el-card :class="item.active?'m-t-16 active':'m-t-16' " class="pointer " v-for="item in DataList"  @click="handleActive(item)">
				      <div class="card-header flex-between">
						<span>
				        <el-space direction="vertical" :size="2" alignment="left">
						<span @click.stop="showPayIndexDialog">{{item.paymethName}}</span>
						<span class="font-extraSmall">{{item.name}}</span>
						</el-space>&nbsp;
						<el-tag  v-if="item.isdefault"  :title="item.name" size="small" effect="plain" type="danger" >默认</el-tag>
						</span>
						<el-dropdown v-if="item.id" trigger="click">
						<el-icon class="font-large text-gray pointer"><MoreFilled /></el-icon>
						    <template #dropdown>
						      <el-dropdown-menu>
								<el-dropdown-item  v-if="item.isdefault"   @click="handleCancelDefault(item)">取消该支付方式默认卡</el-dropdown-item>
								<el-dropdown-item  v-else @click="handleDefault(item)">设为该支付方式默认卡</el-dropdown-item>
						        <el-dropdown-item @click="handleRename(item)">重命名</el-dropdown-item>
						        <el-dropdown-item @click="handleDelect(item)">删除</el-dropdown-item>
						      </el-dropdown-menu>
						    </template>
						  </el-dropdown>
				      </div>
					  <div class="card-body">
						  <span :class="item.balance>0?'text-primary':''">￥
						  <span class="ft-24" v-if="item.balance">{{formatFloat(item.balance)}}</span>
						  <span class="ft-24" v-else>0.00</span>
						  </span>
						  <p class="font-extraSmall">账户资产</p>
					  </div>
			</el-card>
			<el-card @click="handleAdd" class="add-account pointer" shadow="hover">
				<div class="flex-vertical">
				<el-icon class="text-center"><Plus /></el-icon>
				<span class="text-gray">添加账户</span>
				</div>
			</el-card>
			 
	</div>
	<div class="account-chart" ref="rightdiv">
		<el-row :gutter="16">
			<el-col :span="16">
				<LineChart ref="lineChartRef"/>
			</el-col>
			<el-col :span="8">
				<PieChart ref="pieChartRef"/>
			</el-col>
		</el-row>
		<el-row>
			<Table  ref="tableRef" @changeData="refreshData"/>
		</el-row>
	</div>
	</div>
	<el-dialog 
	width="480px"
	:title="title" 
	:rules="rules"
	v-model="acconutVisable"
	>
	<el-form v-model="form" label-width="80px">
		<el-form-item label="名称" prop="name">
			<el-input v-model="form.name"></el-input>
		</el-form-item>
		<el-form-item label="账户类型" v-if="type=='create'">
			    <el-radio-group v-model="form.paymeth" class="ml-4">
			      <el-radio :label="item.id"  v-for="item in payMethodList">{{item.name}}</el-radio>
			    </el-radio-group>
		</el-form-item>
	</el-form>
	 <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="acconutVisable = false">取消</el-button>
	        <el-button type="primary" @click="handleConfirm">
	          确认
	        </el-button>
	      </span>
	    </template>
	</el-dialog>
	<el-dialog
	width="480px"
	title="已删除账户列表" 
	v-model="delvisable"
	>
	 <el-table :data="deltableData">
		 <el-table-column prop="paymethName" label="账户类型" width="180" />
		 <el-table-column prop="name" label="账户名" width="180" />
		 <el-table-column prop="name" label="操作" >
			  <template #default="scope">
				  <el-button link type="primary" @click.stop="recoverItem(scope.row)">恢复</el-button>
			  </template>
		</el-table-column>
	 </el-table>
	 <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="delvisable = false">取消</el-button>
	      </span>
	    </template>
	</el-dialog>
	<PaymethodIndex ref="paymentIndexRef" @change="loadPaymentMethod();loadMyAccount(true)"></PaymethodIndex>
</template>
<script>
	export default{ name:"采购记账" };
</script>
<script setup>
	import { ref,reactive,onMounted,toRefs,nextTick} from 'vue'
	import {MoreFilled,Plus} from '@element-plus/icons-vue'
	import {ElMessage, ElMessageBox } from 'element-plus';
	import LineChart from "./components/lineChart.vue"
	import PieChart from "./components/pieChart.vue"
	import Table from "./components/table.vue";
	import PaymethodIndex from "./components/paymethod_index_dialog.vue";
	import {formatFloat} from '@/utils/index.js';
	import faccountApi from '@/api/erp/finances/faccountApi.js';
    const pieChartRef=ref();
	const tableRef=ref();
	const rightdiv=ref();
	const leftdiv=ref();
	const lineChartRef =ref();
	const paymentIndexRef=ref();
	const state = reactive({
		delvisable:false,
		DataList:[ ],
		title:'账户编辑',
		type:'add',
		activeItem:{},
		acconutVisable:false,
		leftheight:"600px",
		accountid:'',
		form:{
			name:'',
			paymeth:'',
		},
		rules:{
			 name:[{ required: true, message: '名称不能为空！', trigger: 'blur' }],
		},
		payMethodList:[],
		deltableData:[],
	})
	const {
		DataList,
		acconutVisable,
		form,
		rules,
		accountid,
		title,
		type,
		leftheight,
		payMethodList,
		delvisable,
		deltableData,
	}=toRefs(state)
	function showDeleteDailog(){
		state.delvisable=true;
		loadDelTableData();
	}
	function loadDelTableData(){
		faccountApi.findAccountArchiveAll().then((res)=>{
			state.deltableData=res.data;
		});
	}
	function showPayIndexDialog(){
		paymentIndexRef.value.show();
	}
	function recoverItem(row){
		var data={};
		data.id=row.id;
		faccountApi.recoverAccountDelete(data).then((res)=>{
			if(res.data){
				ElMessage.success('恢复成功');
				state.delvisable=false;
				loadMyAccount();
			}
		});
	}
	function handleRename(item){
		state.type = 'add';
		state.acconutVisable = true;
		state.form = item
	}
	function handleConfirm(){
		if(state.form.id){
			faccountApi.updateAccountName(state.form).then(res=>{
				state.acconutVisable = false;
				ElMessage.success('修改成功');
				loadMyAccount();
			})
		}else{
			faccountApi.saveAccount(state.form).then(res=>{
				state.acconutVisable = false;
				ElMessage.success('修改成功');
				loadMyAccount();
			})
		}
	}
	
	function handleDefault(item){
		faccountApi.updateAccountDefault(item).then(res=>{
			ElMessage.success('设置成功');
		    loadMyAccount();
		})
	}
	
	function handleCancelDefault(item){
		faccountApi.cancelAccountDefault(item).then(res=>{
			ElMessage.success('设置成功');
		    loadMyAccount();
		})
	}
	
	function handleDelect(item){
		faccountApi.updateAccountDelete(item).then(res=>{
			ElMessage.success('删除成功');
			loadMyAccount();
		})
	}
	
	function handleAdd(){
		state.title = '添加账户';
		state.type = 'create';
		state.form.name="";
		state.form.id="";
		state.acconutVisable = true;
		
	}
	function loadPaymentMethod(){
		faccountApi.getPaymentMethod().then((res)=>{
			if(res.data && res.data.length>0){
				state.payMethodList=res.data;
				state.form.paymeth=state.payMethodList[0].id;
			   }
			});
	}
	function handleActive(item){
			state.DataList.forEach(row=>{
				if(row.id==item.id){
					row.active=true;
				}else{
				    row.active=false;
				}
			});
			state.activeItem=JSON.parse(JSON.stringify(item));
			loadRightData(item);
	}
	
	function loadRightData(item){
		  pieChartRef.value.show(item);
		  tableRef.value.show(item);
		  lineChartRef.value.show(item);
	}
	function loadMyAccount(isInit){
		faccountApi.getAccountAll().then(res=>{
			state.DataList=res.data;
			var summary=0.00;
			state.DataList.forEach(item=>{
				summary+=item.balance;
			});
			state.DataList.unshift({paymethName:"总账户",isdefault:false,balance:summary,id:"",});
			if(isInit){
				handleActive(state.DataList[0]);
			}else{
				handleActive(state.activeItem);
			}
		});
	}
	function refreshData(){
		tableRef.value.show(state.activeItem);
		lineChartRef.value.show(state.activeItem);
		loadMyAccount(false);
	}
onMounted(()=>{
	 loadPaymentMethod();
	 loadMyAccount(true);
	 nextTick(()=>{
		 var height=rightdiv.value.offsetHeight;
		 state.leftheight=(height-0)+"px";
	 })
	
})
</script>

<style scoped="scoped">
	.flex-vertical{
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-top: 24px;
		margin-bottom: 24px;
	}
	.text-gray{
		color: #999;
	}
	.account{
		display: flex;
	}
	.active{
		background:#ff8000;
		border: 1px solid #ff8000 !important;
		box-shadow: 0 2px 12px 0 rgba(255,107,14,0.2);
	}
	.active,.active .text-primary,.active .text-gray{
		color: #fff!important;
	}
	.active .font-extraSmall{
		color:rgba(255,255,255,0.6)
	}
	.active .el-tag--plain.el-tag--danger{
		background-color: transparent;
		color: #fff;
		border-color:rgba(255,255,255,0.6) ;
	}
	.account-card{
		padding:16px;
		width:280px;
		overflow-y:auto;
	}
	.account-card h4{
		margin-bottom: 16px;
	}
	
	.account-chart{
		padding:16px;
		flex: 1;
		background-color: var(--el-color-info-lighter);
	}
	.dark .account-chart{
		background-color:#0e0e0e;
	}
	.m-t-16{
		margin-bottom: 16px;
	}
	.account-card .card-body{
		margin-top: 24px;
	}
	.ft-24{
		font-size: 24px;
		font-family:DIN Alternate,Helvetica Neue,Helvetica,Arial, SF Pro Display;
		font-weight: 700;
	}
	.text-primary{
		color: var(--el-color-primary);
	}
	
	.add-account{
		border: 2px dashed #DEDEDE;
	}

</style>

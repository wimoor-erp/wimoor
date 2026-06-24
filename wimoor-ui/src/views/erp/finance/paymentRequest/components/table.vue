<template>
	<GlobalTable  
	ref="globalTable"
	 show-summary
	 @selectionChange='selectChange' :tableData="tableData"
	 height="calc(100vh - 302px)"  @loadTable="loadTableData" >
		<template #field>
		<el-table-column type="selection" width="55"></el-table-column>
		<el-table-column label="请款单号" width="150" prop="number" />
		<el-table-column label="关联单号" width="150"  prop="purnumber">
			
		</el-table-column>
		<!-- <el-table-column label="请款类型"  prop="orderType"></el-table-column> -->
		<el-table-column label="付款方式" prop="paymethod"></el-table-column>
		<el-table-column label="状态" prop="number">
			<template #default="scope">
				<el-tag  type="warning" v-if="scope.row.auditstatus==0">待审核</el-tag>
				<el-tag   v-if="scope.row.auditstatus==1">已审核待付款</el-tag>
				<el-tag  type="success" v-if="scope.row.auditstatus==2">已完成</el-tag>
				<el-tag  type="danger" v-if="scope.row.auditstatus==3">已驳回</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="申请总金额(￥)" prop="price" width="200" sortable="custom"></el-table-column>
		<!-- <el-table-column label="已付款(￥)" >
			<template #default="scope">
				<span>{{scope.row.paid}}</span>
			</template>
		</el-table-column> -->
		<!-- <el-table-column label="预计付款日期" prop="calculateDate">	</el-table-column> -->
		<el-table-column label="申请人" prop="creatorname">	</el-table-column>
		<el-table-column label="申请日期" prop="number">
			<template #default="scope">
				 {{dateFormat(scope.row.createtime)}}
			</template>
		</el-table-column>
		<el-table-column label="审核人" prop="auditorname" >
			<template #default="scope">
				 {{NullTransform(scope.row.auditorname)}}
			</template>
		</el-table-column>
		<el-table-column label="审核日期" prop="number">
			<template #default="scope">
				 {{dateFormat(scope.row.audittime)}}
			</template>
		</el-table-column>
		<el-table-column label="备注" prop="number">
			<template #default="scope">
				 {{scope.row.remark}}
			</template>
		</el-table-column>
		<el-table-column label="操作" width="240">
			<template #default="scope">
				<el-popover
				    trigger="click"
					placement="left" 
					width="240px"
					:visible="scope.row.approveVisible"
				  >
					<template #reference>
						<el-button v-if="scope.row.auditstatus==0"  @click="approve(scope.row)" type="primary" link>审核</el-button>
					</template>
				   <el-form 
					 label-position="top">
					   <el-form-item label="审核结果"  prop="radio">
						    <el-radio-group v-model="scope.row.approvepass" >
						         <el-radio label="1" >通过</el-radio>
						         <el-radio label="2" >驳回</el-radio>
						    </el-radio-group>
					   </el-form-item>
					   <el-form-item label="备注">
						   <el-input v-model="scope.row.remark"
							 maxlength="50"
							 show-word-limit
							type="textarea"></el-input>
					   </el-form-item>
					   <el-form-item >
						   <el-button @click="scope.row.approveVisible=false"> 取消</el-button>
						   <el-button @click="handleProcess(scope.row)" type="primary">确认</el-button>
					   </el-form-item>
				   </el-form>
				  </el-popover>
				  <el-popover
				     trigger="click"
				  					placement="left" 
				  					width="240px"
				  					:visible="scope.row.payVisible" >
				  					<template #reference>
				  					  <el-button v-if="scope.row.auditstatus==1" @click="payment(scope.row)" type="primary" link>付款</el-button>
				  					</template>
				                   <el-form 
				  					  :rules="rules"
				  					  :model="from"
				  					 label-position="top">
				  					   <el-form-item label="付款金额"  prop="price">
				  						   {{from.price}} ￥ 
				  					   </el-form-item>
				  					   <el-form-item >
				  						   <el-button @click="scope.row.payVisible=false"> 取消</el-button>
				  						   <el-button @click="handleConfirm(scope.row,form)" type="primary">确认付款</el-button>
				  					   </el-form-item>
				                   </el-form>
				   </el-popover> 
				   <el-popover :visible="scope.row.returnVisible" placement="left"   trigger="click" >
				   				      <p >作废单据，已付款金额将退回！</p>
				   				      <div class="m-t-8">
				   				        <el-button size="small"  @click="scope.row.returnVisible = false">取消</el-button>
				   				        <el-button size="small" type="primary" @click="handlePayReturn(scope.row)"
				   				          >作废</el-button
				   				        >
				   				      </div>
				   				      <template #reference>
				   						  <el-button type="primary" v-if="scope.row.auditstatus==1" link @click="scope.row.returnVisible=true">作废</el-button>
				   				      </template>
				   				    </el-popover> 
				<el-button type="primary" link @click="handleDetail(scope.row)">详情</el-button>
				 
			  
			</template>
		</el-table-column>
		</template>
	</GlobalTable>
</template>

<script setup>
	import {h,ref,reactive,toRefs,nextTick} from 'vue';
	import {dateFormat,dateTimesFormat} from '@/utils/index.js';
	import { ElMessage, ElMessageBox} from 'element-plus'
	import { useRoute,useRouter } from 'vue-router';
	import NullTransform from"@/utils/null-transform";
	import purchaseFinlistApi from '@/api/erp/purchase/finance/listApi.js';
	const emit = defineEmits(["getCheckRow"])
	const router = useRouter();
	let globalTable=ref();
	const state=reactive({
		queryParams:{},
		from:{
			radio:'1',
			price:0,
			remark:'',
		},
		rules:{
			amount:[
				{ required: true, message: '不能为空！', trigger: 'blur' },
				{ type: 'number', message: '请输入数字' },
				{ validator:checkAmount, trigger: 'blur' },
				
			]
		},
		 tableData:{records:[],total:0},
	})
	const{
		tableData,
		from,
		rules,
		queryParams,
	}=toRefs(state)
	
	function handleDetail(row){
		router.push({
			path:"/f/p/d",
			query:{
				title:'请款单详情',
				path:"/f/p/d",
				id:row.id
			},
		})
	}
	/* 金额校验 */
	function checkAmount(rule, value, callback){
		if(value<0)
		return callback(new Error('不能为负数！'))
	}
	/* 确认付款 */
	function handleConfirm(row){
			//确认付款
			var idlist=[];
			idlist.push(row.id); 
			purchaseFinlistApi.paymentForm(idlist).then((res)=>{
				if(res.data){
					state.paydialogVisible=false;
					ElMessage.success('付款成功！');
					load(state.queryParams);
				}
			});
	}
	function handlePayReturn(row){
		var idlist=[];
			idlist.push(row.id); 
		purchaseFinlistApi.approveReturn(idlist).then((res)=>{
			if(res.data){
				ElMessage.success('作废成功！');
				load(state.queryParams);
			}
		});
	}
	function approve(row){
		row.approvepass="1";
		row.approveVisible=true;
	}
	/* 审核 */
	function handleProcess(row){
		var data=[];
		data.push(row.id);
		if(row.approvepass=="1"){
			purchaseFinlistApi.approve(data).then((res)=>{
				if(res.data){
					purchaseFinlistApi.updateRemark({"id":row.id,"remark":row.remark}).then((ress)=>{});
					ElMessage.success('审核通过！');
					row.approveVisible=false;
					load(state.queryParams);
				}
			});
		}else{
			purchaseFinlistApi.approveReturn(data).then((res)=>{
				if(res.data){
					purchaseFinlistApi.updateRemark({"id":row.id,"remark":row.remark}).then((ress)=>{});
					ElMessage.success('审核驳回！');
					row.approveVisible=false;
					load(state.queryParams);
				}
			});
		}
		
		
	}
	 
	function selectChange(selection){
		emit("getCheckRow",selection)
	}
	function payment(row){
		row.payVisible=true;
		nextTick(()=>{
		state.from.price=row.price;
		})
	}
	function loadTableData(params){
		purchaseFinlistApi.list(params).then((res)=>{
			state.tableData.records = res.data.records;
			state.tableData.total =res.data.total;
		})
	}
	function load(params){
		state.queryParams=params;
		globalTable.value.loadTable(params);
	}
	defineExpose({
		load,
	})
</script>

<style>

</style>

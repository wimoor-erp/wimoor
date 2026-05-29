<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写加工单</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							  <el-steps :active="active" finish-status="success" align-center class="m-b-t-24">
							    <el-step title="创建" />
							   <!-- <el-step title="待处理"  /> -->
							    <el-step title="处理中"  />
							    <el-step title="完成"  />
							  </el-steps>
							  
						 <el-form
						  v-if="active==0"
						  :model="form"
						   :rules="rules"
						   label-width="120px">
							  <el-row>
							 <el-col :span="12">
							 <el-form-item label="单据编码"   >
							 	<el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="操作类型" >
							     <el-radio-group v-model="form.ftype" >
							          <el-radio label="ass" >组装</el-radio>
							          <el-radio label="dis">拆分</el-radio>
							     </el-radio-group>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
								 <el-form-item label="操作仓库" prop="warehouse">
								 		<Warehouse @changeware="getWarehouse" defaultValue="only" />
								 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							   <el-form-item label="组合SKU" prop="sku">
							   <el-input 
							   @click="handleAdd" 
							   class="in-wi-24"  v-model="form.sku" placeholder="请选择"> </el-input>
							   </el-form-item>
							 </el-col>
							 <el-col :span="12">
							  <el-form-item label="加工数量"  prop="num">
								<el-input class="in-wi-24"  v-model.number="form.num" placeholder="数量"> </el-input>
							  </el-form-item>
							 							
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="备注"  >
									<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="备注"> </el-input>
								  </el-form-item>
							 							
							 </el-col>
							  </el-row>
						 </el-form>
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >子SKU列表</h5>
						   </div>
						 </div>
						 <el-table border :data="tableData">
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column  prop="image" label="图片" width="80" >
							    <template #default="scope">
							    <el-image v-if="scope.row.image" :src="scope.row.image" class="product-img" ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.mname}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="单位数量" prop="subnumber"/>
							 <el-table-column label="需求数量">
							   <template #default="scope">
								   <span v-if="form.num">
								   {{parseInt(scope.row.subnumber)*form.num}}
								   </span>
							   </template>
							 </el-table-column>
							 <el-table-column label="仓库" >
								  <template #default="scope">
									  <span v-if="scope.row.submap">
										  {{scope.row.submap.warehouse}}
									  </span>
								  </template>
							 </el-table-column> 
							 <el-table-column label="可用库存" >
							 <template #default="scope">
								 <span v-if="scope.row.submap">
								   {{scope.row.submap.amount}}
								   <span v-if="scope.row.submap.amount+scope.row.submap.inbound<parseInt(scope.row.subnumber)*form.num">
								    <el-tag size="small" type="danger">库存不足</el-tag>
								   </span>
								</span>
							 </template>
							 </el-table-column>
							 <el-table-column label="待入库" >
								 <template #default="scope">
									 <span v-if="scope.row.submap">
										  {{scope.row.submap.inbound}}
									 </span>
								</template>
							</el-table-column>	 
						 </el-table>
						 <el-row v-if="active==2">
						 	<div class="mark-re">
						 	   <div>
						 	  <h5 >入库记录</h5>
						 	  </div>
						 	</div>
							<el-table border>
							  <el-table-column label="操作时间" />
							  <el-table-column label="操作人" />
							  <el-table-column label="入库数量" />
							  <el-table-column label="备注" />
							  <el-table-column label="操作">
								  <template #default="scope">
									<el-button
									 link type="primary" >撤销入库</el-button>
								  </template>
							  </el-table-column>
							</el-table>
						 </el-row>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button @click="closeTab">关闭</el-button>
									<el-button v-if="active==2">终止单据</el-button>
									<el-button v-if="active==0" type="primary" @click="submitForm">提交</el-button>
									<el-button v-if="active==1" type="primary" @click="handleStart">开始加工</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<MaterialDialog  :isAssemblySKU="true" @getdata="getdata" ref = "MaterialRef"/>
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage } from 'element-plus'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import Warehouse from '@/components/header/warehouse.vue';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import { useRoute,useRouter } from 'vue-router';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import {getSubForm,saveData } from '@/api/erp/assembly/assemblyApi.js';
	const MaterialRef = ref();
	const router = useRouter();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		form:{
			number:'',
			ftype:'ass',
			warehouseid:'',
			num:1,
			materialid:"",
			id:"",
		},
		rules: {
			warehouse: [{ required: true, message: '操作仓库不能为空', trigger: 'blur' }],
			sku: [{ required: true, message: '组合SKU不能为空', trigger: 'blur' }],
			num:[{ required: true, message: '不能为空', trigger: 'blur' }, { type:'number', message: '请输入数字',},]
		},
		tableData:[],
		active:0,
	})
	const {
		form,
		rules,
		tableData,
		active,
	}=toRefs(state)
	onMounted(()=>{
		load();
	});
    function handleAdd(){
		MaterialRef.value.show()
	}
	function loadSub(){
		state.tableData = [];
		if(state.form.materialid&&state.form.warehouseid){
			getSubForm({"materialid":state.form.materialid,"warehouseid":state.form.warehouseid}).then((res)=>{
				if(res.data.msg){
					ElMessage.error(res.data.msg);
				}else{
					state.tableData=res.data.sublist;
				}
			});
		}
	}
	function getdata(row){
		if(row && row.length==1){
			state.form.sku = row[0].sku;
			state.form.materialid=row[0].id;
			//通过主SKUid 仓库id去查找子产品信息
			loadSub();
			 
		}else{
			ElMessage.error("只能选择一个主SKU！ ")
		}
	}
	//保存单据
	function submitForm(){
		var data={};
		var subAssList=[];
		state.tableData.forEach(function(item){
			var subitem = {};
			subitem.sku = item.sku;
			subitem.formid = "";
			subitem.warehouseid = item.submap.warehouseid;
			data.warehouseid=item.submap.warehouseid;
			subitem.amount =  parseInt(item.subnumber)*state.form.num;
			subitem.whamount = item.submap.amount;
			if (subitem.amount == 0 || subitem.amount == "" || subitem.amount < 0) {
				ElMessage.error("请输入正确数量！");
				return;
			}
			if (typeof(subitem.sku) != "undefined" && subitem.sku != "") {
				subAssList.push(JSON.stringify(subitem));
			}
		});
		data.subAssList=subAssList.toString();
		data.ftype=state.form.ftype;
		data.remark=state.form.remark;
		data.materialid=state.form.materialid;
		data.amount=state.form.num;
		data.id="";
		saveData(data).then((res)=>{
			if(res.data){
				ElMessage.success(res.data.msg);
				state.form.id=res.data.id;
				router.push({
					path:"/e/p/p/d",
					query:{
						title:'加工单详情',
						path:"/e/p/p/d",
						id:res.data.id
					},
				})
				//state.active = 1;
			}
		})
		
	}
	//开始加工
	// function handleStart(){
	// 	state.active = 2
	// }
	function getWarehouse(wid){
		state.form.warehouseid=wid;
		loadSub();
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"MO","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
	}
</script>

<style>
	.m-b-24{
		margin-bottom: 24px;
	}
		.he-scr-car{
			height:calc(100vh - 176px);
			margin-bottom: 20px;
		}
	.in-wi-24{
			width: 400px;
		}
		.mark-re{
			margin-top: 16px;
			margin-bottom:16px;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	.mark-re h5::before{
		content: "";
		display: inline-block;
		height: 13px;
		width: 4px;
		margin-right:8px;
		background-color: #FF6700;
	}
	.fill-from-body{
		margin:16px 24px;
	}
	.m-b-t-24{
		margin-bottom:24px;
		margin-top:24px;
	}
	.m-b-16{
		margin-bottom: 16px;
	}
	.pro-content{
		font-size: 12px;
	}
	.pro-content .sku{
		margin-top:4px;
		color: var(--el-color-blue);
	}
</style>

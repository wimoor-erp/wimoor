<template>
	<div class="el-white-bg">
					 <el-scrollbar  :class="inForm?'he-scr-car-form':'he-scr-car'" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写出库单</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							
						 <el-form :model="form"
						   :rules="rules"
						   label-width="120px">
							  <el-row>
							 <el-col :span="12">
							 <el-form-item label="单据编码"   >
							 	<el-input class="in-wi-24" disabled  v-model="form.number" placeholder="请输入"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="收货方" >
								 <el-input class="in-wi-24"    v-model="form.customername" placeholder="请输入"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
								 <el-form-item label="出库仓库" prop="warehouse">
									 <el-space>
										 <Warehouse :warehouseType="warehouseFromType" @changeware="getWarehouse" defaultValue="only" :isform="true" />
									 </el-space>
								 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="快递单号" >
							 	<el-input class="in-wi-24" v-model="form.expressno" placeholder="请输入快递单号"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="备注"  >
							 	<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="请输入备注"> </el-input>
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="操作类型"  >
									<el-select v-model="form.ftype"    placeholder="操作类型" @change="handleFtype">
										  <el-option  v-for="item in dispatchFormTypeOptions" :key="item.value" :label="item.name" :value="item.value"> </el-option>
									</el-select>
								  </el-form-item>
							 </el-col>
							  <el-col :span="12">
							 <el-form-item label="物流快递" >
							 	<el-input class="in-wi-24" v-model="form.express" placeholder="请输入物流快递"> </el-input>
							 </el-form-item>
							 </el-col>
							  </el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >出库商品 </h5>
						   </div>
						   <el-button @click="handleAdd" type="primary">添加商品</el-button>
						 </div>
						 <el-table border :data="tableData">
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column  prop="image" label="图片"  width="65" >
							    <template #default="scope">
							     <el-image v-if="scope.row.image" :src="scope.row.image"   width="40" height="40"  ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"   width="40" height="40"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="product" label="名称/SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="出库数量">
								  <template #default="scope">
									  <el-input v-model="scope.row.outamount" @input="scope.row.outamount=CheckInputInt(scope.row.outamount)">
									  </el-input>
								  </template>  
							 </el-table-column>
							 <el-table-column label="操作" width="80">
								  <template #default="scope">
								    <el-button @click="handleDelete(scope.$index)" link type="primary" >删除</el-button>
					        	  </template>
							 </el-table-column>
						 </el-table>
						</div>
					</el-scrollbar>
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;">
								<el-space>
									<el-button type="" @click="closeTab">关闭</el-button>
									<el-button type="primary" @click="submitForm">提交</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<MaterialDialog  ref = "MaterialRef"  :isfulfillable="form.isNothungry" :warehouseid="form.warehouseid" @getdata="getRows" />
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue';
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue';
	import {ElMessage } from 'element-plus';
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import { useRoute,useRouter } from 'vue-router';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import outApi from '@/api/erp/inventory/outApi.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import {getDictItemsByTypeCode} from "@/api/sys/admin/dict.js";
	let props = defineProps({inForm:null,isOversea:null});
	const {inForm,isOversea} = toRefs(props);
	const emit = defineEmits(['change']);
	const router = useRouter()
	const MaterialRef = ref();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		if(props.inForm){
			emit("change");
		}else{
			emitter.emit("removeTab", 100);
		}
	};
	const state = reactive({
		form:{
			name:'',
			number:'',
			warehouseid:'',
			isNothungry:true,
			remark:'',
			customer:'',
			expressno:'',
			express:'',
			customername:'',
			ftype:0
		},
		dispatchFormTypeOptions:[],
		rules: {
			warehouse: [{ required: true, message: '出库仓库不能为空', trigger: 'blur' }],
		},
		tableData:[],
		skulist:{},
		warehouseFromType:"self_usable",
	})
	const {
		form,
		rules,
		tableData,
		skulist,
		dispatchFormTypeOptions,
		warehouseFromType,
	}=toRefs(state)
	onMounted(()=>{
		load();
		if(props.isOversea){
			var timer=setTimeout(function(){
				state.warehouseFromType="oversea_usable";
				state.form.ftype=3;
				clearTimeout(timer);
			},1000);
		}
	});
	function handleFtype(){
		if(state.form.ftype==0){
			state.warehouseFromType="self_usable";
		}
		if(state.form.ftype==1){
			state.warehouseFromType="self_unusable";
		}
		if(state.form.ftype==2){
			state.warehouseFromType="self_test";
		}
		if(state.form.ftype==3){
			state.warehouseFromType="oversea_usable";
		}
	}
    function handleAdd(){
		MaterialRef.value.show();
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"O","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
		getDictItemsByTypeCode('out_form_type').then((response) => {
			   if(response.data){
				   response.data.forEach(item=>{item.value=parseInt(item.value);})
			   }
			   state.dispatchFormTypeOptions = response.data;
		});
	}
	function getWarehouse(wid,type,warehouse){
		state.form.warehouseid=wid;
		state.form.isNothungry=!warehouse.ishungry;
	}
	function getRows(rows){
		if(rows && rows.length>0){
			if(state.tableData && state.tableData.length>0){
				var skus="";
				state.tableData.forEach(function(datas){
					 skus+=(datas.sku)+(",");
				});
				rows.forEach(function(item){
					if(skus.indexOf((item.sku+","))<0){
						state.tableData.push(item);
					}
				});
			}else{
				state.tableData=rows;
			}
		}
	}
	 function getSKUList() {
		    state.skulist={};
			var flag=true;
			state.tableData.forEach(function(item){
				var sku = item.id;
				var amount = item.outamount;
				if(state.skulist[sku]){
					ElMessage.error("不能重复添加SKU！ ");
					flag=false;
				}
				if (typeof(sku)!="undefined"&&sku!=""&&amount!="") {
					state.skulist[sku]=amount;
				}
			});
			
			return flag;
	}
	function submitForm(){
		var isok=getSKUList();
		if(isok==true){
			var data={};
			data.skumapstr=JSON.stringify(state.skulist);
			data.remark=state.form.remark;
			data.warehouseid=state.form.warehouseid;
			data.customer=state.form.customername;
			data.express=state.form.express;
			data.expressno=state.form.expressno;
			data.ftype=state.form.ftype;
			data.toaddress="";
			data.id="";
			if(data.skumapstr=="{}"){
				ElMessage.error("至少添加一行数据！ ");
				return;
			}
			outApi.saveData(data).then((res)=>{
				isok=true;
				if(res.data){
					ElMessage.success( "添加成功！");
					if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
						if(props.inForm){
							emit("change");
						}else{
							router.push({
								path:"/e/w/o/d",
								query:{
									title:'出库单详情',
									path:"/e/w/o/d",
									id:res.data.id,
									replace:true
								},
							})
						}
							
					}
				}else{
					ElMessage.error(res.data);
				}
			});
		}
	}
	function handleDelete(index){
		state.tableData.splice(index,1);
	}
 
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
		.he-scr-car{
			height:calc(100vh - 176px);
			margin-bottom: 20px;
		}
		.he-scr-car-form{
			height:calc(100vh - 276px);
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
</style>

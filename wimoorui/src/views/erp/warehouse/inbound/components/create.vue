<template>
	<div class="el-white-bg">
					 <el-scrollbar :class="inForm?'he-scr-car-form':'he-scr-car'" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写入库单</h3>
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
								 <el-form-item label="入库仓库" prop="warehouse">
									 <el-space>
										 <Warehouse ref="warehouseRef" @changeware="getWarehouse" :warehouseType='inForm?"oversea_usable":null' defaultValue="only" :isform="true" />
									 </el-space>
								 </el-form-item>
							 </el-col>
							 <el-col :span="12">
							 <el-form-item label="备注"  >
							 	<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="请输入备注"> </el-input>
							 </el-form-item>
							 </el-col>
							  </el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >入库商品 </h5>
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
							 <el-table-column label="入库数量">
								  <template #default="scope">
									  <el-input v-model="scope.row.inamount" @input="scope.row.inamount=CheckInputInt(scope.row.inamount)">
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
	<MaterialDialog  ref = "MaterialRef"   :warehouseid="form.warehouseid"   @getdata="getRows" />
	<SupplierDialog ref='supDiaRef' @getdata="getSupplierRows" />
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
	import inApi from '@/api/erp/inventory/inApi.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue";
	const router = useRouter()
	const MaterialRef = ref();
	const warehouseRef=ref();
	const supDiaRef =ref();
	let props = defineProps({inForm:null,isOversea:null});
	const {inForm,isOversea} = toRefs(props);
	const emit = defineEmits(['change']);
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
			number:'',
			warehouseid:'',
			remark:'',
		},
		rules: {
			warehouse: [{ required: true, message: '入库仓库不能为空', trigger: 'blur' }],
		},
		tableData:[],
		skulist:{},
	})
	const {
		form,
		rules,
		tableData,
		skulist
	}=toRefs(state)
	onMounted(()=>{
		load();
	});
    function handleAdd(){
		MaterialRef.value.show();
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"IN","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
	}
	function getWarehouse(wid){
		state.form.warehouseid=wid;
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
				var amount = item.inamount;
				if(state.skulist[sku]){
					ElMessage.error( "不能重复添加SKU！ ");
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
			data.id="";
			if(data.skumapstr=="{}"){
				ElMessage.error("至少添加一行数据！ ");
				return;
			}
			inApi.saveData(data).then((res)=>{
				isok=true;
				if(res.data){
					ElMessage.success( "添加成功！");
					if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
						if(props.inForm){
						   emit("change");
						}else{
							router.push({
								path:"/e/w/i/d",
								query:{
									title:'入库单详情',
									path:"/e/w/i/d",
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
	function showSupplier(){
		supDiaRef.value.show();
	}
	function getSupplierRows(row){
		if(row && row.id){
			state.form.customer=row.id;
			state.form.customername=row.name;
		}
	}
	defineExpose({
		getRows,
	})
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

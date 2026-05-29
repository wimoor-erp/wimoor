<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写代料单</h3>
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
										 <Warehouse @changeware="getWarehouse" defaultValue="only" :isform="true" />
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
						   <h5 >代料商品 </h5>
						   </div>
						   <el-button @click="handleAdd" type="primary">添加调入商品</el-button>
						 </div>
						 <el-table border :data="tableData">
							 <el-table-column label="序号" type="index" width="80"/> 
							 <el-table-column  prop="image" label="图片"  width="65" >
							    <template #default="scope">
							     <el-image v-if="scope.row.image" :src="scope.row.image"  class="product-img"  ></el-image>
							 	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-img"  ></el-image>
							   </template>
							 </el-table-column>
							 <el-table-column  prop="product" label="调入SKU"  show-overflow-tooltip>
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							  <el-table-column  prop="fulfillable" label="可用库存"></el-table-column>
							  <el-table-column  prop="product" label="调出SKU"  show-overflow-tooltip>
							     <template #default="scope">
									 <div class="flex-center">
									 <el-image v-if="scope.row.outimage" :src="scope.row.outimage"   class="product-img" ></el-image>
									 <div v-else>
									 <el-image v-if="scope.row.outsku"  :src="$require('empty/noimage40.png')"  class="product-img" ></el-image>
									 </div>
									 <div class="m-l-8">
									 <div class='name'>{{scope.row.outname}}</div>
									<span class="sku">{{scope.row.outsku}}</span>
									</div>
									</div>
									<el-button v-if="scope.row.outsku"  class="position-td" @click="handleAddOut(scope.row)" style="margin-left:8px;">修改SKU</el-button>
							        <el-button v-else @click="handleAddOut(scope.row)" style="margin-left:8px;">添加SKU</el-button>
							    </template>
							  </el-table-column>
							   <el-table-column  prop="outfulfillable" label="可用库存"></el-table-column>
							 <el-table-column label="调动数量">
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
	<MaterialDialog  ref = "rowsMaterialRef" :warehouseid="form.warehouseid"  :isfulfillable="true"  @getdata="getOutRows" />
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
	import changeApi from '@/api/erp/inventory/changeApi.js';
	import Warehouse from '@/components/header/warehouse.vue';
	const router = useRouter();
	const MaterialRef = ref();
	const rowsMaterialRef = ref();
	const supDiaRef =ref();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
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
		skulist:[],
		localrow:{},
	})
	const {
		form,
		rules,
		tableData,
		skulist,
		localrow
	}=toRefs(state)
	onMounted(()=>{
		load();
	});
    function handleAdd(){
		MaterialRef.value.show();
	}
	function handleAddOut(row){
		rowsMaterialRef.value.show();
		state.localrow=row;
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"CH","isfind":"true"}).then((res)=>{
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
	function getOutRows(rows){
		if(rows && rows.length>0){
			var datas=rows[0];
			state.localrow.outsku=datas.sku;
			state.localrow.outfulfillable=datas.fulfillable;
			state.localrow.outname=datas.name;
			state.localrow.outimage=datas.image;
			state.localrow.fromsku=datas.id;
		}
	}
	 function getSKUList() {
		    state.skulist=[];
			var flag=true;
			state.tableData.forEach(function(item){
				var sku = item.id;
				var amount = item.inamount;
				var fromsku=item.fromsku;
				if(!fromsku){
					ElMessage.error("请选择调出SKU！");
					flag=false;
				}
				// if(state.skulist.indexOf(sku)>=0){
				// 	ElMessage.error( "不能重复添加SKU！");
				// 	flag=false;
				// }
				if(amount>item.outfulfillable){
					ElMessage.error( "调动数量不能大于可用库存！");
					flag=false;
				}
				if (typeof(sku)!="undefined"&&sku!=""&&amount!="" && typeof(fromsku)!="undefined"&&fromsku!="") {
					state.skulist.push({"sku_to":sku,"sku_from":fromsku,"amount":amount});
				}
			});
			return flag;
	}
	function submitForm(){
		var isok=getSKUList();
		if(isok==true){
			var data={};
			var skumapstr=JSON.stringify(state.skulist);
			skumapstr=skumapstr.replaceAll("[","");
			skumapstr=skumapstr.replaceAll("]","");
			data.skumapstr=skumapstr;
			data.remark=state.form.remark;
			data.warehouseid=state.form.warehouseid;
			data.id="";
			if(data.skumapstr=="{}"){
				ElMessage.error("至少添加一行数据！ ");
				return;
			}
			changeApi.saveData(data).then((res)=>{
				isok=true;
				if(res.data){
					ElMessage.success("添加成功！");
					if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
						setTimeout(function(){
							router.push({
								path:"/e/w/e/d",
								query:{
									title:'代料单详情',
									path:"/e/w/e/d",
									id:res.data.id,
									replace:true
								},
							})
						},500)
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
	function getSupplierRows(rows){
		if(rows && rows[0] && rows[0].id){
			state.form.customer=rows[0].id;
			state.form.customername=rows[0].name;
		}
	}
</script>

<style>
	.font-12{font-size: 12px;margin-right:4px;}
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
	.m-l-8{
		margin-left:8px ;
	}
	.position-td{
		position: absolute;
		top: 0;
		right: 0;
		opacity: 0;
	}
	tr:hover .position-td{
		opacity:1;
	}
</style>

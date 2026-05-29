<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写调库单</h3>
						 <el-button   class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							  <el-steps :active="0" finish-status="success" align-center class="m-b-t-24">
							    <el-step title="创建" />
							    <el-step title="审核"  />
							    <el-step title="配货中"  />
							    <el-step title="已发货"  />
							    <el-step title="完成"  />
							  </el-steps>
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
							 <el-form-item label="预计到货日期" >
							     <el-date-picker
							         v-model="form.arrivaltime"
							         type="date"
							         placeholder="请选择"
							       />
							 </el-form-item>
							 </el-col>
							 <el-col :span="12">
								<el-form-item label="调出仓库" prop="fromwarehouse">
									 <el-space>
										 <Warehouse :warehouseType="warehouseFromType"  :defaultid="form.fwid" @changeware="getfromWarehouse" defaultValue="only" :isform="true" />
									 </el-space>
								</el-form-item>
							 </el-col>
							 <el-col :span="12">
							   <el-form-item label="调入仓库" prop="towarehouse">
							   <el-space>
							   	  <Warehouse :warehouseType="warehouseToType"   :defaultid="form.twid" @changeware="gettoWarehouse" defaultValue="only" :isform="true" />
							   </el-space>
							   </el-form-item>
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="备注"  >
									<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="输入备注"> </el-input>
								  </el-form-item>
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="操作类型"  >
									<el-select v-model="form.ftype"    placeholder="操作类型" @change="handleFtype">
										  <el-option  v-for="item in dispatchFormTypeOptions" :key="item.value" :label="item.name" :value="item.value"> </el-option>
									</el-select>
								  </el-form-item>
							 </el-col>
							  </el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >调库商品</h5>
						    <span class="font-extraSmall text-red"  v-if="needApply">注意仓库地址不同时，建议由调入仓库负责人审核对应调库单，已确认正常收货</span>
						   </div>
						   <el-button @click="handleAdd" type="primary">添加商品</el-button>
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
							       <div class='name'>{{scope.row.name}}</div>
							       <div class='sku'>{{scope.row.sku}}
							       </div>
							   </template>
							 </el-table-column>
							 <el-table-column label="可用库存" prop="fulfillable"/>
							 <el-table-column label="调库数量">
								  <template #default="scope">
									  <el-input v-model="scope.row.amount" @input="scope.row.amount=CheckInputInt(scope.row.amount)">
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
									<el-button type="" @click="closeTab">取消</el-button>
									<el-button type="primary" v-if="!needApply" @click.stop="submitForm('passdone')" plain>直接完成</el-button>
									<span style="padding-left:10px;"></span>
									<el-button type="success" @click.stop="submitForm('save')">保存</el-button>
									<el-button type="primary" @click.stop="submitForm('submit')">提交</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<MaterialDialog @getdata="getRows" :isfulfillable="true" :warehouseid="form.fromwarehouseid" ref = "MaterialRef"/>
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs,getCurrentInstance } from 'vue'
	import {ElMessage } from 'element-plus'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import { useRoute,useRouter } from 'vue-router';
	import {getDictItemsByTypeCode} from "@/api/sys/admin/dict.js";
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import dispatchApi from '@/api/erp/inventory/dispatchApi.js';
	import preprocessApi from '@/api/erp/purchase/plan/preprocessApi.js';
	import warehouseApi from '@/api/erp/warehouse/warehouseApi.js';
	import {getItemByFormids } from '@/api/erp/assembly/assemblyApi.js'
	const MaterialRef = ref();
	const router = useRouter();
	const editid = router.currentRoute.value.query.id;
	const fwid = router.currentRoute.value.query.fwid;
	const twid = router.currentRoute.value.query.twid;
	const consformid=router.currentRoute.value.query.consformid;
	const assemblyids=router.currentRoute.value.query.assemblyids;
	const { proxy } = getCurrentInstance();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	const state = reactive({
		form:{
			number:'',
			fromwarehouseid:'',
			towarehouseid:'',
			remark:'',
			
			arrivaltime:new Date(),
			fwid:fwid,
			twid:twid,
			ftype:0
		},
		needApply:false,
		warehouseFromType:"self_usable",
		warehouseToType:"self_usable",
		warehouseList:[],
		rules: {
			fromwarehouse: [{ required: true, message: '发货仓库不能为空', trigger: 'blur' }],
			towarehouse: [{ required: true, message: '收货仓库不能为空', trigger: 'blur' }],
		},
		tableData:[],
		skulist:{},
		dispatchFormTypeOptions:[]
	})
	const {
		form,
		rules,
		warehouseList,
		needApply,
		dispatchFormTypeOptions,
		warehouseFromType,
		warehouseToType,
		tableData,
		skulist,
	}=toRefs(state)
	function handleFtype(){
		if(state.form.ftype==0){
			state.warehouseFromType="self_usable";
			state.warehouseToType="self_usable";
		}
		if(state.form.ftype==1){
			state.warehouseFromType="self_usable";
			state.warehouseToType="self_unusable";
		}
		if(state.form.ftype==2){
			state.warehouseFromType="self_unusable";
			state.warehouseToType="self_usable";
		}
		if(state.form.ftype==3){
			state.warehouseFromType="self_usable";
			state.warehouseToType="self_test";
		}
		if(state.form.ftype==4){
			state.warehouseFromType="self_test";
			state.warehouseToType="self_usable";
		}
		if(state.form.ftype==5){
			state.warehouseFromType="self_usable";
			state.warehouseToType="oversea_usable";
		}
		if(state.form.ftype==6){
			state.warehouseFromType="oversea_usable";
			state.warehouseToType="self_usable";
		}
	}		 
    function handleAdd(){
		MaterialRef.value.show();
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
	function handleDelete(index){
		state.tableData.splice(index,1)
	}
	function getfromWarehouse(wid){
		state.form.fromwarehouseid=wid;
		if(checkDifferentWarehouse()){
			state.needApply=true;
		}else{
			state.needApply=false;
		}
	}
	function gettoWarehouse(wid){
		state.form.towarehouseid=wid;
		if(checkDifferentWarehouse()){
			state.needApply=true;
		}else{
			state.needApply=false;
		}
	}
	function checkDifferentWarehouse(){
		var fromwarehouse={};
		var towarehouse={};
		state.warehouseList.forEach(item=>{
			if(item.id==state.form.fromwarehouseid){
				fromwarehouse=item;
			}
			if(item.id==state.form.towarehouseid){
				towarehouse=item;
			}
		})
		if(fromwarehouse.addressid!=towarehouse.addressid){
			return true;
		}else{
			return false;
		}
	}
	function getSKUList() {
		    state.skulist={};
			var flag=true;
			state.tableData.forEach(function(item){
				var sku = item.id;
				var amount = item.amount;
				if(state.skulist[sku]){
					ElMessage.error( "不能重复添加SKU！ ");
					flag=false;
				}
				if (typeof(sku)!="undefined"&&sku!=""&&amount!="" && amount>0) {
					state.skulist[sku]=amount;
				}
			});
			if(isEmptyObject(state.skulist)){
				ElMessage.error("请至少输入一个产品SKU及其数量！ ");
				flag= false;
			}
			return flag;
	} 
	function submitForm(ftype){
		if(state.form.fromwarehouseid==state.form.towarehouseid){
			ElMessage.error( "不能选择相同仓库！ ");
		}else{
			var isok=getSKUList();
			if(isok==true){
				var data={};
				data.action=ftype;
				data.skumapstr=JSON.stringify(state.skulist);
				data.remark=state.form.remark;
				data.fromwarehouseid=state.form.fromwarehouseid;
				data.towarehouseid=state.form.towarehouseid;
				data.arrivaltime=state.form.arrivaltime;
				data.ftype=state.form.ftype;
				data.id="";
				dispatchApi.saveData(data).then((res)=>{
					isok=true;
					if(res.data){
						ElMessage.success( "添加成功！");
						if(consformid!=null && consformid!=undefined && consformid!=''){
							 preprocessApi.update({"formid":consformid,"donetype":"dispatch"});
						}
						if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
								router.push({
									path:"/e/w/t/d",
									query:{
										title:'调库单详情',
										path:"/e/w/t/d",
										id:res.data.id,
									    replace:true
									},
								})
						}
					}else{
						ElMessage.error(res.data);
					}
				});
			}
		}
	}
	function load(){
		getDictItemsByTypeCode('dispatch_form_type').then((response) => {
			   if(response.data){
				   response.data.forEach(item=>{item.value=parseInt(item.value);})
			   }
					state.dispatchFormTypeOptions = response.data;
		});
		
		serialnumberApi.getSerialNumber({"ftype":"PD","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
		if(editid!="" && editid!=undefined && editid!=null){
			setTimeout(function(){
				dispatchApi.getData({"id":editid}).then((res)=>{
					if(res.data){
						var dataform=res.data.warehouseform;
						state.form.fromwarehouseid=dataform.from_warehouseid;
						state.form.towarehouseid=dataform.to_warehouseid;
						state.form.number=dataform.number;
						state.form.remark=dataform.remark;
						state.tableData=res.data.dispatchFormEntryList;
						if(checkDifferentWarehouse()){
							state.needApply=true;
						}else{
							state.needApply=false;
						}
					}
				});
			},800)
		}
		
		if(consformid!=null && consformid!=undefined && consformid!=''){
			//加载run的consform的sku到列表 
			preprocessApi.getSkuListByRunid({"runid":consformid}).then((res)=>{
				state.tableData=res.data;
				if(res.data && res.data.length>0){
					state.form.fwid=res.data[0].warehouseid;
				}
			});
			
		}
		
		if(assemblyids!=null && assemblyids!=undefined && assemblyids!=''){
			getItemByFormids(assemblyids).then((res)=>{
				state.tableData=res.data;
				if(res.data && res.data.length>0){
					state.form.fwid=res.data[0].warehouseid;
				}
			});
		}
	}
	function isEmptyObject(e) {  //判断一个Object对象{}是否为空
	    var t;  
	    for (t in e)  
	        return !1;  
	    return !0  
	} 
	onMounted(()=>{
		state.warehouseList=[];
		warehouseApi.getWarehouseList().then(function(res){
			if(res.data && res.data.length>0){
				res.data.forEach(warehouses=>{
					 if(warehouses&&warehouses.children&&warehouses.children.length>0){
					     warehouses.children.forEach(item=>{  state.warehouseList.push(item);  })
					 }
				})
			}
			load();
		})
	});
</script>

<style scoped>
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
</style>

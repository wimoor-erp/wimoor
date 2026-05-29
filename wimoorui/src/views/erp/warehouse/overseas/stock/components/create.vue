<template>
	<div class="el-white-bg">
						 <div class="gird-line-head">
						 <h3>填写海外仓备货单</h3>
						 <el-button class='ic-btn' title='帮助文档'>
						   <help theme="outline" size="16" :strokeWidth="3"/>
						 </el-button>
						 </div>
						 <div class="fill-from-body">
							  <el-steps :active="0" finish-status="success" align-center class="m-b-t-24">
							    <el-step title="创建"   />
							    <el-step title="审核"   />
							    <el-step title="配货中" />
							    <el-step title="已发货" />
							    <el-step title="完成"   />
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
								<el-form-item label="发货仓库" prop="fromwarehouse">
									 <el-space>
										 <Warehouse   
										 :defaultid="form.fromwarehouseid" 
										 @changeware="getfromWarehouse" 
										  defaultValue="only" 
										  warehouseType="self_usable"
										 :isform="true" />
									 </el-space>
								</el-form-item>
							 </el-col>
							 <el-col :span="12">
							   <el-form-item label="海外仓库" prop="towarehouse">
							   <el-space>
							   	  <Warehouse   
								  :defaultid="form.towarehouseid" 
								  @changeware="gettoWarehouse" 
								   warehouseType="oversea_usable"
								   defaultValue="only" 
								  :isform="true" />
							   </el-space>
							   </el-form-item>
							 </el-col>
							 <el-col :span="12">
							  <el-form-item label="店铺"  >
									 <el-select v-model="form.groupid"     placeholder="发货店铺" @change="groupChange">
										  <el-option  v-for="item in groupList"   :key="item.id"  :label="item.name" :value="item.id"   >
										  </el-option>
									 </el-select>
							  </el-form-item>
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="备注"  >
									<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="输入备注"> </el-input>
								  </el-form-item>
							 </el-col>
							</el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >备货商品 </h5>
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
							 <el-table-column label="名称" >
							    <template #default="scope">
							       <div class='name'>{{scope.row.name}}</div>
							   </template>
							 </el-table-column>
							 <el-table-column label="本地产品SKU" prop="sku" width="150"/>
							 <el-table-column label="平台商品SKU" prop="psku" width="150">
								 <template #default="scope">
								     <div class='name'>{{scope.row.psku}}</div>
									     <div class='font-extraSmall'>FNSKU:{{scope.row.fnsku}}</div>
								 </template>
							 </el-table-column> 
							 <el-table-column label="可用库存" prop="fulfillable" width="110"/>
							 <el-table-column label="长*宽*高(cm)" prop="length" width="180">
									<template #default="scope">
										<div v-if="scope.row.length">
										<span >长:{{scope.row.length}}cm</span>,
										<span >宽:{{scope.row.width}}cm</span>,
										<span >高:{{scope.row.height}}cm</span>
										</div>
										<div v-else>-</div>
									</template>
							 </el-table-column>
							 <el-table-column label="带包装重量(kg)" prop="weight" width="110">
							<template #default="scope">
								<div v-if="scope.row.weight">
								<span >{{scope.row.weight}}</span> 
								</div>
								<div v-else>-</div>
							</template>
							</el-table-column>
							 <el-table-column label="发货数量" width="110">
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
		 
						<div class='text-center mar-top-16'>
							 <div style="padding-top:10px;padding-bottom:20px">
								<el-space >
									<el-button type="" @click="closeTab">取消</el-button>
									<span style="padding-left:10px;"></span>
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
	import { ref,reactive,onMounted,watch,inject,toRefs } from 'vue'
	import {ElMessage } from 'element-plus'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import { useRoute,useRouter } from 'vue-router';
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import dispatchApi from '@/api/erp/inventory/dispatchOverseaApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import productinfoApi from '@/api/amazon/product/productinfoApi.js'
	import {loadInventory} from "@/hooks/erp/shipment/data_helper";
	import planApi from '@/api/erp/ship/planApi.js';
	const MaterialRef = ref();
	const router = useRouter();
	const emitter = inject("emitter"); // Inject `emitter`
	const emit = defineEmits(['close','confirm']);
	
	let props = defineProps({isdialog:""});
	const {isdialog} = toRefs(props);
	function closeTab(){
		if(props.isdialog){
			emit("close");
		}else{
				emitter.emit("removeTab", 100);
		}
	};
	var warehouseid=router.currentRoute.value.query.warehouse;
	var marketplaceid=router.currentRoute.value.query.marketplaceid;
	var overseaid=router.currentRoute.value.query.overseaid;
	var groupid=router.currentRoute.value.query.group;
	var batchnumber=router.currentRoute.value.query.batchnumber;	
	var editid=router.currentRoute.value.query.copyid;
	const state = reactive({
		form:{
			number:'',
			fromwarehouseid:warehouseid,
			towarehouseid:overseaid,
			remark:'',
			arrivaltime:new Date(),
			groupid:groupid,
			country:''
		},
		rules: {
			fromwarehouse: [{ required: true, message: '发货仓库不能为空', trigger: 'blur' }],
			towarehouse: [{ required: true, message: '收货仓库不能为空', trigger: 'blur' }],
		},
		tableData:[],
		groupList:[],
		queryData:{},
		skulist:{},
	})
	const {
		form,
		rules,
		tableData,
		groupList,
		queryData,
		skulist,
	}=toRefs(state)

    function handleAdd(){
		MaterialRef.value.show();
	}
	function loadProductSKU(){
		state.tableData.forEach(item=>{
			item.msku=item.sku;
			productinfoApi.getInfoByMsku({"groupid":state.form.groupid,
			                              country:state.form.country,
										  sku:item.msku}).then(res=>{
				if(res.data){
					item.psku=res.data[0].sku;
					item.fnsku=res.data[0].fnsku;
				}else{
					item.psku="-";
					item.fnsku="-";
				}
			})
		})
	}
	function getRows(rows){
		if(rows && rows.length>0){
			if(state.tableData && state.tableData.length>0){
				var skus="";
				state.tableData.forEach(function(datas){
					 skus+=(datas.sku)+(",");
					 datas.msku=datas.sku;
				});
				rows.forEach(function(item){
					item.msku=item.sku;
					if(skus.indexOf((item.sku+","))<0){
						state.tableData.push(item);
					}
				});
			}else{
				state.tableData=rows;
			}
		    loadProductSKU();
			loadInventory(state,state.tableData,state.form.fromwarehouseid);
		}
	}
	function handleDelete(index){
		state.tableData.splice(index,1)
	}
	function getfromWarehouse(wid){
		state.form.fromwarehouseid=wid;
		loadInventory(state.tableData,state.form.fromwarehouseid);
	}
	function gettoWarehouse(wid,type,warehouse){
		state.form.towarehouseid=wid;
		state.form.country=warehouse.country;
		loadProductSKU();
	}
	function groupChange(groupid){
		loadProductSKU();
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
				}else{
					flag=false;
				}
			});
			if(isEmptyObject(state.skulist)){
				ElMessage.error( "请至少输入一个产品SKU及其数量！ ");
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
				data.skumapstr=JSON.stringify(state.tableData);
				data.remark=state.form.remark;
				data.fromwarehouseid=state.form.fromwarehouseid;
				data.towarehouseid=state.form.towarehouseid;
				data.country=state.form.country;
				data.batchnumber=batchnumber;
				data.groupid=state.form.groupid;
				data.arrivaltime=state.form.arrivaltime;
				data.id="";
				dispatchApi.saveData(data).then((res)=>{
					isok=true;
					if(res.data){
						ElMessage.success("添加成功！");
						if(batchnumber){
							var formid=res.data.id;
							planApi.removeBatchItem(batchnumber).then(res=>{
										router.push({
											path:"/e/w/os/d",
											query:{
												title:'海外仓补货单详情',
												path:"/e/w/os/d",
												id:formid,
												replace:true
											},
										})
							});
						}else{
							
							if(res.data && res.data.id && res.data.id!=null && res.data.id!="" && res.data.id!=undefined){
								if(props.isdialog){
									emit("confirm");
								}else{
										router.push({
											path:"/e/w/os/d",
											query:{
												title:'海外仓补货单详情',
												path:"/e/w/os/d",
												id:res.data.id,
												replace:true
											},
										})
								}
									
							}
						}
					}else{
						ElMessage.error(res.data);
					}
				});
			}
		}
	}
	function load(){
		serialnumberApi.getSerialNumber({"ftype":"OD","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data; 
			}
		});
		groupApi.getAmazonGroup().then((res)=>{
			res.data.push({"id":"",name:"无"})
			state.groupList=res.data;
		})
		if(batchnumber){
							 state.productlist=[];
							 state.totalproducts=0;
							 var data={};
							 if(marketplaceid=="EU"){
								  data.marketplaceid="EU";
							 }else{
								  data.marketplaceid=marketplaceid;
							 }
							 data.batchnumber=batchnumber;
							 data.groupid=groupid;
							 data.warehouseid=warehouseid;
							 planApi.batchList(data).then((res)=>{
										 if(res.data && res.data.length>0){
											 res.data[0].list.forEach(function(item){
												 item.quantity=item.amount;
												 item.id=item.materialid;
												 item.sellersku=item.sku;
												 item.sku=item.msku;
											 });
										     getRows(res.data[0].list);
							       }
							 })
		}
		if(editid!="" && editid!=undefined && editid!=null){
				dispatchApi.getData({"id":editid}).then((res)=>{
					if(res.data){
						var dataform=res.data.warehouseform;
						state.form.fromwarehouseid=dataform.from_warehouseid;
						state.form.towarehouseid=dataform.to_warehouseid;
						state.form.number=dataform.number;
						state.form.remark=dataform.remark;
						state.form.groupid=dataform.groupid;
						state.form.country=dataform.country;
						state.form.id=undefined;
						res.data.dispatchFormEntryList.forEach(item=>{
							item.id=item.materialid;
						});
						state.tableData=res.data.dispatchFormEntryList;
						 getRows(state.tableData);
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
	
	function initData(form){
		state.form.fromwarehouseid=form.fromwarehouseid;
		state.form.towarehouseid=form.towarehouseid;
		 getRows(form.list);
	}
	
	onMounted(()=>{
		load();
	});
	
	defineExpose({ initData });
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

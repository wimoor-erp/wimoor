<template>
	<div class="el-white-bg">
					 <el-scrollbar class="he-scr-car" @scroll="scroll">
						 <div class="gird-line-head">
						 <h3>填写换货单</h3>
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
									<el-form-item label="采购单"  >
										 <el-space>
											 <el-input @click="selectPurchase" v-model="form.entryNumber" placeholder="选择关联采购单" ></el-input>
										 </el-space>
									</el-form-item>
							 </el-col>
							 <el-col :span="12">
								<el-form-item label="操作仓库" prop="fromwarehouse">
									 <el-space>
										 <Warehouse ref="warehouseRef" :defaultid="warehouseid" :warehouseType="warehouseFromType"    @changeware="getfromWarehouse" defaultValue="only" :isform="true" />
									     <el-checkbox v-model="form.withoutInv">不锁定库存</el-checkbox>
									 </el-space>
								</el-form-item>
							 </el-col>
							 <el-col :span="12">
								<el-form-item label="供应商" prop="formsupplier">
									 <el-space>
										 <el-input @click="selectSupplier" v-model="form.suppliername" placeholder="选择供应商" ></el-input>
									 </el-space>
								</el-form-item>
							 </el-col>
							
							 <el-col :span="12">
								  <el-form-item label="备注"  >
									<el-input class="in-wi-24" type="textarea" v-model="form.remark" placeholder="输入备注"> </el-input>
								  </el-form-item>
							 </el-col>
							 
							 <el-col :span="12">
								  <el-form-item label="组装后换货"  >
									 <el-input
									 @click="handleMainAdd" 
									 class="in-wi-24"  v-model="form.sku" placeholder="请选择主SKU"> </el-input>
									 <p class="text-red">选择后会自动拆分主SKU并新建组装单,如果主SKU已经拆分了,请勿再次选择主SKU.</p>
								  </el-form-item>
							 </el-col>
							 <el-col :span="12">
								  <el-form-item label="附件"  >
								 <el-upload
								   
								    :drag="true"
								    action
								    :http-request="uploadFiles"
								    :limit="1"
								    :before-upload="beforeUpload" 
								    :show-file-list="true" 
								    :headers="headers" 
								    accept=".jpg,.png,.jpeg,.bmp,.gif"
								    :multiple="false"
								   >
								    <!-- <el-icon class="font-large"><upload-filled /></el-icon> -->
								     <div class="el-upload__text">
								      拖拽文件到此处或 <em>点击上传</em>
									  支持最大5M的文件
								     </div>
								   </el-upload>
								   </el-form-item>
							 </el-col>
							 
							  </el-row>
						 </el-form>
						
						 <el-divider></el-divider>
						 <div class="mark-re">
							<div>
						   <h5 >换货商品 </h5>
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
							 <el-table-column label="换货数量">
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
									<el-button type="primary" @click.stop="submitForm('submit')">提交</el-button>
								</el-space>
							</div>
						</div>
	</div>
	<!-- :isfulfillable="true" --> 
	<MaterialDialog @getdata="getRows" 
					:warehouseid="form.fromwarehouseid" 
					ref = "MaterialRef"
					:mainid="form.mainid"
					/>
	<SupplierDialog ref='supDiaRef' @getdata="getSupplierRows" />
	<OrderDialog ref='orderDiaRef' @change="getChangeRow" />
	<MaterialDialog ref = "assMaterialRef" :isAssemblySKU="true" @getdata="getdata"  />
</template>

<script setup>
	import {ArrowDown,Edit} from '@element-plus/icons-vue'
	import {Plus,Minus,Help} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,inject,toRefs,getCurrentInstance } from 'vue'
	import {ElMessage } from 'element-plus'
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import OrderDialog from "@/views/erp/purchase/orders/order_dialog.vue";
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue";
	import { useRoute,useRouter } from 'vue-router';
	import {getDictItemsByTypeCode} from "@/api/sys/admin/dict.js";
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import Warehouse from '@/components/header/warehouse.vue';
	import serialnumberApi from '@/api/erp/common/serialnumberApi.js';
	import changeApi from '@/api/erp/purchase/change/changeApi.js';
	import purchaselistApi from '@/api/erp/purchase/form/listApi.js';
	import inventoryApi from "@/api/erp/inventory/inventoryApi.js";
	const MaterialRef = ref();
	const assMaterialRef=ref();
	const supDiaRef =ref();
	const orderDiaRef=ref();
	const warehouseRef=ref();
	const router = useRouter();
	const purchaseentryid = router.currentRoute.value.query.purchaseentryid;
	const { proxy } = getCurrentInstance();
	const emitter = inject("emitter"); // Inject `emitter`
	function closeTab(){
		emitter.emit("removeTab", 100);
	};
	function selectPurchase(){
		orderDiaRef.value.show();
	}
	const state = reactive({
		form:{
			number:'',
			fromwarehouseid:'',
			towarehouseid:'',
			remark:'',
			arrivaltime:new Date(),
			ftype:0,
			suppliername:'',
			supplierid:null,
			entryNumber:'',
			entryid:null,
			uploadVisible:false,
			
		},
		myfile:null,
		warehouseid:"",
		warehouseFromType:"self_usable",
		warehouseToType:"self_usable",
		rules: {
			fromwarehouse: [{ required: true, message: '发货仓库不能为空', trigger: 'blur' }],
		},
		tableData:[],
		skulist:{},
		dispatchFormTypeOptions:[],
	})
	const {
		form,
		rules,
		myfile,
		warehouseid,
		dispatchFormTypeOptions,
		warehouseFromType,
		warehouseToType,
		tableData,
		skulist,
	}=toRefs(state)
	
	function getSupplierRows(row){
		 state.form.suppliername=row.name;
		 state.form.supplierid=row.id; 
	}
	
	function selectSupplier(){
		supDiaRef.value.show();
	}
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
	function getdata(row){
		if(row && row.length==1){
			state.form.sku = row[0].sku;
			state.form.mainid=row[0].id;
			//通过主SKUid 仓库id去查找子产品信息
			//loadSub();
			state.tableData=[];
			 
		}else{
			ElMessage.error("只能选择一个主SKU！ ")
		}
	}
	function handleMainAdd(){
		assMaterialRef.value.show()
	}
    function handleAdd(){
		MaterialRef.value.show();
	}
	function getChangeRow(row){
		state.form.entryid=row.id;
		state.tableData=[];
		row.id=row.materialid;
		row.name=row.mname;
		state.tableData.push(row);
		state.form.fromwarehouseid=row.warehouseid;
		state.warehouseid=row.warehouseid;
		state.form.entryNumber=row.number+"-"+row.sku;
		state.form.suppliername=row.suppliername;
		state.form.supplierid=row.supplier;
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
		state.tableData.forEach(function(item){
			 inventoryApi.getSelfInvDetail({ warehosueid:wid, materialid:item.materialid}).then(res=>{
				  item.fulfillable=res.data.fulfillable;
			 })
		});
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
		var isok=getSKUList();
		if(isok==true){
			var data={};
			data.skumapstr=JSON.stringify(state.skulist);
			if(state.form.remark){
			     data.remark=state.form.remark;
			}
			data.warehouseid=state.form.fromwarehouseid;
			if(state.form.supplierid){
			     data.supplierid=state.form.supplierid;
			}
			if(state.form.entryid){
			   data.entryid=state.form.entryid;
			}
			if(state.form.mainid){
				data.mainid=state.form.mainid;
			}
			data.withoutInv=state.form.withoutInv;
			data.id="";
			let formData = new FormData();
			formData.append('file',state.myfile);
			for (const [key, value] of Object.entries(data)) {
			  formData.append(key, value);
			}
			changeApi.saveData(formData).then((res)=>{
				isok=true;
				if(res.data){
					ElMessage.success( "添加成功！");
					if(res.data){
						router.push({
							path:"/erp/purchase/change",
							query:{
								title:'换货单',
								path:"/erp/purchase/change",
								replace:true,
								refresh:true,
							},
						})
					}
				}else{
					ElMessage.error(res.data);
				}
			});
		}
	}
	function load(){
		getDictItemsByTypeCode('dispatch_form_type').then((response) => {
			   if(response.data){
				   response.data.forEach(item=>{item.value=parseInt(item.value);})
			   }
					state.dispatchFormTypeOptions = response.data;
		});
		
		serialnumberApi.getSerialNumber({"ftype":"SO","isfind":"true"}).then((res)=>{
			if(res.data){
				state.form.number=res.data;
			}
		});
		if(purchaseentryid!="" && purchaseentryid!=undefined && purchaseentryid!=null){
			setTimeout(function(){
				purchaselistApi.list({"entryid":purchaseentryid,"needinventory":true}).then((res)=>{
					if(res.data && res.data.records && res.data.records.length>0){
						 getChangeRow(res.data.records[0]);
					}
				});
			},800)
		}
	}
	function isEmptyObject(e) {  //判断一个Object对象{}是否为空
	    var t;  
	    for (t in e)  
	        return !1;  
	    return !0  
	} 
	function beforeUpload(file){
		if (file.type != "" || file.type != null || file.type != undefined){
		    //截取文件的后缀，判断文件类型
			const FileExt = file.name.replace(/.+\./, "").toLowerCase();
			//计算文件的大小
			const isLt5M = file.size / 1024  < 5000; //这里做文件大小限制
			//如果大于50M
			if (!isLt5M) {
				ElMessage.error('上传文件大小不能超过 5MB!!');
				return false;
			}
			else {
			   return true;
			}
		}
	}
	function uploadFiles(item){
		//上传文件的需要formdata类型;所以要转
		state.myfile=item.file;
	}
	
	
	onMounted(()=>{
		load();
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

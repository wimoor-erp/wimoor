<template>
	  <el-dialog v-model="visible" 
	  title="订单详情" 
	  width="70%">
          <el-row  >
			  <el-col :span="12">
			    <el-form :column="2" label-width="auto">
			      <el-form-item label="店铺-国家"> <Group style="width:330px;" ref="groups" defaultValue="onlyEU" @change="groupChange" /></el-form-item>
			      <el-form-item label="供应商">
							<el-input  style="width:380px;" @click.stop="selectSubSupplier(orderData)"  :readonly="true"  v-model="orderData.supplier" placeholder="请选择供应商">
							</el-input> 
				  </el-form-item>
	<!-- 		      <el-form-item label="预计到货日期">
					  <el-date-picker style="width:130px;"
					         v-model="orderData.preArrivalDate"
					         type="date"
					         placeholder="选择日期"
					         :shortcuts="shortcuts"
					       />
				  </el-form-item> -->
				  <el-form-item label="币种">
				  					 <el-select  style="width:380px;" v-model="orderData.currency" @change="orderData.currencyMark=getCurrencyMark(orderData.currency)">
										 <el-option key="RMB" label="RMB" value="RMB"></el-option>
										 <el-option key="USD" label="USD" value="USD"></el-option>
										 <el-option key="EUR" label="EUR" value="EUR"></el-option>
										 <el-option key="GBP" label="GBP" value="GBP"></el-option>
									 </el-select> 
				  </el-form-item>
				  <el-form-item label="备注">
					 <el-input  style="width:380px;" :rows="2"
						type="textarea" v-model="orderData.remark"></el-input> 
				  </el-form-item>
			    </el-form>
				</el-col>
				 
		  </el-row>
		  <div class="flex-between">
             <div></div>
			 <div style="padding-bottom:10px">
				 <el-button @click="handleAdd" type="primary">添加产品</el-button>
			 </div>
		  </div>

		  <el-table :data="orderData.entryList">
			  <el-table-column width="60" label="图片" prop="image">
				  <template #default='scope'>
				  	<el-image	v-if="scope.row.image" :src="scope.row.image" class="product-image"></el-image>
				  	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-image"  ></el-image>
				  </template>
			  </el-table-column>
			  <el-table-column label="名称" width="170"  show-overflow-tooltip>
				  <template #default='scope'>
					  <div>{{scope.row.name}} </div>
				 </template>
			  </el-table-column>
			  <el-table-column label="本地SKU" width="120"   >
					  <template #default='scope'>
						  <el-input class="font-extraSmall" placeholder="本地SKU" v-model="scope.row.msku"> </el-input>
					 </template>
			  </el-table-column>
			  <el-table-column label="平台SKU"  width="120"   >
					  <template #default='scope'>
						  <el-input class="font-extraSmall" placeholder="平台SKU" v-model="scope.row.sku"> </el-input>
					 </template>
			  </el-table-column>
			<el-table-column label="采购单价"  width="120" prop="price">
							  <template #default='scope'>
							  					  <div v-if="!orderData.auditstatus" class="table-edit-flex">
							  						  <el-space v-if="!scope.row.edit_price">
							  						  <span>{{scope.row.price||'-'}}</span>
							  						  <el-icon
							  						  @click="handleEdit(scope.row,'price')"
							  						  ><edit></edit></el-icon>
							  						  </el-space>
							  						  <el-input
							  						  :ref="(el) => getcellRef(el,scope.row,'price')"
							  						   v-else
													   @input="handlePriceChange(scope.row)"
							  						   type="number"
							  						   v-model="scope.row.price">
							  						   </el-input>
							  	 </div>
								 <div v-else>{{scope.row.price}}</div>
							  </template>
			</el-table-column>
			<el-table-column  width="120">
							 <template #header>
								 <span>生产周期(天)</span>
							 </template>
							  <template #default='scope'>
								  <div v-if="orderData.auditstatus==0" class="table-edit-flex">
									  <el-space v-if="!scope.row.edit_pday">
									  <span>{{scope.row.pday||'-'}}</span>
									  <el-icon
									  @click="handleEdit(scope.row,'pday')"
									  ><edit></edit></el-icon>
									  </el-space>
									  <el-input
									  :ref="(el) => getcellRef(el,scope.row,'pday')"
									   v-else
									   @blur="scope.row.edit_pday=false"
									   type="number"
									   v-model="scope.row.pday">
									   </el-input>
								  </div>
								   <div v-else>{{scope.row.pday}}</div>
							  </template>
			</el-table-column>
			<el-table-column label="海运周期(天)"  width="120">
							  <template #default='scope'>
							  					  <div v-if="orderData.auditstatus==0" class="table-edit-flex">
							  						  <el-space v-if="!scope.row.edit_shipday">
							  						  <span>{{scope.row.shipday||'-'}}</span>
							  						  <el-icon
							  						  @click="handleEdit(scope.row,'shipday')"
							  						  ><edit></edit></el-icon>
							  						  </el-space>
							  						  <el-input
							  						  :ref="(el) => getcellRef(el,scope.row,'shipday')"
							  						   v-else
							  						   @blur="scope.row.edit_shipday=false"
													   type="number"
							  						   v-model="scope.row.shipday">
							  						   </el-input>
							  					  </div>
												   <div v-else>{{scope.row.shipday}}</div>
							  </template>
			</el-table-column>
			<el-table-column label="上架周期(天)" width="120">
							  <template #header>
							  					 <el-space :size="4">
							  					 <span>上架周期(天)</span>
												 <el-tooltip 
												 placement="top"
												 content="产品入库后上架售卖需要的时间">
													 <el-icon
													  :size="14"><Warning /></el-icon>
												 </el-tooltip>
							  					 </el-space>
							  </template>
							  <template #default='scope'>
							  					  <div v-if="orderData.auditstatus==0" class="table-edit-flex">
							  						  <el-space v-if="!scope.row.edit_upday">
							  						  <span>{{scope.row.upday||'-'}}</span>
							  						  <el-icon
							  						  @click="handleEdit(scope.row,'upday')"
							  						  ><edit></edit></el-icon>
							  						  </el-space>
							  						  <el-input
							  						  :ref="(el) => getcellRef(el,scope.row,'upday')"
							  						   v-else
							  						   @blur="scope.row.edit_upday=false"
													    type="number"
							  						   v-model="scope.row.upday">
							  						   </el-input>
							  					  </div>
												   <div v-else>{{scope.row.upday}}</div>
							  </template>
			</el-table-column>
			  <el-table-column label="计划生产" prop="planqty"></el-table-column>
			  <el-table-column label="实际下单数" prop="quantity">
				  <template #default='scope'>
					  <span
					  v-if="orderData.auditstatus>0"
					  >{{scope.row.quantity}}</span>
					  <el-input 
					   v-else
					   @input="handleAmountChange(scope.row)"
					  v-model="scope.row.quantity" > </el-input>
				  </template>
			  </el-table-column>
			  <el-table-column width="60" label="操作">
				  <template #default='scope'>
					  <el-link v-if="orderData.auditstatus===0||orderData.auditstatus===undefined" type="primary" @click="removeItem(scope.row,scope.$index)" :underline="false">删除</el-link>
					  <el-link v-else disabled :underline="false">删除</el-link>
				  </template>
			  </el-table-column>
		  </el-table>
		  <el-row>
			  <el-col :span="6" :offset="18">
				  <div class="summary-box">
				  <el-space  fill
				    style="width: 100%"
				    >
				  <div class="flex-center-between">
					  <div >下单总数：</div>
					  <div class="font-bold">{{orderData.totalAmount}}</div>
				  </div>
				  <div class="flex-center-between">
					  <div >实际采购金额：</div>
					  <div class="font-bold font-large text-orange">
						  <span class="font-small">{{orderData.currencyMark}}</span>{{outputmoney(orderData.totalprice)}}
						  </div>
				  </div>
				  <div class="flex-center-between">
				  					  <div >已付定金：</div>
									  <div v-if="orderData.auditstatus==0">
										  <div 
										  v-if="depositEdit"
										  @click="editDeposit"
										  class="font-bold">{{orderData.currencyMark}} {{outputmoney(orderData.depositfee)}} <el-icon class="pointer">
											  <edit></edit>
										  </el-icon></div>
										  <el-input 
										  v-else
										  ref="depositRef"
										  style="width:80px"
										  size="small"
										  @blur="depositEdit=true"
										  v-model="orderData.depositfee"
										  type="number"
										  >
										  <template #suffix>{{orderData.currencyMark}}</template>
										  </el-input>
									</div>
									<div v-else>{{orderData.currencyMark}} {{outputmoney(orderData.depositfee)}}</div>
				  </div>
	<!-- 			  <div
				   v-if="orderData.auditstatus>=0"
				   class="flex-center-between">
				  					  <div >待付尾款：</div>
				  					  <div class="font-bold">{{orderData.currencyMark}} {{outputmoney(orderData.needamount)}}</div>
				  </div> -->
				 <!-- <div 
				  class="flex-center-between">
				  					  <div >运费：</div>
				  					  <div class="font-bold font-400 ">
										  <el-space>
									 <span v-if="orderData.auditstatus==0" class="light-font">{{orderData.currencyMark}} {{outputmoney(orderData.shipfee)}}</span>
									 <el-popover  v-if="orderData.auditstatus==0"
									 trigger="click"
									 placement="left"
									  :visible="visibles"
									 >
										       <template #reference>
										        <el-icon 
												@click="visibles = true"
												class="pointer"
										        ><edit></edit></el-icon>
										       </template>
					                 <el-input v-model="orderData.shipfee">
										 <template #prefix>{{orderData.currencyMark}}</template>
									 </el-input>
									 <div class="m-t-8 text-right">
									 <el-button
									  @click="visibles = false"
									  size="small">确认</el-button>
									 </div>
									  </el-popover>
									   <span v-else>{{orderData.currencyMark}} {{outputmoney(orderData.shipfee)}}</span>
									  </el-space>
									  </div>
				  </div> -->
				  </el-space>
				  </div>
			  </el-col>
		  </el-row>
		  <template #footer>
			  <div class="flex-center-between">
				  <div></div>
				  <el-button type="primary"   @click="saveForm(0)">创建订单</el-button>
			  </div>
		  </template>
	  </el-dialog>
	   <SupplierDialog ref="subsupplierRef" @getdata="getSubSupplierRows"  />
	   <MaterialDialog ref = "materialRef"    @getdata="getRows" />
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs, nextTick,} from 'vue';
	import {Edit,Warning,UploadFilled,ArrowDown,ArrowDownBold} from '@element-plus/icons-vue';
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {formatFloat,CheckInputInt,CheckInputFloat,outputmoney,debounce,dateFormat,getValue,dateTimesFormat} from '@/utils/index.js';
	import {FilePdf,FileWord,} from '@icon-park/vue-next';
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue";
	import OverseasCreateDialog from "./overseas_create_dialog.vue";
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import FBADialog from "./fba_dialog.vue";
	import Group from '@/components/header/group.vue';
	import purchaseEditApi from '@/api/erp/purchase/form/editApi.js';
	import productformApi from "@/views/customized/chelsea/api/productionform/productformApi.js";
	import inventoryApi from "@/views/customized/chelsea/api/inventory/inventoryApi.js";
	import {getCurrencyMark} from '@/utils/index.js';
	const emit = defineEmits(["change"]);
	const depositRef =ref();
	const overseasCreateDialogRef=ref();
	const fbaDialogRef=ref();
	const subsupplierRef = ref();
	const materialRef=ref();
	const state = reactive({
		orderData:{
			entryList:[],
			currency:"RMB"
		},
		depositEdit:true,
		visible:false,
		visibles:false,
		payVisible:false,
		fileVisible:false,
		fileRadio:'Word',
		shippingFee:0,
		active:'english',
		form:{
		 debt:5000,	
		},
		queryParams:{},
		globalForm:{},
	})
	const {
		active,
		orderData,
		fileRadio,
		payVisible,
		fileVisible,
		visible,
		visibles,
		dataList,
		shippingFee,
		depositEdit,
		form,queryParams,
		globalForm,
	} = toRefs(state)
	const inputRefs = ref({});
	
	function changePayAmount(form){
		form.needamount=CheckInputFloat(form.needamount);
		state.form.totalfee=parseFloat(state.form.needamount)+parseFloat(state.form.depositfee);
	}
	function showPayModal(){
		state.payVisible=true;
		state.form.needamount=state.orderData.needamount;
		state.form.depositfee=state.orderData.depositfee;
		state.form.totalfee=state.form.needamount+state.form.depositfee;
	}
	function handleAdd(){
		materialRef.value.show();
	}
	function getRows(rows){
		var groupid=state.queryParams.groupid;
		var marketplaceid=state.queryParams.marketplaceid;
		if(rows && rows.length>0){
			var skus="";
			state.orderData.entryList.forEach(function(datas){
				 skus+=(datas.sku)+(",");
			});
			rows.forEach(function(item){
				if(skus.indexOf((item.sku+","))<0){
					var items={};
					items.groupid=groupid;
					items.marketplaceid=marketplaceid;
					items.name=item.name;
					items.image=item.image;
					items.pday=state.globalForm.pday;
					items.planqty=0;
					items.quantity=0;
					items.shipday=state.globalForm.shipday;
					items.upday=state.globalForm.upday;
					items.summaryprice=0;
					items.warningText="";
					items.msku=item.sku;
					items.sku=item.sku;
					items.price=item.price;
					items.supplier=item.supplier;
					items.supplierid=item.supplierid;
					items.materialid=item.id;
					state.orderData.entryList.push(items);
				}
			});
		}
		 
	}
	
	function loadSetting(){
		inventoryApi.getGlobalSetting().then((res)=>{
			   if(res.data){
				   state.globalForm=res.data;
			   }else{
				   state.globalForm={};
			   }
		});
	}
	
	function showOverseaForm(){
		overseasCreateDialogRef.value.show(state.orderData);
	}
 
	function showFBAForm(){
		fbaDialogRef.value.show(state.orderData);
	}
 
	function calcAmount(){
		var amount=0;
		var price=0;
		if(state.orderData.entryList && state.orderData.entryList.length>0){
			state.orderData.entryList.forEach(item=>{
				amount=amount+parseInt(item.quantity);
				price=price+formatFloat(parseInt(item.quantity)*parseFloat(item.price));
			});
			state.orderData.totalAmount=amount;
			state.orderData.totalprice=price;
			if(state.orderData && state.orderData.shipfee){
				state.orderData.totalfee=price+parseInt(state.orderData.shipfee);
			}else{
				state.orderData.totalfee=price;
			}
		}
	}
	
	const handleAmountChange=debounce(function(row){
		row.quantity=CheckInputInt(row.quantity);
		row.summaryprice=row.price*row.amount;
		calcAmount();
		//loadStepPrice(state.orderData.supplierid,row.materialid,row.quantity,row);
	},500); 
	const handlePriceChange=debounce(function(row){
		row.quantity=CheckInputInt(row.quantity);
		row.summaryprice=row.price*row.amount;
		calcAmount();
	},500); 
	async function loadStepPrice(supplierid,materialid,amount,row){
		row.warningText="";
		if(amount>0){
			if(row.boxnum&&amount%parseInt(row.boxnum)>0){
					row.warningText=row.warningText+"当前采购量不满足整箱个数："+row.boxnum;
			}
		    await purchaseEditApi.getPriceBySupplier({"supplierid":supplierid,"materialid":materialid,"amount":amount}).then((res)=>{
			if(res.data){
				var needamount=res.data.needamount;
				if(amount<needamount){
					if(row.warningText){
						row.warningText=row.warningText+"<br/>当前供应商下最低起订量是："+needamount;
					}else{
						row.warningText="当前供应商下最低起订量是："+needamount;
					}
				}
				 console.log(res.data);
				if(res.data.itemprice){
					row.price= res.data.itemprice;
					row.summaryprice=row.price*amount;
				}
				calcAmount();
			}else{
				calcAmount();
			} 
		  });
		} 
	}
	
	function show(){
		state.visible  = true;
		state.orderData.entryList=[];
		state.orderData.remark=null;
		loadSetting();
	}
	
	function getSubSupplierRows(row){
		var name=row.name;
		state.orderData.supplier=name;
		state.orderData.supplierid=row.id;
	}
	
	function selectSubSupplier(row){
		subsupplierRef.value.show(null);
	}
	
	function downloadForm(){
			productformApi.downloadForm({"formid":state.orderData.id,"language":state.active});
	}
	
	function removeItem(row,index){
		if(state.orderData.entryList.length==1){
			ElMessage.error("不支持删除最后一个!");
			return;
		}
	     state.orderData.entryList.splice(index,1);
	}
	
    function getcellRef(el,row,columnName){
		if(el){
			inputRefs.value[`input_${row.sku}_${columnName}`] = el
		}
	}
	
	function handleEdit(row,columnName){
		row['edit_'+columnName]=true;
		nextTick(()=>{
			const input = inputRefs.value[`input_${row.sku}_${columnName}`];
			   if (input) {
			      input.focus();
			    }
		})
	}
	
	function settlePay(){
		state.orderData.payamount=state.form.needamount;
		state.orderData.paystatus = 1;
		saveForm(state.orderData.auditstatus);
	}
	
	function editDeposit(){
		state.depositEdit=false;
		nextTick(()=>{
				depositRef.value.focus();
		})
	}
	
	function saveForm(status){
		state.orderData.auditstatus=status;
		var lists=[];
		state.orderData.needamount=state.orderData.totalfee-state.orderData.depositfee;
		state.orderData.createtime=new Date(state.orderData.createtime);
		state.orderData.auditime=new Date(state.orderData.auditime);
		state.orderData.opttime=new Date(state.orderData.opttime);
		state.orderData.preArrivalDate=new Date(state.orderData.preArrivalDate);
		state.orderData.auditime1=state.orderData.auditime1? new Date(state.orderData.auditime1):null;
		state.orderData.auditime2=state.orderData.auditime2? new Date(state.orderData.auditime2):null;
		state.orderData.auditime3=state.orderData.auditime3? new Date(state.orderData.auditime3):null;
		state.orderData.auditime4=state.orderData.auditime4? new Date(state.orderData.auditime4):null;
		state.orderData.paytime=state.orderData.paytime? new Date(state.orderData.paytime):null;
		state.orderData.groupid=state.queryParams.groupid;
		state.orderData.marketplaceid=state.queryParams.marketplaceid;
		if(state.orderData.entryList && state.orderData.entryList.length>0){
			state.orderData.entryList.forEach(item=>{
				if(item.msku==''){
					item.msku=null;
				}
				if(item.sku==''){
					item.sku=null;
				}
				item.createtime=new Date(item.createtime);
				item.opttime=new Date(item.opttime);
			});
			lists.push(state.orderData);
			productformApi.saveForm(lists).then((res)=>{
				ElMessage.success("操作成功!");
				state.visible=false;
				emit("change");
			});
		}else{
			ElMessage.error("产品列表为空！");
		}
		
	}
	
	function groupChange(obj){
		state.queryParams.groupid=obj.groupid;
		state.queryParams.marketplaceid=obj.marketplaceid;
	}
	
	defineExpose({
		show,
	})
</script>

<style >
	.lage-card{
		width: 120px;
		text-align: center;
		padding-top:4px;
	}
	.lage-card.active{
		border-color: var(--el-color-primary);
	}
	.font-400{
		font-weight: 400;
	}
	.summary-box{
		margin-top:16px;
	}
	.summary-box .text-orange{
		color:var(--el-color-primary)
	}
	/* 步骤条新样式 */
	.new-sty-steps .el-step__head.is-success{
		    color: var(--el-color-primary);
		    border-color: var(--el-color-primary);
	}
	.new-sty-steps .el-step__title.is-success{
		color: var(--el-text-color-primary);
	}
	.new-sty-steps .el-step__description.is-success{
		color: var(--el-text-color-placeholder);
	}
	.new-sty-steps .el-step__head.is-process{
		color: var(--el-text-color-primary);
		border-color: var(--el-color-primary);
	}
	.new-sty-steps .el-step__head.is-process .el-step__icon.is-text{
		background-color: var(--el-color-primary);
		color:var(--el-fill-color-blank) ;
	}
	.new-sty-steps .el-step__title.is-process{
		color: var(--el-color-primary);
		font-weight: 400;
	}
	.new-sty-steps .el-step__icon{
		background:var(--el-fill-color-blank)  ;
	}
	.new-sty-steps .el-step.is-horizontal .el-step__line{
		margin-right:8px;
		margin-left:30px;
	}
</style>
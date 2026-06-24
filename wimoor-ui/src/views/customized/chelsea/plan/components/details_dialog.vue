<template>
	  <el-dialog v-model="visible" 
	  title="订单详情" 
	  width="1000">
          <el-row >
			  <el-col :span="orderData.auditstatus==0?24:12"    >
			    <el-descriptions :column="2">
			      <el-descriptions-item label="订单号">
					  {{orderData.number}}
					   <el-tag v-if="orderData.auditstatus===0" type="warning" effect="plain">待提交</el-tag>
					  <el-tag v-else-if="orderData.auditstatus===1" type="warning" effect="plain">待生产</el-tag>
					  <el-tag v-else-if="orderData.auditstatus===3" type="blue" effect="plain">已出货</el-tag>
					  <el-tag v-else-if="orderData.auditstatus===4" type="success" effect="plain">已结束</el-tag>
					  <el-tag v-else-if="orderData.auditstatus===-1" type="info" effect="plain">已取消</el-tag>
				  </el-descriptions-item>
			      <el-descriptions-item label="付款状态">
					  <span class=" text-warning" v-if="orderData.paystatus===0">待结清</span>
					  <span class=" text-green" v-else>已结清</span>
				  </el-descriptions-item>
			      <el-descriptions-item label="店铺">{{orderData.groupname}}</el-descriptions-item>
			      <el-descriptions-item label="下单日期"  v-if="orderData.auditstatus>0">{{ dateFormat(orderData.auditime1)}}</el-descriptions-item>
			      <el-descriptions-item label="国家">{{orderData.marketname}}</el-descriptions-item>
			  <!--    <el-descriptions-item label="出货日期">{{dateFormat(orderData.auditime3)}}</el-descriptions-item> -->
				  <el-descriptions-item label="币种">
				  <el-select v-if="orderData.auditstatus==0"  v-model="orderData.currency" style="width:100px" @change="orderData.currencyMark=getCurrencyMark(orderData.currency)">
				  										<el-option key="RMB" label="RMB" value="RMB"></el-option>
				  										<el-option key="USD" label="USD" value="USD"></el-option>
				  										<el-option key="EUR" label="EUR" value="EUR"></el-option>
				  										<el-option key="GBP" label="GBP" value="GBP"></el-option>
				  </el-select> 
				
				  <span v-else>  {{orderData.currency}}</span>
				  </el-descriptions-item>
			      <el-descriptions-item label="供应商">
							<el-input style="width:130px;" v-if="orderData.auditstatus==0" @click.stop="selectSubSupplier(orderData)"  :readonly="true"  v-model="orderData.supplier" placeholder="请选择供应商">
							</el-input> 
							<span v-else>{{orderData.supplier}}</span>
				  </el-descriptions-item>
<!-- 			      <el-descriptions-item label="预计到货日期">
					  <el-date-picker style="width:130px;"
					  v-if="orderData.auditstatus==0"  
					         v-model="orderData.preArrivalDate"
					         type="date"
					         placeholder="选择日期"
					         
					       />
					<span v-else>{{dateFormat(orderData.preArrivalDate)}}</span>
				  </el-descriptions-item> -->
				  <el-descriptions-item label="备注">
					 <el-input v-if="orderData.auditstatus==0" :rows="2"	type="textarea" style="width:200px" v-model="orderData.remark"></el-input> 
					 <span v-else>{{orderData.remark}}</span>
				  </el-descriptions-item>
			    </el-descriptions>
				</el-col>
				<el-col :span="12"  v-if="orderData.auditstatus>0" >
					<div class="new-sty-steps">
				  <el-steps   :active="orderData.auditstatus"    finish-status="success">
					<el-step title="待生产" :description="'操作人:'+getValue(orderData.auditor1name)" />
				    <el-step title="已出货" :description="'操作人:'+getValue(orderData.auditor3name)" />
				    <el-step title="已结束" :description="'操作人:'+getValue(orderData.auditor4name)" />
				  </el-steps>
				  </div>
				</el-col>
		  </el-row>
		  <div class="flex-between">
		     <div></div>
		  			 <div style="padding-bottom:10px" v-if="orderData.auditstatus>=0&&orderData.auditstatus<1">
		  				 <el-button @click="handleAdd"   type="primary">添加产品</el-button>
		  			 </div>
		  </div>
		  <el-table :data="orderData.entryList">
			  <el-table-column width="60" label="图片" prop="image">
				  <template #default='scope'>
				  	<el-image	v-if="scope.row.image" :src="scope.row.image" class="product-image"></el-image>
				  	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-image"  ></el-image>
				  </template>
			  </el-table-column>
			  <el-table-column label="名称/SKU" show-overflow-tooltip>
				  <template #default='scope'>
					  <div>{{scope.row.name}} </div>
					  <div class="font-extraSmall" >{{scope.row.msku}}</div>
				 </template>
			  </el-table-column>
			  <el-table-column label="平台SKU" show-overflow-tooltip>
			  				  <template #default='scope'>
			  					  {{scope.row.sku}} 
			  				 </template>
			  </el-table-column>
			<el-table-column label="采购单价"  width="120" prop="price">
							  <template #default='scope'>
							  					  <div v-if="orderData.auditstatus==0" class="table-edit-flex">
							  						  <el-space v-if="!scope.row.edit_price">
							  						  <span>{{scope.row.price||'-'}}</span>
							  						  <el-icon
							  						  @click="handleEdit(scope.row,'price')"
							  						  ><edit></edit></el-icon>
							  						  </el-space>
							  						  <el-input
							  						  :ref="(el) => getcellRef(el,scope.row,'price')"
							  						   v-else
							  						   type="number"
													   @input="handleAmountChange(scope.row)"
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
					  <el-input 
					    v-if="orderData.auditstatus==0"
					   @input="handleAmountChange(scope.row)"
					  v-model="scope.row.quantity" > </el-input>
					  <span v-else>{{scope.row.quantity}}</span>
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
						  <span class="font-small">{{getCurrencyMark(orderData.currency)}}</span>{{outputmoney(orderData.totalPrice)}}
						  </div>
				  </div>
				  <div class="flex-center-between">
				  					  <div >已付定金：</div>
									  <div v-if="orderData.auditstatus>=0">
										  <div 
										  v-if="depositEdit"
										  @click="editDeposit"
										  class="font-bold">{{getCurrencyMark(orderData.currency)}}{{outputmoney(orderData.depositfee)}} <el-icon class="pointer">
											  <edit></edit>
										  </el-icon></div>
										  <el-input 
										  v-else
										  ref="depositRef"
										  style="width:80px"
										  size="small"
										  @blur="depositEdit=true;calcAmount();savePrice()"
										  v-model="orderData.depositfee"
										  type="number"
										  >
										  <template #suffix>{{getCurrencyMark(orderData.currency)}}</template>
										  </el-input>
									</div>
									<div v-else>{{getCurrencyMark(orderData.currency)}}{{outputmoney(orderData.depositfee)}}</div>
				  </div>
				  <div
				   v-if="orderData.auditstatus>=0"
				   class="flex-center-between">
				  					  <div >待付尾款：</div>
				  					  <div class="font-bold">{{getCurrencyMark(orderData.currency)}}{{outputmoney(orderData.needamount)}}</div>
				  </div>
	<!-- 			  <div 
				  class="flex-center-between">
				  					  <div >运费：</div>
									  <div v-if="orderData.auditstatus>=0">
									  	  <div 
									  	  v-if="shipfeeEdit"
									  	  @click="editShipfee"
									  	  class="font-bold">{{getCurrencyMark(orderData.currency)}} {{outputmoney(orderData.shipfee)}} <el-icon class="pointer">
									  		  <edit></edit>
									  	  </el-icon></div>
									  	  <el-input 
									  	  v-else
									  	  ref="shipfeeRef"
									  	  style="width:80px"
									  	  size="small"
									  	  @blur="shipfeeEdit=true;calcAmount();savePrice()"
									  	  v-model="orderData.shipfee"
									  	  type="number"
									  	  >
									  	  <template #suffix>{{getCurrencyMark(orderData.currency)}}</template>
									  	  </el-input>
									  </div>
				  					  
				  </div> -->
				 
				  </el-space>
				  </div>
			  </el-col>
		  </el-row>
		  <template #footer>
			  <div class="flex-center-between">
				  <el-space>
				  <el-button 
				  v-if="orderData.auditstatus>0"
				  @click="fileVisible=true"
				  type="success">
					  <el-icon :size="16"><UploadFilled /></el-icon> 
					  &nbsp;导出订单合同</el-button>
					    <el-popover :visible="btnvisible"  v-if="orderData.auditstatus>=1" placement="top" :width="180">
					      <p>您确定要撤回订单吗?</p>
					      <div style="text-align: right; margin: 0">
					        <el-button size="small" text @click="btnvisible = false">取消</el-button>
					        <el-button size="small" type="primary" @click="saveForm(0);btnvisible = false">
					          确认
					        </el-button>
					      </div>
					      <template #reference>
					        <el-button @click="btnvisible = true" type="danger">撤回</el-button>
					      </template>
					    </el-popover>
					
					  </el-space>
				  <div>
				  <el-space >
					  <el-button
					   @click="visible=false"
					   type="defalut" >关闭</el-button>
					   <div v-if="orderData.auditstatus>0">
					  <el-button
					   @click="showPayModal"
					   type="primary" plain>
					   <span v-if="orderData.paystatus===0">支付尾款</span>
					   <span v-else>付款记录</span>
					   </el-button>
					 <el-button type="primary" plain  @click="showOverseaForm()">发货到海外仓</el-button>
					  <el-button type="primary" plain @click="showFBAForm()">发货到FBA</el-button>
					  </div>
					  <el-button type="primary" v-if="orderData.auditstatus===0||orderData.auditstatus===undefined" @click="saveForm(1)">下单生产</el-button>
					  <!-- <el-button type="primary" v-if="orderData.auditstatus===1" @click="saveForm(2)" >完成生产</el-button>  -->
					   <el-button type="primary" v-if="orderData.auditstatus===2||orderData.auditstatus===1" @click="saveForm(3)" >出货</el-button>  
					   <el-button type="primary" v-if="orderData.auditstatus===3" @click="saveForm(4)" >完成订单</el-button>
				  </el-space>

				  </div>
			  </div>
		  </template>
	  </el-dialog>
	  <el-dialog
	  :modal="false"
	   v-model="payVisible"
	   width="320px"
	   :title="orderData.paystatus===0?'付款确认':'付款记录'" >
		<el-form :model="form" label-position="top">
			<el-form-item :label="orderData.paystatus===0?'待付尾款':''">
				<el-input
				 v-if="orderData.paystatus===0"
				 v-model="form.needamount" @input="changePayAmount(form)">
					 <template #prefix>￥</template>
				</el-input>
				<div 
				v-else
				class="summary-box light-font" style="width: 100%;">
					<div class="flex-center-between" >
										  <div >操作人：</div>
										  <div class="">{{orderData.payname}}</div>
					</div>
					<div class="flex-center-between" >
										  <div >操作时间：</div>
										  <div class="">{{dateTimesFormat(orderData.paytime)}}</div>
					</div>
					<div class="flex-center-between" >
										  <div > </div>
										  <div class="">
											  <el-popover :visible="pricevisible"   placement="top" :width="180">
											    <p>您确定要撤回付款吗?</p>
											    <div style="text-align: right; margin: 0">
											      <el-button size="small" text @click="pricevisible = false">取消</el-button>
											      <el-button size="small" type="primary" @click="orderData.paystatus=0;savePrice();pricevisible = false">
											        确认
											      </el-button>
											    </div>
											    <template #reference>
											      <el-button size="small" @click="pricevisible = true" type="info">撤回付款</el-button>
											    </template>
											  </el-popover>
										  </div>
					</div>
					
				 
				</div>
				<div class="summary-box " style="width: 100%;">
					<div
					 v-show="orderData.paystatus===1"
					class="flex-center-between" >
										  <div >已付尾款：</div>
										  <div class="font-bold">{{getCurrencyMark(orderData.currency)}}{{outputmoney(orderData.payamount)}}</div>
					</div>
				<div class="flex-center-between" >
									  <div >已付定金：</div>
									  <div class="font-bold">{{getCurrencyMark(orderData.currency)}}{{outputmoney(form.depositfee)}}</div>
				</div>
				<div class="flex-center-between">
									  <div >合计金额：</div>
									 <div class="font-bold font-large text-orange">
									 	 <span class="font-small">{{getCurrencyMark(orderData.currency)}}</span>{{getTotalFee(orderData,form)}}
						</div>
						</div>
				</div>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="payVisible=false">关闭</el-button>
			<el-button 
			v-show="orderData.paystatus===0"
			@click="settlePay"
			type="primary">确认支付</el-button>
		</template>
	  </el-dialog>
	  <el-dialog 
	  	  v-model="fileVisible"
	  	  width="480px"
	  	  title="导出合同">
	  		  <p>文件格式</p><br/>
	  			  <el-row>
	  				  <el-col :span="24">
	  			  <el-space>
	  				  <el-card 
	  				  @click="active='chinese'"
	  				  shadow="none" class="pointer lage-card" :class = "active==='chinese'&&'active'">
	  					  <el-space :size="4" direction="vertical">
	  					  <file-word   theme="filled" size="40" :fill="active==='chinese'?'#ff7315':'#999'" />
	  					  <span>中文</span>
	  					  </el-space>
	  				  </el-card>
	  				  <el-card 
	  				  @click="active='english'"
	  				  shadow="none" class="pointer lage-card" :class = "active==='english'&&'active'">
	  					  <el-space :size="4" direction="vertical">
	  					  <file-word   theme="filled" size="40" :fill="active==='english'?'#ff7315':'#999'"/>
	  					  <span>英文</span>
	  					  </el-space>
	  				  </el-card>
	  			  </el-space>
	  			  </el-col>
	  			  </el-row>
	  			  <template #footer>
	  			   <div class="flex-center-between">
	  				   <div> 
	  				  <el-button @click="fileVisible=false">取消</el-button>
	  				  <el-button type="primary" @click="downloadForm">下载合同</el-button>
	  				  </div>
	  				  </div>
	  			  </template>
	  	  </el-dialog>
	   <SupplierDialog ref="subsupplierRef" @getdata="getSubSupplierRows"  />
	   <OverseasCreateDialog ref="overseasCreateDialogRef" @save="handleOverseaSave" />
	   <FBADialog ref="fbaDialogRef" @save="handleFbaSave"/>
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
	import FBADialog from "./fba_dialog.vue";
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue"
	import purchaseEditApi from '@/api/erp/purchase/form/editApi.js';
	import productformApi from "@/views/customized/chelsea/api/productionform/productformApi.js";
	import inventoryApi from "@/views/customized/chelsea/api/inventory/inventoryApi.js";
	import {getCurrencyMark} from '@/utils/index.js';
	const emit = defineEmits(["change"]);
	const depositRef =ref();
	const shipfeeRef=ref();
	const materialRef=ref();
	const overseasCreateDialogRef=ref();
	const fbaDialogRef=ref();
	const subsupplierRef = ref();
	const state = reactive({
		orderData:{},
		depositEdit:true,
		shipfeeEdit:true,
		visible:false,
		visibles:false,
		btnvisible:false,
		payVisible:false,
		fileVisible:false,
		pricevisible:false,
		fileRadio:'Word',
		shippingFee:0,
		active:'english',
		form:{
		 debt:5000,	
		},
		globalForm:{},
		queryParams:{},
	})
	const {
		active,
		orderData,
		fileRadio,
		payVisible,
		fileVisible,
		pricevisible,
		btnvisible,
		visible,
		visibles,
		dataList,
		shippingFee,
		depositEdit,
		shipfeeEdit,
		form,
		globalForm,
		queryParams,
	} = toRefs(state)
	const inputRefs = ref({});
	
	function getTotalFee(row,form){
		 return outputmoney(getNumic(row.payamount)+getNumic(form.depositfee));
	}
	
	function changePayAmount(form){
		form.needamount=CheckInputFloat(form.needamount);
		state.form.totalfee=parseFloat(state.form.needamount)+parseFloat(state.form.depositfee);
	}
	function showPayModal(){
		state.payVisible=true;
		state.form.needamount=state.orderData.needamount;
		state.form.depositfee=state.orderData.depositfee;
		state.form.totalfee=parseFloat(state.form.needamount)+parseFloat(state.form.depositfee);
	}
	
	function showOverseaForm(){
		overseasCreateDialogRef.value.show(state.orderData);
	}
	function showFBAForm(){
		fbaDialogRef.value.show(state.orderData);
	}
	function getNumic(value){
		if(value){
			return parseFloat(value);
		}else{
			return 0;
		}
	}
	function savePrice(){
		if(state.orderData.auditstatus>=1){
			saveForm(state.orderData.auditstatus);
		}
	}
	function handleOverseaSave(){
		if(state.orderData.auditstatus==2){
			saveForm(3);
		}
	}
	function handleFbaSave(){
		if(state.orderData.auditstatus==2){
			saveForm(3);
		}
	}
	function calcAmount(){
		var amount=0;
		var price=0;
		if(state.orderData.entryList && state.orderData.entryList.length>0){
			state.orderData.entryList.forEach(item=>{
				amount=amount+parseInt(item.quantity);
				price=price+formatFloat(item.quantity*item.price);
			});
			state.orderData.totalAmount=amount;
			state.orderData.totalPrice=price;
			if(state.orderData.shipfee){
				state.orderData.totalfee=price+parseFloat(state.orderData.shipfee) ;
				state.orderData.needamount=state.orderData.totalfee-getNumic(state.orderData.depositfee);
			}else{
				state.orderData.totalfee=price;
				state.orderData.needamount=state.orderData.totalfee-getNumic(state.orderData.depositfee);
			}
		}
	}
	
	const handleAmountChange=debounce(function(row){
		row.quantity=CheckInputInt(row.quantity);
		row.summaryprice=row.price*row.quantity;
		calcAmount();
		//loadStepPrice(state.orderData.supplierid,row.materialid,row.quantity,row);
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
	
	function show(rowid){
		state.visible  = true;
		state.orderData.id = rowid;
		loadData();
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
		state.payVisible=false;
	}
	function editDeposit(){
		state.depositEdit=false;
		nextTick(()=>{
				depositRef.value.focus();
		})
	}
	function editShipfee(){
		state.shipfeeEdit=false;
		nextTick(()=>{
				shipfeeRef.value.focus();
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
		state.orderData.entryList.forEach(item=>{
			item.createtime=new Date(item.createtime);
			item.opttime=new Date(item.opttime);
			item.oktime=new Date(item.oktime);
		});
		lists.push(state.orderData);
		productformApi.saveForm(lists).then((res)=>{
			ElMessage.success("操作成功!");
			loadData();
			emit("change");
		});
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
					items.price=item.price;
					items.supplier=item.supplier;
					items.supplierid=item.supplierid;
					items.materialid=item.id;
					state.orderData.entryList.push(items);
					loadStepPrice(state.orderData.supplierid,items.materialid,items.quantity,items);
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
	
	function loadData(){
		productformApi.listForm({"paramother":{"id":state.orderData.id}}).then((res)=>{
			if(res.data.records && res.data.records.length>0){
			  state.orderData=res.data.records[0];
			  state.orderData.currencyMark=getCurrencyMark(state.orderData.currency);
			  calcAmount()
			}
		});
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
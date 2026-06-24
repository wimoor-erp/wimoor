<template>
	  <el-dialog v-model="visible" 
	  title="订单详情" 
	  top="3vh"
	  width="80%">
		    <el-steps
		      style="width: 400px"
		      :active="activeStatus"
		      finish-status="success"
		    >
		      <el-step title="查看详情"  @click="activeStatus=0"/>
		      <el-step title="提交"   />
		    </el-steps>
			<div class="flex-between">
			   <div></div>
				 <div style="padding-bottom:10px">
					 <el-button @click="handleAdd" type="primary">添加产品</el-button>
				 </div>
			</div>
		  <el-table v-if="activeStatus==0"  :data="skuList">
			  <el-table-column width="60" label="图片" prop="image">
				  <template #default='scope'>
				  	<el-image	v-if="scope.row.image" :src="scope.row.image" class="product-image"></el-image>
				  	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-image"  ></el-image>
				  </template>
			  </el-table-column>
			  <el-table-column label="名称/SKU" show-overflow-tooltip>
				  <template #default='scope'>
					  <div>{{scope.row.name}} </div>
					  <div class="font-extraSmall" >{{scope.row.sku}}</div>
					  <div v-if="scope.row.warningText" v-html="scope.row.warningText" class="text-warning"> </div>
				 </template>
			  </el-table-column>
			  <el-table-column label="采购单价"  width="120" prop="price">
				  <template #default='scope'>
				  					  <div class="table-edit-flex">
				  						  <el-space v-if="!scope.row.edit_price">
				  						  <span>{{scope.row.price||'-'}}</span>
				  						  <el-icon
				  						  @click="handleEdit(scope.row,'price')"
				  						  ><edit></edit></el-icon>
				  						  </el-space>
				  						  <el-input
				  						  :ref="(el) => getcellRef(el,scope.row,'price')"
				  						   v-else
				  						   @blur="scope.row.edit_price=false"
				  						   type="number"
				  						   v-model="scope.row.price">
				  						   </el-input>
				  	 </div>
				  </template>
			  </el-table-column>
			  <el-table-column  width="120">
				 <template #header>
					 <span>生产周期(天)</span>
				 </template>
				  <template #default='scope'>
					  <div class="table-edit-flex">
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
				  </template>
			  </el-table-column>
			  <el-table-column label="海运周期(天)"  width="120">
				  <template #default='scope'>
				  					  <div class="table-edit-flex">
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
				  					  <div class="table-edit-flex">
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
				  </template>
			  </el-table-column>
			  <el-table-column label="七日销量"  width="120" prop="sales_week"></el-table-column>
			  <el-table-column label="实际下单数"  width="100">
				  <template #default='scope'>
					  <el-input
					  v-model="scope.row.quantity"  @input="handleAmountChange(scope.row)"> </el-input>
				  </template>
			  </el-table-column>
			  <el-table-column label="供应商">
				  <template #default='scope'>
					<el-input @click.stop="selectSubSupplier(scope.row)"  :readonly="true"  v-model="scope.row.supplier" placeholder="请选择供应商">
					  <template #append>
							<el-button @click.stop="selectSubSupplier(scope.row)" :icon="ArrowDownBold" />
					   </template>
					</el-input> 
				  </template>
			  </el-table-column>
			  <el-table-column width="60" label="操作">
				  <template #default='scope'>
					  <el-link   type="primary" :underline="false" @click="removeItem(scope.$index)">删除</el-link>
				  </template>
			  </el-table-column>
		  </el-table>
		  
		  <el-card  style="margin-bottom:10px;" v-if="activeStatus==1"  v-for="forms in dataList">
		      <template #header>
		        <div class="card-header">
					<el-space>
						<div>
							
						</div>
						<el-form :inline="true" >
							<el-form-item label="供应商:">
							{{forms.supplier}}
							</el-form-item>
							<el-form-item label="币种:">
							      <el-select v-model="forms.currency"  style="width:100px">
									  <el-option key="RMB" label="RMB" value="RMB"></el-option>
									  <el-option key="USD" label="USD" value="USD"></el-option>
									  <el-option key="EUR" label="EUR" value="EUR"></el-option>
									  <el-option key="GBP" label="GBP" value="GBP"></el-option>
								  </el-select>  
							</el-form-item>
						    <el-form-item label="总费用:">
						      {{forms.totalfee}}
						    </el-form-item>
						    <el-form-item label="定金:">
						          <el-input v-model="forms.depositfee"  @input="calcTotalPrice(forms)" style="width:100px"></el-input>  
						    </el-form-item>
				<!-- 		    <el-form-item label="运费:">
						     <el-input v-model="forms.shipfee"  @input="calcTotalPrice(forms)" style="width:100px"></el-input>  
						    </el-form-item> -->
							<el-form-item label="预计到货日期:">
							 <el-date-picker
							        v-model="forms.preArrivalDate"
							        type="date"
							        placeholder="选择日期"
							        :shortcuts="shortcuts"
							        
							      />
							</el-form-item>
							<el-form-item label="备注:">
							 <el-input v-model="forms.remark"   ></el-input>  
							</el-form-item>
						  </el-form>
					</el-space>
		          
				  
		        </div>
		      </template>
		      <el-table  :data="forms.entryList"> 
			  <el-table-column width="60" label="图片" prop="image">
			  				  <template #default='scope'>
			  				  	<el-image	v-if="scope.row.image" :src="scope.row.image" class="product-image"></el-image>
			  				  	<el-image v-else :src="$require('empty/noimage40.png')"  class="product-image"  ></el-image>
			  				  </template>
			  </el-table-column>
			  <el-table-column label="名称/SKU" show-overflow-tooltip>
			  				  <template #default='scope'>
			  					  <div>{{scope.row.name}} </div>
			  					  <div class="font-extraSmall" >{{scope.row.sku}}</div>
			  				 </template>
			  </el-table-column>
			  <el-table-column label="生产周期(天)" width="120" prop="pday"> </el-table-column>
			  <el-table-column label="海运周期(天)"  width="120" prop="shipday"></el-table-column>
			  <el-table-column label="上架周期(天)" width="120" prop="upday"></el-table-column>
			  <el-table-column label="采购单价"  width="120" prop="price"></el-table-column>
			  <el-table-column label="七日销量"  width="120" prop="sales_week"></el-table-column>
			  <el-table-column label="实际下单数"  width="100"  prop="quantity"></el-table-column>
			  <el-table-column label="供应商" prop="supplier"></el-table-column>
			  </el-table>
		 </el-card>
		  
		 
		  <el-row v-if="activeStatus==0">
			  <el-col :span="6" :offset="18">
				  <div class="summary-box">
				  <el-space  fill
					style="width: 100%"
					>
				  <div class="flex-center-between">
					  <div >下单总数：</div>
					  <div class="font-bold">{{totalamount}}</div>
				  </div>
				  <div class="flex-center-between">
					  <div >实际采购金额：</div>
					  <div class="font-bold font-large text-orange">
						  <span class="font-small">￥</span>{{outputmoney(totalPrice)}}
						  </div>
				  </div>
				  </el-space>
				  </div>
			  </el-col>
		  </el-row>
		  <template #footer>
		  			  <div class="flex-center-between">
						  <div></div>
		  				  <div>
		  				  <el-button type="primary"  v-if="activeStatus==0" @click="goStep">下一步</el-button>
		  				  <el-button type="primary" v-else  @click="savePlan">生成订单</el-button>
		  				  </div>
		  			  </div>
		  </template>
		 </el-dialog>
	  <SupplierDialog ref="subsupplierRef" @getdata="getSubSupplierRows"  />
	  <MaterialDialog ref = "materialRef"    @getdata="getRows" />
</template>

<script setup>
	import { ref,reactive,onMounted,toRefs, nextTick,} from 'vue'
	import {Edit,Warning,UploadFilled,ArrowDown,ArrowDownBold} from '@element-plus/icons-vue'
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {formatFloat,CheckInputInt,outputmoney,debounce,getCurrencyMark} from '@/utils/index.js';
	import {FilePdf,FileWord,} from '@icon-park/vue-next';
	import SupplierDialog from "@/views/erp/baseinfo/supplier/supplier_dialog.vue";
	import MaterialDialog from "@/views/erp/baseinfo/material/materialDialog.vue";
	import purchaseEditApi from '@/api/erp/purchase/form/editApi.js';
	import productformApi from "@/views/customized/chelsea/api/productionform/productformApi.js";
	const emit = defineEmits(['change',]);
	const materialRef=ref();
	const depositRef =ref();
	const subsupplierRef = ref();
	const state = reactive({
		activeStatus:0,
		 totalamount:0,
		 payprice:0,
		 skuList:[],
		depositEdit:true,
		visible:false,
		shippingFee:0,
		form:{
		 debt:5000,	
		},
		nowRow:{},
		dataList:[],
		totalPrice:0,
		tablerows:[],
		globalForm:{},
	})
	const {
		dataList,
		activeStatus,
		visible,
		shippingFee,
		depositEdit,
		nowRow,
		form,
		skuList,
		totalamount,
		totalPrice,
		payprice,
		tablerows,
		globalForm,
	} = toRefs(state)
	const inputRefs = ref({});
	function removeItem(index){
		if(state.skuList.length==1){
			ElMessage.error("不支持删除最后一个!");
			return;
		}
	     state.skuList.splice(index,1);
		 calcTotalAmount();
	}
	
	
	function handleSupplierData(){
		var lists=[];
		var forms={};
		state.skuList.forEach(item=>{
			var row={};
			if(item.pday){
				item.pday=parseInt(item.pday);
			}
			if(item.shipday){
				item.shipday=parseInt(item.shipday);
			}
			if(item.upday){
				item.upday=parseInt(item.upday);
			}
			var form=forms[item.supplierid];
			if(!form){
				form={entryList:[],totalfee:0,preArrivalDate:null,supplierid:item.supplierid,
				supplier:item.supplier,marketplaceid:item.marketplaceid,groupid:item.groupid};
				forms[item.supplierid]=form;
			}
			var entryList=form.entryList;
			if(!entryList){
				entryList=[];
			}
			form.totalfee=formatFloat(form.totalfee+parseFloat(item.price)*parseInt(item.quantity));
			form.totalPrice=form.totalfee;
			form.currency="RMB";
			entryList.push(item);
		});
		Object.keys(forms).forEach((key)=>{
			var form=forms[key];
			var maxday=0
			form.entryList.forEach(item=>{
				var pday=0;
				var shipday=0;
				if(item.pday){
					pday=parseInt(item.pday);
				}
				if(item.shipday){
					shipday=parseInt(item.shipday);
				}
				if(maxday<(pday+shipday)){
					maxday=pday+shipday;
				}
			});
			var date=new Date();
			date.setDate(date.getDate() + maxday);
			form.preArrivalDate=date;
			form.currency="RMB";
			lists.push(form);
		})
		state.dataList=lists;
	}
	
	
	function goStep(){
		if(state.skuList && state.skuList.length>0){
			state.activeStatus=1;
			handleSupplierData();
		}else{
			ElMessage.error("请至少选择一个数据行!");
			return;
		}
	
	}
	function savePlan(){
		productformApi.saveForm(state.dataList).then((res)=>{
			state.visible  = false;
			ElMessage.success("生成订单成功");
			 emit("change");
		});
	}
	
	function calcTotalPrice(form){
		if(form.shipfee){
			form.totalfee=parseFloat(form.totalPrice)+parseFloat(form.shipfee);
		}else{
			form.totalfee=parseFloat(form.totalPrice);
		}
		if(form.depositfee){
			form.needamount=parseFloat(form.totalfee)-parseFloat(form.depositfee);
		}else{
			form.needamount=parseFloat(form.totalfee);
		}
	}
	const handleAmountChange=debounce(function(row){
		row.quantity=CheckInputInt(row.quantity);
		loadStepPrice(row.supplierid,row.materialid,row.quantity,row);
	},500); 
	
	function selectSubSupplier(row){
		state.nowRow=row;
		subsupplierRef.value.show(row.materialid);
	}
	
	function getSubSupplierRows(row){
		var name=row.name;
		state.nowRow.supplier=name;
		state.nowRow.supplierid=row.id;
		loadStepPrice(state.nowRow.supplierid,state.nowRow.materialid,state.nowRow.quantity,state.nowRow);
	}
	
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
				calcTotalAmount();
			}else{
				calcTotalAmount();
			} 
		  });
		} 
	}
	function show(rows,tablerow,globalForm){
		state.activeStatus=0;
		state.visible  = true;
		state.skuList = rows;
		state.globalForm=globalForm;
		if(tablerow.records){
			state.tablerows=tablerow.records;
		}
		for(var i=0;i<state.skuList.length;i++){
			var row=state.skuList[i];
			row.planqty=row.quantity;
			loadStepPrice(row.supplierid,row.materialid,row.quantity,row);
		}
		calcTotalAmount();
		
	}
	
    function getcellRef(el,row,columnName){
		if(el){
			inputRefs.value[`input_${row.sku}_${columnName}`] =  el; 
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
	function editDeposit(){
		state.depositEdit=false;
		nextTick(()=>{
				depositRef.value.focus();
		})
	
	}
	function calcTotalAmount(){
		state.totalamount=0;
		var totalprice=0;
		if(state.skuList && state.skuList.length>0){
			state.skuList.forEach(item=>{
				state.totalamount=state.totalamount+parseInt(item.quantity);
				totalprice=item.summaryprice+totalprice;
			});
			state.totalPrice=totalprice;
		}
	}
	
	function handleAdd(){
		materialRef.value.show();
	}
	function getRows(rows){
		var amazonauthid=state.tablerows[0].amazonauthid;
		var groupid=null;
		if(state.skuList && state.skuList.length>0){
			groupid=state.skuList[0].groupid;
		}
		var groupname=state.tablerows[0].groupname;
		var marketname=state.tablerows[0].marketname;
		var marketplaceid=state.tablerows[0].marketplaceid;
		if(rows && rows.length>0){
			var skus="";
			state.skuList.forEach(function(datas){
				 skus+=(datas.sku)+(",");
			});
			rows.forEach(function(item){
				if(skus.indexOf((item.sku+","))<0){
					var items={};
					items.amazonauthid=amazonauthid;
					items.groupid=groupid;
					items.groupname=groupname;
					items.marketname=marketname;
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
					items.sku=item.sku;
					items.price=item.price;
					items.supplier=item.supplier;
					items.supplierid=item.supplierid;
					items.materialid=item.id;
					state.skuList.push(items);
				}
			});
		}
		 
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
<template>
   <div class="flex-center-between "  v-if="formData.hasbind" >
	   <div>
		   <h3 v-for="item in authList" v-show="formData.alibabaAuthid==item.id">{{item.name}}</h3>
		   <p class="font-extraSmall m-t-8"><span>1688订单号: </span>{{formData.alibabaOrderid}}
		    <el-button size="small" @click="saveReport(formData)">物流报价</el-button>
		   </p>
	   </div>
	   <el-button  @click="handleUnBind" type="info" plain>解除关联</el-button>
   </div>
	<el-form v-else label-position="top">
		<el-form-item label="已绑定的1688账号" >
			<el-select  v-model="formData.alibabaAuthid" :disabled="formData.hasbind" @change="handleAuthChange">
				<el-option   v-for="item in authList"  :key="item.id"  :label="item.name" :value="item.id"   >
				</el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="1688订单号" >
			<el-input v-model="formData.alibabaOrderid" :disabled="formData.hasbind"></el-input>
		</el-form-item>
		<el-form-item >
			<el-button  type="primary" @click="handleBind">确认关联</el-button>
		</el-form-item>
	</el-form> 
	<el-divider class="divider-cell"></el-divider>
	<h5 >订单信息</h5>
	<div v-if="orderInfo.baseInfo" class="m-t-8">
		<el-table :data="orderInfo.productItems"   >
			<el-table-column prop="type" label="图片" width="65">
				<template #default="scope">
					<el-image :src="scope.row.productImgUrl[0]" class="product-img"></el-image>
			</template>
			</el-table-column>
			<el-table-column  prop="name" label="商品名称" show-overflow-tooltip>
			<template #default="scope">
				<P>{{scope.row.name}}</P>
				<span class="font-extraSmall">优惠金额：{{scope.row.entryDiscount/100}}</span>
			</template>
	    	</el-table-column>
			<el-table-column prop="price" label="单价" width="70">
						<template #default="scope">
						<div >{{scope.row.price}}</div>	
							<div >
								<el-tag v-if="scope.row.isbind=='true'" type="info"  size="small">已绑定</el-tag>
								<el-button v-else type="primary"  plain @click="bind1688(scope.row,orderInfo)" size="small">
							绑定
							</el-button></div>	
				</template>
				</el-table-column>
			 <el-table-column prop="quantity" label="数量" width="80"/>
			  <el-table-column prop="itemAmount" label="总价"  width="60"/>
		</el-table>
		<div class="m-t-8  sum-p-t">
			<div>
			   <p class="font-small"><span >运费</span><span >￥{{orderInfo.baseInfo.shippingFee}}</span></p>
			   <p  class="font-small"><span >退款</span><span >￥{{orderInfo.baseInfo.refund}}</span></p>
			   <p ><span>总付款</span><span class="text-red-18">￥{{orderInfo.baseInfo.totalAmount}}</span></p>
			   </div>
		</div>
		 <el-tabs
		    v-model="activeName"
		    type="card"
		    @tab-click="handleClick"
		  >
		    <el-tab-pane label="收货信息" name="1">
				<el-descriptions  size="small" :column="1" class="row-descriptions">
								 <el-descriptions-item label="订单号" >
								 <div class="flex-center-between">
									 <span>{{orderInfo.baseInfo.id}}</span>
									<div>
								 <el-button size="small" @click="dialog.visible=true" type="primary" link v-if="orderInfo.baseInfo.status=='waitbuyerpay'">取消订单</el-button>
								 <el-button size="small" @click="handleBind"   ><el-icon><Refresh /></el-icon>&nbsp;刷新</el-button>
								 </div>
								 </div>
								 </el-descriptions-item> 
								 <el-descriptions-item   label="订单状态"  >
								 	<span v-if="OrderStatus[orderInfo.baseInfo.status]">{{OrderStatus[orderInfo.baseInfo.status].name}}</span>
								     <span v-else>{{orderInfo.baseInfo.status}}</span>
								 	<span v-if="orderInfo.baseInfo.refundStatus" class="font-extraSmall">
								 		 (<span v-if="OrderStatus[orderInfo.baseInfo.refundStatus]">{{OrderStatus[orderInfo.baseInfo.refundStatus]}}</span>
								 	     <span v-else>orderInfo.baseInfo.refundStatus</span>
								 		 )
								 	</span>
								 	<el-button style="margin-left:5px" size="small" @click="handlePay" type="warning" link v-if="orderInfo.baseInfo.status=='waitbuyerpay'">付款</el-button>
								 	<el-button style="margin-left:5px" size="small" @click="handleRefund" circle  type="warning"   v-if="orderInfo.baseInfo.status=='waitsellersend'">退</el-button>
								 </el-descriptions-item>
				   <el-descriptions-item label="收货人">{{orderInfo.baseInfo.receiverInfo.toFullName}}</el-descriptions-item>
				   <el-descriptions-item label="收货地址">{{orderInfo.baseInfo.receiverInfo.toArea}}</el-descriptions-item>
				   <el-descriptions-item label="手机">{{orderInfo.baseInfo.receiverInfo.toMobile}}</el-descriptions-item>
				   <el-descriptions-item label="邮编">{{orderInfo.baseInfo.receiverInfo.toPost}}</el-descriptions-item>
				 </el-descriptions>
			</el-tab-pane>
		    <el-tab-pane label="卖家信息" name="2">
				<el-descriptions class="row-descriptions"  size="small" :column="1">
				   <el-descriptions-item label="供应商">{{orderInfo.baseInfo.sellerContact.companyName}}</el-descriptions-item>
				   <el-descriptions-item label="会员登陆名">{{orderInfo.baseInfo.sellerContact.imInPlatform}}</el-descriptions-item>
				   <el-descriptions-item label="支付宝账户">{{orderInfo.baseInfo.sellerID}}</el-descriptions-item>
				   <el-descriptions-item label="手机">{{orderInfo.baseInfo.sellerContact.mobile}}</el-descriptions-item>
				   <el-descriptions-item label="电话">{{orderInfo.baseInfo.sellerContact.phone}}</el-descriptions-item>
				 </el-descriptions>
			</el-tab-pane>
		  </el-tabs>
	<el-row >
		<el-col :span="24">
					  <div class="flex-center-between">
					  	<h5 class="flex-center"><el-icon class="font-medium"><ChatLineSquare /></el-icon>&nbsp;买家留言</h5>
						<div  class="pointer" @click.stop="edit.visible=true">
							<el-button  size="small" ><el-icon><EditPen /></el-icon>&nbsp;补充留言</el-button>
						</div>
					  </div> 
					<div  class="font-small m-t-8">{{orderInfo.baseInfo.buyerFeedback}}</div>
				    <div v-if="edit.visible" class=" m-t-8">
					<el-input   type="textarea" maxlength="498"
			                   show-word-limit v-model="formData.remark"></el-input> 
					<div style="padding-top:10px;">
					<el-button size="small" @click.stop="edit.visible=false">取消</el-button>
					<el-button size="small" @click.stop="submitNotice" type="primary">补充留言</el-button>
					</div>
				</div>
				
		</el-col>
	</el-row>
	</div>
	<div v-else>
		<Open1688Order ref="open1688OrderRef" @confirm="handleBindOrder"></Open1688Order>
	</div>
	<el-dialog
	    v-model="dialog.visible"
	    title="取消原因"
	    width="30%"
	    :before-close="handleClose"
	  >
	  <el-form :inline="true">
	  	<el-form-item label="取消原因"  >
	    <el-radio-group v-model="formData.cancelReason" size="small">
	          <el-radio-button label="buyerCancel" >买家取消订单 </el-radio-button>
	          <el-radio-button label="sellerGoodsLack" >卖家库存不足</el-radio-button>
	          <el-radio-button label="other" >其它</el-radio-button>
	        </el-radio-group>
		</el-form-item >
		<el-form-item label="原因描述">
			 <el-input
			    v-model="formData.remark"
			    :autosize="{ minRows: 2, maxRows: 4 }"
			    type="textarea"
			    placeholder="请输入取消原因"
			  />
		</el-form-item >
		</el-form>
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="dialog.visible = false">取消</el-button>
	        <el-button type="primary" @click="cancelOrder">
	          确认
	        </el-button>
	      </span>
	    </template>
	  </el-dialog>  
	  
	<el-dialog
	    v-model="paydialog.visible"
	    title="付款"
	    width="30%"
	    :before-close="handleClose"
	  >
	  <el-form  >
	  	<el-form-item label="付款订单"  >
			{{payWay.orderId}}
	    </el-form-item >
	    <el-form-item label="付款费用"  >
			{{payWay.payFee/100}}
	    </el-form-item >
		<el-form-item label="失效日期"  >
			{{payWay.timeout}}
		</el-form-item >
			
		<el-form-item label="付款方式">
			<el-radio-group v-model="payWay.code" size="small">
				  <el-radio-button :label="channels.code"  v-for="channels in payWay.channels">
					  {{channels.name}} 
				   </el-radio-button>
				</el-radio-group>
		</el-form-item >
		</el-form>
	    <template #footer>
	      <span class="dialog-footer">
	        <el-button @click="paydialog.visible = false">取消</el-button>
	        <el-button type="primary" @click="orderPay">
	          确认
	        </el-button>
	      </span>
	    </template>
	  </el-dialog> 
	  
	  <el-dialog
	      v-model="refunddialog.visible"
	      title="退款退货"
	      width="30%"
	      :before-close="handleClose"
	    >
	    <el-form  >
	  	<el-form-item label="货物状态">
	  		<el-radio-group v-model="refund.goodsStatus" size="small" @change="loadRefundReason">
	  			  <el-radio v-for="goodstatus in refund.goodStatusList" :label="goodstatus.value" >
	  				                 {{goodstatus.name}}
	  			   </el-radio>
	  			</el-radio-group>
	  	</el-form-item >
			<el-form-item label="退款内容">
				<el-radio-group v-model="refund.disputeRequest" size="small"  >
					  <el-radio  label="refund" >   退款 </el-radio>
					  <el-radio  label="returnRefund" >   退款退货 </el-radio>
					</el-radio-group>
			</el-form-item >
			
			<el-form-item label="退货理由"  >
				  <el-radio-group v-model="refund.applyReasonId" size="small">
				  	  <el-radio v-for="reason in refund.reasons" :label="reason.id" >
				  		                 {{reason.name}}
				  	   </el-radio>
				  	</el-radio-group>	 
			</el-form-item >
			<el-form-item label="描述"  >
					<el-input
					   v-model="refund.description"
					   :autosize="{ minRows: 2, maxRows: 4 }"
					   type="textarea"
					   placeholder="请输入取消原因"
					 />
			</el-form-item >
	  	</el-form>
	      <template #footer>
	        <span class="dialog-footer">
	          <el-button @click="refunddialog.visible = false">取消</el-button>
	          <el-button type="primary" @click="refundOrderSumbit">
	            确认
	          </el-button>
	        </span>
	      </template>
	    </el-dialog> 
	  
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs,} from 'vue'
	import purchasealibabaApi from '@/api/erp/purchase/open1688/purchasealibabaApi.js';
	import Open1688Order from "@/views/erp/purchase/open1688/order/open1688order.vue";
	import { ElMessage, ElMessageBox } from 'element-plus';
	import {Edit,Refresh,ChatLineSquare,EditPen} from '@element-plus/icons-vue';
	import OrderStatus from "@/model/erp/purchase/open1688/order_status.json"
	import purchaseApi from '@/api/quote/purchaseApi.js';
	import thirdpartyApi from "@/api/erp/thirdparty/thirdpartyApi.js";
	import materialApi from "@/api/erp/material/materialApi.js";
	import userApi from "@/api/sys/admin/userApi.js";
	
	const open1688OrderRef=ref();
	const state = reactive({
		 activeName:'1',
		 authList:[],
		 formData:{alibabaAuthid:"",alibabaOrderid:"",purchaseEntryid:"",hasbind:false,cancelReason:"buyerCancel"},
		 orderInfo:{},
		 entry:{},
		 payWay:{},
		 refund:{disputeRequest:"refund",
		         reasons:[],
				 goodStatusList:[
								 {value:"refundWaitSellerSend",name:"售中等待买家发货"},
								 {value:"refundWaitBuyerReceive",name:"售中等待买家收货"},
								 {value:"refundBuyerReceived",name:"售中已收货（未确认完成交易）"},
								 {value:"aftersaleBuyerNotReceived",name:"售后未收货"},
								 {value:"aftersaleBuyerReceived",name:"售后已收到货"},
								 ],
		 },
		 edit:{visible:false},
		 dialog:{visible:false},
		 paydialog:{visible:false},
		 refunddialog:{visible:false},
		 
	})
	const {
	   authList,entry,formData,orderInfo,edit,dialog,paydialog,payWay,refund,refunddialog,activeName
	}=toRefs(state)
	const emit = defineEmits(['change','noauth']);
	function show(entry){
		 state.formData.purchaseEntryid=entry.id;
		 state.entry=entry;
		 handleQuery();
	}
    function cancelOrder(){
		if(!state.formData.remark){
			state.formData.remark="";
		}
		if(!state.formData.cancelReason){
			state.formData.cancelReason="";
		}
		purchasealibabaApi.cancelOrder(state.formData).then(res=>{
			state.dialog.visible = false;
			handleBind();
		}); 
	}
	function submitNotice(){
		purchasealibabaApi.addFeedback(state.formData).then(res=>{
			state.edit.visible=false;
			handleBind();
		})
		
	}
	function refundOrderSumbit(){
		state.refund.applyPayment=state.orderInfo.baseInfo.sumProductPayment;
		state.refund.applyCarriage=state.orderInfo.baseInfo.shippingFee;
		if(!state.refund.description||state.refund.description.length<=2){
			ElMessage.error( '退款理由必须大于两个字符');
			return;
		}
		purchasealibabaApi.createRefund(state.refund).then(res=>{
			if(res.result.success){
				ElMessage.success('操作成功');
				state.refunddialog.visible=false;
			   handleBind();
			}else{
				ElMessage.error(res.result.message);
			}
		})
	}
	function loadRefundReason(){
		var orderEntryIds=[];
		orderEntryIds.push(state.formData.alibabaOrderid);
		state.refund.orderEntryIds=JSON.stringify(orderEntryIds);
		state.refund.alibabaOrderid=state.formData.alibabaOrderid;
		state.refund.alibabaAuthid=state.formData.alibabaAuthid;
		state.refund.purchaseEntryid=state.formData.purchaseEntryid;
		purchasealibabaApi.getRefundReasonList(state.refund).then(res=>{
			if(res.data&&res.data.result&&res.data.result.result){
			   state.refund.reasons=res.data.result.result.reasons;
			   if(state.refund.reasons&&state.refund.reasons.length>0){
				   state.refund.applyReasonId=state.refund.reasons[0].id;
			   }
			}
		})
	}
	function handleRefund(){
		state.refunddialog.visible=true;
		state.refund.goodStatusList.forEach(item=>{ 
			if(item.value.toLowerCase().includes(state.orderInfo.baseInfo.status)){
				state.refund.goodsStatus=item.value;
			}
		})
		 loadRefundReason();
	}
	function handlePay(){
		state.payWay={};
		purchasealibabaApi.payWay(state.formData).then(res=>{
			state.paydialog.visible=true;
			if(res.data&&res.data.resultList){
				state.payWay=res.data.resultList;
				state.payWay.code=res.data.resultList.channels[0].code;
			}
		});
	}
	function orderPay(){
		state.formData.channel=state.payWay.code;
		var orderIdList=[];
		orderIdList.push(state.formData.alibabaOrderid);
		state.formData.orderIdList=JSON.stringify(orderIdList);
		purchasealibabaApi.payUrl(state.formData).then(res=>{
			 window.open(res.data.payUrl, "_blank");
		});
	}
	function handleAuthChange(){
		if(state.formData.alibabaOrderid==""){
			if(open1688OrderRef.value&&open1688OrderRef.value.show){
			    open1688OrderRef.value.show(state.formData);
			}
		}
	}
	function bind1688(row,orderInfo){
		var data=JSON.parse(JSON.stringify(state.formData));
		data.productInfo=row;
		data.orderInfo=orderInfo;
		purchasealibabaApi.bindProductInfoByEntry(data).then(res=>{
			state.edit.visible=false;
			handleQuery();
		    ElMessage.success( '操作成功');
		})
	}
	function handleQuery(){
		purchasealibabaApi.getAlibabaOrder(state.formData).then(res=>{
			var fee={price:0,ship:0};
			if(res.data&&res.data.orderInfo){
				state.formData.alibabaAuthid=res.data.alibabaAuth;
				state.formData.alibabaOrderid=res.data.alibabaOrderid;
			    if(res.data.orderInfo){
						state.formData.hasbind=true;
						state.orderInfo=res.data.orderInfo.result;
						if(state.orderInfo.productItems.length==1){
							fee.price=state.orderInfo.productItems[0].itemAmount;
						}
						
				}
				fee.ship=state.orderInfo.baseInfo.shippingFee;
			}else{
				if(state.authList&&state.authList.length>0){
					state.formData.alibabaAuthid=state.authList[0].id;
				}
				state.formData.alibabaOrderid="";
				state.formData.hasbind=false;
				state.orderInfo={};
				if(open1688OrderRef.value&&open1688OrderRef.value.show){
				    open1688OrderRef.value.show(state.formData);
				}
			}
			emit("change",fee);
		})
	}
	function handleUnBind(){
		purchasealibabaApi.unbindAlibabaOrder(state.formData).then(res=>{
			state.formData.alibabaOrderid="";
			handleQuery();
		});
	}
	function handleBindOrder(orderid){
		state.formData.alibabaOrderid=orderid;
		purchasealibabaApi.bindAlibabaOrder(state.formData).then(res=>{
			
		});
	}
	function saveReport(formData){
		thirdpartyApi.getQuoteToken().then((res)=>{
			var dto={};
			dto.token=res.data.buyertoken;
			dto.othername=res.data.name;
			var buyerContact=state.orderInfo.baseInfo.buyerContact;
			dto.buyer={"id":state.orderInfo.baseInfo.buyerID,
			           "buyerloginid":state.orderInfo.baseInfo.buyerLoginId,
					   "phone":buyerContact.phone,
					   "name":buyerContact.name,
					   "loginname":buyerContact.imInPlatform,
					   "mobile":buyerContact.mobile,
					   "email":buyerContact.email,
					   "companyname":buyerContact.companyName,
					   "shopname":null};
			var sellerContact=state.orderInfo.baseInfo.sellerContact;		   
			dto.seller={"id":state.orderInfo.baseInfo.sellerID,
			             "sellerloginid":state.orderInfo.baseInfo.sellerLoginId,
			             "companyname":sellerContact.companyName,
						 "loginname":sellerContact.imInPlatform,
						 "name":sellerContact.name,
						 "phone":sellerContact.phone,
						 "shopname":sellerContact.shopName,
						 "email":sellerContact.email,
						 };
			var receiverInfo=state.orderInfo.baseInfo.receiverInfo;
			dto.receiver={"orderid":state.orderInfo.baseInfo.id,
			               "area":receiverInfo.toArea,
						   "division":receiverInfo.toDivisionCode,
						   "name":receiverInfo.toFullName,
						   "mobile":receiverInfo.toMobile,
						   "post":receiverInfo.toPost,
						   "town":receiverInfo.toTownCode};
			dto.order={"id":state.orderInfo.baseInfo.id,
			           "sellerid":state.orderInfo.baseInfo.sellerID,
					   "buyerid":state.orderInfo.baseInfo.buyerID,
					   "buyername":"",
					   "buyercompany":"",
					   "buyerothername":"",
					   "price":state.orderInfo.baseInfo.totalAmount,
					   "remark":state.orderInfo.baseInfo.remark,
					   "sysbuyerid":res.data.id,
					   "createtime":""};
			dto.orderItems=[];
			state.orderInfo.productItems.forEach(item=>{
				var image=item.productImgUrl&&item.productImgUrl.length>0?item.productImgUrl[0]:null;
				var myitem={"id":item.subItemID,
				            "orderid":state.orderInfo.baseInfo.id,
							"skuid":item.skuID,
							"productid":item.productID,
							"name":item.name,
							"productcargonumber":item.productCargoNumber,
							"image":image,
							"quantity":item.quantity,
							"status":item.status,
							"statusstr":item.statusStr,
							"price":item.price,
							"amount":item.itemAmount};
				dto.orderItems.push(myitem);
			})
			dto.entry={"id":state.entry.id,
			           "orderid":state.orderInfo.baseInfo.id,
			           "number":state.entry.number,
					   "materialid":state.entry.materialid,
					   "itemprice":state.entry.itemprice,
					   "orderprice":state.entry.orderprice,
					   "sku":state.entry.sku,
					   "image":state.entry.image,
					   "name":state.entry.mname,
					   "warehousename":state.entry.warehousename,
					   "suppliername":state.entry.suppliername,
					   "length":"",
					   "width":"",
					   "height":"",
					   "weight":"",
					   "audittime":state.entry.audittime?state.entry.audittime.substring(0,10):null
					   };
			userApi.getInfo().then(info=>{
				dto.order.buyername=info.data.name;
				materialApi.getMaterialInfo({"id":state.entry.materialid}).then(res=>{
							   dto.entry.length=res.data.pkgDim.length;
							   dto.entry.width=res.data.pkgDim.width;
							   dto.entry.height=res.data.pkgDim.height;
							   dto.entry.weight=res.data.pkgDim.weight;
							   purchaseApi.save(dto).then(res=>{
							   	 ElMessage.success( '操作成功');
							   })
				})
			})		   
		  
			
		});  
	  
	}
 
	function handleBind(){
		purchasealibabaApi.bindAlibabaOrder(state.formData).then(res=>{
			handleQuery();
		});
	}
	defineExpose({
		show,
	})
	onMounted(async()=>{
		await purchasealibabaApi.getAuthData().then(res=>{
			state.authList=res.data;
			if(state.authList&&state.authList.length>0){
				state.formData.alibabaAuthid=state.authList[0].id;
				var noauth=true;
				state.authList.forEach(item=>{
					if(item.refreshToken&&noauth){
						noauth=false;
					}
				});
				if(noauth){
					emit("noauth");
				} 
			}else{
				emit("noauth");
				
			}
		})
	})
</script>

<style scoped>
	.sum-p-t p{
		display:flex;
		justify-content: space-between;
		width: 200px;
	}
	
	.sum-p-t{
		display: flex;
		justify-content: flex-end;
	}
	.text-red-18{
		color:var(--el-color-primary);
		font-size: 18px;
		font-weight: 700;
	}
      .divider-cell{
		margin-top:16px!important;
		margin-bottom:16px !important;
	}
</style>
<style>
	.row-descriptions .el-descriptions__cell{
		display:flex;
	}
	.row-descriptions .el-descriptions__label{
		width:60px;
		color:#999;
	}
	.row-descriptions  .el-descriptions__content{
		flex:1;
	}
</style>
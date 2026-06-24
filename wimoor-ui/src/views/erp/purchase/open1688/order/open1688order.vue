<template>
 <div>
	 <el-form label-position="top">
	 	<el-form-item >
			 <el-row v-if="addressList.length" >
			           <el-col :span="24" class="m-t-8">
						   <el-select
						   v-model="formData.addressid"
						   @change="addressChange"
						   >
						  <el-option v-for="item in addressList"  :label="item.fullName" :value="item.id"></el-option>
						</el-select>
			           </el-col>
			           <el-col :span="24">
			            <el-descriptions :column="1"  class="m-t-8" size="small" border >
			              <el-descriptions-item>
			                <template #label>
			                  <div class="cell-item">
			                    <el-icon >
			                      <user />
			                    </el-icon>
			                    用户名
			                  </div>
			                </template>
			                {{address.fullName}}
			              </el-descriptions-item>
			              <el-descriptions-item>
			                <template #label>
			                  <div class="cell-item">
			                    <el-icon  >
			                      <iphone />
			                    </el-icon>
			                      手机
			                  </div>
			                </template>
			                {{address.mobilePhone}}
			              </el-descriptions-item>
			              <el-descriptions-item>
			                <template #label>
			                  <div class="cell-item">
			                    <el-icon >
			                      <location />
			                    </el-icon>
			                    城市
			                  </div>
			                </template>
			                 {{address.addressCodeText}}
			              </el-descriptions-item>
			              <el-descriptions-item v-if="address.townName&&address.townName!=''">
			                <template #label>
			                  <div class="cell-item">
			                    <el-icon>
			                      <tickets />
			                    </el-icon>
			                    街道
			                  </div>
			                </template>
			                <el-tag size="small">{{address.townName}}</el-tag>
			              </el-descriptions-item>
			              <el-descriptions-item>
			                <template #label>
			                  <div class="cell-item">
			                    <el-icon >
			                      <office-building />
			                    </el-icon>
			                    地址
			                  </div>
			                </template>
			                      {{address.address}}
			              </el-descriptions-item>
			            </el-descriptions>
			           </el-col>
			         </el-row>
	       <el-row v-else class="font-extraSmall">
			  未找到收货地址
		   </el-row>  
	  </el-form-item>
	  <el-form-item label="产品"   >
		   <el-col v-if="productinfo&&productinfo.specId" :span="24">
		        <div  class="product-box">
		        	<el-image v-if="productinfo.productImgUrl" 
					:src="productinfo.productImgUrl" 
					class="product-img"  
					></el-image>
		        	<el-image v-else 
					:src="$require('empty/noimage40.png')"  
					class="product-img"  
					></el-image>
		        	<div>
		        		<p class="name">{{productinfo.name}}</p>
		        		<p class="font-extraSmall">{{productinfo.skuInfos}}</p>
						<p class="text-orange font-medium" >￥{{productinfo.price}}</p>
		        	</div>
		        </div>
		   </el-col>
		   <el-col v-else :span="24" class="font-extraSmall">
			       未绑定1688商品
		   </el-col>
	  </el-form-item>
	  <el-form-item label="订单预览"   >
	  		   <el-col v-if="productinfo&&productinfo.specId" :span="24">
	  		        <div v-for="preview in orderPreview">
						<el-descriptions :column="1"  size="small" border >
							<div v-for="p in preview.cargoList">
								  <el-descriptions-item >
									<template #label>
									  <div class="cell-item">
										<el-icon >
										  <user />
										</el-icon>
									   产品信息-单价
									  </div>
									</template>
								      {{p.finalUnitPrice}} 
						          </el-descriptions-item>
								  <el-descriptions-item >
											<template #label>
											  <div class="cell-item">
												<el-icon >
												  <user />
												</el-icon>
											   产品信息-购买数量
											  </div>
											</template>
								     {{p.amount}}
								   </el-descriptions-item>
						   </div>
						  <el-descriptions-item>
						    <template #label>
						      <div class="cell-item">
						        <el-icon  >
						         <location />
						        </el-icon>
						       运费
						      </div>
						    </template>
						   ￥{{preview.sumCarriage/100}}
						  </el-descriptions-item>
						  <el-descriptions-item>
						    <template #label>
						      <div class="cell-item">
						        <el-icon >
						        <tickets />
						        </el-icon>
						        金额
						      </div>
						    </template>
						     ￥{{preview.sumPaymentNoCarriage/100}}
						  </el-descriptions-item>
						  <el-descriptions-item>
						    <template #label>
						      <div class="cell-item">
						        <el-icon>
						          <tickets />
						        </el-icon>
						     总金额
						      </div>
						    </template>
						    ￥{{preview.sumPayment/100}} 
						  </el-descriptions-item>
						  <el-descriptions-item v-if="preview.shopPromotionList&&preview.shopPromotionList.length>0">
						    <template #label>
						      <div class="cell-item">
						        <el-icon>
						          <tickets />
						        </el-icon>
						         优惠
						      </div>
						    </template>
						   <el-radio-group v-model="promotion" size="large">
						         <el-radio-button    v-for="shopPromotion in preview.shopPromotionList" 
													 :key="shopPromotion.promotionId" 
													 :label="shopPromotion.promotionId">
						            <span>{{shopPromotion.text}}</span> 
									<span class="text-orange">￥{{shopPromotion.discountFee/100}}</span> 
									<span v-if="shopPromotion.freePostage">免邮</span>
								   <el-tooltip  effect="light" >
								       <template #content>
								         <div style="width:500px;">{{shopPromotion.desc}}</div>
								       </template>
								   	   <el-icon><Warning /></el-icon>
								   </el-tooltip>
						         </el-radio-button>
						       </el-radio-group>
						  </el-descriptions-item>
						  
						  <el-descriptions-item>
						    <template #label>
						      <div class="cell-item">
						        <el-icon >
						          <office-building />
						        </el-icon>
						        支付类型
						      </div>
						    </template>
						        <el-radio-group v-model="tradeType" size="small">
						             <el-radio-button 
									   :disabled="!tradeModel.opSupport" 
									   :label="tradeModel.tradeType"  
										v-for="tradeModel in preview.tradeModelList">
						             {{tradeModel.name}}
									<el-tooltip  effect="light" >
									    <template #content>
									      <div style="width:500px;">{{tradeModel.description}}</div>
									    </template>
										   <el-icon><Warning /></el-icon>
									</el-tooltip>
								   </el-radio-button>
						           </el-radio-group>
						  </el-descriptions-item>
						</el-descriptions>
					</div>
					 <el-button @click="submitOrder">下单</el-button>
	  		   </el-col>
	  		   <el-col v-else :span="24" class="font-extraSmall">
	  			     未找到预览信息
	  		   </el-col>
	  </el-form-item>
	 
	</el-form>
 </div>
</template>

<script setup>
	import {ref,reactive,onMounted,toRefs,} from 'vue';
	import {
	  Iphone,Location,
	  OfficeBuilding,Warning,
	  Tickets,User,
	} from '@element-plus/icons-vue';
	import purchasealibabaApi from '@/api/erp/purchase/open1688/purchasealibabaApi.js';
	const state = reactive({
		 authList:[],
		 formData:{alibabaAuthid:"",alibabaOrderid:"",purchaseEntryid:"",
		           hasbind:false,addressid:""},
		 orderInfo:{},
		 tradeType:"",
		 address:{fullName:""},
		 entry:{},
		 addressVo:{},
		 addressList:[],
		 productinfo:{},
		 promotion:"",
		 productinfoVo:{},
		 orderPreview:[],
	})
	const {
	   authList,formData,orderInfo,addressList,address,productinfo,orderPreview,tradeType,promotion
	}=toRefs(state)
	const emit = defineEmits(['confirm']);
	function show(formData){
		if(formData.entry&&!formData.purchaseEntryid){
			formData.purchaseEntryid=formData.entry;
			state.entry=formData.entry;
		}
		state.formData=formData;
		state.addressList=[];
		state.formData.addressid="";
		state.productinfo={};
	    purchasealibabaApi.getAddress({id:formData.alibabaAuthid}).then(res=>{
	    	state.addressList=res.data.result.receiveAddressItems;
			state.formData.addressid=state.addressList[0].id;
			state.address=state.addressList[0];
			purchasealibabaApi.getBindProductInfoByEntry(formData).then(res=>{
				state.productinfo=res.data;
				loadPreView();
			})
	    });
	}
	function loadPreView(){
		state.orderPreview={};
		if(!state.productinfo)return ;
		var preparams=state.formData;
		var addressCodeTexts=state.address.addressCodeText.split(" ");
		var myaddress={
			addressId:state.address.id,
			fullName:state.address.fullName,
			mobile:state.address.mobilePhone,
			phone:"",
			postCode:state.address.post,
			districtCode:state.address.townCode,
			townText:state.address.townName
		}
		if(addressCodeTexts.length>=3){
			myaddress.provinceText=addressCodeTexts[0];
			myaddress.cityText=addressCodeTexts[1];
			myaddress.areaText=addressCodeTexts[2];
		}
		if(addressCodeTexts.length>=4){
			myaddress.townText=addressCodeTexts[3];
		}
		
		preparams.address=JSON.stringify(myaddress);
		state.addressVo=preparams.address;
		var product={
			offerId:state.productinfo.productID,
			specId:state.productinfo.specId,
			quantity:state.entry.amount
		}
		preparams.product=JSON.stringify(product);
		state.productinfoVo=preparams.product;
		purchasealibabaApi.preview(preparams).then(res=>{
			if(res.data&&res.data.orderPreviewResuslt){
				for(var i=0;i<res.data.orderPreviewResuslt[0].tradeModelList.length;i++){
					var trade=res.data.orderPreviewResuslt[0].tradeModelList[i];
					if(trade.opSupport){
						state.tradeType=trade.tradeType;
						break;
					}
				}
				var shopPromotionList=res.data.orderPreviewResuslt[0].shopPromotionList;
				if(shopPromotionList&&shopPromotionList.length>0){
					shopPromotionList.forEach(item=>{
						if(item.selected){
						    state.promotion=item.promotionId;
						}
					})
				}
			}
		   state.orderPreview=res.data.orderPreviewResuslt;
		});
	}
	function addressChange(){
		state.addressList.forEach(item=>{
			if(state.formData.addressid==item.id){
				state.address=item;
				loadPreView();
			}
		});
	}
	function submitOrder(){
		var preparams=state.formData;
		preparams.tradeType=state.tradeType;
		preparams.address=state.addressVo;
		preparams.product=state.productinfoVo;
		preparams.promotion=state.promotion;
		purchasealibabaApi.createCrossOrder(preparams).then(res=>{
			emit("confirm",res.data.result.orderId);
		});
	}
	function handleOrderReview(){
		var address={};
		state.addressList.forEach(item=>{
			if(state.formData.addressid==item.id){
				state.address=item;
			}
		});
		purchasealibabaApi.preview(state.formData);
	}
	defineExpose({
		show,
	})
	onMounted(()=>{
		
	})
</script>

<style scoped>
	.sum-p-t p{
		display:flex;
		justify-content: space-between;
		width: 200px;
	}
	.product-box{
		display: flex;
		line-height:20px;
	}	
	.product-box .el-image{
		margin-right: 16px;
	}
	.product-box .name{
	 font-size: 12px;
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
	.margin-b{
		margin-bottom: 8px;
	}
</style>

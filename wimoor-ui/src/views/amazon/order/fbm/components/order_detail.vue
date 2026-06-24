<template>
	<!-- 订单详情 -->
	<el-dialog v-model="orderVisible"  title="订单详情"  @close='closeDialog'>
		<div v-loading="orderloading">
		<el-descriptions>
		    <el-descriptions-item label="订单号"> {{orderData.orderid}}
			<el-icon class="pointer" @click="handleDeepQuery" title="刷新订单信息"><Refresh /></el-icon>
			</el-descriptions-item>
		    <el-descriptions-item label="店铺国家">{{orderData.groupname}}-{{orderData.marketname}}</el-descriptions-item>
		    <el-descriptions-item label="购买时间"> {{dateTimesFormat(orderData.buydate)}}</el-descriptions-item>
			<el-descriptions-item label="发货方式"> {{orderData.channel}}</el-descriptions-item>
			<el-descriptions-item label="买家邮箱">{{buyeremail}}</el-descriptions-item>
			<el-descriptions-item label="买家名称">
			<span v-if="buyername">{{buyername}}</span>
			<span v-else><el-icon style='cursor: pointer;' @click="reloadOrderDailog"><View /></el-icon></span>
			
			</el-descriptions-item>
		    <el-descriptions-item label="订单状态">
		      <el-tag :type="statusFunc(orderData.orderstatus)" size="small">{{orderData.orderstatus}}</el-tag>
		    </el-descriptions-item>
		    <el-descriptions-item label="配送地址"  >
			   <div v-if="address " >
					<div v-if="address.hasAddress=='nohas'">
						 <div>--</div>
					</div>
					<div v-else>
						 <div>{{address.addressName}} </div>
						 <div>{{address.addressLine}},{{address.addressState}}</div>
						 <div>{{address.addressCity}},{{address.addressCountry}},{{address.addressPostal}}</div>
					</div>
				</div>
			</el-descriptions-item>
			<el-descriptions-item label="备注">
				  <el-popover
				    placement="top-start"
				    title="备注"
				    :width="200"
				    trigger="click"
				  >
				  <p><el-input    type="textarea" v-model="orderData.remark"></el-input></p>
				      <div style="text-align: left;padding-top:20px; margin: 0">
				        <el-button size="small" type="primary" @click="saveOrderRemark(orderData)">保存</el-button>
				      </div>
				    <template #reference>
				     <div class="pointer" v-if="orderData.remarkhtml">{{orderData.remarkhtml}}</div>
				     <div class="pointer" v-else>--</div>
				    </template>
				  </el-popover>
			</el-descriptions-item>
		    </el-descriptions>
		  <el-divider/>
		<el-table :data="productData.list" border class="od-table">
			<el-table-column prop="image" label="图片" width="60"> 
				<template #default="scope">
					<el-image :src="scope.row.image" width="40" height="40"></el-image>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="名称/SKU"  show-overflow-tooltip>
				<template #default="scope">
					<div class='name'>{{scope.row.name}}</div>
					<div class='sku'>{{scope.row.sku}} </div>
				</template>
			</el-table-column>
			<el-table-column prop="asin" label="ASIN" width="100"   />
			<el-table-column prop="itemprice" label="售价" width="80"   />
			<el-table-column prop="currency" label="币种" width="80"   />
			<el-table-column prop="quantity" label="数量" width="80"   />
			<el-table-column prop="shipprice" label="运费" width="80"   />
			<el-table-column prop="itemdiscount" label="优惠金额" width="80"  >
					<template #default="scope">
						<div>{{scope.row.itemdiscount+scope.row.shipdiscount}}</div>
					</template>
			</el-table-column>
			<el-table-column prop="itemtax" label="商品税" width="80"    />
		</el-table>
		<div class="od-sum text-right">
			<p class="od-label">订单总计:</p>
			<span>{{currencyFunc(orderData.currency)}}{{totalorderprice}}</span>
		</div>
		<el-divider></el-divider>
		交易详情
		<el-table :data="finlist.list" border class="od-table">
			<el-table-column prop="ftype" label="费用名"     />
			<el-table-column prop="marketplaceId" label="费用中文名"     />
			<el-table-column prop="amount" label="金额"    >
				<template #default="scope">
					<div>{{currencyFunc(orderData.currency)}}{{scope.row.amount}}</div>
				</template>
			</el-table-column>
		</el-table>
		<div style="margin-top: 10px;">卖家账户余额变动:{{currencyFunc(orderData.currency)}}{{finfee}}</div>
		</div>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="orderVisible = false">关闭</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import {ref ,watch,reactive,onMounted,onUpdated} from 'vue';
	import orderListApi from '@/api/amazon/order/orderListApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage,ElLoading} from 'element-plus';
	import {View,Refresh} from '@element-plus/icons-vue'
	import myUtil from "@/hooks/amazon/order/financial.js";
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import FeedStatus from "@/components/feedstatus/index.vue";
	import {decodeText} from '@/utils/index.js';
	const {statusFunc,countryFunc,currencyFunc,totalRowFunc}=myUtil();
	const emit = defineEmits(['change' ]);
	let productData=reactive({list:[]});
	let finlist=reactive({list:[]});
	let invoiceModel={logourl:"",
	                      company:"",
	                      country:"",
						  province:"",
						  city:"",
	                      address:"",
		                  phone:"",postalcode:"",email:"",
	                     }
	 let totalorderprice=ref("")
    let orderData=ref(invoiceModel);
	let orderVisible =ref(false);
	let orderloading=ref(false);
	let totalAmount=ref(0)
	let subtotal=ref(0)
	let discounttotal=ref(0)
	let vattotal=ref(0)
	let shiptotal=ref(0)
	let vatloading=ref(false);
	let buyername=ref("")
	let buyeremail=ref("")
	let vattype=ref("normal");
	let finfee=ref(0);
	let address=ref({hasAddress:"nohas",addressLine:"",addressState:'',addressCity:'',addressCountry:'',addressPostal:'',addressName:''})
	function show(row){
		var rows=JSON.parse(JSON.stringify(row));
		orderData.value=rows;
		orderData.value.remarkhtml=decodeText(orderData.value.remark);
		loadOrderDetail(orderData.value.orderid,dateTimesFormat(orderData.value.buydate),orderData.value.groupid,"true","false");
		orderVisible.value=true;
		orderloading.value=true;
	}
	defineExpose({show})
	function loadOrderDetail(orderid,buydate,groupid,nonaddress,nonfin,isdeep){
		vatloading.value=true;
		orderloading.value=true;
		if(!isdeep){
			isdeep="false";
		}else{
			isdeep="true";
		}
		orderListApi.showOrderDetail({
			"purchaseDate":buydate,
			"orderid":orderid,
			"groupid":groupid,
			"nonaddress":nonaddress,
			"nonfin":nonfin,
			"isdeep":isdeep
		}).then(function(res){
			vatloading.value=false;
			orderloading.value=false;
			if(res.data){
				productData.list=res.data;
				if(res.data[0] && res.data[0]["orderMain"]){
					buyername.value=res.data[0].orderMain.buyerName;
					buyeremail.value=res.data[0].orderMain.buyerEmail;
				}
				if(res.data[0] && res.data[0]["financialList"] &&  res.data[0]["financialList"].length>0){
					finlist.list=res.data[0]["financialList"]
					finfee.value=res.data[0].financialfee
				}
				address.value=res.data[0];
				totalOrderPriceFunc();
				vatotalPriceAmount();
			}
		})
	}
	function saveOrderRemark(item){
		orderListApi.updateRemark({orderid:item.orderid,value:item.remark}).then(res=>{
			ElMessage.success('操作成功！');
			orderData.value.remarkhtml=decodeText(orderData.value.remark);
			emit("change",item);
		})
	}
	function vatotalPriceAmount(){
		if(productData.list){
			var subtotals=0;var shippingfee=0;
			var promotionaldiscount=0;var vatsummarytotals=0;
			var total=0;
			productData.list.forEach(function(item,index){
				var price = 0;var nums=0;
				item.total= parseFloat(item.itemprice) * parseInt(item.quantity);
				item.total=item.total.toFixed(2);
			
				promotionaldiscount+=(parseFloat(item.itemdiscount) + parseFloat(item.shipdiscount));
				shippingfee+=(parseFloat(item.shipprice));
				total+=(item.quantity*item.itemprice)+(item.shipprice)-(item.itemdiscount+item.shipdiscount);
				if(vattype.value=='Vat'){
					var rate=item.vatRate;
					if (rate > 0) {
						var rates = (rate / 100);
						nums = (parseFloat(item.itemprice) * rates) / (1 + rates);
					}
					item.vat=parseFloat(nums) ;
					item.vat=item.vat.toFixed(2);
					vatsummarytotals =parseFloat(vatsummarytotals)+parseFloat(item.vat)* parseInt(item.quantity) ;
					subtotals+= ( parseFloat(item.itemprice)-item.vat)*parseInt(item.quantity);
				}
				
			})
			totalAmount.value= total.toFixed(2);
			subtotal.value= parseFloat(subtotals).toFixed(2);
			discounttotal.value= promotionaldiscount.toFixed(2);
			vattotal.value= vatsummarytotals.toFixed(2);
			shiptotal.value=shippingfee.toFixed(2);
		}
		
	}
	function handleDeepQuery(){
		loadOrderDetail(orderData.value.orderid,dateTimesFormat(orderData.value.buydate),orderData.value.groupid,"true","false",true);
	}
	function reloadOrderDailog(){
		loadOrderDetail(orderData.value.orderid,dateTimesFormat(orderData.value.buydate),orderData.value.groupid,"false","false");
	}
	function closeDialog(){
		orderVisible.value=false;
	}
	function totalOrderPriceFunc(){
		if(productData.list){
			var totalPrice=0;
			productData.list.forEach(function(item,index){
				var discount = parseFloat(item.itemdiscount) + parseFloat(item.shipdiscount);
				if (item.region == "EU"||item.region == "UK") {
					totalPrice += parseFloat(item.itemprice) * parseInt(item.quantity) + parseFloat(item.shipprice) - parseFloat(discount);
				} else {
					totalPrice += parseFloat(item.itemprice) * parseInt(item.quantity) + parseFloat(item.shipprice) + parseFloat(item.itemtax) - parseFloat(discount);
				}
			})
		}
		totalorderprice.value=totalPrice.toFixed(2);
	}
</script>

<style>
</style>
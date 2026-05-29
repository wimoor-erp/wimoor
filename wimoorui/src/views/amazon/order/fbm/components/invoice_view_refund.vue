<template>
	
	<!-- 预览退款发票信息 -->
	<el-dialog v-model="lookvatVisible"   title="预览退款发票详情" width="80%"  @close='closeDialog'>
		<div style="border: 1px solid #5A5E66;" v-if="invoiceData1" v-loading="orderloading">
			<el-descriptions :column="2">
				<el-descriptions-item :span="1">
					<el-image style="border-right: 1px solid #5A5E66;" v-if="invoiceData1" :src="invoiceData1.logourl"   width="180" height="60"></el-image>
				</el-descriptions-item>
				<el-descriptions-item  align="right" :span="1"   ><h2 style="padding-right: 20px;font-size: 32px;"> Credit Note </h2>
				</el-descriptions-item>   
			</el-descriptions>
		   <el-descriptions :column="1">
			   <el-descriptions-item :span="1" style="font-weight: bolder;">
				 <span style="font-weight: bolder;font-size: 16px;">{{invoiceData1.company}} </span>
				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions v-if="invoiceData1.sign" :column="1">
		   			   <el-descriptions-item :span="1">
		   				 {{invoiceData1.sign}}
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="1">
		   			   <el-descriptions-item :span="1">
		   				{{invoiceData1.address}}
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="1" style="border-bottom: 1px solid #000000;">
		   			   <el-descriptions-item :span="1">
		   				{{invoiceData1.city}},{{invoiceData1.province}},{{invoiceData1.country}},{{invoiceData1.postalcode}}
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2"  >
		   				  <el-descriptions-item :span="1" align="left"  >
		   						<span  > </span>
		   				  </el-descriptions-item>  
		   				 <el-descriptions-item :span="1" align="right" >
		   				 			 <span style="padding-right: 20px;font-size: 18px;font-weight: bolder;">Refunded </span>
		   				 </el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2"  >
				  <el-descriptions-item :span="1" align="left"  >
						<span  >{{address.addressName}} </span>
				  </el-descriptions-item>  
				   <el-descriptions-item :span="1" align="right" label="Credit Note:">
					 <span style="padding-right: 20px;">{{vatNo}} </span>
					</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{address.addressLine}},{{address.addressState}} </span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Original invoice:">
		   				 <span style="padding-right: 20px;">{{invoiceNo}}</span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{address.addressCity}},{{address.addressCountry}} </span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Credit note date:">
		   				 <span style="padding-right: 20px;"> {{dateFormat(new Date())}} </span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{address.addressPostal}}</span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Total payable:">
		   				 <span style="padding-right: 20px;">-{{totalAmount}} </span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		  <el-table :data="productData.list" border class="od-table" style="margin-bottom: 20px;">
		  	<el-table-column prop="name" label="Credit note details"    >
		  		<template #default="scope">
		  			<div  class="name">{{scope.row.name}}</div>
		  		</template>
		  	</el-table-column>
			<el-table-column prop="quantity" label="Quantity" width="80"   />
		  	<el-table-column prop="itemprice" label="Price" width="80" >
				<template #default="scope">
					<div>-{{currencyFunc(scope.row.currency)}}
					<span v-if="vattype=='Vat'">
						{{scope.row.itemprice- scope.row.vat}}
					</span>
					<span v-else>
					   {{scope.row.itemprice}}
					</span>
					</div>
				</template>
			</el-table-column>
		  	<el-table-column prop="itemdiscount" label="Promotional Discount" width="120" >
				<template #default="scope">
					<div>-{{currencyFunc(scope.row.currency)}}{{scope.row.itemdiscount+scope.row.shipdiscount}}</div>
				</template>
			</el-table-column>
		  	<el-table-column prop="shipprice" label="Shipping Fee" width="100"  >
		  			<template #default="scope">
		  				<div>-{{currencyFunc(scope.row.currency)}}{{scope.row.shipprice}}</div>
		  			</template>
		  	</el-table-column>
			<el-table-column prop="vatRate" v-if="vattype=='Vat'" label="VAT Rate" width="100"  >
					<template #default="scope">
						<div> {{scope.row.vatRate}}%</div>
					</template>
			</el-table-column>
			<el-table-column prop="vatRate" v-if="vattype=='Vat'" label="VAT" width="100"  >
					<template #default="scope">
						<div>-{{currencyFunc(scope.row.currency)}} {{scope.row.vat}}
						</div>
					</template>
			</el-table-column>
		  	<el-table-column prop="shipprice" label="Total Price" width="100"    >
				<template #default="scope">
					<div>-{{totalRowFunc(scope.row)}}
					  </div>
				</template>
			</el-table-column>	
		  </el-table>
		  <el-descriptions :column="1"  >
		  			   <el-descriptions-item :span="1" align="right" label="Sub Total:">
		  				 <span style="padding-right: 20px;">-{{subtotal}} </span>
		  				</el-descriptions-item>  
		  </el-descriptions>
		  <el-descriptions :column="1"  >
		  			   <el-descriptions-item :span="1" align="right" label="Shipping Fee:">
		  				 <span style="padding-right: 20px;">-{{shiptotal}} </span>
		  				</el-descriptions-item>  
		  </el-descriptions>
		  <el-descriptions :column="1"  >
		  			   <el-descriptions-item :span="1" align="right" label="Promotional Discount:">
		  				 <span style="padding-right: 20px;">-{{discounttotal}} </span>
		  				</el-descriptions-item>  
		  </el-descriptions>
		  <el-descriptions :column="1"  v-if="vattype=='Vat'" >
		  			   <el-descriptions-item :span="1" align="right" label="VAT Total:" >
		  				 <span   style="padding-right: 20px;">-{{vattotal}} </span>
		  				</el-descriptions-item>  
		  </el-descriptions>
		  <el-descriptions :column="1"  >
		  			   <el-descriptions-item :span="1" align="right" label="Credit note total:">
		  				 <span style="padding-right: 20px;">-{{totalAmount}} </span>
		  				</el-descriptions-item>  
		  </el-descriptions>
		  </div>
		  <template #footer>
		  	<span class="dialog-footer">
				<el-button @click="downloadVatInfo">下载</el-button>
		  		<el-button @click="lookvatVisible = false">关闭</el-button>
				<el-button @click="submitFBAVat" type="primary" title="需在亚马逊后台开启‘我将上传自己的增值税发票’功能才能使用！">上传FBA发票</el-button>
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
    const {statusFunc,countryFunc,currencyFunc,totalRowFunc}=myUtil();
    let invoiceData1=ref({});
	let invoiceData2=reactive({list:[]});
	let lookvatVisible=ref(false)
	let productData=reactive({list:[]});
	let address=ref({hasAddress:"nohas",addressLine:"",addressState:'',addressCity:'',addressCountry:'',addressPostal:'',addressName:''})
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
	 let totalAmount=ref(0)
	 let subtotal=ref(0)
	 let discounttotal=ref(0)
	 let vattotal=ref(0)
	 let shiptotal=ref(0)
	 let vatloading=ref(false);
	 let buyername=ref("")
	 let buyeremail=ref("")
	 let orderloading=ref(false);
	 let vatNo=ref("");
	 let vatRates=ref(0);
	 let vattype=ref("normal")
	 let ordertype=ref("normal")
	 let vatlabel=ref("Word")
	 let invoiceNo=ref("");
	function show(row,mvattype,mvatlabel,mordertype,invNo){
		lookvatVisible.value=true;
		invoiceNo.value=invNo;
		vattype.value=mvattype;
		vatlabel.value=mvatlabel;
		ordertype.value=mordertype;
		orderData.value=JSON.parse(JSON.stringify(row));
		loadOrderInfo(row);
	}
	defineExpose({show})
	
	function loadOrderInfo(row){
		            loadOrderDetail(row.orderid,dateTimesFormat(row.buydate),row.groupid,"false","true");
					orderListApi.selectVatInfoByGroup({
						"groupid":orderData.value.groupid
					}).then(function(res){
						if(res.data){
							if(res.data.data1){
							   invoiceData1.value=res.data.data1;
							}else{
								invoiceData1.value=invoiceModel;
							}
							invoiceData2.list=res.data.data2;
							vatNo.value=res.data.vatNo;
							if(vattype.value!="Vat"){
								vatRates.value=0;
							}else{
								var market=handlerMarket(orderData.value.market);
								invoiceData2.list.forEach(function(item,index){
									if(item.country==market){
										vatRates.value=item.vatRate;
									}
								});
							}
						}
					})
				}
	function submitFBAVat(){
		if(vattype.value!="Vat"){
			  ElMessage.error("自动上传的发票类型只支持VAT");
			  return;
		}else{
			orderListApi.sendAmzVatInvoince({
				"groupid":orderData.value.groupid,
				"country":handlerMarket(orderData.value.market),
				"orderid":orderData.value.orderid,
				"itemstatus":orderData.value.itemstatus,
				"postDate":dateTimesFormat(orderData.value.buydate),
				"vatlabel":vatlabel.value,
				"vattype":vattype.value
			}).then(function(res){
				if(res.data){ 
					if(res.data.isOk=="true"){
						feedid.value=res.data.feedid;
						ElMessage.success(res.data.msg);
					}else{
						ElMessage.error(res.data.msg);
					}
				}
			})
		}
	}
	
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
	function handlerMarket(market){
		if (market == "Amazon.fr"){
			market = "FR";
		}
		if (market == "Amazon.co.uk"){
			market = "UK";
		}
		if (market == "Amazon.de"){
			market = "DE";
		}
		if (market == "Amazon.es"){
			market = "ES";
		}
		if (market == "Amazon.it"){
			market = "IT";
		}
		if (market == "Amazon.nl"){
			market = "NL";
		}
		if (market == "Amazon.se"){
			market = "SE";
		}
		if (market == "Amazon.com.be"){
			market = "BE";
		}
		if (market == "Amazon.pl"){
			market = "PL";
		}
		return market;
	}
	function downloadVatInfo(){
		//下载pdf或者word
		orderListApi.downloadOrderVatInvoice({
			"orderid":orderData.value.orderid,
			"groupid":orderData.value.groupid,
			"vatlabel":vatlabel.value,
			"vattype":vattype.value,
			"country":handlerMarket(orderData.value.market),
			"itemstatus":orderData.value.itemstatus,
			"postDate":dateTimesFormat(orderData.value.buydate),
			"ordertype":ordertype.value,
			"invoiceno":invoiceNo.value
		}).then(res => {
			 ElMessage.success('导出成功！');
			 const blob = new Blob([res]);
			 if(window.navigator["msSaveOrOpenBlob"]&&window.navigator.msSaveOrOpenBlob()){
				 if(vatlabel.value=="PDF"){
					 navigator.msSaveBlob(blob, "ordersVatInfo.pdf")
				 }else{
					  navigator.msSaveBlob(blob, "ordersVatInfo.doc")
				 }
			 		  
			 }else{
				 var link=document.createElement("a");
				 link.href=window.URL.createObjectURL(blob);
				 if(vatlabel.value=="PDF"){
					link.download="ordersVatInfo.pdf";
				 }else{
					 link.download="ordersVatInfo.doc";
				 }
				 link.click();
				 window.URL.revokeObjectURL(link.href);
			 }
		
		}).catch(e=>{
				ElMessage.error('导出失败！');
		})
	}		
</script>

<style>
</style>
<template>
	
	<!-- 预览退款发票信息 -->
	<el-dialog v-model="visible"   title="PDF发票预览" width="80%"  >
		<div  v-if="dataForm"     >
			<el-descriptions :column="2">
				<el-descriptions-item :span="1">
					<el-image style="border-right: 1px solid #5A5E66;" :src="dataForm.logoUrl" width="180" height="60"></el-image>
				</el-descriptions-item>
				<el-descriptions-item  align="right" :span="1"   ><h2 style="padding-right: 20px;font-size: 32px;"> Credit Note </h2>
				</el-descriptions-item>   
			</el-descriptions>
		   <el-descriptions :column="1">
			   <el-descriptions-item :span="1" style="font-weight: bolder;">
				 <span style="font-weight: bolder;font-size: 16px;">{{dataForm.company}} </span>
				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions v-if="dataForm.sign" :column="1">
		   			   <el-descriptions-item :span="1">
		   				 {{dataForm.sign}}
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="1">
		   			   <el-descriptions-item :span="1">
		   				{{dataForm.address}}
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="1" style="border-bottom: 1px solid #000000;">
		   			   <el-descriptions-item :span="1">
		   				{{dataForm.city}},{{dataForm.province}},{{dataForm.country}},{{dataForm.postalcode}}
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
						<span  >{{dataForm.addressName}} </span>
				  </el-descriptions-item>  
				   <el-descriptions-item :span="1" align="right" label="Credit Note:">
					 <span style="padding-right: 20px;">{{vatNo}} </span>
					</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{dataForm.addressLine}},{{dataForm.addressState}} </span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Original invoice:">
		   				 <span style="padding-right: 20px;">{{invoiceNo}}</span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{dataForm.addressCity}},{{dataForm.addressCountry}} </span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Credit note date:">
		   				 <span style="padding-right: 20px;"> {{dateFormat(new Date())}} </span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		   <el-descriptions :column="2" >
			   <el-descriptions-item :span="1" align="left"  >
			   						<span  >{{dataForm.addressPostal}}</span>
			   </el-descriptions-item>
		   			   <el-descriptions-item :span="1" align="right" label="Total payable:">
		   				 <span style="padding-right: 20px;">-{{totalAmount}} </span>
		   				</el-descriptions-item>  
		   </el-descriptions>
		  <el-table :data="list" border class="od-table" style="margin-bottom: 20px;">
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
		  		<el-button @click="visible = false">关闭</el-button>
				<el-button @click="submitFBAVat" type="primary" >下载发票</el-button>
		  	</span>
		  </template>
	</el-dialog>
</template>

<script setup>
	import {ref ,watch,reactive,onMounted,onUpdated,toRefs} from 'vue';
	import orderListApi from '@/api/amazon/order/orderListApi.js';
	import groupApi from '@/api/amazon/group/groupApi.js';
	import {formatFloat,dateFormat,dateTimesFormat} from '@/utils/index.js';
	import {ElMessage,ElLoading} from 'element-plus';
	import {View,Refresh} from '@element-plus/icons-vue'
	import myUtil from "@/hooks/amazon/order/financial.js";
	import GlobalTable from "@/components/Table/GlobalTable/index.vue";
	import FeedStatus from "@/components/feedstatus/index.vue";
	import CountryName from "@/model/erp/order/countryName.json";
    const {statusFunc,countryFunc,currencyFunc,totalRowFunc}=myUtil();
	const state =reactive({
		visible:false,
		invoiceInfo:{sales:"KG",date:new Date(),invNo:"MEN"},
		dataForm:{},
		list:[],
	  })
	  const {
		visible,dataForm,invoiceInfo,list
	  }=toRefs(state)
	function show(data){
		 state.visible=true;
		 state.invoiceInfo=data.invoiceInfo;
		 state.dataForm=data.dataForm;
		 state.list=data.list;
	}
	defineExpose({show})
	 
</script>

<style>
</style>
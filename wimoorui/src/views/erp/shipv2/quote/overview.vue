<template>
	<el-dialog  top="3vh"  v-model="visiable" title="报价供应商信息" width="80%">
		 <el-scrollbar>
		<div>
			<el-table show-summary :summary-method="summaryMethod" height="calc(100vh - 280px)" :data="shipment">
				<el-table-column label="货件" fixed="left">
					<template #default="scope">
						<div> {{scope.row.shipmentid}}</div> 
						<div>
							<span class="font-extraSmall">重量:{{scope.row.weight}} kg </span>
							<span class="font-extraSmall"> 体积:{{formatFloat(scope.row.volume/1000000)}}  m³</span>
						</div>
					</template>
				</el-table-column>
					<el-table-column v-for="price in quotationPriceList" :label="price.supplier.name" >
					   <template #header>
								<div class="text-omit-1">{{price.supplier.name}}</div>
						 </template>
						<template #default="scope">
							<template v-for="mprice in scope.row.quotationPriceList">
									<div  v-if="mprice.supplier.name==price.supplier.name">
										<div class=" text-omit-1" v-if="!mprice.disabled">
											<span  :title="mprice.supplier.name"> {{mprice.totalfee}} &nbsp;&nbsp;&nbsp;&nbsp;</span>
											<el-tooltip
											        class="box-item"
											        effect="dark"
											        :content="mprice.remark"
											        placement="top"
													v-if="mprice.remark"
											      >
											        <el-tag size="small">备注</el-tag>
											      </el-tooltip>
										</div>
										<div v-if="!mprice.disabled"><span class="font-extraSmall" v-if="mprice.unitprice">单价:{{mprice.unitprice}} </span>
										     <span class="font-extraSmall" v-if="mprice.tax"> 附加费:{{mprice.tax}} </span>
											 <span class="font-extraSmall" v-if="mprice.otherfee"> 其他费用:{{mprice.otherfee}} </span>
											
										</div>
										<div v-else><span class="font-extraSmall" v-if="mprice.unitprice">放弃报价 </span>
										    
											
										</div>
									</div> 
									<div v-else>
										<div> </div>
									</div> 
							</template>
						</template>
					</el-table-column>
			</el-table>
			 <!-- 
			<table class="sd-table m-b-16 " style="border-left:none"  border="0" cellpadding="0" cellspacing="0">
				<thead>
					<th style="width:120px">货件</th>
					<th v-for="price in quotationPriceList" style="width:80px">
						{{price.supplier.name}}
					</th>
				</thead>
				<tbody>
					<template v-for="address in order.addressList">
						<tr v-for="shipment in address.shipmentList" >
							<td >
								{{shipment.shipmentid}}
								<div>
									<span class="font-extraSmall">重量:{{shipment.weight}} kg </span>
									<span class="font-extraSmall"> 体积:{{formatFloat(shipment.volume/1000000)}}  m³</span>
								</div>
							</td>
							<td v-for="price in quotationPriceList" style="width:120px">
								<template v-for="mprice in shipment.quotationPriceList">
										<div  v-if="mprice.supplier.name==price.supplier.name">
											<div><span :title="mprice.supplier.name"> {{mprice.totalfee}}</span></div>
											<div><span class="font-extraSmall" v-if="mprice.unitprice">单价:{{mprice.unitprice}} </span>
											     <span class="font-extraSmall" v-if="mprice.tax"> 附加费:{{mprice.tax}} </span>
												 <span class="font-extraSmall" v-if="mprice.otherfee"> 其他费用:{{mprice.otherfee}} </span>
											</div>
										</div> 
										<div v-else>
											<div> </div>
										</div> 
								</template>
							</td>
								
							
						</tr>
						
					</template>
					<tr>
						<td>合计</td>
						<td v-for="price in quotationPriceList" style="width:80px" >
							<span v-if="price" :title="price.supplier.name"> {{price.totalfee}}</span>
						</td>
					</tr>
				</tbody>
			</table> -->
		</div>
		</el-scrollbar>
		  <template #footer>
		  	    <el-button @click="visiable=false"  >关闭</el-button>
		  </template>
	</el-dialog>
	 
</template>

<script setup>
	import '@/assets/css/packbox_table.css';
	import {Search,ArrowDown,} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import supplierApi from '@/api/quote/supplierApi.js';
	import {ElMessage, ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat,CheckInputFloat,formatFloat} from '@/utils/index';
	
	const state = reactive({ 
		order:{},
		quotationPriceList:[],
	    visiable:false, 
		shipment:[]
	})
	const{order,visiable,quotationPriceList,shipment}=toRefs(state)
	function show(order){
		 state.visiable=true;
		 state.order=order;
		 state.quotationPriceList=state.order.quotationPriceList;
		 state.shipment=[];
		 order.addressList.forEach(address=>{
			 address.shipmentList.forEach(shipment=>{
				  state.shipment.push(shipment);
			 })
		 })
	 }
	 function summaryMethod(field,row){
		 var summary=["汇总"];
		 state.quotationPriceList.forEach(price=>{
			 summary.push(price.totalfee);
		 })
		return summary;
	 }
	defineExpose({ show })
</script>

<style>
</style>
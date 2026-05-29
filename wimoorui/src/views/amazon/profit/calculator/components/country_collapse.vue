<template>
	<el-col :span="8"   class="m-b-16"> 
	                   <el-collapse class="collapse-card" v-model="activeCard" @change="handleChange">
							<el-collapse-item title="Consistency" :name="1">
								 <template #title>
									 <el-space>
									<el-image class="flex-center" :src="$require('country/'+country.market+'-flag-small.jpg')">
									</el-image> 
									<span>{{country.name}}</span>
									</el-space>
									<el-space :size="2"  v-if="country.market=='IN'">
										 <el-divider direction="vertical" />
										 <el-popover
										     placement="top-start"
										     title="税率配置"
										     :width="300"
										   > 
										   <el-form label-width="100px">
											   <el-form-item label="佣金比率">
												   <el-input @input="rateInput()"  v-model="tableData.referralrate" placeholder="默认按照所选品类匹配"> <template #append>%</template></el-input>
											   </el-form-item>
											   <el-form-item  label="进口税率">
												   <el-input @input="rateInput()"  v-model="tableData.taxrate"><template #append>%</template></el-input>
											   </el-form-item>
											   <el-form-item  label="进口GST税率">
												   <el-input @input="rateInput()"  v-model="tableData.gstrate"><template #append>%</template></el-input>
											   </el-form-item>
											   <el-form-item label="销售GST税率">
												   <el-input @input="rateInput()"  v-model="tableData.sellingGSTRate"><template #append>%</template></el-input>
											   </el-form-item>
										   </el-form>
										     <template #reference>
	                                     <el-button link>
										<el-icon class="font-base" >
											<Coin/>
										</el-icon>
										<span >自定义</span>	
										</el-button>	
										     </template>
										   </el-popover>
										
									</el-space>	
									<el-space :size="2"  v-if="country.market=='IN'">
										 <el-divider direction="vertical" />
										 <el-select v-model="tableData.shipmentType" size="small" class="w-8"  @change="changeWeight">
										 	<el-option   value="local"  label="local"></el-option>
											<el-option   value="regional"  label="regional"></el-option>
											<el-option   value="national"  label="national"></el-option>
										 </el-select>
										
									</el-space>		
								</template>	
								<table  border="0" cellpadding="0" cellspacing="0" class="sd-table table-striped" v-if="tableData.country&&tableData.country[country.market]">
									<tbody>
									<tr><th>利润率</th><th>售价({{tableData.country[country.market].currency}})</th><th>利润</th></tr>
									<tr><td class="font-bold text-green">{{tableData.country[country.market].margin}}</td>
									    <td><el-input 
										v-if="tableData.country&&tableData.country[country.market]"
										@input="marketPriceInput(country.market)" 
										v-model="tableData.country[country.market].sellingPrice"/>	
										</td>
										<td class="font-bold text-yellow">{{tableData.country[country.market].profit}}</td>
									</tr>
								 	<tr v-for="item in tableData.country[country.market].resultList" :class="item.margin=='30.0%'?'font-bold':''">
										<td >{{item.margin}}</td>
										<td>{{item.sellingPrice}}</td>
										<td>{{item.profit}}</td>
									</tr>
									 </tbody>
								 </table>
								 <table cellpadding="0" cellspacing="0"  class="sd-table table-striped" v-else>
								 	<tbody>
									<tr><th class="text-center">利润率</th><th>售价</th><th>利润</th></tr>
								 	<tr><td></td>
								 	    <td><el-input  disabled/></td>
								 		<td></td>
								 	</tr>
								  	<tr v-for="item in marginOptions" :class="item.margin=='30.0%'?'font-bold':''">
								 		<td>{{item*100}}%</td>
								 		<td></td>
								 		<td></td>
								 	</tr>	
									</tbody>
								  </table>
							</el-collapse-item>
						</el-collapse>
					</el-col>
	 
</template>

<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
	import {CheckInputFloat,CheckInputInt} from '@/utils/index.js';
	import '@/assets/css/packbox_table.css'
    const emit = defineEmits(['dataChange']);
	let props = defineProps({
	   loading:false,
	   country:"",
	   marginOptions:[],
	   tableData:{country:[{currency:"",resultList:[{}]}]},
	})
	const state=reactive({
        activeCard:[1],
	})
	function handleChange(){
		
	}
	function marketPriceInput(market){
		if(props.tableData&&props.tableData.country[market]){
		    props.tableData.country[market].sellingPrice=CheckInputFloat(props.tableData.country[market].sellingPrice)
		}
		var price=props.tableData.country[market].sellingPrice;
		var myprice=""+price;
		if(myprice.substring(myprice.length-1,myprice.length)=="."||(myprice.length-2>=0&&myprice.substring(myprice.length-2,myprice.length)==".0")){
			return ;
		}
		emit('dataChange',props.tableData);
	}
	function rateInput(){
		emit('rateChange',props.tableData);
	}
 
	onMounted(()=>{
		 
	})
	let {loading,country,tableData,marginOptions} =toRefs(props);
	let {activeCard } =toRefs(state);
</script>

<style scoped="scoped">
	.table-striped > tbody > tr:nth-of-type(odd) {
	    background-color: #fbfbfb;
	}
	.table-bordered > thead > tr > th, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > tbody > tr > td, .table-bordered > tfoot > tr > td {
	    border: 1px solid #f4f4f4;
	}
	.dark .table-striped > tbody > tr:nth-of-type(odd) {
	    background-color: #2f2f2f;
	}
	.dark .table-bordered > thead > tr > th,.dark .table-bordered > tbody > tr > th,.dark .table-bordered > tfoot > tr > th,.dark .table-bordered > thead > tr > td,.dark .table-bordered > tbody > tr > td,.dark .table-bordered > tfoot > tr > td {
	    border: 1px solid #2f2f2f;
	}
	.sd-table th, .sd-table td{
		padding:2px 8px;
		text-align: center;
		border-right:0;
	}
	.standrate{
		background-color:red;
	}
	.sd-table{
		border:none;
	}
</style>
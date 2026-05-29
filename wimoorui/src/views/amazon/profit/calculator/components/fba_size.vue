 <template>
 <el-drawer
    v-model="drawer"
    title="FBA费用各国标准尺寸对照"
    direction="ltr"	
  >
<ul class="card-tabs"  v-if="countryOptions" >
	<li v-for="market in countryOptions" >
		<span v-if="tableData.country&&tableData.country[market.market]&&tableData.country[market.market].productTier">
		<el-link target="_blank" :href="countryFee[market.market]" v-if="market.market=='US'">
		     <span class="text-info padding-right-5">{{market.name}} Tier: </span>  {{tableData.country[market.market].productTier}} 
			 <span v-if="tableData.country[market.market].outboundWeight<1" style="padding-left:5px">{{tableData.country[market.market].outboundWeight*16}} oz</span>
			 <span v-else style="padding-left:5px"> 	 {{tableData.country[market.market].outboundWeight}}  </span>
			 <span class="text-info padding-right-5 padding-left-10"> FBA:</span>  
			  {{tableData.country[market.market].currency}}
			  {{tableData.country[market.market].fba}}
			 </el-link>
		<el-link  :href="countryFee[market.market]"  target="_blank" v-else>
			<span class="text-info padding-right-5">{{market.name}} Tier: </span>  {{tableData.country[market.market].productTier}} 
			<span class="text-info padding-right-5 padding-left-10"> FBA: </span> 
			 {{tableData.country[market.market].currency}}
			 {{tableData.country[market.market].fba}}</el-link>  
			</span>
		 </li>
		
</ul>
  </el-drawer>
  </template>
<script setup>
	import {ref,reactive,toRefs,onMounted}from"vue"
		import countryFee from '@/model/amazon/profit/countryFee.json';
	let drawer = ref(false)
    let props = defineProps({
	   tableData:{country:[{currency:"",resultList:[{}]}]},
	   isSmlAndLight:"",
	   countryOptions:[],
	})
   let {tableData,countryOptions,isSmlAndLight} =toRefs(props);
	defineExpose({
	  drawer
	})
	
</script>

<style>
 .padding-right-5{
		  padding-right:5px;
	}
  .padding-right-10{
	  padding-right:10px;
  }
  .padding-left-10{
  	  padding-left:10px;
  }
 .card-tabs li::marker{
            color: #FF6700;
			line-height: 32px;
        }
 .card-tabs{
	 margin-left: 16px;
 }
</style>

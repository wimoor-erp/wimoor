<template>
<el-dialog v-model="visible" title="商品销售信息"   :destroy-on-close='true' :append-to-body="true" width="50%" >
		<div class="from-body" 	 v-loading="loading">
			    <div class="flex-center font-small m-b-24">
			 	   <el-image class="product-img" :src="productInfoData.image"></el-image>
			 	   <div class="pro-message ">
			 		   <div class="name">{{productInfoData.name}}</div>
					   <div class="flex-center-between"> 
			 		       <div class="sku">{{productInfoData.sku}}
					       <span class=" font-extraSmall">ASIN:{{productInfoData.asin}}</span>
					     </div>
					    <div>
					      <span class="pad10 font-extraSmall">上架日期:{{productInfoData.opendate}}</span>
						  <span class="pad10 font-extraSmall">站点:{{productInfoData.groupname}}-{{productInfoData.marketplacename}}</span>
						 </div>
					   </div>
					   <div>
						   <el-space>
						   <el-popover placement="right" :width="230" trigger="click"  >
						   	      <template #reference>
						   	       <span class="sale-price pointer">
						   	       {{productInfoData.landedCurrency}}{{productInfoData.landedAmount}}
						   	       </span>
						   	      </template>
						   	      <el-table  :data="productInfoData.priceList">
						   	        <el-table-column width="110" prop="ptype" label="类型" >
						   				<template #default="scope">
						   					<span v-if="scope.row.ptype=='BUYP'">您的售价</span>
						   					<span v-if="scope.row.ptype=='BOXP'">购物车售价</span>
						   					<span v-if="scope.row.ptype=='LOWP'">最低售价</span>
						   				</template>
						   		    </el-table-column>
						   	        <el-table-column width="90" prop="landedAmount" label="价格" >
						   				<template #default="scope">
						   					{{scope.row.landedCurrency}} {{scope.row.landedAmount}}
						   				</template>
						   			</el-table-column>	
						   	      </el-table>
						   	    </el-popover>
								<div  class="font-extraSmall ">利润率</div>
								<div  class="font-extraSmall l-h-0">{{productInfoData.newprorate}}</div>
								<el-tag size="small" effect="dark" :type="productInfoData.optstatuscolor" v-if="productInfoData.optstatusname">{{productInfoData.optstatusname}}</el-tag>
								<el-tag v-if="productInfoData.tagNameList" effect="plain" :type="item.color"  v-for="item in productInfoData.tagNameList" size="small" >
									{{item.name}}
								</el-tag>
								<el-tag size="small" v-if="productInfoData.flownnumber" type="danger">{{productInfoData.flownnumber}}人跟卖</el-tag>
								</el-space>
						   </div>
			 			<div class=" flex-t">
							<p>备注</p>
							<span class="gary-bg" v-html="productInfoData.htmlremark"> </span>
						</div>
			 	   </div>
			    </div>		
			<div >
				<el-divider></el-divider>
				<el-row :gutter="16">
				<el-col :span="12">
					        <h5>销售数据</h5>
					 <el-row >
						 <el-col :span="12">
							 <div class="font-extraSmall">日均销量</div>
							 {{NullTransform(productInfoData.averageSalesDay)}} 
						 </el-col>
						 <el-col :span="12">
							<div class="font-extraSmall">七天销量</div>
							{{NullTransform(productInfoData.sumweek)}} 
						 </el-col>
					 </el-row>
					 <el-row class="m-t-24">
						 <el-col :span="12">
							<div class="font-extraSmall">三十日销量</div>
							{{NullTransform(productInfoData.summonth)}}({{NullTransform(productInfoData.ordermonth)}}orders)
						 </el-col>
						 <el-col :span="12">
							<div class="font-extraSmall">销量排名</div>
							{{NullTransform(productInfoData.rank)}}  
						 </el-col>
					 </el-row>
					 <el-row class="m-t-24">
						 <el-col :span="12">
									  <div class="font-extraSmall">可售库存 </div>
										<span class="pointer" >
											{{NullTransform(productInfoData.afnFulfillableQuantity)}}  
										</span>
										<span class="font-extraSmall  " >
											<span v-if="productInfoData.country=='DE' || productInfoData.country=='PL'">
												(DE+PL+CZ:{{NullTransform(productInfoData.czinv)}})  
											</span>
											<span v-else>({{productInfoData.country}}:{{NullTransform(productInfoData.countryinv)}})</span>
										</span>
						 </el-col>
						 <el-col :span="12">
                            <div class="font-extraSmall">可售天数</div>
										<span >
											{{formatInteger(productInfoData.dayfulfilla)}}
										</span>
										<span v-if="productInfoData.dayinbound">
										   +{{formatInteger(productInfoData.dayinbound)}}
										</span>
										&nbsp;<el-link :underline="false" type="info" @click="handlarrivalChart(productInfoData)">
											<el-tooltip content="预计到货报表" placement="top" :hide-after="0" :show-after="200">
										</el-tooltip>
										</el-link>
						 </el-col>
					 </el-row>
					 <el-row class="m-t-24">
					 						 <el-col :span="12">
					 						<div class="font-extraSmall">购物车比例</div>
					 						 {{formatFloat(productInfoData.buybox)}}%
					 						 </el-col>
					 </el-row>
				</el-col>	
				<el-col :span="12" >
						        <h5>成本利润</h5>
						<el-row >
							 <el-col :span="12">
								<div class="font-extraSmall">利润</div>
								{{productInfoData.landedCurrency}}{{formatFloat(productInfoData.profits)}} 
							 </el-col>
							 <el-col :span="12">
				  <div class="font-extraSmall flex-center">利润率 <el-icon class="pointer" @click="viewProfitDetails(scope.row,'')"></el-icon></div>
					{{NullTransform(productInfoData.prorate)}}% 
							 </el-col>
						</el-row> 
						<el-row class="m-t-24">
							<el-col :span="12">
							  <div class="font-extraSmall">净利润</div>
							{{productInfoData.landedCurrency}}{{formatFloat(productInfoData.profitall)}} 
								 </el-col>
							 <el-col :span="12">
						 <div class="font-extraSmall">净利润率</div>
							 {{formatPercent(productInfoData.proprate)}}%  
							 </el-col>
						</el-row>
						<el-row class="m-t-24">
								 <el-col :span="12">
								<div class="font-extraSmall">其他成本</div>
							<span>{{NullTransform(productInfoData.othercost)}} </span>
								 </el-col> 
								 <el-col :span="12">
								<div class="font-extraSmall">广告费</div>
						{{productInfoData.landedCurrency}}{{NullTransform(productInfoData.advspend)}} 
								 </el-col>
						</el-row>
				</el-col>	
			    </el-row>
			<el-row>
			 </el-row>
			 </div>
		</div>	
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="visible = false">关闭</el-button>
			</span>
		</template>
	</el-dialog>
	</template>
<script setup>
	import {ref, reactive,} from 'vue'
	import productinfoApi from '@/api/amazon/product/productinfoApi.js';
	import NullTransform from"@/utils/null-transform.js";
	import {formatFloat,formatPercent,formatInteger,decodeText} from '@/utils/index.js';
	let visible=ref(false);
	let loading=ref(false);
	let productInfoData=ref([]);
	function percentformat(value){
		if(value && value!=0){
			return (value*100).toFixed(2)+'%';
		}else{
			return 0;
		}
	}
	function show(param){
		visible.value=true;
		loading.value=true;
		param.marketplace=param.marketplaceid;
		productinfoApi.productList(param).then((res)=>{
			if(res.data.records && res.data.total>0){
				productInfoData.value=res.data.records[0];
				productInfoData.value.sku=param.sku;
				productInfoData.value.htmlremark=decodeText(productInfoData.value.remark);
			}
			loading.value=false;
		});	 
	}
	defineExpose({
	  show
	}) 
</script>

<style scoped>
	.flex-v-bet{
		padding-top:20px;
	}
     
	.pro-message .sku{
		color: var(--el-color-blue);
		margin-top:8px;
		margin-bottom:8px;
	}
	.m-t-24{
		margin-top:16px;
	}
	.m-b-24{
		margin-bottom:24px;
	}
	.item-font .el-descriptions__label{
		color:var(--el-text-color-secondary)
	}
	.item-font .el-descriptions__content{
		font-weight: 600;
	}
	.pad10{
		padding-left:10px;
	}
	.product-img{
		width: 88px;
		height:88px;
		margin-right:16px;
		    flex: none;
	}
	.product-img img{
		width:88px;
		height:88px;
	}
	.sale-price{
		font-size:16px;
		font-weight: 600;
		color:#f5a20c;
	}
	.flex-t{
		display: flex;
		margin-top:16px;
	}
	.flex-t p{
		 margin-right:8px;
		 white-space:nowrap;
	}
	.flex-t span{
		padding: 8px;
		border-radius: 4px;
	}
	h5{
		margin-bottom: 16px;
		margin-top:16px;
	}

</style>

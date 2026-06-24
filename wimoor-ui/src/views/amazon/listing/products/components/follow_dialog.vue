	<template>
		<el-dialog 
		 class="fllow" v-model="visable" title="商品跟卖信息" destroy-on-close='true' width="1000px"  >
			<div class="con-header">
				<el-row  :gutter="32">
					<el-col :span="19">
						<div class="flex-center">
							   <el-image 
							   :src="row.image"   
							   class="product-img" 
							   style=""/>
							   <el-space direction="vertical" alignment="left">
							 <div class="font-bold">{{row.sku}}</div>
							 <div class="font-small">{{row.name}}</div>
							 <el-space class="font-extraSmall">
								 <span>ASIN：{{row.asin}}</span>
								 <span>站点：{{row.marketplacename}}</span>
							 </el-space>
							 </el-space>
						 </div>
						 </el-col>
						 <el-col :span="5" class="text-right">
						 <el-link :href="href"  target="_blank" type="primary">Offers for this product</el-link>
						 </el-col>
				</el-row>
				</div>
				<el-row :gutter="32">
					<el-col :span="24">
						 <el-table :data="tableData" style="width: 100%">
						       <el-table-column prop="price" label="商品价格+配送费" width="120" >
								    <template #default="scope">
									  <div  class="text-red font-small" > {{scope.row.currency}}<span class="font-large">{{scope.row.price}}</span></div>
									  <div v-if="scope.row.is_prime">
										<el-image :src="$require('prime.png')"></el-image>
									  </div>
									</template>
								</el-table-column>
						    <el-table-column label="状况" width="80">
								全新品
							</el-table-column>	
							<el-table-column label="配送方式" width="120">
								<template #default="scope">
								<el-tag v-if="scope.row.is_fulfilled_by_amazon" type="warning"  effect="plain">亚马逊配送</el-tag>
								<el-tag v-else type="info" effect="plain">卖家自配送</el-tag>
								</template>
							</el-table-column>
							<el-table-column prop="shopname" label="卖家信息" >
							<template #default="scope">
							   <el-link :href="scope.row.hrefstr"  target="_blank" type="primary">{{scope.row.shopname}}</el-link>
							</template>
							</el-table-column>
						    <el-table-column prop="is_fulfilled_by_amazon" label="评价"  width="220" >
								<template #default="scope">
									<el-space class="flex-center">
									 <el-rate
									   text-color="#ff9900"
									    v-model="scope.row.positive_feedback_rating20"
									    disabled
									  />	
									<div>{{scope.row.positive_feedback_rating}}% 好评</div>
									</el-space>
									<div>共 {{scope.row.feedback_count}} 次评级</div>
									</template>
								</el-table-column>
							<el-table-column prop="findtime" width="100" label="发现时间" />
							<el-table-column prop="losttime" width="100" label="离开时间" />
						  </el-table>
					</el-col>
				</el-row>
		  <template #footer>
		  	<span class="dialog-footer">
		  		<el-button @click="visable = false"> 关闭</el-button>
		  	</span>
		  </template>
		</el-dialog>
	</template>
	
	<script setup>
		import {ref,reactive,onMounted,toRefs} from "vue";
		import productinfoApi from '@/api/amazon/product/productinfoApi.js';
		import {ElMessage,ElDivider} from 'element-plus';
		import {getCurrencyMark,dateTimesFormat,formatFloat} from '@/utils/index.js';
		import {Select} from '@element-plus/icons-vue'
		let state = reactive({visable:false,
		                       href:"",
							   row:{},
							   tableData:[]});
		const {visable,href,row,tableData} = toRefs(state);
		function show(row){
			state.row=row;
			state.href="https://www." +row.pointName+"/gp/offer-listing/" +row.asin +"/ref=dp_olp_new_mbc?ie=UTF8&condition=new";
			productinfoApi.follow(row.followid).then(res=>{
				if(res.data){
					res.data.forEach(item=>{
						var hrefstr='https://www.'+item.point_name+'/sp?seller='+item.sellerid;
					    var shopname='';
					    if(item.shopname==undefined){
						        shopname =item.sellerid;
						 } else {
						        shopname= item.shopname;
						 }
						 var primestr='';
						var price=parseFloat(item.price)+parseFloat(item.shiping_amount)
						item.currency=getCurrencyMark(item.currency);
						item.price=formatFloat(price);
						item.is_fulfilled_by_amazon=item.is_fulfilled_by_amazon;
						item.hrefstr=hrefstr;
						item.shopname= item.shopname;
						item.positive_feedback_rating20=formatFloat(parseFloat(item.positive_feedback_rating)/20,0);
						item.findtime= dateTimesFormat(item.findtime);
						item.losttime= dateTimesFormat(item.losttime);
					})
					state.tableData=res.data;
				}
				 state.visable=true;
			});
		}

		defineExpose({ show });
	</script>
	
	<style scoped>
		.fllow .product-img img{
			width:80px;
			height:80px;
			margin-right: 16px;
		}
		.text-red{
			color:#9d0000;
		}
	</style>
	

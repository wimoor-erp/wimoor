<template>
	<div class='pag-radius-bor '>
		<div class="flex-center-between">
			<h4 >最近七天广告转化数据</h4>
            <AdGroup :selectSize="'small'" :border="true"   @change="changeGroup" />
		</div>
		<div class="funnel-chart">
			<div class="funnel-item">
				<div class="ad-funnel-data">{{data.impressions}}</div>
				<span>曝光量</span>
				<div class="ad-funnel-rate">
					<p>点击率:<span> {{data.ctr}}%</span></p>
				</div>
			</div>
			<div class="funnel-item">
				<div class="ad-funnel-data">{{data.clicks}}</div>
				<span>点击次数</span>
				<div class="ad-funnel-rate">
					<p>转化率:<span> {{data.cr}}%</span></p>
				</div>
			</div>
			<div class="funnel-item">
				<div class="ad-funnel-data">{{data.attributedUnitsOrdered}}</div>
				<span>订单数</span>
			</div>
		</div>
		<div class="data-center ">
		  <div class="ad-data-item " style="margin-right:8px">
			  <el-card shadow="never">
			  <h5 class="text-red">￥{{data.cost}}</h5>
			  <p class="name">广告花费</p>
			  </el-card>
		  </div>
		  <div class="ad-data-item " style="margin-left:4px;margin-right:4px">
			  <el-card shadow="never">
			  <h5 class="text-green">￥{{data.attributedSales}}</h5>
			  <p class="name">广告贡献销售额</p>
			  </el-card>
		  </div>
		  <div class="ad-data-item " style="margin-left:8px">
			  <el-card shadow="never">
			  <h5>{{data.acos}}%</h5>
			  <p class="name">ACOS</p>
			  </el-card>
		  </div>
		</div>
	</div>	
</template>

<script setup>
	import{ref,reactive,toRefs}from'vue';
	import AdGroup from '@/components/header/ad_group.vue';
	import summaryApi from '@/api/amazon/advertisement/summary/summaryApi.js';
	import {formatFloat,dateFormat,dateTimesFormat,outputmoney} from '@/utils/index.js';
	const state = reactive({
		data:{},
	})
	const{	
		 data
	}=toRefs(state)
	 
	function changeGroup(data){
		var queryParams={};
			queryParams.groupid=data.groupid;
			queryParams.profileid=data.profileid;
			queryParams.marketplaceid=data.marketplaceid;
			queryParams.profile=data.profile;
			summaryApi.getsumproductAll(queryParams).then(res=>{
				if(res.data){
					if(res.data.ctr){
						res.data.ctr=formatFloat(parseFloat(res.data.ctr));
					}
					if(res.data.cost){
						res.data.cost=formatFloat(parseFloat(res.data.cost));
					}
					if(res.data.attributedSales){
						res.data.attributedSales=formatFloat(parseFloat(res.data.attributedSales));
					}
				}
				state.data=res.data;
			})
	}
	
</script>

<style scoped>
	.funnel-chart{
		margin-top:24px;
	}
	.funnel-item{
		    position:relative;
		    border-top: 80px solid;
		    border-left: 50px solid transparent;
		    border-right: 50px solid transparent;
			margin: 20px auto;
	}
	.funnel-item:first-child {
	    border-top-color: #ff6700;
	    width: 420px;
	}
	.funnel-item:nth-child(2){
		border-top-color: #409eff;
		width: 300px;
	}
	.funnel-item:nth-child(3) {
	    border-top-color: #67c23a;
	    width: 180px;
	}
	.funnel-item>span::after {
	    content: "";
	    position: absolute;
	    left: 52px;
	    top: 6px;
	    height: 2px;
	    width: 40px;
	    background-color: #ddd;
		}
	
	.ad-funnel-data{
		position:absolute;
		left: 0px;
		right: 0px;
		top: -52px;
		color: #fff;
		text-align:center;
		font-size:24px;
	}
	.funnel-item>span{
		    position: absolute;
		    left: -120px;
		    top: -44px;
		    font-size: 12px;
		    color: #333;
	}
	.ad-funnel-rate p{
		    position: absolute;
		    right: -120px;
		    top: 2px;
		    font-size: 12px;
		    color: #333;
	}
	.ad-funnel-rate p::before{
		    content: "";
		    position: absolute;
		    left: -50px;
		    top: 6px;
		    height: 2px;
		    width: 40px;
		    background-color: #ddd;
	}
	.data-center{
		display:flex;
		align-items: center;
		justify-content:center;
	}
	.ad-data-item {
		flex-grow:1;
		margin-top:24px;
		margin-bottom:16px;
		text-align:center;
	}
	.ad-data-item h5{
		font-size:20px;
		font-weight:700;
		font-family: DIN Alternate,Helvetica Neue,Helvetica,Arial,SF Pro Display;
		margin-bottom:8px;
	}
	.ad-data-item .name{
		font-size:14px;
		color:#999;
	}
	.text-green{
		color:#53a52a;
	}
	.text-red{
		color:#df4f4f;
	}
</style>
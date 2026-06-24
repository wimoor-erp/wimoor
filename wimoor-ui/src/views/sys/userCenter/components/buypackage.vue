<template>
	<div class="vippay-wrap">
		<div class="vip-content">
			  <el-alert 
			  class="pointer"
			   @click="handleHelpQQ"
			  title="您有任何疑问，请联系我们！" type="warning" show-icon />
			  <div class="vipprice-wrap">
				  <div class="vip-title-text">
				  <h3 v-if="true">您当前为免费试用版，请选择一个套餐升级。</h3>
				  <h3 v-else>您当前为免费专业版，请选择一个套餐续费或升级。</h3>
				  </div>
				<el-row :gutter="16">
					<el-col :span="8" v-for="(item,index) in vipData" :key="index">
						<div class="pag-radius-bor">
							<div class="vip-card-header" 
							:class="getcardClass(item.type)">
								<div class="vip-name">{{item.type}}</div>
								<div class="vip-sub-text font-small">{{item.subtext}}</div>
								<div class="vip-price-text">{{item.price}}
								<span class="font-small">-每年</span>
								</div>
								<el-button v-if="item.type==='旗舰版'" color="#FFA900" @click="buyVip">立即购买</el-button>
								<el-button @click="buyVip" v-else  color="#fff">立即购买</el-button>
							</div>
							<div class="vip-card-body">
								<div class="vip-item-num">
								<div class="flex-between">
									<span>订单数量</span>
									<span class="vip-list-value">{{item.order}}</span>
									</div>
								<div class="flex-between">
									<span>子账号</span>
									<span class="vip-list-value">{{item.subcount}}</span>
									</div>
								<div class="flex-between">
									<span>利润计算方案</span>
									<span class="vip-list-value">{{item.plan}}</span>
									</div>
								</div>
								<div class="vip-item-bloon">
								<div class="flex-between">
									<span>进销存</span>
									<el-icon size="16"  color="#777"><CircleCheckFilled /></el-icon>
									</div>
								<div class="flex-between">
									<span>商品运营</span>
									<el-icon size="16"  color="#777"><CircleCheckFilled /></el-icon>
									</div>
								<div class="flex-between">
									<span>广告管理</span>
									<el-icon size="16" color="#777"><CircleCheckFilled /></el-icon>
								</div>
								<div class="flex-between">
									<span>财务管理</span>
									<el-icon size="16" color="#777"><CircleCheckFilled /></el-icon>
								</div>
								<div class="flex-between">
									<span>客服管理</span>
									<el-icon size="16" color="#777"><CircleCheckFilled /></el-icon>
								</div>
								<div class="flex-between">
									<span>权限管理</span>
									<el-icon size="16" color="#777"><CircleCheckFilled /></el-icon>
								</div>
								<div class="flex-between">
									<span>移动端/小程序支持</span>
									<el-icon size="16" color="#777"><CircleCheckFilled /></el-icon>
								</div>
								</div>
							</div>
						</div>
					</el-col>
				</el-row>
				<div class="pag-radius-bor after-sales">
					<div class="after-sales-title">售后保障</div>
					<el-row :gutter="16">
						<el-col :span="12">
							 <el-card shadow="never">
								 <el-space size="large">
									 <el-icon size="32"><Umbrella /></el-icon>
									 <div>
									 <div class="">30天无理由退款</div>
									 <div class="font-extraSmall m-t-8">
										 联系客服退款
									 </div>
									 </div>
								 </el-space>
							 </el-card>
						</el-col>
						<el-col :span="12">
							<el-card shadow="never">
									 <el-space size="large">
										 <el-icon size="32"><ChatLineSquare /></el-icon>
										 <div>
										 <div class="">极速服务应答</div>
										 <div class="font-extraSmall m-t-8">
											 专业工程师品质服务
										 </div>
										 </div>
									 </el-space>
							</el-card>
						</el-col>
					</el-row>
				</div>
			  </div>
		</div>
	</div>
	<el-drawer
	title="套餐购买"
	custom-class="pay-wrap"
	size="700"
	v-model="dialogVisible">
          <div class="form-wrap">
			  <div class="form-group">
			  <div class="form-item flex-center">
				 <div class="form-label">套餐版本</div>
				 <div class="form-content">
					     <el-radio-group v-model="viplevel" >
					       <el-radio-button label="0"  >基础版</el-radio-button >
					       <el-radio-button label="1"  >专业版</el-radio-button >
					       <el-radio-button label="2"  >旗舰版</el-radio-button >
					     </el-radio-group>
				 </div>
				 </div>
			  <div class="form-item flex-center">
				 <div class="form-label">购买时长</div>
				 <div class="form-content">
					     <el-radio-group v-model="viplevel" >
					       <el-radio-button label="0"  >1 年</el-radio-button >
					       <el-radio-button label="1"  >2 年</el-radio-button >
					       <el-radio-button label="2"  >3 年</el-radio-button >
					     </el-radio-group>
				 </div>
				 </div>
			  </div>
			  <div class="form-group">
			  <div class="form-item flex-center">
				 <div class="form-label">优惠金额</div>
				 <div class="form-content">
					    <el-button type="primary" text>-￥20.00</el-button>
				 </div>
				 </div>
			  </div>
			  <div class="form-group">
			  <div class="form-item flex-center">
				 <div class="form-label">支付方式</div>
				 <div class="form-content">
					     <el-radio-group v-model="paytype">
					        <el-radio label="0">
								<div class="flex-center-between">
									<span >微信支付</span>
									<el-image :src="$require('/WeChat.png')"></el-image>
								</div>
							</el-radio>
					        <el-radio label="1">
								<div class="flex-center-between">
									<span>支付宝</span>
									<el-image :src="$require('/Alipay.png')"></el-image>
								</div>
							</el-radio>
					      </el-radio-group>
				 </div>
				 </div>
			  </div>
		  </div>
		<template #footer>
			<div class="sum-price-wrap text-right">
				<el-space>
					<div class="">
						<span class="font-extraSmall">应付金额 </span>
						<span class="sum-num">￥500.00</span>
						<div class="text-right ">
							<el-text type="info" tag="del">￥520.00</el-text>
							</div>
						</div>
						<!-- 收款二维码 -->
					<pay-code-one theme="outline" size="100" fill="#333" :strokeWidth="1"/>
				</el-space>
			</div>
		</template>
	</el-drawer>
</template>

<script setup>
	import {CircleCheckFilled,Umbrella,ChatLineSquare,Picture}from"@element-plus/icons-vue";
	import {PayCodeOne} from"@icon-park/vue-next"
	import {reactive,ref,toRefs,}from"vue";
	const state = reactive({
		vipData:[
			{
				type:"基础版",
				subtext:'适合小型企业',
				price:"￥500",
				order:"5000/月",
				subcount:10,
				plan:5,
				},
			{
				type:"专业版",
				subtext:'适合中型企业',
				price:"￥899",
				order:"20万",
				subcount:50,
				plan:100,
				},
			{
				type:"旗舰版",
				subtext:'适合大型企业',
				price:"￥2899",
				order:"100万",
				subcount:"不限",
				plan:200,
				}
		],
		viplevel:"0",
		paytype:"0",
		dialogVisible:false,
	})
	const {
	viplevel,
	paytype,
	vipData,
	dialogVisible,
	}=toRefs(state)
	function handleHelpQQ(){
		window.open('http://wpa.qq.com/msgrd?v=3&uin=1874049814&site=qq&menu=yes','_blank');
	}
	function getcardClass(val){
		if(val==='专业版'){
			return "vip-card-blue";
		}else if(val==='旗舰版'){
			return "vip-card-svip";
		}else{
			return"";
		}
	}
	
	function buyVip(){
		state.dialogVisible = true;
	}
</script>

<style scoped>
	.sum-num{
		color:#ff7315;
		font-size:22px;
		font-weight: 700;
	}
	.form-group{
		background-color: #fff;
		padding-top: 24px;
		padding-left:16px;
		border-radius: 4px;
	}
	.form-item{
		background-color: #fff;
		border-radius: 4px;
		padding-bottom:24px;
		
	}
	.form-item .form-label{
		width: 120px;
		font-size: 14px;
	}
.after-sales-title{
	margin-bottom: 16px;
}
	.vip-name{
		font-size:22px;
		font-weight:500;
		margin-bottom: 8px;
	}
	.vip-price-text{
		padding-top:32px;
		font-size: 28px;
		font-weight:700;
		padding-bottom: 32px;
	}
	.pag-radius-bor{
		padding:0;
	}
	.vip-card-header{
		background-image: linear-gradient(to bottom, #ff7315, #ffab66);
		color:#fff;  
		border-radius: 4px;
		text-align: center;
		padding:32px;
	}
	.vip-card-blue{
		background-image: linear-gradient(to bottom, #3c8bfa, #7eb6fa);
	}
	.vip-card-svip{
		background-image: linear-gradient(to bottom, #f3c58d, #F3E0C1);
		color:#ad5819;
	}
	.vip-card-header .el-button{
		width: 100%;
		color:#ff7315;
	}
	.vip-card-blue .el-button{
		color:#3c8bfa;
	}
	.vip-card-svip .el-button{
		color:#ffffff;
	}
	.vip-content{
		width:1000px;
		margin:0 auto;
	}
	.vip-title-text{
		line-height: 60px;
		text-align: center;
	}
	.vip-list-value{
		color:#777;
	}
	.vip-card-body{
		font-size: 14px;
		color:#333;
		padding:0px 24px;
	}
	.vippay-wrap{
		margin:16px;
	}
	.vip-item-num{
		border-bottom: 1px solid #eee;
		margin-bottom: 16px;
		margin-top: 16px;
	}
	.vip-item-bloon .flex-between,.vip-item-num .flex-between{
		margin-bottom: 16px;
		
	}
	.vip-item-bloon{
		padding-bottom: 16px;
	}
	.after-sales{
		margin-top: 16px;
		padding:16px 24px;
	}
</style>
<style>
	.pay-wrap .el-drawer__body{
		background-color: #f5f5f5;
	}
	.pay-wrap .el-drawer__footer{
		padding-top: 24px;
	}
	.pay-wrap .el-drawer__header{
		margin-bottom:16px;
	}
	.pay-wrap .el-radio {
	   border:1px solid #eee;
	   border-radius: 4px;
	   padding:24px 16px;
	   margin-right:16px;
	}
	.form-content .flex-center-between{
		width: 120px;
	}
</style>
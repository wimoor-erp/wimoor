<template>
			  <div v-if="managerLimit.tariffpackage==4" class="vip-card vip-card-black">
					<div class="vip-text">
						<div class="vip-lvel-title">旗舰版</div>
						<div class="vip-titme">有效期至 {{dateFormat(managerLimit.losingEffect)}}</div>
					</div>
					<button type="button"   @click.stop="buypkgfee" class="btn-buy btn-buy-black">购买续费</button>
					<span class=" paytime">开通时间 {{dateFormat(managerLimit.createtime)}}</span>
				</div>
			  <div v-else-if="managerLimit.tariffpackage==3" class="vip-card vip-card-blue">
					<div class="vip-text">
						<div class="vip-lvel-title text-white">专业版</div>
						<div class="vip-titme text-white">有效期至 {{dateFormat(managerLimit.losingEffect)}}</div>
					</div>
					<button type="button"   @click.stop="buypkgfee" class="btn-buy btn-buy-blue">购买续费</button>
					<span class=" paytime text-white">开通时间 {{dateFormat(managerLimit.createtime)}}</span>
				</div>
			  <div v-else-if="managerLimit.tariffpackage==2||managerLimit.tariffpackage==1" class="vip-card vip-card-orange">
					<div class="vip-text">
						<div class="vip-lvel-title text-white">基础版</div>
						<div class="vip-titme text-white">有效期至 {{dateFormat(managerLimit.losingEffect)}}</div>
					</div>
					<button type="button"   @click.stop="buypkgfee" class="btn-buy btn-buy-orange">购买续费</button>
					<span class=" paytime text-white">开通时间 {{dateFormat(managerLimit.createtime)}}</span>
				</div>
			  <div v-else class="vip-card vip-card-gray">
					<div class="vip-text">
						<div class="vip-lvel-title text-white">免费版</div>
						<div class="vip-titme text-white">有效期至 {{dateFormat(managerLimit.losingEffect)}}</div>
					</div>
					<button type="button"   @click.stop="buypkgfee" class="btn-buy btn-buy-gray">购买续费</button>
					<span class=" paytime text-white">开通会员，获取更多权限</span>
				</div>
</template>

<script setup>
	import {reactive,toRefs,ref} from  "vue";
	import {dateFormat,formatFloat,getValue,formatPercent} from '@/utils/index.js';
	import {useRouter } from 'vue-router';
	let props = defineProps({managerLimit:{}});
	const { managerLimit} = toRefs(props);
	let router = useRouter();
	
	function buypkgfee(){
		router.push({
			path:"/s/p/d",
			query:{
				title:'购买/续费套餐',
				path:"/s/p/d",
			},
		})
	}
</script>

<style>
		.vip-card-black{
			 background-image: url('../../../../assets/image/svipCard.png');
			 background-repeat: no-repeat;
			 background-position:0,0
		}
		.vip-card-blue{
			background-image: url('../../../../assets/image/tvipCard.png');
			background-repeat: no-repeat;
			background-position:0,0
		}		
		.vip-card-orange{
			background-image: url('../../../../assets/image/bvipCard.png');
			background-repeat: no-repeat;
			background-position:0,0
		}	
		.vip-card-gray{
			background-image: url('../../../../assets/image/freeCard.png');
			background-repeat: no-repeat;
			background-position:0,0
		}	
		.btn-buy-black{
			color:#974914;
			background:#fff !important;
		}
		.btn-buy-blue{
			color:#1d95ff;
			background:#fff !important;
		}
		.btn-buy-orange{
			color:#ff7315;
			background:#fff !important;
		}
		.btn-buy-gray{
			color:#666;
			background:#fff !important;
		}
		.vip-card{
	          height:300px;
			  width: 497px;
	          overflow: hidden;
	          position: relative;
	          }

	       .vip-lvel-title{
			   font-size:32px;color:#974914;
			   font-weight:600;
			   }
	       .vip-text{
			   left:32px;
			   top:64px;
		       position: absolute;
	        }
	    .vip-titme{
	    	font-size:14px;color:#974914;margin-top:8px;
	    }
	    .paytime{
	    	font-size:14px;color:#974914;margin-top:8px;
			position: absolute;
			right:32px;
			bottom: 50px;
			opacity: 0.6;
	    }
	    .vip-card .btn-buy{
	    	height:40px;
	    	padding:0 24px;
	    	background: #FFFFFF;
	    	border-radius: 20px;
	    	 bottom:40px;
			 left:32px;
	    	 z-index: 2;
	    	 cursor: pointer;
	         border: none;
			 position: absolute;
	    }
	    .vip-card .btn-buy:hover, .vip-card .btn-buy:active, .vip-card .btn-buy:focus{
	    	 outline:none;
	    	 box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.4);
	    }
		.text-white{
			color:#fff;
		}
</style>
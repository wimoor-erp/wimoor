<template>
	<div style="padding: 30px;text-align: center;">
		<el-image
		  :src="$require('common/bindshop.png')"
		></el-image>
		<div style="font-size:24px;text-align: center;font-weight: 400;">
		<div>{{resmsg}}</div>
		<div>
			<el-button size='mini' @click="backAuthPath">返回授权列表</el-button> 
		</div>
		</div>
		
	</div>
</template>

<script>
	import {ref,reactive,onMounted} from "vue"
	import authApi from '@/api/amazon/auth/authApi.js';
	import { useRoute } from "vue-router"
	export default {
		name:'authResult',
		setup(){
			onMounted(()=>{
				showResult()
			})
			const route = useRoute()
			let resmsg=ref("进行授权中。。。") 
			function showResult(){
				authApi.authSeller({"state":route.query.state,"selling_partner_id":route.query.selling_partner_id,
				"mws_auth_token":route.query.mws_auth_token,"spapi_oauth_code":route.query.spapi_oauth_code}).then((res)=>{
					if(res.data&&res.data.id){
						resmsg.value="授权成功，请回到授权列表页面刷新。";
					}else{
						resmsg.value="授权失败，请再次尝试（注意：您必须在店铺所在的网络环境重新尝试）";
					}
				})
			}
			function backAuthPath(){
				location.href="http://"+location.host+"/amazon/storeAuth?title=店铺管理&path=/amazon/storeAuth";
			}
			return{
				showResult,
				resmsg,
				backAuthPath
			}
		}
	}
	
</script>

<style>
</style>

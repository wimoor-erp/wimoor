<template>
	<div style="padding: 30px;text-align: center;">
		<el-image
		  :src="$require('common/bindshop.png')"
		></el-image>
		<div style="font-size:24px;text-align: center;font-weight: 400;">
		    {{resmsg}}
		<div>
			<el-button size='mini' @click="backAuthPath" :title="title">返回授权列表</el-button> 
		</div>
		</div>
		
	</div>
</template>

<script>
	import {ref,reactive,onMounted} from "vue"
	import authApi from '@/api/amazon/advertisement/auth/authApi.js';
	import { useRoute } from "vue-router"
	export default {
		name:'authResult',
		setup(){
			onMounted(()=>{
				showResult()
			})
			const route = useRoute()
			let resmsg=ref("进行授权中。。。") 
			let title=ref("进行授权中。。。");
			function showResult(){
					authApi.authSeller({"code":route.query.code,"state":route.query.state}).then(res=>{
							resmsg.value="授权成功，请回到授权列表页面刷新。";
							console.log(res);
							title.value=JSON.stringify(res);
							if(res.code!="201"){
								authApi.authSeller({"code":route.query.code,"state":route.query.state}).then(res=>{
										resmsg.value="授权成功，请回到授权列表页面刷新。";
										console.log(res);
										title.value=JSON.stringify(res);
								}).catch(error=>{
										resmsg.value="授权失败，请再次尝试（注意：您必须在店铺所在的网络环境重新尝试）";
										console.log(error);
										if(error){
									       title.value=JSON.stringify(error);
										}
								})
							} 
					}).catch(error=>{
							resmsg.value="授权失败，请再次尝试（注意：您必须在店铺所在的网络环境重新尝试）";
							console.log(error);
							if(error){
						       title.value=JSON.stringify(error);
							   authApi.authSeller({"code":route.query.code,"state":route.query.state}).then(res=>{
							   		resmsg.value="授权成功，请回到授权列表页面刷新。";
									console.log(res);
							   		title.value=JSON.stringify(res);
							   }).catch(error=>{
								    console.log(error);
							   		resmsg.value="授权失败，请再次尝试（注意：您必须在店铺所在的网络环境重新尝试）";
							   		if(error){
							   	       title.value=JSON.stringify(error);
							   		}
							   })
							}
					})
			}
			function backAuthPath(){
				location.href="http://"+location.host+"/amazon/storeAuth?title=店铺管理&path=/amazon/storeAuth";
			}
			return{
				showResult,
				resmsg,title,
				backAuthPath
			}
		}
	}
	
</script>

<style>
</style>

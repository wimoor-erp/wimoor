<template>
	<el-dialog
		   v-model="authVisible"
		   title="店铺授权"
		   width="600px"
		 >
		  <el-alert  v-if="authtype=='manuAuthority'&&!formInline.isdeveloper"  title="请确保当前的电脑和IP是您要授权店铺的常用环境，以免店铺关联!" type="error" show-icon />
		  <el-alert  v-if="authtype=='amazonAuthority'&&!formInline.isdeveloper"  title="检测到你正在绑定授权,请选择当前要绑定的店铺!" type="error" show-icon />
		  <el-form :inline="true" :model="formInline" class="form-width-fill" label-width="auto">
		     <el-form-item label="店铺名称"   >
		       <el-select v-model="formInline.groupid" placeholder="请选择..." @change="selectStote">
		         <el-option v-for="(item,index) in storelist.list" :label="item.name" :value="item.id" />
		       </el-select>
		     </el-form-item>
			 <el-form-item  v-if="authtype=='manuAuthority' && !formInline.isdeveloper" label="站点"  >
			   <el-select v-model="formInline.marketplaceid" placeholder="同区域站点绑定一个,该区域所有站点都会绑定!" @change="selectCountry">
			     <el-option  v-for="(item,index) in market" :label="item.name" v-show="item.name !=='中国'" :value="item.marketplaceid" >
				 {{item.name}}<span class="font-extraSmall"> - {{item.regionName}}</span>
				 </el-option> 
			   </el-select>
			 </el-form-item>
			 <el-form-item  v-if="authtype=='amazonAuthority' || formInline.isdeveloper" label="站点" >
			   <el-select v-model="formInline.awsRegion" placeholder="同区域站点绑定一个,该区域所有站点都会绑定!" @change="selectCountry">
			          <el-option   label="北美"  value="us-east-1" ></el-option>
					  <el-option   label="欧洲"  value="eu-west-1" ></el-option>
					  <el-option   label="远东"  value="us-west-2" ></el-option>
			   </el-select>
			 </el-form-item>
			  <el-form-item  label="类型" >
				 <el-switch
					 v-model="formInline.isdeveloper"
					 class="ml-2"
					 :width="60"
					 style="--el-switch-on-color: #ff4949; --el-switch-off-color:  #13ce66"
					 inline-prompt
					 active-text="高级"
					 inactive-text="常规"
				   />
				</el-form-item>
				<el-form-item  v-if="!formInline.isdeveloper" label="授权链接" >
				 	        <el-button @click="handleChange" v-if="formInline.showurl==false" size="small" type="primary">查看</el-button>
							<el-alert v-if="formInline.showurl"  type="warning" @close="formInline.showurl=false"  class="mar-bot">
										  <template #default>
											 {{url}}
										  </template>  
							</el-alert>
							<div v-if="formInline.showurl" style="margin-top:5px">
								<el-button   type="primary" link  @click.stop="CopyText(url)">复制</el-button>
								<div class="font-extraSmall"  >若无法在店铺所在网络登录本网站，可以复制此链接授权</div>
							</div>
							
							 
				</el-form-item>    
				<el-form-item  v-if="formInline.isdeveloper" label="Seller Id" >
				 	 <el-input v-model="formInline.sellerid"></el-input>
				</el-form-item>    
				<el-form-item  v-if="formInline.isdeveloper" label="Client Id" >
					 <el-input v-model="formInline.clientId"></el-input>
				</el-form-item> 
				<el-form-item  v-if="formInline.isdeveloper" label="Client Secret" >
					 <el-input v-model="formInline.clientSecret"></el-input>
				</el-form-item> 
				<el-form-item  v-if="formInline.isdeveloper" label="Refresh Token" >
					 <el-input v-model="formInline.refreshToken"></el-input>
				</el-form-item> 
				<el-form-item  v-if="formInline.isdeveloper" label="Proxy Ip" >
					 <el-input v-model="formInline.proxyIp"></el-input>
				</el-form-item> 
				<el-form-item  v-if="formInline.isdeveloper" label="Proxy Port" >
					 <el-input v-model="formInline.proxyPort"></el-input>
				</el-form-item> 
		   </el-form>
		   <template #footer>
		     <span class="dialog-footer">
			    <div   v-if="formInline.isdeveloper" >
				       <el-button @click="authVisible = false">取消</el-button>
				       <el-button  type="primary" @click="submitDeveloperAuth"  >确认</el-button>
			    </div>
				 <div v-else>
					 <el-button @click="authVisible = false">取消</el-button>
					 <el-button v-if="authtype=='manuAuthority'" type="primary" @click="getamazonUrl"  >登录亚马逊授权</el-button >
					 <el-button v-if="authtype=='amazonAuthority'" type="primary" @click="subMitAuth"  >确认绑定</el-button >
				 </div>
		     </span>
		   </template>
		 </el-dialog>
</template>

<script setup>
	import {Help,Plus,MenuUnfold,SettingTwo} from '@icon-park/vue-next';
	import {ref,reactive,onMounted,toRefs,watch} from "vue"
	import {Edit,ArrowDown,Delete} from '@element-plus/icons-vue';
	import { ElDivider,ElMessageBox ,ElMessage } from 'element-plus'
	import authApi from '@/api/amazon/auth/authApi.js';
	import marketApi from '@/api/amazon/market/marketApi.js';
	import {dateFormat} from '@/utils/index.js';
	import CopyText from"@/utils/copy_text.js";
	import { useRoute } from "vue-router"
    let props = defineProps({storelist:[]});
    const { storelist} = toRefs(props);
	const emit = defineEmits(['comfirm']);
	const route = useRoute();
	let authVisible =ref(false)
	let market =ref()
	var authtype=ref('manuAuthority');
	let activeNames=ref("");
	let formInline = reactive({
		 groupid: '',
		 marketplaceid:'',
		 awsRegion:"us-east-1",
		 showurl:false,
		 isdeveloper:false,
	})
	let url=ref("");
	function initAuth(){
		if((route.query&&route.query.mws_auth_token)||(route.query&&route.query.path&&route.query.path.indexOf("mws_auth_token")>-1)){
			authVisible.value =true
		    authtype.value='amazonAuthority';	
		}
	}
	function handleChange(){
		formInline.showurl=true;
		loadUrl();
	}
	function selectStote(val){
		formInline.groupid = val
		loadUrl();
	}
	function selectCountry(val){
		formInline.marketplaceid = val;
		loadUrl();
	}
	function loadUrl(){
		let params={}
		params = formInline
		if(params.groupid&&params.marketplaceid){
			authApi.getAuthUrl(params).then((res)=>{
				url.value=res.data;
		  });
		}
	}
	function getamazonUrl(){
		let params={}
		params = formInline
		if(params.groupid&&params.marketplaceid){
			authApi.getAuthUrl(params).then((res)=>{
				ElMessageBox.confirm(
									    '为避免店铺关联，请确认当前设备环境是您授权店铺的常用环境！',
									    {
									      confirmButtonText: '前往授权',
									      cancelButtonText: '取消',
									      type: 'warning',
										  callback:(action)=>{
											 if(action=="confirm"){
												 window.open(res.data)
											 }
										  }
									    }
									  )
			})
		}else{
			ElMessage.error('请选择对应店铺或站点！');
		}
		
	}
	function subMitAuth(){
		var groupid=formInline.groupid;
		var awsregion =formInline.awsRegion;
		var state=groupid+"@"+awsregion;
		var mwstoken="";
		if(route.query.mws_auth_token){
			mwstoken=route.query.mws_auth_token;
		}else{
			var param=route.query.path.split("?")[1];
			var query=param.split("&");
			query.forEach(item=>{
				if(item.indexOf("mws_auth_token=")>-1){
					mwstoken=item.split("=")[1];
				}
			})
		}
		if(mwstoken==""){
			ElMessage.error("授权失败，无法获取到token");
			return ;
		}
		authApi.authSeller({"state":state,"selling_partner_id":route.query.selling_partner_id,
		"mws_auth_token":mwstoken,"spapi_oauth_code":route.query.spapi_oauth_code}).then((res)=>{
			if(res.data&&res.data.id){
				ElMessage.success("授权成功，请回到授权列表页面刷新。");
			}else{
				ElMessage.error( "授权失败，请再次尝试（注意：您必须在店铺所在的网络环境重新尝试）");
			}
		})
	}
	function submitDeveloperAuth(){
		formInline.AWSRegion=formInline.awsRegion;
		formInline.awsregion=formInline.awsRegion;
		if(formInline.awsRegion=="us-east-1"){
			formInline.region="NA";
		}else if(formInline.awsRegion=="eu-west-1"){
			formInline.region="EU";
		}else if(formInline.awsRegion=="us-west-2"){
			formInline.region="FE";
		}
		authApi.saveAuth(formInline).then((res)=>{
			ElMessage.success("授权成功！");
			authVisible.value =false;
			initAuth();
		});
	}
	function show(){
		authVisible.value =true
		loadUrl();
	}
	watch(props,()=>{
		 if(props.storelist&&props.storelist.list.length>0){
		 	formInline.groupid=props.storelist.list[0].id;
		 }else{
		 	formInline.groupid="";
		 }
		
	})
	function loadMarketAll(){
		marketApi.getMarketAll().then((res)=>{
			market.value = res.data
			if(res.data&&res.data.length>0){
				formInline.marketplaceid=res.data[0].marketplaceid;
			}
		})
	}
	onMounted(()=>{
		loadMarketAll()
		initAuth();
	})
	 defineExpose({ show })
</script>

<style>
</style>
<template>
	<el-dialog
	    v-model="visible"
	    title="重置密码"
	    width="500"
	    :before-close="handleClose"
	  >
	  <el-form>
	  	<el-form-item label="手机号码">
	  		<el-input placeholder="对应手机号码" v-model="mobile"></el-input>
	  	</el-form-item>
		<el-form-item label="验证码">
			<el-input v-model="code">
				  <template #append>
					  <el-button  :disabled="timeout" @click="handleSendCode">发送验证码{{title}}</el-button>
					  
					  </template>
			</el-input>
		</el-form-item>
	  	<el-form-item label="新密码">
	  		<el-input v-model="password" type="password" show-password ></el-input>
	  	</el-form-item>
	  </el-form>
	    <template #footer>
	      <div class="dialog-footer">
	        <el-button @click="visible=false">取消</el-button>
	        <el-button type="primary"   @click="handleSubmit()">
	          确认
	        </el-button>
	      </div>
	    </template>
	  </el-dialog>
	
</template>


<script setup>
import {Search,ArrowDown,Link} from '@element-plus/icons-vue'
	import {MenuUnfold,SettingTwo,Help,Copy,MoreOne} from '@icon-park/vue-next';
	import { ref,reactive,onMounted,watch,toRefs,nextTick } from 'vue'
	import {ElMessage,ElMessageBox } from 'element-plus';
	import userApi from "@/api/sys/admin/userApi.js"
	import supplierApi from '@/api/quote/supplierApi.js';
	const emit = defineEmits(['change']);
	const globalTableRef=ref();
	const state = reactive({ visible:false,timeout:false,title:"",mobile:"",password:"",code:"",token:"" })
	const{ visible,timeout,title,mobile,password,code,token }=toRefs(state);
	function handleSubmit(){
		userApi.verifySmsCode({"key":state.key,"code":state.code}).then((res)=>{
			if(res.data=="success"){
				var loginForm={"operate":"reset"};
				loginForm.mobile=state.mobile;
				loginForm.password=state.password;
				 supplierApi.submitlogin(state.token,loginForm).then(res=>{
				 	state.visible = false
				 })
				
			}else{
				 ElMessage.success("验证码错误");
			}
		});
		
	}
	 function timeOut(t){
		state.timeout=t;
		state.title=t+"秒";
		if(t>=1){
			setTimeout(function(){
				timeOut(t-1);
			},1000);	
		}else{
			state.timeout=false;
			state.title="";
		}
	 }
	 function handleSendCode(){
		 userApi.getSmsCodes({"mobile":state.mobile,"ftype":"losepassword"}).then((res)=>{
		 					if(res.data){
		 						state.key=res.data;
		 					}				 
		 }).catch(error=>{
		 					  
		 });
		
		 state.timeout=true;
		 timeOut(60)
	 }
	function show(mobile,token){
		    state.mobile=mobile;
			state.token=token;
			state.visible=true;
	}
 defineExpose({show});
</script>

<style>
</style>
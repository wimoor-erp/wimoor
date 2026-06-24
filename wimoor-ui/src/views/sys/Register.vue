<template>
	    <div class="login-bg">
			<el-scrollbar>
	        <div class="screen-height">
				<div class="login-content">
				<div class="left-content"></div>
				<el-card class="login-box">
	           <el-image src="/login-logo.png"></el-image>
			   <h4 class="m-t-24">欢迎注册Wimoor</h4>
	        <el-form ref="myform" :model="form" :rules="rules" size="normal" class="m-t-24">
	           <el-form-item label="手机号">
	               <el-input   v-model="form.account"  size="large"   placeholder="请输入手机" clearable>
	               </el-input>
	           </el-form-item>
	             <el-form-item label="验证码">
	        		 <div  style="flex:1" class="flex-center-between">
	        			<el-input style="margin-right:8px" v-model="form.code"   size="large" placeholder="请输入验证码"></el-input>
	        		   <el-button v-if="waitSecond>0" size="large" disabled type="primary" loading plain>需要等待<span>（{{waitSecond}}s)</span> </el-button>
	        		   <el-button v-else type="primary" size="large" @click.stop="sendSMSCode" plain>获取手机验证码 </el-button>
	        	   </div>
	        	</el-form-item >
	            <el-form-item label="密　码">
	            <el-input   v-model="form.oldpassword"  size="large"  show-password   placeholder="请输入密码">
	            </el-input>
	           <el-input style="margin-top: 15px;"  size="large"  v-model="form.password"  show-password   placeholder="请再次输入密码">
	           </el-input>
	            </el-form-item >
	        	<el-form-item  label="用户名">
	        	    <el-input   v-model="form.name"  size="large"  placeholder="请输入用户名" clearable>
	        	    </el-input>
	        	</el-form-item>
	        	<el-form-item label="邮　箱">
	        	    <el-input   v-model="form.email"  size="large"  placeholder="请输入邮箱" clearable>
	        	    </el-input>
	        	</el-form-item>
	        	<el-form-item label="公司名">
	        	    <el-input   v-model="form.company"  size="large"  placeholder="请输入公司名称" clearable>
	        	    </el-input>
	        	</el-form-item>
	            <el-form-item style="text-align: center;">
	                <el-button style="width: 100%;" type="primary" size="large" @click="userRegister">同意条款并注册</el-button>
	            </el-form-item>
	        </el-form>
			<div class="font-extraSmall text-center m-t-24  ">
					<div style="float:right"> 
						<el-link href="/clause.html" 
						type="primary" :underline="false"  target="_blank">
						《wimoor用户协议条款》
						</el-link>
					</div>
					<div style="float:left">
								 
							<el-link type="primary" @click="userLogin"  :underline="false"  class="font-12">
									已有帐号去登陆
							</el-link>
					</div>
			</div>
		      
	         </el-card>
	        </div>
	    </div>
		</el-scrollbar>
	 
				
			</div>
		 
</template>

<script setup>
import {ref,reactive,toRefs} from"vue"
import {  } from '@element-plus/icons-vue';
import {useRouter } from 'vue-router';
import {ElForm,ElMessage} from "element-plus";
import userApi from "@/api/sys/admin/userApi.js"
	const myform =ref(ElForm);
	let router = useRouter();
	const invitecode=router.currentRoute.value.query.invitecode;
	const key=router.currentRoute.value.query.key;
	const salekey=router.currentRoute.value.query.salekey;
	var validatePass = (rule, value, callback) => {
	      if (value === '') {
	        callback(new Error('请再次输入密码'))
	      } else if (value !== state.form.password) {
	        callback(new Error('两次输入密码不一致!'))
	      } else {
	        callback()
	      }
	    }
     const state=reactive({
     			form:{account:'',smscode:'',password:"",repassword:""},
     			waitSecond:0,
     			rules:{
     				//account: [  {validator: checkPhone, trigger: 'blur'} ],
     				oldpassword:[
     					{ required: true, message: '请输入密码', trigger: 'blur' },
     					{ pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,30}$/, message: '密码为数字，小写字母，大写字母，特殊符号 至少包含三种，长度为 8 - 30位 ' },
     				],
     				password: [ { required: true, validator: validatePass, trigger: 'blur' }  ]
     			 }
     });
	 const { form,rules,waitSecond} = toRefs(state);
        
          function userLogin(){
                 router.push("/login"); 
           }
		function reSendSMS(){
			state.waitSecond=state.waitSecond-1;
			if(state.waitSecond<=0){
				if(interval!=null){
					window.clearInterval(interval);
					interval=null;
				} 
			}
		}
		let interval=null;
		function sendSMSCode(){
			if(state.form.account!=""){
				// 手机发验证码
				state.waitSecond=60;
				interval= window.setInterval( reSendSMS,1000)
				 userApi.getSmsCodes({"mobile":state.form.account,"ftype":"register"}).then((res)=>{
					if(res.data){
						state.form.key=res.data;
					}				 
				 }).catch(error=>{
					 state.waitSecond=0;
					 window.clearInterval(interval);
				 });
			}else{
					ElMessage.error('手机号码不能为空');
			}
		}
		function userRegister(){
			if(!state.form.account || state.form.account==""){
				ElMessage.error('手机不能为空');
				return;
			}
			if(!state.form.email || state.form.email==""){
				ElMessage.error('邮箱不能为空');
				return;
			}
			if(!state.form.name || state.form.name==""){
				ElMessage.error('用户名不能为空');
				return;
			}
			if(!state.form.company || state.form.company==""){
				ElMessage.error('公司名称不能为空');
				return;
			}
			if(!state.form.password  || !state.form.oldpassword || state.form.password!=state.form.oldpassword){
				ElMessage.error('密码不能为空或者两次输入不一致');
				return;
			}
			if(!state.form.code){
				ElMessage.error('验证码不能为空');
				return;
			}
			state.phone=state.account;
			state.form.salekey=key?key:salekey;
			state.form.invitecode=invitecode;
			userApi.register(state.form).then((res)=>{
				ElMessage.success('注册成功');
				setTimeout(function(){
					router.push({
						path:'/login',
						query:{
						  title:"登录",
						  path:'/login',
						}
					})
				},500);
				
			});
		}
</script>


<style scoped>
  .login-bg{
	  background-image: url(../../assets/image/login-bg.png);
	  background-size: cover;
	  position: fixed;
	      left: 0;
	      right: 0;
	      top: 0;
	      bottom: 0;
  }
  .screen-height{
	  height: calc(100vh);
  }
  .login-content{
	  display: flex;
	  justify-content: center;
	  padding-top:5%;
  }
  .login-box{
	  width:560px;
	  padding:24px 32px;
	  border:none;
	  background-color:rgba(255,255,255,);
  }
  .font-12{
	  font-size: 12px;
  }
  .left-content{
	  width:36%;
  }
  .m-t-24{
	  margin-top: 16px;
  }
  .m-t-32{
	  margin-top:32px;
	  justify-content: center;
  }
  .text-gray{
	  color:var(--el-color-info)
  }
</style>
<template>
	<div class="gary-bg bg-height" >
	<el-row>
		<el-col 
		:xs="24" 
		:sm ="24" 
		:md ="12" 
		:lg ="12" 
		:xl="6"
		 style="margin:0 auto"
		 >
			<el-card class="box-top">
			<h3>忘记密码</h3>
			  <el-steps :active="active" finish-status="success">
			    <el-step title="身份验证" />
			    <el-step title="重置登录密码" />
			    <el-step title="完成" />
			  </el-steps>
			  <el-row>
			  <el-col :span="12" :offset="6">
			  <el-form ref="myform" label-width="100px" :model="form" :rules="rules">
				    <el-form-item v-if="active<1" label="手机号/邮箱" prop="account">
				        <el-input style="min-width:200px;"  v-model="form.account" />
				      </el-form-item>
				    <el-form-item v-if="active<1" label="验证码">
						<el-space>
							<el-input style="min-width:100px;" v-model="form.code">
							</el-input>
							<el-button v-if="waitSecond>0" disabled type="primary" loading plain>重新获取等待<span>（{{waitSecond}}s)</span> </el-button>
							<el-button v-else type="primary" @click.stop="sendSMSCode" plain>获取验证码 </el-button>
						</el-space>
				      </el-form-item>
					  <el-form-item v-if="active>0&&active<2" 
							prop="password"
	                        label="新密码">
					      <el-input 
						   v-model="form.password"
							type="password"
							style="min-width:100px;" 
							placeholder="请输入密码"
							show-password
							/>
					    </el-form-item>
					  <el-form-item v-if="active>0&&active<2" 
					          prop="repassword" 
							  label="确认密码"
							  style="padding-top:20px;" 
							>
					      <el-input v-model="form.repassword"
								   type="password"
								   style="min-width:100px;" 
								   placeholder="请输入确认密码"
								   show-password
								   />
					    </el-form-item>
			
					 						  
				     <el-form-item  v-if="active<2">
				        <el-button @click="next" type="primary">下一步</el-button>
				      </el-form-item>
					  <el-form-item v-else class="m-0-c">
					   <el-result
							   icon="success"
							   title="修改成功"
							   sub-title="请返回登录页面登录!"
							 >
							   <template #extra>
									<el-button type="primary" @click="toLoginPage">去登录</el-button>
							   </template>
							 </el-result>
					  </el-form-item>
			  </el-form>
			  </el-col>
			</el-row>  
			</el-card>
		</el-col>
	</el-row>
	</div>
</template>

<script setup>
	import {ref,reactive,toRefs} from"vue"
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	import userApi from "@/api/sys/admin/userApi.js"
	import {useRouter } from 'vue-router';
	import {checkPassword,checkEmail} from '@/utils/index.js';
	let router = useRouter()
	const myform =ref(ElForm);
	var checkPhone = (rule, value, callback) => {
	        if (!value) {
	          return callback(new Error('手机号不能为空'));
	        } else {
	          const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
	          if (reg.test(value)) {
	            callback();
	          } else {
	            return callback(new Error('请输入正确的手机号'));
	          }
	        }
	      }
		  
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
				active:0,
				waitSecond:0,
				rules:{
					// account: [  {validator: checkPhone, trigger: 'blur'} ],
					password:[
						{ required: true, message: '请输入密码', trigger: 'blur' },
						{ pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,30}$/, message: '密码为数字，小写字母，大写字母，特殊符号 至少包含三种，长度为 8 - 30位 ' },
					],
					repassword: [ { required: true, validator: validatePass, trigger: 'blur' }  ]
				 }
	});
	const { form,active,rules,waitSecond} = toRefs(state);
	let interval=null;
	function next(){
		if(state.active==0){
			userApi.verifySmsCode({"key":state.form.key,"code":state.form.code}).then((res)=>{
				if(res.data=="success"){
					state.active++;
				}
			});
		}else if(state.active==1){
			myform.value.validate((isValid) => {
				if (isValid) {
					userApi.updatePasswordForget(state.form).then(res=>{
						state.active=2;
					})
				 }
			});
		}
	}
	const reSendSMS=()=>{
			state.waitSecond=state.waitSecond-1;
			if(state.waitSecond<=0){
				if(interval!=null){
					window.clearInterval(interval);
					interval=null;
				} 
			}
	}
	function sendSMSCode(){
		if(state.form.account!=""){
			//邮箱发邮箱  手机发手机
			state.waitSecond=60;
			interval= window.setInterval( reSendSMS,1000)
			if(state.form.account.indexOf("@")>=0){
				 if(!checkEmail(state.form.account)){
					 ElMessage.warning('请输入正确的邮箱号！');
					 state.waitSecond=0;
					 window.clearInterval(interval);
					 return;
				 }    
				 userApi.getEmailCode({"email":state.form.account,"ftype":"losepassword"}).then((res)=>{
					 if(res.data){
						 state.form.key=res.data;
					 }
				 }).catch(error=>{
					 state.waitSecond=0;
					 window.clearInterval(interval);
				 });
			}else{
				 userApi.getSmsCodes({"mobile":state.form.account,"ftype":"losepassword"}).then((res)=>{
					if(res.data){
						state.form.key=res.data;
					}				 
				 }).catch(error=>{
					 state.waitSecond=0;
					 window.clearInterval(interval);
				 });
			}
		}else{
				ElMessage.warning('手机号码或者邮箱不能为空');
		}
	}
	function toLoginPage(){
		router.push({
			path:'/login',
			query:{
			  title:"登录",
			  path:'/login',
			}
		})
	}
</script>

<style>
	.bg-height{
		height: calc(100vh);
	}
	.box-top{
		margin-top: 10%;
		min-height:480px;
	}
	.box-top .el-steps{
	  margin: 48px;	
	}
	.m-0-c .el-form-item__content{
		margin-left:0!important;
		justify-content: center;
	}
</style>

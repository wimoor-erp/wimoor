<template>
	<div >
		<el-row>
			<el-col  :span="8" >
				<span class="font-base">账户</span>
			</el-col>
			<el-col  :span="6" >
				<span class="font-small">{{userData.account}}</span>
			</el-col>
			<el-col :span="4"  :offset="6" class="text-right">
				<el-link type="danger" v-if="usertype=='manager'" style="margin-right:20px;" :underline="false" @click="showMerge">迁移</el-link>
				<el-link type="primary" :underline="false" @click="changephone">更换</el-link>
			</el-col>
		</el-row>
		  <el-divider />
		  <el-row>
		  	<el-col  :span="8" >
		  		<span class="font-base">密码</span>
		  	</el-col>
		  	<el-col  :span="6" >
		  		<span class="font-small">已设置</span>
		  	</el-col>
		  	<el-col :span="4"  :offset="6" class="text-right">
		  		<el-link type="primary" :underline="false" @click = "changepassedword">修改</el-link>
		  	</el-col>
		  </el-row>
		   <el-divider />
		   <el-row>
		   	<el-col  :span="8" >
		   		<span class="font-base">邮箱</span>
		   	</el-col>
		   	<el-col  :span="6" >
		   		<span class="font-small" style="margin-right:10px">{{email}} </span>
				<el-tag v-if="state.userData.hasEmail==false" size="small" type="danger">未激活</el-tag>
				<el-tag v-else type="success" size="small">已激活</el-tag>
		   	</el-col>
		   	<el-col :span="4"  :offset="6" class="text-right">
				<el-link type="success" v-if="state.userData.hasEmail==false" :underline="false" style="margin-right:20px;" @click="showEmail('active')" >激活</el-link>
		   		<el-link type="primary" :underline="false" @click="showEmail('change')" >更换</el-link>
		   	</el-col>
		   </el-row>
		   <el-divider />
		   <el-row>
		   	<el-col  :span="8" >
		   		<span class="font-base">到期时间</span>
		   	</el-col>
		   	<el-col  :span="6" >
		   		<span class="font-small">{{dateFormat(losttime)}}</span>
		   	</el-col>
		   	<el-col :span="4"  :offset="6" class="text-right">
		   		 
		   	</el-col>
		   </el-row>
		   
		  
		   
	</div>
	<el-dialog v-model="phoneVisible" title="设置手机" destroy-on-close='true' width="560px" >
			<el-steps :active="active" finish-status="success">
			    <el-step title="密码验证" />
			    <el-step title="验证码" />
			    <el-step title="更换账号" />
			  </el-steps>
			 <div class="ph-box">
				 <div  v-if="step=='step1'">
				  <el-input v-model="userData.account" disabled >
					   <template #prepend>原账号</template>
				  </el-input>
				  <div class="flex-center">
					  <el-input v-model="oldpassword" type="password" show-password >
					  				 		  <template #prepend>账户密码</template>
					  </el-input>
				  </div>
				
				  <div class="flex-center">
					 <el-input v-model="oldcode" placeholder="请输入验证码" > </el-input>
					 <el-button type=""  :disabled="olddisable"  @click="sendOldCode" plain>{{oldtext}}</el-button>
					</div>
				 </div>
				 <div  v-else-if="step=='step2'">
				  <el-input v-model="newphone" >
				 					   <template #prepend>新手机号/邮箱</template>
				  </el-input>
				  <div class="flex-center">
						 <el-input v-model="newcode" placeholder="请输入验证码" > </el-input>
						 <el-button type="" :disabled="newdisable" @click="sendNewCode" plain>{{newtext}}</el-button>
						</div>
				 </div>
				 <div  v-else>
				  <div class="flex-center text-center">
				 		<el-result
				 		        icon="success"
				 		        title="修改成功!"
				 		      >
				 		      </el-result>
				 	</div>
				 </div>
			</div>
		<template #footer>
			<span class="dialog-footer">
				<div v-if="step !=='step3'">
				<el-button @click="phoneVisible = false">取消</el-button>
				<el-button type="primary" @click="nextstep" >下一步</el-button>
				</div>
				<el-button v-else  @click="successphone" >知道了</el-button>
			</span>
		</template>
	</el-dialog>
	<el-dialog v-model="passedwordVisible" title="设置密码" destroy-on-close='true' width="560px" >
				 <el-form :inline="true" :model="passedword" class="form-width-fill" label-width="auto">
				    <el-form-item label="旧密码"   >
				 	   <el-input  v-model="passedword.old" type="password" show-password  placeholder="请输入..." ></el-input>
				    </el-form-item>
				 		 <el-form-item label="新密码"  >
				 		   <el-input v-model="passedword.new"  type="password" show-password placeholder="至少8位数以上的数字或字母组合" ></el-input>
				 		 </el-form-item>
						 <el-form-item label="确认密码"  >
						   <el-input v-model="passedword.new2" type="password" show-password  placeholder="请输入..." ></el-input>
						 </el-form-item>
				  </el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="passedwordVisible = false">取消</el-button>
				<el-button type="primary" @click="submitpassedword" >确认</el-button>
			</span>
		</template>
	</el-dialog>
	
	<el-dialog v-model="mergeVisible" title="迁移数据" destroy-on-close='true' width="560px" >
				 <el-form :inline="true" :model="mergeForm" class="form-width-fill" label-width="auto">
				    <el-form-item label="目标账号"   >
				 	   <el-input  v-model="mergeForm.account"   placeholder="请输入目标账号" ></el-input>
				    </el-form-item>
				 		 <el-form-item label="目标账号密码"  >
				 		   <el-input v-model="mergeForm.password" type="password"  show-password placeholder="请输入密码" ></el-input>
				 	</el-form-item>
				  </el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="mergeVisible = false">取消</el-button>
				<el-button type="primary" @click="submitMerge" >确认</el-button>
			</span>
		</template>
	</el-dialog>
	
	<el-dialog v-model="emailVisible" :title="emailForm.title" destroy-on-close='true' width="560px" >
			<el-form :inline="true" :model="emailForm" class="form-width-fill" label-width="auto">
				<div class="flex-center">
					 <el-input  v-model="emailForm.email"   placeholder="请输入新邮箱" ></el-input>
				</div>
			   <div class="flex-center">
					 <el-input v-model="emailForm.code" placeholder="请输入验证码" > </el-input>
					 <el-button type="" :disabled="emaildisable" @click="sendEmailCode" plain>{{emailtext}}</el-button>
			  	</div>
			  
			 </el-form>	 
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="emailVisible = false">取消</el-button>
				<el-button type="primary" @click="submitEmail" >确认</el-button>
			</span>
		</template>
	</el-dialog>
	
</template>

<script setup>
	import {reactive,toRefs,ref}from"vue";
	import { ElMessage,ElMessageBox } from 'element-plus';
	import {dateFormat,dateTimesFormat,checkPassword,checkEmail} from '@/utils/index.js';
	import userApi from '@/api/sys/admin/userApi.js';
	import limitApi from '@/api/sys/admin/limitApi.js';
	const emit = defineEmits(['change']);
	const state = reactive({
		phoneVisible:false,
		passedwordVisible:false,
		step:"step1",
		newphone:"",
		newcode:'',
		oldphone:'',
		oldcode:'',
		active:0,
		passedword:{
			old:'',
			new:'',
			new2:'',
		},
		mergeForm:{},
		emailForm:{},
		email:'',
		losttime:null,
		oldpassword:'',
		userData:{},
		oldkey:'',
		newkey:'',
		mergeVisible:false,
		emailVisible:false,
		usertype:'',
		oldtext:'获取验证码',
		newtext:'获取验证码',
		emailtext:'获取邮箱验证码',
		emaildisable:false,
		newdisable:false,
		olddisable:false,
		invitecode1:'',
		invitecode2:'',
		invitenumber:'',
		packageData:null,
	})
	const {
		emaildisable,
		newdisable,
		olddisable,
		oldtext,
		newtext,
		emailtext,
		phoneVisible,
		passedwordVisible,
		step,
		active,
		passedword,
		email,
		losttime,
		newphone,
		newcode,
		oldphone,
		oldcode,
		oldpassword,
		userData,
		oldkey,
		newkey,
		mergeVisible,
		mergeForm,
		emailVisible,
		emailForm,
		usertype,
		invitecode1,
		invitecode2,
		invitenumber,
		packageData,
	}=toRefs(state)
	

	
	function submitpassedword(){
		//先校验 旧密码
		if(!checkPassword(state.passedword.new2)){
			ElMessage.error('输入的密码不符合规格！至少8位数以上的数字或字母组合。');
			return;
		}
		if(state.passedword.new2!=state.passedword.new){
			ElMessage.error('两次输入的密码不一致！');
			return;
		}
		 
		userApi.updatePasswordSelf({"account":state.userData.account,
		"oldpassword":state.passedword.old,"password":state.passedword.new}).then((res)=>{
			ElMessage.success( '修改成功！');
			state.passedwordVisible=false;
		});
		 
	}
	
	  let countdown = 60;
	  function setTime(ftype) {
	      if (countdown == 0) {
			  if(ftype=="old"){
				  state.olddisable=false;
				  state.oldtext="获取验证码";
			  }else if(ftype=="new"){
				  state.newdisable=false;
				  state.newtext="获取验证码";
			  }else{
				  state.emaildisable=false;
				  state.emailtext="获取邮箱验证码";
			  }
	          countdown = 60;//60秒过后button上的文字初始化,计时器初始化;
	          return;
	      } else {
			  var title="("+countdown+"s)后重新发送";
			  if(ftype=="old"){
				  state.olddisable=true;
				  state.oldtext=title;
			  }else if(ftype=="new"){
				  state.newdisable=true;
				  state.newtext=title;
			  }else{
				  state.emaildisable=true;
				  state.emailtext=title;
			  }
	          countdown--;
	      }
	      setTimeout(function() { setTime(ftype) },1000) //每1000毫秒执行一次
	  }
	
	function sendEmailCode(){
		if(state.emailForm.email.indexOf("@")>=0 && state.emailForm.email.indexOf(".")<0){
			ElMessage.error( '请输入正确的邮箱地址')
			return;
		}
		setTime("email");
		userApi.getEmailCode({"email":state.emailForm.email,"ftype":state.emailForm.ftype}).then((res)=>{
			 if(res.data){
				 state.emailForm.key=res.data;
			 }
		});
	}
	function submitEmail(){
		userApi.updateEmailSelf(state.emailForm).then((res)=>{
			 ElMessage.success( '修改成功！');
			 emit("change");
			state.emailVisible=false;
		});
	}
	 function showMerge(){
		ElMessageBox.confirm(
			'此操作将本账户下所有数据迁移至指定账户,具有非常高的风险,且此操作不可逆,是否要继续?',
			{
			  confirmButtonText: '继续',
			  cancelButtonText: '取消',
			  type: 'warning',
			  callback:(action)=>{
				 if(action=="confirm"){
					 state.mergeVisible=true;
				 }
			  }
			}
		  )
	 }
	 function submitMerge(){
		 //mergeForm
		 userApi.mergeAccount(state.mergeForm).then(res=>{
			 ElMessage.success( '迁移成功！');
			  state.mergeVisible=false;
		 })
	 }
	function changephone(){
		state.phoneVisible = true
	}
	function successphone(){
		state.phoneVisible = false;
		state.step = "step1";
		state.active = 0;
	}
	function changepassedword(){
		state.passedwordVisible = true;
	}
	function sendOldCode(){
		if(state.userData.ftype=="email" && state.userData.account.indexOf("@")>=0 && state.userData.account.indexOf(".")<0){
			ElMessage.error( '账户邮箱异常,请联系主账号修改！');
			return;
		}
		if(state.oldpassword=="" || state.oldpassword==null || state.oldpassword==undefined){
			ElMessage.error('输入账户密码才能获取验证码！');
			return;
		}
		//需要验证密码+旧手机验证码
		var data={};
		data.oldpassword=state.oldpassword;
		data.account=state.userData.account;
		userApi.verifyPassword(data).then((res)=>{
			if(res.data==true){
				 //邮箱发邮箱  手机发手机
				 setTime("old");
				 if(state.userData.ftype=="email"){
					 userApi.getEmailCode({"email":state.userData.account,"ftype":"changephone"}).then((res)=>{
						 if(res.data){
							 state.oldkey=res.data;
						 }
					 });
				 }else{
					 userApi.getSmsCodes({"mobile":state.userData.account,"ftype":"changephone"}).then((res)=>{
					 	if(res.data){
					 		state.oldkey=res.data;
					 	}				 
					 });
				 }
			}else{
				  ElMessage.error('请输入正确的账户密码')
			}
		});
	}
	
	function sendNewCode(){
		//邮箱发邮箱  手机发手机
		if(state.userData.ftype=="email" && state.newphone.indexOf("@")>=0 && state.newphone.indexOf(".")<0){
			ElMessage.error(  '请输入正确的邮箱！')
			return;
		}
		setTime("new");
		if(state.userData.ftype=="email"){
			 userApi.getEmailCode({"email":state.newphone,"ftype":"changephone"}).then((res)=>{
				 if(res.data){
					 state.newkey=res.data;
				 }
			 });
		}else{
			 userApi.getSmsCodes({"mobile":state.newphone,"ftype":"changephone"}).then((res)=>{
				if(res.data){
					state.newkey=res.data;
				}				 
			 });
		}
		
	}
	function nextstep(){
		//验证旧手机/邮箱验证码
		if(state.active==0){
			userApi.verifySmsCode({"key":state.oldkey,"code":state.oldcode}).then((res)=>{
				if(res.data=="success"){
					state.step = "step2";
					state.active++;
				}
			});
		}
		//需要验证新手机验证码
		if(state.active==1){
			var data={};
			data.key=state.newkey;
			data.code=state.newcode;
			data.account=state.newphone;
			data.password=state.oldpassword;
			 userApi.updateAccountSelf(data).then((res)=>{
				 if(res.data){
					 state.step = "step3";
					 state.active++;
					 emit("change");
				 }
			 });
		}
	}
	function showEmail(ftype){
		if(ftype=="active"){
			state.emailForm.title="激活邮箱";
			state.emailForm.ftype="active";
			state.emailForm.email=state.email;
		}else{
			state.emailForm.title="修改邮箱";
			state.emailForm.ftype="changephone";
			state.emailForm.email="";
		}
		state.emailVisible=true;
	}
	function show(datas){
		if(datas.tel){
			state.oldphone=datas.tel;
		}
		if(datas.email){
			state.email=datas.email;
		}
		if(datas.user){
			state.usertype=datas.usertype;
			state.losttime=datas.user.losingeffect;
			state.userData=datas.user;
		}
		userApi.showInvitePage().then((res)=>{
			var data=res.data;
			state.invitecode1='http://'+location.host+'/register?key='+data.invitecode;
			state.invitecode2='https://wimoor.com?key='+data.invitecode;
			state.invitenumber=data.invitenumber;
		});
		
	}
	defineExpose({
		show,
		invitecode1,
		invitecode2,
		invitenumber,
	})
	 
</script>

<style scoped="scoped">
	.font-small{
		color:#999;
	}
	.ph-box{margin-top:32px;}
	.el-row{margin-top: 16px;margin-bottom: 16px;}
	.flex-center{
		display: flex;
		margin-top:16px;
	}
	.flex-center .el-input{
		margin-right:8px;
	}
	.text-center{
		justify-content: center;
	}

</style>

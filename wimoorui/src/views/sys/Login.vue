<template>
    <div class="login-bg">
		<el-scrollbar>
        <div class="screen-height">
			<div class="login-content">
			<div class="left-content"></div>
			<el-card class="login-box">
           <el-image src="/login-logo.png"></el-image>
		   <h4 class="m-t-24">欢迎登录Wimoor</h4>
            <el-form class="m-t-24" ref="form" :model="form" size="normal">
                <el-form-item>
                    <el-input :prefix-icon="User" v-model="form.account"  @keydown.enter="userLogin" :autofocus="true" size="large"  placeholder="请输入手机号或邮箱" clearable>
                         <template #prefix>
                        <el-icon   class="font-medium el-input__icon"><user /></el-icon>
                         </template>
                    </el-input>
                </el-form-item>
                <el-form-item>
                <el-input :prefix-icon="Lock" v-model="form.password"  @keydown.enter="userLogin" size="large" show-password   placeholder="请输入密码">
                    <template #prefix>
                        <el-icon  class="font-medium el-input__icon"><lock /></el-icon>
                    </template>
                </el-input>
                </el-form-item>
				<el-form-item style="text-align: center;">
					  <el-button style="width: 100%;margin-top: 16px;" type="primary" size="large" @click="submitLogin">登录</el-button>
					  <div class="flex-center-between" style="flex:1;margin-top: 8px;" >
					      <el-button  type="info"  link   @click.stop="bindMFA">	MFA加密登录</el-button>
					  	 <el-button    type="info"  link @click="goResetPassword">忘记密码</el-button>
					  </div>
				</el-form-item>
            </el-form>
			<div class="font-extraSmall text-center m-t-64 flex-center ">
			<span>没有账号吗&nbsp;</span>
			<el-link type="primary" @click="userRegister"  :underline="false"  class="font-12">
			注册新账号
			</el-link>
			</div>
         </el-card>
        </div>
    </div>
	</el-scrollbar>
	 <el-affix position="bottom" :offset="20">
		 <div class="text-center filing-number">
	   <span class="font-extraSmall">
	   	Copyright © 2017 深圳市万墨信息科技有限公司版权所有 | 粤ICP备19045760号
	   </span>
	   </div>
	  </el-affix>

	</div>
 <el-dialog
    v-model="dialogVisible"
    title="MFA 登录"
    width="350" 
    :before-close="handleClose"
  >
         <el-space direction="vertical"	 alignment="normal" v-if="isshow">
			 <el-switch
			   v-model="mfaState"
			   @change="mfaOpen"
			   active-text="MFA绑定"
			 />
			 <div class="font-extraSmall" style="margin-bottom:24px">首次登录需绑定MFA,再次绑定需重新扫码！</div>
		 </el-space>
	   <el-space direction="vertical" alignment="normal" v-else >
		   <div>请使用Google Authenticator扫描二维码</div>
		   <div class="font-extraSmall">可使用微信搜索兼容Google Authenticator的密码验证器</div>
    <el-image 
	:src="mfacode"></el-image>

	</el-space>
	<el-input :prefix-icon="Lock" v-model="form.mfa" clearable  @keydown.enter="userLogin" size="large"     placeholder="MFA 编码">
	     <template #prefix>
	         <el-icon  class="font-medium el-input__icon"><lock /></el-icon>
	     </template>
	 </el-input>
	<div class="font-extraSmall m-t-8">Google Authenticator中获取MFA编码</div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
		 <el-button  type="primary"  @click="submitLogin">登录</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import { Lock, User } from '@element-plus/icons-vue';
import request from "@/utils/request.js"; 
import store from '@/store/index';
import loginApi from '@/api/sys/login.js';
import {ElMessage} from "element-plus";
    export default {
        name: "Login",
        components:{
            User,Lock
        },
        data(){
            return{
                form:{},
                dialogVisible:false,
				mfacode:"",
				mfaState:false,
				isshow:true,
            }
        },
        methods:{
			userRegister(){
				 this.$router.push("/register"); 
			},
			submitLogin(){
				if(this.$route.query&&this.$route.query.openid&&this.$route.query.appType){
					this.thirdValid();
				}else{
					this.userLogin();
				}
			},
	
			bindMFA(){
				var self=this;
				self.dialogVisible=true;

				
			},
			thirdValid(){
					let self=this;
					var openid=self.$route.query.openid;//uni.getStorage('openid');
					var sessionid=self.$route.query.sessionid;//uni.getStorage('JSessionId');
					var data={'openid': openid,
					          'email':self.form.account,
							  'password':self.form.password};
					data.jsessionid=sessionid;
					data.appType=self.$route.query.appType;
					loginApi.verifyWechatApp(data).then(res => {
						              var data=res.data?res.data:res;
									   if(data){
											 let jsid=data.jsessionid;
											 localStorage.setItem("jsessionid",jsid);
											 var time=new Date().getTime();
											 localStorage.setItem("jsessiontime",time);
											 store.dispatch("jessionStore/setJessionTime",time);
											 localStorage.setItem("logintype",self.$route.query.appType);
											 store.commit('setSessionid',jsid);
											 localStorage.setStorage("JSessionId", jsid);
											 self.$router.push("/home"); 
									 }else{
										 this.$message({
														type:"error",
															message:"请输入正确的账号或密码！!"+res.msg
											 });
										 
										}
							    });
			},
            userLogin(){
                 request.post("/admin/api/v1/auth/login",this.form).then(res=>{
                    if(res.data){
                        this.$message({
                            type:"success",
                            message:"登录成功!"
                        });
						let path =sessionStorage.getItem("old_url_before_login");
						localStorage.setItem("jsessionid",res.data.session);
						var time=new Date().getTime();
						localStorage.setItem("jsessiontime",time);
						store.dispatch("jessionStore/setJessionTime",time);
						localStorage.setItem("logintype","api");
						if(path){
								if(path.indexOf("auth/getJSession")>0||path.indexOf("ssologin")>0||path=="/"||path=="/login"){
									this.$router.push("/home"); 
								}else if(this.$router.resolve(path).matched.length>0){
									this.$router.push(path);
								}else{
									this.$router.push("/home"); 
								}
						}else{
							  this.$router.push("/home"); 
						}		             
                    }else{
						if(res.msg){
							 this.$message({
								type:"error",
									message:"登录失败!"+res.msg
								});
						  }else{
							this.$message({
								type:"error",
									message:"登录失败!"
								});
						}
                            
                    }
                });
            },
			goResetPassword(){
				 this.$router.push("/resetPassword"); 
			},
			mfaOpen(){
				request.post("/admin/api/v1/auth/mfacode",this.form).then(res=>{
					this.mfacode=res.data;
				});
				this.$nextTick(() => {
				  this.isshow = false;
				});
			}
        },
		mounted() {
			    let jsessionid= localStorage.getItem("jsessionid");
			    console.log(jsessionid)
			     if(jsessionid){
			         	 this.$router.push("/home"); 
			       }
			  }
    }
</script>

<style scoped>
	.filing-number{
		opticy:0.8;
	}
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
	  padding-top: 10%;
  }
  .login-box{
	  width:480px;
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
	  margin-top: 24px;
  }
  .m-t-64{
	  margin-top:64px;
	justify-content: center;
  }
  .text-gray{
	  color:var(--el-color-info)
  }
</style>
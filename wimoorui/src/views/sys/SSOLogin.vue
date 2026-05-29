<template>
   <div class="login-check-wrapper">
	   <div class="login-image">
		   <div class="login-message">
			   <el-image :src="$require('loginMessage.png')"></el-image>
		   </div>
		   <div class="login-web">
			   <el-image :src="$require('loginweb.png')"></el-image>
		   </div>
		   <div class="login-under-bg"></div>
	   </div>
	   <div class="login-text">
		   登录信息检查...
	   </div>
   </div>
</template>

<script>
import { Lock, User } from '@element-plus/icons-vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import loginApi from '@/api/sys/login.js';
    export default {
        name: "SSOLogin",
        components:{
            User,Lock
        },
        data(){
            return{
                form:{},
            }
        },
        methods:{
            async userLogin(){
                   var jsessionid = this.$route.query.jsessionid;
                   let path =sessionStorage.getItem("old_url_before_login");
		            if(jsessionid){
		                 localStorage.setItem("jsessionid",jsessionid);
						 localStorage.setItem("jsessiontime",new Date());
						 localStorage.setItem("logintype","sso");
						 let authserver=  localStorage.getItem("authserver");
						 if(!authserver){
							 this.ssologinisrun().then((result)=>{
								 if(result.data!="false"){
								 	  localStorage.setItem("authserver",result.data);
								 }
							 }).catch(e=>{
								  this.$router.push("/login");
							 });
						 }  
		                 if(path){
							if(path.indexOf("auth/getJSession")>0||path.indexOf("ssologin")>0||path=="/"){
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
		            	jsessionid=localStorage.getItem("jsessionid");
		            	if(jsessionid){
							let authserver=  localStorage.getItem("authserver");
							if(!authserver){
								 this.ssologinisrun().then((result)=>{
									 if(result.data!="false"){
										  localStorage.setItem("authserver",result.data);
									 }
								 }).catch(e=>{
								  this.$router.push("/login");
							    });
							} 
	                       if(path){
								if(path.indexOf("auth/getJSession")>0||path.indexOf("ssologin")>0||path=="/"){
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
							 this.ssologinisrun().then(result=>{
								 if(result.data!="false"){
								 	  localStorage.setItem("authserver",result.data);
								 	  location=result.data+"/getJSession";
								 }else{
								 	 this.$router.push("/login");
								 }
							 }).catch(e=>{
								  this.$router.push("/login");
							 });
							
		            	}
		            }
            
            },
			 ssologinisrun(){
			       return request.post("/admin/api/v1/auth/ssologinisrun");
			 },
			 autoLoginWeixinWeb(){
			 	var self=this;
			  	loginApi.loginWechat({
			  			code: self.$route.query.code,
			  			appType:"mp"
			  		}).then(res => {
			  		var data=res.data?res.data:res;
			 		   localStorage.setItem("openid",data.openid);
			  		if (data && data.status != 'isfail') {
			  			let jsid = data.jsessionid;
			  			let currentuser = data.currentuser;
			 			localStorage.setItem("jsessionid",jsid);
			 			localStorage.setItem("jsessiontime",new Date());
			 			localStorage.setItem("logintype","mp");
			  			self.$router.push("/home"); 
			  		}  
			  	}).catch(e=>{
			 		var data=e.data;
			 		if (data.openid) {
			 			self.$router.push({path: "/login",
			 								query: {
			 									openid: data.openid,
			 									sessionid:data.jsessionid,
			 									appType:"mp",
			 									title: "登录",
			 									path:"/login",
			 								}});
			 		} 
			 	});
			  },
			autoLoginFeiShuWeb(){
				var self=this;
			 	loginApi.loginWechat({
			 			code: self.$route.query.code,
			 			appType:"feishu"
			 		}).then(res => {
			 		var data=res.data?res.data:res;
					   localStorage.setItem("openid",data.openid);
			 		if (data && data.status != 'isfail') {
			 			let jsid = data.jsessionid;
			 			let currentuser = data.currentuser;
						localStorage.setItem("jsessionid",jsid);
						localStorage.setItem("jsessiontime",new Date());
						localStorage.setItem("logintype","feishu");
			 			self.$router.push("/home"); 
			 		}  
			 	}).catch(e=>{
					var data=e.data;
					if (data.openid) {
						self.$router.push({path: "/login",
											query: {
												openid: data.openid,
												sessionid:data.jsessionid,
												appType:"feishu",
												title: "登录",
												path:"/login",
											}});
					} 
				});
			 }
        },
        mounted() {
			    var jsessionid=localStorage.getItem("jsessionid");
			   if(this.$route.query.state=="feishu"&&!jsessionid){
				    this.autoLoginFeiShuWeb()
			   } if(this.$route.query.state=="mp"&&!jsessionid){
				    this.autoLoginWeixinWeb()
			   }else{
				    this.userLogin();
			   }
			  
			  }
    }
</script>

<style scoped>
  .login-check-wrapper{
	  display: flex;
	  align-items: center;
      justify-content: center;
	  flex-direction: column;
	  height:100%;
  }
  .login-image{
	  position: relative;
  }
  .login-message{
	  position: absolute;
	  z-index: 1;
	  right:28px;
	  bottom: 0px;
  }
  .login-under-bg{
	  width: 100px;
	  height: 100px;
	  border-radius: 50%;
	  background-color: #FFF3F3;
	  position: absolute;
	  z-index: -1;
	  bottom:-10px;
  }
  .login-text{
	  color:rgba(0,0,0,0.6)
  }
  
  @keyframes text{
	    0% {  
	      transform: rotate3d(1, 0, 0, 50deg) scale(1); /* 初始大小，即原始大小 */  
	    }  
	    50% {  
	      transform: rotate3d(1, 0, 0, 50deg) scale(0.9); /* 元素大小放大到原始大小的1.5倍 */  
	    }  
	    100% {  
	      transform: rotate3d(1, 0, 0, 50deg) scale(1); /* 返回到原始大小 */  
	    }  
  }
  .login-under-bg{
	  animation: text 2s ease-in-out infinite;
  }
  
  @keyframes web {
  	0%,100%{
		 transform: translateY(0);
	}
	50%{
		transform: translateY(-30px);
		
	}
  }
  .login-web{
	   animation: web 2s linear infinite;
  }
  @keyframes message {
  	0%,100%{
  		 transform: translateY(0);
  	}
  	50%{
  		transform: translateY(-30px);
  		
  	}
  }
  .login-message{
  	   animation: message 2s ease-in-out infinite;
  }
</style>
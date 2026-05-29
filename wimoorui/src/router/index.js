import { createRouter, createWebHistory} from 'vue-router';
import Layout from '../layout/index.vue'
import {menuApi} from '@/api/sys/admin/menuApi.js';
import store from '@/store/index.js';
import sysRouter from './modules/sysRouter.js';
import erp from './modules/erp.js';
import amazon from './modules/amazon.js';
import finance from './modules/finance.js';
import axios from 'axios';
let modules = import.meta.glob('../views/**/*.vue')
const routes = [
  {
    path: '/',
    name: 'index',
    component: Layout,
    redirect:"/ssologin",
    children:[
      {
        path: 'home',
        name: 'Home',
		meta:{ keepAlive: true },
        component: ()=>import("@/views/dashboard/index.vue")
      },
	  {
	    path: 'blank',
	    name: 'blank',
	  		meta:{ keepAlive: true },
	    component: ()=>import("@/views/sys/blank/index.vue")
	  },
	  {
	  	  path:"usercenter",
	  	  name:"UserCenter",
	  	  component: ()=>import("@/views/sys/userCenter/index.vue")
	  },
	...erp,
	...amazon,
	...finance,
    ]
  },
...sysRouter
]
const router = createRouter({
  mode:'hash',
  history: createWebHistory(),
  routes
}) 
const loadflog={hasload:false}
const whitePath = ["/ssologin", "/authresult",'/login',"/register","/resetPassword","/quote"]

router.beforeEach(async(to,from,next)=>{
  //跳转前 获取到当前页面的路由
   if (whitePath.includes(to.path)) {  // 判断该路由是否需要登录权限
       if(!sessionStorage.getItem("old_url_before_login")){
		     sessionStorage.setItem("old_url_before_login",from.path);
	   }
	   next(); 
   }else{
		if(store&&store.state.routerStore&&store.state.routerStore.router&&store.state.routerStore.router.length>0) {
			if(to.path=="/home"){
				document.title = "首页"
			}else{
				document.title = to.query.title
			}
			next(); 
		}else {
			 let jsessionid = localStorage.getItem("jsessionid");
			    loadflog.hasload=true;
	   	         await axios({
					method:"get",
					headers:{'Content-Type':'application/json;charset=utf-8','jsessionid':jsessionid},
					url: '/api/admin/api/v1/menus/route'
				}).then(res=>{
					var result=res.data;
					if (result==undefined || result.code == 'A0231'||result.code == 'A0200'||result.code == 'S0003'||result.code == 'S0002'||result.code == 'S0001') {
							localStorage.removeItem("jsessionid");
							if(!sessionStorage.getItem("old_url_before_login")){
							   sessionStorage.setItem("old_url_before_login",window.location.pathname+window.location.search);
							}
							 router.push("/ssologin");
					} 
				    else{
							store.dispatch("routerStore/setRouter",result.data);
							if(result.data&&result.data.length>0){
								var permission=new Set(result.data[0].meta.permissions);
								store.dispatch("permissionStore/setPermission",permission);
							}
							addRoute(router,result.data);
                        }  
				}).catch(error=>{
					localStorage.removeItem("jsessionid");
					sessionStorage.setItem("old_url_before_login",window.location.pathname+window.location.search);
				    setTimeout(function(){
						window.location=window.location.origin+"/ssologin";
				    },1000);
			   });
			if(router.resolve(to.path).matched.length>0){
	   	       next({...to,replace:true});  
			}else{
				router.push("/home");
			}
	   }
   }

   
  
})
function addRoute(router,data,routerData){
	 data.forEach(v=>{
		 let item={};
		 var needroute=false;
		 item.path= 'home';
		 item.name= v.name;
		 if(v["redirect"]){
			 if(v.redirect.indexOf(".do")>0||v.redirect.indexOf(".jsp")>0){
				//这种属于外部菜单与本系统路由无关 item.path= 'https://erp.wimoor.com'+v.redirect; 
			 }else{
				 item.redirect=v.redirect;
				 needroute=true;
			 }
		 }
		 if(v["path"]){
			item.path= v.path;
			needroute=true;
		 }
		 if(v["component"]){
			  item.component = modules[`../views/${v.component}.vue`]
		 }
		 item.meta=v.meta;
		 if(needroute){
			 if(item.meta){
				 item.meta.keepAlive=true;
			 }else{
				 item.meta={ keepAlive: true, };
			 }
			 if(['/sys/notepad','/fin/voucher/create'].includes(item.path)){
				item.meta.keepAlive=false;
			 }
			 router.addRoute('index',item);
			 router.options.routes.push(item);
		 }
		 if(v["children"]){
			 addRoute(router,v.children);
		 }
	 });
}
export default router

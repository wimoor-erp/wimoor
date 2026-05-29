<template>
    <div id="menubar" :class="theme" >
		 <el-scrollbar height="calc(100vh)" >
			 <div class="aside-flex" style="height:calc(100vh)">
			 <div>
			 <div class="logo pointer" @click="goHome">
			 <el-image 
			   :src="imgPath"
			 ></el-image>
			 </div>
		   <el-menu 
		           :default-active="activeIndex"
		           class="el-menu-vertical"
		           :default-openeds="[1]">
		         <!--主菜单-->  
		       <el-menu-item  
			       @mouseleave="laveMenu"   
			       @mouseenter="changeMenu(item.id,index)" 
				   @click="changeDrawer(item)" 
				   v-for="(item,index) in menuData.list" 
				   :key="item.id" :index='index+""' >
					 <icon-park 
						v-if="theme=='themeLight'"
						:type="item.iconName" 
						 theme="outline" 
						 :size="18" 
						:strokeWidth="3"/>
					<icon-park
					   v-else
					   :type="item.iconName" 
						theme="outline" 
						:size="18" 
					   :strokeWidth="4"/>
		           <span>{{item.name}}</span>
		       </el-menu-item>
		      
		   </el-menu>
		   </div>
		   <div>  
			<HeaderPlatform/>
		   </div>
		    </div>
		  </el-scrollbar>
       <!--弹出层!-->
       <el-drawer v-model="drawer"   :size="drawerSize" direction="ltr" title="子菜单列表" :with-header="false" :destroy-on-close='true'>
          <div 
			 :class="'menu-item '+felxcolumn"
			 v-show='submenu.isShow' v-for="submenu in submenus.list" :key="submenu.id">
                 <div class="item" v-for="subgroup in submenu.menugroup" :key="subgroup.id">
					 <div class="menu-group  flex-center" >
						 <span class="font-extraSmall">{{subgroup.name}}</span>
					 <el-icon
					class="pointer icon-pos"
					:class="subgroup.rotate"
					 @click="handleExpend(subgroup)"
					 :size="10" color="#acb0b9"><ArrowRightBold /></el-icon>
					</div>
					<transition-group name="expend">
					 <div 
					 v-show="!subgroup.isShow"
					 v-for='sub in subgroup.namegroup' :key='sub.id'>
					 <a class="text" v-if="((sub.isout))" :href="sub.path" target="_blank">
						 {{sub.name}}
					 </a>	 
                     <router-link v-if="(!(sub.isout))" class="text"  active-class="active" :to="{
                          path:sub.path,
                          query:{
                              title:sub.name,
                              path:sub.path,
                          }
                      }">{{sub.name}}</router-link> 
                    </div>
					</transition-group>
                 </div>
           </div>
       </el-drawer>
    </div>
</template>

<script>
import {IconPark} from '@icon-park/vue-next/es/all';
import {Commodity,SalesReport,ShoppingBag,Home,Ship,Ad,Finance,HeadsetOne,Config,Workbench,Calculator,Cruise,HomeTwo} from '@icon-park/vue-next';
import {ArrowRightBold}from"@element-plus/icons-vue"
import { defineComponent,ref,reactive,watch,onMounted } from 'vue'
import {  useRoute } from 'vue-router'
import {menuApi} from "@/api/sys/admin/menuApi.js";
import {menuDataModel,submenulistModel,allMenuModel} from '@/model/sys/admin/menu.js';
import HeaderPlatform from "../Header/HeaderPlatform.vue";
import router from "@/router";
const imgPath = new URL("@/assets/image/common/color-logo.png", import.meta.url).href;
export default defineComponent({
        name: "AsideMenu",
        components:{
			Cruise, HeaderPlatform,
			HomeTwo, 
			ArrowRightBold,
            Commodity,
            SalesReport,
			Calculator,
            ShoppingBag,
            Home,
            Ship,Ad,Finance,
            HeadsetOne,Config,Workbench,
            IconPark,menuApi,menuDataModel,submenulistModel
        },
    setup() {
		   let timer = ''
           let drawer = ref(false);
		   let activeIndex=ref();
		   let drawerSize=ref(176);
		   let theme=ref("themeBlack");
		   let felxcolumn=ref("felxcolumn");
           let menuData=reactive({list:menuDataModel});
           let submenus=reactive({list:submenulistModel});
        	   menuApi.getMenuALL(response=>{
  	      	     menuData.list=response.menuData;
  	      	     submenus.list=response.submenus;
  	          });
           //监听路由
           let route = useRoute();
	       watch(() =>route.path,()=>{
	            drawer.value = false;
	        });
			function changeDrawer(menu){
				 resizeDrawer(menu);
				if(menu.path){
					  router.replace({name:menu.id});
				}else{
				      drawer.value=true;
				}
			}
        //方法
		if( localStorage.getItem("theme")){
			theme.value=localStorage.getItem("theme");
		}
		 function goHome(){
			 if(theme.value=="themeBlack"){
				  theme.value="themeLight";
				  localStorage.setItem("theme","themeLight");
			 }else{
				 theme.value="themeBlack";
			     localStorage.setItem("theme","themeBlack");
			 }
			 router.push({ 'path':'/home',
							 query: {
								title: "首页",
								path: "/home",
								name: 'Home',
								closable:false,
								replace:false
							 },
							 closable:false,
							 replace:false,
							 meta:{ keepAlive: true }
						   });
						   drawer.value = false
		 }
         function changeMenu(id,currentIndex){
            timer =setTimeout(()=>{
			 submenus.list.forEach(submenu => {
			            submenu.isShow = false
			       });
			         const index = submenus.list.findIndex((submenu)=>submenu.id === id)
			 				 if(index>=0){
			 					 const nowstaus = submenus.list[index].isShow
			 						  submenus.list[index].isShow = !nowstaus
			 				 }else{
			 					 drawer.value = false;
			 				 }
			 				 //hover当前的激活状态
							 if(drawer.value == true){
								   activeIndex.value=currentIndex+'';
								   resizeDrawer();
							 }
		 },250)   
       }
		   
	   function resizeDrawer(menu){
		   var submenu= submenus.list[activeIndex.value];
		   if(menu){
			   submenus.list.forEach(item=>{
				   if(item.id==menu.id){
					    submenu=item;
				   }
			   })
			  
		   }
		   var numbermenu=0;
		         submenu.menugroup.forEach((subgroup)=>{
					numbermenu++;
		          subgroup.namegroup.forEach((sub)=>{
		   											 if(sub.path){
		   											 numbermenu++;
		   											 }
		   	     })})
		    if(numbermenu>20){
		        drawerSize.value=160*2;
		    }else{
		        drawerSize.value=180;
		    }
	   }
	   //离开
	  function laveMenu(){
		  clearInterval(timer)
	  }
	  //通过路由记住菜单激活状态
	  function getMenuactive(){
		  let arr = submenus.list.filter((submenu)=>{
		       return submenu.menugroup.filter((subgroup)=>{
		       return subgroup.namegroup.filter((sub)=>{
		  	   return sub.path == route.path 
		   }).length>0
		   }).length>0
		   })
		 if(arr.length>0){
			let menuindex  =  menuData.list.findIndex((item)=>item.id === arr[0].id)
			 activeIndex.value=menuindex+'';	
		 }
	  }
	  
	  function handleExpend(subgroup){
		  subgroup.isShow = !subgroup.isShow;
		  if(subgroup.isShow){
			  subgroup.rotate = "rotate"
		  }else{
			  subgroup.rotate = "rotatezero";
		  }
	  }
	  //刷新页面记住当前菜单激活状态
	  onMounted(()=>{
	    getMenuactive();
	  })
       //返回一个对象
       return {
		   getMenuactive,
		   laveMenu,
		   timer,
           changeMenu,
		   changeDrawer,
           menuData,
		   activeIndex,
           submenus,
           drawer,
		   goHome,
		   theme,
		   drawerSize,
		   handleExpend,
		   felxcolumn,imgPath
    }
  },

})
 
</script>
<style>
.dark .el-drawer{
	 background-color: #2c2c2c !important;
}
</style>
<style scoped>
	.icon-pos{
		margin-left: 4px;
	}
	.menu-item .item{
		margin-bottom:20px;
		margin-right: 10px;
	}
	.menu-item{
		display: -webkit-box;      /* OLD - iOS 6-, Safari 3.1-6, BB7 */
		display: -ms-flexbox;     /* TWEENER - IE 10 */
		display: -webkit-flex;    /* NEW - Safari 6.1+. iOS 7.1+, BB10 */
		-webkit-flex-direction:row-reverse;
		display: flex;
		height:calc(100vh - 50px);
		flex-wrap: wrap;
	}
	.menu-item.felxcolumn{
		flex-direction: column;
	}
	.rotate{
		 transition: transform 0.5s ease;
		 transform: rotate(90deg); 
	}
    .rotatezero{
		transition: transform 0.5s ease;
		transform: rotate(0deg); 
	}
.expend-leave-active,
	.expend-enter-active{
		  transition: all 0.5s ease;
		  max-height:50px;
		  overflow: hidden;
	}
.expend-enter-from,
.expend-leave-to{
	max-height:0px;
}	

.meun-hide{display:none;}
.themeBlack,.themeBlack  #menubar,.themeBlack .el-menu-vertical{
		background:#131921 !important;
	}
.themeBlack	 .el-menu-item:hover{
    background-color:  #37475a;
}	
.themeBlack .el-menu-item{
	color:#dedede !important;
	opacity: 0.8;
}
.themeBlack	.el-menu-item.is-active{
	 background-color: #07090c;
	 color:#ff6700 !important;
}
.themeBlack	 .text{
      text-decoration:none;
	  color:#fff;
	  font-size:14px;
	  padding:8px 16px;
	  border-radius: 4px;
	  cursor: pointer;
	  transition: background-color var(--el-text-color-primary),color var(--el-text-color-primary);
	  color: var(--el-text-color-primary);
      display:flex;
  }
.themeBlack	 .text:hover{
     background-color:var(--el-bg-color);
	}
.themeBlack	 .item .active{
		 background-color:var(--el-color-primary-light-9);
		 color: var(--el-color-primary);
	}

/* ---------------------白色主题-------------------------------*/
.themeLight,.themeLight .el-menu{
	    background: var(--el-bg-color);
}
 
.themeLight	 .el-menu-item:hover{
    background-color: var(--el-border-color-light);
}	
.themeLight	.el-menu-item.is-active{
	 background-color: var(--el-border-color-light);
}

.el-menu-vertical{
	 padding-top:16px;
}
.themeLight	 .text{
      text-decoration:none;
	  color:var(--el-text-color-primary);
	  font-size:14px;
	  padding:8px 16px;
	  border-radius: 4px;
	  cursor: pointer;
	  transition: background-color var(--el-transition-duration),color var(--el-transition-duration);
      display:flex;
  }
.themeLight	 .text:hover{
     background-color: var(--el-bg-color);
	}
.themeLight	 .item .active{
         color:var(--el-color-primary);
		 background-color: var(--el-color-primary-light-9);
	}
/*-----------------dark主题------------------------*/
.dark .themeBlack,.dark .themeBlack  #menubar,.dark .themeBlack .el-menu-vertical,
.dark .themeLight,.dark .themeLight  #menubar,.dark .themeLight .el-menu-vertical
{
		background:#1a1a1a !important;
}
.dark .themeBlack .el-menu-item:hover,.dark .themeLight .el-menu-item:hover{
    background-color:  #323232;
}	
.dark .themeBlack .el-menu-item,.dark .themeLight .el-menu-item{
	color:#dedede !important;
	opacity: 0.8;
}
.dark .themeBlack	.el-menu-item.is-active,.dark .themeLight .el-menu-item.is-active{
	 background-color: #323232;
	 color:#ff7315 !important;
}

.dark .themeBlack .text,.dark .themeLight .text{
      text-decoration:none;
	  color:#fff;
	  font-size:14px;
	  padding:8px 16px;
	  border-radius: 4px;
	  cursor: pointer;
	  transition: background-color var(--el-text-color-primary),color var(--el-text-color-primary);
	  color: var(--el-text-color-primary);
      display:flex;
  }
.dark .themeBlack .text:hover,.dark .themeLight	.text:hover{
     background-color:var(--el-bg-color);
	}
.dark .themeBlack	 .item .active,.dark .themeLight	 .item .active{
		 background-color:#272727;
		 color: var(--el-color-primary);
	}
#menubar{
width:64px;
}
#menubar {
	z-index:1999;
}
.el-menu{
	border-right: 0;
}
.el-menu-item{
    flex-direction: column;
    justify-content: center;
	
}
.el-menu-item .i-icon{
  margin-bottom:2px;
}
.el-menu-item span{
    line-height:16px;
}
.el-menu-item{
    height:64px;
}
.el-menu-item .i-icon{
    display:  none;
}

.el-menu-item {
     height: 30px;
 }
 .el-menu-item.is-active {
     color: var(--el-menu-active-color);
	 font-weight: 700;
 }
@media(min-height:300px) {
 .el-menu-item {
      height: 24px;
 	  font-size:12px;
  }
 }
@media(min-height:500px) {
 .el-menu-item {
      height: 30px;
	  font-size:12px;
  }
 }
 @media(min-height:600px) {
 .el-menu-item .i-icon{
     display:inherit;
 }
 .el-menu-item {
      height: 48px;
 	  font-size:12px;
  }
 }
 @media(min-height:700px) {
 .el-menu-item .i-icon{
     display:inherit;
 }
 .el-menu-item {
      height: 53px;
	  font-size:12px;
  }
 }
 @media(min-height:800px) {
 .el-menu-item .i-icon{
     display:inherit;
 }
 .el-menu-item {
      height: 60px;
	  font-size:14px;
  }
 }
@media(min-height:900px) {
.el-menu-item .i-icon{
	display:inherit;
}

 .el-menu-item {
      height:65px;
	  font-size:14px;
  }
 }
.menu-group{
	padding-left:16px;
	padding-top: 8px;
	padding-bottom:8px;
}

/*过渡动画*/

.logo{
	display: flex;
	justify-content: center;
	align-items: center;
}

.logo .el-image{
	height:28px;
	width: 28px;
	margin-top:16px;
}
.aside-flex {
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	border-right: solid 1px #e6e6e6;
}
.dark .aside-flex {
 	border-right:0;
 }
      
</style>
<style>
	.themeBlack .el-button--primary.is-plain{
		--el-button-border-color:#37475a;
		--el-button-bg-color: #1f2936;
		color: #fff;
	}
</style>
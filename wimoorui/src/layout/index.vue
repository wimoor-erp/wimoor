<template>
	  <el-container>
	      <el-aside>
			  <AsideMenu  ref="leftMenu"/>
			  </el-aside>
	      <el-container>
	        <el-header>
				<div class="top-nav">
				<!-- <HeaderNavbtn/> -->
				<HeaderTab ref="headerTab" @clear="clearCache"/>
				<HeaderAvatar  @changeSKin="changeSKin"/>
				</div>
			</el-header>
	        <el-main  style="flex: 1;height:100%">
				  <router-view v-slot="{ Component }" >
				      <keep-alive ref="keepAlive"   :exclude="excludes">
				        <component  :is="wrap($route.query.title, Component)"   v-if="$route.meta.keepAlive" :key="$route.query.title"/>
				      </keep-alive>
				      <component :is="wrap($route.query.title, Component)" v-if="!$route.meta.keepAlive" :key="$route.query.title"/>
				  </router-view>
				   <el-backtop :right="50" :bottom="50" />
			</el-main>
	      </el-container>
	    </el-container>
</template>
<script setup>

/* import HeaderNavbtn from "./components/Header/HeaderNavbtn"; */
 import { ref,reactive,onMounted ,inject,getCurrentInstance,h } from 'vue'
 import HeaderTab from "./components/Header/HeaderTab.vue";
 import HeaderAvatar from "./components/Header/HeaderAvatar.vue";
 import AsideMenu from "./components/Aside/AsideMenu.vue";
 import { useRoute,useRouter } from 'vue-router';
 const emitter = inject("emitter"); // Inject `emitter`
 const headerTab=ref(HeaderTab);
 const keepAlive=ref();
 const leftMenu=ref();
 const excludes=ref([]);
 let router = useRouter();
function clearCache(activeName){
			  removeCache(activeName);
			  router.replace({ 'path':"/blank",
							   'query':{"refresh":new Date(),"title":"blank"},
							});
			  setTimeout(function(){
				  headerTab.value.showTab(activeName);
			  },500)
		}
router.afterEach((to,from)=>{
		  excludes.value=[];
})
function removeCache(activeName){
		 excludes.value.push(activeName);
	}
			
function changeSKin(){
				 leftMenu.value.goHome()
			}
	  
emitter.on("removeCache", (value) => { // 监听事件
   removeCache(value);
});
 // 自定义name的壳的集合
const wrapperMap = new Map()
function wrap(title, component) {
   let wrapper
   // 重点就是这里，这个组件的名字是完全可控的，
   // 只要自己写好逻辑，每次能找到对应的外壳组件就行，完全可以写成任何自己想要的名字
   // 这就能配合 keep-alive 的 include 属性可控地操作缓存
   if (component) {
     const wrapperName = title;
     if (wrapperMap.has(wrapperName)) {
       wrapper = wrapperMap.get(wrapperName)
     } else {
       wrapper = {
         name: wrapperName,
         render() {
           return h("div", { className: "vaf-page-wrapper" }, component)
         },
       }
       wrapperMap.set(wrapperName, wrapper)
     }
     return h(wrapper)
   }
   }
</script>
<style>
.el-header{height:34px!important;
}
.el-aside{width:64px!important;
}
.el-main{padding:0px!important;min-width:1280px}
#menubar{
    position: fixed;
    left: 0;
    z-index: 999;
}
#menubar .el-overlay{
   left:64px!important;
}
#menubar .el-drawer{
    min-width:176px;
}
.top-nav{
    position: fixed;
    top: 0px;
    right: 0px;
	left: 64px;
    z-index:998;
	padding:0px 24px;
}
.top-nav{
    display: flex;
    background-color:#fff;  
    align-items: center;
    box-shadow: 0px 1px 6px rgba(0,0,0,0.1);
}
.dark .top-nav{
    display: flex;
    background-color:#202224;  
    align-items: center;
    box-shadow: 0px 1px 6px rgba(0,0,0,0.1);
}
.el-backtop{
	color:#fff;
	background-color:rgb(0 0 0 / 62%);
}
.el-backtop:hover{
	background-color:rgb(0 0 0 / 80%);
}
</style>
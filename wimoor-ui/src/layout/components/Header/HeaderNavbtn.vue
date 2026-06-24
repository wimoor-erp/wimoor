<template>
	<div class="navbtn">
		<el-button type="primary" @mouseenter="enterBtn" @mouseleave="leaveBtn"> 
			<hamburger-button theme="outline" size="32" fill="#FFF" :strokeWidth="3"/>
		</el-button>
		 <!--弹出层!-->
       <el-drawer v-model="drawer" :open-delay="300"
	    :append-to-body='false' size="856px" direction="ltr" class="nav-drawer">
		<template v-slot:title>
			<div class="menu-title">
			<application-menu theme="outline" size="16" fill="var(--el-text-color-regular)" :strokeWidth="3"/>
			<span  class="title-name">全局菜单</span>
			<right theme="outline" size="14" fill="var(--el-text-color-regular)" :strokeWidth="3"/>
			</div>
		</template>
		<!--收藏的菜单!-->
		<div class="all-menu">
		 <div class="collect-menu">
          <div class="collect-title">我的收藏</div>
<!-- 		    <div  class="text item">
				<span>本地产品</span>
				<close  theme="outline" size="14" fill="#333" :strokeWidth="2"/>
			</div>
			 <div  class="text item">
				<span>采购单</span>
				<close  theme="outline" size="14" fill="#333" :strokeWidth="2"/>
			</div> -->
		  </div>
          <!--全局菜单-->
		 <div class="list-menu">
        <el-row>
       <el-input v-model.trim="keywords" placeholder="请输入关键词">
        <template #prefix>
         <el-icon style="font-size:20px;align-itmes:center">
            <search />
         </el-icon>
        </template>
      </el-input>
    </el-row>
	 <!--搜索结果-->
	<el-row class="restyle" v-show='keywords'>
         <div class="sma-tit">共搜索到 <span>{{newArr.length}}</span> 个结果：</div>
		 <div v-for='(n,index) in newArr' :key='index'  style="margin-right:16px">
			 <el-link  type="primary">{{n.name}}</el-link>
		 </div>
	</el-row>

   <div v-show='!keywords' style="height:calc(100vh - 128px);overflow: auto;">
	   <div class="list">
		   <div  class="items"  v-for="(a,index) in allMenu.list" :key="index">
			   <div class="li-title" >{{a.title}}</div>
			   <div class="li-menu" >
                    <div class="li-group"  v-for="(s,index) in a.subMenu" :key="index"> 
						<div class="text"  v-for="(g,index) in s.subGroup" :key="index"><a>{{g.name}}</a>
                          <el-button title="收藏菜单" class='IC-rbtn' type='text'><el-icon size="20px"><star /></el-icon></el-button>
						</div>
				   </div>
				</div>
		   </div>
	   </div>
	 </div>
	</div>
</div>
       </el-drawer>
	</div>
</template>

<script>

import {HamburgerButton,ApplicationMenu,Right,Close, Watch,} from '@icon-park/vue-next';
import {Search,Star} from '@element-plus/icons-vue';
import { defineComponent, ref,reactive,watch, computed } from 'vue';
import {menuApi} from "@/api/sys/admin/menuApi.js";     
import {menuDataModel,submenulistModel,allMenuModel} from '@/model/sys/admin/menu.js';    
export default defineComponent({
        name: "HeaderNavbtn",
        components:{
			HamburgerButton,ApplicationMenu,Right,Close,Search,Star,allMenuModel,menuApi
		},
	setup() {
	   let keywords = ref('')
       let drawer = ref(false)
	   //let allMenu = reactive({list:allMenuModel});
       let allMenu = reactive({list:[]});
	   menuApi.getMenuALL(response=>{
		    // allMenu.list=response.allMenu;
          });
	   let newArr=ref([])
	   //监视
	   watch(keywords,(val)=>{
		   let arr=[]
		    allMenu.list.forEach((a)=>{
			  a.subMenu.forEach((s)=>{
			     s.subGroup.forEach((g)=>{
					 arr.push(g)
					 return
				  })
				})
			})
			arr =arr.filter((name)=>{
			return name.name.indexOf(val) !== -1
			})
			if(val!==''){
              newArr.value= arr
			}else{
				newArr.value=[]
			}

	   })
      //返回对象
       return {
	   keywords,
	   allMenu,
	    newArr,
	   drawer,
    }
  },
   methods:{
	   enterBtn(){
		  this.timer= setTimeout(() => {
			   this.drawer = true
		  }, 300)
	   },
	     leaveBtn(){
		  clearInterval(this.timer)
	     },
       } 
	})
</script>
<style>
.IC-rbtn{font-size:15px;padding:7px!important;position:absolute;right:0;opacity:0;}
.restyle{font-size: var(--el-font-size-small);color:var(--el-text-color-secondary);margin:8px 0;}
.restyle .sma-tit span{color:var(--el-color-primary)}
.list-menu .text{position: relative;}
.list-menu .text a{padding:6px 8px;color:var(--el-text-color-regular);display:inline-block;width:100%}
.list-menu .text:hover{background-color: var(--el-bg-color);cursor:pointer;transition: background-color var(--el-transition-duration),color var(--el-transition-duration);}
.list-menu .text:hover .IC-rbtn{opacity:1}
.all-menu .list-menu{flex: 1 1 0%;padding:0px 24px;height:100%;flex-direction: column;display: flex;overflow: hidden;}
.list-menu .li-group{margin-bottom:16px;}
.list-menu .list .items{padding-right:8px;margin-bottom:48px;font-size:14px;
break-inside: avoid;width:100%;display: inline-block;}
.list-menu .list {padding-top:32px;columns:150px 3;}
.list-menu .li-title{color:var(--el-color-primary);margin-bottom:24px;font-weight:600;margin-left:8px;}
  .all-menu .el-input__prefix-inner{align-items: center}
.nav-drawer .el-drawer__header{
	padding:0px;
	display:flex;
	height:40px;
	margin-bottom:0px;
	justify-content: space-between;
}
.nav-drawer .el-drawer__body{padding:0}
.menu-title .i-icon-application-menu{display: inherit;}
.menu-title .i-icon-right{display: inherit; margin-left:auto;margin-right:16px;height: 12px;}

.menu-title .title-name{
	margin-left:8px;
	font-size:14px;
}
   .menu-title{
	   height:40px;
	   padding-left:20px;
	   align-items: center;
	   display:flex;
	   flex: inherit!important;
	   width:160px;
	   background-color:var(--el-border-color-base);
   }
  .navbtn button{
	width:64px;height:56px;
	border-radius: 0;
  }
   .navbtn>.el-overlay{
	  top:56px!important;
	  z-index: 2000!important;
  }
  .navbtn>.el-overlay> .el-drawer.ltr{border-top:1px solid #eee}
  .all-menu{
	   height:100%;
	   display:flex;
  }
  .collect-menu{
	  width:160px;
	  background:var(--el-bg-color);
  }
 .collect-menu .collect-title{height:48px;display:flex;
 align-items: center;
 font-size:14px;
 margin-left:20px;
 color:var(--el-text-color-secondary)
 }
  .collect-menu .text{padding:0px;padding-left:20px;line-height:40px;display:flex;font-size:14px;color:var(--el-text-color-regular)}
  .collect-menu .text:hover{background-color: var(--el-border-color-light);}
  .collect-menu .text:hover .i-icon-close{opacity: 1}
  .collect-menu .i-icon-close{display:flex;align-items: center;margin-left:auto;margin-right:20px;opacity: 0}

</style>
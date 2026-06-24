<template>
	<div class="header-right-content">
		<div   class="phone pointer">
			 <div v-if="needGroup&&tenantAllList&&tenantAllList.length>0" class="tenant-dropdown">
         <span class="period-name">{{currentPeriodName}}</span>
			 <el-dropdown >
			 <span class="el-dropdown">
			   {{finStore.currentTenantName}}
			   <el-icon class="el-icon--right"  >
			     <arrow-down />
			   </el-icon>
			 </span>
			     <template #dropdown>
			         <el-dropdown-menu>
			           <el-dropdown-item @click="changeTenant(item.id)"  v-for="item in tenantAllList"  divided >
			             <span v-if="item.company">{{item.company}}</span>
						        <span v-else>{{item.name}}</span>
			           </el-dropdown-item>
			         </el-dropdown-menu>
			     </template>
			   </el-dropdown>
			   </div>
		</div>
		<div  class="phone pointer">
			<connect-address-one @click="handToPage" title="常用地址" theme="outline" size="16" />
		</div>
		<div class="message">
			<RemindMessage  ref="messageRef"></RemindMessage>
		</div>
		<div class="phone pointer">
           <headset-one  @click="handleHelpQQ" theme="outline" size="16"  /> 
		</div>
		
		<div class="avatar">
			 <el-menu
			     :default-active="'1'"
			     class="el-menu-avatar"
			     mode="horizontal"
				 :collapse-transition="false"
			     :ellipsis="false"
			   >
			     <el-sub-menu popper-class="el-menu-popper-avatar" index="1">
			       <template #title>
					   <span class="el-dropdown-link">
					   	<el-avatar v-if="image" :src="image"></el-avatar>
					   	<el-avatar v-else  >
					   		<div style="padding-top:1px;color:var(--el-color-primary);font-size:10px;" >{{avatarNameComputed}}</div></el-avatar>
					   	<span class="user-name">{{name}}</span>
					   </span>
				   </template>
			       <el-menu-item index="1-1" disabled class="company" > {{companyname}} </el-menu-item>
			       <el-menu-item index="1-2"    @click="goTocenter">  个人中心</el-menu-item>
				   <el-sub-menu index="1-3">
				     <template #title>切换账号</template>
				     <el-menu-item index="1-3-1" 
					 @click="changeAccount(item)" 
					 :class="item.isactive? 'active':''" 
					  v-for="item in bindlist">
					     <template #title>
						    <div>{{item.companyname}} </div>
							<div class="font-extraSmall" style="padding-left:5px;"> - {{item.name}} </div>
							<el-icon v-if="item.isactive" style="padding-left: 10px;"><Check /></el-icon>
						  </template>
					 </el-menu-item>
				     <el-menu-item index="1-3-1000" @click="bindVisible=true"><el-icon><people-plus    size="14"/></el-icon>添加账号</el-menu-item>
				   </el-sub-menu>
				   <el-menu-item index="1-7"  @click="handDocument">
					  <el-icon class="font-extraSmall"><help  /></el-icon> 帮助文档
				   </el-menu-item>
				  <el-menu-item index="1-8"   @click="changeTheme" >
					  <div class="message pointer" v-if="lightSkin">
					  <el-icon class="font-extraSmall">	<brightness theme="outline"   /></el-icon>白天模式
					  </div>
					  <div class="message pointer" v-else  >
					  	<el-icon class="font-extraSmall"><moon theme="outline"   /></el-icon>黑夜模式
					  </div>
				  </el-menu-item>
				   <el-menu-item index="1-6" v-if="isSSO()" @click="backOldsys">返回旧系统</el-menu-item>
			       <el-menu-item index="1-4" @click="logout">退出</el-menu-item>
				   <el-menu-item index="1-5" @click="clearCath">清缓存</el-menu-item>
			     </el-sub-menu>
			   </el-menu>
			 
		</div>
	</div>
	<!-- 绑定账号 -->
	 <el-dialog v-model="bindVisible" title="绑定其它账号" :destroy-on-close='true' width="560px" >
	 			 <el-form :inline="true" :model="bindForm" class="form-width-fill" label-width="auto">
	 			    <el-form-item label="其它账号"   >
	 			 	   <el-input  v-model="bindForm.account"   placeholder="请输入要绑定的账号" ></el-input>
	 			    </el-form-item>
	 			 		 <el-form-item label="账号密码"  >
	 			 		   <el-input v-model="bindForm.password" type="password"  show-password placeholder="请输入密码" ></el-input>
	 			 	</el-form-item>
	 			  </el-form>
	 	<template #footer>
	 		<span class="dialog-footer">
	 			<el-button @click="bindVisible = false">取消</el-button>
	 			<el-button type="primary" @click="submitBind" >确认</el-button>
	 		</span>
	 	</template>
	 </el-dialog>
	 <el-dialog v-model="siteVisible" top="4vh" class="sitedialog"  :destroy-on-close='true' width="80%" >
	 		 <iframe id="myIframe" src="https://site.wimoor.com" width="100%"   frameborder="0" ></iframe>
	 	 
	 </el-dialog>
</template>

<script setup>
import { menuApi } from "@/api/sys/admin/menuApi.js"
import userApi from '@/api/sys/admin/userApi.js'
import { toggleDark } from "@/components/composables/index.js"
import finStore from "@/hooks/store/useFinanceStore.js"
import useUserStore from "@/hooks/store/useUserStore.js"
import store from '@/store/index'
import request from "@/utils/request.js"
import { ArrowDown, Check } from '@element-plus/icons-vue'
import {
	Brightness,
	ConnectAddressOne,
	HeadsetOne,
	Help,
	Moon,
	PeoplePlus,
} from '@icon-park/vue-next'
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import RemindMessage from "../remind_message/index.vue";
const emitter=inject("emitter");
const router = useRouter()
const route = useRoute()
// 响应式数据
const bindVisible = ref(false)
const siteVisible = ref(false)
const image = ref("")
const name = ref("")
const avatarName = ref("")
const lightSkin = ref(false)
const companyname = ref("")
const tenantAllList = ref([])
const messageRef = ref(null)
const needGroup=ref(false);
const currentUser = ref({})
const currentPeriodName = ref('无当前期间')
let activeName="";
const bindForm = reactive({
	account: "",
	password: ""
})

// 计算属性
const avatarNameComputed = computed(() => {
	return name.value ? name.value.substring(0, 1) : ""
})
const bindlist = computed(() => store.getters['user/getBindList'] || [])

// 方法
const loadTenantList = async () => {
	try {
		//console.log('开始加载租户列表');
		const res = await finStore.getTenantList();
		if (res.data && res.data.length > 0) {
			tenantAllList.value = res.data;
			// 确保获取到当前会计期间
			if (finStore.currentTenantId) {
				// 更新当前会计期间名称
				try {
					const period = await finStore.getCurrentPeriod();
					// 更新当前会计期间名称
					currentPeriodName.value = period?.periodName || '无当前期间';
				} catch (error) {
					console.error('手动获取当前会计期间失败:', error);
					console.error('错误详情:', error.response);
				}
			}
		}
	} catch (error) {
		console.error('加载租户列表失败:', error);
	}
}
async function changeTenant(tenantId){
  finStore.setCurrentTenantId(tenantId);
  const period = await finStore.getCurrentPeriod();
  currentPeriodName.value = period?.periodName || '无当前期间';
  emitter.emit("removeAllCache");
}

const handToPage = () => {
	siteVisible.value = true
	setTimeout(function() {
		document.getElementById("myIframe")
	}, 1000)
}

const changeTheme = () => {
	lightSkin.value = !lightSkin.value
	toggleDark()
}

const isSSO = () => {
	return localStorage.getItem("logintype") == "sso"
}

const backOldsys = () => {
	window.location = 'https://erp.wimoor.com/page.do'
}

const clearCath = () => {
	menuApi.cleanUserCache()
}

const goTocenter = () => {
	router.push({
		path: "/usercenter",
		query: {
			title: "个人中心",
			path: "/usercenter",
		}
	})
}

const handDocument = () => {
	window.open("https://wiki.wimoor.com/shelves/wimoor")
}

const handleHelpQQ = () => {
	window.open('https://bbs.wimoor.com', '_blank')
}

const submitBind = () => {
	const data = {
		openid: "",
		password: bindForm.password,
		account: bindForm.account,
		appType: "website"
	}
	
	userApi.openidbind(data).then((res) => {
		ElMessage({
			type: "success",
			message: "操作成功!"
		})
		bindVisible.value = false
		useUserStore.loadBindList()
	})
}

const changeAccount = (item) => {
	const data = {
		openid: item.lastloginsession,
		account: item.account,
		appType: item.lastloginip
	}
	
	userApi.changeLoginWechatApp(data).then((res) => {
		const query = router.currentRoute.value.query
		localStorage.setItem("jsessiontime", new Date())
		if (query["title"] && query["path"] && Object.keys(query).length == 2) {
			window.location.reload()
		} else {
			window.location = "/home"
		}
	})
}

const logout = () => {
	useUserStore.logout()
}

const getDecode = (companyname) => {
	if (companyname && companyname.indexOf("%") >= 0) {
		try {
			return decodeURI(companyname)
		} catch (error) {
			return decodeURIComponent(companyname)
		}
	} else {
		return companyname
	}
}

const getData = () => {
	window.onstorage = function(e) {
		if (e.key == "jsessiontime" && e.oldValue != e.newValue) {
			const arr = Math.floor(Math.random() * 1000 + 500)
			const timer = setTimeout(function() {
				const query = router.currentRoute.value.query
				if (query["title"] && query["path"] && Object.keys(query).length == 2) {
					window.location.reload()
				} else {
					window.location = "/home"
				}
				clearTimeout(timer)
			}, arr)
		}
	}
	userApi.getInfo().then(response => {
		if (response && response.data) {
			if (response.data.image) {
				image.value = response.data.image
			}
			currentUser.value = response.data
			messageRef.value?.show(currentUser.value)
			if (response.data.name) {
				name.value = response.data.name
			}
			if (response.data.company) {
				companyname.value = response.data.company
			}
			store.commit('user/SET_USER_INFO', response.data)
			if (response.data.image) {
				store.commit('user/SET_USER_IMAGE', response.data.image)
			}
			if (response.data.name) {
				store.commit('user/SET_USER_NAME', response.data.name)
			}
			if (response.data.company) {
				store.commit('user/SET_COMPANY_NAME', response.data.company)
			}
			store.commit('user/SET_IS_LOADED', true)
		}
	})
}
 
router.beforeEach((to,from)=>{
  needGroup.value=to.path.indexOf("/fin/")>=0;
  activeName=to.query.title;
	});
// 生命周期
onMounted(() => {
	getData();
	loadTenantList();
  needGroup.value=route.path.indexOf("/fin/")>=0;
  activeName=route.query.title;

	
})

// 暴露给模板
defineExpose({
	handToPage,
	changeTheme,
	isSSO,
	backOldsys,
	clearCath,
	goTocenter,
	handDocument,
	handleHelpQQ,
	submitBind,
	changeAccount,
	logout,
	getDecode,
	getData,
})
</script>

<style>
	#myIframe {
	  width: 100%;
	  height: 81vh; /* 视口高度 */
	  border: none; /* 移除边框 */
	}
	.arco-typography{
		display:none;
	}
	.phone,
	.message {
		line-height:36px;
	}

	.phone,
	.message .i-icon {
		padding: 0px 12px;
	}

	.phone>.i-icon>svg,
	.message>.i-icon>svg {
		vertical-align: middle;
	}
    .sitedialog .el-dialog__body{
		padding:0px;
	}
	.avatar {
		line-height:36px;
		display: flex;
		align-items: center;
	}
	.header-right-content{
		display: flex;
		align-items: center;
	}
	.header-right-content .el-avatar{
		width:24px;
		height: 24px;
	}
	.user-name{
		font-size: 13px;
		margin-left: 8px;
		margin-top:2px;
	}
	.el-avatar{
		position: relative;
		background-color: #606266;
	}
	.dark .el-avatar{
		position: relative;
		background-color: #EBEDF0;
	}
	
	.el-menu-avatar .el-sub-menu__icon-arrow{
		display:none;
	}
	.el-menu-avatar{
		border-bottom:none !important;
		
	}
	.el-menu-avatar .el-sub-menu__title {
		line-height:0px !important;
		padding-right:0px !important;
	}
	
    .el-menu-avatar .el-sub-menu.is-active .el-sub-menu__title{
		border-bottom:none !important;
		padding-right:0px !important;
	}
 .avatar .el-menu--horizontal>.el-sub-menu .el-sub-menu__title{
 	border-bottom:none;
 }
</style>
<style scoped>
.tenant-dropdown .el-dropdown{
  font-size:18px !important;
  padding-top:5px;
  height:30px;
  padding-right:10px;
  padding-left:10px;
}
.tenant-dropdown{
  font-size: 20px;
   background: linear-gradient(135deg, 
      #e6e6e6 0%, 
      #cccccc 25%, 
      #999999 50%, 
      #b3b3b3 75%, 
      #e6e6e6 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  transform: perspective(100px) rotateX(2deg);
  text-shadow:
     0.1px 0.1px 0 currentColor,
     0.2px 0.2px 0 currentColor,
     0.3px 0.3px 0 currentColor,
     0.4px 0.4px 0 currentColor,
     0.5px 0.5px 0 currentColor,
     0.6px 0.6px 2px currentColor;
}

.period-name{
  font-size: 12px;
  padding-top:5px;
  padding-right:10px;
  color: #999;
  text-shadow:0px 0px 0 currentColor, 0px 0px 0 currentColor, 0px 0px 0 currentColor, 0px 0px 0 currentColor, 0px 0px 0 currentColor, 0px 0px 0px currentColor;
}
 
	.company{
		border-bottom:1px solid #e5e5e5 !important;
		margin-bottom:5px !important;
		padding-top:5px !important;
		padding-bottom:5px !important;
		opacity: .66  !important;
	}
</style>
<template>
	<div class="el-white-bg">
 <el-row>
    <el-col :span="4" >
		<div class="w-grid-content" >
			  <RoleList ref="roles" @selectRole="selectRoleHandler"/>
	    </div>
	</el-col> 
	<el-col :span="6" >
		<div class="w-grid-content" >
			<MenuList ref="menus" @selectMenu="selectMenuHandler"/>
	    </div> 
	</el-col>
    <el-col :span="14" >
		<div class="w-grid-content" >
			<PageList ref="auths"/>
	    </div> 
	</el-col>
  </el-row>
</div>
</template>

<script>
import { ref ,watch,reactive,onMounted} from 'vue'
import {Help} from '@icon-park/vue-next';
import RoleList from "./components/role_list.vue"
import PageList from "./components/page_list.vue"
import MenuList from "./components/menu_list.vue"
export default {
  name: '角色权限',
  components: {Help, RoleList,PageList,MenuList },
  setup(){
	    const roles=ref(RoleList);
		const menus=ref(MenuList);
		const auths=ref(PageList);
		function selectRoleHandler(m_role){
			menus.value.loadData(m_role);
		}
		function selectMenuHandler(m_role,m_menu){
			auths.value.loadData(m_role,m_menu);
		}
		return {selectRoleHandler,selectMenuHandler,roles,menus,auths}
    }
}
</script>
<style >
	.gird-head{display:flex;
	 padding:16px;
	 border-bottom:1px solid var(--el-border-color-light)
	}
	.w-grid-content{
	border-right:1px solid var(--el-border-color-light);
	height:calc(100vh - 57px);
	}
</style>
<template>
	<div>
		  <el-button @click="logout">注销</el-button>
	</div>
	<el-dialog v-model="bindVisible" title="注销账号" destroy-on-close='true' width="560px" >
			 <span>您确定要注销账号吗？注销后账号相关数据将不再保留！</span>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="bindVisible = false">取消</el-button>
				<el-button type="primary" @click="agreelogout" >注销账号</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
	import userApi from '@/api/sys/admin/userApi.js';
	import { ref,reactive,onMounted ,h} from 'vue';
	import { ElMessage } from 'element-plus';
	export default{
		setup(){
				let bindVisible=ref(false)
				function agreelogout(){
					userApi.unbindAccount().then((res)=>{
						ElMessage.success('注销成功！');
						localStorage.removeItem("jsessionid");
						localStorage.removeItem("logintype");
						let authserver=  localStorage.getItem("authserver");
						location=authserver+"/logout"; 
					})
				}
				function logout(){
					bindVisible.value = true
				}
				return{
					bindVisible,
					agreelogout,logout,
				}
		}
	}
</script>

<style>
</style>

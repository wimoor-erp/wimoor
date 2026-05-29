<template>
	<el-table :data="tableData">
		<el-table-column prop="companyname" label="公司名称"></el-table-column>
		<el-table-column prop="account" label="用户账号"></el-table-column>
		<el-table-column prop="account" label="用户名称">
			<template #default="scope">
				{{scope.row.userinfo.name}}
				</template>
		</el-table-column>
		<el-table-column prop="account" label="操作">
			<template #default="scope">
				<el-link type="primary" :underline="false" @click="unbindItem(scope.row)" >解绑</el-link>
				</template>
		</el-table-column>
	</el-table>
	
</template>

<script setup>
	import {reactive,toRefs,onMounted,h,ref}from"vue";
	import userApi from '@/api/sys/admin/userApi.js';
	import { ElMessage } from 'element-plus';
	const state = reactive({
		tableData:[],
	})
	const {
		tableData,
		
	}=toRefs(state)
	function unbindItem(row){
		 userApi.unbindWechat({
			 "appType":row.lastloginip,
			 "account":row.account, 
			 "openid":row.lastloginsession}).then(res => {
				 ElMessage.success("解绑成功！");
				 window.location.reload();
			 });
	}
	function getDecode(companyname){
		if(companyname&&companyname.indexOf("%")>=0){
			try {
			   return decodeURI(companyname);
			} catch (error) {
			   return decodeURIComponent(companyname);
			}
		}else{
			return companyname;
		}
	}
	function loadData(){
		userApi.findbindlist().then((res)=>{
					var list=[];
					if(res.data){
						res.data.forEach(item=>{
							var data=item;
							item.userid=item.id;
							item.isactive=false;
							item.companyname=item.userinfo.companyname;
							data.account=item.account;
							if(data.companyname){
								    data.companyname=getDecode(data.companyname);
							}
							list.push(data);
						});
					}
					state.tableData=list;
		});
	}
	function show(){
		loadData();
	}
	defineExpose({
		show
	})
</script>

<style>
</style>
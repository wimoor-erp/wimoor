<template>
	<div class="con-body">
	<el-checkbox-group v-model="formData.permissionNames" @change="permChange(item)">
	
		<el-row :gutter="16">
			<el-col   :span="12" >
			<el-card  style="min-height:calc(100vh - 82px)" >
			  <template #header>
			    <div class="card-header">
			      <span>功能权限</span><span class="font-extraSmall">{{title}}</span>
			    </div>
			  </template>
			  <div    class="text item">
				  <el-space  wrap>
				  <span v-for="item in permList" class="m-r-24">
					  <el-checkbox    v-if="!item.isfield"  :v-model="item.hasPerm" :label="item.name" :key="item.id"  />
				  </span>
				</el-space>	 
			  </div>
			</el-card>
			</el-col>
			<el-col :span="12" >
			<el-card   style="min-height:calc(100vh - 82px)">
			  <template #header>
			    <div class="card-header">
			      <span>字段查看权限</span><span class="font-extraSmall">{{title}}</span>
			    </div>
			  </template>
			  <div  class="text item">
				  <el-space wrap>
				  <span v-for="item in permList"  class="m-r-24">
				  <el-checkbox  v-if="item.isfield" :v-model="item.hasPerm" :label="item.name" :key="item.id" />
			    </span>
				</el-space>
			  </div>
			</el-card>
			</el-col>
			
		</el-row>
			</el-checkbox-group>  
	</div>

</template>

<script>
	import { onMounted, reactive, ref, toRefs, watch } from 'vue';
	import {Help} from '@icon-park/vue-next';
	import {listPerms} from "@/api/sys/admin/perm"
	import {getRoleResources,updateRoleResource} from "@/api/sys/admin/roles"
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	export default {
	  name: 'PageList',
	  components: {Help,ElForm, ElMessage, ElMessageBox
	  },
	  setup(){
		  const state = reactive({
		    loading: false,
		    title:"",
			formData:{roleId:"",permissionIds:[],permissionNames:[],menuId:""},
			permList:[],
			
		   });
		   const { loading,title,formData,permList} =  toRefs(state);
          function loadData(role,menu){
			  title.value="-"+menu.name;
			  state.loadiargumentsng=true;
			  state.formData.roleId=role.id;
			  state.formData.menuId=menu.id;
			 
			  listPerms({'menuId':menu.id}).then(res=>{
				   let perms=res.data;
				   getRoleResources(role.id,{'menuId':menu.id}).then(res=>{
					   let selectids=res.data;
					     state.formData.permissionIds=selectids;
						 state.formData.permissionNames=[];
					   if(selectids&&perms){
						   perms.forEach(item=>{
							   if(selectids.includes(item.id)){
								   item.hasPerm=true;
								   state.formData.permissionNames.push(item.name);
							   }else{
								   item.hasPerm=false;
							   }
							   if(item&&item.btnPerm.includes("field")){
								   item.isfield=true;
							   }else{
								   item.isfield=false;
							   }
						   })
					   }
					    state.permList=perms;
					    state.loading=false;
				   })
				   
			  })
		  }
		  function permChange(perms){
			      state.formData.permissionIds=[];
				  state.permList.forEach(item=>{
					  if(state.formData.permissionNames.includes(item.name)){
							state.formData.permissionIds.push(item.id);	  
					  }
				  });
			  updateRoleResource(state.formData.roleId,state.formData).then(res=>{
				    ElMessage.success('修改成功');
			  })
		  }
		   return{
			   loadData,permChange,//function
			   loading,title,formData,permList,//modal
		   }
	}
	}
</script>

<style scoped>
	.con-body .el-checkbox-group{
		font-size:16px;
	}
	.m-r-24{
		margin-right:24px;
	}
	.con-body{
		padding:16px;
		padding-top:8px;
	}
</style>

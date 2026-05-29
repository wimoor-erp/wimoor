<template>
	
		<div class='gird-head'>
				<el-button type="primary" @click="handleAdd()">添加角色</el-button>	
				<div class='rt-btn-group'>
						 <el-button   class='ic-btn' title='帮助文档'>
						  <help theme="outline" size="16" :strokeWidth="3"/>
						</el-button>
				</div>
		</div>
		  <div class="w-gird-body">
			 <div class="w-header">
					 <el-input v-model="queryParams.name" @input="handleQuery"  placeholder="搜索角色"  >
						<template #suffix>
						<el-icon style="font-size:16px;" class="el-input__icon"><search/></el-icon>
						 </template> 
					 </el-input>
			 </div>
		   </div>
		<div class='w-gird-body'>
			 <div class='title'>角色列表</div>
			 <div class="rule-list pointer" v-for="(item,index) in ruleData" @click="ruleChange(index)"  :class="{u_active:currentindex==index}">
			 				 <div class="font-small" >
			 					{{item.name}}
			 				 </div>
							 <el-dropdown trigger="click">
							   <span class="el-dropdown-link">
							    <el-link :underline="false" >
							    <more-one class="ic-cen"  theme="outline" size="20"  :strokeWidth="3"/>
							    </el-link>
							   </span>
							    <template #dropdown>
							     <el-dropdown-menu>
							       <el-dropdown-item @click="handleUpdate(item)">编辑</el-dropdown-item>
							       <el-dropdown-item  @click="handleAdd(item)">复制</el-dropdown-item>
								   <el-dropdown-item @click="handleDelete(item)">删除</el-dropdown-item>
							     </el-dropdown-menu>
							 </template>
							 </el-dropdown>
			 </div>
			 <pagination
			   v-if="total > 0"
			   :total="total"
			   layout="pager,total"
			   v-model:page="queryParams.page"
			   v-model:limit="queryParams.limit"
			   @pagination="handleQuery"
			 />
		</div>
		
		<el-dialog
		  :title="dialog.title"
		  v-model="dialog.visible"
		  width="500px"
		  @close="cancel"
		>
		  <el-form
		    ref="dataFormRef"
		    :model="formData"
		    :rules="rules"
		    label-width="100px"
		  >
		    <el-form-item label="角色名称" prop="name">
		      <el-input
		        v-model="formData.name"
		        placeholder="请输入"
		      />
		    </el-form-item>
		  </el-form>
		  <template #footer>
		    <div class="dialog-footer">
		      <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
		      <el-button @click="cancel">取 消</el-button>
		    </div>
		  </template>
		</el-dialog>
</template>
<script>
	import { onMounted, reactive, ref, toRefs, watch } from 'vue';
	import {Help,MoreOne} from '@icon-park/vue-next';
	import {Search} from '@element-plus/icons-vue'
	import {listRolePages,deleteRoles,updateRole,addRole,getRoleFormDetail,getRoleMenus,updateRoleMenu} from "@/api/sys/admin/roles"
	import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
	export default {
	  name: 'RoleList',
	  components: {Help,Search,MoreOne,
	  },
	  emits:["selectRole"],
	  props:[],
	  setup(props,context){
	      const dataFormRef = ref(ElForm);
		  const state = reactive({
		    loading: false,
		    queryParams: { page: 1, limit: 10, name:'',  } ,
		    ruleData: [],
		    total: 0,
			currentindex:0,
		    dialog: { visible: false } ,
		    formData: { name: '', } ,
		    rules: {
		  	     name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
		      },
		   });
		const { total,currentindex, dialog, loading, ruleData, formData, rules, queryParams } =  toRefs(state);
		 
		 function cancel() {
		   state.dialog.visible = false;
		   state.loading=false;
		   state.formData.id = undefined;
		   dataFormRef.value.resetFields();
		 }
		 
		function handleQuery(){
			listRolePages(queryParams.value).then(res=>{
				state.ruleData=res.data;
				state.total=res.total;
				ruleChange(0)
			})
		}
		/**
		 * 角色提交
		 */
		function submitForm() {
		
		  dataFormRef.value.validate((valid) => {
		    if (valid) {
				state.loading=true;
		      if (state.formData.id) {
		        updateRole(state.formData.id, state.formData).then(() => {
		          ElMessage.success('修改成功');
				  state.loading=false;
		          cancel();
		          handleQuery();
		        });
		      }else if(state.formData.copyid){
				  addRole(state.formData).then(res => {
					 var mrole=res.data;
					getRoleMenus(state.formData.copyid).then(res=>{
						  mrole.menuIds=res.data;
						  updateRoleMenu(mrole.id,mrole).then(res=>{
								  ElMessage.success('复制新增成功');
								  state.loading=false;
								  cancel();
								  handleQuery();
						 })
					})
				  });
				
			  } else {
		        addRole(state.formData).then(() => {
		          ElMessage.success('新增成功');
				  state.loading=false;
		          cancel();
		          handleQuery();
		        });
		      }
		    }
		  });
		}
	
		function ruleChange(index){
			currentindex.value = index
			context.emit("selectRole",state.ruleData[index]);
		}
		function handleAdd(row){
			state.formData.id = undefined;
			state.loading=false;
			if(row&&row.id){
				state.formData.copyid=row.id;
				state.dialog = {
				  title: '复制新增角色',
				  visible: true,
				};
			}else{
				state.dialog = {
				  title: '添加角色',
				  visible: true,
				};
			}
			
		}
		function handleUpdate(row){
			state.loading=false;
			const roleId = row.id || state.ids;
			state.dialog = {
			  title: '修改角色',
			  visible: true,
			};
			getRoleFormDetail(roleId).then(res=> {
			  state.formData = res.data;
			});
		}
		function handleDelete(row){
			const ids = [row.id || state.ids].join(',');
			ElMessageBox.confirm(`确认删除已选中的数据项?`, '警告', {
			  confirmButtonText: '确定',
			  cancelButtonText: '取消',
			  type: 'warning',
			})
			  .then(() => {
			    deleteRoles(ids)
			      .then(() => {
			        handleQuery();
			        ElMessage.success('删除成功');
			      })
			      .catch(() => {
			        console.log(`删除失败`);
			      });
			  })
			  .catch(() => ElMessage.info('已取消删除'));
		}
		onMounted(()=>{
			setTimeout(function(){
				handleQuery();
			},500);
			
		});
		return{
			  total,currentindex, dialog, loading, ruleData, formData, rules, queryParams,
			  //modal
			  ruleChange,handleQuery,handleDelete,handleUpdate,handleAdd,submitForm,cancel,
			  //funciton
			  dataFormRef,
			  //ref
		}
	    }
	}
</script>

<style scoped>
	.w-gird-body{
		margin:16px;
	}
	.w-gird-body  .title{
		font-weight: 700;
		font-size: 16px;
		margin-bottom:16px;
	}
	.rule-list{
		display: flex;
		justify-content: space-between;
		align-items: center;
		line-height: 40px;
		padding-right:16px;
		padding-left:16px;
	}
	.rule-list:hover{
		background-color: var(--el-bg-color);
	}
	.rule-list .el-link{
		opacity: 0;
	}
	.rule-list:hover .el-link{
		opacity: 1;
	}
	.u_active{
		background-color: var(--el-color-primary-light-8);
		color: var(--el-color-primary);
	}
	.u_active:hover{
		background-color: var(--el-color-primary-light-8);
	}
</style>

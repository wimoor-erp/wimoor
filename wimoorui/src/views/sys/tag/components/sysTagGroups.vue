 

<template>
    <!-- 搜索表单 -->
		  <div class="flex-center-between m-b-16">
			  <h5>标签分类</h5>
			  <div class="flex-center">
				  <el-button v-if="SearchShow" @click="SearchShow=false" link class="im-but-one " >
				  <el-icon class="font-base"><Search/></el-icon>
				    <span>搜索</span>
				  </el-button>
				  <el-input
				    v-else
				    v-model="queryParams.name"
				    placeholder="标签分类名称"
				    clearable
					@clear = "SearchShow=true;handleQuery"
				    @input="handleQuery"
				  />
				  <el-button @click="handleAdd" link class="im-but-one " >
				   <el-icon class="font-base"><Plus /></el-icon>
				    <span>添加</span>
				  </el-button>
			  </div>
		  </div>
    <!-- 数据表格 -->
	  <el-menu
	        :default-active="0"
	        class="m-t-8"
	      >
	        <el-menu-item  v-for="(item,index) in dictList" :index="index" 
			 @click="handleRowClick(item)"
			>
	          <div style="width:300px; overflow: hidden;text-overflow: ellipsis" >{{item.name}}</div>
			   <el-dropdown class="text-right" @visible-change="visibleChange(item)">
			      <el-icon class="more-icon" :class="{'isvisable':item.isvisable}"><MoreFilled /></el-icon>
			      <template #dropdown>
			        <el-dropdown-menu>
			          <el-dropdown-item  @click.stop="handleDelete(item)">停用</el-dropdown-item>
			          <el-dropdown-item  @click.stop="handleUpdate(item)">重命名</el-dropdown-item>
			        </el-dropdown-menu>
			      </template>
			    </el-dropdown>
	        </el-menu-item>
	      </el-menu>
<!--    <el-table
      highlight-current-row
      :data="dictList"
      v-loading="loading"
     
      @selection-change="handleSelectionChange"
      border
    >
    </el-table>

    <pagination
      v-if="total > 0"
      :total="total"
      v-model:page="queryParams.page"
      v-model:limit="queryParams.limit"
      @pagination="handleQuery"
    /> -->

    <!-- 弹窗表单 -->
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
        label-width="95px"
      >
        <el-form-item label="标签组名称" prop="name">
          <el-input v-model="formData.name" maxlength="20" placeholder="请输入标签组名称" />
        </el-form-item>
        <el-form-item label="标签组描述" prop="description">
          <el-input v-model="formData.description" placeholder="请输入标签组描述" />
        </el-form-item>
		<el-form-item label="排序" prop="sort">
		  <el-input-number
		    v-model="formData.sort"
		    style="width: 80px"
		    controls-position="right"
		    :min="0"
		  />
		   </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio> 
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            placeholder="请输入内容"
            :autosize="{ minRows: 2, maxRows: 4 }"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script  >
import { onMounted, reactive, ref, toRefs,} from 'vue';
import { Plus,Search, Edit, Refresh, Delete,MoreFilled } from '@element-plus/icons-vue';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
 import {
   updateDictType,
   listPageDictTypes, 
   getDictFormData,
   deleteDictTypes,
   addDictType,
 } from '@/api/sys/admin/tag.js';
export default {
  name: 'dictType',
  components:{Search,Plus,Edit,Refresh,Delete,MoreFilled},
  emits:["change"],
  setup(props,context){
			const queryFormRef = ref(ElForm);
			const dataFormRef = ref(ElForm);
			const state = reactive({
			  loading: true,
			  // 选中ID数组
			  ids: [],
			  // 非单个禁用
			  single: true,
			  // 非多个禁用
			  multiple: true,
			  queryParams: {
				page: 1,
				limit: 10,
				name:'',
			  } ,
			  SearchShow:true,
			  dictList: [],
			  total: 0,
			  dialog: { visible: false } ,
			  formData: {
				status: 1,
			  } ,
			  rules: {
				name: [{ required: true, message: '请输入标签组名称', trigger: 'blur' }],
			  },
			});

			const { total, dialog, loading, dictList, formData, rules, queryParams,SearchShow} =
			  toRefs(state);

			function handleQuery() {
			  context.emit('change', {id:null});
			  state.loading = true;
			  listPageDictTypes(state.queryParams).then(res=> {
				state.dictList = res.data;
				//开始默认选中
				handleRowClick(state.dictList[0])
				state.total = res.total;
				state.loading = false;
			  });
			}

			function handleAdd() {
			  state.dialog = {
				title: '添加标签组',
				visible: true,
			  };
			}

			function handleUpdate(item) {
			  state.dialog = {
				title: '修改标签组',
				visible: true,
			  };
			  const id = item.id || state.ids;
			  getDictFormData(id).then(({ data }) => {
				state.formData = data;
			  });
			}

			function submitForm() {
			  dataFormRef.value.validate((isValid) => {
				if (isValid) {
				  if (state.formData.id) {
					updateDictType(state.formData.id, state.formData).then(() => {
					  ElMessage.success('修改成功');
					  cancel();
					  handleQuery();
					});
				  } else {
					addDictType(state.formData).then(() => {
					  ElMessage.success('新增成功');
					  cancel();
					  handleQuery();
					});
				  }
				}
			  });
			}

			function cancel() {
			  state.formData.id = undefined;
			  dataFormRef.value.resetFields();
			  state.dialog.visible = false;
			}

			function handleDelete(item) {
			  const ids = [item.id || state.ids].join(',');
			  ElMessageBox.confirm('确认停用已选中的数据项?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
			  })
				.then(() => {
				  deleteDictTypes(ids).then(() => {
					ElMessage.success('停用成功');
					handleQuery();
				  });
				})
				.catch(() => ElMessage.info('已取消停用'));
			}

			function handleRowClick(row) {
			  context.emit('change', row);
			}
            function visibleChange(item){
				item.isvisable = !item.isvisable
			}
			onMounted(() => {
			  handleQuery();
			});

			return {handleQuery,handleDelete,handleRowClick,
			       cancel,submitForm,handleUpdate,handleAdd,visibleChange,
				   //function
				   state,total, dialog, loading, dictList, formData, rules, queryParams,
				   //modal
				   dataFormRef,
				   //ref
				  SearchShow,
			}
  }
};
</script>
<style scoped="scoped">
	.m-b-16{
		margin-bottom:16px;
	}
	
	.m-b-16 h5{
		line-height: 32px;
	}
	.m-b-16 .el-input{
		margin-right:8px;
	}
	.el-menu{
		border-right:none;
	}
	.more-icon{
		transform:rotate(90deg);
		font-size:14px!important;
		margin-right: -8px!important;
		opacity: 0;
	}
	.el-menu-item{
		display: flex;
		justify-content: space-between;
	}
	.el-menu-item.is-active{
		background-color: var(--el-color-info-light-9);
	}
	.el-menu-item .isvisable{
		opacity: 1;
	}
	.el-menu-item:hover .more-icon{
		opacity: 1;
	}
</style>
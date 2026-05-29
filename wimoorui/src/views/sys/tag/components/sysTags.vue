<template>
    <!-- 搜索表单 -->
     <el-space class="m-b-16">
        <el-button type="primary" :icon="Plus" @click="handleAdd" >新增</el-button>
        <el-button
          :icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          >批量停用</el-button
        >
		<el-input
		  v-model="queryParams.name"
		  placeholder="标签名称" 
		  @clear="handleQuery()"
		  @input="handleQuery()"
		  clearable
		/>
</el-space>

    <!-- 数据表格 -->
    <el-table
      :data="dictItemList"
      v-loading="loading"
      :stripe="true" 
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"   />
      <el-table-column label="标签名称" prop="name" />
      <el-table-column label="标签颜色" prop="color" >
	  <template #default="scope">
		   <el-tag :type="scope.row.color" effect="plain">
			   {{scope.row.name}}
		   </el-tag>
	  </template>
	  </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            type="primary"
            :icon="Edit"
            link
            @click.stop="handleUpdate(scope.row)"
           >编辑</el-button>
          <!-- <el-button
            type="primary"
            link
            @click.stop="handleDelete(scope.row)"
          >删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-if="total > 0"
      :total="total"
      v-model:page="queryParams.page"
      v-model:limit="queryParams.limit"
      @pagination="handleQuery()"
    />

    <!-- 表单弹窗 -->
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
        <el-form-item label="标签名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入标签标签名称"
          />
        </el-form-item>
		<el-form-item label="标签颜色" prop="color">
			    <el-radio-group v-model="formData.color" class="color-radio">
			     <el-radio label="info" border>
			   			<span class="cilcle-black"></span>
			     </el-radio>
			      <el-radio label="blue" border>
					  <span class="cilcle-blue"></span>
				  </el-radio>
				  <el-radio label="success" border>
				  		<span class="cilcle-green"></span>
				  </el-radio>
			      <el-radio label="warning" border>
					  <span class="cilcle-yellow"></span>
				  </el-radio>
			      <el-radio label="danger" border>
					  <span class="cilcle-red"></span>
				  </el-radio>
			    </el-radio-group>
		<!--  <el-input v-model="formData.color" placeholder="请输入标签颜色" /> -->
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
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea"></el-input>
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
<script>

 
import { onMounted, reactive, ref, toRefs, watch } from 'vue';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
import {listPageDictItems, getDictItemData, addDictItem,  updateDictItem, deleteDictItems} from '@/api/sys/admin/tag.js';
import { Search, Plus, Edit, Refresh, Delete } from '@element-plus/icons-vue';
export default {
  name: 'dictItem',
  components:{Search,Plus,Edit,Refresh,Delete,ElForm, ElMessage, ElMessageBox},
  props:["typeCode",],
  setup(props,context){
		const dataFormRef = ref(ElForm);
		const state = reactive({
		  loading: true,
		  // 选中ID数组
		  ids: [] ,
		  // 非单个禁用
		  single: true,
		  // 非多个禁用
		  multiple: true,
		  total: 0,
		  queryParams: { page: 1, limit: 50 } ,
		  dictItemList: [] ,
		  dialog: { visible: false },
		  formData: {
			typeCode: props.typeCode,
			color:'info',
			status: 1,
			sort: 1,
		  } ,
		  rules: {
			name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
		  },
		  localDictCode: props.typeCode,
		});

		const {
		  loading,
		  multiple,
		  queryParams,
		  dictItemList,
		  dialog,
		  formData,
		  rules,
		  total,
		} = toRefs(state);

		function handleQuery(id) {
			if(id&&id.length>1){
				state.queryParams.groupid=id;
				state.formData.taggroupid=id;
			}
		  if (state.queryParams.groupid) {
			  state.loading = true;
			  state.queryParams.groupid=state.queryParams.groupid;
			  listPageDictItems(state.queryParams).then(res => {
				  state.dictItemList = res.data;
				  state.total = res.total;
				  state.loading = false;
			});
		  } else {
			state.dictItemList = [];
			state.total = 0;
			state.queryParams.page = 1;
			state.loading = false;
		  }
		}


		function handleSelectionChange(selection) {
		  state.ids = selection.map((item) => item.id);
		  state.single = selection.length !== 1;
		  state.multiple = !selection.length;
		}

		function handleAdd() {
		  if (!state.formData.taggroupid) {
			ElMessage.warning('请选择标签类型');
			return;
		  }
		  state.dialog = {
			title: '添加标签',
			visible: true,
		  };
		}

		function handleUpdate(row) {
		  state.dialog = {
			title: '修改标签',
			visible: true,
		  };
		  const id = row.id || state.ids;
		  getDictItemData(id).then(({ data }) => {
			state.formData = data;
		  });
		}

		function submitForm() {
		  dataFormRef.value.validate((isValid) => {
			if (isValid) {
			  if (state.formData.id) {
				updateDictItem(state.formData.id, state.formData).then(() => {
				  ElMessage.success('修改成功');
				  cancel();
				  handleQuery();
				});
			  } else {
				addDictItem(state.formData).then(() => {
				  ElMessage.success('新增成功');
				  cancel();
				  handleQuery();
				});
			  }
			}
		  });
		}

		function cancel() {
		  state.dialog.visible = false;
		  state.formData.id = undefined;
		  dataFormRef.value.resetFields();
		}

		function handleDelete(row) {
		  const ids = [row.id || state.ids].join(',');
		  ElMessageBox.confirm('确认停用已选中的数据项?', '警告', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
		  })
			.then(() => {
			  deleteDictItems(ids).then(() => {
				ElMessage.success('批量停用成功');
				handleQuery();
			  });
			})
			.catch(() => ElMessage.info('已取消停用'));
		}
		onMounted(() => {
		  handleQuery();
		});
		
		return{handleQuery,handleDelete,cancel,submitForm,handleUpdate,handleAdd,
		       handleSelectionChange,
		       //function
			   dataFormRef,
			   //ref
			   loading,multiple,queryParams,dictItemList,
			   dialog,formData,rules,total,state,
			   //modal
			
		}
  }
};
</script>
<style>
	.m-b-16{
		margin-bottom: 16px;
	}
	.color-radio .el-radio__input{
		display: none;
	}
	.color-radio .el-radio.is-bordered{
		padding:0px 10px 0px 10px;
	}
	.color-radio .el-radio__label{
		padding-left:0;
	}
	.color-radio .el-radio{
		margin-right:16px;
	}
</style>
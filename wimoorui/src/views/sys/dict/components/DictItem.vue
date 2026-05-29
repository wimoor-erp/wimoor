<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form ref="queryFormRef" :model="queryParams" :inline="true">
      <el-form-item>
        <el-button type="success" :icon="Plus" @click="handleAdd"
          >新增</el-button
        >
        <el-button
          type="danger"
          :icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          >删除</el-button
        >
      </el-form-item>
      <el-form-item prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="数据项名称"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery"
          >搜索</el-button
        >
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      :data="dictItemList"
      v-loading="loading"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"   />
      <el-table-column label="数据项名称" prop="name" />
      <el-table-column label="数据项值" prop="value" />
      <el-table-column label="类型" prop="listClass">
        <template #default="scope">
           {{scope.row.listClass}}
        </template>
      </el-table-column>
      <el-table-column label="样式类" prop="cssClass" />
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="info">禁用</el-tag>
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
          <el-button
            type="danger"
            link
            @click.stop="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-if="total > 0"
      :total="total"
      v-model:page="queryParams.page"
      v-model:limit="queryParams.limit"
      @pagination="handleQuery"
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
        <el-form-item label="字典类型名称">{{ typeName }}</el-form-item>
        <el-form-item label="数据项名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入字典数据项名称"
          />
        </el-form-item>
        <el-form-item label="数据项值" prop="value">
          <el-input v-model="formData.value" placeholder="请输入字典数据项值" />
        </el-form-item>
        <el-form-item label="类型" prop="value">
          <el-input v-model="formData.listClass" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="样式类" prop="cssClass">
          <el-input v-model="formData.cssClass" placeholder="请输入字典样式类" />
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
  </div>
</template>
<script>

 
import { onMounted, reactive, ref, toRefs, watch } from 'vue';
import { ElForm, ElMessage, ElMessageBox } from 'element-plus';
import {listPageDictItems, getDictItemData, addDictItem,  updateDictItem, deleteDictItems} from "@/api/sys/admin/dict.js";
import { Search, Plus, Edit, Refresh, Delete } from '@element-plus/icons-vue';
export default {
  name: 'dictItem',
  components:{Search,Plus,Edit,Refresh,Delete,ElForm, ElMessage, ElMessageBox},
  props:["typeCode","typeName"],
  setup(props,context){
		const queryFormRef = ref(ElForm);
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
		  queryParams: { page: 1, limit: 10 } ,
		  dictItemList: [] ,
		  dialog: { visible: false },
		  formData: {
			typeCode: props.typeCode,
			typeName: props.typeName, name: '',
			value: '',
			listClass: 'default',
			cssClass: '',
			status: 1,
			sort: 1,
		  } ,
		  rules: {
			name: [{ required: true, message: '请输入字典项名称', trigger: 'blur' }],
			value: [{ required: true, message: '请输入字典项值', trigger: 'blur' }],
		  },
		  localDictCode: props.typeCode,
		  localDictName: props.typeName,
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

		function handleQuery(typeCode) {
			if(typeCode&&typeCode.length>1){
				state.queryParams.typeCode = typeCode;
				state.formData.typeCode = typeCode;
				state.queryParams.dictCode=typeCode;
				state.formData.dictCode=typeCode;
			}
		  if (state.queryParams.typeCode) {
			 
			  state.loading = true;
			  listPageDictItems(state.queryParams).then(res => {
				  state.dictItemList = res.data;
				  state.total = res.data.length;
				  state.loading = false;
			});
		  } else {
			state.dictItemList = [];
			state.total = 0;
			state.queryParams.page = 1;
			state.loading = false;
		  }
		}

		function resetQuery() {
		  queryFormRef.value.resetFields();
		  handleQuery();
		}

		function handleSelectionChange(selection) {
		  state.ids = selection.map((item) => item.id);
		  state.single = selection.length !== 1;
		  state.multiple = !selection.length;
		}

		function handleAdd() {
		  if (!state.formData.typeCode) {
			ElMessage.warning('请选择字典类型后添加数据项');
			return;
		  }
		  state.dialog = {
			title: '添加字典数据项',
			visible: true,
		  };
		}

		function handleUpdate(row) {
		  state.dialog = {
			title: '修改字典数据项',
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
		  ElMessageBox.confirm('确认删除已选中的数据项?', '警告', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
		  })
			.then(() => {
			  deleteDictItems(ids).then(() => {
				ElMessage.success('删除成功');
				handleQuery();
			  });
			})
			.catch(() => ElMessage.info('已取消删除'));
		}
		
		watch(props.typeCode, (value) => {
			state.queryParams.typeCode = props.typeCode;
			state.formData.typeCode = props.typeCode;
			handleQuery();
		  },
			{
			  deep: true,
			}
		);
		onMounted(() => {
		  handleQuery();
		});
		return{handleQuery,handleDelete,cancel,submitForm,handleUpdate,handleAdd,
		       handleSelectionChange,resetQuery,
		       //function
			   queryFormRef,dataFormRef,
			   //ref
			   loading,multiple,queryParams,dictItemList,
			   dialog,formData,rules,total,state,
			   //modal
			
		}
  }
};
</script>
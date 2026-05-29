<template>
  <div >

    <div style="padding:0px 0px 10px 0px">
      <el-radio-group v-model="type"   @change="handleChange"  >
        <el-radio-button  v-for="item in typesList"
                          :key="item.typeId"
                          :label="item.typeName"
                          :value="item.typeName" />
      </el-radio-group>
      <el-button style="margin-top:6px;margin-left:6px" link @click="open=true" :icon="Edit">自定义类别</el-button>
    </div>
    <!-- 添加或修改辅助核算类别对话框 -->
    <el-dialog :title="title" v-model="open" width="80%" append-to-body>
      <el-form ref="typesRef" :model="form" :rules="rules" >
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入类型名称：部门、员工、客户等" />
        </el-form-item>
        <el-form-item label="类型编码" prop="typeCode">
          <el-input v-model="form.typeCode" placeholder="请输入类型编码：DEPT、EMP、CUST等" />
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button   v-if="form.typeId"   icon="Plus"   type="primary" @click="submitForm">更 新</el-button>
          <el-button   v-else   icon="Plus"  type="primary" @click="submitForm">新 增</el-button>
          <el-button       @click="reset">重置</el-button>
        </el-col>

        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList">
          <el-tooltip class="item" effect="dark" content="导出" placement="top" >
            <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"

            >导出</el-button>
          </el-tooltip>
        </right-toolbar>
      </el-row>
      <el-table v-loading="loading" :data="typesList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="类型名称" align="center" prop="typeName" />
        <el-table-column label="类型编码" align="center" prop="typeCode" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"  >修改</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"  >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Types">
import { listTypes, getTypes, delTypes, addTypes, updateTypes } from "@/api/finance/auxiliar_types.js"
import {Edit} from '@element-plus/icons-vue';
import finStore from "@/hooks/store/useFinanceStore.js"
const emit = defineEmits(['change']);
const { proxy } = getCurrentInstance()
const typesList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  type:null,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    typeName: null,
    typeCode: null
  },
  rules: {
    typeName: [
      { required: true, message: "类型名称：部门、员工、客户等不能为空", trigger: "blur" }
    ],
    typeCode: [
      { required: true, message: "类型编码：DEPT、EMP、CUST等不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules, type } = toRefs(data)

/** 查询辅助核算类别列表 */
async function getList() {
  loading.value = true
  queryParams.value.tenantId = await finStore.getCurrentTenantId();
  listTypes(queryParams.value).then(response => {
    typesList.value = response.rows;
    if (typesList.value.length > 0) {
      type.value = typesList.value[0].typeId;
    }
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    typeId: null,
    tenantId: null,
    typeName: null,
    typeCode: null
  }
  proxy.resetForm("typesRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}
function handleChange(){
  emit('change', type.value);
}
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.typeId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加辅助核算类别"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _typeId = row.typeId || ids.value;
  getTypes(_typeId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改辅助核算类别"
  })
}

/** 提交按钮 */
 function submitForm() {
  proxy.$refs["typesRef"].validate(async valid => {
    if (valid) {
      form.value.tenantId =await  finStore.getCurrentTenantId();
      if (form.value.typeId != null) {
        updateTypes(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addTypes(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _typeIds = row.typeId || ids.value
  proxy.$modal.confirm('是否确认删除辅助核算类别编号为"' + _typeIds + '"的数据项？').then(function() {
    return delTypes(_typeIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
async function handleExport() {
  queryParams.value.tenantId = await finStore.getCurrentTenantId();
  proxy.download('finance/types/export', {
    ...queryParams.value
  }, `types_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<template>
  <div class="app-container">
    <Types ref="typesRef" @change="handleChange" />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目编码" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入项目编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in subject_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
<!--        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>-->
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Refresh"
          @click="handleSync"
        >同步数据</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="itemsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="辅助核算项目ID" align="center" prop="itemId" />
      <el-table-column label="项目编码" align="center" prop="itemCode" />
      <el-table-column label="项目名称" align="center" prop="itemName" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="subject_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"  >修改</el-button>
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['finance:items:remove']">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改辅助核算具体项目对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="itemsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目编码" prop="itemCode">
          <el-input v-model="form.itemCode" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in subject_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup name="Items">
import { listItems, getItems, delItems, addItems, updateItems, syncSupplier, syncEmployee, syncDept } from "@/api/finance/auxiliar_items.js"
import Types from "@/views/finance/auxiliary/types/index.vue"
import finStore from "@/hooks/store/useFinanceStore.js"
const { proxy } = getCurrentInstance()
const { subject_status } = proxy.useDict('subject_status')

const itemsList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: { status: 1},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    itemCode: null,
    itemName: null,
    status: null,
    createdTime: null
  },
  rules: {
    tenantId: [
      { required: true, message: "租户ID不能为空", trigger: "blur" }
    ],
    typeId: [
      { required: true, message: "辅助核算类型ID不能为空", trigger: "blur" }
    ],
    itemCode: [
      { required: true, message: "项目编码不能为空", trigger: "blur" }
    ],
    itemName: [
      { required: true, message: "项目名称不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)
function handleChange(typeInfo){
  queryParams.value.typeId = typeInfo.typeId;
  queryParams.value.typeCode = typeInfo.typeCode;
  getList();
}
/** 查询辅助核算具体项目列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  listItems(queryParams.value).then(response => {
    itemsList.value = response.rows
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
    itemId: null,
    tenantId: null,
    typeId: null,
    itemCode: null,
    itemName: null,
    status: 1,
    createdTime: null
  }
  proxy.resetForm("itemsRef")
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

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.itemId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加辅助核算具体项目"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _itemId = row.itemId || ids.value
  getItems(_itemId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改辅助核算具体项目"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["itemsRef"].validate(async valid => {
    if (valid) {
      form.value.groupid = await finStore.getCurrentTenantId();
      form.value.typeId=queryParams.value.typeId;
      if (form.value.itemId != null) {
        updateItems(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addItems(form.value).then(response => {
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
  const _itemIds = row.itemId || ids.value
  proxy.$modal.confirm('是否确认删除辅助核算具体项目编号为"' + _itemIds + '"的数据项？').then(function() {
    return delItems(_itemIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/items/export', {
    ...queryParams.value
  }, `items_${new Date().getTime()}.xlsx`)
}

/** 同步数据按钮操作 */
async function handleSync() {
  const typeCode = queryParams.value.typeCode;
  if (!typeCode) {
    proxy.$modal.msgWarning("请先选择一个辅助核算类别");
    return;
  }
  const syncConfig = {
    'SUPPLIER': { label: '供应商', api: syncSupplier, source: 'ERP模块' },
    'EMPLOYEE': { label: '员工', api: syncEmployee, source: 'Admin模块' },
    'DEPT': { label: '部门', api: syncDept, source: 'Admin模块' }
  };
  const config = syncConfig[typeCode];
  if (!config) {
    proxy.$modal.msgWarning("当前类型暂不支持自动同步数据");
    return;
  }
  proxy.$modal.confirm('确认要从' + config.source + '同步' + config.label + '数据吗？已存在的项目编码将自动跳过。').then(async () => {
    const groupid = await finStore.getCurrentTenantId();
    config.api({
      groupid: groupid,
      typeId: queryParams.value.typeId
    }).then(response => {
      proxy.$modal.msgSuccess(response.msg || "同步成功");
      getList();
    });
  }).catch(() => {});
}

getList()
</script>

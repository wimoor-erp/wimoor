<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="凭证编码" prop="voucherNo">
        <el-input
          v-model="queryParams.voucherNo"
          placeholder="请输入凭证编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="凭证日期" prop="voucherDate">
        <el-date-picker clearable
          v-model="queryParams.voucherDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择凭证日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="分录序号" prop="entryNo">
        <el-input
          v-model="queryParams.entryNo"
          placeholder="请输入分录序号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借方金额" prop="debitAmount">
        <el-input
          v-model="queryParams.debitAmount"
          placeholder="请输入借方金额"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="贷方金额" prop="creditAmount">
        <el-input
          v-model="queryParams.creditAmount"
          placeholder="请输入贷方金额"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="摘要说明" prop="summary">
        <el-input
          v-model="queryParams.summary"
          placeholder="请输入摘要说明"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="辅助核算对象ID" prop="auxiliaryId">
        <el-input
          v-model="queryParams.auxiliaryId"
          placeholder="请输入辅助核算对象ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提交人" prop="preparerBy">
        <el-input
          v-model="queryParams.preparerBy"
          placeholder="请输入提交人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdTime">
        <el-date-picker clearable
          v-model="queryParams.createdTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="${comment}" prop="isSuccess">
        <el-input
          v-model="queryParams.isSuccess"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="statusLog">
        <el-input
          v-model="queryParams.statusLog"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['finance:upload:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['finance:upload:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['finance:upload:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['finance:upload:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="uploadList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="租户ID" align="center" prop="groupid" />
      <el-table-column label="凭证类型" align="center" prop="voucherType" />
      <el-table-column label="凭证编码" align="center" prop="voucherNo" />
      <el-table-column label="凭证日期" align="center" prop="voucherDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分录序号" align="center" prop="entryNo" />
      <el-table-column label="会计科目ID" align="center" prop="subjectId" />
      <el-table-column label="借方金额" align="center" prop="debitAmount" />
      <el-table-column label="贷方金额" align="center" prop="creditAmount" />
      <el-table-column label="摘要说明" align="center" prop="summary" />
      <el-table-column label="辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目" align="center" prop="auxiliaryType" />
      <el-table-column label="辅助核算对象ID" align="center" prop="auxiliaryId" />
      <el-table-column label="提交人" align="center" prop="preparerBy" />
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
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

    <!-- 添加或修改凭证分录明细，存储凭证的每一笔分录信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="uploadRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="凭证编码" prop="voucherNo">
          <el-input v-model="form.voucherNo" placeholder="请输入凭证编码" />
        </el-form-item>
        <el-form-item label="凭证日期" prop="voucherDate">
          <el-date-picker clearable
            v-model="form.voucherDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择凭证日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="分录序号" prop="entryNo">
          <el-input v-model="form.entryNo" placeholder="请输入分录序号" />
        </el-form-item>
        <el-form-item label="借方金额" prop="debitAmount">
          <el-input v-model="form.debitAmount" placeholder="请输入借方金额" />
        </el-form-item>
        <el-form-item label="贷方金额" prop="creditAmount">
          <el-input v-model="form.creditAmount" placeholder="请输入贷方金额" />
        </el-form-item>
        <el-form-item label="摘要说明" prop="summary">
          <el-input v-model="form.summary" placeholder="请输入摘要说明" />
        </el-form-item>
        <el-form-item label="辅助核算对象ID" prop="auxiliaryId">
          <el-input v-model="form.auxiliaryId" placeholder="请输入辅助核算对象ID" />
        </el-form-item>
        <el-form-item label="提交人" prop="preparerBy">
          <el-input v-model="form.preparerBy" placeholder="请输入提交人" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdTime">
          <el-date-picker clearable
            v-model="form.createdTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="${comment}" prop="isSuccess">
          <el-input v-model="form.isSuccess" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="statusLog">
          <el-input v-model="form.statusLog" placeholder="请输入${comment}" />
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

<script setup name="Upload">
import { listUpload, getUpload, delUpload, addUpload, updateUpload } from "@/api/finance/upload"

const { proxy } = getCurrentInstance()

const uploadList = ref([])
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
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    groupid: null,
    voucherType: null,
    voucherNo: null,
    voucherDate: null,
    entryNo: null,
    subjectId: null,
    debitAmount: null,
    creditAmount: null,
    summary: null,
    auxiliaryType: null,
    auxiliaryId: null,
    preparerBy: null,
    createdTime: null,
    voucherId: null,
    isSuccess: null,
    statusLog: null
  },
  rules: {
    groupid: [
      { required: true, message: "租户ID不能为空", trigger: "blur" }
    ],
    entryNo: [
      { required: true, message: "分录序号不能为空", trigger: "blur" }
    ],
    voucherId: [
      { required: true, message: "关联的凭证ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询凭证分录明细，存储凭证的每一笔分录信息列表 */
function getList() {
  loading.value = true
  listUpload(queryParams.value).then(response => {
    uploadList.value = response.rows
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
    id: null,
    groupid: null,
    voucherType: null,
    voucherNo: null,
    voucherDate: null,
    entryNo: null,
    subjectId: null,
    debitAmount: null,
    creditAmount: null,
    summary: null,
    auxiliaryType: null,
    auxiliaryId: null,
    preparerBy: null,
    createdTime: null,
    voucherId: null,
    isSuccess: null,
    statusLog: null
  }
  proxy.resetForm("uploadRef")
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
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加凭证分录明细，存储凭证的每一笔分录信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getUpload(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改凭证分录明细，存储凭证的每一笔分录信息"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["uploadRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateUpload(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addUpload(form.value).then(response => {
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
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除凭证分录明细，存储凭证的每一笔分录信息编号为"' + _ids + '"的数据项？').then(function() {
    return delUpload(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/upload/export', {
    ...queryParams.value
  }, `upload_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="凭证日期" prop="voucherDate">
          <el-date-picker clearable
                          v-model="queryParams.voucherDate"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择凭证日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="凭证编号" prop="voucherNo">
          <el-input
              v-model="queryParams.voucherNo"
              placeholder="请输入凭证编号"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
              v-model="queryParams.summary"
              placeholder="请输入摘要"
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
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ledgerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="凭证日期" align="center" prop="voucherDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="凭证编号" align="center" prop="voucherNo" >
        <template #default="scope">
          <span>{{scope.row.voucherType}}-{{scope.row.voucherNo}}</span>
        </template>
      </el-table-column>
      <el-table-column label="摘要" align="center" prop="summary" />
      <el-table-column label="借方金额" align="center" prop="debitAmount" />
      <el-table-column label="贷方金额" align="center" prop="creditAmount" />
      <el-table-column label="当前余额" align="center" prop="balance" />
      <el-table-column label="余额方向" align="center" prop="balanceDirection">
        <template #default="scope">
          <dict-tag :options="fin_direction" :value="scope.row.balanceDirection"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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

    <!-- 添加或修改明细账表对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="ledgerRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="凭证日期" prop="voucherDate">
          <el-date-picker clearable
            v-model="form.voucherDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择凭证日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="凭证编号" prop="voucherNo">
          <el-input v-model="form.voucherNo" placeholder="请输入凭证编号" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="借方金额" prop="debitAmount">
          <el-input v-model="form.debitAmount" placeholder="请输入借方金额" />
        </el-form-item>
        <el-form-item label="贷方金额" prop="creditAmount">
          <el-input v-model="form.creditAmount" placeholder="请输入贷方金额" />
        </el-form-item>
        <el-form-item label="当前余额" prop="balance">
          <el-input v-model="form.balance" placeholder="请输入当前余额" />
        </el-form-item>
        <el-form-item label="余额方向" prop="balanceDirection">
          <el-input v-model="form.balanceDirection" placeholder="请输入余额方向" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdTime">
          <el-date-picker clearable
            v-model="form.createdTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
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

<script setup name="Ledger">
import { listLedger, getLedger, delLedger, addLedger, updateLedger,listLedgerInfo } from "@/api/finance/ledger"
import finStore from "@/hooks/store/useFinanceStore.js"
const { proxy } = getCurrentInstance()
const { subject_status, fin_is_leaf, fin_subject_level, fin_subject_type, fin_direction } = proxy.useDict('subject_status', 'fin_is_leaf', 'fin_subject_level', 'fin_subject_type', 'fin_direction')

const ledgerList = ref([])
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
    subjectId: null,
    voucherId: null,
    entryId: null,
    voucherDate: null,
    voucherNo: null,
    summary: null,
    debitAmount: null,
    creditAmount: null,
    balance: null,
    balanceDirection: null,
    createdTime: null
  },
  rules: {
    voucherId: [
      { required: true, message: "凭证ID不能为空", trigger: "blur" }
    ],
    entryId: [
      { required: true, message: "凭证分录ID不能为空", trigger: "blur" }
    ],
    voucherDate: [
      { required: true, message: "凭证日期不能为空", trigger: "blur" }
    ],
    voucherNo: [
      { required: true, message: "凭证编号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询明细账表列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid= await finStore.getCurrentTenantId();
  listLedgerInfo(queryParams.value).then(response => {
    ledgerList.value = response.rows
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
    detailId: null,
    tenantId: null,
    subjectId: null,
    voucherId: null,
    entryId: null,
    voucherDate: null,
    voucherNo: null,
    summary: null,
    debitAmount: null,
    creditAmount: null,
    balance: null,
    balanceDirection: null,
    createdTime: null
  }
  proxy.resetForm("ledgerRef")
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
  ids.value = selection.map(item => item.detailId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加明细账表"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _detailId = row.detailId || ids.value
  getLedger(_detailId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改明细账表"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["ledgerRef"].validate(valid => {
    if (valid) {
      if (form.value.detailId != null) {
        updateLedger(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addLedger(form.value).then(response => {
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
  const _detailIds = row.detailId || ids.value
  proxy.$modal.confirm('是否确认删除明细账表编号为"' + _detailIds + '"的数据项？').then(function() {
    return delLedger(_detailIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/ledger/export', {
    ...queryParams.value
  }, `ledger_${new Date().getTime()}.xlsx`)
}

getList()
</script>

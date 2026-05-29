<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="会计期间" prop="period">
          <el-input
              v-model="queryParams.period"
              placeholder="请输入会计期间"
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
        <el-form-item label="更新时间" prop="updatedTime">
          <el-date-picker clearable
                          v-model="queryParams.updatedTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择更新时间">
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
      <el-table-column label="会计期间" align="center" prop="period" />
      <el-table-column label="会计科目" align="center" prop="subjectName" >

      </el-table-column>
      <el-table-column label="期初余额" align="center" prop="beginBalance" />
      <el-table-column label="期初余额方向" align="center" prop="beginDirection">
        <template #default="scope">
          <dict-tag :options="fin_direction" :value="scope.row.beginDirection"/>
        </template>
      </el-table-column>
      <el-table-column label="本期借方合计" align="center" prop="debitTotal" />
      <el-table-column label="本期贷方合计" align="center" prop="creditTotal" />
      <el-table-column label="期末余额" align="center" prop="endBalance" />
      <el-table-column label="期末余额方向" align="center" prop="endDirection">
        <template #default="scope">
          <dict-tag :options="fin_direction" :value="scope.row.endDirection"/>
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

    <!-- 添加或修改总账对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="ledgerRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="租户ID" prop="tenantId">
          <el-input v-model="form.tenantId" placeholder="请输入租户ID" />
        </el-form-item>
        <el-form-item label="会计期间" prop="period">
          <el-input v-model="form.period" placeholder="请输入会计期间" />
        </el-form-item>
        <el-form-item label="期初余额" prop="beginBalance">
          <el-input v-model="form.beginBalance" placeholder="请输入期初余额" />
        </el-form-item>
        <el-form-item label="期初余额方向" prop="beginDirection">
          <el-input v-model="form.beginDirection" placeholder="请输入期初余额方向" />
        </el-form-item>
        <el-form-item label="本期借方合计" prop="debitTotal">
          <el-input v-model="form.debitTotal" placeholder="请输入本期借方合计" />
        </el-form-item>
        <el-form-item label="本期贷方合计" prop="creditTotal">
          <el-input v-model="form.creditTotal" placeholder="请输入本期贷方合计" />
        </el-form-item>
        <el-form-item label="期末余额" prop="endBalance">
          <el-input v-model="form.endBalance" placeholder="请输入期末余额" />
        </el-form-item>
        <el-form-item label="期末余额方向" prop="endDirection">
          <el-input v-model="form.endDirection" placeholder="请输入期末余额方向" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdTime">
          <el-date-picker clearable
            v-model="form.createdTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updatedTime">
          <el-date-picker clearable
            v-model="form.updatedTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择更新时间">
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
import { listLedger, getLedger, delLedger, addLedger, updateLedger,listInfo } from "@/api/finance/ledger_general"
import finStore from "@/hooks/store/useFinanceStore.js";
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
    period: null,
    createdTime: null,
    updatedTime: null
  },
  rules: {
    period: [
      { required: true, message: "会计期间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询总账列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid= await finStore.getCurrentTenantId();
  listInfo(queryParams.value).then(response => {
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
    ledgerId: null,
    tenantId: null,
    subjectId: null,
    period: null,
    beginBalance: null,
    beginDirection: null,
    debitTotal: null,
    creditTotal: null,
    endBalance: null,
    endDirection: null,
    createdTime: null,
    updatedTime: null
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
  ids.value = selection.map(item => item.ledgerId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加总账"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _ledgerId = row.ledgerId || ids.value
  getLedger(_ledgerId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改总账"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["ledgerRef"].validate(valid => {
    if (valid) {
      if (form.value.ledgerId != null) {
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
  const _ledgerIds = row.ledgerId || ids.value
  proxy.$modal.confirm('是否确认删除总账编号为"' + _ledgerIds + '"的数据项？').then(function() {
    return delLedger(_ledgerIds)
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

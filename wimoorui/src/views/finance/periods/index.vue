<template>
  <div class="app-container">
     <el-row gutter="20">
       <el-col :span="6"  style="margin-bottom:20px" v-for="item in periodsList" :key="item.periodId" >
         <el-card style="padding:20px" :shadow="item.periodStatus === 2? 'always' :item.periodStatus === 1? 'hover' : 'never'" :class="item.periodStatus === 2? 'activeCard' :item.periodStatus === 1? 'infoCard' : 'dangerCard'">
           <template #header>
             <div class="card-header">
                 {{ item.periodName }}
             </div>
           </template>
            <div class="text-center">
              {{item.startDate}} 到 {{item.endDate}}
            </div>
           <template #footer>
             <div class="text-right">
               <el-tag :type="item.periodStatus === 2? 'success' :item.periodStatus === 1? 'info' : 'danger'">
                 {{ item.periodStatus === 2 ? '已开启' :item.periodStatus === 1 ? '未启用':'已关闭'}}
               </el-tag>
             </div>
           </template>


         </el-card>
       </el-col>
     </el-row>

  </div>
</template>

<script setup name="Periods">
import { listPeriods, getPeriods, delPeriods, addPeriods, updatePeriods } from "@/api/finance/periods"
import finStore from "@/hooks/store/useFinanceStore.js";
const { proxy } = getCurrentInstance()

const periodsList = ref([])
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
    pageSize: 1000,
    periodCode: null,
    periodName: null,
    startDate: null,
    groupid:"#",
    endDate: null,
    periodStatus: null,
    isCurrent: null,
    createdTime: null
  },
  rules: {
    tenantId: [
      { required: true, message: "租户ID不能为空", trigger: "blur" }
    ],
    periodCode: [
      { required: true, message: "期间编码，格式：YYYYMM不能为空", trigger: "blur" }
    ],
    periodName: [
      { required: true, message: "期间名称，如：2024年1月不能为空", trigger: "blur" }
    ],
    startDate: [
      { required: true, message: "期间开始日期不能为空", trigger: "blur" }
    ],
    endDate: [
      { required: true, message: "期间结束日期不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询会计期间管理列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  listPeriods(queryParams.value).then(response => {
    periodsList.value = response.rows
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
    periodId: null,
    tenantId: null,
    periodCode: null,
    periodName: null,
    startDate: null,
    endDate: null,
    periodStatus: null,
    isCurrent: null,
    createdTime: null
  }
  proxy.resetForm("periodsRef")
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
  ids.value = selection.map(item => item.periodId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加会计期间管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _periodId = row.periodId || ids.value
  getPeriods(_periodId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会计期间管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["periodsRef"].validate(valid => {
    if (valid) {
      if (form.value.periodId != null) {
        updatePeriods(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPeriods(form.value).then(response => {
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
  const _periodIds = row.periodId || ids.value
  proxy.$modal.confirm('是否确认删除会计期间管理编号为"' + _periodIds + '"的数据项？').then(function() {
    return delPeriods(_periodIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/periods/export', {
    ...queryParams.value
  }, `periods_${new Date().getTime()}.xlsx`)
}

getList()
</script>

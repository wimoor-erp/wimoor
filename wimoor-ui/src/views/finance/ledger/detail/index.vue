<template class="main-sty">
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="凭证期间" prop="voucherDateStr">
          <el-date-picker
              v-model="queryParams.periodRange"
              type="monthrange"
              value-format="YYYYMM"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          />
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
    <el-row>
      <el-col :span="4">
        <el-tree
          ref="treeRef"
          :data="subjectTreeData"
          :props="{ label: 'subjectName', children: 'children' }"
          node-key="subjectCode"
          :expand-on-click-node="false"
          highlight-current
          @node-click="handleTreeNodeClick"
          style="height:calc(100vh - 170px); overflow-y: auto; border-right: 1px solid #ebeef5;"
        />
      </el-col>
      <el-col :span="20">
        <div v-if="currentSubject.code" style="text-align: right; padding: 0 0 8px 0; font-size: 14px; color: #409eff; font-weight: bold;">
          当前科目：{{ currentSubject.code }}-{{ currentSubject.name }}
        </div>
    <el-table v-loading="loading" border style="height:calc(100vh - 240px); overflow-y: auto;" :data="ledgerList" @sort-change="handleSortChange" @selection-change="handleSelectionChange" :default-sort="{ prop: 'voucherDate', order: 'ascending' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="凭证日期"   prop="voucherDate" width="100"  >
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="凭证编号"  width="98" prop="voucherNo" >
        <template #default="scope">
            <el-link  @click="handleEdit(scope.row)"   type="primary">{{ scope.row.voucherType }}-{{ scope.row.voucherNo }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="摘要" show-overflow-tooltip width="300" prop="summary"/>
      <el-table-column label="会计科目" show-overflow-tooltip width="430" prop="subjectId"  >
        <template #default="scope">
          <span>{{ scope.row.subjectName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借方金额"   prop="debitAmount" />
      <el-table-column label="贷方金额"   prop="creditAmount" />
      <el-table-column label="当前余额"  prop="balance" />
      <el-table-column label="余额方向"   prop="balanceDirection" width="80">
        <template #default="scope">
          <dict-tag :options="fin_direction" :value="scope.row.balanceDirection"/>
        </template>
      </el-table-column>
    </el-table>
      </el-col>
    </el-row>
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
import { onMounted, nextTick } from "vue";
import { listLedger, getLedger, delLedger, addLedger, updateLedger,listLedgerInfo,treeSubjects } from "@/api/finance/ledger"
import finStore from "@/hooks/store/useFinanceStore.js"
import {useRouter, useRoute} from "vue-router";
const emitter = inject("emitter");
const { proxy } = getCurrentInstance()
const { subject_status, fin_is_leaf, fin_subject_level, fin_subject_type, fin_direction } = proxy.useDict('subject_status', 'fin_is_leaf', 'fin_subject_level', 'fin_subject_type', 'fin_direction')
let router = useRouter();
let route = useRoute();
const ledgerList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const title = ref("")
const treeRef=ref();
const subjectTreeData = ref([])
const currentSubject = ref({ code: '', name: '' })
const initSubjectCode = ref(route.query.subjectCode || '')

const data = reactive({
  form: {
    voucherDate: null
  },
  queryParams: {
    subjectId: null,
    voucherId: null,
    entryId: null,
    periodRange: [],
    voucherNo: null,
    summary: null,
    debitAmount: null,
    creditAmount: null,
    balance: null,
    balanceDirection: null,
    createdTime: null,
    orderByColumn: 'v.voucherDate,v.voucherNo',
    isAsc: 'asc'
  },
  rules: {
    voucherId: [
      { required: true, message: "凭证ID不能为空", trigger: "blur" }
    ],
    entryId: [
      { required: true, message: "凭证分录ID不能为空", trigger: "blur" }
    ],
    voucherNo: [
      { required: true, message: "凭证编号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)





/** 查询明细账表列表 */
async function getList() {
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  await loadTree()
  await fetchList()
}

/** 加载科目树（仅首次或搜索时调用） */
async function loadTree() {
  const groupid = await finStore.getCurrentTenantId();
  const params = { groupid: groupid };
  if (queryParams.value.periodRange && queryParams.value.periodRange.length === 2) {
    params.startPeriod = queryParams.value.periodRange[0];
    params.endPeriod = queryParams.value.periodRange[1];
    delete params.periodRange;
  }
  const response = await treeSubjects(params);
  subjectTreeData.value = response.data || [];

  // 如果有路由参数 subjectCode，自动选中对应科目
  if (initSubjectCode.value) {
    const targetCode = initSubjectCode.value;
    const findNode = (nodes) => {
      for (const node of nodes) {
        if (node.subjectCode === targetCode) return node;
        if (node.children && node.children.length > 0) {
          const found = findNode(node.children);
          if (found) return found;
        }
      }
      return null;
    };
    const targetNode = findNode(subjectTreeData.value);
    if (targetNode) {
      await nextTick();
      const expandParent = (nodes, targetCode) => {
        for (const node of nodes) {
          if (node.children && node.children.length > 0) {
            if (node.children.some(c => c.subjectCode === targetCode)) {
              treeRef.value?.store.nodesMap[node.subjectCode]?.expand();
              return true;
            }
            if (expandParent(node.children, targetCode)) {
              treeRef.value?.store.nodesMap[node.subjectCode]?.expand();
              return true;
            }
          }
        }
        return false;
      };
      expandParent(subjectTreeData.value, targetCode);
      treeRef.value?.setCurrentKey(targetCode);
      currentSubject.value = { code: targetNode.subjectCode, name: targetNode.subjectName };
      queryParams.value.subjectCode = targetNode.subjectCode;
      queryParams.value.subjectId = null;
      initSubjectCode.value = '';
    }
  } else if (subjectTreeData.value.length > 0 && !currentSubject.value.code) {
    const firstNode = subjectTreeData.value[0];
    await nextTick();
    treeRef.value?.setCurrentKey(firstNode.subjectCode);
    if (firstNode.children && firstNode.children.length > 0) {
      treeRef.value?.store.nodesMap[firstNode.subjectCode]?.expand();
    }
    currentSubject.value = { code: firstNode.subjectCode, name: firstNode.subjectName };
    await nextTick();
    if (firstNode.children && firstNode.children.length > 0) {
      queryParams.value.subjectCode = firstNode.subjectCode;
      queryParams.value.subjectId = null;
    } else {
      queryParams.value.subjectId = firstNode.subjectId;
      queryParams.value.subjectCode = null;
    }
  }
}

/** 仅获取列表数据（不重新加载树） */
function fetchList() {
  loading.value = true
  const listParams = { ...queryParams.value };
  if (queryParams.value.periodRange && queryParams.value.periodRange.length === 2) {
    listParams.startPeriod = queryParams.value.periodRange[0];
    listParams.endPeriod = queryParams.value.periodRange[1];
    delete listParams.periodRange;
  }
  listLedgerInfo(listParams).then(response => {
    ledgerList.value = response.rows
  }).finally(() => {
    loading.value = false
  })
}

/** 树节点点击 */
function handleTreeNodeClick(data) {
  currentSubject.value = { code: data.subjectCode, name: data.subjectName };
  if (data.children && data.children.length > 0) {
    queryParams.value.subjectCode = data.subjectCode;
    queryParams.value.subjectId = null;
  } else {
    queryParams.value.subjectId = data.subjectId;
    queryParams.value.subjectCode = null;
  }
  fetchList();
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
  getList()
}
function handleSortChange({ prop, order }) {
  if (prop === "voucherDate" || prop === "voucherNo") {
    queryParams.value.orderByColumn = "v." + prop;
  } else {
    queryParams.value.orderByColumn = "fdl." + prop;
  }
  queryParams.value.isAsc = order === 'ascending' ? 'asc' : 'desc';
  fetchList()
}
/** 重置按钮操作 */
function resetQuery() {
  currentSubject.value = { code: '', name: '' };
  queryParams.value.subjectCode = null;
  queryParams.value.subjectId = null;
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
function handleEdit(row){
  emitter.emit("removeCache", "凭证录入");
  router.push({
    path:"/fin/voucher/create",
    query:{
      title:'凭证录入',
      path:"/fin/voucher/create",
      voucherId:row.voucherId
    },
  })
}
/** 导出按钮操作 */
function handleExport() {
  const params = { ...queryParams.value };
    if (queryParams.value.periodRange&&queryParams.value.periodRange.length === 2) {
    params.startPeriod = queryParams.value.periodRange[0];
    params.endPeriod = queryParams.value.periodRange[1];
    delete params.periodRange;
  }
  proxy.download('/api/finance/detail_ledger/export', {
    ...params
  }, `ledger_${new Date().getTime()}.xlsx`)
}
// 获取当前期间
async function getCurrentPeriod() {
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = String(now.getMonth() + 1).padStart(2, '0')
  const period = await finStore.getCurrentPeriod()
  const currentPeriod = `${currentYear}${currentMonth}`

  if (period && period.periodCode) {
    const periodYear = parseInt(period.periodCode.substring(0, 4))
    if (periodYear <= currentYear + 1) {
      queryParams.value.periodRange = [period.periodCode, currentPeriod]
    } else {
      queryParams.value.periodRange = [currentPeriod, currentPeriod]
    }
  } else {
    queryParams.value.periodRange = [currentPeriod, currentPeriod]
  }
}

onMounted(async () => {
  await getCurrentPeriod();
  getList()
})


</script>

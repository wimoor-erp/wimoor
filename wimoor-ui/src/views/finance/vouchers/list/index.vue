<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="凭证期间" prop="voucherDateStr">
          <el-date-picker
              v-model="queryParams.periodRange"
              type="monthrange"
              range-separator="至"
              @change="submitSearch()"
              value-format="YYYYMM"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          />

          <!--        <el-date-picker clearable-->
          <!--                        v-model="queryParams.voucherDate"-->
          <!--                        type="date"-->
          <!--                        value-format="YYYY-MM-DD"-->
          <!--                        placeholder="请选择凭证期间">-->
          <!--        </el-date-picker>-->
        </el-form-item>
        <el-form-item>
          <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible"   :width="400" trigger="click">
            <template #reference>

              <el-button  :color="filterBtnColor" plain class='ic-btn'>
                  <filtericon></filtericon>
              </el-button>
            </template>

              <el-form-item label="凭证字号" prop="voucherNo">
                <el-input
                    v-model="queryParams.voucherNo"
                    placeholder="请输入凭证字号,不含类型"
                    clearable
                />
              </el-form-item>
              <el-form-item label="记录摘要" prop="summary">
                <el-input
                    v-model="queryParams.summary"
                    placeholder="请输入摘要"
                    clearable
                />
              </el-form-item>
              <el-form-item label="会计科目" prop="subjectId">
                <el-select
                    :teleported="false"
                    v-model="queryParams.subjectId"
                    placeholder="请选择会计科目"
                    clearable
                >
                  <el-option
                      v-for="item in subjects"
                      :key="item.subjectId"
                      :label="item.subjectName"
                      :value="item.subjectId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="制单人" prop="preparerBy">
                <el-input
                    v-model="queryParams.preparerBy"
                    placeholder="请输入制单人"
                    clearable
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitSearch()">搜索</el-button>
                <el-button @click="resetSearch()">取消</el-button>
              </el-form-item>

          </el-popover>

        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            :icon="Check"
            @click="handleApprove"
        >审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown>
          <el-button
              type="warning"
              plain
              :icon="Download"
          >
            导出<el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleExport">模板导出</el-dropdown-item>
              <el-dropdown-item @click="handleRecordExport">记录导出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>

      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            :icon="Upload"
            @click="handleImportVoucherShow"
        >导入凭证</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table height="calc(100vh - 220px)" border v-loading="loading" :data="vouchersList" @selection-change="handleSelectionChange" :span-method="objectSpanMethod" :cell-style="tableCellStyle">
      <el-table-column type="selection" width="40" align="center"  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <!-- 编辑和删除按钮-->
          <el-button link type="primary" :icon="Edit" @click="handleEdit(scope.row)"   >编辑</el-button>
          <el-button link type="danger" style="margin-left: 0px" :icon="Delete" @click="handleDelete(scope.row)"  >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="凭证日期"  prop="voucherDate" width="130">
        <template #header >
          <el-space>
            <div>凭证日期</div>
            <el-icon v-if="queryParams.order === 'desc'" @click="queryParams.order='asc';handleQuery()" style="margin-left: 5px;"> <sort-amount-down strokeLinecap="square"/></el-icon>
            <el-icon v-if="queryParams.order === 'asc'" @click="queryParams.order='desc';handleQuery()" style="margin-left: 5px;"> <sort-amount-up strokeLinecap="square"/></el-icon>
          </el-space>

        </template>
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="凭证字号"   width="100" prop="voucherNo" >
        <template #header >
          <el-space>
            <div>凭证字号</div>
            <el-icon v-if="queryParams.order2 === 'desc'" @click="queryParams.order2='asc';handleQuery()" style="margin-left: 5px;"> <sort-amount-down strokeLinecap="square"/></el-icon>
            <el-icon v-if="queryParams.order2 === 'asc'" @click="queryParams.order2='desc';handleQuery()" style="margin-left: 5px;"> <sort-amount-up strokeLinecap="square"/></el-icon>
          </el-space>
        </template>
        <template #default="scope">
          <el-link  @click="handleEdit(scope.row)"   type="primary">{{ scope.row.voucherType }}-{{ scope.row.voucherNo }}</el-link>
        </template>
      </el-table-column>

      <el-table-column label="摘要"  prop="summary"  width="400"/>
      <el-table-column label="科目" prop="subjectName" width="480"/>
      <el-table-column label="借方金额"  prop="debitAmount" width="140"/>
      <el-table-column label="贷方金额"   prop="creditAmount" width="140"/>

      <el-table-column label="附件数量" align="center" prop="attachmentCount" />
      <el-table-column label="附件上传" align="center" prop="attachmentCount"  >
        <template #default="scope">
          <el-button type="primary" link @click="uploadAttachment(scope.row)" size="small">
            <el-icon><Upload /></el-icon>上传附件
          </el-button>

        </template>
      </el-table-column>
      <el-table-column label="原单据编号"   width="130" prop="originalDocNo" />
      <el-table-column label="制单人"  prop="preparerBy" />
      <el-table-column label="审核人"   prop="auditorBy" />
      <el-table-column label="状态"  prop="auditStatus" >
        <template #default="scope">
<!--          0-草稿，1-待审核，2-已审核，3-已过账，4-已作废-->
          <span v-if="scope.row.voucherStatus == 0">草稿</span>
          <span v-else-if="scope.row.voucherStatus == 1">待审核</span>
          <span v-else-if="scope.row.voucherStatus == 2">已审核</span>
          <span v-else-if="scope.row.voucherStatus == 3">已过账</span>
          <span v-else-if="scope.row.voucherStatus == 4">已作废</span>
          <span v-else>未知状态</span>
        </template>
      </el-table-column>
<!--      备注-->
      <el-table-column label="备注"   prop="remark" />
<!--      制单日期-->
      <el-table-column label="制单日期"  prop="voucherDate" width="130">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
        v-show="total>0"
        :total="total"
        :pageSizes="[10, 50, 100, 500]"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />
    <UploadDialog ref="fileUpload" @change="handleFilesChange" type="finance" :file-type="['pdf','jpg','png']"></UploadDialog>
    <ImportVoucherDialog ref="importVoucherDialog" @upload-success="handleImportSuccess"></ImportVoucherDialog>
  </div>
</template>

<script setup name="Vouchers">
import { onMounted, inject, reactive, ref, toRefs } from "vue";
import {Refresh,Search,Download,Check,Upload,Edit,Delete,ArrowDown} from '@element-plus/icons-vue'
import { isDark } from "@/components/composables/dark.js"
import {SortAmountDown,SortAmountUp} from '@icon-park/vue-next';
import { listVouchers, approveVoucher,passVouchers,delVouchers, updateVouchersFiles } from "@/api/finance/vouchers.js"
import { listSubjects } from "@/api/finance/subjects.js"
import finStore from "@/hooks/store/useFinanceStore.js"
import { useRoute,useRouter } from 'vue-router'
import UploadDialog from "@/components/FileUpload/UploadDialog.vue";
import ImportVoucherDialog from "@/views/finance/vouchers/create/components/ImportVoucherDialog.vue";
import filtericon from "@/components/icon/filtericon.vue";
const emitter = inject("emitter");
let router = useRouter();
const { proxy } = getCurrentInstance()

const vouchersList = ref([])
const subjects = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const total = ref(0)
const filterBtnColor = ref()
const queryRef=ref();
let moreSearchVisible=ref(false)
const fileUpload=ref();
const importVoucherDialog=ref();
let currentVoucher = ref(null)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    voucherNo: null,
    voucherDate: null,
    summary: null,
    order:'desc',
    order2:'desc',
    subjectName: null,
    preparerName: null,
    periodRange: [],
    preparerBy: null,
    subjectId: null,
    groupid: null,
  }
})

const { queryParams } = toRefs(data)

/** 获取指定月份的最后一天 */
function getMonthLastDay(dateStr) {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const lastDay = new Date(year, month, 0);
  
  const yyyy = lastDay.getFullYear();
  const mm = String(lastDay.getMonth() + 1).padStart(2, '0');
  const dd = String(lastDay.getDate()).padStart(2, '0');
  
  return `${yyyy}-${mm}-${dd}`;
}

/** 查询记账凭证列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid=await finStore.getCurrentTenantId();
  
  // 处理日期范围参数
  const params = { ...queryParams.value };
  if (params.periodRange&&params.periodRange.length === 2) {
    params.startPeriod = params.periodRange[0];
    params.endPeriod = params.periodRange[1];
    delete params.periodRange;
  }
  if(params.order==='desc'){
      if(params.order2==='desc'){
        params.order='desc'
      }else{
        params.order='descasc'
      }
  }else{
    if(params.order2==='desc'){
      params.order='ascdesc'
    }else{
      params.order='asc'
    }
  }
  listVouchers(params).then(response => {
	  vouchersList.value = [];
	  response.rows.forEach(row=>{
		 var myrow=row.entries;
		 var length=row.entries.length;
	     myrow.forEach((item,index)=>{
			 const targetObj =item;
			 const sourceObj =row;
			 const excludeProp = 'entries';
			 
			 // 创建源对象的副本
			 const sourceCopy = { ...sourceObj };
			 
			 // 删除要避开的属性
			 delete sourceCopy[excludeProp];
			 
			 // 使用Object.assign合并
			 const result = Object.assign({}, targetObj, sourceCopy);
			 if(index==0){
			 result.rowspan=length;
			 }
			 // 标记合并行的最后一行
			 result.isLastRow = (index === length - 1);
			 // 追加辅助核算项目后缀到科目名称
			 if (result.auxiliaryList && result.auxiliaryList.length > 0 && result.subjectName) {
			   const parts = result.auxiliaryList.map(a => a.itemCode + '_' + a.itemName);
			   result.subjectName = result.subjectName + ' - ' + parts.join(' - ');
			 }
			 vouchersList.value.push(result);
		 })
	  });
    total.value = response.total
    loading.value = false
  })
}

function submitSearch() {
  if (queryParams.value.voucherNo != "" ||
      queryParams.value.summary != "" ||
      queryParams.value.subjectId != "" ||
      queryParams.value.preparerBy != ""
  ) {
    filterBtnColor.value = "#ff6700"
  } else {
    filterBtnColor.value = ""
  }
  handleQuery();
  moreSearchVisible.value = false;
}

function resetSearch() {
  resetQuery();
  moreSearchVisible.value = false;
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
function handleEdit(row){
  // 凭证录入 缓存移除
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

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  filterBtnColor.value = ""
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.voucherId)
}

/** 查看按钮操作 */
function handleView(row) {
  // 这里可以添加查看详情的逻辑
  proxy.$modal.msg({
    title: '查看凭证',
    content: '凭证详情查看功能待实现',
    showCancel: false
  })
}

async function loadSubjects() {
  listSubjects({groupid: await finStore.getCurrentTenantId()}).then(response => {
    subjects.value = response.data;
  })
}

/** 模板导出按钮操作 */
async function handleExport() {
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  
  // 处理日期范围参数
  const params = { ...queryParams.value };
  if (params.voucherDateStr && params.voucherDateStr.length === 2) {
    params.startPeriod = params.periodRange[0];
    params.endPeriod = params.periodRange[1];
    delete params.voucherDateStr;
  }
  
  proxy.download('/api/finance/entries/export', {
    ...params
  }, `vouchersEntries_${new Date().getTime()}.xlsx`)
}

/** 记录导出按钮操作 */
async function handleRecordExport() {
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  
  // 处理日期范围参数
  const params = { ...queryParams.value };
  if (params.voucherDateStr && params.voucherDateStr.length === 2) {
    params.startDate = getMonthLastDay(params.voucherDateStr[0]);
    params.endDate = getMonthLastDay(params.voucherDateStr[1]);
    delete params.voucherDateStr;
  }
  
  proxy.download('/api/finance/entries/exportRecord', {
    ...params
  }, `voucherRecord_${new Date().getTime()}.xlsx`)
}

const objectSpanMethod = ({ row, column, rowIndex, columnIndex }) => {
  if(![4,5,6,7].includes(columnIndex)){
    return {
      rowspan: row.rowspan,
      colspan: 1,
    }
  }else {
    return {
      rowspan: 1,
      colspan: 1,
    }
  }

}

const tableCellStyle = ({ row, column, rowIndex, columnIndex }) => {
  const thickBorder = isDark.value
    ? { borderBottom: '1.5px solid #aaaaaa' }
    : { borderBottom: '1.5px solid #303133' }
  const thinBorder = isDark.value
    ? { borderBottom: '0.5px solid #4a4a4a' }
    : { borderBottom: '0.5px solid #dcdfe6' }

  // 非合并列（摘要、科目、借方、贷方）每行都有独立的 td
  if ([4,5,6,7].includes(columnIndex)) {
    return row.isLastRow ? thickBorder : thinBorder
  }

  // 合并列：td 存在于组的第一行，rowspan 跨越整个组
  if (row.rowspan && row.rowspan > 1) {
    const lastRowIndex = rowIndex + row.rowspan - 1
    const lastRow = vouchersList.value[lastRowIndex]
    if (lastRow && lastRow.isLastRow) {
      return thickBorder
    }
  } else if (row.isLastRow) {
    // 单行组
    return thickBorder
  }

  return {}
}

// 审核按钮操作
function handleApprove() {
  if(ids.value.length==0){
    //MessageBox 消息弹框
    proxy.$message({
      message: '请选择要审核的凭证',
      type: 'warning'
    });
    return;
  }
  //弹框提示 请确认是否操作审核
  proxy.$confirm('确认审核选中的凭证吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ids.value = [...new Set(ids.value)];
    approveVoucher(ids.value).then(response => {
      console.log(response);
      proxy.$modal.msgSuccess(
          '审核凭证成功'
      )
      getList();
    })
  })
}


function handleDelete(row) {
  //弹框提示 请确认是否操作过账
  proxy.$confirm('确认要删除凭证吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    delVouchers([row.voucherId]).then(res=>{
      proxy.$modal.msgSuccess(
          '删除凭证成功'
      )
      getList();
    });
  })
}

function uploadAttachment(row) {
  currentVoucher.value = row;
  fileUpload.value.show();
}

function handleFilesChange(files) {
  if (currentVoucher.value) {
    const voucher = {
      voucherId: currentVoucher.value.voucherId,
      files: files
    };
    updateVouchersFiles(voucher).then(() => {
      proxy.$modal.msgSuccess('上传成功');
      fileUpload.value.hide();
      getList();
    });
  }
}

function handleImportVoucherShow() {
  importVoucherDialog.value.show();
}

function handleImportSuccess() {
  proxy.$modal.msgSuccess('导入成功');
  getList();
}

// 获取当前期间
async function initDefaultPeriod() {
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
  await initDefaultPeriod();
  loadSubjects();
  getList();
})
</script>
<style scoped>

</style>

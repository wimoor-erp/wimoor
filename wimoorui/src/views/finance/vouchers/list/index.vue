<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="凭证期间" prop="voucherDateStr">


          <el-date-picker
              v-model="queryParams.voucherDateStr"
              type="monthrange"
              range-separator="至"
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
                <i>
                  <svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
                    <path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
                </i>
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
        <el-button
            type="warning"
            plain
            :icon="Download"
            @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vouchersList" @selection-change="handleSelectionChange" :span-method="objectSpanMethod">
      <el-table-column type="selection" width="40" align="center"  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <!-- 编辑和删除按钮-->
          <el-button link type="primary" :icon="Edit" @click="handleEdit(scope.row)"   >编辑</el-button>
          <el-button link type="danger" style="margin-left: 0px" :icon="Delete" @click="handleDelete(scope.row)"  >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="凭证日期" align="center" prop="voucherDate" width="130">
        <template #default="scope">
          <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="凭证字号" align="center" prop="voucherNo" >
        <template #default="scope">
          <el-link  @click="handleEdit(scope.row)"   type="primary">{{ scope.row.voucherType }}-{{ scope.row.voucherNo }}</el-link>
        </template>
      </el-table-column>

      <el-table-column label="摘要" align="center" prop="summary" />
      <el-table-column label="科目" align="center" prop="subjectName" />
      <el-table-column label="借方金额" align="center" prop="debitAmount" />
      <el-table-column label="贷方金额" align="center" prop="creditAmount" />

      <el-table-column label="附件数量" align="center" prop="attachmentCount" />
      <el-table-column label="原单据编号" align="center" prop="originalDocNo" />
      <el-table-column label="制单人" align="center" prop="preparerBy" />
      <el-table-column label="审核人" align="center" prop="auditorBy" />
      <el-table-column label="状态" align="center" prop="auditStatus" >
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
      <el-table-column label="备注" align="center" prop="remark" />
<!--      制单日期-->
      <el-table-column label="制单日期" align="center" prop="voucherDate" width="130">
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
  </div>
</template>

<script setup name="Vouchers">
import { onMounted, inject, reactive, ref, toRefs } from "vue";
import {Refresh,Search,Download,Check,Upload,Edit,Delete} from '@element-plus/icons-vue'
import { listVouchers, approveVoucher,passVouchers,delVouchers } from "@/api/finance/vouchers.js"
import { listSubjects } from "@/api/finance/subjects.js"
import finStore from "@/hooks/store/useFinanceStore.js"
import { useRoute,useRouter } from 'vue-router'
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

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    voucherNo: null,
    voucherDate: null,
    summary: null,
    subjectName: null,
    preparerName: null,
    voucherDateStr: null,
    preparerBy: null,
    subjectId: null,
    tenantId: null,
  }
})

const { queryParams } = toRefs(data)

/** 查询记账凭证列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid=await finStore.getCurrentTenantId();
  listVouchers(queryParams.value).then(response => {
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
  listSubjects({tenantId: await finStore.getCurrentTenantId()}).then(response => {
    subjects.value = response.data;
  })
}

/** 导出按钮操作 */
async function handleExport() {
  queryParams.value.tenantId = await finStore.getCurrentTenantId();
  proxy.download('finance/entries/export', {
    ...queryParams.value
  }, `vouchersEntries_${new Date().getTime()}.xlsx`)
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



onMounted(() => {
    loadSubjects();
})

getList()
</script>
<style scoped>

</style>

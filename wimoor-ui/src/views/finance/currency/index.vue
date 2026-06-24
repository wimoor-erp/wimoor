<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="币种名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入币种名称"
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
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
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
          @click="handleSyncRate"
        >同步汇率</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="currencyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="币种" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="汇率" align="center" prop="rate" />
      <el-table-column label="是否本位币" align="center" prop="islocal" >
        <template #default="scope">
          <span v-if="scope.row.islocal">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="自动同步汇率" align="center" prop="isauto" >
        <template #default="scope">
          <span v-if="scope.row.isauto">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"  >修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"  >删除</el-button>
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

    <!-- 添加或修改currency对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="currencyRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="币种名称" prop="code">
          <el-select v-model="form.code" @change="changeCurrency">
            <el-option v-for="item in currencyData"  :label="item.symbol" :value="item.name" :key="item.name" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="币种编码" prop="name">
          <el-input disabled v-model="form.code" placeholder="请选择币种名称" />
        </el-form-item>
        <el-form-item label="币种汇率" prop="rate">
          <el-input v-model="form.rate" placeholder="请输入币种汇率" />
        </el-form-item>
        <el-form-item label="是否本位币" prop="islocal">
          <el-radio-group v-model="form.islocal">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="自动同步汇率" prop="isauto">
          <el-switch v-model="form.isauto" :active-value="1" :inactive-value="0" />
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

<script setup name="Currency">
import {listCurrency, getCurrency, delCurrency, saveCurrency} from "@/api/finance/currency"
import { ElLoading } from 'element-plus'
import finStore from "@/hooks/store/useFinanceStore.js"
import $ from 'jquery';
import exchangeRateAPI from "@/api/amazon/finances/exchangeRateAPI.js";
const { proxy } = getCurrentInstance()

const currencyList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {
    islocal: 0,
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    name: null,
    rate: null,
    islocal: null,
  },
  rules: {
  },
  byday:new Date(),
  currencyData:[],
})

const { queryParams, form, rules,byday,currencyData } = toRefs(data)

/** 查询currency列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId()
  listCurrency(queryParams.value).then(response => {
    currencyList.value = response.rows
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
    code: null,
    groupid: null,
    name: null,
    rate: null,
    islocal: 0,
    isauto: 0,
    modifyBy: null,
    createBy: null,
    createdTime: null,
    updatedTime: null
  }
  proxy.resetForm("currencyRef")
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
  ids.value = selection.map(item => item.code);
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加币别"
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const _code = row.code || ids.value[0];
  const _groupid = await finStore.getCurrentTenantId();
  getCurrency({"code": _code, "groupid": _groupid}).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改币别"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["currencyRef"].validate(async valid => {
    if (valid) {
      form.value.groupid=await finStore.getCurrentTenantId();
        saveCurrency(form.value).then(response => {
          proxy.$modal.msgSuccess("操作成功")
          open.value = false
          getList()
        })
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _codes = row.code || ids.value
  proxy.$modal.confirm('是否确认删除currency编号为"' + _codes + '"的数据项？').then(function() {
    return delCurrency(_codes)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('finance/currency/export', {
    ...queryParams.value
  }, `currency_${new Date().getTime()}.xlsx`)
}

/** 同步汇率 */
async function handleSyncRate() {
  const loadingInstance = ElLoading.service({ text: "正在同步汇率...", background: "rgba(0, 0, 0, 0.7)" })
  try {
    const res = await exchangeRateAPI.getMyCurrencyRate({"byday": byday.value.format("yyyy-MM-dd")})
    if (!res.data || res.data.length === 0) {
      proxy.$modal.msgWarning("未获取到汇率数据")
      return
    }
    const rateMap = {};
    res.data.forEach(item => {
      rateMap[item.name] = item;
    });
    let successCount = 0;
    for (const row of currencyList.value) {
      if (row.islocal || !row.isauto) continue;
      const match = rateMap[row.code];
      if (match) {
        const rate = (match.price / 100).toFixed(2);
        try {
          await saveCurrency({
            code: row.code,
            groupid: row.groupid,
            name: row.name,
            rate: rate,
            islocal: row.islocal
          });
          successCount++;
        } catch (e) {
          console.error("同步汇率失败:", row.code, e);
        }
      }
    }
    proxy.$modal.msgSuccess("同步完成，已更新 " + successCount + " 条汇率")
    getList()
  } catch (e) {
    console.error("获取汇率数据失败:", e);
  } finally {
    loadingInstance.close()
  }
}

function loadCurrencyData(){
  exchangeRateAPI.getMyCurrencyRate({"byday":byday.value.format("yyyy-MM-dd")}).then(res=>{
    if(res.data&&res.data.length>0){
      currencyData.value=res.data;
    }else{
      currencyData.value=[];
    }
  })
}
function changeCurrency(value){
  let selectedOption = currencyData.value.find(item => item.name === value);
  form.value.code=value;
  form.value.name=selectedOption.symbol;
  form.value.rate=(selectedOption.price/100).toFixed(2);
}
loadCurrencyData()
getList()
</script>

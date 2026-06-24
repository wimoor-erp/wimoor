<template>
  <div class="app-container">
      <el-tabs v-model="queryParams.subjectType"  @tab-change="handleQuery" >
        <el-tab-pane v-for="dict in fin_subject_type"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value" :name="dict.value" ></el-tab-pane>

      </el-tabs>

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
          type="info"
          plain
          icon="Download"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Upload"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Sort"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>

      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
          <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible"   :width="400" trigger="click">
            <template #reference>
              <el-button  :color="filterBtnColor" plain class='ic-btn'>
                  <filtericon></filtericon>
              </el-button>
            </template>

              <el-form-item label="科目编码" prop="subjectCode">
                <el-input
                    v-model="queryParams.subjectCode"
                    placeholder="请输入科目编码"
                    clearable
                    @keyup.enter="handleQuery"
                />
              </el-form-item>
              <el-form-item label="科目名称" prop="subjectName">
                <el-input
                    v-model="queryParams.subjectName"
                    placeholder="请输入科目名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
              </el-form-item>

              <el-form-item label="父级编码" prop="parentCode">
                <el-input
                    v-model="queryParams.parentCode"
                    placeholder="请输入父级科目编码"
                    clearable
                    @keyup.enter="handleQuery"
                />
              </el-form-item>

              <el-form-item label="余额方向" prop="direction">
                <el-select v-model="queryParams.direction" placeholder="请选择余额方向" clearable>
                  <el-option
                      v-for="dict in fin_direction"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuery()">搜索</el-button>
                <el-button @click="resetQuery()">取消</el-button>
              </el-form-item>


          </el-popover>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="subjectsList"
      row-key="subjectCode"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="科目名称" prop="subjectName" show-overflow-tooltip width="280" align="left">
         <template #default="{ row }">
            <span v-if="row.children||row.isLeaf">
              <span v-if="row.subjectLevel ==1" style="font-weight: bold;">{{ row.subjectName }}</span>
              <span v-else>{{ row.subjectName }}</span>
            </span>
            <span v-else :style="{ 'padding-left': (row.subjectLevel - 1) * 40 + 'px' }">
              <span v-if="row.subjectLevel ==1" style="font-weight: bold;">{{ row.subjectName }}</span>
              <span v-else>{{ row.subjectName }}</span>
            </span>
          </template>
        </el-table-column>
      <el-table-column  label="科目编码" align="left" prop="subjectCode" >
        <template #default="{ row }">
            <span v-if="row.subjectLevel ==1" style="font-weight: bold;">{{ row.subjectCode }}</span>
            <span v-else>{{ row.subjectCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="科目级别" align="center" prop="subjectLevel" width="120">
        <template #default="scope">
          <dict-tag :options="fin_subject_level" :value="scope.row.subjectLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="父级编码" align="center" prop="parentCode" />
      <el-table-column label="科目类别" align="center" prop="subjectTypeName">
        <template #default="scope">
          <el-tag type="success">{{ scope.row.subjectTypeName }}</el-tag>
<!--          <dict-tag :options="fin_subject_type" :value="scope.row.subjectTypeName"/>-->
        </template>
      </el-table-column>
      <el-table-column label="余额方向" align="center" prop="direction" width="100">
        <template #default="scope">
          <dict-tag :options="fin_direction"     :value="scope.row.direction"/>
        </template>
      </el-table-column>
      <el-table-column label="末级科目" align="center" prop="isLeaf" width="100">
        <template #default="scope">
          <dict-tag :options="fin_is_leaf" :value="scope.row.isLeaf"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <dict-tag :options="subject_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"  >修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"  >新增</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"  >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改会计科目对话框 -->
    <el-dialog :title="title" v-model="open" top="3vh" width="600px" append-to-body>
      <el-form ref="subjectsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input v-model="form.subjectCode" placeholder="请输入科目编码（4-2-2-2编码规则：一级4位，二级6位，三级8位，四级10位）" @input="formatSubjectCode" @change="autoSetParentCode" />
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">
            编码规则：4-2-2-2（例如：一级1001，二级1001 01，三级1001 01 01，四级1001 01 01 01）
          </div>
        </el-form-item>
        <el-form-item label="科目名称" prop="subjectName">
          <el-input v-model="form.subjectName" placeholder="请输入科目名称" />
        </el-form-item>
        <el-form-item label="科目级别" prop="subjectLevel">
          <el-select v-model="form.subjectLevel" placeholder="请选择科目级别">
            <el-option
              v-for="dict in fin_subject_level"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
       
        <el-form-item label="父级编码" prop="parentCode">
          <el-tree-select
            v-model="form.parentCode"
            :data="subjectsOptions"
            :props="{ value: 'subjectCode', label: 'subjectName', children: 'children' }"
            value-key="subjectCode"
            placeholder="请选择父级科目编码"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="科目类型" prop="subjectType">
          <el-select v-model="form.subjectType" disabled placeholder="请选择科目类型">
            <el-option
              v-for="dict in fin_subject_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
         <el-form-item label="科目类别" prop="subjectTypeId">
          <el-select v-model="form.subjectTypeId" placeholder="请选择科目类别">
            <el-option
              v-for="category in subjectCategories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向" prop="direction">
          <el-radio-group v-model="form.direction">
            <el-radio
              v-for="dict in fin_direction"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
          <el-checkbox style="margin-left: 15px" v-model="form.isCash" label="1">是否为现金科目</el-checkbox>
        </el-form-item>
        <el-form-item label="末级科目" prop="isLeaf">
          <el-radio-group v-model="form.isLeaf">
            <el-radio
              v-for="dict in fin_is_leaf"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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
        <el-form-item label="存货核算" prop="isInventoryDetailEnabled">
          <el-switch v-model="form.isInventoryDetailEnabled" :active-value="true" :inactive-value="false"  ></el-switch>
        </el-form-item>
        <el-form-item label="辅助核算" prop="isAuxiliary">
            <el-switch v-model="form.isAuxiliary" :active-value="true" :inactive-value="false"  ></el-switch>
        </el-form-item>
        <el-form-item v-if="form.isAuxiliary" label="核算项目" prop="auxiliaryTypes">
          <el-checkbox-group v-model="form.auxiliaryTypes" >
            <el-checkbox v-for="item in auxiliaryTypeList" :value="item.typeCode" :key="item.typeCode">{{item.typeName}}</el-checkbox>
          </el-checkbox-group>
            自定义类别:<el-button link type="primary" @click="handleCustomAuxiliaryAccount">设置自定义类别</el-button>
        </el-form-item>
        <el-form-item label="数量核算" prop="isQuantity">
          <el-switch v-model="form.isQuantity" :active-value="true" :inactive-value="false"  ></el-switch>
        </el-form-item>
        <el-form-item v-if="form.isQuantity" label="计算单位" prop="unitOfMeasure">
             <el-input style="width: 150px" v-model="form.unitOfMeasure" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="外币核算" prop="isForeignCurrency" style="margin-bottom: 0px;">
          <el-switch v-model="form.isForeignCurrency" :active-value="true" :inactive-value="false"  ></el-switch>
        </el-form-item>
        <el-form-item v-if="form.isForeignCurrency" style="margin-bottom: 0px;border-bottom: 1px dashed #e4e7ed;"  label="" prop="isExchange">
          <div style="display: block" > <el-checkbox v-model="form.isExchange" label="">期末调汇</el-checkbox></div>
        </el-form-item>
        <el-form-item v-if="form.isForeignCurrency"   label="" prop="foreignCurrency">
          <el-checkbox-group v-model="form.foreignCurrencys" style="display: flex; flex-wrap: wrap; gap: 0 16px;">
            <el-checkbox v-for="item in currencyList" :value="item.code" :key="item.code">{{item.name}}</el-checkbox>
          </el-checkbox-group>
           <el-button icon="CirclePlus"  type="primary" link size="small" @click="handleAddCurrency">新增币别</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 导入文件弹框 -->
    <el-dialog
      v-model="uploadVisible"
      :title="uploadTitle"
      width="400px"
    >
      <el-upload
        :drag="true"
        action
        ref="uploadRef"
        :http-request="uploadFiles"
        :limit="1"
        :on-exceed="handleExceed"
        :before-upload="beforeUpload"
        :show-file-list="true"
        :headers="headers"
        accept=".xls,.xlsx"
      >
        <el-icon class="font-large"><upload-filled /></el-icon>
        <div class="el-upload__text">
          拖拽文件到此处或 <em>点击上传</em>
        </div>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <div class="flex-center-between">
            <el-button type="success" @click.stop="downloadTemp" plain>下载模板</el-button>
            <div>
              <el-button @click="uploadVisible = false">取消</el-button>
              <el-button type="primary" v-loading="uploadloading" @click.stop="uploadExcel">
                上传文件
              </el-button>
            </div>
          </div>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Subjects">
import { listSubjects, getSubjects, delSubjects, addSubjects, updateSubjects, uploadSubjectsFile, exportSubjects, listSubjectTypesByCategoryType } from "@/api/finance/subjects"
import { listCurrency } from "@/api/finance/currency.js"
import { listTypes } from "@/api/finance/auxiliar_types.js"
import finStore from "@/hooks/store/useFinanceStore.js"
import { useRoute,useRouter } from 'vue-router'
import { ElMessage, genFileId } from 'element-plus'
import { watch } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import filtericon from "@/components/icon/filtericon.vue";

const { proxy } = getCurrentInstance()
const { subject_status, fin_is_leaf, fin_subject_level, fin_subject_type, fin_direction } = proxy.useDict('subject_status', 'fin_is_leaf', 'fin_subject_level', 'fin_subject_type', 'fin_direction')

let router = useRouter();
const subjectsList = ref([])
const subjectsOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const title = ref("")
const isExpandAll = ref(true)
const refreshTable = ref(true)
const queryRef=ref()
const uploadVisible = ref(false)
const uploadloading = ref(false)
const uploadTitle = ref("导入科目")
const uploadRef = ref()
const myfile = ref(null)
const headers = ref({})
const subjectCategories = ref([])
const data = reactive({
  form: {
    isAuxiliary: false,
    auxiliaryTypes: [],
    isQuantity: false,
    isForeignCurrency: false,
    endOfPeriodAdjustment: false,
    foreignCurrency: [],
    unitOfMeasure: "套",
    isExchange: false,
    isInventoryDetailEnabled:false,
  },
  currencyList:[],
  auxiliaryTypeList:[],
  queryParams: {
    subjectCode: null,
    subjectName: null,
    subjectLevel: null,
    parentCode: null,
    subjectType: "1",
    direction: null,
    isLeaf: null,
    status: "1",
    createdTime: null,
    updatedTime: null
  },
  rules: {
    subjectName: [
      { required: true, message: "科目名称不能为空", trigger: "blur" }
    ],
    subjectLevel: [
      { required: true, message: "科目级别不能为空", trigger: "change" }
    ],
    subjectType: [
      { required: true, message: "科目类型不能为空", trigger: "change" }
    ],
    direction: [
      { required: true, message: "余额方向不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules,currencyList,auxiliaryTypeList } = toRefs(data)

/** 查询会计科目列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  listSubjects(queryParams.value).then(response => {
    subjectsList.value = response.data;
    loading.value = false
  })
}

function handleAddCurrency(){
  open.value = false;
   router.push({
    path:'/fin/currency',
    query:{
      title:"财务币别",
      path:'/fin/currency',
    }
  })
}

/** 查询会计科目下拉树结构 */
async function getTreeselect(subjectType) {
  listSubjects({"groupid": await finStore.getCurrentTenantId(),"subjectType":subjectType}).then(response => {
    subjectsOptions.value = []
    // 递归处理所有节点，添加科目编码前缀
    function processNodes(nodes) {
      nodes.forEach(item => {
        item.subjectName = item.subjectCode + " " + item.subjectName;
        if (item.children && item.children.length > 0) {
          processNodes(item.children);
        }
      });
    }
    processNodes(response.data);
    const data = {subjectCode: 0, subjectName: '顶级节点', children: response.data}
    subjectsOptions.value.push(data)
  })
}

/** 根据科目类型获取科目类别 */
async function fetchSubjectCategories(subjectType) {
  // 映射subjectType值到categoryType
  const categoryTypeMap = {
    1: 'asset',
    2: 'liability',
    3: 'equity',
    4: 'cost',
    5: 'profit_loss'
  };
  
  const categoryType = categoryTypeMap[subjectType];
  if (categoryType) {
    listSubjectTypesByCategoryType(categoryType).then(response => {
      subjectCategories.value = response.data;
      // 默认选中第一个值
      if (subjectCategories.value.length > 0) {
        form.value.subjectTypeId = subjectCategories.value[0].id;
      } else {
        form.value.subjectTypeId = null;
      }
    });
  } else {
    subjectCategories.value = [];
    form.value.subjectTypeId = null;
  }
}
	
// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    subjectId: null,
    subjectCode: null,
    tenantId: null,
    subjectName: null,
    subjectLevel: null,
    parentCode: null,
    subjectType: null,
    direction: null,
    isLeaf: null,
    status: null,
    createdTime: null,
    updatedTime: null,
    isQuantity: false,
    isForeignCurrency: false,
    unitOfMeasure: "套",
    isExchange: false,
    isAuxiliary: false,
    isInventoryDetailEnabled:false,
  }
  proxy.resetForm("subjectsRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset()
  const subjectType = parseInt(data.queryParams.subjectType);
  getTreeselect(subjectType)
  fetchSubjectCategories(subjectType)
  if (row != null && row.subjectCode) {
    form.value.parentCode = row.subjectCode
    form.value.subjectLevel=row.subjectLevel+1;
  } else {
    form.value.parentCode = 0
  }
  open.value = true;
  form.value.subjectType = subjectType;
  title.value = "添加会计科目"
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true
  })
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const subjectType = parseInt(data.queryParams.subjectType);
  await getTreeselect(subjectType)
  fetchSubjectCategories(subjectType)
  if (row != null) {
    form.value.parentCode = row.parentCode
  }
  getSubjects(row.subjectId).then(response => {
    form.value = response.data
    // 将后台返回的外币字符串转换为数组，如 "USD/CAD" -> ["USD","CAD"]
    if (form.value.foreignCurrencys && typeof form.value.foreignCurrencys === 'string') {
      form.value.foreignCurrencys = form.value.foreignCurrencys.split('/');
    }
    if (!Array.isArray(form.value.auxiliaryTypes)) {
      form.value.auxiliaryTypes = [];
    }
    if (!Array.isArray(form.value.foreignCurrencys)) {
      form.value.foreignCurrencys = [];
    }
    open.value = true;
    form.value.subjectType = subjectType;
    title.value = "修改会计科目"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["subjectsRef"].validate(async valid => {
    if (valid) {
      // 移除科目编码中的所有空格，确保提交到后端的是纯数字编码
      if (form.value.subjectCode) {
        form.value.subjectCode = form.value.subjectCode.replace(/\s/g, '');
      }
      form.value.groupid = await finStore.getCurrentTenantId();
      // 设置是否开启辅助核算
      if (form.value.auxiliaryTypes && form.value.auxiliaryTypes.length > 0) {
        form.value.isAuxiliary = true;
      } else {
        form.value.isAuxiliary = false;
        form.value.auxiliaryTypes = [];
      }
      // 将外币数组转换为字符串格式，如 ["USD","CAD"] -> "USD/CAD"
      if (form.value.foreignCurrencys && Array.isArray(form.value.foreignCurrencys)) {
        form.value.foreignCurrencys = form.value.foreignCurrencys.join('/');
      }
      if (form.value.subjectId != null) {
        updateSubjects(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addSubjects(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除会计科目编号为"' + row.subjectId + '"的数据项？').then(function() {
    return delSubjects(row.subjectId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
getAuxiliaryTypes()
loadCurrencyList()

/** 获取币种列表 */
async function loadCurrencyList() {
  let groupid = await finStore.getCurrentTenantId();
  listCurrency({ groupid: groupid }).then(response => {
    currencyList.value = response.rows || [];
  })
}

/** 获取辅助核算类型列表 */
async function getAuxiliaryTypes() {
  let groupid = await finStore.getCurrentTenantId();
  let params={
    "groupid":groupid,
    "status": 1,
  };
  listTypes(params).then(response => {
    auxiliaryTypeList.value = response.rows;
  })
}

// 监听科目类型变化，自动获取科目类别
watch(() => form.value.subjectType, (newVal) => {
  if (newVal) {
    fetchSubjectCategories(newVal);
  }
});

// 格式化科目编码，显示为xxxx xx xx的格式
function formatSubjectCode() {
  let subjectCode = form.value.subjectCode;
  if (subjectCode) {
    // 移除所有空格和非数字字符
    subjectCode = subjectCode.replace(/\D/g, '');
    // 限制长度为10位（四级科目）
    subjectCode = subjectCode.substring(0, 10);
    
    // 根据4-2-2-2规则添加空格
    let formattedCode = '';
    if (subjectCode.length > 4) {
      formattedCode = subjectCode.substring(0, 4) + ' ';
      if (subjectCode.length > 6) {
        formattedCode += subjectCode.substring(4, 6) + ' ';
        if (subjectCode.length > 8) {
          formattedCode += subjectCode.substring(6, 8) + ' ';
          if (subjectCode.length > 10) {
            formattedCode += subjectCode.substring(8, 10);
          } else {
            formattedCode += subjectCode.substring(8);
          }
        } else {
          formattedCode += subjectCode.substring(6);
        }
      } else {
        formattedCode += subjectCode.substring(4);
      }
    } else {
      formattedCode = subjectCode;
    }
    
    form.value.subjectCode = formattedCode;
  }
}

// 根据科目编码自动设置父级科目编码
function autoSetParentCode() {
  let subjectCode = form.value.subjectCode;
  if (subjectCode && subjectCode.length > 0) {
    // 移除所有空格，只保留数字
    subjectCode = subjectCode.replace(/\s/g, '');
    
    // 科目编码规则：4-2-2-2
    // 一级科目：4位（如1001）
    // 二级科目：6位（如100101）
    // 三级科目：8位（如10010101）
    // 四级科目：10位（如1001010101）
    
    // 计算科目级别
    let subjectLevel = 1;
    if (subjectCode.length > 4) {
      subjectLevel = Math.floor((subjectCode.length - 4) / 2) + 1;
    }
    form.value.subjectLevel = subjectLevel;
    
    // 计算父级科目编码
    if (subjectLevel > 1) {
      // 父级科目编码长度 = 当前科目编码长度 - 2
      const parentCodeLength = subjectCode.length - 2;
      form.value.parentCode = subjectCode.substring(0, parentCodeLength);
    } else {
      // 一级科目没有父级
      form.value.parentCode = 0;
    }
  }
}

/** 导入按钮操作 */
function handleImport() {
  uploadVisible.value = true;
  uploadloading.value = false;
  uploadTitle.value = "导入科目";
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('/api/finance/subjects/export', {
    groupid: queryParams.value.groupid
  }, `subjects_${new Date().getTime()}.xlsx`);
}

/** 下载模板 */
function downloadTemp() {
  proxy.download('/api/finance/subjects/downloadTemplate', {}, `subject_template.xlsx`);
}

/** 文件上传之前 */
function beforeUpload(file) {
  if (file.type != "" || file.type != null || file.type != undefined){
    // 截取文件的后缀，判断文件类型
    const FileExt = file.name.replace(/.+\./, "").toLowerCase();
    // 计算文件的大小
    const isLt5M = file.size / 1024  < 50000; // 这里做文件大小限制
    // 如果大于50M
    if (!isLt5M) {
      ElMessage.error('上传文件大小不能超过 50MB!!');
      return false;
    }
    else {
      return true;
    }
  }
}

/** 处理文件上传 */
function uploadFiles(item) {
  // 上传文件的需要formdata类型;所以要转
  myfile.value = item.file;
}

/** 处理文件超出限制 */
function handleExceed(files) {
  uploadRef.value.clearFiles();
  const file = files[0];
  file.uid = genFileId();
  uploadRef.value.handleStart(file);
}

/** 上传Excel文件 */
function uploadExcel() {
  let FormDatas = new FormData();
  uploadloading.value = true;
  FormDatas.append('file', myfile.value);
  uploadSubjectsFile(FormDatas, queryParams.value.groupid).then(function(res) {
    ElMessage.success('上传成功');
    uploadloading.value = false;
    uploadVisible.value = false;
    handleQuery();
  }).catch(() => {
    uploadloading.value = false;
  });
}
</script>

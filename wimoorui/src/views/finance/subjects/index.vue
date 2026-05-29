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
                <i>
                  <svg width="16" height="16" fill="none" viewBox="0 0 48 48"  xmlns="http://www.w3.org/2000/svg">
                    <path d="M6 9L20.4 25.8178V38.4444L27.6 42V25.8178L42 9H6Z" fill="none" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>
                </i>
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
      <el-table-column label="科目编码" prop="subjectCode" width="120" />
      <el-table-column label="科目名称" align="center" prop="subjectName" />
      <el-table-column label="科目级别" align="center" prop="subjectLevel" width="120">
        <template #default="scope">
          <dict-tag :options="fin_subject_level" :value="scope.row.subjectLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="父级编码" align="center" prop="parentCode" />
      <el-table-column label="科目类型" align="center" prop="subjectType">
        <template #default="scope">
          <dict-tag :options="fin_subject_type" :value="scope.row.subjectType"/>
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
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="subjectsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input v-model="form.subjectCode" placeholder="请输入科目编码" />
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
          <el-select v-model="form.subjectType" placeholder="请选择科目类型">
            <el-option
              v-for="dict in fin_subject_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
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

<script setup name="Subjects">
import { listSubjects, getSubjects, delSubjects, addSubjects, updateSubjects } from "@/api/finance/subjects"
import finStore from "@/hooks/store/useFinanceStore.js"
 
const { proxy } = getCurrentInstance()
const { subject_status, fin_is_leaf, fin_subject_level, fin_subject_type, fin_direction } = proxy.useDict('subject_status', 'fin_is_leaf', 'fin_subject_level', 'fin_subject_type', 'fin_direction')

const subjectsList = ref([])
const subjectsOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const title = ref("")
const isExpandAll = ref(true)
const refreshTable = ref(true)
const queryRef=ref();
const data = reactive({
  form: {},
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

const { queryParams, form, rules } = toRefs(data)

/** 查询会计科目列表 */
async function getList() {
  loading.value = true
  queryParams.value.groupid = await finStore.getCurrentTenantId();
  listSubjects(queryParams.value).then(response => {
    subjectsList.value = proxy.handleTree(response.data, "subjectCode", "parentCode")
    loading.value = false
  })
}

/** 查询会计科目下拉树结构 */
function getTreeselect() {
  listSubjects({tenantId:finStore.getCurrentTenantId()}).then(response => {
    subjectsOptions.value = []
    const data = { subjectCode: 0, subjectName: '顶级节点', children: [] }
    data.children = proxy.handleTree(response.data, "subjectCode", "parentCode")
    subjectsOptions.value.push(data)
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
    updatedTime: null
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
  getTreeselect()
  if (row != null && row.subjectCode) {
    form.value.parentCode = row.subjectCode
  } else {
    form.value.parentCode = 0
  }
  open.value = true
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
  await getTreeselect()
  if (row != null) {
    form.value.parentCode = row.parentCode
  }
  getSubjects(row.subjectId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会计科目"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["subjectsRef"].validate(async valid => {
    if (valid) {
      form.value.groupid = await finStore.getCurrentTenantId();
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
</script>

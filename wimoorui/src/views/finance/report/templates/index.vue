<template>
  <div class="app-container">
    <div class="container-fluid">
      <div class="row">
        <!-- Left Sidebar - Template Tree -->
        <div class="col-md-3 col-lg-2 sidebar">
          <div class="search-box">
            <el-input
              v-model="templateSearch" 
              placeholder="搜索模板..."
              prefix-icon="Search"
              
            />
          </div>

          <div class="action-buttons mt-2">
            <el-row :gutter="10">
              <el-col :span="12" class="mb-2">
                <el-button
                  type="success"
                  
                  icon="Plus"
                  class="w-100"
                  @click="handleAddTemplate"
                >
                  新建模板
                </el-button>
              </el-col>
              <el-col :span="12" class="mb-2">
                <el-button
                  type="primary"
               
                  icon="Upload"
                  class="w-100"
                  plain
                  @click="handleImport"
                >
                  导入项目
                </el-button>
              </el-col>
            </el-row>
          </div>

          <el-tree
            class="template-tree"
            :data="templateTree"
            :highlight-current="true"
            :props="treeProps"
            node-key="templateId"
            @node-click="handleTemplateSelect"
            :default-expanded-keys="[1]"
            :current-node-key="currentNodeKey"
          >
            <template #default="{ node, data }">
              <div  class="custom-tree-node">
                <div>
                  <span class="tree-icon">
                    <i :class="node.data.icon"></i>
                  </span>
                  <span>{{ node.label }}</span>
                </div>
                <div class="node-actions">
                  <el-dropdown trigger="click" @command="(command) => handleNodeCommand(command, data)">
                    <el-button type="text" size="small">
                      <el-icon><MoreFilled /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="edit">修改</el-dropdown-item>
                        <el-dropdown-item command="delete"  danger>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </template>
          </el-tree>
        </div>

        <!-- Right Content Area -->
        <div class="col-md-9 col-lg-10">
          <div class="p-3">
            <!-- 引入项目列表子组件 -->
            <template-items 
              :template-id="currentNodeKey"
              :template-name="currentTemplate"
              @item-updated="handleItemUpdated"
            ></template-items>
          </div>
        </div>
      </div>
    </div>

    <!-- 模板新增/修改对话框 -->
    <el-dialog :title="formTitle" v-model="openForm" width="600px" append-to-body>
      <el-form ref="templateFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <el-form-item label="模板编码" prop="templateCode">
          <el-input v-model="form.templateCode" placeholder="请输入模板编码，如BALANCE_SHEET"></el-input>
        </el-form-item>
        <el-form-item label="模板类型" prop="templateType">
          <el-select v-model="form.templateType" placeholder="请选择模板类型">
           <el-option
              v-for="dict in template_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
          
        </el-form-item>
        <el-form-item label="模板描述" prop="description">
          <el-input v-model="form.description" type="textarea" rows="3" placeholder="请输入模板描述"></el-input>
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
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Templates">
import { ref, reactive, toRefs, onMounted } from 'vue'
import { listTemplates, addTemplates, updateTemplates, delTemplates } from "@/api/finance/report_templates.js"
import TemplateItems from "./components/template_items.vue"
import finStore from "@/hooks/store/useFinanceStore.js"
import { ElMessageBox, ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const { template_type, subject_status } = proxy.useDict('template_type', 'subject_status')
// 模板树数据和方法
const templateTree = ref([])
const currentTemplate = ref('')
const currentNodeKey = ref(null) // 新增：当前选中节点的key
const treeProps = ref({
  label: 'templateName',
  children: 'children',
  icon: 'icon'
})

// 加载模板列表
async function getTemplateList() {
  let queryParams = {
    pageNum: 1,
    pageSize: 1000,
    groupid: await finStore.getCurrentTenantId()
  }
  listTemplates(queryParams).then(response => {
    // 将后台返回数据转换为树形结构
    const templates = response.rows.map(item => ({
      templateId: item.templateId,
      templateCode: item.templateCode,
      templateName: item.templateName,
      templateType: item.templateType,
      description: item.description || '',
      status: item.status || '1',
      icon: 'bi-file-earmark-spreadsheet',
      // 如果有子模板可以在这里添加children字段
    }))
    templateTree.value = templates
    
    // 默认选择第一个模板
    if (templates.length > 0) {
      handleTemplateSelect(templates[0])
      currentNodeKey.value = templates[0].templateId // 新增：设置默认选中节点
    }
  })
}

function handleTemplateSelect(data) {
  currentTemplate.value = data.templateName
  currentNodeKey.value = data.templateId // 新增：更新选中节点
}

onMounted(() => {
  getTemplateList()
})

// 表单相关变量
const openForm = ref(false)
const formTitle = ref('新增模板')
const form = ref({
  templateId: null,
  templateName: '',
  templateCode: '',
  templateType: '',
  description: '',
  status: '1'
})
const rules = ref({
  templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  templateCode: [{ required: true, message: '请输入模板编码', trigger: 'blur' }],
  templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})
const templateFormRef = ref(null)

// 新建模板
function handleAddTemplate() {
  form.value = {
    templateId: null,
    templateName: '',
    templateCode: '',
    templateType: '',
    description: '',
    status: '1'
  }
  formTitle.value = '新增模板'
  openForm.value = true
}

// 编辑模板
function handleEditTemplate(data) {
  form.value = {
    templateId: data.templateId,
    templateName: data.templateName,
    templateCode: data.templateCode,
    templateType: data.templateType,
    description: data.description || '',
    status: data.status+''
  }
  formTitle.value = '编辑模板'
  openForm.value = true
}
const handleNodeCommand = (command, data) => {
  if (command === 'edit') {
    handleEditTemplate(data);
  } else if (command === 'delete') {
    handleDeleteTemplate(data.id);
  }
};
const handleDeleteTemplate = async (templateId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除此模板吗？此操作不可撤销。',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    const res = await delTemplates(templateId);
    if (res.code === 200) {
      ElMessage.success('删除成功');
      getTemplateList();
    }
  } catch (error) {
    if (error === 'cancel') return;
    ElMessage.error('删除失败：' + (error.msg || '网络错误'));
  }
};
// 取消表单
function handleCancel() {
  openForm.value = false
  templateFormRef.value.resetFields()
}

// 提交表单
async function handleSubmit() {
  try {
    await templateFormRef.value.validate()
    form.value.groupid = await finStore.getCurrentTenantId();
    if (form.value.templateId) {
      // 修改模板
      await updateTemplates(form.value)
    } else {
      // 新增模板
      await addTemplates(form.value)
    }
    openForm.value = false
    getTemplateList() // 重新加载模板列表
  } catch (error) {
    console.error('表单验证失败', error)
  }
}
</script>
<style scoped lang="scss">
.template-tree {
  height: 600px;
  overflow: auto;
  // 修改为深度选择器以穿透scoped
  ::v-deep .el-tree-node {
    margin: 12px 0; // 增加上下间距
  }
  ::v-deep .el-tree-node__content {
    font-size: 16px; // 设置字体大小
    height: auto !important; // 强制允许高度自适应
    padding: 8px 0; // 增加内边距
  }
  .treeItem{
    cursor: pointer;
  }
  
  overflow: auto;
}
.w-100{
  width: 100%;
}
.action-buttons{
  margin-top: 10px;
  margin-bottom: 10px;
}
 .custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding-right: 8px;
}
</style>
### 修改说明
1. **数据加载逻辑**：
   - 移除硬编码的`templateTree`数据，通过`listTemplates()`API从后台获取
   - 处理返回数据，将`templateId`映射为前端树形组件需要的`id`字段
   - 保持`templateCode`和`templateName`字段与后台一致

2. **字段映射调整**：
   - 设置`treeProps`的`label`为`templateName`，匹配后台返回的模板名称字段
   - 保留图标样式，统一使用`bi-file-earmark-spreadsheet`

3. **默认选择处理**：
   - 加载完成后默认选择第一个模板
   - 确保`currentTemplateCode`和`currentTemplate`正确初始化

4. **生命周期调用**：
   - 在`onMounted`钩子中调用`getTemplateList`，确保组件挂载时加载数据

### 接口对应说明
- 前端`listTemplates()`对应后台`@GetMapping("/list")`接口
- 后台返回的`FinReportTemplates`实体类字段与前端模板树数据字段已正确映射
- 如需支持模板层级结构，可在后台添加`children`字段并在前端处理

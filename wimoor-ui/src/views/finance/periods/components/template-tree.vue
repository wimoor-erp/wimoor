
<template>
  <div class="template-tree">
    <el-card shadow="never">
    <div class="template-group">
      <div class="group-title">启用模板</div>
      <el-tree
        ref="enabledTreeRef"
        :data="enabledTemplates"
        :props="defaultProps"
        node-key="id"
        default-expand-all
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        @node-click="handleNodeClick"
        @node-drop="handleNodeDrop"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ data.name }}</span>
            <span class="node-actions">
              <el-button size="small" link type="danger" icon="Delete" @click.stop="handleDelete(data, 'enabled')"></el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </div>
    <div class="template-group">
      <div class="group-title">禁用模板</div>
      <el-tree
        ref="disabledTreeRef"
        :data="disabledTemplates"
        :props="defaultProps"
        node-key="id"
        default-expand-all
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        @node-click="handleNodeClick"
        @node-drop="handleNodeDrop"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ data.name }}</span>
            <span class="node-actions">
              <el-button size="small" link type="danger" icon="Delete" @click.stop="handleDelete(data, 'disabled')"></el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </div>
    <div class="add-template">
      <el-button type="primary" size="small" icon="Plus" @click="openAddDialog">
        新增模板
      </el-button>
    </div>
    
    <!-- 新增模板弹出框 -->
    <el-dialog :title="'新增模板'" v-model="dialogVisible" width="400px">
      <el-form ref="templateFormRef" :model="templateForm" :rules="rules" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="templateForm.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型" prop="type">
          <el-select v-model="templateForm.type" placeholder="请选择模板类型">
            <el-option label="亚马逊月度模板" value="amzpayment" />
            <el-option label="结转损益" value="loss" />
            <el-option label="飞书表格" value="feishu" />
            <el-option label="期末调汇" value="fct" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddTemplate">确认</el-button>
        </span>
      </template>
    </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import { listFinClosingTemplate, delFinClosingTemplate, addFinClosingTemplate, updateFinClosingTemplate } from '@/api/finance/closing_template.js';
import finStore from "@/hooks/store/useFinanceStore.js";

const { proxy } = getCurrentInstance();

const enabledTemplates = ref([]);
const disabledTemplates = ref([]);
const enabledTreeRef = ref(null);
const disabledTreeRef = ref(null);
const defaultProps = {
  children: 'children',
  label: 'name'
};

// 新增模板相关
const dialogVisible = ref(false);
const templateFormRef = ref(null);
const templateForm = ref({
  name: '',
  type: ''
});
const rules = {
  name: [
    { required: true, message: '请输入模板名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ]
};

const emit = defineEmits(['change']);

async function loadList(template) {
  let formData={pageSize:2000,pageNum:1,orderByColumn:'findex'};
  formData.groupid= await finStore.getCurrentTenantId();
  try {
  const response = await listFinClosingTemplate(formData);
  if (response && response.code === 200 && response.rows) {
    // 根据disabled字段将模板分为启用和禁用两组
    enabledTemplates.value = response.rows.filter(item => item.disabled === 0);
    disabledTemplates.value = response.rows.filter(item => item.disabled === 1);
    if(!template&&enabledTemplates.value.length>0){
      template=enabledTemplates.value[0];
    }
    // 如果传入了template，选中对应记录
    if (template && template.id) {
      // 使用nextTick确保DOM已更新
      setTimeout(() => {
        // 查找模板是否在启用列表中
        const enabledTemplate = enabledTemplates.value.find(item => item.id === template.id);
        if (enabledTemplate && enabledTreeRef.value) {
          enabledTreeRef.value.setCurrentKey(template.id);
           handleNodeClick(template)
        } else {
            // 查找模板是否在禁用列表中
            const disabledTemplate = disabledTemplates.value.find(item => item.id === template.id);
            if (disabledTemplate && disabledTreeRef.value) {
              disabledTreeRef.value.setCurrentKey(template.id);
              handleNodeClick(template)
            }
          }
        }, 100);
      }
    } else {
      console.error('Invalid response structure:', response);
      enabledTemplates.value = [];
      disabledTemplates.value = [];
    }
  } catch (error) {
    console.error('Failed to load template list:', error);
    enabledTemplates.value = [];
    disabledTemplates.value = [];
  }
}

function show(template) {
  loadList(template);
}

// 处理树节点点击
function handleNodeClick(data) {
  emit('node-click', data);
}

// 打开新增模板弹出框
function openAddDialog() {
  // 重置表单
  templateForm.value = {
    name: '',
    type: ''
  };
  if (templateFormRef.value) {
    templateFormRef.value.resetFields();
  }
  dialogVisible.value = true;
}

// 提交新增模板
function submitAddTemplate() {
  templateFormRef.value.validate(async valid => {
    if (valid) {
      try {
        // 获取当前租户ID
        const groupid = await finStore.getCurrentTenantId();
        
        // 准备新增模板的数据
        const templateData = {
          name: templateForm.value.name,
          ftype: templateForm.value.type, // 使用ftype字段匹配数据库
          disabled: 0, // 默认为启用状态
          groupid: groupid
        };
        
        // 调用后台新增接口
        const response = await addFinClosingTemplate(templateData);
        
        if (response && response.code === 200) {
          // 新增成功，重新加载模板列表
          await loadList();
          
          // 发送change事件通知父组件刷新
          emit('change');
          
          // 关闭弹出框
          dialogVisible.value = false;
          
          // 提示成功
          proxy.$modal.msgSuccess('新增模板成功');
        } else {
          // 新增失败
          proxy.$modal.msgError('新增模板失败');
        }
      } catch (error) {
        console.error('Failed to add template:', error);
        proxy.$modal.msgError('新增模板失败');
      }
    }
  });
}

// 允许拖拽
function allowDrag(node) {
  return true;
}

// 允许放入（仅允许同级排序，不允许嵌套）
function allowDrop(draggingNode, dropNode, type) {
  // 不允许拖入子节点（只允许上下排序）
  return type !== 'inner';
}

// 处理拖拽排序
async function handleNodeDrop(draggingNode, dropNode, dropType, event) {
  // 确定是哪个树的拖拽
  const draggingData = draggingNode.data;
  let targetList;
  
  if (enabledTemplates.value.some(item => item.id === draggingData.id)) {
    targetList = enabledTemplates;
  } else {
    targetList = disabledTemplates;
  }

  // 从原位置移除拖拽节点
  const dragIndex = targetList.value.findIndex(item => item.id === draggingData.id);
  if (dragIndex === -1) return;
  const [draggedItem] = targetList.value.splice(dragIndex, 1);

  // 找到目标位置并插入
  const dropIndex = targetList.value.findIndex(item => item.id === dropNode.data.id);
  if (dropIndex === -1) return;

  if (dropType === 'before') {
    targetList.value.splice(dropIndex, 0, draggedItem);
  } else if (dropType === 'after') {
    targetList.value.splice(dropIndex + 1, 0, draggedItem);
  }

  // 更新所有项的 findex
  targetList.value.forEach((item, index) => {
    item.findex = index + 1;
  });

  // 调用后台接口更新排序
  try {
    await updateFinClosingTemplate({ id: draggingData.id, findex: draggingData.findex });
    proxy.$modal.msgSuccess('排序已更新');
  } catch (error) {
    console.error('Failed to update order:', error);
    // 排序失败时重新加载列表
    loadList();
  }
}

// 处理删除模板
function handleDelete(data, listType) {
  proxy.$modal.confirm('是否确认删除模板"' + data.name + '"？').then(function() {
    if (data.id) {
      // 有ID的模板，调用后台删除接口
      delFinClosingTemplate(data.id).then(response => {
        if (response && response.code === 200) {
          // 删除成功后从列表中移除
          if (listType === 'enabled') {
            enabledTemplates.value = enabledTemplates.value.filter(item => item.id !== data.id);
          } else {
            disabledTemplates.value = disabledTemplates.value.filter(item => item.id !== data.id);
          }
          
          // 删除成功后自动选择第一个模板
          let firstTemplate = null;
          if (enabledTemplates.value.length > 0) {
            firstTemplate = enabledTemplates.value[0];
          } else if (disabledTemplates.value.length > 0) {
            firstTemplate = disabledTemplates.value[0];
          }
          
          if (firstTemplate) {
            // 选中第一个模板
            setTimeout(() => {
              if (listType === 'enabled' && enabledTreeRef.value) {
                enabledTreeRef.value.setCurrentKey(firstTemplate.id);
              } else if (disabledTreeRef.value) {
                disabledTreeRef.value.setCurrentKey(firstTemplate.id);
              }
              handleNodeClick(firstTemplate);
            }, 100);
          } else {
            // 如果没有模板了，通知父组件清空
            emit('node-click', null);
          }
          
          // 通知父组件模板已变更
          emit('change');
          
          proxy.$modal.msgSuccess('删除成功');
        } else {
          proxy.$modal.msgError('删除失败');
        }
      }).catch(error => {
        proxy.$modal.msgError('删除失败');
      });
    } else {
      // 没有ID的模板，直接从前端列表中删除
      if (listType === 'enabled') {
        enabledTemplates.value = enabledTemplates.value.filter(item => item !== data);
      } else {
        disabledTemplates.value = disabledTemplates.value.filter(item => item !== data);
      }
      
      // 删除成功后自动选择第一个模板
      let firstTemplate = null;
      if (enabledTemplates.value.length > 0) {
        firstTemplate = enabledTemplates.value[0];
      } else if (disabledTemplates.value.length > 0) {
        firstTemplate = disabledTemplates.value[0];
      }
      
      if (firstTemplate) {
        // 选中第一个模板
        setTimeout(() => {
          if (listType === 'enabled' && enabledTreeRef.value) {
            enabledTreeRef.value.setCurrentKey(firstTemplate.id);
          } else if (disabledTreeRef.value) {
            disabledTreeRef.value.setCurrentKey(firstTemplate.id);
          }
          handleNodeClick(firstTemplate);
        }, 100);
      } else {
        // 如果没有模板了，通知父组件清空
        emit('node-click', null);
      }
      
      // 通知父组件模板已变更
      emit('change');
      
      proxy.$modal.msgSuccess('删除成功');
    }
 
  }).catch(function() {
    // 取消删除
  });
}

defineExpose({
  show
})
</script>

<style scoped>
.template-tree {
  width: 100%;
  height: 100%;
  overflow: auto;
  padding: 10px;
  box-sizing: border-box;
}

.template-group {
  margin-bottom: 20px;
}

.group-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 10px;
  color: #303133;
}

.custom-tree-node {
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.node-actions {
  margin-left: 10px;
}

.add-template {
  margin-top: 20px;
  text-align: center;
}

/* 调整树节点样式 */
:deep(.el-tree-node__content) {
  height: 32px;
  line-height: 32px;
  border-radius: 4px;
  padding-right:10px;
}

:deep(.el-tree-node.is-current > .el-tree-node__content) {
  background-color: #ecf5ff;
}

:deep(.el-tree-node.is-current > .el-tree-node__content:hover) {
  background-color: #ecf5ff;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}
</style>
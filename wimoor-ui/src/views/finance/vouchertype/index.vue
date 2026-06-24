<template>
  <div class="app-container">
    <div class="voucher-type-header">
      <h3>凭证字管理</h3>
      <el-button type="primary" @click="handleAddVoucherType">新增</el-button>
    </div>
    <el-table :data="voucherTypeList" border size="small" style="width: 100%;">
      <el-table-column prop="name" label="凭证字名称" align="center"  width="250">
        <template #default="{ row }">
          <el-input
              v-if="row.editing"
              v-model="row.name"
              size="small"
              @keyup.enter="handleSaveVoucherType(row)"
          />
          <span v-else>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row, $index }">
          <el-button v-if="row.editing" type="success" link size="small" @click="handleSaveVoucherType(row)">保存</el-button>
          <el-button type="primary" link size="small" @click="handleEditVoucherType(row)" :disabled="!row.groupid">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDeleteVoucherType(row, $index)" :disabled="!row.groupid">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label=""  align="center">
        <template #default="{ row, $index }">

        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup name="VoucherType">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listVoucherTypes, addVoucherType, updateVoucherType, deleteVoucherType } from "@/api/finance/voucher_type"
import finStore from "@/hooks/store/useFinanceStore.js"

const voucherTypeList = ref([])

const showMessage = (message, type = 'info') => {
  ElMessage({ message, type })
}
const showSuccess = (message) => showMessage(message, 'success')

const showConfirm = (message, title = '确认') => {
  return ElMessageBox.confirm(message, title, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
}

// 加载凭证字列表
async function loadVoucherTypes() {
  let params = { "groupid": await finStore.getCurrentTenantId(), "pageNum": 1, "pageSize": 100 };
  listVoucherTypes(params).then(res => {
    voucherTypeList.value = (res.rows || []).map(item => ({ ...item, editing: false }));
  });
}

// 新增凭证字
function handleAddVoucherType() {
  voucherTypeList.value.push({ id: null, name: '', editing: true, isNew: true });
}

// 编辑凭证字
function handleEditVoucherType(row) {
  row.editing = true;
}

// 保存凭证字
async function handleSaveVoucherType(row) {
  if (!row.name || !row.name.trim()) {
    if (row.isNew) {
      const idx = voucherTypeList.value.indexOf(row);
      if (idx > -1) voucherTypeList.value.splice(idx, 1);
    } else {
      row.editing = false;
    }
    return;
  }
  row.name = row.name.trim();
  let groupid = await finStore.getCurrentTenantId();
  if (row.isNew) {
    addVoucherType({ name: row.name, groupid: groupid }).then(() => {
      row.isNew = false;
      row.editing = false;
      loadVoucherTypes();
      showSuccess('新增成功');
    });
  } else {
    updateVoucherType({ id: row.id, name: row.name, groupid: groupid }).then(() => {
      row.editing = false;
      loadVoucherTypes();
      showSuccess('修改成功');
    });
  }
}

// 删除凭证字
function handleDeleteVoucherType(row, index) {
  if (row.isNew && !row.id) {
    voucherTypeList.value.splice(index, 1);
    return;
  }
  showConfirm('确认删除凭证字"' + row.name + '"？').then(() => {
    deleteVoucherType(row.id).then(() => {
      voucherTypeList.value.splice(index, 1);
      loadVoucherTypes();
      showSuccess('删除成功');
    });
  }).catch(() => {});
}

onMounted(() => {
  loadVoucherTypes();
})
</script>

<style scoped>
.voucher-type-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.voucher-type-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}
</style>

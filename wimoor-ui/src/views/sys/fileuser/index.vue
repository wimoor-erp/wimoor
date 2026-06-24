<template>
  <div class="file-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-tabs v-model="activeTab" @tab-change="handleTabChange">
            <el-tab-pane label="我的文件" name="personal" />
            <el-tab-pane label="公司文件" name="company" />
          </el-tabs>
          <el-upload
            :auto-upload="false"
            :show-file-list="false"
            :on-change="(file) => handleFileChange(file, activeTab === 'company')"
            multiple
          >
            <el-button type="primary" :icon="Upload">
              {{ activeTab === 'company' ? '上传公司文件' : '上传我的文件' }}
            </el-button>
          </el-upload>
        </div>
      </template>

      <el-table
        :data="activeTab === 'personal' ? fileList : companyFileList"
        v-loading="activeTab === 'personal' ? loading : companyLoading"
        stripe
        style="width: 100%"
        height="calc(100vh - 250px)"
      >
        <template #empty>
          <el-empty :description="activeTab === 'company' ? '暂无公司文件，请上传' : '暂无文件，请上传'" />
        </template>
        <el-table-column prop="name" label="文件名" min-width="300" show-overflow-tooltip>
          <template #default="scope">
            <el-icon style="margin-right: 8px; vertical-align: middle;"><Document /></el-icon>
            <el-link type="primary" :underline="false" @click="handleDownload(scope.row)">
              {{ scope.row.name }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="createtime" label="上传时间" width="200">
          <template #default="scope">
            {{ formatDate(scope.row.createtime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="handleDownload(scope.row)">下载</el-button>
            <el-button link type="success" @click="handleCopyLink(scope.row)">复制链接</el-button>
            <el-button link type="warning" @click="handleRename(scope.row)">重命名</el-button>
            <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 重命名对话框 -->
    <el-dialog v-model="renameDialogVisible" title="重命名" width="400px">
      <el-form :model="renameForm" label-width="80px">
        <el-form-item label="文件名">
          <el-input v-model="renameForm.name" placeholder="请输入文件名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renameDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRename" :loading="renameLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Upload, Document } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import largeFileUserApi from '@/api/sys/tool/largeFileUserApi'

const activeTab = ref('personal')
const loading = ref(false)
const companyLoading = ref(false)
const renameLoading = ref(false)
const fileList = ref([])
const companyFileList = ref([])
const renameDialogVisible = ref(false)
const renameForm = ref({ id: null, name: '' })

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  const s = String(date.getSeconds()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}:${s}`
}

async function loadFileList() {
  loading.value = true
  try {
    const res = await largeFileUserApi.list()
    fileList.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function loadCompanyFileList() {
  companyLoading.value = true
  try {
    const res = await largeFileUserApi.listCompany()
    companyFileList.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    companyLoading.value = false
  }
}

function handleTabChange(tab) {
  if (tab === 'personal') {
    loadFileList()
  } else {
    loadCompanyFileList()
  }
}

async function handleFileChange(file, isCompany = false) {
  const formData = new FormData()
  formData.append('file', file.raw)
  formData.append('name', file.name)

  try {
    if (isCompany) {
      await largeFileUserApi.uploadCompany(formData)
      ElMessage.success('公司文件上传成功')
      loadCompanyFileList()
    } else {
      await largeFileUserApi.upload(formData)
      ElMessage.success('上传成功')
      loadFileList()
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

async function handleDownload(row) {
  try {
    const res = await largeFileUserApi.download(row.id)
    const blob = new Blob([res.data])
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = row.name
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    ElMessage.error('下载失败')
  }
}

async function handleCopyLink(row) {
  try {
    const res = await largeFileUserApi.getLink(row.id)
    const url = res.data
    await navigator.clipboard.writeText(url)
    ElMessage.success('链接已复制到剪贴板')
  } catch (e) {
    ElMessage.error('复制失败')
  }
}

function handleRename(row) {
  renameForm.value = { id: row.id, name: row.name }
  renameDialogVisible.value = true
}

async function confirmRename() {
  if (!renameForm.value.name.trim()) {
    ElMessage.warning('请输入文件名')
    return
  }
  renameLoading.value = true
  try {
    if (activeTab.value === 'personal') {
      await largeFileUserApi.rename(renameForm.value.id, renameForm.value.name)
    } else {
      await largeFileUserApi.renameCompany(renameForm.value.id, renameForm.value.name)
    }
    ElMessage.success('重命名成功')
    renameDialogVisible.value = false
    if (activeTab.value === 'personal') {
      loadFileList()
    } else {
      loadCompanyFileList()
    }
  } catch (e) {
    ElMessage.error('重命名失败')
  } finally {
    renameLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除文件「${row.name}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const loading = ElLoading.service({
      lock: true,
      text: '删除中...',
      background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
      if (activeTab.value === 'personal') {
        await largeFileUserApi.remove(row.id)
      } else {
        await largeFileUserApi.removeCompany(row.id)
      }
      ElMessage.success('删除成功')
      if (activeTab.value === 'personal') {
        loadFileList()
      } else {
        loadCompanyFileList()
      }
    } finally {
      loading.close()
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadFileList()
})
</script>

<style scoped>
.file-container {
  padding: 10px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
:deep(.el-tabs__header) {
  margin-bottom: 0;
  background-color: transparent;
}
:deep(.el-tabs__nav-wrap::after) {
  display: none;
}
</style>

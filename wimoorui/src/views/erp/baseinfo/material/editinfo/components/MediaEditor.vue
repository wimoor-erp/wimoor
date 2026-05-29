<template>
  <div class="media-editor">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="SPU 图片池" name="pool">
        <div class="toolbar">
          <el-upload
            :show-file-list="false"
            :http-request="handleUpload"
            :multiple="true"
            drag
            accept="image/*,video/*"
            class="upload-drag"
          >
            <el-icon><upload-filled /></el-icon>
            <div class="el-upload__text">拖拽或点击上传（支持多文件）</div>
          </el-upload>
          <el-upload
            :show-file-list="false"
            :http-request="handleZipUpload"
            accept=".zip"
          >
            <el-button v-hasPerm="'erp:material:media:upload'">上传 ZIP</el-button>
          </el-upload>
          <el-button v-hasPerm="'erp:material:media:sync'" @click="syncDialogVisible = true">从 Amazon 同步</el-button>
        </div>
        <div v-if="poolList.length === 0" class="empty"><el-empty :image-size="60" description="暂无图片" /></div>
        <draggable
          v-else
          v-model="poolList"
          item-key="refId"
          class="media-grid"
          @end="onSortEnd('pool')"
        >
          <template #item="{ element }">
            <div class="media-card">
              <video v-if="element.mediaType === 1"
                :src="element.location || element.url"
                class="media-thumb"
                controls
                preload="metadata"
                style="object-fit:cover"
              />
              <el-image v-else :src="element.thumbLocation || element.location || element.url" fit="cover" class="media-thumb" />
              <div class="ops">
                <el-tooltip content="分配到 SKU 展示图"><el-button size="small" type="primary" @click="assignToSku(element)">→SKU</el-button></el-tooltip>
                <el-tooltip content="删除"><el-button size="small" type="danger" @click="onDelete(element)">删</el-button></el-tooltip>
              </div>
              <div v-if="element.platform === 'amazon'" class="badge-src">Amazon</div>
            </div>
          </template>
        </draggable>
      </el-tab-pane>

      <el-tab-pane label="SKU 展示图" name="sku">
        <div class="toolbar">
          <el-upload
            :show-file-list="false"
            :http-request="(r) => handleUpload(r, true)"
            :multiple="true"
            drag
            accept="image/*,video/*"
            class="upload-drag"
          >
            <el-icon><upload-filled /></el-icon>
            <div class="el-upload__text">拖拽或点击上传到 SKU 展示图</div>
          </el-upload>
        </div>
        <div v-if="skuList.length === 0" class="empty"><el-empty :image-size="60" description="暂无 SKU 展示图" /></div>
        <draggable
          v-else
          v-model="skuList"
          item-key="refId"
          class="media-grid"
          @end="onSortEnd('sku')"
        >
          <template #item="{ element }">
            <div class="media-card">
              <video v-if="element.mediaType === 1"
                :src="element.location || element.url"
                class="media-thumb"
                controls
                preload="metadata"
                style="object-fit:cover"
              />
              <el-image v-else :src="element.thumbLocation || element.location || element.url" fit="cover" class="media-thumb" />
              <el-tag v-if="element.isMain === 1" size="small" type="success" class="badge-main">主图</el-tag>
              <div class="ops">
                <el-tooltip v-if="element.isMain !== 1" content="设为主图">
                  <el-button size="small" @click="onSetMain(element)">主</el-button>
                </el-tooltip>
                <el-tooltip content="取消分配">
                  <el-button size="small" type="warning" @click="onUnassign(element)">撤</el-button>
                </el-tooltip>
                <el-select v-model="element.picClass" size="small" style="width:90px" @change="(v) => onUsageChange(element, v)">
                  <el-option :value="1" label="成品图" />
                  <el-option :value="2" label="说明图" />
                  <el-option :value="3" label="效果图" />
                  <el-option :value="4" label="主图" />
                  <el-option :value="5" label="场景图" />
                </el-select>
              </div>
            </div>
          </template>
        </draggable>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="syncDialogVisible" title="从 Amazon 同步图片" width="420px">
      <el-form label-width="100px">
        <el-form-item label="SKU"><el-input v-model="syncForm.sku" /></el-form-item>
        <el-form-item label="授权ID"><el-input v-model="syncForm.authorityId" /></el-form-item>
        <el-form-item label="站点"><el-input v-model="syncForm.marketplaceId" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSync">同步</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import draggable from 'vuedraggable'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  uploadMedia, uploadMediaBatch, listMedia, poolMedia,
  assignMedia, unassignMedia, setMainMedia, sortMedia,
  updateUsage, deleteMedia, syncFromAmazon
} from '@/api/erp/material/mediaApi.js'

const props = defineProps({
  materialId: { type: String, required: true }
})

const activeTab = ref('pool')
const poolList = ref([])
const skuList = ref([])
const syncDialogVisible = ref(false)
const syncForm = ref({ sku: '', authorityId: '', marketplaceId: '' })

async function load() {
  if (!props.materialId) return
  try {
    const [a, b] = await Promise.all([listMedia(props.materialId), poolMedia(props.materialId)])
    skuList.value = a.data || []
    poolList.value = b.data || []
  } catch (e) { /* ignore */ }
}

async function handleUpload(req, toSku = false) {
  const fd = new FormData()
  fd.append('file', req.file)
  fd.append('materialid', props.materialId)
  fd.append('refType', toSku ? 1 : 0)
  // 根据 MIME 类型自动判断 mediaType（0=图片 1=视频）
  const isVideo = req.file.type && req.file.type.startsWith('video/')
  fd.append('mediaType', isVideo ? 1 : 0)
  try {
    await uploadMedia(fd)
    ElMessage.success('上传成功')
    load()
  } catch (e) { ElMessage.error('上传失败') }
}

async function handleZipUpload(req) {
  const fd = new FormData()
  fd.append('file', req.file)
  fd.append('materialid', props.materialId)
  try {
    await uploadMediaBatch(fd)
    ElMessage.success('ZIP 上传成功')
    load()
  } catch (e) { ElMessage.error('ZIP 上传失败') }
}

async function assignToSku(item) {
  try {
    await assignMedia({
      mediaId: item.id,
      materialId: props.materialId,
      refType: 1,
      picClass: item.picClass || 1
    })
    ElMessage.success('已分配到 SKU 展示图')
    load()
  } catch (e) { ElMessage.error('分配失败') }
}

async function onUnassign(item) {
  try {
    await unassignMedia(item.refId)
    ElMessage.success('已撤销分配')
    load()
  } catch (e) { ElMessage.error('撤销失败') }
}

async function onSetMain(item) {
  try {
    await setMainMedia(item.refId)
    ElMessage.success('已设为主图')
    load()
  } catch (e) { ElMessage.error('设置失败') }
}

async function onSortEnd(which) {
  const list = which === 'pool' ? poolList.value : skuList.value
  const refIds = list.map(it => it.refId).filter(Boolean)
  try { await sortMedia(refIds) } catch (e) { /* ignore */ }
}

async function onUsageChange(item, picClass) {
  try { await updateUsage(item.refId, picClass) } catch (e) { ElMessage.error('修改用途失败') }
}

async function onDelete(item) {
  try {
    await ElMessageBox.confirm('确认删除该媒体资源？', '提示', { type: 'warning' })
    // 先解除当前关联，再尝试删除媒体文件（若还有其他关联则忽略）
    if (item.refId) {
      await unassignMedia(item.refId)
    }
    try {
      await deleteMedia(item.id, false)
    } catch (e) {
      // 仍有其他关联，文件保留，关联已解除，不报错
    }
    ElMessage.success('已删除')
    load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

async function onSync() {
  try {
    const res = await syncFromAmazon({
      materialId: props.materialId,
      sku: syncForm.value.sku,
      authorityId: syncForm.value.authorityId,
      marketplaceId: syncForm.value.marketplaceId
    })
    ElMessage.success(`已同步 ${res.data || 0} 张图片`)
    syncDialogVisible.value = false
    load()
  } catch (e) { ElMessage.error('同步失败') }
}

onMounted(load)
watch(() => props.materialId, load)
defineExpose({ reload: load })
</script>

<style scoped>
.media-editor { padding: 8px 0; }
.toolbar { display: flex; gap: 12px; align-items: center; margin-bottom: 12px; flex-wrap: wrap; }
.upload-drag { width: 360px; }
.empty { padding: 12px 0; }
.media-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap: 10px; }
.media-card { position: relative; aspect-ratio: 1/1; border: 1px solid #e4e7ed; border-radius: 4px; overflow: hidden; }
.media-thumb { width: 100%; height: 100%; }
.badge-main { position: absolute; top: 4px; left: 4px; }
.badge-src { position: absolute; bottom: 4px; right: 4px; background: #ff9900; color: #fff; padding: 1px 6px; font-size: 11px; border-radius: 2px; }
.ops { position: absolute; bottom: 4px; left: 4px; display: flex; gap: 4px; align-items: center; }
</style>

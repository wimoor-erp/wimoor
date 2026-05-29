<template>
  <div class="media-gallery">
    <div class="media-section">
      <h3 class="media-title">
        <span>SKU 展示图</span>
        <el-tag size="small" type="info">{{ skuList.length }}</el-tag>
      </h3>
      <div v-if="skuList.length === 0" class="media-empty">
        <el-empty :image-size="60" description="暂无 SKU 展示图" />
      </div>
      <div v-else class="media-grid">
        <div
          v-for="(item, idx) in skuList"
          :key="item.refId || item.id"
          class="media-card"
          @click="preview(skuList, idx)"
        >
          <video
            v-if="item.mediaType === 1"
            :src="item.location || item.url"
            class="media-thumb"
            preload="metadata"
          />
          <el-image
            v-else
            :src="item.thumbLocation || item.location || item.url"
            fit="cover"
            class="media-thumb"
          />
          <div v-if="item.mediaType === 1" class="badge-video">▶</div>
          <el-tag
            v-if="item.isMain === 1"
            size="small"
            type="success"
            class="badge-main"
          >主图</el-tag>
          <div v-if="item.slotPosition" class="badge-slot">{{ item.slotPosition }}</div>
        </div>
      </div>
    </div>

    <div class="media-section">
      <h3 class="media-title">
        <span>SPU 图片池</span>
        <el-tag size="small" type="info">{{ poolList.length }}</el-tag>
      </h3>
      <div v-if="poolList.length === 0" class="media-empty">
        <el-empty :image-size="60" description="暂无 SPU 图片池" />
      </div>
      <div v-else class="media-grid">
        <div
          v-for="(item, idx) in poolList"
          :key="item.refId || item.id"
          class="media-card"
          @click="preview(poolList, idx)"
        >
          <video
            v-if="item.mediaType === 1"
            :src="item.location || item.url"
            class="media-thumb"
            preload="metadata"
          />
          <el-image
            v-else
            :src="item.thumbLocation || item.location || item.url"
            fit="cover"
            class="media-thumb"
          />
          <div v-if="item.mediaType === 1" class="badge-video">▶</div>
          <div v-if="item.platform === 'amazon'" class="badge-src">Amazon</div>
        </div>
      </div>
    </div>

    <!-- 图片查看器 -->
    <el-image-viewer
      v-if="viewerVisible"
      :url-list="viewerUrls"
      :initial-index="viewerIndex"
      @close="viewerVisible = false"
    />

    <!-- 视频播放器弹窗 -->
    <el-dialog
      v-model="videoDialogVisible"
      :title="videoTitle"
      width="800px"
      destroy-on-close
      align-center
      @close="videoDialogVisible = false"
    >
      <video
        v-if="videoDialogVisible"
        :src="videoUrl"
        controls
        autoplay
        style="width:100%;max-height:70vh;background:#000;display:block;"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { listMedia, poolMedia } from '@/api/erp/material/mediaApi.js'

const props = defineProps({
  materialId: { type: String, required: true }
})

const skuList = ref([])
const poolList = ref([])
const viewerVisible = ref(false)
const viewerUrls = ref([])
const viewerIndex = ref(0)
const videoDialogVisible = ref(false)
const videoUrl = ref('')
const videoTitle = ref('视频预览')

async function load() {
  if (!props.materialId) return
  try {
    const [a, b] = await Promise.all([
      listMedia(props.materialId),
      poolMedia(props.materialId)
    ])
    skuList.value = a.data || []
    poolList.value = b.data || []
  } catch (e) {
    // 安静失败，组件渲染空态
  }
}

function preview(list, idx) {
  const item = list[idx]
  if (item.mediaType === 1) {
    // 视频：弹出播放器
    videoUrl.value = item.location || item.url
    videoTitle.value = item.name || '视频预览'
    videoDialogVisible.value = true
  } else {
    // 图片：图片查看器（仅过滤图片项）
    const imageItems = list.filter(it => it.mediaType !== 1)
    const imageIdx = imageItems.findIndex(it => it === item)
    viewerUrls.value = imageItems.map(it => it.location || it.url).filter(Boolean)
    viewerIndex.value = imageIdx >= 0 ? imageIdx : 0
    viewerVisible.value = true
  }
}

onMounted(load)
watch(() => props.materialId, load)
defineExpose({ reload: load })
</script>

<style scoped>
.media-gallery { padding: 12px 0; }
.media-section { margin-bottom: 20px; }
.media-title { font-size: 14px; font-weight: 600; margin-bottom: 8px; display: flex; align-items: center; gap: 8px; }
.media-empty { padding: 12px 0; }
.media-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(120px, 1fr)); gap: 10px; }
.media-card { position: relative; aspect-ratio: 1/1; border: 1px solid #e4e7ed; border-radius: 4px; overflow: hidden; cursor: pointer; }
.media-thumb { width: 100%; height: 100%; object-fit: cover; }
.badge-main { position: absolute; top: 4px; left: 4px; }
.badge-slot { position: absolute; bottom: 4px; left: 4px; background: rgba(0,0,0,0.6); color: #fff; padding: 1px 6px; font-size: 11px; border-radius: 2px; }
.badge-src { position: absolute; bottom: 4px; right: 4px; background: #ff9900; color: #fff; padding: 1px 6px; font-size: 11px; border-radius: 2px; }
.badge-video { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); background: rgba(0,0,0,0.55); color: #fff; width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; pointer-events: none; }
</style>

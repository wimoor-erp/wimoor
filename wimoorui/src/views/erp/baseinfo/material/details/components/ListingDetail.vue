<template>
  <div id="listing" class="tab-scroll mar-top-16">
    <h3>标题描述</h3>
    <div v-if="listingList.length > 0" class="listing-detail">
      <el-tabs v-model="activeLang" type="card" @tab-click="handleLangChange">
        <el-tab-pane v-for="lang in langList" :key="lang.value" :label="lang.label" :name="lang.value" />
      </el-tabs>
      <div v-if="currentListing" class="listing-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="标题">{{ currentListing.title || '—' }}</el-descriptions-item>
        </el-descriptions>
        <div class="listing-desc-label">描述：</div>
        <div class="listing-desc-content" v-html="currentListing.description"></div>
      </div>
      <el-empty v-else description="该语言暂无Listing信息" :image-size="60" />
    </div>
    <el-empty v-else description="暂无 标题描述" :image-size="80" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getListingList, getListingByLang } from '@/api/erp/material/materialListingApi.js'

const props = defineProps({
  materialId: { type: String, default: '' }
})

const langList = [
  { label: '英文', value: 'en' },
  { label: '德语', value: 'de' },
  { label: '法语', value: 'fr' },
  { label: '西班牙语', value: 'es' },
  { label: '意大利语', value: 'it' },
  { label: '荷兰语', value: 'nl' },
  { label: '波兰语', value: 'pl' },
  { label: '瑞典语', value: 'sv' },
  { label: '土耳其语', value: 'tr' },
  { label: '日本语', value: 'ja' },
  { label: '俄语', value: 'ru' }
]

const activeLang = ref('en')
const listingList = ref([])
const currentListing = ref(null)

async function loadList() {
  if (!props.materialId) return
  try {
    const res = await getListingList(props.materialId)
    listingList.value = res.data || []
    if (listingList.value.length > 0) {
      // 默认选中第一个有数据的语言
      activeLang.value = listingList.value[0].lang || 'en'
      await loadDetail(activeLang.value)
    }
  } catch (e) {
    listingList.value = []
  }
}

async function loadDetail(lang) {
  if (!props.materialId) return
  try {
    const res = await getListingByLang(props.materialId, lang)
    currentListing.value = res.data || null
  } catch (e) {
    currentListing.value = null
  }
}

function handleLangChange() {
  loadDetail(activeLang.value)
}

watch(() => props.materialId, (val) => {
  if (val) loadList()
}, { immediate: true })
</script>

<style scoped>
.listing-detail {
  padding: 10px 0;
}
.listing-content {
  margin-top: 10px;
}
.listing-desc-label {
  font-weight: bold;
  margin: 12px 0 6px;
  color: var(--el-text-color-regular);
}
.listing-desc-content {
  border: 1px solid var(--el-border-color-light);
  border-radius: 4px;
  padding: 16px;
  min-height: 100px;
  color: var(--el-text-color-primary);
  background: var(--el-fill-color-light);
  line-height: 1.6;
}

.listing-desc-content :deep(*) {
  color: inherit;
}

.listing-desc-content :deep(a) {
  color: var(--el-color-primary);
}

.listing-desc-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
}

.listing-desc-content :deep(td),
.listing-desc-content :deep(th) {
  border: 1px solid var(--el-border-color);
  padding: 8px;
}

.listing-desc-content :deep(img) {
  max-width: 100%;
  height: auto;
}
</style>

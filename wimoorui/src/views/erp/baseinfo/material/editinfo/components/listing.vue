<template>
  <h3 id="listing" class="tab-scroll">标题描述</h3>
  <div class="listing-editor">
    <el-tabs v-model="activeLang" type="card" @tab-click="handleLangChange">
      <el-tab-pane v-for="lang in langList" :key="lang.value" :label="lang.label" :name="lang.value" />
    </el-tabs>

    <el-form label-width="80px" class="listing-form">
      <el-form-item label="标题">
        <el-input
          v-model="formData.title"
          placeholder="请输入Listing标题"
          maxlength="500"
          show-word-limit
          clearable
        />
      </el-form-item>
      <el-form-item label="描述">
        <div class="tinymce-wrapper">
          <TinyMCEEditor
            v-model="formData.description"
            :editor-key="'listing-editor-' + activeLang"
            :config="editorConfig"
            @change="onEditorChange"
          />
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import TinyMCEEditor from '@/components/TinyMCE/TinyMCEEditor.vue'
import { getListingByLang, saveListing } from '@/api/erp/material/materialListingApi.js'

const props = defineProps({
  materialId: { type: String, default: '' },
  readonly: { type: Boolean, default: false }
})

const emit = defineEmits(['save'])

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
const loading = ref(false)
const saving = ref(false)
const formData = reactive({
  id: '',
  title: '',
  description: ''
})

const editorConfig = {
  height: 350,
  menubar: false,
  plugins: [
    'advlist', 'autolink', 'lists', 'link', 'image', 'charmap',
    'searchreplace', 'visualblocks', 'code', 'fullscreen',
    'insertdatetime', 'table', 'help', 'wordcount'
  ],
  toolbar: [
    'undo redo | blocks | bold italic underline strikethrough | removeformat',
    'alignleft aligncenter alignright alignjustify | bullist numlist | link image table | code fullscreen'
  ].join(' | '),
  base_url: '/tinymce',
  suffix: '.min',
  branding: false,
  content_style: `
    body { font-family: Arial, sans-serif; font-size: 14px; color: #000; background: #fff; line-height: 1.6; }
    table { border-collapse: collapse; width: 100%; }
    table td, table th { border: 1px solid #ddd; padding: 8px; }
  `
}

function resetForm() {
  formData.id = ''
  formData.title = ''
  formData.description = ''
}

async function loadLangData(lang) {
  if (!props.materialId) return
  loading.value = true
  try {
    const res = await getListingByLang(props.materialId, lang)
    if (res.data) {
      formData.id = res.data.id || ''
      formData.title = res.data.title || ''
      formData.description = res.data.description || ''
    } else {
      resetForm()
    }
  } catch (e) {
    resetForm()
  } finally {
    loading.value = false
  }
}

function handleLangChange() {
  loadLangData(activeLang.value)
}

function onEditorChange(val) {
  formData.description = val
}

async function handleSave() {
  if (!props.materialId) {
    ElMessage.warning('请先保存基本信息')
    return
  }
  if (formData.title && formData.title.length > 500) {
    ElMessage.error('标题长度不能超过500个字符')
    return
  }
  saving.value = true
  try {
    const data = {
      id: formData.id || undefined,
      materialid: props.materialId,
      lang: activeLang.value,
      title: formData.title,
      description: formData.description
    }
    const res = await saveListing(data)
    if (res.data) {
      formData.id = res.data.id
    }
    ElMessage.success('Listing信息保存成功')
    emit('save')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// Expose save method for parent component
defineExpose({ handleSave, saving })

watch(() => props.materialId, (val) => {
  if (val) {
    loadLangData(activeLang.value)
  }
}, { immediate: true })
</script>

<style scoped>
.listing-editor {
  padding: 10px 0;
}
.listing-form {
  margin-top: 10px;
}
.tinymce-wrapper {
  width: 100%;
}
</style>

<template>
  <!-- 新增科目对话框 -->
  <el-dialog 
    :model-value="visible" 
    @update:model-value="emit('update:model-value', $event)" 
    title="批量新增科目" 
    width="500px"
  >
    <el-select v-model="newSubjectIds" multiple placeholder="请选择科目" style="width: 100%" filterable automatic-dropdown>
      <el-option
        v-for="subject in availableSubjects"
        :key="subject.subjectId"
        :label="`${subject.subjectCode} ${subject.subjectName}`"
        :value="subject.subjectId"
      >
        <div class="account-option">
          <span>{{ subject.subjectCode }}</span>
          <span>{{ subject.subjectName }}</span>
        </div>
      </el-option>
    </el-select>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  subjects: {
    type: Array,
    default: () => []
  },
  selectedSubjectIds: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:model-value', 'confirm'])

const newSubjectIds = ref([])

// 所有可用科目
const availableSubjects = computed(() => {
  return props.subjects
})

const handleCancel = () => {
  newSubjectIds.value = []
  emit('update:model-value', false)
}

const handleConfirm = () => {
  if (!newSubjectIds.value || newSubjectIds.value.length === 0) {
    ElMessage.warning('请选择科目')
    return
  }
  
  // 触发确认事件，传递选中的科目ID
  emit('confirm', [...newSubjectIds.value])
  
  // 清空选择并关闭对话框
  newSubjectIds.value = []
  emit('update:model-value', false)
}

// 监听visible变化，重置选择
watch(() => props.visible, (newVal) => {
  if (newVal) {
    newSubjectIds.value = []
  }
})
</script>

<style scoped>
.account-option {
  display: flex;
  align-items: center;
}

.account-option span:first-child {
  margin-right: 10px;
  font-weight: 500;
}
</style>

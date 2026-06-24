<template>
  <el-select
      :model-value="modelValue"
      @update:model-value="handleChange"
      :placeholder="placeholder"
      :clearable="clearable"
      :disabled="disabled"
      :filterable="filterable"
      :size="size"
      :loading="loading"
      :remote="remote"
      :remote-method="remoteMethod"
      @visible-change="handleVisibleChange"
  >
    <el-option
        v-for="item in computedOptions"
        :key="item.value"
        :label="item.label"
        :value="item.value"
        :disabled="item.disabled"
    />

    <!-- 空数据提示 -->
    <template v-if="!loading && computedOptions.length === 0">
      <div class="empty-tip">
        {{ emptyText }}
      </div>
    </template>
  </el-select>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

// 接收 props
const props = defineProps({
  // v-model 绑定值
  modelValue: {
    type: [String, Number, Array],
    default: ''
  },
  // 选项数据（由父组件直接传递）
  options: {
    type: Array,
    default: () => []
  },
  // 类型（如果还需要的话）
  type: {
    type: String,
    default: ''
  },
  // 占位符
  placeholder: {
    type: String,
    default: '请选择'
  },
  // 是否可清空
  clearable: {
    type: Boolean,
    default: true
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  },
  // 是否可搜索
  filterable: {
    type: Boolean,
    default: false
  },
  // 是否支持远程搜索
  remote: {
    type: Boolean,
    default: false
  },
  // 尺寸
  size: {
    type: String,
    default: 'default'
  },
  // 空数据提示文本
  emptyText: {
    type: String,
    default: '暂无数据'
  },
  // 是否启用本地过滤
  enableLocalFilter: {
    type: Boolean,
    default: false
  },
  // 值字段名（如果options中的字段名不是标准的value/label）
  valueField: {
    type: String,
    default: 'value'
  },
  // 标签字段名
  labelField: {
    type: String,
    default: 'label'
  },
  // 禁用字段名
  disabledField: {
    type: String,
    default: 'disabled'
  }
})

const emit = defineEmits([
  'update:modelValue',
  'change',
  'visible-change'
])

// 响应式数据
const loading = ref(false)
const searchQuery = ref('')

// 处理后的选项（用于远程搜索时的过滤）
const filteredOptions = ref([])

// 计算属性：处理后的选项
const computedOptions = computed(() => {
  if (props.enableLocalFilter && searchQuery.value) {
    return filteredOptions.value
  }

  // 如果父组件传递了options，直接使用
  if (props.options && props.options.length > 0) {
    // 标准化选项格式
    return props.options.map(item => {
      // 如果已经是标准格式，直接返回
      if (item.value && item.label) {
        return item
      }
      // 否则进行转换
      return {
        value: item[props.valueField] || item.id || item.code,
        label: item[props.labelField] || item.name || item.title,
        disabled: item[props.disabledField] || false,
        // 保留原始数据
        raw: item
      }
    })
  }

  return []
})

// 处理值变化
const handleChange = (value) => {
  emit('update:modelValue', value)
  emit('change', value)
}

// 处理下拉框显示/隐藏
const handleVisibleChange = (visible) => {
  emit('visible-change', visible)

  // 关闭下拉框时重置搜索
  if (!visible) {
    searchQuery.value = ''
    filteredOptions.value = []
  }
}

// 远程搜索方法（本地过滤）
const remoteMethod = (query) => {
  searchQuery.value = query

  if (!query) {
    filteredOptions.value = []
    return
  }

  // 本地过滤
  filteredOptions.value = computedOptions.value.filter(item => {
    const label = item.label || ''
    return label.toLowerCase().includes(query.toLowerCase())
  })
}

// 监听父组件传递的 options 变化
watch(() => props.options, (newOptions) => {
  console.log('父组件传递的 options 更新了:', newOptions)
}, {
  deep: true
})

// 监听 modelValue 变化（如果需要处理值的变化）
watch(() => props.modelValue, (newValue) => {
  console.log('选中的值变化了:', newValue)
})
</script>

<style scoped>
.empty-tip {
  padding: 10px;
  text-align: center;
  color: #999;
  font-size: 12px;
}
</style>
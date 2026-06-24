<template>
  <el-button :type="btnType" :link="link" class='ic-btn' title='打印' v-print="printContent">
    <el-icon><Printer /></el-icon>
  </el-button>
</template>

<script setup>
import { Printer } from '@element-plus/icons-vue';
import { computed } from 'vue';

const props = defineProps({
  printId: {
    type: String,
    required: true
  },
  title: {
    type: String,
    default: ''
  },
  scale: {
    type: Number,
    default: 0.8  // 可自定义缩放比例，0-1之间
  },
  btnType: {
    type: String,
    default: 'default'  // 可选值: default, primary, success, warning, danger, info
  },
  link: {
    type: Boolean,
    default: false  // 是否为链接按钮
  }
});

const printContent = computed(() => ({
  id: props.printId,
  popTitle: props.title,
  extraHead: `<style>
		@media print {
			html {
				transform: scale(${props.scale});
				transform-origin: top left;
				width: ${100 / props.scale}%;
			}
		}
	</style>`,
  beforeOpenCallback() {
    // 可选：临时修改容器高度以适应缩放
    const container = document.getElementById(props.printId);
    if (container) {
      container.style.height = 'auto';
    }
  }
}));
</script>
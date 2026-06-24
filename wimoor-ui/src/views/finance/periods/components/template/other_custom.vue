<template>
  <div class="other-custom-template">
    <h2>其他自定义模板</h2>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>模板详情</span>
          <el-button type="primary" size="small" @click="refreshTemplate">刷新</el-button>
        </div>
      </template>
      
      <div class="template-content">
        <p>这是一个其他自定义模板的示例</p>
        <p>您可以根据需要修改此模板的内容</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 刷新模板
const refreshTemplate = () => {
  console.log('Refresh template')
}
</script>

<style scoped>
.other-custom-template {
  padding: 20px;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-content {
  padding: 20px 0;
}
</style>
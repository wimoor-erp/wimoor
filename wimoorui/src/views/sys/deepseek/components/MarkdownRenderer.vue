<template>
  <div class="markdown-renderer">
    <div 
      class="rendered-content" 
      v-html="processedContent"
      @click="handleContentClick"
    ></div>
  </div>
</template>

<script setup>
import{ref,reactive,toRefs,onMounted,nextTick,computed,watch} from"vue"
import { useDark, useToggle } from "@vueuse/core";
import { renderMarkdown, copyToClipboard ,loadTheme} from './markdownParser';
import hljs from 'highlight.js';
 const isDark = useDark();
import { 
  getNormalizedLanguage, 
  getLanguageIcon, 
  getLanguageColor,
  extractLanguageFromMarkdown 
} from './languageParser';

const props = defineProps({
  content: {
    type: String,
    required: true
  }
});
watch(isDark, (newVal) => {
      // 在 DOM 更新后执行高亮
     if(isDark.value){
     		loadTheme("dark");
     }else{
     		loadTheme("light")
     }
});
// 假设 markdownContent 是包含原始 Markdown 文本的 ref
watch(props, (newVal) => {
  if (newVal) {
    nextTick(() => {
      // 在 DOM 更新后执行高亮
      document.querySelectorAll('pre code').forEach((block) => {
        hljs.highlightBlock(block);
      });
    });
  }
});
 onMounted(()=>{
	 if(isDark.value){
		loadTheme("dark");
	 }else{
		loadTheme("light")
	 }
 });
 // HTML 转义函数（防止 XSS 和语法冲突）
 function escapeHtml(unsafe) {
   return unsafe
     .replace(/&/g, "&amp;")
     .replace(/</g, "&lt;")
     .replace(/>/g, "&gt;")
     .replace(/"/g, "&quot;")
     .replace(/'/g, "&#039;");
 }

const processedContent = computed(() => {
  let content = props.content;
  // 在渲染前为代码块添加语言标签
    content = content.replace(/```(\w*)\n([\s\S]*?)```/g, (match, lang, code) => {
      const normalizedLang = getNormalizedLanguage(lang);
      const languageIcon = getLanguageIcon(normalizedLang);
      return `\n<div class="code-block-wrapper">
        <div class="code-header">
          <span class="language-tag" :style="{ color: getLanguageColor(normalizedLang) }">
            ${languageIcon} ${normalizedLang}
          </span>
          <span class="copy-hint">Alt+点击复制</span>
        </div>
        <pre><code class="language-${lang || 'text'}">${escapeHtml(code)}</code></pre>
      </div>\n`;
    });
   return  renderMarkdown(content);
});

// 处理代码块的复制点击
const handleContentClick = async (event) => {
  const codeWrapper = event.target.closest('.code-block-wrapper');
  if (codeWrapper && event.altKey) {
    const codeElement = codeWrapper.querySelector('code');
    if (codeElement) {
      const success = await copyToClipboard(codeElement.textContent);
      if (success) {
        showCopyFeedback(codeWrapper);
      }
    }
    event.preventDefault();
  }
};

// 显示复制反馈
const showCopyFeedback = (element) => {
  const feedback = document.createElement('div');
  feedback.textContent = '已复制！';
  feedback.className = 'copy-feedback';
  element.appendChild(feedback);
  
  setTimeout(() => {
    if (element.contains(feedback)) {
      element.removeChild(feedback);
    }
  }, 2000);
};

</script>
<style scoped>
.markdown-renderer {
  max-width: 100%;
  line-height: 1.7;
}

.rendered-content {
  font-size: 15px;
}

/* 代码块包装器样式 */
.rendered-content :deep(.code-block-wrapper) {
  position: relative;
  margin: 1.5em 0;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #dedede;
}

.rendered-content :deep(.code-header) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #bdbdbd;
  font-size: 14px;
}

.rendered-content :deep(.language-tag) {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
  font-family: 'Monaco', 'Menlo', monospace;
}

.rendered-content :deep(.copy-hint) {
  color: #888;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.rendered-content :deep(.code-block-wrapper:hover .copy-hint) {
  opacity: 1;
}

.rendered-content :deep(.code-block-wrapper pre) {
  margin: 0;
  padding: 16px;
  overflow-x: auto;
}

.rendered-content :deep(.copy-feedback) {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 123, 255, 0.9);
  color: white;
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  z-index: 10;
  animation: fadeInOut 2s ease;
}

@keyframes fadeInOut {
  0%, 100% { opacity: 0; transform: translate(-50%, -40%); }
  20%, 80% { opacity: 1; transform: translate(-50%, -50%); }
}

/* 其他样式保持不变 */
.rendered-content :deep(h1) {
  font-size: 1.8em;
  margin: 1.2em 0 0.8em;
  font-weight: 700;
  border-bottom: 2px solid #eaecef;
  padding-bottom: 0.3em;
}

.rendered-content :deep(p) {
  margin: 1em 0;
  line-height: 1.8;
}

.rendered-content :deep(:not(pre) > code) {
  background: #f4f4f4;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  border: 1px solid #e1e4e8;
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 0.9em;
}

@media (max-width: 768px) {
  .rendered-content :deep(.code-header) {
    padding: 8px 12px;
    font-size: 12px;
  }
  
  .rendered-content :deep(.copy-hint) {
    display: none;
  }
}
</style>
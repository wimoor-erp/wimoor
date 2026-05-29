<!-- components/TinyMCEEditor.vue -->
<template>
  <div class="tinymce-editor">
    <div ref="editorElement" :id="editorKey?editorKey:'tinymce'" class="editor-element"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch,nextTick,toRefs } from 'vue'
import hljs from 'highlight.js'
import largeFileApi from '@/api/sys/tool/largeFileApi.js';
import { useRoute,useRouter } from 'vue-router';
// 导入你需要的语言
import javascript from 'highlight.js/lib/languages/javascript'
import python from 'highlight.js/lib/languages/python'
import java from 'highlight.js/lib/languages/java'
import css from 'highlight.js/lib/languages/css'
import xml from 'highlight.js/lib/languages/xml'
import {tableHeaderFloat,screenToTop,checkVisiable} from '@/utils/jquery/table/float-header.js';
// 注册语言
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('python', python)
hljs.registerLanguage('java', java)
hljs.registerLanguage('css', css)
hljs.registerLanguage('html', xml)

const props = defineProps({
  modelValue: '',
  config: {},
  editorKey:"",
  debugMode: { type: Boolean, default: false }
})
const { editorKey} = toRefs(props);
	
const emit = defineEmits(['update:modelValue', 'init', 'change','mceNewDocument']);
const uploading=ref(false);
const editorElement = ref(null)
const editorInstance = ref(null)
const editorStatus = ref('未初始化')
const contentLength = ref(0)
let route = useRoute();
let router = useRouter();
const defaultConfig = {
  height: 400,
  menubar: false,
  selector: `#${props.editorKey}`,
  plugins: [
    'save','advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
    'searchreplace', 'visualblocks', 'code','codesample',  'fullscreen',
    'insertdatetime', 'media', 'table', 'help', 'wordcount',  
  ],
  toolbar:[
  'save undo redo',
  'blocks',
  'bold italic underline strikethrough',
  'removeformat',
  'alignleft aligncenter alignright alignjustify',
  'bullist numlist',
  'link code codesample',
  'image table help'
  ].join(' | '),
  base_url: '/tinymce',
  suffix: '.min',
  branding: false,
  skin: 'oxide',
  codesample_languages: [
	  { text: 'HTML/XML', value: 'markup' },
	  { text: 'JavaScript', value: 'javascript' },
	  { text: 'CSS', value: 'css' },
	  { text: 'Python', value: 'python' },
	  { text: 'Java', value: 'java' },
	  { text: 'C++', value: 'cpp' },
	  { text: 'C#', value: 'csharp' },
	  { text: 'PHP', value: 'php' },
	  { text: 'Ruby', value: 'ruby' },
	  { text: 'SQL', value: 'sql' },
	  { text: 'Bash', value: 'bash' },
	  { text: 'JSON', value: 'json' },
	  { text: 'YAML', value: 'yaml' },
	  { text: 'Markdown', value: 'markdown' }
	],
  content_style: `
    body { 
      font-family: Arial, sans-serif; 
      font-size: 14px; 
      color: #000000 !important; 
      background-color: #ffffff !important; 
      line-height: 1.5;
    }
    /* 确保编辑区域内容可见 */
    .mce-content-body {
      color: #000000 !important;
      background-color: #ffffff !important;
      min-height: 200px;
    }
    /* 确保工具栏和状态栏可见 */
    .tox-toolbar, .tox-statusbar {
      background-color: #ffffff !important;
    }
    table { border-collapse: collapse; width: 100%; }
    table td, table th { border: 1px solid #ddd; padding: 8px; }
	 /* highlight.js 样式 */
	    pre code.hljs {
	      display: block;
	      overflow-x: auto;
	      padding: 1em;
	      border-radius: 4px;
	    }
	    
	    code.hljs {
	      padding: 3px 5px;
	      border-radius: 3px;
	    }
	    
	    .hljs {
	      background: #f8f9fa;
	      color: #24292e;
	    }
	    
	    .hljs-doctag,
	    .hljs-keyword,
	    .hljs-meta .hljs-keyword,
	    .hljs-template-tag,
	    .hljs-template-variable,
	    .hljs-type,
	    .hljs-variable.language_ {
	      color: #d73a49;
	    }
	    
	    .hljs-title,
	    .hljs-title.class_,
	    .hljs-title.class_.inherited__,
	    .hljs-title.function_ {
	      color: #6f42c1;
	    }
	    
	    .hljs-attr,
	    .hljs-attribute,
	    .hljs-literal,
	    .hljs-meta,
	    .hljs-number,
	    .hljs-operator,
	    .hljs-variable,
	    .hljs-selector-attr,
	    .hljs-selector-class,
	    .hljs-selector-id {
	      color: #005cc5;
	    }
	    
	    .hljs-string,
	    .hljs-meta .hljs-string,
	    .hljs-regexp,
	    .hljs-template-tag {
	      color: #032f62;
	    }
	    
	    .hljs-built_in,
	    .hljs-symbol {
	      color: #e36209;
	    }
	    
	    .hljs-comment,
	    .hljs-code,
	    .hljs-formula {
	      color: #6a737d;
	    }
	    
	    .hljs-name,
	    .hljs-quote,
	    .hljs-selector-tag,
	    .hljs-selector-pseudo {
	      color: #22863a;
	    }
	    
	    .hljs-subst {
	      color: #24292e;
	    }
	    
	    .hljs-section {
	      color: #005cc5;
	      font-weight: bold;
	    }
	    
	    .hljs-bullet {
	      color: #735c0f;
	    }
	    
	    .hljs-emphasis {
	      color: #24292e;
	      font-style: italic;
	    }
	    
	    .hljs-strong {
	      color: #24292e;
	      font-weight: bold;
	    }
	    
	    .hljs-addition {
	      color: #22863a;
	      background-color: #f0fff4;
	    }
	    
	    .hljs-deletion {
	      color: #b31d28;
	      background-color: #ffeef0;
	    }
  `,
   init_instance_callback: (editor) => {
      editor.setContent(props.modelValue);
      // 监听内容变化，对代码块进行高亮
      editor.on('SetContent', () => {
        highlightCodeBlocks()
      })
      
      // 监听DOM更新
      editor.on('NodeChange', () => {
        highlightCodeBlocks()
      })
      
      // 初始高亮
      setTimeout(() => {
        highlightCodeBlocks()
      }, 100)
    },
	// 图片上传配置
	  images_upload_handler: async (blobInfo) => {
		uploading.value = true
		try {
		  // 创建 FormData
		  const formData = new FormData()
		  formData.append('file', blobInfo.blob(), blobInfo.filename())
		  // 实际项目中替换为你的上传接口
		  const response = await largeFileApi.upload("notepad",formData);
		  const data = await response.data
		  uploading.value = false
		  // 返回服务器上的图片 URL
		  return data
		  
		} catch (error) {
		  uploading.value = false
		  console.error('图片上传失败:', error)
		  throw new Error('图片上传失败: ' + error.message)
		}
	  },
	  
	  // 自动上传
	  automatic_uploads: true,
	  
	  // 图片高级选项
	  image_advtab: true,
	  image_caption: true,
	  image_title: true,
	  
	  // 图片样式
	  image_class_list: [
		{ title: '无样式', value: '' },
		{ title: '阴影', value: 'image-shadow' },
		{ title: '边框', value: 'image-border' }
	  ],
}
// 高亮代码块函数 - 改进版，支持iframe中的内容
const highlightCodeBlocks = () => {
  // 确保editorElement.value存在
  if (!editorElement || !editorElement.value) {
    return;
  }
  
  try {
    // 尝试获取TinyMCE编辑器实例
    if (editorInstance.value) {
      // 获取编辑器的iframe内容文档
      const iframeBody = editorInstance.value.getBody();
      if (iframeBody) {
        const preElements = iframeBody.querySelectorAll('pre code');
        preElements.forEach((block) => {
          hljs.highlightElement(block);
        });
        return;
      }
    }
    
    // 如果无法获取编辑器实例或iframe内容，回退到原始方法
    const preElements = editorElement.value.querySelectorAll('pre code');
    preElements.forEach((block) => {
      hljs.highlightElement(block);
    });
  } catch (error) {
    console.error(`高亮代码块时出错:`, error);
  }
}

const initEditor = async () => {
  
  // 首先检查编辑器元素引用是否存在
  if (!editorElement || !editorElement.value) {
    editorStatus.value = '未初始化';
    return;
  }
  
  // 检查编辑器元素是否在DOM中
  if (!isEditorElementInDOM()) {
    editorStatus.value = '未初始化';
    return;
  }
  
  // 确保 TinyMCE 已加载
  if (!window.tinymce) {
    try {
      await loadTinyMCE();
    } catch (error) {
      editorStatus.value = '未初始化';
      // 重试加载
      setTimeout(() => {
        loadTinyMCE().then(() => {
          initEditor();
        }).catch(err => {
          editorStatus.value = '未初始化';
        });
      }, 1000);
      return;
    }
  }

  if (window.tinymce && editorElement.value) {
    try {
      // 确保没有重复的编辑器实例
      const existingEditor = getEditorById(props.editorKey);
      if (existingEditor) {
        window.tinymce.remove(existingEditor);
      }
      
      const config = {
        ...defaultConfig,
        ...props.config,
        target: editorElement.value,
        setup: (editor) => {
          editor.id = props.editorKey
          
          editor.on('init', () => {
      // 再次检查元素引用，因为初始化可能耗时较长
      if (!editorElement || !editorElement.value) {
        editorStatus.value = '未初始化';
        return;
      }
      
      editorStatus.value = '已初始化';
      
      // 强制设置内容，确保内容可见
      if (props.modelValue) {
        editor.setContent(props.modelValue);
      } else {
        editor.setContent('<p>请输入内容...</p>');
      }
      
      // 确保正确设置实例引用
      editorInstance.value = editor;
      contentLength.value = editor.getContent().length;
      emit('init', editor);
      
      // 初始化后立即高亮代码块
      setTimeout(() => {
        highlightCodeBlocks();
      }, 100);
    });
          
          // 添加实例就绪事件处理
          editor.on('setup', () => {
            // 检查元素引用是否存在
            if (!editorElement || !editorElement.value) {
              editorStatus.value = '未初始化';
              return;
            }
            
          });
          
          editor.on('ExecCommand', (e) => {
            if (e.command === 'mceNewDocument') {
              emit('mceNewDocument');
            }
          });
          
          editor.on('change keyup undo redo', () => {
            const content = editor.getContent();
            contentLength.value = content.length;
            emit('update:modelValue', content);
            emit('change', content);
          });
          
          // 监听编辑器失去焦点事件，保存内容
          editor.on('blur', () => {
            const content = editor.getContent();
            emit('update:modelValue', content);
            emit('change', content);
          });
          
          // 调试：检查所有可用的按钮
          const buttons = editor.ui.registry.getAll().buttons;
          
          // 检查 removeformat 按钮是否存在
          if (buttons.removeformat) {
            console.log('removeformat 按钮已注册');
          } else {
            console.log('removeformat 按钮未注册，使用自定义按钮');
            // 如果内置按钮不存在，添加自定义按钮
            editor.ui.registry.addButton('customremoveformat', {
              icon: 'remove-formatting',
              tooltip: '清除格式',
              onAction: () => {
                editor.execCommand('RemoveFormat');
              }
            });
          }
        }
      };
      
      // 初始化编辑器
      window.tinymce.init(config);
      
    } catch (error) {
      console.error(`初始化编辑器 ${props.editorKey} 失败:`, error);
      // 重试初始化
      setTimeout(() => {
        console.log(`尝试重新初始化编辑器 ${props.editorKey}`);
        initEditor().catch(err => console.error('重试初始化失败:', err));
      }, 500);
    }
  } else {
    console.warn('无法初始化编辑器：TinyMCE 未加载或编辑器元素不存在');
  }
}
// 安全的获取 TinyMCE 实例 - 添加重试机制
const getTinyMCE = () => {
  // 多种方式获取 tinymce 实例
  if (window.tinymce) {
    return window.tinymce
  } else if (window.parent?.tinymce) {
    return window.parent.tinymce
  } else if (window.top?.tinymce) {
    return window.top.tinymce
  }
  return null
}

// 通过 ID 获取指定的编辑器实例 - 增强版本
const getEditorById = (id) => {
  const tinymce = getTinyMCE()
  if (!tinymce) return null
  
  // 方法1: 使用 get()
  if (typeof tinymce.get === 'function') {
    const editor = tinymce.get(id)
    if (editor) return editor
  }
  
  // 方法2: 使用 activeEditor (如果只有一个活动编辑器)
  if (tinymce.activeEditor && tinymce.activeEditor.id === id) {
    return tinymce.activeEditor
  }
  
  // 方法3: 遍历 editors 对象
  if (tinymce.editors) {
    // 直接访问
    if (tinymce.editors[id]) return tinymce.editors[id]
    
    // 遍历所有编辑器寻找匹配ID
    for (let key in tinymce.editors) {
      if (tinymce.editors[key] && tinymce.editors[key].id === id) {
        return tinymce.editors[key]
      }
    }
  }
  
  return null
}

// 检查编辑器元素是否存在于DOM中
// 确保当editorElement.value为null时，始终返回false
const isEditorElementInDOM = () => {
  try {
    // 首先检查editorElement引用是否存在
    if (!editorElement) {
      return false;
    }
    
    // 然后检查editorElement.value是否存在
    if (!editorElement.value) {
      return false;
    }
    
    // 再检查value是否是DOM元素类型
    if (!(editorElement.value instanceof Element)) {
      return false;
    }
    
    // 优先使用现代API检查元素是否在DOM中
    if ('isConnected' in editorElement.value) {
      const isConnected = editorElement.value.isConnected;
      console.log(`isConnected: ${isConnected}, 元素ID: ${editorElement.value.id || '无ID'}`);
      return isConnected;
    } else {
      // 降级方案：检查元素是否在document.body中
      let el = editorElement.value;
      let depth = 0;
      while (el && depth < 1000) { // 添加深度限制防止无限循环
        if (el === document.body) {
          return true;
        }
        el = el.parentElement;
        depth++;
      }
      console.warn(`降级方案检查失败，元素不在DOM中`);
      return false;
    }
  } catch (error) {
    console.error(`检查编辑器元素是否在DOM中时出错:`, error);
    return false;
  }
};

// 刷新编辑器（修复状态）- 增强版
const refreshEditor = async () => {
  
  // 检查编辑器元素是否存在
  if (!editorElement || !editorElement.value) {
    editorStatus.value = '未初始化';
    // 销毁可能存在的实例
    if (editorInstance.value) {
      try {
        destroyEditor();
      } catch (error) {
        console.error(`销毁编辑器实例失败:`, error);
      }
    }
    return false;
  }
  
  // 检查编辑器元素是否在DOM中
  if (!isEditorElementInDOM()) {
    editorStatus.value = '未初始化';
    // 销毁可能存在的实例
    if (editorInstance.value) {
      try {
        destroyEditor();
      } catch (error) {
        console.error(`销毁编辑器实例失败:`, error);
      }
    }
    return false;
  }
  
  try {
    // 先销毁可能存在的编辑器实例
    if (editorInstance.value) {
      await destroyEditor();
    }
    
    // 等待DOM更新
    await nextTick();
    
    // 再次检查元素是否存在（防止在等待期间被移除）
    if (!editorElement || !editorElement.value) {
      console.warn(`编辑器元素在等待DOM更新期间丢失`);
      editorStatus.value = '未初始化';
      return false;
    }
    // 重新初始化编辑器
    await initEditor();
    return true;
  } catch (error) {
    console.error(`刷新编辑器 ${props.editorKey} 失败:`, error);
    editorStatus.value = '未初始化';
    
    // 添加重试机制
    const retryRefresh = () => {
      initEditor().catch(err => {
        console.error('重试刷新失败:', err);
        editorStatus.value = '未初始化';
      });
    };
    
    // 使用setTimeout进行异步重试
    setTimeout(retryRefresh, 500);
    return false;
  }
}

const loadTinyMCE = () => {
  return new Promise((resolve, reject) => {
    // 首先检查是否已经加载了TinyMCE
    if (window.tinymce) {
      resolve();
      return;
    }
    
    // 如果TinyMCE未加载，尝试从public目录加载
    const script = document.createElement('script');
    script.src = '/tinymce/tinymce.min.js';
    
    // 设置加载超时处理
    const timeoutId = setTimeout(() => {
      reject(new Error('加载TinyMCE超时（5秒）'));
      document.head.removeChild(script); // 清理
    }, 5000); // 5秒超时
    
    script.onload = () => {
      clearTimeout(timeoutId);
      if (window.tinymce) {
        resolve();
      } else {
        reject(new Error('TinyMCE脚本已加载但window.tinymce不存在'));
      }
    };
    
    script.onerror = () => {
      clearTimeout(timeoutId);
      reject(new Error('Failed to load TinyMCE from public directory'));
    };
    
    // 添加脚本到文档头部
    document.head.appendChild(script);
  });
}

const destroyEditor = () => {
  if (editorInstance.value) {
    window.tinymce.remove(editorInstance.value)
    editorInstance.value = null
  }
}

// 监听内容变化 - 增强版
watch(() => props.modelValue, (newValue) => {
  if (editorInstance.value && newValue !== editorInstance.value.getContent()) {
    editorInstance.value.setContent(newValue);
    // 重新高亮代码块
    setTimeout(() => highlightCodeBlocks(), 100);
  }
});

// 监听组件可见性变化
let visibilityObserver = null;
let visibleState = ref(true);

// 监听编辑器可见性变化时，确保iframe内容正确渲染
watch(visibleState, (newVisible, oldVisible) => {
  if (newVisible && !oldVisible && editorInstance.value) {
    // 强制刷新内容
    setTimeout(() => {
      if (editorInstance.value) {
        const currentContent = editorInstance.value.getContent();
        editorInstance.value.setContent(''); // 清空
        editorInstance.value.setContent(currentContent); // 重新设置
        highlightCodeBlocks();
      }
    }, 100);
  }
});

// 添加对元素可见性的监听
const setupVisibilityObserver = () => {
  // 检查是否支持IntersectionObserver
  if ('IntersectionObserver' in window) {
    visibilityObserver = new IntersectionObserver((entries) => {
      const isVisible = entries[0].isIntersecting;
      
      // 记录上一次的可见状态
      const wasVisible = visibleState.value;
      visibleState.value = isVisible;
      // 当组件从不可见变为可见时，确保内容正确显示
    if (isVisible && !wasVisible) {
      // 延迟处理，确保DOM已完全更新
      setTimeout(() => {
        // 1. 检查编辑器元素是否存在
        if (!editorElement || !editorElement.value) {
          editorStatus.value = '未初始化';
          // 销毁可能存在的实例
          if (editorInstance.value) {
            try {
              destroyEditor();
            } catch (error) {
              console.error(`销毁编辑器实例失败:`, error);
            }
          }
          return;
        }
        
        // 2. 检查编辑器元素是否仍在DOM中
        if (!isEditorElementInDOM()) {
          editorStatus.value = '未初始化';
          // 销毁可能存在的实例
          if (editorInstance.value) {
            try {
              destroyEditor();
            } catch (error) {
              console.error(`销毁编辑器实例失败:`, error);
            }
          }
          return;
        }
        
        // 3. 如果有编辑器实例，检查内容是否匹配
        if (editorInstance.value) {
          const editorContent = editorInstance.value.getContent();
          const modelContent = props.modelValue;
          
          // 如果内容不匹配，更新编辑器内容
          if (editorContent !== modelContent) {
            editorInstance.value.setContent(modelContent);
            highlightCodeBlocks();
          }  
        } else {
          // 4. 如果没有编辑器实例，尝试重新初始化
          initEditor();
        }
      }, 300); // 增加延迟时间，确保在复杂的UI切换中DOM已完全稳定
    }
    }, {
      threshold: 0.1,  // 当元素可见部分超过10%时触发
      rootMargin: '0px' // 边缘距离
    });
    
    // 确保元素存在再进行观察
    if (editorElement.value) {
      visibilityObserver.observe(editorElement.value);
     }  
  } else {
    // 降级方案：使用定时器定期检查
    const interval = setInterval(() => {
      if (isEditorElementInDOM() && !visibleState.value) {
        visibleState.value = true;
        setTimeout(() => {
          if (editorInstance.value && props.modelValue !== editorInstance.value.getContent()) {
            editorInstance.value.setContent(props.modelValue);
            highlightCodeBlocks();
          }
        }, 100);
      }
    }, 2000); // 每2秒检查一次
    
    // 存储interval ID以便卸载时清理
    onUnmounted(() => clearInterval(interval));
  }
};

 // 修复路由监听 - 正确监听路径变化
watch(() => router.currentRoute.value.path, (newPath, oldPath) => {
  console.log(`路由变化: ${oldPath} -> ${newPath}`);
  // 当路由变化时，尝试重新初始化编辑器（如果需要）
  if (checkVisiable()) {
    // 延迟初始化，确保DOM已更新
    setTimeout(() => {
      refreshEditor();
    }, 100);
  }
});

// 组件挂载时的初始化
onMounted(() => {
  initEditor();
  setupVisibilityObserver();
});

// 组件卸载时的清理
onUnmounted(() => {
  destroyEditor();
  if (visibilityObserver) {
    visibilityObserver.disconnect();
    visibilityObserver = null;
  }
});
// 暴露编辑器实例给父组件
defineExpose({
  // 安全地获取编辑器实例，增加错误处理和重试逻辑
  getEditor: () => {
    // 情况1: 实例已经存在，直接返回
    if (editorInstance.value) {
      return editorInstance.value;
    }
    
    // 情况2: 实例不存在但元素在DOM中，尝试重新获取
    if (editorElement && editorElement.value && isEditorElementInDOM()) {
      const instance = getEditorById(props.editorKey);
      
      if (instance) {
        editorInstance.value = instance;
        // 同步状态
        editorStatus.value = '已初始化';
        contentLength.value = instance.getContent().length;
        return instance;
      }
    }
    
    // 情况3: 实例不存在且元素不在DOM中或通过ID获取失败，尝试异步刷新
    setTimeout(() => {
      refreshEditor().then(success => {
        console.log(`异步刷新编辑器 ${props.editorKey} ${success ? '成功' : '失败'}`);
      });
    }, 100);
    
    return null;
  },
  
  // 提供一个刷新编辑器的公共方法
  refreshEditor: () => refreshEditor()
})


</script>

<style scoped>
.tinymce-editor {
  overflow: hidden;
  position: relative;
}

.editor-element {
  min-height: 400px;
  /* 确保编辑器容器可见 */
  background-color: #ffffff !important;
}


/* 确保TinyMCE编辑器内部元素可见 */
:deep(.tox-edit-area) {
  background-color: #ffffff !important;
}

:deep(.tox-edit-area__iframe) {
  background-color: #ffffff !important;
}
</style>
// editor-instance-manager.js
class EditorInstanceManager {
  constructor() {
    this.editorInstances = new Map()
    this.tinymceSource = null
  }

  // 注册 tinymce 来源
  registerTinyMCESource(source) {
    this.tinymceSource = source
    console.log('TinyMCE 来源已注册:', source)
  }

  // 获取 tinymce 实例
  getTinyMCE() {
    // 优先使用注册的来源
    if (this.tinymceSource) {
      return this.tinymceSource
    }
    
    // 回退到全局查找
    if (window.tinymce) {
      return window.tinymce
    } else if (window.parent?.tinymce) {
      return window.parent.tinymce
    } else if (window.top?.tinymce) {
      return window.top.tinymce
    }
    
    return null
  }

  // 获取指定编辑器实例
  getEditorInstance(editorId) {
    // 先从缓存中获取
    if (this.editorInstances.has(editorId)) {
      const instance = this.editorInstances.get(editorId)
      if (this.isEditorValid(instance)) {
        return instance
      } else {
        // 实例无效，从缓存中移除
        this.editorInstances.delete(editorId)
      }
    }

    // 从 tinymce 中查找
    const tinymce = this.getTinyMCE()
    if (!tinymce) return null

    let editor = null
    
    // 多种方式查找编辑器
    if (typeof tinymce.get === 'function') {
      editor = tinymce.get(editorId)
    } else if (tinymce.editors && tinymce.editors[editorId]) {
      editor = tinymce.editors[editorId]
    } else if (tinymce.activeEditor && tinymce.activeEditor.id === editorId) {
      editor = tinymce.activeEditor
    }

    if (editor && this.isEditorValid(editor)) {
      this.editorInstances.set(editorId, editor)
      return editor
    }

    return null
  }

  // 检查编辑器实例是否有效
  isEditorValid(editor) {
    if (!editor) return false
    
    try {
      // 检查编辑器基本功能
      return (
        typeof editor.getContent === 'function' &&
        typeof editor.setContent === 'function' &&
        typeof editor.focus === 'function' &&
        editor.initialized !== false
      )
    } catch (error) {
      return false
    }
  }

  // 注册编辑器实例
  registerEditorInstance(editorId, editorInstance) {
    if (this.isEditorValid(editorInstance)) {
      this.editorInstances.set(editorId, editorInstance)
      console.log(`编辑器实例 ${editorId} 已注册`)
      return true
    }
    return false
  }

  // 注销编辑器实例
  unregisterEditorInstance(editorId) {
    if (this.editorInstances.has(editorId)) {
      this.editorInstances.delete(editorId)
      console.log(`编辑器实例 ${editorId} 已注销`)
    }
  }

  // 重新连接所有实例
  reconnectAllInstances() {
    const reconnected = []
    
    this.editorInstances.forEach((instance, editorId) => {
      if (!this.isEditorValid(instance)) {
        const newInstance = this.getEditorInstance(editorId)
        if (newInstance) {
          this.editorInstances.set(editorId, newInstance)
          reconnected.push(editorId)
        } else {
          this.editorInstances.delete(editorId)
        }
      }
    })
    
    console.log(`重新连接了 ${reconnected.length} 个编辑器实例:`, reconnected)
    return reconnected
  }

  // 获取所有有效的实例
  getAllValidInstances() {
    const validInstances = {}
    
    this.editorInstances.forEach((instance, editorId) => {
      if (this.isEditorValid(instance)) {
        validInstances[editorId] = instance
      }
    })
    
    return validInstances
  }

  // 清理所有无效实例
  cleanupInvalidInstances() {
    let cleanedCount = 0
    
    this.editorInstances.forEach((instance, editorId) => {
      if (!this.isEditorValid(instance)) {
        this.editorInstances.delete(editorId)
        cleanedCount++
      }
    })
    
    console.log(`清理了 ${cleanedCount} 个无效的编辑器实例`)
    return cleanedCount
  }
}

// 创建单例
export const editorInstanceManager = new EditorInstanceManager()
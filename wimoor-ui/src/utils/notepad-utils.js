// 笔记工具函数，用于处理笔记的CRUD操作
import { ElMessage } from 'element-plus'

/**
 * 笔记数据存储键名
 */
const NOTEPAD_STORAGE_KEY = 'notepad-notes'

/**
 * 从本地存储获取所有笔记
 * @returns {Array} 笔记列表
 */
export const getAllNotes = () => {
  try {
    const notes = localStorage.getItem(NOTEPAD_STORAGE_KEY)
    return notes ? JSON.parse(notes) : []
  } catch (error) {
    console.error('获取笔记列表失败:', error)
    ElMessage.error('获取笔记列表失败')
    return []
  }
}

/**
 * 获取指定ID的笔记
 * @param {string} noteId - 笔记ID
 * @returns {Object|null} 笔记对象或null
 */
export const getNoteById = (noteId) => {
  try {
    const notes = getAllNotes()
    return notes.find(note => note.id === noteId) || null
  } catch (error) {
    console.error('获取笔记失败:', error)
    ElMessage.error('获取笔记失败')
    return null
  }
}

/**
 * 保存笔记到本地存储
 * @param {Object} noteData - 笔记数据对象
 * @param {string} [noteData.id] - 笔记ID（可选，不提供则自动生成）
 * @param {string} noteData.title - 笔记标题
 * @param {string} noteData.content - 笔记内容
 * @param {string} [noteData.format] - 内容格式（html或markdown）
 * @returns {Object} 保存后的笔记对象
 */
export const saveNote = (noteData) => {
  try {
    const notes = getAllNotes()
    const now = new Date().toLocaleString('zh-CN')
    
    // 准备笔记数据
    const noteToSave = {
      id: noteData.id || Date.now().toString(),
      title: noteData.title || '未命名笔记',
      content: noteData.content || '',
      format: noteData.format || 'html', // 默认为HTML格式
      updatedAt: now,
      createdAt: noteData.createdAt || now
    }
    
    // 查找并更新现有笔记或添加新笔记
    const existingIndex = notes.findIndex(note => note.id === noteToSave.id)
    if (existingIndex >= 0) {
      notes[existingIndex] = noteToSave
    } else {
      notes.unshift(noteToSave) // 添加到列表开头
    }
    
    // 保存到本地存储
    localStorage.setItem(NOTEPAD_STORAGE_KEY, JSON.stringify(notes))
    
    return noteToSave
  } catch (error) {
    console.error('保存笔记失败:', error)
    ElMessage.error('保存笔记失败')
    throw error
  }
}

/**
 * 删除笔记
 * @param {string} noteId - 要删除的笔记ID
 * @returns {boolean} 删除是否成功
 */
export const deleteNote = (noteId) => {
  try {
    const notes = getAllNotes()
    const filteredNotes = notes.filter(note => note.id !== noteId)
    
    if (filteredNotes.length === notes.length) {
      ElMessage.warning('未找到要删除的笔记')
      return false
    }
    
    localStorage.setItem(NOTEPAD_STORAGE_KEY, JSON.stringify(filteredNotes))
    ElMessage.success('笔记删除成功')
    return true
  } catch (error) {
    console.error('删除笔记失败:', error)
    ElMessage.error('删除笔记失败')
    return false
  }
}

/**
 * 清空所有笔记
 * @returns {boolean} 清空是否成功
 */
export const clearAllNotes = () => {
  try {
    if (confirm('确定要清空所有笔记吗？此操作不可恢复！')) {
      localStorage.removeItem(NOTEPAD_STORAGE_KEY)
      ElMessage.success('所有笔记已清空')
      return true
    }
    return false
  } catch (error) {
    console.error('清空笔记失败:', error)
    ElMessage.error('清空笔记失败')
    return false
  }
}

/**
 * 搜索笔记
 * @param {string} keyword - 搜索关键词
 * @returns {Array} 符合条件的笔记列表
 */
export const searchNotes = (keyword) => {
  try {
    if (!keyword.trim()) {
      return getAllNotes()
    }
    
    const notes = getAllNotes()
    const lowerKeyword = keyword.toLowerCase()
    
    return notes.filter(note => 
      note.title.toLowerCase().includes(lowerKeyword) || 
      note.content.toLowerCase().includes(lowerKeyword)
    )
  } catch (error) {
    console.error('搜索笔记失败:', error)
    ElMessage.error('搜索笔记失败')
    return getAllNotes()
  }
}

/**
 * 导出笔记数据
 * @returns {string} 笔记数据的JSON字符串
 */
export const exportNotes = () => {
  try {
    const notes = getAllNotes()
    return JSON.stringify(notes, null, 2)
  } catch (error) {
    console.error('导出笔记失败:', error)
    ElMessage.error('导出笔记失败')
    return '[]'
  }
}

/**
 * 导入笔记数据
 * @param {string} jsonData - 笔记数据的JSON字符串
 * @returns {boolean} 导入是否成功
 */
export const importNotes = (jsonData) => {
  try {
    if (!jsonData.trim()) {
      ElMessage.warning('导入数据不能为空')
      return false
    }
    
    const importedNotes = JSON.parse(jsonData)
    
    // 验证导入数据格式
    if (!Array.isArray(importedNotes)) {
      ElMessage.error('导入数据格式不正确')
      return false
    }
    
    // 保存导入的笔记
    localStorage.setItem(NOTEPAD_STORAGE_KEY, JSON.stringify(importedNotes))
    ElMessage.success(`成功导入 ${importedNotes.length} 条笔记`)
    return true
  } catch (error) {
    console.error('导入笔记失败:', error)
    ElMessage.error('导入笔记失败，请检查数据格式')
    return false
  }
}

/**
 * 生成Markdown格式的笔记内容
 * @param {Object} note - 笔记对象
 * @returns {string} Markdown格式的笔记内容
 */
export const generateMarkdownContent = (note) => {
  if (!note) return ''
  
  let markdown = `# ${note.title}\n\n`
  markdown += `> 更新时间: ${note.updatedAt}\n\n`
  markdown += note.content || ''
  
  return markdown
}

/**
 * 下载笔记为Markdown文件
 * @param {Object} note - 笔记对象
 */
export const downloadNoteAsMarkdown = (note) => {
  try {
    const markdownContent = generateMarkdownContent(note)
    const blob = new Blob([markdownContent], { type: 'text/markdown;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    
    const a = document.createElement('a')
    a.href = url
    a.download = `${note.title.replace(/[\\/:*?"<>|]/g, '-')}.md`
    document.body.appendChild(a)
    a.click()
    
    // 清理
    document.body.removeChild(a)
    URL.revokeObjectURL(url)
    
    ElMessage.success('笔记下载成功')
  } catch (error) {
    console.error('下载笔记失败:', error)
    ElMessage.error('下载笔记失败')
  }
}
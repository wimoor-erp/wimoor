import { marked } from 'marked';
import { markedHighlight } from "marked-highlight"; 
import hljs from 'highlight.js';
 // 导入默认的亮色主题作为基础
// 配置 marked-highlight
// 定义可用主题及其对应的 CSS 导入函数
const THEMES = {
  light: () => import('highlight.js/styles/github.css'),
  dark: () => import('highlight.js/styles/night-owl.css')
};
  // 动态加载主题样式
 export const loadTheme = async (themeName) => {
    try {
      // 调用该主题对应的导入函数
      await THEMES[themeName]();
      // 将主题选择存储到浏览器缓存中
      localStorage.setItem('hljs-theme', themeName);
    } catch (error) {
      console.error(`Failed to load highlight.js theme: ${themeName}`, error);
    }
  };

marked.use(markedHighlight({
  langPrefix: 'language-', // 类名前缀
  highlight(code, lang) {
    const language = hljs.getLanguage(lang) ? lang : 'plaintext';
    return hljs.highlight(code, { language }).value;
  }
}));
// 设置 marked 选项
marked.setOptions({
  gfm: true, // 启用 GitHub 风格的 Markdown
  // ... 其他选项
});

// 安全地渲染 Markdown（不分离代码块）
export function renderMarkdown(content) {
  return marked.parse(content);
}

// 添加复制功能
export const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch (err) {
    console.error('复制失败:', err);
    return false;
  }
};
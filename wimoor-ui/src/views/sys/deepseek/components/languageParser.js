// utils/languageParser.js
// 支持的语言类型映射
export const LANGUAGE_MAPPING = {
  // 前端开发
  'javascript': 'JavaScript',
  'js': 'JavaScript',
  'typescript': 'TypeScript',
  'ts': 'TypeScript',
  'html': 'HTML',
  'css': 'CSS',
  'sass': 'SASS',
  'scss': 'SCSS',
  'less': 'Less',
  
  // 后端开发
  'python': 'Python',
  'py': 'Python',
  'java': 'Java',
  'php': 'PHP',
  'ruby': 'Ruby',
  'rb': 'Ruby',
  'go': 'Go',
  'golang': 'Go',
  'rust': 'Rust',
  'rs': 'Rust',
  'c': 'C',
  'cpp': 'C++',
  'c++': 'C++',
  'csharp': 'C#',
  'cs': 'C#',
  'swift': 'Swift',
  'kotlin': 'Kotlin',
  'kt': 'Kotlin',
  'scala': 'Scala',
  
  // 脚本语言
  'bash': 'Bash',
  'shell': 'Shell',
  'sh': 'Shell',
  'powershell': 'PowerShell',
  'ps1': 'PowerShell',
  'sql': 'SQL',
  'mysql': 'MySQL',
  'postgresql': 'PostgreSQL',
  'plsql': 'PL/SQL',
  
  // 配置和标记语言
  'json': 'JSON',
  'yaml': 'YAML',
  'yml': 'YAML',
  'xml': 'XML',
  'markdown': 'Markdown',
  'md': 'Markdown',
  'toml': 'TOML',
  'ini': 'INI',
  'dockerfile': 'Dockerfile',
  
  // 框架相关
  'vue': 'Vue',
  'react': 'React',
  'angular': 'Angular',
  'dart': 'Dart',
  'flutter': 'Flutter',
  
  // 其他
  'text': 'Text',
  'plaintext': 'Plain Text',
  'diff': 'Diff',
  'log': 'Log'
};

// 语言别名映射
export const LANGUAGE_ALIASES = {
  'js': 'javascript',
  'ts': 'typescript',
  'py': 'python',
  'rb': 'ruby',
  'rs': 'rust',
  'cpp': 'c++',
  'cs': 'csharp',
  'kt': 'kotlin',
  'sh': 'bash',
  'ps1': 'powershell',
  'yml': 'yaml',
  'vue': 'vue',
  'md': 'markdown'
};

// 获取规范化的语言名称
export function getNormalizedLanguage(lang) {
  if (!lang) return 'text';
  
  const lowerLang = lang.toLowerCase().trim();
  
  // 首先检查别名映射
  const aliasedLang = LANGUAGE_ALIASES[lowerLang] || lowerLang;
  
  // 返回可读的语言名称，如果不存在则返回原始值
  return LANGUAGE_MAPPING[aliasedLang] || capitalizeFirstLetter(aliasedLang);
}

// 从代码内容推断语言
export function detectLanguageFromCode(code) {
  if (!code) return 'text';
  
  const firstLine = code.split('\n')[0].trim();
  
  // 检查文件头注释
  if (firstLine.includes('#!/usr/bin/env')) {
    if (firstLine.includes('python')) return 'python';
    if (firstLine.includes('node')) return 'javascript';
    if (firstLine.includes('bash')) return 'bash';
    if (firstLine.includes('ruby')) return 'ruby';
  }
  
  // 检查常见语法模式
  if (code.includes('<?php')) return 'php';
  if (code.includes('<?=')) return 'php';
  if (code.includes('import React')) return 'javascript';
  if (code.includes('from flask import')) return 'python';
  if (code.includes('package main')) return 'go';
  if (code.includes('fn main()')) return 'rust';
  if (code.includes('public class')) return 'java';
  if (code.includes('using System')) return 'csharp';
  if (code.includes('<html')) return 'html';
  if (code.includes('SELECT')) return 'sql';
  if (code.includes('CREATE TABLE')) return 'sql';
  
  return 'text';
}

// 从代码块标记中提取语言
export function extractLanguageFromMarkdown(codeBlock) {
  const langMatch = codeBlock.match(/^```(\w+)/);
  return langMatch ? langMatch[1] : null;
}

// 获取语言图标
export function getLanguageIcon(lang) {
  const normalizedLang = getNormalizedLanguage(lang).toLowerCase();
  
  const iconMap = {
    'javascript': '🟨',
    'typescript': '🔷',
	'vue': '🔷',
    'python': '🐍',
    'java': '☕',
    'html': '🌐',
    'css': '🎨',
    'php': '🐘',
    'ruby': '💎',
    'go': '🐹',
    'rust': '🦀',
    'c++': '🔷',
    'csharp': 'C#',
    'swift': '🐦',
    'kotlin': 'K',
    'bash': '💻',
    'sql': '🗃️',
    'json': '{}',
    'yaml': 'YAML',
    'xml': 'XML',
    'markdown': '📝',
    'dockerfile': '🐳'
  };
  
  return iconMap[normalizedLang] || '📄';
}

// 获取语言颜色
export function getLanguageColor(lang) {
  const normalizedLang = getNormalizedLanguage(lang).toLowerCase();
  
  const colorMap = {
    'javascript': '#f7df1e',
    'typescript': '#3178c6',
    'python': '#3776ab',
    'java': '#007396',
    'html': '#e34f26',
    'css': '#1572b6',
    'php': '#777bb4',
    'ruby': '#cc342d',
    'go': '#00add8',
    'rust': '#000000',
    'c++': '#00599c',
    'csharp': '#239120',
    'swift': '#fa7343',
    'kotlin': '#7f52ff',
    'bash': '#4eaa25',
    'sql': '#336791',
    'json': '#ffffff',
    'yaml': '#cb171e'
  };
  
  return colorMap[normalizedLang] || '#6c757d';
}

// 工具函数
function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

// 检查是否支持该语言的高亮
export function isLanguageSupported(lang) {
  const normalizedLang = getNormalizedLanguage(lang).toLowerCase();
  return !!hljs.getLanguage(normalizedLang);
}
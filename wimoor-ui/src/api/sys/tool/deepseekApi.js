import request from "@/utils/request.js";
import { getToken } from '@/utils/auth'

function search(data){
	 return request.post('/admin/api/v1/deepseek/search',data);
}

function getSession(){
	 return request.get('/admin/api/v1/deepseek/getSession');
}

function deleteSession(sessionId){
	 return request.delete(`/admin/api/v1/deepseek/deleteSession/${sessionId}`);
}

function getKey(){
	 return request.get('/admin/api/v1/deepseek/getKey');
}

// 流式请求方法
async function searchStream(data, onMessage, onError, onComplete) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || '';
  const url = `${baseURL}/admin/api/v1/deepseek/search/stream`;
  
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'jsessionid': localStorage.getItem('jsessionid') || ''
      },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    let buffer = '';
    let sessionId = null;
    let chunkCount = 0;

    while (true) {
      const { done, value } = await reader.read();
      
      if (done) {
        if (onComplete) onComplete(sessionId);
        break;
      }

      chunkCount++;
      buffer += decoder.decode(value, { stream: true });
      
      // 处理SSE格式的数据
      const lines = buffer.split('\n');
      buffer = lines.pop() || ''; // 保留未完成的行
      
      for (const line of lines) {
        const trimmedLine = line.trim();
        
        // 跳过空行和id行
        if (!trimmedLine || trimmedLine.startsWith('id:')) {
          continue;
        }
        
        if (trimmedLine.startsWith('data:')) {
          const content = trimmedLine.slice(5).trim();
          
          // 跳过空数据
          if (!content) continue;
          
          try {
            let parsed = JSON.parse(content);
            
            // 如果 parsed 是字符串，说明是双重编码，需要再次解析
            if (typeof parsed === 'string') {
              parsed = JSON.parse(parsed);
            }
            
            // 检查是否是完成标记
            if (parsed.content === '[DONE]') {
              // 保存sessionId
              if (parsed.id) {
                sessionId = parsed.id;
              }
              if (onComplete) onComplete(sessionId);
              return;
            }
            
            // 保存sessionId
            if (parsed.id) {
              sessionId = parsed.id;
            }
            
            // 发送消息
            if (onMessage) onMessage(parsed);
          } catch (e) {
            // 如果不是JSON，检查是否是完成标记
            if (content === '[DONE]') {
              if (onComplete) onComplete(sessionId);
              return;
            }
            // 否则直接传递内容
            if (onMessage) onMessage({ content: content });
          }
        }
      }
    }
  } catch (error) {
    console.error('流式请求错误:', error);
    if (onError) onError(error);
  }
}

export default{
	 search, getSession, deleteSession, getKey, searchStream
}
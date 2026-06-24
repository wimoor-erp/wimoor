package com.wimoor.feishu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class FeiShuUrlParser {

    // 飞书开放平台获取 tenant_access_token 的接口
    private static final String TENANT_ACCESS_TOKEN_URL = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";
    // 飞书开放平台获取知识库节点信息的接口
    private static final String WIKI_NODE_INFO_URL = "https://open.feishu.cn/open-apis/wiki/v2/spaces/get_node";

    // 需要配置你的飞书应用凭证
    private static final String APP_ID = "your_app_id";
    private static final String APP_SECRET = "your_app_secret";

    private final ObjectMapper objectMapper;

    public FeiShuUrlParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 从飞书多维表格 URL 中解析出 appToken, tableId, viewId
     * @param urlString 飞书多维表格的完整 URL
     * @return Map 包含键 appToken, tableId, viewId
     * @throws Exception 解析或API调用失败时抛出
     */
    public Map<String, String> parseFeiShuTableUrl(String urlString) throws Exception {
        URI uri = new URI(urlString);
        Map<String, String> result = new HashMap<>();
        // 1. 解析 tableId 和 viewId（两种格式都相同）
        String query = uri.getQuery();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int eqIdx = pair.indexOf('=');
                if (eqIdx == -1) continue;
                String key = pair.substring(0, eqIdx);
                String value = pair.substring(eqIdx + 1);
                if ("table".equals(key)) {
                    result.put("tableId", value);
                } else if ("view".equals(key)) {
                    result.put("viewId", value);
                }
            }
        }

        if (!result.containsKey("tableId")) {
            throw new IllegalArgumentException("URL 中缺少 table 参数");
        }

        // 2. 根据路径格式解析 appToken
        String path = uri.getPath();
        if (path == null) {
            throw new IllegalArgumentException("URL 路径不能为空");
        }

        if (path.startsWith("/base/")) {
            // /base/ 格式：直接提取
            String appToken = path.substring("/base/".length());
            int slashIdx = appToken.indexOf('/');
            if (slashIdx != -1) {
                appToken = appToken.substring(0, slashIdx);
            }
            result.put("appToken", appToken);
        } else if (path.startsWith("/wiki/")) {
            // /wiki/ 格式：需要 API 转换
            String nodeToken = path.substring("/wiki/".length());
            int slashIdx = nodeToken.indexOf('/');
            if (slashIdx != -1) {
                nodeToken = nodeToken.substring(0, slashIdx);
            }
            result.put("appToken", nodeToken);
        } else {
            throw new IllegalArgumentException("不支持的 URL 格式，请提供 /base/ 或 /wiki/ 开头的链接");
        }

        return result;
    }


}

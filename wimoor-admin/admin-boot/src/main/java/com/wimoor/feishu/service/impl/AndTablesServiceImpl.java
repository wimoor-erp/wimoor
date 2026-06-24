package com.wimoor.feishu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParser;
import com.lark.oapi.Client;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.bitable.v1.model.*;
import com.wimoor.common.mvc.BizException;
import com.wimoor.feishu.config.FeiShuClientBuilder;
import com.wimoor.feishu.pojo.entity.*;
import com.wimoor.feishu.service.IAndTablesService;
import com.wimoor.feishu.service.ISysFeishuTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AndTablesServiceImpl implements IAndTablesService {
    @Autowired
    FeiShuClientBuilder clientBuilder;
    
    @Autowired
    ISysFeishuTableService sysFeishuTableService;

    /**
     * 创建多维表格同时添加数据表，使用到两个OpenAPI：
     * 1. [创建多维表格](<a href="/ssl:ttdoc/server-docs/docs/bitable-v1/app/create">...</a>)
     * 2. [新增一个数据表](<a href="/ssl:ttdoc/server-docs/docs/bitable-v1/app-table/create">...</a>)
     */
    public static BaseResponse<?> CreateAppAndTables(Client client, CreateAppAndTablesRequest request) throws Exception {
        // 创建多维表格
        CreateAppReq createAppReq = CreateAppReq.newBuilder()
                .reqApp(ReqApp.newBuilder()
                        .name(request.getName())
                        .folderToken(request.getFolderToken())
                        .build())
                .build();

        CreateAppResp createAppResp = client.bitable().app().create(createAppReq);
        if (!createAppResp.success()) {
            System.out.printf("client.bitable.app.create failed, code: %d, msg: %s, logId: %s%n",
                    createAppResp.getCode(), createAppResp.getMsg(), createAppResp.getRequestId());
            return createAppResp;
        }
        // 添加数据表
        List<CreateAppTableRespBody> tables = new ArrayList<>();
        for (ReqTable table : request.getTables()) {
            CreateAppTableReq createAppTableReq = CreateAppTableReq.newBuilder()
                    .appToken(createAppResp.getData().getApp().getAppToken())
                    .createAppTableReqBody(CreateAppTableReqBody.newBuilder().table(table).build())
                    .build();
            CreateAppTableResp createAppTableResp = client.bitable().appTable().create(createAppTableReq);
            if (!createAppTableResp.success()) {
                System.out.printf("client.bitable.appTable.create failed, code: %d, msg: %s, logId: %s%n",
                        createAppTableResp.getCode(), createAppTableResp.getMsg(), createAppTableResp.getRequestId());
                return createAppTableResp;
            }
            tables.add(createAppTableResp.getData());
        }
        // 返回结果
        CreateAppAndTablesResponse response = new CreateAppAndTablesResponse();
        response.setCode(0);
        response.setMsg("success");
        response.setCreateAppResponse(createAppResp.getData());
        response.setCreateAppTableResponse(tables);
        return response;
    }

    public static RawResponse CopyAndPasteByRange(Client client, CopyAndPasteByRangeRequest request) throws Exception {
        // 读取单个范围
        RawResponse readResp = client.get(
                String.format("/open-apis/sheets/v2/spreadsheets/%s/values/%s", request.getSpreadsheetToken(), request.getSrcRange()),
                null,
                AccessTokenType.Tenant);
        if (readResp.getStatusCode()==200) {
            System.out.printf("read spreadsheet failed, code: %d, msg: %s, logId: %s%n",
                    readResp.getStatusCode(), readResp.getBody(), readResp.getRequestID());
            return readResp;
        }
        // 向单个范围写入数据
        Map<String, Object> valueRange = new HashMap<>();
        JSONObject respone= JSON.parseObject(new String(readResp.getBody()));
        valueRange.put("range",respone.getString("range"));
        valueRange.put("values", respone.getJSONArray("values"));
        Map<String, Object> body = new HashMap<>();
        body.put("valueRange", valueRange);
        RawResponse writeResp = client.put(
                String.format("/open-apis/sheets/v2/spreadsheets/%s/values", request.getSpreadsheetToken()),
                body,
                AccessTokenType.Tenant);
        if (writeResp.getStatusCode()==200) {
            System.out.printf("write spreadsheet failed, code: %d, msg: %s, logId: %s%n",
                    writeResp.getStatusCode(), writeResp.getBody(), writeResp.getRequestID());
            return writeResp;
        }
      return writeResp;
    }

    public   void getTable(Client client) throws Exception {
        // 创建请求对象
        GetAppReq req = GetAppReq.newBuilder()
                .appToken("I6vObyPD0apgBGs5fyrcI9PonJh")
                .build();
        // 发起请求
        GetAppResp resp = client.bitable().v1().app().get(req);

        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                    resp.getCode(), resp.getMsg(),
                    resp.getRequestId(),
                    Jsons.createGSON(true, false).toJson(
                            JsonParser.parseString(new String(resp.getRawResponse().getBody(),
                                    StandardCharsets.UTF_8)))));
            return;
        }
        // 业务数据处理
        System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
    }



    public SysFeishuTable getFields(Auth auth, String url, String name, String operator)  {
        Client client = clientBuilder.getClient(auth.getAppId());
        // 创建请求对象
        FeiShuUrlParser parser = new FeiShuUrlParser();
        Map<String, String> result = null;
        try {
            result = parser.parseFeiShuTableUrl(url);
            ListAppTableFieldReq req = ListAppTableFieldReq.newBuilder()
                    .appToken(result.get("appToken"))
                    .tableId(result.get("tableId"))
                    .viewId(result.get("viewId"))
                    .pageSize(100)
                    .build();
            // 发起请求
            ListAppTableFieldResp resp = client.bitable().v1().appTableField().list(req);
            // 处理服务端错误
            if (!resp.success()) {
                System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))));
                return null;
            }
            String jsonResult = Jsons.DEFAULT.toJson(resp.getData());
            
            // 检查是否已存在相同的记录
            SysFeishuTable existingRecord = sysFeishuTableService.lambdaQuery()
                    .eq(SysFeishuTable::getAppid, auth.getAppId())
                    .eq(SysFeishuTable::getApptoken, result.get("appToken"))
                    .eq(SysFeishuTable::getTableid, result.get("tableId"))
                    .eq(SysFeishuTable::getViewid, result.get("viewId"))
                    .one();
            
            if (existingRecord != null) {
                // 更新现有记录
                existingRecord.setFieldjson(jsonResult);
                existingRecord.setOpttime(new java.util.Date());
                existingRecord.setUrl(url);
                existingRecord.setDisabled(false);
                existingRecord.setName(name);
                existingRecord.setOperator(new BigInteger(operator));
                existingRecord.setPagenum("1");
                existingRecord.setPagesize("10");
                setPrimaryKey(existingRecord,jsonResult);
                sysFeishuTableService.updateById(existingRecord);
                return existingRecord;
            } else {
                // 插入新记录
                SysFeishuTable sysFeishuTable = new SysFeishuTable();
                sysFeishuTable.setAppid(auth.getAppId());
                sysFeishuTable.setApptoken(result.get("appToken"));
                sysFeishuTable.setTableid(result.get("tableId"));
                sysFeishuTable.setViewid(result.get("viewId"));
                sysFeishuTable.setFieldjson(jsonResult);
                sysFeishuTable.setUrl(url);
                sysFeishuTable.setOperator(new BigInteger(operator));
                sysFeishuTable.setName(name);
                sysFeishuTable.setPagenum("1");
                sysFeishuTable.setDisabled(false);
                sysFeishuTable.setPagesize("10");
                sysFeishuTable.setOpttime(new java.util.Date());
                setPrimaryKey(sysFeishuTable,jsonResult);
                sysFeishuTableService.save(sysFeishuTable);
                return sysFeishuTable;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void setPrimaryKey(SysFeishuTable sysFeishuTable,String json){
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray items = jsonObject.getJSONArray("items");
            if(items!=null && items.size()>0){
                for(int i=0;i<items.size();i++){
                    JSONObject item = items.getJSONObject(i);
                    Boolean is_primary = item.getBoolean("is_primary");
                    if(is_primary!=null&&is_primary){
                        sysFeishuTable.setFieldkey(item.getString("field_id")); // Changed from setFieldkey to setFieldKey
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void create(Auth auth, String title) {
        // 应用和表格管理

    }
    public String getTableInfo(Auth auth, String url) {
        Client client = clientBuilder.getClient(auth.getAppId());
        FeiShuUrlParser parser = new FeiShuUrlParser();
        // 创建请求对象
        Map<String, String> result = null;
        try {
            result = parser.parseFeiShuTableUrl(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 创建请求对象
        ListAppTableReq req = ListAppTableReq.newBuilder()
                .appToken(result.get("appToken"))
                .pageSize(100)
                .build();

        // 发起请求
        ListAppTableResp resp = null;
        try {
              resp = client.bitable().v1().appTable().list(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 处理服务端错误
        if(resp==null){
            return null;
        }
        if (resp!=null&&!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                    resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))));
            return null;
        }
        AppTable[] items = resp.getData().getItems();
        if (items == null || items.length == 0) {
            return null;
        }
        for (AppTable item : items) {
            if (item.getTableId().equals(result.get("tableId"))) {
                return item.getName();
            }
        }
        // 业务数据处理
        return Jsons.DEFAULT.toJson(resp.getData());
    }

    @Override
    public String getRecord(Auth auth, String url, String filter) {
        Client client = clientBuilder.getClient(auth.getAppId());
        // 创建请求对象
        FeiShuUrlParser parser = new FeiShuUrlParser();
        Map<String, String> result = null;
        try {
            result = parser.parseFeiShuTableUrl(url);
            SearchAppTableRecordReq.Builder reqBuilder =SearchAppTableRecordReq.newBuilder()
                    .appToken(result.get("appToken"))
                    .tableId(result.get("tableId"))
                    .pageSize(100);
            
            // 如果有 filter 参数，添加到请求中
            if (filter != null && !filter.isEmpty()) {
                String filterJson = filter;
                SearchAppTableRecordReqBody.Builder reqBodyBuilder = SearchAppTableRecordReqBody.newBuilder();
                // 解析 filter，判断是公式模式还是条件模式
                try {
                    com.alibaba.fastjson.JSONObject filterObj = com.alibaba.fastjson.JSON.parseObject(filterJson);
                    filterObj.remove("mode");
                    // 条件模式：需要包装成 {"filter": {...}} 格式
                    if (filterJson.startsWith("{\"filter\":")) {
                        // 如果有外层包装，获取内层filter对象
                        filterObj = filterObj.getJSONObject("filter");
                    }
                    
                    // 将 JSONObject 转换为 FilterInfo 对象
                    FilterInfo info = convertJsonToFilterInfo(filterObj);
                    
                    reqBodyBuilder.filter(info);
                    reqBuilder.searchAppTableRecordReqBody(reqBodyBuilder.build());
                } catch (Exception e) {
                    // 如果解析失败，尝试直接作为公式处理
                     throw new BizException("无法解析公式结构");
                }
            }
             SearchAppTableRecordReq req = reqBuilder.build();
            // 发起请求
             SearchAppTableRecordResp resp = client.bitable().v1().appTableRecord().search(req);
            // 处理服务端错误
            if (!resp.success()) {
                System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))));
                return null;
            }
            return Jsons.DEFAULT.toJson(resp.getData());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSONObject 转换为 FilterInfo 对象
     */
    private FilterInfo convertJsonToFilterInfo(JSONObject filterObj) {
        FilterInfo.Builder infoBuilder = FilterInfo.newBuilder();
        
        // 设置 conjunction（AND/OR）
        if (filterObj.containsKey("conjunction")) {
            infoBuilder.conjunction(filterObj.getString("conjunction"));
        }
        
        // 处理 conditions 数组
        if (filterObj.containsKey("conditions") && filterObj.get("conditions") instanceof JSONArray) {
            JSONArray conditionsArray = filterObj.getJSONArray("conditions");
            Condition[] conditions = new Condition[conditionsArray.size()];
            for (int i = 0; i < conditionsArray.size(); i++) {
                JSONObject conditionObj = conditionsArray.getJSONObject(i);
                conditions[i] = convertJsonToCondition(conditionObj);
            }
            infoBuilder.conditions(conditions);
        }
        
        return infoBuilder.build();
    }

    /**
     * 将 JSONObject 转换为 Condition 对象
     */
    private Condition convertJsonToCondition(JSONObject conditionObj) {
        Condition.Builder conditionBuilder = Condition.newBuilder();
        
        // 设置字段名
        if (conditionObj.containsKey("field_name")) {
            conditionBuilder.fieldName(conditionObj.getString("field_name"));
        }
        
        // 设置操作符
        if (conditionObj.containsKey("operator")) {
            conditionBuilder.operator(conditionObj.getString("operator"));
        }
        
        // 设置值
        if (conditionObj.containsKey("value")) {
            Object value = conditionObj.get("value");
            if (value instanceof JSONArray) {
                JSONArray valueArray = (JSONArray) value;
                String[] valueArrayResult = new String[valueArray.size()];
                for (int i = 0; i < valueArray.size(); i++) {
                    valueArrayResult[i] = valueArray.getString(i);
                }
                conditionBuilder.value(valueArrayResult);
            } else {
                conditionBuilder.value(new String[]{value.toString()});
            }
        }
        
        return conditionBuilder.build();
    }

    @Override
    public String updateCallback(Auth auth, String tableId, List<String> fieldkeys, String feedbackValue) {
        Client client = clientBuilder.getClient(auth.getAppId());
        try {
            // 从数据库中查询表格信息
            SysFeishuTable tableInfo = sysFeishuTableService.getById(new BigInteger(tableId));
            
            if (tableInfo == null) {
                throw new RuntimeException("Table information not found for tableId: " + tableId);
            }
            
            // 构建批量更新请求
            List<AppTableRecord> records = new ArrayList<>();
            for (String fieldkey : fieldkeys) {
                // 自动生成fields，使用feedback字段值作为key，值设为"已同步"
                Map<String, Object> fields = new HashMap<>();
                if(StrUtil.isBlankOrUndefined(feedbackValue)){
                    fields.put(tableInfo.getFeedback(), "已同步");
                }else{
                    fields.put(tableInfo.getFeedback(), feedbackValue);
                }
                // 创建记录对象
                AppTableRecord record = AppTableRecord.newBuilder()
                        .recordId(fieldkey)
                        .fields(fields)
                        .build();
                records.add(record);
            }
            
            // 创建批量更新请求对象
            BatchUpdateAppTableRecordReq req = BatchUpdateAppTableRecordReq.newBuilder()
                    .appToken(tableInfo.getApptoken())
                    .tableId(tableInfo.getTableid())
                    .userIdType("open_id")
                    .ignoreConsistencyCheck(true)
                    .batchUpdateAppTableRecordReqBody(BatchUpdateAppTableRecordReqBody.newBuilder()
                            .records(records.toArray(new AppTableRecord[0]))
                            .build())
                    .build();
            
            // 发起批量更新请求
            BatchUpdateAppTableRecordResp resp = client.bitable().v1().appTableRecord().batchUpdate(req);

            // 处理服务端错误
            if (!resp.success()) {
                System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))));
                throw new RuntimeException("Failed to batch update records: " + resp.getMsg());
            }

            // 业务数据处理
            return new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addCallback(Auth auth, String tableId, String fieldName) {
        Client client = clientBuilder.getClient(auth.getAppId());
        try {
            // 从数据库中查询表格信息
            SysFeishuTable tableInfo = sysFeishuTableService.getById(new BigInteger(tableId));
            
            if (tableInfo == null) {
                throw new RuntimeException("Table information not found for tableId: " + tableId);
            }
            
            // 创建字段请求对象
            CreateAppTableFieldReq req = CreateAppTableFieldReq.newBuilder()
                    .appToken(tableInfo.getApptoken())
                    .tableId(tableInfo.getTableid())
                    .clientToken(UUID.randomUUID().toString())
                    .appTableField(AppTableField.newBuilder()
                            .fieldName(fieldName)
                            .type(1) // 文本类型
                            .build())
                    .build();
            
            // 发起请求
            CreateAppTableFieldResp resp = client.bitable().v1().appTableField().create(req);
            
            // 处理服务端错误
            if (!resp.success()) {
                System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                        resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))));
                throw new RuntimeException("Failed to create field: " + resp.getMsg());
            }
            
            // 更新t_sys_feishu_table的feedback字段
            tableInfo.setFeedback(fieldName);
            tableInfo.setOpttime(new Date());
            sysFeishuTableService.updateById(tableInfo);
            
            // 业务数据处理
            return Jsons.DEFAULT.toJson(resp.getData());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

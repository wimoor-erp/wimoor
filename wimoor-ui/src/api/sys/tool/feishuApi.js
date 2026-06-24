import request from "@/utils/request.js";

// 获取绑定信息
function getBindInfo() {
  return request({
    url: "/admin/api/v1/feishu/getBindInfo",
    method: "get"
  });
}

// 保存绑定信息
function save(data) {
  return request({
    url: "/admin/api/v1/feishu/save",
    method: "post",
    data
  });
}

// 更新绑定信息
function update(data) {
  return request({
    url: "/admin/api/v1/feishu/update",
    method: "post",
    data
  });
}
function createDocx(data) {
  return request({
    url: "/admin/api/v1/feishu/docx/create",
    method: "post",
    data
  });
}
function createAppAndTables(data) {
  return request({
    url: "/admin/api/v1/feishu/appAndTables/create",
    method: "post",
    data
  });
}

// 获取表格字段
function getTableFields(tableUrl, tableType) {
  return request({
    url: "/admin/api/feishu/table/getFields",
    method: "post",
    data: { url: tableUrl,name: tableType }
  });
}

// 获取表格列表
function getTableList(data) {
  return request({
    url: "/admin/api/feishu/table/list",
    method: "get",
    params: data
  });
}
// 更新数据表类型
function updateTable(data) {
  return request({
    url: "/admin/api/feishu/table/update",
    method: "put",
    data
  });
}
function getTableInfo(data) {
  return request({
    url: "/admin/api/feishu/table/getTableInfo",
    method: "post",
    data
  });
}

// 获取数据表类型列表
function getTypeList() {
  return request({
    url: "/admin/api/feishu/table/typeList",
    method: "get"
  });
}
function getRecord(params) {
  return request({
    url: "/admin/api/feishu/table/getRecord",
    method: "get",
    params: {
      url: params.url,
      // 直接传递内层条件对象，后端处理外层包装
      filter: params.filter ? JSON.stringify(params.filter) : undefined
    }
  });
}
function updateCallback(data){
  return request({
    url: "/admin/api/feishu/table/updateCallback",
    method: "post",
    data:data
  });
}
// 获取数据表类型列表
function addCallback(data) {
  return request({
    url: "/admin/api/feishu/table/addCallback",
    method: "post",
    data:data
  });
}

export default{
    getBindInfo,
    update,
    save,
    createDocx,
    createAppAndTables,
    getTableFields,
    getTableList,
    getTypeList,
    addCallback,
    getRecord,
    updateCallback,
    updateTable,
    getTableInfo,
}
# 物流报价设置页面详细帮助文档

## 1. 页面概述

`setting.vue` 是Wimoor ERP系统中物流模块下的报价设置页面，主要用于管理询价商和物流供应商信息。该页面实现了询价商令牌绑定、询价商信息管理以及物流供应商的增删改查功能，是物流报价流程的基础设置页面。

**页面路径**：`wimoor-ui/src/views/erp/shipv2/quote/setting.vue`
**技术栈**：Vue 3 + Element Plus + Spring Boot + MyBatis Plus

## 2. 前端功能模块详解

### 2.1 页面布局

页面采用左右分栏布局：
- **左侧（25%宽度）**：询价商管理模块
- **右侧（75%宽度）**：物流供应商管理模块

### 2.2 询价商管理模块

#### 2.2.1 状态显示与令牌管理

```html
<div v-if="token" style="padding-bottom:20px">
  <el-descriptions :column="1" border >
    <el-descriptions-item label="状态"><el-tag type="success">已绑定</el-tag> </el-descriptions-item>
    <el-descriptions-item label="令牌">  <span v-if="tokenname">({{tokenname}})</span> {{token}} 
              <copy class="" @click.stop="CopyText(token)" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/> </el-descriptions-item>
    <el-descriptions-item label="操作">  <el-button @click="unbindToken" link type="primary" >解绑</el-button></el-descriptions-item>
  </el-descriptions>
</div>
<div v-else style="padding-bottom:20px">
  <el-descriptions :column="1" border >
    <el-descriptions-item label="状态"> <el-tag type="danger">未绑定</el-tag> </el-descriptions-item>
    <el-descriptions-item label="令牌"> <el-input v-model="edittoken" placeholder="填写询价商Token"></el-input> </el-descriptions-item>
    <el-descriptions-item label="别名"> <el-input v-model="editname" placeholder="填写别名"></el-input> </el-descriptions-item>
    <el-descriptions-item label="操作"> <el-button @click="bindToken" type="primary" >绑定</el-button></el-descriptions-item>
  </el-descriptions>
</div>
```

**功能说明**：
- 根据是否绑定令牌显示不同的UI界面
- 已绑定状态：显示令牌信息、别名和解绑按钮
- 未绑定状态：提供输入框和绑定按钮
- 支持复制令牌功能

#### 2.2.2 询价商信息设置

```html
<el-collapse v-if="token" v-model="activeNames" @change="handleChange">
  <el-collapse-item title="高级" name="1">
    <el-form-item label="名称">
      <el-input v-model="buyer.name" :disabled="!buyer.edit" placeholder="填写供应商名称"></el-input>
    </el-form-item>
    <el-form-item label="地址">
      <el-input v-model="buyer.address" :disabled="!buyer.edit" placeholder="填写地址信息"></el-input>
    </el-form-item>
    <el-form-item label="手机号">
      <el-input v-model="buyer.mobile" :disabled="!buyer.edit" placeholder="填写手机号"></el-input>
    </el-form-item>
    <el-form-item label="联系人">
      <el-input v-model="buyer.contact" :disabled="!buyer.edit" placeholder="填写联系人"></el-input>
    </el-form-item>
    <div style=" margin-bottom:20px">
      <div v-if="token" >
        <el-button v-if="!buyer.edit" @click="buyer.edit=true" type="primary">修改</el-button>
        <el-button v-else @click="addBuyer" type="primary">保存</el-button>
      </div>
      <div v-else>
        <el-button @click="addBuyer" type="primary">新增</el-button>
      </div>
    </div>
  </el-collapse-item>
</el-collapse>
```

**功能说明**：
- 通过折叠面板展示询价商详细信息设置
- 支持编辑/保存模式切换
- 包含名称、地址、手机号、联系人四个字段

### 2.3 物流供应商管理模块

#### 2.3.1 供应商列表

```html
<el-table :data="tableData" height="calc(100vh - 145px)" >
  <el-table-column prop="name" label="名称" >
    <template #default="scope">
      <div>{{scope.row.name}}</div>
      <div class="font-extraSmall">{{scope.row.address}}</div>
    </template>
  </el-table-column>
  <el-table-column prop="contact" label="联系人" width="230" >
    <template #default="scope">
      <div>{{scope.row.contact}}</div>
      <div>{{scope.row.mobile}}</div>
    </template>
  </el-table-column>
  <el-table-column prop="createtime" label="链接" width="240" v-if="isowner" v-hasPerm="'erp:pi:supplier:link'" >
    <template #default="scope">
      <el-link type="success" :href="urlFormat(scope.row)" target="_blank" > <el-icon><Link /></el-icon> 供应商页面</el-link>
      <copy style="padding-left:10px" @click.stop="CopyText(urlFormat(scope.row))" title='复制SKU' theme="outline" size="14" fill="#666" :strokeWidth="3"/>
    </template>
  </el-table-column>
  <el-table-column prop="createtime" label="创建时间" width="200" >
    <template #default="scope">
      {{dateTimesFormat(scope.row.createtime)}} 
    </template>
  </el-table-column>
  <el-table-column prop="createtime" label="操作" width="180" >
    <template #default="scope">
      <el-button @click="editSupplier(scope.row)">编辑</el-button>
      <el-button type="danger" @click="delSupplier(scope.row.id)">删除</el-button>
    </template>
  </el-table-column>
</el-table>
```

**功能说明**：
- 表格展示供应商列表，包含名称、地址、联系人、联系电话等信息
- 根据用户权限（`isowner`和`v-hasPerm`）决定是否显示供应商页面链接
- 提供编辑和删除操作按钮

#### 2.3.2 供应商操作

```html
<template #header>
  <div class="card-header flex-between">
    <el-button @click="handleShow">新增</el-button>
    <div>物流供应商管理</div>
  </div>
</template>
```

**功能说明**：
- 新增按钮：打开创建对话框
- 编辑按钮：打开编辑对话框
- 删除按钮：删除供应商记录（需确认）

## 3. 后端数据模型

### 3.1 询价商数据模型（UserBuyer）

**表名**：`t_user_buyer`

```java
@Data
@ApiModel(value="t_user_buyer对象", description="买家")
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_buyer")
public class UserBuyer extends BaseEntity{
    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "公司id")
    @TableField(value = "company")
    private String company;

    @ApiModelProperty(value = "地址")
    @TableField(value = "address")
    private String address;

    @ApiModelProperty(value = "联系人")
    @TableField(value = "contact")
    private String contact;

    @ApiModelProperty(value = "手机号")
    @TableField(value = "mobile")
    private String mobile;

    @ApiModelProperty(value = "token")
    @TableField(value = "token")
    private String token;

    @ApiModelProperty(value = "授权时间")
    @TableField(value = "tokentime")
    private Date tokentime;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "createtime")
    private Date createtime;
}
```

### 3.2 供应商数据模型（UserSupplier）

**表名**：`t_user_supplier`

```java
@Data
@ApiModel(value="UserSupplier对象", description="供应商")
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_supplier")
public class UserSupplier extends BaseEntity{
    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "buyerid")
    @TableField(value = "buyerid")
    private String buyerid;

    @ApiModelProperty(value = "地址")
    @TableField(value = "address")
    private String address;

    @ApiModelProperty(value = "联系人")
    @TableField(value = "contact")
    private String contact;

    @ApiModelProperty(value = "手机号")
    @TableField(value = "mobile")
    private String mobile;

    @ApiModelProperty(value = "token")
    @TableField(value = "token")
    private String token;

    @ApiModelProperty(value = "password")
    @TableField(value = "password")
    private String password;

    @ApiModelProperty(value = "授权时间")
    @TableField(value = "tokentime")
    private Date tokentime;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "createtime")
    private Date createtime;

    @ApiModelProperty(value = "停用")
    @TableField(value = "disabled")
    private Boolean disabled;
}
```

## 4. API接口分析

### 4.1 前端API调用

#### 4.1.1 询价商相关API

| 功能 | 调用方法 | API路径 | 所属文件 |
|------|----------|---------|----------|
| 获取询价商信息 | `orderApi.getBuyer({"token":state.token})` | `/quote/api/v1/quote/getBuyer` | `orderApi.js` |
| 添加/更新询价商 | `orderApi.addBuyer(state.buyer)` | `/quote/api/v1/quote/addBuyer` | `orderApi.js` |
| 获取报价令牌 | `thirdpartyApi.getQuoteToken()` | `/erp/api/v1/thirdparty/getQuoteToken` | `thirdpartyApi.js` |
| 保存报价令牌 | `thirdpartyApi.saveQuoteToken(data)` | `/erp/api/v1/thirdparty/saveQuoteToken` | `thirdpartyApi.js` |
| 解绑报价令牌 | `thirdpartyApi.unbindQuoteToken()` | `/erp/api/v1/thirdparty/unbindQuoteToken` | `thirdpartyApi.js` |

#### 4.1.2 供应商相关API

| 功能 | 调用方法 | API路径 | 所属文件 |
|------|----------|---------|----------|
| 获取供应商列表 | `supplierApi.listsupplier(datas)` | `/quote/api/v1/quote/supplier/listSupplier` | `supplierApi.js` |
| 删除供应商 | `supplierApi.deletesupplier({"id":id})` | `/quote/api/v1/quote/supplier/deleteSupplier` | `supplierApi.js` |
| 添加供应商 | `supplierApi.addsupplier(token,data)` | `/quote/api/v1/quote/supplier/addSupplier/{token}` | `supplierApi.js` |
| 更新供应商 | `supplierApi.updatesupplier(data)` | `/quote/api/v1/quote/supplier/updateSupplier` | `supplierApi.js` |

### 4.2 后端API实现

#### 4.2.1 询价商API实现

```java
// UserBuyerController.java (示例)
@RestController
@RequestMapping("/quote/api/v1/quote")
public class UserBuyerController {
    
    @Autowired
    private IUserBuyerService userBuyerService;
    
    @GetMapping("/getBuyer")
    public Result<UserBuyer> getBuyer(@RequestParam String token) {
        UserBuyer buyer = userBuyerService.getByToken(token);
        return Result.success(buyer);
    }
    
    @PostMapping("/addBuyer")
    public Result<String> addBuyer(@RequestBody UserBuyer buyer) {
        String token = userBuyerService.saveOrUpdateBuyer(buyer);
        return Result.success(token);
    }
}
```

#### 4.2.2 第三方API实现

```java
// ThirdPartyController.java (示例)
@RestController
@RequestMapping("/erp/api/v1/thirdparty")
public class ThirdPartyController {
    
    @GetMapping("/getQuoteToken")
    public Result<Map<String, Object>> getQuoteToken() {
        // 从当前用户获取绑定的令牌信息
        Map<String, Object> tokenInfo = new HashMap<>();
        // ... 实现逻辑
        return Result.success(tokenInfo);
    }
    
    @PostMapping("/saveQuoteToken")
    public Result<String> saveQuoteToken(@RequestBody Map<String, Object> data) {
        // 保存令牌信息到当前用户
        // ... 实现逻辑
        return Result.success();
    }
    
    @GetMapping("/unbindQuoteToken")
    public Result<String> unbindQuoteToken() {
        // 解绑当前用户的令牌
        // ... 实现逻辑
        return Result.success();
    }
}
```

#### 4.2.3 供应商API实现

```java
// SupplierManagerController.java (示例)
@RestController
@RequestMapping("/quote/api/v1/quote/supplier")
public class SupplierManagerController {
    
    @Autowired
    private IUserSupplierService userSupplierService;
    
    @PostMapping("/listSupplier")
    public Result<List<UserSupplier>> listSupplier(@RequestBody Map<String, Object> data) {
        String token = (String) data.get("token");
        List<UserSupplier> suppliers = userSupplierService.listByBuyerToken(token);
        return Result.success(suppliers);
    }
    
    @DeleteMapping("/deleteSupplier")
    public Result<String> deleteSupplier(@RequestParam String id) {
        userSupplierService.removeById(id);
        return Result.success();
    }
    
    @PostMapping("/addSupplier/{token}")
    public Result<String> addSupplier(@PathVariable String token, @RequestBody UserSupplier supplier) {
        supplier.setBuyerid(token);
        userSupplierService.save(supplier);
        return Result.success();
    }
    
    @PutMapping("/updateSupplier")
    public Result<String> updateSupplier(@RequestBody UserSupplier supplier) {
        userSupplierService.updateById(supplier);
        return Result.success();
    }
}
```

## 5. 业务流程解析

### 5.1 页面初始化流程

1. 页面加载时调用 `onMounted(() => loadToken())`
2. `loadToken()` 调用 `thirdpartyApi.getQuoteToken()` 获取当前用户绑定的令牌信息
3. 如果令牌存在，调用 `loadBuyer()` 和 `loadSupplier()` 分别加载询价商和供应商数据
4. 渲染页面，显示已绑定或未绑定状态

### 5.2 令牌绑定流程

1. 用户输入令牌和别名
2. 点击"绑定"按钮，调用 `bindToken()` 方法
3. `bindToken()` 先调用 `orderApi.getBuyer()` 验证令牌有效性
4. 如果验证成功，调用 `thirdpartyApi.saveQuoteToken()` 保存令牌信息
5. 保存成功后，加载询价商和供应商数据，更新页面状态

### 5.3 供应商管理流程

#### 5.3.1 添加供应商
1. 点击"新增"按钮，调用 `handleShow()` 方法
2. 打开 `CreateDialog` 组件对话框
3. 用户填写供应商信息并点击保存
4. 调用 `supplierApi.addsupplier()` 保存供应商信息
5. 保存成功后，刷新供应商列表

#### 5.3.2 编辑供应商
1. 点击供应商列表中的"编辑"按钮，调用 `editSupplier()` 方法
2. 打开 `CreateDialog` 组件对话框并填充现有数据
3. 用户修改信息并点击保存
4. 调用 `supplierApi.updatesupplier()` 更新供应商信息
5. 更新成功后，刷新供应商列表

#### 5.3.3 删除供应商
1. 点击供应商列表中的"删除"按钮，调用 `delSupplier()` 方法
2. 弹出确认对话框
3. 用户确认后，调用 `supplierApi.deletesupplier()` 删除供应商
4. 删除成功后，刷新供应商列表

## 6. 操作指南

### 6.1 绑定询价商令牌

1. 在左侧"询价商管理"区域，找到"令牌"输入框
2. 输入从询价商获取的有效令牌
3. 输入别名（可选）
4. 点击"绑定"按钮
5. 系统验证令牌有效性并完成绑定
6. 绑定成功后，页面显示已绑定状态和令牌信息

### 6.2 设置询价商详细信息

1. 确保已成功绑定询价商令牌
2. 找到"高级"折叠面板并点击展开
3. 点击"修改"按钮进入编辑模式
4. 填写或修改以下信息：
   - 名称：询价商公司名称
   - 地址：询价商公司地址
   - 手机号：联系电话
   - 联系人：对接人姓名
5. 点击"保存"按钮完成设置

### 6.3 新增物流供应商

1. 确保已成功绑定询价商令牌
2. 在右侧"物流供应商管理"区域，点击"新增"按钮
3. 在弹出的对话框中填写供应商信息：
   - 名称：物流供应商名称
   - 地址：供应商地址
   - 联系人：对接人姓名
   - 手机号：联系电话
4. 点击"保存"按钮完成新增
5. 新增成功后，供应商列表会自动刷新

### 6.4 编辑物流供应商

1. 在供应商列表中找到需要修改的供应商
2. 点击该供应商右侧的"编辑"按钮
3. 在弹出的对话框中修改供应商信息
4. 点击"保存"按钮完成修改
5. 修改成功后，供应商列表会自动刷新

### 6.5 删除物流供应商

1. 在供应商列表中找到需要删除的供应商
2. 点击该供应商右侧的"删除"按钮
3. 在弹出的确认对话框中点击"确定"按钮
4. 删除成功后，供应商列表会自动刷新

## 7. 常见问题解答

### 7.1 绑定令牌失败

**问题现象**：点击"绑定"按钮后，系统提示"绑定失败，未找到询价商"

**可能原因**：
- 令牌输入错误
- 令牌已过期
- 网络连接问题
- 询价商系统故障

**解决方案**：
1. 检查令牌是否正确输入（注意大小写和空格）
2. 联系询价商确认令牌有效性
3. 检查网络连接
4. 稍后重试

### 7.2 无法看到供应商列表

**问题现象**：绑定令牌后，右侧供应商列表为空

**可能原因**：
- 该询价商下尚未添加任何物流供应商
- 网络连接问题
- 权限不足

**解决方案**：
1. 点击"新增"按钮添加物流供应商
2. 检查网络连接
3. 确认当前用户有查看供应商列表的权限

### 7.3 无法删除供应商

**问题现象**：点击"删除"按钮后，系统提示删除失败

**可能原因**：
- 供应商已被其他业务引用
- 权限不足
- 网络连接问题

**解决方案**：
1. 确认该供应商没有关联的报价订单
2. 检查当前用户是否有删除权限
3. 检查网络连接

### 7.4 无法访问供应商页面

**问题现象**：点击供应商页面链接后无法访问

**可能原因**：
- 不是询价商所有者（`isowner`为false）
- 没有访问权限（`v-hasPerm='erp:pi:supplier:link'`不满足）
- 供应商页面URL格式错误

**解决方案**：
1. 确认当前用户是询价商所有者
2. 联系系统管理员获取访问权限
3. 尝试手动复制URL并在新窗口打开

## 8. 代码优化建议

### 8.1 前端代码优化

#### 8.1.1 状态管理优化

当前代码使用 `reactive` 创建状态对象，然后使用 `toRefs` 解构。建议使用 `ref` 直接创建响应式变量，使代码更简洁：

```javascript
// 优化前
const state = reactive({
    edittoken:"",
    editname:"",
    supplier:{},
    tableData:[],
    token:"",
    tokenname:"",
    title:'新增',
    isowner:false,
    buyer:{'edit':true},
})
const{ token,buyer,edittoken,editname,supplier,tableData,title,isowner,tokenname }=toRefs(state);

// 优化后
const edittoken = ref("");
const editname = ref("");
const supplier = ref({});
const tableData = ref([]);
const token = ref("");
const tokenname = ref("");
const title = ref('新增');
const isowner = ref(false);
const buyer = ref({ edit: true });
```

#### 8.1.2 错误处理优化

当前代码在API调用失败时只显示简单的错误信息，建议添加更详细的错误处理和用户提示：

```javascript
// 优化前
orderApi.addBuyer(state.buyer).then(res=>{
    if(res.data){
        state.edittoken=res.data;
        bindToken("isowner");
    }else{
        ElMessage.error("操作失败");
        state.tableData=[];
    }
})

// 优化后
orderApi.addBuyer(state.buyer).then(res=>{
    if(res.data){
        state.edittoken=res.data;
        bindToken("isowner");
    }else{
        ElMessage.error(res.message || "操作失败");
        state.tableData=[];
    }
}).catch(error => {
    console.error("添加询价商失败:", error);
    ElMessage.error("网络错误，请稍后重试");
})
```

### 8.2 后端代码优化

#### 8.2.1 API参数验证

建议在后端API中添加参数验证，确保数据完整性和安全性：

```java
@PostMapping("/addBuyer")
public Result<String> addBuyer(@Valid @RequestBody UserBuyer buyer) {
    // 实现逻辑
}

// 在UserBuyer实体类中添加验证注解
@Data
public class UserBuyer {
    @NotBlank(message = "名称不能为空")
    private String name;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String mobile;
    
    // 其他字段...
}
```

#### 8.2.2 事务管理

在涉及多个数据库操作的业务逻辑中添加事务管理，确保数据一致性：

```java
@Service
public class UserSupplierServiceImpl implements IUserSupplierService {
    
    @Autowired
    private UserSupplierMapper userSupplierMapper;
    
    @Autowired
    private QuoteOrderSupplierMapper quoteOrderSupplierMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSupplier(String id) {
        // 删除供应商
        boolean result = userSupplierMapper.deleteById(id) > 0;
        
        // 同时删除相关的报价订单关联
        quoteOrderSupplierMapper.deleteBySupplierId(id);
        
        return result;
    }
}
```

## 9. 总结

物流报价设置页面是Wimoor ERP系统中物流报价流程的基础设置页面，实现了询价商令牌绑定、询价商信息管理以及物流供应商的增删改查功能。通过该页面，用户可以建立与询价商的连接，并管理物流供应商信息，为后续的物流报价流程奠定基础。

该页面采用了清晰的左右分栏布局，使用Vue 3和Element Plus构建了友好的用户界面，后端使用Spring Boot和MyBatis Plus实现了高效的数据处理。页面的业务逻辑设计合理，流程清晰，能够满足用户的日常操作需求。
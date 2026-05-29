# 图片存储实现分析：MinIO 与多后端架构

## 1. 概览

Wimoor 项目**确实使用了 MinIO** 作为图片（及视频）的对象存储后端之一。存储层采用三选一的策略模式，按以下优先级自动降级：

```
阿里云 OSS（优先）→ MinIO → FTP（兜底）
```

只有当更高优先级的后端配置了有效的 endpoint + 密钥时才会启用，未配置时自动跳到下一个。

---

## 2. 模块结构

```
wimoor-common/common-storage/
├── pom.xml                                 # 依赖：minio:8.6.0, aliyun-sdk-oss, commons-net
└── src/main/java/com/wimoor/common/
    ├── pojo/entity/
    │   └── StorageType.java                # 枚举: FTP / OSS / MinIO
    └── service/
        ├── ObjectHandler.java              # 读取回调接口
        ├── impl/
        │   ├── StorageService.java         # 普通文件统一入口
        │   └── StorageLargeService.java    # 大文件统一入口
        └── util/
            ├── MinIOApiUtil.java           # 普通 MinIO 客户端工具
            ├── MinIOLargeApiUtil.java      # 大文件 MinIO 客户端工具
            ├── OSSApiUtil.java             # 阿里云 OSS
            ├── OSSLargeApiUtil.java        # 阿里云 OSS（大文件）
            ├── FTPServerUtil.java          # FTP
            └── FTPLargeServerUtil.java     # FTP（大文件）

wimoor-common/common-mvc/
└── src/main/java/com/wimoor/common/mvc/
    ├── FileUpload.java                     # 路径 → 访问 URL 转换工具
    └── MyStringJsonSerializer.java         # JSON 序列化时自动转换 image/location 字段
```

---

## 3. 配置

### 3.1 Nacos 配置文件

配置位于 `init-config/nacos/DEFAULT_GROUP/wimoor-commom-ext`（注意拼写为 `commom`）：

```properties
# ── 阿里云 OSS（当前默认激活，优先级最高）──────────────────────────────
aliyun.oss_endpoint=    oss-cn-shenzhen.aliyuncs.com
aliyun.accessKeyId=     ABCD2eFg460hIJKGXpcVySDzk
aliyun.accessKeySecret= AB4C5efIGRSDzeeGeGVrGJuWmRHQbE
aliyun.bucketName=      myBucketName-file
aliyun.bucketPath=      https://myBucketPath-file.oss-cn-diqu.aliyuncs.com

# ── MinIO（默认注释掉，取消注释即可启用）──────────────────────────────
#minio.minio_endpoint=  http://127.0.0.1:9000
#minio.accessKeyId=     admin
#minio.accessKeySecret= password
#minio.bucketName=      wimoor-file
#minio.bucketPath=      http://127.0.0.1:9000/wimoor-file

# ── FTP（兜底，若 OSS 和 MinIO 均未配置时生效）────────────────────────
#ftp.ftpUserName=
#ftp.ftpPassword=
#ftp.ftpHost=
```

> **切换到 MinIO**：取消注释 `minio.*` 配置块，同时注释掉 `aliyun.*` 配置，重启服务即生效。

### 3.2 配置属性绑定

| 类 | Spring 前缀 | 说明 |
|---|---|---|
| `MinIOApiUtil` | `minio` | 普通文件（≤20MB 图片，≤200MB 视频） |
| `MinIOLargeApiUtil` | `large.minio` | 大文件专用实例，独立配置 |
| `OSSApiUtil` | `aliyun` | 阿里云 OSS |

---

## 4. 核心类实现

### 4.1 `MinIOApiUtil` — 普通存储客户端

```java
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOApiUtil {
    private String minio_endpoint;   // MinIO 服务地址
    private String accessKeyId;      // Access Key
    private String accessKeySecret;  // Secret Key
    public  String bucketName;       // Bucket 名称
    public  String bucketPath;       // 公开访问 URL 前缀

    // 判断是否已配置（三个字段均不为空才认为启用）
    public boolean isRun() { ... }

    // 每次创建新 Client（无连接池）
    public MinioClient getClient() {
        return new MinioClient.Builder()
                .endpoint(minio_endpoint)
                .credentials(accessKeyId, accessKeySecret)
                .build();
    }

    // 上传：先将 InputStream 完整读入内存，再调用 putObject
    public boolean putObject(String bucketName, String objectName, InputStream inputStream) { ... }

    // 删除
    public void removeObject(String bucketName, String objectName) { ... }

    // 读取：通过 ObjectHandler 回调处理流
    public void getObject(String bucketName, String objectName,
                          ObjectHandler handler, Map<String,Object> param) { ... }
}
```

**注意**：当前实现对每次操作都 `new MinioClient.Builder()` 创建客户端，未做连接池复用，高并发场景有优化空间。

### 4.2 `StorageService` — 统一存储入口（策略模式）

```java
@Component
public class StorageService {
    @Resource OSSApiUtil oSSApiUtil;
    @Resource MinIOApiUtil minIOApiUtil;
    @Resource FTPServerUtil ftpServerUtil;

    public Boolean putObject(String bucketName, String objectName, InputStream stream) {
        if (oSSApiUtil.isRun()) {
            oSSApiUtil.putObject(bucketName, objectName, stream);
        } else if (minIOApiUtil.isRun()) {
            minIOApiUtil.putObject(bucketName, objectName, stream);
        } else {
            ftpServerUtil.uploadFileOther(bucketName, objectName, stream);
        }
        return true;
    }

    // getBucketName() / getBucketPath() 同样按相同优先级返回
    public String getBucketName() { ... }
    public String getBucketPath() { ... }

    // 支持显式指定 StorageType 的重载版本（StorageType.OSS / MinIO / FTP）
    public Boolean putObject(String bucketName, String objectName,
                             InputStream stream, StorageType storageType) { ... }
}
```

---

## 5. 图片上传完整流程

以商品图片上传为例，调用链为：

```
前端 POST /erp/api/v1/material/media/upload
    │
    ▼
MaterialMediaController.upload()
    │  @RequestParam("file") MultipartFile
    │  @RequestParam("mediaType") Integer（0=图片, 1=视频）
    │
    ▼
ErpMediaServiceImpl.upload()
    │
    ├─1─ 参数校验：文件不为空、mediaType、用户上下文
    │
    ├─2─ 文件类型 & 大小校验
    │      图片：JPEG/JPG/PNG/WEBP/GIF，≤20MB
    │      视频：MP4/MOV/AVI，≤200MB
    │
    ├─3─ 读取文件字节 → 计算 MD5
    │
    ├─4─ MD5 去重查询（同 shopid 范围内）
    │      已存在 → 直接返回已有记录（不重复上传）
    │
    ├─5─ 构造 objectName：
    │      media/{shopid}/{md5前2位}/{md5全文}{ext}
    │      例：media/shop123/ab/abcdef...jpg
    │
    ├─6─ 上传原文件到对象存储
    │      storageService.putObject(bucketName, objectName, inputStream)
    │      → OSS/MinIO/FTP 三选一
    │
    ├─7─ 图片缩略图生成（仅图片，失败不阻断主流程）
    │      使用 thumbnailator 压缩到 300×300
    │      上传路径：{objectName}.thumb.jpg
    │      同时读取图片宽高存入元数据
    │
    └─8─ 入库 t_erp_media
           字段：shopid, media_type, usage_type, source,
                 location(objectName), thumb_location,
                 name, content_type, file_size, md5,
                 width, height, process_status, creator
```

### 批量上传（ZIP）

`POST /erp/api/v1/material/media/uploadBatch`：接收一个 ZIP 包，遍历其中的 `.jpg/.png/.webp` 文件，逐一调用 `upload()` 方法。

---

## 6. 图片读取（URL 转换）

数据库中存储的是相对路径（`location` 字段），例如：

```
media/shop123/ab/abcdefghij1234567890.jpg
```

展示给前端时需转换为完整 URL，转换逻辑集中在 `FileUpload.getPictureImage()`：

```java
public String getPictureImage(String value) {
    if (value != null) {
        // 已是完整 URL（Amazon CDN 等外部图）直接返回
        if (value.contains("http")) return value;

        // 旧 FTP 图片路径
        if (value.contains("photo/")) {
            return value.replace("photo/", photoServerUrl + "/");
        }

        // 普通对象存储（OSS/MinIO）：bucketName/xxx → bucketPath/xxx
        if (value.contains(storageService.getBucketName() + "/")) {
            return value.replace(
                storageService.getBucketName() + "/",
                storageService.getBucketPath() + "/"
            );
        }

        // 大文件对象存储
        if (storageLargeService.getBucketName() != null &&
            value.contains(storageLargeService.getBucketName() + "/")) {
            return value.replace(
                storageLargeService.getBucketName() + "/",
                storageLargeService.getBucketPath() + "/"
            );
        }
    } else {
        // 无图时返回默认占位图
        return storageService.getBucketPath() + "/sys/photos/noimg.png";
    }
    return value;
}
```

**转换示例（MinIO 场景）**：
- 配置：`minio.bucketName=wimoor-file`，`minio.bucketPath=http://127.0.0.1:9000/wimoor-file`
- 存储路径：`media/shop123/ab/abcdef.jpg`
- 转换后：`http://127.0.0.1:9000/wimoor-file/media/shop123/ab/abcdef.jpg`

### 自动序列化转换

`MyStringJsonSerializer` 注册为全局 Jackson 自定义序列化器，在输出 JSON 时自动对字段名为 `image` 和 `location` 的字符串值调用 `getPictureImage()`，无需在每个 Service 手动调用。

```java
if ("image".equals(currentFieldName) || "location".equals(currentFieldName)) {
    value = fileUpload.getPictureImage(value);
}
```

---

## 7. 数据库表结构

### `t_erp_media`（媒体元数据）

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | bigint | Snowflake 主键 |
| `shopid` | varchar | 租户 ID（企业隔离） |
| `media_type` | int | 0=图片 1=视频 |
| `usage_type` | int | 10=参考图 30=原图 40=成品图 60=橱窗图 70=公共图 |
| `source` | int | 1=引用 2=自拍 3=白底 4=Amazon同步 5=批量导入 6=AI生成 |
| `url` | varchar | 外部原始 URL（如 Amazon CDN） |
| `location` | varchar | 对象存储**相对路径**（key） |
| `thumb_location` | varchar | 缩略图路径（`{location}.thumb.jpg`） |
| `name` | varchar | 原始文件名 |
| `width` / `height` | int | 图片尺寸（px） |
| `file_size` | bigint | 字节数 |
| `content_type` | varchar | MIME 类型 |
| `md5` | varchar | 文件 MD5（同 shopid 内去重） |
| `process_status` | int | 0=无需 1=处理中 2=完成 3=失败 |

### `t_erp_media_ref`（媒体-商品关联）

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | bigint | Snowflake 主键 |
| `media_id` | bigint | FK → t_erp_media |
| `material_id` | bigint | FK → t_erp_material（商品） |
| `shopid` | varchar | 租户 ID |
| `ref_type` | int | 0=SPU 图片池 1=SKU 展示图 |
| `pic_class` | int | 1=成品图 2=橱窗图 3=公共图 4=说明图 5=场景图 |
| `sort_order` | int | 排序（越小越靠前） |
| `is_main` | int | 0=否 1=主图 |
| `platform` | varchar | amazon/tiktok/ebay，NULL=通用 |
| `marketplace_id` | varchar | 站点 ID（多站点差异图） |
| `slot_position` | varchar | MAIN/PT01~PT08（Listing 图片位） |

---

## 8. 关键 API 端点

| 方法 | 路径（带 ERP 前缀） | 说明 |
|---|---|---|
| POST | `/erp/api/v1/material/media/upload` | 上传单个图片/视频 |
| POST | `/erp/api/v1/material/media/uploadBatch` | ZIP 批量上传 |
| GET | `/erp/api/v1/material/media/list?materialid=` | SKU 展示图列表 |
| GET | `/erp/api/v1/material/media/pool?materialid=` | SPU 图片池 |
| POST | `/erp/api/v1/material/media/assign` | 分配媒体到商品 |
| POST | `/erp/api/v1/material/media/batchAssign` | 批量分配 |
| DELETE | `/erp/api/v1/material/media/unassign/{refId}` | 取消分配 |
| PUT | `/erp/api/v1/material/media/setMain/{refId}` | 设置主图 |
| POST | `/erp/api/v1/material/media/sort` | 排序 |
| DELETE | `/erp/api/v1/material/media/delete/{mediaId}` | 删除媒体 |

---

## 9. 关键设计决策

| 决策 | 说明 |
|---|---|
| **多后端策略模式** | OSS/MinIO/FTP 三选一，靠 `isRun()` 自动检测，切换无需改代码 |
| **MD5 去重** | 同租户内同 MD5 文件只存一份，上传时先查再存 |
| **objectName 路径规则** | `media/{shopid}/{md5前2位}/{md5全文}{ext}` 自然分片，避免目录过深 |
| **缩略图不阻断** | 生成缩略图失败只打 warn 日志，不影响原文件上传结果 |
| **bucketPath 配置** | MinIO 需配置公开访问地址（`bucketPath`），与 Bucket 内部名独立，支持 CDN/反代 |
| **JSON 自动转换** | `MyStringJsonSerializer` 全局拦截 `image`/`location` 字段，避免 Service 层散落转换逻辑 |
| **无连接池** | 当前每次调用都 new MinioClient，适合低并发；高并发场景建议改为单例或池化 |

---

## 10. 如何启用 MinIO

1. 本地启动 MinIO（或使用现有服务），默认端口 `9000`，Console `9001`。
2. 创建 Bucket，例如 `wimoor-file`，并设置为 Public 访问策略（或通过 nginx 代理）。
3. 修改 Nacos 配置 `wimoor-commom-ext`：
   - 注释掉 `aliyun.*` 相关配置
   - 取消注释并填写 `minio.*` 配置：
     ```properties
     minio.minio_endpoint=http://your-minio-host:9000
     minio.accessKeyId=your-access-key
     minio.accessKeySecret=your-secret-key
     minio.bucketName=wimoor-file
     minio.bucketPath=http://your-minio-host:9000/wimoor-file
     ```
4. 重启 `wimoor-erp` 服务，`StorageService.putObject()` 即自动走 MinIO 分支。

# media-asset-storage Specification

## Purpose
TBD - created by archiving change product-media-management. Update Purpose after archive.
## Requirements
### Requirement: 媒体元数据表采用双表分离架构
The system SHALL store media assets in `t_erp_media` (元数据) with fields: id (snowflake), shopid, media_type (0=图片/1=视频), usage_type (10=参考图/30=原图/40=成品图/60=橱窗图/70=公共图/90=说明图/100=场景图), source (1=引用/2=自拍/3=白底/4=Amazon同步/5=批量导入/6=AI生成), url, location, thumb_location, name, width, height, file_size, duration, content_type, md5, process_status, process_task_id, creator, create_time, update_time.

#### Scenario: Upload image creates media record
- **WHEN** a user uploads a product image for company "shop123"
- **THEN** the system creates 1 record in `t_erp_media` with shopid='shop123', media_type=0, source=2(自拍)
- **THEN** the record contains the file's MD5 hash, dimensions, and storage path

#### Scenario: Video stored alongside images
- **WHEN** a user uploads a video for the same company
- **THEN** the system creates 1 record with media_type=1, duration populated, content_type='video/mp4'
- **THEN** existing image records remain unchanged

### Requirement: 媒体-商品关联表实现一图多用（SPU池+SKU分配）
The system SHALL maintain `t_erp_media_ref` linking media to materials with fields: id, media_id (FK→t_erp_media.id), material_id (FK→t_erp_material.id), shopid, ref_type (0=SPU级图片池/1=SKU级展示图), pic_class (1=成品图/2=橱窗图/3=公共图/4=说明图/5=场景图), sort_order, is_main, platform, marketplace_id, slot_position, create_time. Unique constraint on (media_id, material_id, platform, marketplace_id).

#### Scenario: Image uploaded to SPU pool
- **WHEN** a user uploads 5 product images for SPU material "SPU-001"
- **THEN** 5 records in `t_erp_media` are created (one per file)
- **THEN** 5 records in `t_erp_media_ref` are created with material_id=SPU-001, ref_type=0, sort_order=0,1,2,3,4

#### Scenario: Image assigned from SPU pool to SKU
- **WHEN** a user assigns image media_id=100 from SPU pool to SKU "SKU-001A"
- **THEN** a new record in `t_erp_media_ref` is created with media_id=100, material_id=SKU-001A, ref_type=1
- **THEN** the original SPU pool record (ref_type=0) remains unchanged

#### Scenario: Same image assigned to multiple SKUs (一图多用)
- **WHEN** image media_id=100 is assigned to both SKU-001A and SKU-001B
- **THEN** 2 records exist in `t_erp_media_ref` with the same media_id but different material_id values
- **THEN** the physical file is NOT duplicated in storage

### Requirement: MD5文件去重机制
The system SHALL calculate MD5 hash for every uploaded file and check `t_erp_media WHERE shopid=? AND md5=?` before storing. If a duplicate exists, the system SHALL reuse the existing media record by creating only a new `t_erp_media_ref` association without re-uploading the physical file.

#### Scenario: Duplicate file detected
- **WHEN** user uploads a file whose MD5 matches an existing record in the same company
- **THEN** no new file is written to object storage
- **THEN** a new `t_erp_media_ref` record is created linking the existing media to the target material
- **THEN** response indicates the file was deduplicated

#### Scenario: Same file in different companies
- **WHEN** company "shop123" and "shop456" both upload the same file (same MD5)
- **THEN** both get separate `t_erp_media` records (dedup is per-tenant)
- **THEN** both files are stored separately in storage

### Requirement: Physical storage path follows hierarchical convention
The system SHALL store uploaded media files at path `{bucketName}/erp/media/{shopid}/{materialid}/{yyyy}/{MM}/{dd}/{mediaId}_{filename}` for images, and `{bucketName}/erp/media/{shopid}/{materialid}/{yyyy}/{MM}/{dd}/video/{mediaId}_{filename}` for videos. Thumbnails SHALL be stored at `{bucketName}/erp/media/{shopid}/{materialid}/{yyyy}/{MM}/{dd}/thumb/{mediaId}_{filename}.jpg`.

#### Scenario: Image upload creates correct storage path
- **WHEN** user in company "shop123" uploads "product.png" for material "mat456" on 2025-01-15
- **THEN** the file is stored at `{bucket}/erp/media/shop123/mat456/2025/01/15/{newId}_product.png`
- **THEN** a 200x200 thumbnail is generated at `{bucket}/erp/media/shop123/mat456/2025/01/15/thumb/{newId}_product.jpg`

#### Scenario: Video upload creates correct storage path
- **WHEN** user in company "shop123" uploads "demo.mp4" for material "mat456" on 2025-01-15
- **THEN** the file is stored at `{bucket}/erp/media/shop123/mat456/2025/01/15/video/{newId}_demo.mp4`

### Requirement: Thumbnail generation on image upload
The system SHALL automatically generate a 200x200 pixel JPEG thumbnail for every uploaded image using the existing Thumbnails library (quality=0.3).

#### Scenario: Thumbnail created on upload
- **WHEN** a 2000x2000px JPEG image is uploaded
- **THEN** the system creates a 200x200 thumbnail at the `thumb/` sub-path
- **THEN** `thumb_location` field in the database record contains the thumbnail path

#### Scenario: Non-image media skips thumbnail
- **WHEN** an MP4 video file is uploaded without a cover image
- **THEN** no thumbnail is generated
- **THEN** `thumb_location` remains null

### Requirement: Main image synchronization with t_erp_material
The system SHALL keep `t_erp_material.image` synchronized with the media ref marked `is_main=1`. When `is_main` changes, a corresponding `t_picture` record MUST be created/updated and linked.

#### Scenario: First image auto-becomes main
- **WHEN** a user uploads the first product image for a SKU that has no existing media refs
- **THEN** the ref record is created with `is_main=1`
- **THEN** `t_erp_material.image` is updated to point to a new `t_picture` record with the same location

#### Scenario: Main image deleted triggers fallback
- **WHEN** the current main image ref is removed (unassigned)
- **THEN** the ref with the lowest `sort_order` among remaining active refs becomes `is_main=1`
- **THEN** `t_erp_material.image` is updated accordingly

#### Scenario: No remaining images clears main
- **WHEN** the last image ref for a SKU is removed
- **THEN** `t_erp_material.image` is set to null

### Requirement: Amazon platform media cache table
The system SHALL store Amazon listing images in `t_amz_product_media` with a unique constraint on (authority_id, marketplace_id, sku, variant). Records represent the current state of images on Amazon's CDN.

#### Scenario: Amazon sync captures all image variants
- **WHEN** the system fetches listing data for SKU "XYZ" and Amazon returns MAIN + PT01 + PT02 images
- **THEN** 3 records are stored/updated in `t_amz_product_media` with variant values 'MAIN', 'PT01', 'PT02'
- **THEN** each record contains the Amazon CDN URL and local cached path

#### Scenario: Amazon image URL changed triggers re-download
- **WHEN** a sync finds that the URL for variant 'PT01' differs from the stored record
- **THEN** the system downloads the new image to local storage
- **THEN** updates the record with new url, location, and sync_time

### Requirement: File type and size validation
The system SHALL validate uploaded files: images MUST be JPEG/PNG/WEBP and ≤10MB; videos MUST be MP4(H.264) and ≤500MB. Invalid files SHALL be rejected with a descriptive error message.

#### Scenario: Oversized image rejected
- **WHEN** a user uploads a 15MB PNG image
- **THEN** the system returns HTTP 400 with message indicating file exceeds 10MB limit

#### Scenario: Invalid video format rejected
- **WHEN** a user uploads an AVI video file
- **THEN** the system returns HTTP 400 with message indicating only MP4 format is supported

#### Scenario: Valid image accepted
- **WHEN** a user uploads a 3MB JPEG image
- **THEN** the file is stored successfully, MD5 is calculated, and media + ref records are created

### Requirement: AI处理状态追踪预留
The system SHALL support `process_status` field (0=无需处理/1=处理中/2=处理完成/3=处理失败) and `process_task_id` field in `t_erp_media` for future AI Agent integration. These fields SHALL NOT be used in v1 but MUST exist in the schema.

#### Scenario: Normal upload sets no-processing state
- **WHEN** a user manually uploads an image
- **THEN** the record is created with process_status=0, process_task_id=null

#### Scenario: Future AI processing updates status (v2 placeholder)
- **WHEN** an AI Agent starts processing media_id=100
- **THEN** process_status is updated to 1, process_task_id is set to the agent's task ID
- **THEN** on completion, process_status changes to 2

### Requirement: Redis缓存策略
The system SHALL cache media query results in Redis with pattern `erp:media:list:{materialId}` (TTL 1h) and `erp:media:main:{materialId}` (TTL 1h). Cache MUST be invalidated on upload, delete, assign, unassign, sort, or setMain operations.

#### Scenario: List query hits cache
- **WHEN** a user queries media list for materialId that was recently queried
- **THEN** the result is served from Redis cache without hitting the database

#### Scenario: Upload invalidates cache
- **WHEN** a new image is uploaded and assigned to materialId="mat456"
- **THEN** Redis keys `erp:media:list:mat456` and `erp:media:main:mat456` are deleted
- **THEN** the next list query re-fetches from database and repopulates cache


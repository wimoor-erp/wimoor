# media-crud-api Specification

## Purpose
TBD - created by archiving change product-media-management. Update Purpose after archive.
## Requirements
### Requirement: Upload single media file
The system SHALL provide `POST /erp/api/v1/material/media/upload` accepting multipart form data with parameters: file (required), materialid (required), media_type (default 0), usage_type (default 40), ref_type (default 0=SPU池). The response SHALL return the created media record with accessible URLs. The system SHALL calculate MD5 and perform dedup check before storage.

#### Scenario: Successful image upload (new file)
- **WHEN** an authenticated user POSTs a 2MB JPEG file with materialid="12345" and ref_type=0
- **THEN** the system calculates file MD5, stores the file, creates `t_erp_media` record, creates `t_erp_media_ref` record
- **THEN** returns HTTP 200 with the new media record including id, location URL, thumb URL, and sort_order

#### Scenario: Successful upload with dedup (duplicate file)
- **WHEN** user uploads a file whose MD5 matches an existing media in the same company
- **THEN** the system does NOT re-upload the physical file
- **THEN** creates only a new `t_erp_media_ref` linking the existing media_id to the specified materialid
- **THEN** returns HTTP 200 with the media record and a `deduplicated: true` flag

#### Scenario: Upload without authentication
- **WHEN** an unauthenticated request is sent to the upload endpoint
- **THEN** the system returns HTTP 401

#### Scenario: Upload for non-existent material
- **WHEN** user uploads a file with materialid that does not exist in the user's company
- **THEN** the system returns HTTP 400 with error "商品不存在"

### Requirement: Batch upload via ZIP archive
The system SHALL provide `POST /erp/api/v1/material/media/uploadBatch` accepting a ZIP file and materialid. The system SHALL extract all image files from the ZIP, sort them by filename alphabetically, calculate MD5 for each, perform dedup, and create media + ref records with sequential sort_order.

#### Scenario: ZIP with 5 images uploaded
- **WHEN** user uploads a ZIP containing 01.jpg, 02.jpg, 03.jpg, 04.jpg, 05.jpg for materialid="12345"
- **THEN** 5 `t_erp_media` records are created (or reused via dedup) with sort_order 0,1,2,3,4
- **THEN** 5 `t_erp_media_ref` records are created linking to the material
- **THEN** each image has a thumbnail generated

#### Scenario: ZIP containing non-image files
- **WHEN** user uploads a ZIP containing mix of .jpg and .txt files
- **THEN** only image files (JPEG/PNG/WEBP) are processed
- **THEN** non-image files are silently skipped

### Requirement: List media for a material (SKU展示图)
The system SHALL provide `GET /erp/api/v1/material/media/list?materialid={id}` returning all media refs for the specified material with ref_type=1 (SKU展示图), ordered by sort_order. Image URLs SHALL be transformed to accessible URLs via `FileUpload.getPictureImage()`. Results SHALL be cached in Redis.

#### Scenario: List returns all SKU display images
- **WHEN** user requests media list for a SKU material that has 5 assigned images and 1 video
- **THEN** the response contains 6 records sorted by sort_order
- **THEN** each record includes accessible URL, thumbnail URL, is_main flag, and pic_class

#### Scenario: List from Redis cache
- **WHEN** user requests media list for a material that was recently queried
- **THEN** the result is served from Redis cache `erp:media:list:{materialId}`

### Requirement: Query SPU image pool
The system SHALL provide `GET /erp/api/v1/material/media/pool?materialid={id}` returning all media refs for the specified material with ref_type=0 (SPU图片池), ordered by sort_order. This shows images available for assignment to child SKUs.

#### Scenario: Pool returns all unassigned SPU images
- **WHEN** user queries pool for SPU material "SPU-001" that has 10 images in pool
- **THEN** the response contains 10 records with ref_type=0, including media details

#### Scenario: Pool for standalone SKU (no parent)
- **WHEN** user queries pool for a material with isparent=0 and no groupid
- **THEN** the response returns all images directly associated (ref_type=0 treated as its own pool)

### Requirement: Assign images from SPU pool to SKU
The system SHALL provide `POST /erp/api/v1/material/media/assign` accepting parameters: media_id (required), target_material_id (required), pic_class (default 1), sort_order (optional). This creates a new `t_erp_media_ref` with ref_type=1 linking the media to the target SKU.

#### Scenario: Assign one image to SKU
- **WHEN** user assigns media_id=100 to target SKU "SKU-001A" with pic_class=1
- **THEN** a new `t_erp_media_ref` record is created with media_id=100, material_id=SKU-001A, ref_type=1, pic_class=1
- **THEN** Redis cache `erp:media:list:SKU-001A` is invalidated

#### Scenario: Assign already-assigned image (idempotent)
- **WHEN** user assigns media_id=100 to SKU-001A that already has this assignment
- **THEN** no duplicate record is created (unique constraint prevents it)
- **THEN** the existing record is returned

### Requirement: Batch assign images to SKU
The system SHALL provide `POST /erp/api/v1/material/media/batchAssign` accepting parameters: media_ids (array), target_material_id (required). This batch-creates `t_erp_media_ref` records with sequential sort_order.

#### Scenario: Batch assign 5 images
- **WHEN** user submits media_ids=[100,101,102,103,104] for target_material_id="SKU-001A"
- **THEN** 5 `t_erp_media_ref` records are created with sort_order 0,1,2,3,4

### Requirement: Unassign image from SKU
The system SHALL provide `POST /erp/api/v1/material/media/unassign` accepting parameters: ref_id (the t_erp_media_ref.id to remove). The ref record is deleted. If the unassigned image was the main image, the system SHALL auto-promote the next image.

#### Scenario: Unassign non-main image
- **WHEN** user unassigns ref_id=999 which is NOT the main image
- **THEN** the `t_erp_media_ref` record is deleted
- **THEN** no other records are modified

#### Scenario: Unassign main image triggers promotion
- **WHEN** user unassigns the current main image and 3 other refs exist
- **THEN** the ref record is deleted
- **THEN** the ref with lowest sort_order among remaining is promoted to is_main=1
- **THEN** `t_erp_material.image` is updated to the new main image

### Requirement: Delete media asset
The system SHALL provide `DELETE /erp/api/v1/material/media/{id}` performing deletion of a `t_erp_media` record. Before deletion, the system SHALL check if any `t_erp_media_ref` records reference this media. If active refs exist and force=false, reject; if force=true, cascade-delete all refs.

#### Scenario: Delete unreferenced media
- **WHEN** user deletes media_id=100 that has no refs
- **THEN** the `t_erp_media` record is deleted
- **THEN** the physical file MAY be retained (lazy cleanup) or deleted

#### Scenario: Delete media with active refs (force=false)
- **WHEN** user attempts to delete media_id=100 which has 3 active refs, without force
- **THEN** the system returns HTTP 400 with "该媒体已被 3 个商品引用，请先取消关联或使用强制删除"

#### Scenario: Delete media with active refs (force=true)
- **WHEN** user attempts to delete media_id=100 with force=true
- **THEN** all `t_erp_media_ref` records for media_id=100 are deleted
- **THEN** the `t_erp_media` record is deleted
- **THEN** affected materials' main image status is recalculated

### Requirement: Update media sort order
The system SHALL provide `POST /erp/api/v1/material/media/sort` accepting an array of {ref_id, sort_order} objects. The system SHALL update all specified `t_erp_media_ref` records' sort_order in a single transaction.

#### Scenario: Reorder 5 images
- **WHEN** user submits [{ref_id:1, sort_order:4}, {ref_id:2, sort_order:0}, {ref_id:3, sort_order:1}, {ref_id:4, sort_order:2}, {ref_id:5, sort_order:3}]
- **THEN** all 5 ref records are updated with their new sort_order values
- **THEN** Redis cache is invalidated for the affected material

### Requirement: Set main image
The system SHALL provide `POST /erp/api/v1/material/media/setMain?ref_id={refId}` which sets the specified media ref as main image (is_main=1) and clears is_main on all other refs of the same material. The system SHALL synchronize `t_erp_material.image`.

#### Scenario: Promote image to main
- **WHEN** user sets ref_id=999 as main for material that currently has ref_id=888 as main
- **THEN** ref_id=999 gets is_main=1
- **THEN** ref_id=888 gets is_main=0
- **THEN** `t_erp_material.image` points to the t_picture record corresponding to the media of ref_id=999

### Requirement: Update usage type
The system SHALL provide `POST /erp/api/v1/material/media/updateUsage` accepting parameters: ref_id, pic_class (new value). This updates the `t_erp_media_ref.pic_class` field.

#### Scenario: Change image from 成品图 to 橱窗图
- **WHEN** user updates ref_id=100 with pic_class=2
- **THEN** the ref record's pic_class is changed from 1 to 2

### Requirement: Sync Amazon images to ERP media library
The system SHALL provide `POST /erp/api/v1/material/media/syncFromAmazon` accepting materialid and optional authority_id/marketplace_id. The system SHALL query `t_amz_product_media` for matching records and create corresponding entries in `t_erp_media` (source=4) + `t_erp_media_ref` (with platform='amazon', slot_position matching variant).

#### Scenario: Sync 3 Amazon images to ERP
- **WHEN** user triggers sync for materialid="12345" and Amazon has MAIN + PT01 + PT02 cached
- **THEN** 3 new `t_erp_media` records created with source=4 (Amazon同步)
- **THEN** 3 new `t_erp_media_ref` records created with platform='amazon', slot_position='MAIN'/'PT01'/'PT02'
- **THEN** the images are copied from Amazon cache path to ERP media path (if needed)
- **THEN** MAIN image ref is set as is_main=1 if no main image exists

#### Scenario: Duplicate sync skips existing
- **WHEN** user triggers sync again for the same SKU and Amazon images haven't changed
- **THEN** MD5 dedup prevents duplicate media records
- **THEN** existing refs are preserved unchanged

### Requirement: Amazon product media sync during listing refresh
The system SHALL extend `ProductListingsItemServiceImpl.refreshByAuthority` to capture all image variants (MAIN, PT01~PT08) from SP-API response and persist them to `t_amz_product_media`.

#### Scenario: Listing refresh captures all variants
- **WHEN** the scheduled listing refresh processes SKU "XYZ" and SP-API returns images with variants MAIN, PT01, PT02, PT03
- **THEN** 4 records are upserted in `t_amz_product_media`
- **THEN** the existing `t_product_info.image` update logic for MAIN remains unchanged

#### Scenario: Image removed from Amazon
- **WHEN** a variant that previously existed (e.g., PT03) is no longer returned by SP-API
- **THEN** the existing record remains but sync_time is not updated (stale detection)

### Requirement: AI processing callback endpoint (预留)
The system SHALL provide `POST /erp/api/v1/material/media/processCallback` accepting parameters: taskId, status (2=完成/3=失败), resultMediaIds[] (newly generated media IDs), errorMessage. This endpoint is a placeholder for future AI Agent integration.

#### Scenario: AI agent completes processing
- **WHEN** an AI agent calls processCallback with taskId="task-001", status=2, resultMediaIds=[200,201]
- **THEN** the original media record's process_status is updated to 2
- **THEN** new media records 200,201 are automatically linked to the same material via t_erp_media_ref

#### Scenario: AI agent reports failure
- **WHEN** an AI agent calls processCallback with taskId="task-001", status=3, errorMessage="..."
- **THEN** the original media record's process_status is updated to 3
- **THEN** no new media records are created


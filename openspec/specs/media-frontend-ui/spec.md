# media-frontend-ui Specification

## Purpose
TBD - created by archiving change product-media-management. Update Purpose after archive.
## Requirements
### Requirement: Media tab in product detail page
The system SHALL add a "图片视频" tab in the left sidebar of `views/erp/baseinfo/material/details/index.vue` following the existing tab pattern. The tab SHALL display all media assets for the current material in a dual-zone layout: SPU图片池 + SKU展示图.

#### Scenario: Tab visible in product detail
- **WHEN** user navigates to product detail page for any SKU
- **THEN** a "图片视频" tab appears in the left sidebar between existing tabs
- **THEN** clicking the tab scrolls to the media section

#### Scenario: SPU image pool displayed for parent materials
- **WHEN** user views the media tab for a SPU material (isparent=1)
- **THEN** the section shows "SPU图片池" area with all ref_type=0 images in a grid
- **THEN** below that, "SKU展示图" area shows ref_type=1 images for the current/selected SKU

#### Scenario: Standalone SKU shows simplified view
- **WHEN** user views the media tab for a standalone SKU (no parent/group)
- **THEN** only a single image grid is shown (ref_type=0 treated as display images)
- **THEN** the "分配到SKU" functionality is hidden

#### Scenario: Empty state display
- **WHEN** user views the media tab for a SKU with no media assets
- **THEN** an empty state placeholder is shown with an "上传图片" button

### Requirement: Media tab in product edit page with SPU pool management
The system SHALL add a "图片视频" tab in `views/erp/baseinfo/material/editinfo/index.vue` with full management capabilities: upload to SPU pool, assign to SKU, reorder, delete.

#### Scenario: Tab visible in product edit
- **WHEN** user navigates to product edit page for any SKU
- **THEN** a "图片视频" tab appears with upload area, SPU pool grid, and SKU assignment panel

#### Scenario: Drag and drop upload to SPU pool
- **WHEN** user drags 3 image files onto the upload area
- **THEN** all 3 files are uploaded to the SPU image pool (ref_type=0)
- **THEN** MD5 dedup is performed; duplicate files show a "已存在" notice
- **THEN** upload progress is shown for each file
- **THEN** the pool grid updates to show new images after upload completes

#### Scenario: Assign image from SPU pool to SKU
- **WHEN** user clicks "分配" on an image in SPU pool and selects target SKU
- **THEN** a new ref_type=1 record is created for the target SKU
- **THEN** the original pool image remains unchanged
- **THEN** the SKU assignment panel updates to show the newly assigned image

#### Scenario: Drag to reorder within SKU display images
- **WHEN** user drags image from position 3 to position 1 in the SKU display grid
- **THEN** the sort order is updated and persisted via the sort API
- **THEN** visual feedback (drag ghost, drop indicator) is shown during drag

### Requirement: Image preview with fullscreen viewer
The system SHALL provide fullscreen image preview when user clicks on a media thumbnail. The viewer SHALL support zoom, rotation, and navigation between images.

#### Scenario: Click image opens preview
- **WHEN** user clicks on any image thumbnail in the media grid
- **THEN** a fullscreen overlay opens showing the full-resolution image
- **THEN** left/right navigation arrows allow browsing adjacent images

#### Scenario: Preview controls
- **WHEN** user is in fullscreen preview mode
- **THEN** zoom in/out, rotate, and close controls are available
- **THEN** pressing Escape closes the preview

### Requirement: Set main image from media grid
The system SHALL allow users to designate any product image as the main image via a "设为主图" action in the media grid. The main image SHALL be visually distinguished with a badge.

#### Scenario: Set as main image
- **WHEN** user clicks "设为主图" on a ref in the SKU display grid
- **THEN** the system calls `POST /erp/api/v1/material/media/setMain`
- **THEN** the previous main image badge is removed
- **THEN** the selected image receives the "主图" badge

#### Scenario: Main image indicator
- **WHEN** the media grid loads with one ref marked is_main=1
- **THEN** that image displays a "主图" badge in its top-left corner

### Requirement: Image usage type management
The system SHALL allow users to change the `pic_class` of an image ref via a dropdown or tag selector. Available classes: 成品图(1), 橱窗图(2), 公共图(3), 说明图(4), 场景图(5).

#### Scenario: Change image classification
- **WHEN** user right-clicks or uses dropdown on an image and selects "橱窗图"
- **THEN** the system calls `POST /erp/api/v1/material/media/updateUsage` with new pic_class=2
- **THEN** the image's classification tag updates in the UI

#### Scenario: Filter by classification
- **WHEN** user selects a filter for "成品图" in the grid header
- **THEN** only images with pic_class=1 are displayed

### Requirement: Video upload with cover image
The system SHALL support video upload (MP4, ≤500MB) in the media tab. Users MUST manually upload a cover image for each video (no auto-extraction in v1).

#### Scenario: Upload video
- **WHEN** user clicks "上传视频" and selects an MP4 file
- **THEN** the video is uploaded and displayed in the media grid with a play icon overlay
- **THEN** a prompt asks the user to upload a cover image (stored as thumb_location)

#### Scenario: Video thumbnail display
- **WHEN** the media grid contains a video entry with a cover image
- **THEN** the grid shows the cover image with a play icon overlay
- **THEN** clicking the play icon opens an inline video player (HTML5 `<video>` element)

### Requirement: Amazon image sync action in UI
The system SHALL provide a "从 Amazon 同步" button in the media tab that triggers `syncFromAmazon` for the current material. A dialog SHALL show which Amazon shops/marketplaces have images available for sync.

#### Scenario: Sync button triggers dialog
- **WHEN** user clicks "从 Amazon 同步" button
- **THEN** a dialog opens showing available Amazon authorizations and marketplaces
- **THEN** user can select which source to sync from
- **THEN** clicking confirm calls the syncFromAmazon API

#### Scenario: Sync result feedback
- **WHEN** the syncFromAmazon API returns successfully with 5 new images (or dedup results)
- **THEN** the dialog shows "成功同步 5 张图片（去重跳过 2 张）"
- **THEN** the media grid refreshes to show the new images with source indicator "Amazon同步"

#### Scenario: No Amazon images available
- **WHEN** user clicks sync and no Amazon images exist for this SKU
- **THEN** the dialog shows "该商品在 Amazon 上暂无图片"

### Requirement: SKU assignment panel for parent materials
The system SHALL provide a panel within the media edit tab showing child SKUs and their assigned images. For SPU (parent) materials, users SHALL be able to select a child SKU and manage its image assignments independently.

#### Scenario: View child SKU assignments
- **WHEN** user opens the media tab for a SPU with 3 child SKUs
- **THEN** a SKU selector (dropdown/tabs) shows all child SKUs
- **THEN** selecting a child SKU displays its assigned images (ref_type=1)

#### Scenario: Batch assign from pool to SKU
- **WHEN** user selects 3 images in SPU pool and clicks "批量分配" with target SKU selected
- **THEN** the system calls batchAssign API to create 3 refs for the target SKU
- **THEN** the SKU panel updates to show newly assigned images

#### Scenario: Unassign image from SKU
- **WHEN** user clicks "取消分配" on an assigned SKU image
- **THEN** the system calls unassign API to delete the ref
- **THEN** the image is removed from the SKU panel (but remains in SPU pool)

### Requirement: Listing binding panel
The system SHALL provide a section within the media tab showing platform-specific image bindings. Users SHALL be able to assign images to specific Listing slots (MAIN, PT01~PT08) for each platform/marketplace combination.

#### Scenario: View Listing bindings
- **WHEN** user expands the "Listing 绑定" section in the media tab
- **THEN** a panel shows platform/marketplace combinations where this SKU has listings
- **THEN** each combination shows image slots (MAIN, PT01~PT08) with assigned images or empty placeholders

#### Scenario: Assign image to Listing slot
- **WHEN** user drags an image from the SKU display grid to the PT01 slot for Amazon US
- **THEN** the system updates the ref record's platform='amazon', marketplace_id='ATVPDKIKX0DER', slot_position='PT01'
- **THEN** the slot displays the newly assigned image thumbnail

### Requirement: Bulk operations toolbar
The system SHALL provide a toolbar above the media grid with bulk operation buttons: "全选", "批量删除", "批量下载", "上传ZIP", "批量分配". The toolbar appears when the media tab is active in edit mode.

#### Scenario: Bulk delete multiple images
- **WHEN** user selects 3 images via checkboxes and clicks "批量删除"
- **THEN** a confirmation dialog shows "确认删除 3 张图片？"
- **THEN** on confirm, all 3 are deleted (refs removed; media deleted if no other refs exist)
- **THEN** the grid refreshes to remove deleted items

#### Scenario: Bulk download as ZIP
- **WHEN** user selects 5 images and clicks "批量下载"
- **THEN** the system packages selected images into a ZIP and triggers browser download
- **THEN** filenames in the ZIP follow pattern: {sort_order}_{original_filename}

#### Scenario: Bulk assign to SKU
- **WHEN** user selects 4 images in SPU pool and clicks "批量分配"
- **THEN** a dialog shows available child SKUs for selection
- **THEN** selecting a SKU creates 4 ref_type=1 records


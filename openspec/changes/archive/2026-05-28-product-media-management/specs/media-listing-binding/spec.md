## ADDED Requirements

### Requirement: Listing binding via t_erp_media_ref platform fields
The system SHALL manage media-to-listing bindings through the `platform`, `marketplace_id`, and `slot_position` fields in `t_erp_media_ref`. A ref record with non-null platform represents a Listing-level binding. One media asset MAY have multiple refs with different platform/marketplace/slot combinations.

#### Scenario: Bind image to Amazon US MAIN slot
- **WHEN** user assigns image (ref_id=100) to Amazon US Listing with slot_position='MAIN'
- **THEN** the `t_erp_media_ref` record is updated: platform='amazon', marketplace_id='ATVPDKIKX0DER', slot_position='MAIN'

#### Scenario: Same image bound to multiple marketplaces
- **WHEN** image media_id=100 is assigned to both US and UK marketplaces
- **THEN** 2 `t_erp_media_ref` records exist with same media_id + material_id but different marketplace_id values

#### Scenario: Same slot replaced by different image
- **WHEN** material already has ref_id=100 at slot_position='MAIN' for Amazon US, and user assigns a different image to the same slot
- **THEN** the old ref_id=100 record's slot_position is cleared (set to NULL) or the record is deleted
- **THEN** a new/updated ref record has the new media at slot_position='MAIN'

### Requirement: Query media by Listing context
The system SHALL support filtering `t_erp_media_ref` by platform + marketplace_id to retrieve Listing-specific image assignments. Query: `GET /erp/api/v1/material/media/list?materialid={id}&platform={p}&marketplace_id={m}` returns only refs matching the platform/marketplace filter, ordered by slot_position.

#### Scenario: Query Amazon US Listing media
- **WHEN** user queries with materialid='12345', platform='amazon', marketplace_id='ATVPDKIKX0DER'
- **THEN** the response returns refs with matching platform/marketplace, showing which slots (MAIN/PT01~PT08) have assigned images
- **THEN** each record includes full media details (url, thumbnail, dimensions)

#### Scenario: Query with no Listing bindings
- **WHEN** user queries with platform filter but no refs have that platform assigned
- **THEN** the response returns an empty list (no Listing-specific bindings exist)

### Requirement: Batch bind media to Listing slots
The system SHALL support `POST /erp/api/v1/material/media/bindListing` accepting parameters: material_id, platform, marketplace_id, and bindings (array of {media_id, slot_position}). The system SHALL update existing refs or create new ones to match the specified slot assignments.

#### Scenario: Assign 7 images to Amazon Listing variants
- **WHEN** user submits bindings [{media_id:1, slot:'MAIN'}, {media_id:2, slot:'PT01'}, ..., {media_id:7, slot:'PT06'}] for material in Amazon US
- **THEN** 7 `t_erp_media_ref` records are created/updated with platform='amazon', marketplace_id='ATVPDKIKX0DER', and respective slot_position values
- **THEN** any previously bound images at those slots (for the same platform/marketplace) are unbound

#### Scenario: Clear all Listing bindings
- **WHEN** user submits an empty bindings array for a material+platform+marketplace
- **THEN** all refs with that platform/marketplace have their slot_position cleared (or refs are deleted)
- **THEN** the images remain in the SKU display set (ref_type=1) but without platform-specific assignment

### Requirement: Query which Listings use a specific media
The system SHALL support querying all `t_erp_media_ref` records for a given media_id that have non-null platform fields: effectively showing which Listings reference a specific image.

#### Scenario: Image used in 3 Listing slots
- **WHEN** user queries usedBy for media_id=100 which has refs for Amazon US (MAIN), Amazon UK (MAIN), Amazon DE (PT01)
- **THEN** the response contains 3 records with their platform/marketplace/slot details

#### Scenario: Unused image (no Listing bindings)
- **WHEN** user queries usedBy for a media that only has generic refs (platform=NULL)
- **THEN** the response returns an empty list

### Requirement: Auto-create Listing bindings on Amazon sync
The system SHALL automatically set platform/marketplace_id/slot_position on `t_erp_media_ref` records when Amazon images are synced via `syncFromAmazon`. The slot_position SHALL match the Amazon image variant (MAIN/PT01/etc.).

#### Scenario: Amazon sync creates Listing-bound refs
- **WHEN** user triggers syncFromAmazon and 4 Amazon images (MAIN, PT01, PT02, PT03) are synced
- **THEN** 4 `t_erp_media_ref` records are created with platform='amazon', marketplace_id set from the sync source, slot_position matching variant
- **THEN** these refs represent both SKU display images (ref_type=1) AND Listing bindings simultaneously

### Requirement: Prevent deletion of media with active Listing bindings
The system SHALL check `t_erp_media_ref` for refs with non-null platform before allowing media deletion. If platform-bound refs exist, the deletion SHALL warn the user.

#### Scenario: Delete media with active Listing bindings (no force)
- **WHEN** user attempts to delete media_id=100 which has 2 platform-bound refs
- **THEN** the system returns HTTP 400 with message "该图片已绑定到 2 个 Listing 图片位，请先解绑或使用强制删除"

#### Scenario: Delete media with Listing bindings (force=true)
- **WHEN** user attempts to delete media_id=100 with force=true
- **THEN** all refs (including platform-bound ones) for media_id=100 are deleted
- **THEN** the media record itself is deleted
- **THEN** affected Listing slots become empty

### Requirement: Multi-platform differentiation support
The system SHALL support different image sets for different platforms/marketplaces for the same material. A SKU can have its Amazon US images differ from Amazon DE or TikTok images.

#### Scenario: Different images for US vs DE marketplace
- **WHEN** user assigns image A as MAIN for Amazon US and image B as MAIN for Amazon DE
- **THEN** two separate refs exist: one with marketplace_id='ATVPDKIKX0DER' and one with 'A1PA6795UKMFR9'
- **THEN** querying by marketplace returns the respective image for each

#### Scenario: Generic images (no platform binding)
- **WHEN** user uploads images without assigning to any platform
- **THEN** refs have platform=NULL, marketplace_id=NULL, slot_position=NULL
- **THEN** these images are shown in the general SKU display but not tied to any Listing

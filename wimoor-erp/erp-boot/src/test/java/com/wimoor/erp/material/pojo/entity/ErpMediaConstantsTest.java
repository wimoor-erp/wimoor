package com.wimoor.erp.material.pojo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * 媒体实体常量与状态分类的纯函数单元测试。
 * 不依赖 Spring/DB；用于保证 OpenSpec 提案中定义的状态码不会被无意改动。
 */
public class ErpMediaConstantsTest {

    @Test
    public void mediaTypeConstantsAreStable() {
        assertEquals(0, ErpMedia.MEDIA_TYPE_IMAGE);
        assertEquals(1, ErpMedia.MEDIA_TYPE_VIDEO);
    }

    @Test
    public void usageTypeMatchesProposal() {
        // 与 openspec/changes/product-media-management 设计保持一致
        assertEquals(10, ErpMedia.USAGE_REFERENCE);
        assertEquals(30, ErpMedia.USAGE_ORIGINAL);
        assertEquals(40, ErpMedia.USAGE_FINISHED);
        assertEquals(60, ErpMedia.USAGE_SHOWCASE);
        assertEquals(70, ErpMedia.USAGE_PUBLIC);
        assertEquals(90, ErpMedia.USAGE_INSTRUCTION);
        assertEquals(100, ErpMedia.USAGE_SCENE);
    }

    @Test
    public void sourceConstantsAreDistinct() {
        assertEquals(1, ErpMedia.SOURCE_REFERENCE);
        assertEquals(6, ErpMedia.SOURCE_AI_GENERATED);
        assertNotEquals(ErpMedia.SOURCE_REFERENCE, ErpMedia.SOURCE_AMAZON_SYNC);
    }

    @Test
    public void processStatusFlow() {
        assertEquals(0, ErpMedia.PROCESS_NONE);
        assertEquals(3, ErpMedia.PROCESS_FAILED);
    }

    @Test
    public void refTypeAndMainFlagsAreStable() {
        assertEquals(0, ErpMediaRef.REF_TYPE_SPU_POOL);
        assertEquals(1, ErpMediaRef.REF_TYPE_SKU_DISPLAY);
        assertEquals(1, ErpMediaRef.MAIN_YES);
        assertEquals(0, ErpMediaRef.MAIN_NO);
        assertEquals("MAIN", ErpMediaRef.SLOT_MAIN);
        assertEquals("amazon", ErpMediaRef.PLATFORM_AMAZON);
    }
}

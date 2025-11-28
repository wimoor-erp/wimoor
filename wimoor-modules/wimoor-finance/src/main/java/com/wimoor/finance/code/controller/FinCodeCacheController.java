package com.wimoor.finance.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.finance.code.domain.FinCodeCache;
import com.wimoor.finance.code.service.IFinCodeCacheService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码缓存Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/cache")
public class FinCodeCacheController extends BaseController
{
    @Autowired
    private IFinCodeCacheService finCodeCacheService;

    /**
     * 查询编码缓存列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeCache finCodeCache)
    {
        startPage();
        List<FinCodeCache> list = finCodeCacheService.selectFinCodeCacheList(finCodeCache);
        return getDataTable(list);
    }

    /**
     * 导出编码缓存列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeCache finCodeCache)
    {
        List<FinCodeCache> list = finCodeCacheService.selectFinCodeCacheList(finCodeCache);
        ExcelUtil<FinCodeCache> util = new ExcelUtil<FinCodeCache>(FinCodeCache.class);
        util.exportExcel(response, list, "编码缓存数据");
    }

    /**
     * 获取编码缓存详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeCacheService.selectFinCodeCacheById(id));
    }

    /**
     * 新增编码缓存
     */
    @PostMapping
    public Result add(@RequestBody FinCodeCache finCodeCache)
    {
        return toResult(finCodeCacheService.insertFinCodeCache(finCodeCache));
    }

    /**
     * 修改编码缓存
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeCache finCodeCache)
    {
        return toResult(finCodeCacheService.updateFinCodeCache(finCodeCache));
    }

    /**
     * 删除编码缓存
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeCacheService.deleteFinCodeCacheByIds(ids));
    }
}

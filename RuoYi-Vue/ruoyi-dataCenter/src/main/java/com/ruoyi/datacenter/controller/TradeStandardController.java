package com.ruoyi.datacenter.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.datacenter.domain.TradeStandard;
import com.ruoyi.datacenter.service.ITradeStandardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 行业标准管理Controller
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
@RestController
@RequestMapping("/datacenter/standard")
public class TradeStandardController extends BaseController
{
    @Autowired
    private ITradeStandardService tradeStandardService;

    /**
     * 查询行业标准管理列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:list')")
    @GetMapping("/list")
    public TableDataInfo list(TradeStandard tradeStandard)
    {
        startPage();
        List<TradeStandard> list = tradeStandardService.selectTradeStandardList(tradeStandard);
        return getDataTable(list);
    }

    /**
     * 导出行业标准管理列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:export')")
    @Log(title = "行业标准管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TradeStandard tradeStandard)
    {
        List<TradeStandard> list = tradeStandardService.selectTradeStandardList(tradeStandard);
        ExcelUtil<TradeStandard> util = new ExcelUtil<TradeStandard>(TradeStandard.class);
        util.exportExcel(response, list, "行业标准管理数据");
    }

    /**
     * 获取行业标准管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tradeStandardService.selectTradeStandardById(id));
    }

    /**
     * 新增行业标准管理
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:add')")
    @Log(title = "行业标准管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TradeStandard tradeStandard)
    {
        return toAjax(tradeStandardService.insertTradeStandard(tradeStandard));
    }

    /**
     * 修改行业标准管理
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:edit')")
    @Log(title = "行业标准管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TradeStandard tradeStandard)
    {
        return toAjax(tradeStandardService.updateTradeStandard(tradeStandard));
    }

    /**
     * 删除行业标准管理
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:remove')")
    @Log(title = "行业标准管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tradeStandardService.deleteTradeStandardByIds(ids));
    }
}

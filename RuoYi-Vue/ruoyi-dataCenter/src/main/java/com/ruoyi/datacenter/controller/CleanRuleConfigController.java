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
import com.ruoyi.datacenter.domain.CleanRuleConfig;
import com.ruoyi.datacenter.service.ICleanRuleConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 清洗规则配置Controller
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
@RestController
@RequestMapping("/datacenter/cleanRule")
public class CleanRuleConfigController extends BaseController
{
    @Autowired
    private ICleanRuleConfigService cleanRuleConfigService;

    /**
     * 查询清洗规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(CleanRuleConfig cleanRuleConfig)
    {
        startPage();
        List<CleanRuleConfig> list = cleanRuleConfigService.selectCleanRuleConfigList(cleanRuleConfig);
        return getDataTable(list);
    }

    /**
     * 导出清洗规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:export')")
    @Log(title = "清洗规则配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CleanRuleConfig cleanRuleConfig)
    {
        List<CleanRuleConfig> list = cleanRuleConfigService.selectCleanRuleConfigList(cleanRuleConfig);
        ExcelUtil<CleanRuleConfig> util = new ExcelUtil<CleanRuleConfig>(CleanRuleConfig.class);
        util.exportExcel(response, list, "清洗规则配置数据");
    }

    /**
     * 获取清洗规则配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cleanRuleConfigService.selectCleanRuleConfigById(id));
    }

    /**
     * 新增清洗规则配置
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:add')")
    @Log(title = "清洗规则配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CleanRuleConfig cleanRuleConfig)
    {
        return toAjax(cleanRuleConfigService.insertCleanRuleConfig(cleanRuleConfig));
    }

    /**
     * 修改清洗规则配置
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:edit')")
    @Log(title = "清洗规则配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CleanRuleConfig cleanRuleConfig)
    {
        return toAjax(cleanRuleConfigService.updateCleanRuleConfig(cleanRuleConfig));
    }

    /**
     * 删除清洗规则配置
     */
    @PreAuthorize("@ss.hasPermi('datacenter:cleanRule:remove')")
    @Log(title = "清洗规则配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cleanRuleConfigService.deleteCleanRuleConfigByIds(ids));
    }
}

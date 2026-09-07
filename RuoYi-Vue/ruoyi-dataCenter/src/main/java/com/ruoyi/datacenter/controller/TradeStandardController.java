package com.ruoyi.datacenter.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.datacenter.domain.TradeStandard;
import com.ruoyi.datacenter.service.ITradeStandardService;

/**
 * 行业标准管理Controller
 *
 * 关键业务约束（前端表单应遵守）：
 *  - 新增/修改表单不应包含以下系统字段，由后端自动填充：
 *      creator / creatorName / modifier / modifierName / delFlag / createTime / updateTime
 *  - 删除采用软删除，前端调用 remove 后 del_flag 置 1，列表/详情查询自动过滤
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
     * 查询行业标准管理列表（自动过滤已删除数据）
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
     * 导入行业标准管理数据
     *
     * @param file 待导入的 Excel 文件
     * @param updateSupport 是否支持更新（已存在同名标准时）
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:import')")
    @Log(title = "行业标准管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<TradeStandard> util = new ExcelUtil<TradeStandard>(TradeStandard.class);
        List<TradeStandard> list = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = tradeStandardService.importTradeStandard(list, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载行业标准管理导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<TradeStandard> util = new ExcelUtil<TradeStandard>(TradeStandard.class);
        util.importTemplateExcel(response, "行业标准管理数据");
    }

    /**
     * 获取行业标准管理详细信息（仅未删除）
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tradeStandardService.selectTradeStandardById(id));
    }

    /**
     * 新增行业标准管理
     * 审计字段（creator/creatorName/createTime）由 Service 自动填充
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:add')")
    @Log(title = "行业标准管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TradeStandard tradeStandard)
    {
        return toAjax(tradeStandardService.insertTradeStandard(tradeStandard));
    }

    /**
     * 修改行业标准管理
     * 审计字段（modifier/modifierName/updateTime）由 Service 自动填充
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:edit')")
    @Log(title = "行业标准管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TradeStandard tradeStandard)
    {
        return toAjax(tradeStandardService.updateTradeStandard(tradeStandard));
    }

    /**
     * 删除行业标准管理（软删除：del_flag=1）
     * 物理数据保留以供审计追溯，列表/详情查询自动过滤已删除记录
     */
    @PreAuthorize("@ss.hasPermi('datacenter:standard:remove')")
    @Log(title = "行业标准管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 软删除：记录修改人，便于追溯
        return toAjax(tradeStandardService.softDeleteTradeStandardByIds(ids,
                SecurityUtils.getUserId(), SecurityUtils.getUsername()));
    }
}

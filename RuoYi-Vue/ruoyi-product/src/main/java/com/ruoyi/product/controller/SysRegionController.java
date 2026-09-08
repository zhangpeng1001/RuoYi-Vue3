package com.ruoyi.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.product.domain.SysRegion;
import com.ruoyi.product.service.ISysRegionService;

/**
 * 省市区字典 Controller
 *
 * 接口约定：
 *  - GET /region/provinces          获取所有省份
 *  - GET /region/cities?parentCode= 根据省编码获取城市列表
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@RestController
@RequestMapping("/region")
public class SysRegionController extends BaseController
{
    @Autowired
    private ISysRegionService sysRegionService;

    /** 获取所有省份 */
    @GetMapping("/provinces")
    public AjaxResult provinces()
    {
        List<SysRegion> list = sysRegionService.selectProvinces();
        return success(list);
    }

    /** 根据省编码获取城市列表 */
    @GetMapping("/cities")
    public AjaxResult cities(@RequestParam("parentCode") String parentCode)
    {
        List<SysRegion> list = sysRegionService.selectCitiesByParentCode(parentCode);
        return success(list);
    }
}

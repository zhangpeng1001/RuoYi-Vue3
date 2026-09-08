package com.ruoyi.product.service;

import java.util.List;
import com.ruoyi.product.domain.SysRegion;

/**
 * 省市区字典 Service 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface ISysRegionService
{
    /** 查询所有省份 */
    public List<SysRegion> selectProvinces();

    /** 按父级编码查询城市 */
    public List<SysRegion> selectCitiesByParentCode(String parentCode);
}

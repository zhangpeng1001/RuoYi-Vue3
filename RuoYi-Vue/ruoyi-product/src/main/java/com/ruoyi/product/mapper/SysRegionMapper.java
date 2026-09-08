package com.ruoyi.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.SysRegion;

/**
 * 省市区字典 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface SysRegionMapper
{
    /** 查询所有省份（level=1） */
    public List<SysRegion> selectProvinces();

    /** 按父级编码查询城市列表（level=2） */
    public List<SysRegion> selectCitiesByParentCode(@Param("parentCode") String parentCode);
}

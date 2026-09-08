package com.ruoyi.product.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.product.domain.SysRegion;
import com.ruoyi.product.mapper.SysRegionMapper;
import com.ruoyi.product.service.ISysRegionService;

/**
 * 省市区字典 Service 实现
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@Service
public class SysRegionServiceImpl implements ISysRegionService
{
    @Autowired
    private SysRegionMapper sysRegionMapper;

    @Override
    public List<SysRegion> selectProvinces()
    {
        return sysRegionMapper.selectProvinces();
    }

    @Override
    public List<SysRegion> selectCitiesByParentCode(String parentCode)
    {
        return sysRegionMapper.selectCitiesByParentCode(parentCode);
    }
}

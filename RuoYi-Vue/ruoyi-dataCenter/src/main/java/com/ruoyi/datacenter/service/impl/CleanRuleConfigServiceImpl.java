package com.ruoyi.datacenter.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.datacenter.mapper.CleanRuleConfigMapper;
import com.ruoyi.datacenter.domain.CleanRuleConfig;
import com.ruoyi.datacenter.service.ICleanRuleConfigService;

/**
 * 清洗规则配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
@Service
public class CleanRuleConfigServiceImpl implements ICleanRuleConfigService 
{
    @Autowired
    private CleanRuleConfigMapper cleanRuleConfigMapper;

    /**
     * 查询清洗规则配置
     * 
     * @param id 清洗规则配置主键
     * @return 清洗规则配置
     */
    @Override
    public CleanRuleConfig selectCleanRuleConfigById(Long id)
    {
        return cleanRuleConfigMapper.selectCleanRuleConfigById(id);
    }

    /**
     * 查询清洗规则配置列表
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 清洗规则配置
     */
    @Override
    public List<CleanRuleConfig> selectCleanRuleConfigList(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.selectCleanRuleConfigList(cleanRuleConfig);
    }

    /**
     * 新增清洗规则配置
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 结果
     */
    @Override
    public int insertCleanRuleConfig(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.insertCleanRuleConfig(cleanRuleConfig);
    }

    /**
     * 修改清洗规则配置
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 结果
     */
    @Override
    public int updateCleanRuleConfig(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.updateCleanRuleConfig(cleanRuleConfig);
    }

    /**
     * 批量删除清洗规则配置
     * 
     * @param ids 需要删除的清洗规则配置主键
     * @return 结果
     */
    @Override
    public int deleteCleanRuleConfigByIds(Long[] ids)
    {
        return cleanRuleConfigMapper.deleteCleanRuleConfigByIds(ids);
    }

    /**
     * 删除清洗规则配置信息
     * 
     * @param id 清洗规则配置主键
     * @return 结果
     */
    @Override
    public int deleteCleanRuleConfigById(Long id)
    {
        return cleanRuleConfigMapper.deleteCleanRuleConfigById(id);
    }
}

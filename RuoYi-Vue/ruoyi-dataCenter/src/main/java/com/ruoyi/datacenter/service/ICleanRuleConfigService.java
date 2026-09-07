package com.ruoyi.datacenter.service;

import java.util.List;
import com.ruoyi.datacenter.domain.CleanRuleConfig;

/**
 * 清洗规则配置Service接口
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
public interface ICleanRuleConfigService 
{
    /**
     * 查询清洗规则配置
     * 
     * @param id 清洗规则配置主键
     * @return 清洗规则配置
     */
    public CleanRuleConfig selectCleanRuleConfigById(Long id);

    /**
     * 查询清洗规则配置列表
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 清洗规则配置集合
     */
    public List<CleanRuleConfig> selectCleanRuleConfigList(CleanRuleConfig cleanRuleConfig);

    /**
     * 新增清洗规则配置
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 结果
     */
    public int insertCleanRuleConfig(CleanRuleConfig cleanRuleConfig);

    /**
     * 修改清洗规则配置
     * 
     * @param cleanRuleConfig 清洗规则配置
     * @return 结果
     */
    public int updateCleanRuleConfig(CleanRuleConfig cleanRuleConfig);

    /**
     * 批量删除清洗规则配置
     * 
     * @param ids 需要删除的清洗规则配置主键集合
     * @return 结果
     */
    public int deleteCleanRuleConfigByIds(Long[] ids);

    /**
     * 删除清洗规则配置信息
     * 
     * @param id 清洗规则配置主键
     * @return 结果
     */
    public int deleteCleanRuleConfigById(Long id);
}

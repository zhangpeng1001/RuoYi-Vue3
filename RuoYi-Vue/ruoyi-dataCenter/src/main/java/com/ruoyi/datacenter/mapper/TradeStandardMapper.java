package com.ruoyi.datacenter.mapper;

import java.util.List;
import com.ruoyi.datacenter.domain.TradeStandard;

/**
 * 行业标准管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
public interface TradeStandardMapper 
{
    /**
     * 查询行业标准管理
     * 
     * @param id 行业标准管理主键
     * @return 行业标准管理
     */
    public TradeStandard selectTradeStandardById(Long id);

    /**
     * 查询行业标准管理列表
     * 
     * @param tradeStandard 行业标准管理
     * @return 行业标准管理集合
     */
    public List<TradeStandard> selectTradeStandardList(TradeStandard tradeStandard);

    /**
     * 新增行业标准管理
     * 
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    public int insertTradeStandard(TradeStandard tradeStandard);

    /**
     * 修改行业标准管理
     * 
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    public int updateTradeStandard(TradeStandard tradeStandard);

    /**
     * 删除行业标准管理
     * 
     * @param id 行业标准管理主键
     * @return 结果
     */
    public int deleteTradeStandardById(Long id);

    /**
     * 批量删除行业标准管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTradeStandardByIds(Long[] ids);
}

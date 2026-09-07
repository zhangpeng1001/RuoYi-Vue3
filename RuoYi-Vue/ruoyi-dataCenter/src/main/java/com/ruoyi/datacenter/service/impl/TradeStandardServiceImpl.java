package com.ruoyi.datacenter.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.datacenter.mapper.TradeStandardMapper;
import com.ruoyi.datacenter.domain.TradeStandard;
import com.ruoyi.datacenter.service.ITradeStandardService;

/**
 * 行业标准管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
@Service
public class TradeStandardServiceImpl implements ITradeStandardService 
{
    @Autowired
    private TradeStandardMapper tradeStandardMapper;

    /**
     * 查询行业标准管理
     * 
     * @param id 行业标准管理主键
     * @return 行业标准管理
     */
    @Override
    public TradeStandard selectTradeStandardById(Long id)
    {
        return tradeStandardMapper.selectTradeStandardById(id);
    }

    /**
     * 查询行业标准管理列表
     * 
     * @param tradeStandard 行业标准管理
     * @return 行业标准管理
     */
    @Override
    public List<TradeStandard> selectTradeStandardList(TradeStandard tradeStandard)
    {
        return tradeStandardMapper.selectTradeStandardList(tradeStandard);
    }

    /**
     * 新增行业标准管理
     * 
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    @Override
    public int insertTradeStandard(TradeStandard tradeStandard)
    {
        tradeStandard.setCreateTime(DateUtils.getNowDate());
        return tradeStandardMapper.insertTradeStandard(tradeStandard);
    }

    /**
     * 修改行业标准管理
     * 
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    @Override
    public int updateTradeStandard(TradeStandard tradeStandard)
    {
        tradeStandard.setUpdateTime(DateUtils.getNowDate());
        return tradeStandardMapper.updateTradeStandard(tradeStandard);
    }

    /**
     * 批量删除行业标准管理
     * 
     * @param ids 需要删除的行业标准管理主键
     * @return 结果
     */
    @Override
    public int deleteTradeStandardByIds(Long[] ids)
    {
        return tradeStandardMapper.deleteTradeStandardByIds(ids);
    }

    /**
     * 删除行业标准管理信息
     * 
     * @param id 行业标准管理主键
     * @return 结果
     */
    @Override
    public int deleteTradeStandardById(Long id)
    {
        return tradeStandardMapper.deleteTradeStandardById(id);
    }
}

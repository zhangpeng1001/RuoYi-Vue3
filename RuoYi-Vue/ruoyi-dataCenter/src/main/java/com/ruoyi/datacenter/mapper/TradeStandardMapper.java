package com.ruoyi.datacenter.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.datacenter.domain.TradeStandard;

/**
 * 行业标准管理Mapper接口
 *
 * 说明：
 *  - 列表/详情查询均过滤 del_flag=0
 *  - 软删除由 softDeleteTradeStandardByIds 实现，更新 del_flag=1 及修改人/时间
 *
 * @author ruoyi
 * @date 2026-09-07
 */
public interface TradeStandardMapper
{
    /**
     * 查询行业标准管理（仅未删除）
     *
     * @param id 行业标准管理主键
     * @return 行业标准管理
     */
    public TradeStandard selectTradeStandardById(Long id);

    /**
     * 查询行业标准管理列表（过滤 del_flag=0）
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
     * 修改行业标准管理（仅更新业务字段，不含 del_flag）
     *
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    public int updateTradeStandard(TradeStandard tradeStandard);

    /**
     * 物理删除行业标准管理（仅供内部清理任务使用）
     *
     * @param id 行业标准管理主键
     * @return 结果
     */
    public int deleteTradeStandardById(Long id);

    /**
     * 批量物理删除（仅供内部清理任务使用）
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTradeStandardByIds(Long[] ids);

    /**
     * 批量软删除（更新 del_flag=1，并记录修改人）
     *
     * @param ids 主键集合
     * @param modifier 修改人编码
     * @param modifierName 修改人名称
     * @param updateTime 更新时间
     * @return 结果
     */
    public int softDeleteTradeStandardByIds(@Param("ids") Long[] ids,
                                            @Param("modifier") Long modifier,
                                            @Param("modifierName") String modifierName,
                                            @Param("updateTime") Date updateTime);
}

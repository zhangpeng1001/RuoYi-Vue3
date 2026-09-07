package com.ruoyi.datacenter.service;

import java.util.List;
import com.ruoyi.datacenter.domain.TradeStandard;

/**
 * 行业标准管理Service接口
 *
 * 业务约定：
 *  - 标准名称（tradeStandardName）在未删除范围内全局唯一
 *  - 删除采用软删除（del_flag=1），列表/详情查询自动过滤 del_flag=0
 *  - 审计字段（creator/modifier/creatorName/modifierName）由 Service 自动填充，不由前端维护
 *
 * @author ruoyi
 * @date 2026-09-07
 */
public interface ITradeStandardService
{
    /**
     * 查询行业标准管理（仅未删除）
     *
     * @param id 行业标准管理主键
     * @return 行业标准管理
     */
    public TradeStandard selectTradeStandardById(Long id);

    /**
     * 查询行业标准管理列表（自动过滤已删除数据）
     *
     * @param tradeStandard 行业标准管理查询条件
     * @return 行业标准管理集合
     */
    public List<TradeStandard> selectTradeStandardList(TradeStandard tradeStandard);

    /**
     * 新增行业标准管理
     * 业务逻辑：
     *  1) 校验标准名称唯一性
     *  2) 校验IP格式（非空时）
     *  3) 自动填充审计字段 creator/creatorName/createTime
     *
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    public int insertTradeStandard(TradeStandard tradeStandard);

    /**
     * 修改行业标准管理
     * 业务逻辑：
     *  1) 校验标准名称唯一性（排除自身）
     *  2) 校验IP格式（非空时）
     *  3) 自动填充审计字段 modifier/modifierName/updateTime
     *
     * @param tradeStandard 行业标准管理
     * @return 结果
     */
    public int updateTradeStandard(TradeStandard tradeStandard);

    /**
     * 批量软删除行业标准管理（更新 del_flag=1，并记录 modifier/modifierName）
     *
     * @param ids 需要软删除的行业标准管理主键集合
     * @param modifier 操作人编码
     * @param modifierName 操作人名称
     * @return 结果
     */
    public int softDeleteTradeStandardByIds(Long[] ids, Long modifier, String modifierName);

    /**
     * 删除行业标准管理信息（物理删除，仅内部清理任务使用，前端不暴露）
     *
     * @param id 行业标准管理主键
     * @return 结果
     */
    public int deleteTradeStandardById(Long id);

    /**
     * 批量删除行业标准管理（物理删除，仅内部清理任务使用，前端不暴露）
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTradeStandardByIds(Long[] ids);

    /**
     * 校验标准名称是否唯一
     *
     * @param tradeStandard 待校验对象（新增时 id 为 null，修改时需带上自身 id 以排除自身）
     * @return true 唯一可用，false 已存在
     */
    public boolean checkTradeStandardNameUnique(TradeStandard tradeStandard);

    /**
     * 批量导入行业标准数据
     *
     * @param tradeStandardList 待导入数据
     * @param isUpdateSupport 是否支持更新（已存在同名标准时）
     * @param operName 操作人账号
     * @return 结果提示消息
     */
    public String importTradeStandard(List<TradeStandard> tradeStandardList, Boolean isUpdateSupport, String operName);
}

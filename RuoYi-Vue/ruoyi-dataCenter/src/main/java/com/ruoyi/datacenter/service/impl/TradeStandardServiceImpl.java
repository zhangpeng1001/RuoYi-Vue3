package com.ruoyi.datacenter.service.impl;

import java.util.List;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.datacenter.mapper.TradeStandardMapper;
import com.ruoyi.datacenter.domain.TradeStandard;
import com.ruoyi.datacenter.service.ITradeStandardService;

/**
 * 行业标准管理Service业务层处理
 *
 * 核心业务规则：
 *  1. 标准名称在未删除数据范围内全局唯一，新增/修改前校验
 *  2. 审计字段（creator/creatorName/modifier/modifierName）由本层自动从当前登录用户填充
 *  3. 删除走软删除（del_flag=1），保留数据可追溯；物理删除仅供内部清理任务调用
 *  4. 列表/详情查询自动过滤 del_flag=0
 *  5. IP 字段非空时校验 IPv4 格式
 *
 * @author ruoyi
 * @date 2026-09-07
 */
@Service
public class TradeStandardServiceImpl implements ITradeStandardService
{
    private static final Logger log = LoggerFactory.getLogger(TradeStandardServiceImpl.class);

    @Autowired
    private TradeStandardMapper tradeStandardMapper;

    /** JSR303 校验器，用于导入数据时按对象注解校验 */
    @Autowired
    protected Validator validator;

    /**
     * 查询行业标准管理（仅未删除）
     */
    @Override
    public TradeStandard selectTradeStandardById(Long id)
    {
        return tradeStandardMapper.selectTradeStandardById(id);
    }

    /**
     * 查询行业标准管理列表（自动过滤已删除数据）
     */
    @Override
    public List<TradeStandard> selectTradeStandardList(TradeStandard tradeStandard)
    {
        // 强制只查询未删除数据，避免 del_flag 被前端覆盖
        tradeStandard.setDelFlag(0);
        return tradeStandardMapper.selectTradeStandardList(tradeStandard);
    }

    /**
     * 新增行业标准管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTradeStandard(TradeStandard tradeStandard)
    {
        // 业务校验：标准名称唯一
        if (!checkTradeStandardNameUnique(tradeStandard))
        {
            throw new ServiceException("新增标准'" + tradeStandard.getTradeStandardName() + "'失败，标准名称已存在");
        }
        // 业务校验：IP 格式（非空时）
        if (!tradeStandard.isIpValid())
        {
            throw new ServiceException("新增标准'" + tradeStandard.getTradeStandardName() + "'失败，数据源IP格式不正确");
        }
        // 自动填充审计字段
        tradeStandard.setCreator(SecurityUtils.getUserId());
        tradeStandard.setCreatorName(SecurityUtils.getUsername());
        tradeStandard.setCreateTime(DateUtils.getNowDate());
        // 新增数据默认未删除
        tradeStandard.setDelFlag(0);
        return tradeStandardMapper.insertTradeStandard(tradeStandard);
    }

    /**
     * 修改行业标准管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTradeStandard(TradeStandard tradeStandard)
    {
        // 业务校验：标准名称唯一（排除自身）
        if (!checkTradeStandardNameUnique(tradeStandard))
        {
            throw new ServiceException("修改标准'" + tradeStandard.getTradeStandardName() + "'失败，标准名称已存在");
        }
        // 业务校验：IP 格式（非空时）
        if (!tradeStandard.isIpValid())
        {
            throw new ServiceException("修改标准'" + tradeStandard.getTradeStandardName() + "'失败，数据源IP格式不正确");
        }
        // 自动填充审计字段（修改人）
        tradeStandard.setModifier(SecurityUtils.getUserId());
        tradeStandard.setModifierName(SecurityUtils.getUsername());
        tradeStandard.setUpdateTime(DateUtils.getNowDate());
        // 防止通过修改接口篡改 del_flag，强制不更新该字段（update 语句已不含 del_flag）
        return tradeStandardMapper.updateTradeStandard(tradeStandard);
    }

    /**
     * 批量软删除行业标准管理（更新 del_flag=1，并记录修改人）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int softDeleteTradeStandardByIds(Long[] ids, Long modifier, String modifierName)
    {
        return tradeStandardMapper.softDeleteTradeStandardByIds(ids, modifier, modifierName, DateUtils.getNowDate());
    }

    /**
     * 物理删除行业标准管理（仅供内部清理任务使用）
     */
    @Override
    public int deleteTradeStandardById(Long id)
    {
        return tradeStandardMapper.deleteTradeStandardById(id);
    }

    /**
     * 批量物理删除（仅供内部清理任务使用）
     */
    @Override
    public int deleteTradeStandardByIds(Long[] ids)
    {
        return tradeStandardMapper.deleteTradeStandardByIds(ids);
    }

    /**
     * 校验标准名称是否唯一
     *
     * @param tradeStandard 待校验对象
     * @return true 唯一可用；false 已存在
     */
    @Override
    public boolean checkTradeStandardNameUnique(TradeStandard tradeStandard)
    {
        Long id = StringUtils.isNull(tradeStandard.getId()) ? -1L : tradeStandard.getId();
        // 仅在未删除范围内校验唯一性
        TradeStandard query = new TradeStandard();
        query.setTradeStandardName(tradeStandard.getTradeStandardName());
        query.setDelFlag(0);
        List<TradeStandard> list = tradeStandardMapper.selectTradeStandardList(query);
        // 不存在同名 或 同名记录就是自身（修改场景）
        return list.isEmpty() || list.stream().allMatch(item -> item.getId().equals(id));
    }

    /**
     * 批量导入行业标准数据
     *
     * @param tradeStandardList 待导入数据
     * @param isUpdateSupport 是否支持更新（已存在同名标准时）
     * @param operName 操作人账号（仅用于日志，审计字段以登录态为准）
     * @return 结果提示消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importTradeStandard(List<TradeStandard> tradeStandardList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(tradeStandardList) || tradeStandardList.isEmpty())
        {
            throw new ServiceException("导入行业标准数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (TradeStandard item : tradeStandardList)
        {
            try
            {
                // JSR303 校验
                BeanValidators.validateWithException(validator, item);
                // 校验名称唯一性（基于未删除数据范围）
                TradeStandard query = new TradeStandard();
                query.setTradeStandardName(item.getTradeStandardName());
                query.setDelFlag(0);
                List<TradeStandard> exists = tradeStandardMapper.selectTradeStandardList(query);
                if (exists.isEmpty())
                {
                    // 新增：自动填充审计字段
                    item.setCreator(SecurityUtils.getUserId());
                    item.setCreatorName(SecurityUtils.getUsername());
                    item.setCreateTime(DateUtils.getNowDate());
                    item.setDelFlag(0);
                    tradeStandardMapper.insertTradeStandard(item);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、标准 ").append(item.getTradeStandardName()).append(" 导入成功");
                }
                else if (isUpdateSupport)
                {
                    // 更新：以已存在记录的主键为准，仅更新业务字段
                    TradeStandard existing = exists.get(0);
                    item.setId(existing.getId());
                    item.setModifier(SecurityUtils.getUserId());
                    item.setModifierName(SecurityUtils.getUsername());
                    item.setUpdateTime(DateUtils.getNowDate());
                    tradeStandardMapper.updateTradeStandard(item);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、标准 ").append(item.getTradeStandardName()).append(" 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、标准 ").append(item.getTradeStandardName()).append(" 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String name = item.getTradeStandardName() == null ? "" : item.getTradeStandardName();
                String msg = "<br/>" + failureNum + "、标准 " + name + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}

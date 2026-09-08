package com.ruoyi.product.mapper;

import java.util.List;
import com.ruoyi.product.domain.Agent;

/**
 * 代理商 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface AgentMapper
{
    /**
     * 按关键字模糊查询启用状态的代理商（仅未删除）
     * keyword 为空时返回全部启用代理商
     */
    public List<Agent> selectAgentList(Agent agent);
}

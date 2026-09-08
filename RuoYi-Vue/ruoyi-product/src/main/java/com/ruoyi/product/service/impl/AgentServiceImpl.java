package com.ruoyi.product.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.product.domain.Agent;
import com.ruoyi.product.mapper.AgentMapper;
import com.ruoyi.product.service.IAgentService;

/**
 * 代理商 Service 实现
 *
 * 说明：查询时强制只返回启用(status=1)且未删除(is_deleted=0)的代理商
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@Service
public class AgentServiceImpl implements IAgentService
{
    @Autowired
    private AgentMapper agentMapper;

    @Override
    public List<Agent> selectAgentList(Agent agent)
    {
        // 强制启用 + 未删除
        agent.setStatus(1);
        agent.setIsDeleted(0);
        return agentMapper.selectAgentList(agent);
    }
}

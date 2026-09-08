package com.ruoyi.product.service;

import java.util.List;
import com.ruoyi.product.domain.Agent;

/**
 * 代理商 Service 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface IAgentService
{
    /** 按关键字模糊查询启用状态的代理商（仅未删除） */
    public List<Agent> selectAgentList(Agent agent);
}

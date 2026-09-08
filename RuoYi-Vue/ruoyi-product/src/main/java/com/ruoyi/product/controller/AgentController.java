package com.ruoyi.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.product.domain.Agent;
import com.ruoyi.product.service.IAgentService;

/**
 * 代理商 Controller
 *
 * 接口约定：
 *  - GET /agent/search?keyword= 代理商联想搜索（仅启用且未删除）
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@RestController
@RequestMapping("/agent")
public class AgentController extends BaseController
{
    @Autowired
    private IAgentService agentService;

    /** 代理商联想搜索 */
    @GetMapping("/search")
    public AjaxResult search(@RequestParam(value = "keyword", required = false) String keyword)
    {
        Agent query = new Agent();
        query.setAgentName(keyword);
        List<Agent> list = agentService.selectAgentList(query);
        return success(list);
    }
}

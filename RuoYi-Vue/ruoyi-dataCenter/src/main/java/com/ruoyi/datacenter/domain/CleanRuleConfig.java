package com.ruoyi.datacenter.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 清洗规则配置对象 clean_rule_config
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
public class CleanRuleConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 父id */
    @Excel(name = "父id")
    private String parentId;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 排序 */
    @Excel(name = "排序")
    private Long ruleSort;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String ext;

    /** 状态 1：可用；0：不可用 */
    @Excel(name = "状态 1：可用；0：不可用")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setParentId(String parentId) 
    {
        this.parentId = parentId;
    }

    public String getParentId() 
    {
        return parentId;
    }

    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }

    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }

    public void setRuleSort(Long ruleSort) 
    {
        this.ruleSort = ruleSort;
    }

    public Long getRuleSort() 
    {
        return ruleSort;
    }

    public void setExt(String ext) 
    {
        this.ext = ext;
    }

    public String getExt() 
    {
        return ext;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("ruleName", getRuleName())
            .append("ruleCode", getRuleCode())
            .append("ruleSort", getRuleSort())
            .append("ext", getExt())
            .append("remark", getRemark())
            .append("status", getStatus())
            .toString();
    }
}

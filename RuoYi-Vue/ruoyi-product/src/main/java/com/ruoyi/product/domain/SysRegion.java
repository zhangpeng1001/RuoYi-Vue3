package com.ruoyi.product.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 省市区字典对象 sys_region
 *
 * 说明：level=1 省，level=2 市，level=3 区县；parent_code=0 表示省级
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public class SysRegion
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 区域编码 */
    private String regionCode;

    /** 区域名称 */
    private String regionName;

    /** 父级编码（0表示省级） */
    private String parentCode;

    /** 层级：1-省，2-市，3-区县 */
    private Integer level;

    /** 排序 */
    private Integer sortOrder;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getRegionCode()
    {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public String getParentCode()
    {
        return parentCode;
    }

    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("regionCode", getRegionCode())
            .append("regionName", getRegionName())
            .append("parentCode", getParentCode())
            .append("level", getLevel())
            .append("sortOrder", getSortOrder())
            .toString();
    }
}

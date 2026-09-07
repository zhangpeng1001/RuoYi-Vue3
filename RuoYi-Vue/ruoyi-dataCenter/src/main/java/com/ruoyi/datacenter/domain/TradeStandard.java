package com.ruoyi.datacenter.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 行业标准管理对象 trade_standard
 * 
 * @author ruoyi
 * @date 2026-09-07
 */
public class TradeStandard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String tradeStandardName;

    /** 标准类型 1行业标准、2自定义标准 */
    @Excel(name = "标准类型 1行业标准、2自定义标准")
    private Integer type;

    /** 数据源ID */
    @Excel(name = "数据源ID")
    private Long dataSourceId;

    /** 文件的ID：文件夹的ID对应package_storage的id */
    @Excel(name = "文件的ID：文件夹的ID对应package_storage的id")
    private Long forldId;

    /** 提取标准类型 1:file 2:建模数据源 */
    @Excel(name = "提取标准类型 1:file 2:建模数据源")
    private Integer extractType;

    /** 数据源ip */
    @Excel(name = "数据源ip")
    private String ip;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 逻辑删除标识 0 未删除 1 删除 */
    private Integer delFlag;

    /** 创建人编码。创建人指运营系统管理人员 */
    @Excel(name = "创建人编码。创建人指运营系统管理人员")
    private Long creator;

    /** 修改人编码。修改人指运营系统人员 */
    @Excel(name = "修改人编码。修改人指运营系统人员")
    private Long modifier;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String creatorName;

    /** 修改人名称 */
    @Excel(name = "修改人名称")
    private String modifierName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTradeStandardName(String tradeStandardName) 
    {
        this.tradeStandardName = tradeStandardName;
    }

    public String getTradeStandardName() 
    {
        return tradeStandardName;
    }

    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }

    public void setDataSourceId(Long dataSourceId) 
    {
        this.dataSourceId = dataSourceId;
    }

    public Long getDataSourceId() 
    {
        return dataSourceId;
    }

    public void setForldId(Long forldId) 
    {
        this.forldId = forldId;
    }

    public Long getForldId() 
    {
        return forldId;
    }

    public void setExtractType(Integer extractType) 
    {
        this.extractType = extractType;
    }

    public Integer getExtractType() 
    {
        return extractType;
    }

    public void setIp(String ip) 
    {
        this.ip = ip;
    }

    public String getIp() 
    {
        return ip;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDelFlag(Integer delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() 
    {
        return delFlag;
    }

    public void setCreator(Long creator) 
    {
        this.creator = creator;
    }

    public Long getCreator() 
    {
        return creator;
    }

    public void setModifier(Long modifier) 
    {
        this.modifier = modifier;
    }

    public Long getModifier() 
    {
        return modifier;
    }

    public void setCreatorName(String creatorName) 
    {
        this.creatorName = creatorName;
    }

    public String getCreatorName() 
    {
        return creatorName;
    }

    public void setModifierName(String modifierName) 
    {
        this.modifierName = modifierName;
    }

    public String getModifierName() 
    {
        return modifierName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tradeStandardName", getTradeStandardName())
            .append("type", getType())
            .append("dataSourceId", getDataSourceId())
            .append("forldId", getForldId())
            .append("extractType", getExtractType())
            .append("ip", getIp())
            .append("description", getDescription())
            .append("delFlag", getDelFlag())
            .append("creator", getCreator())
            .append("modifier", getModifier())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("creatorName", getCreatorName())
            .append("modifierName", getModifierName())
            .toString();
    }
}

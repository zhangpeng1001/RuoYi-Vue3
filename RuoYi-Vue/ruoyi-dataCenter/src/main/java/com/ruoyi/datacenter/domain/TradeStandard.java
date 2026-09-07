package com.ruoyi.datacenter.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.ip.IpUtils;

/**
 * 行业标准管理对象 trade_standard
 *
 * 业务说明：
 *  - 标准名称在系统内全局唯一（未删除范围内）
 *  - type/extractType 为枚举值，前端用字典下拉展示，后端用 readConverterExp 导出时翻译
 *  - creator/modifier/creatorName/modifierName/delFlag 为系统字段，由后端自动填充，前端不维护
 *  - 删除采用软删除（del_flag=1），列表/详情查询自动过滤已删除数据
 *
 * @author ruoyi
 * @date 2026-09-07
 */
public class TradeStandard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 标准名称（业务唯一） */
    @Excel(name = "标准名称")
    @NotBlank(message = "标准名称不能为空")
    @Size(max = 100, message = "标准名称长度不能超过100个字符")
    private String tradeStandardName;

    /** 标准类型 1行业标准、2自定义标准 */
    @Excel(name = "标准类型", readConverterExp = "1=行业标准,2=自定义标准")
    @NotNull(message = "标准类型不能为空")
    private Integer type;

    /** 数据源ID */
    @Excel(name = "数据源ID")
    private Long dataSourceId;

    /** 文件的ID：文件夹的ID对应package_storage的id */
    @Excel(name = "文件ID")
    private Long forldId;

    /** 提取标准类型 1:file 2:建模数据源 */
    @Excel(name = "提取标准类型", readConverterExp = "1=file,2=建模数据源")
    private Integer extractType;

    /** 数据源IP */
    @Excel(name = "数据源IP")
    private String ip;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 逻辑删除标识 0 未删除 1 删除（系统字段，不由前端维护） */
    private Integer delFlag;

    /** 创建人编码（系统自动填充，不由前端维护） */
    @Excel(name = "创建人编码")
    private Long creator;

    /** 修改人编码（系统自动填充，不由前端维护） */
    @Excel(name = "修改人编码")
    private Long modifier;

    /** 创建人名称（系统自动填充，不由前端维护） */
    @Excel(name = "创建人名称")
    private String creatorName;

    /** 修改人名称（系统自动填充，不由前端维护） */
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

    /**
     * 校验IP格式（IPv4）
     * 非空时校验，空值视为不校验（业务可空）
     *
     * @return 合法返回 true
     */
    public boolean isIpValid()
    {
        if (ip == null || ip.isEmpty())
        {
            return true;
        }
        return IpUtils.isIP(ip);
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

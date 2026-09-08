package com.ruoyi.product.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品主表对象 product
 *
 * 业务说明：
 *  - 商品名称在未删除数据范围内可重复（业务未要求唯一）
 *  - productType 单选（类型1/类型2/类型3），存单值字符串
 *  - productColor 多选，前端多选后端以逗号拼接存储
 *  - status：1-上架，0-下架
 *  - createById/createByName/isDeleted 为系统字段，由后端自动填充，前端不维护
 *  - 删除采用软删除（is_deleted=1），列表/详情查询自动过滤已删除数据
 *  - imageUrls/userIds/contacts 为新增接收的瞬态集合；imageList/contactList/userList 为详情返回的瞬态集合
 *
 * 注意：本表 create_by 为 BIGINT（用户ID），与 BaseEntity 的 createBy（String）类型不符，
 *       故使用 createById/createByName 字段名映射到 create_by/create_by_name 列，避免冲突；
 *       createTime/updateTime/remark/params 复用 BaseEntity。
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属省编码 */
    @Excel(name = "省编码")
    private String provinceCode;

    /** 所属省名称 */
    @Excel(name = "省")
    private String provinceName;

    /** 所属市编码 */
    @Excel(name = "市编码")
    private String cityCode;

    /** 所属市名称 */
    @Excel(name = "市")
    private String cityName;

    /** 代理商ID */
    @Excel(name = "代理商ID")
    private Long agentId;

    /** 代理商名称 */
    @Excel(name = "代理商")
    private String agentName;

    /** 商品名称（最多20字） */
    @Excel(name = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 20, message = "商品名称长度不能超过20个字符")
    private String productName;

    /** 金额（保留2位小数） */
    @Excel(name = "金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    /** 商品类型（类型1/类型2/类型3，单选） */
    @Excel(name = "商品类型")
    private String productType;

    /** 商品颜色（多选，逗号分隔） */
    @Excel(name = "商品颜色")
    private String productColor;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "发布时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 状态：1-上架，0-下架 */
    @Excel(name = "状态", readConverterExp = "1=上架,0=下架")
    @NotNull(message = "状态不能为空")
    private Integer status;

    /** 商品描述（富文本） */
    @NotBlank(message = "商品描述不能为空")
    private String description;

    /** 逻辑删除标识 0-未删除 1-已删除（系统字段） */
    private Integer isDeleted;

    /** 创建人ID（对应 create_by 列，系统自动填充） */
    @Excel(name = "操作人ID")
    private Long createById;

    /** 创建人姓名（对应 create_by_name 列，系统自动填充） */
    @Excel(name = "操作人")
    private String createByName;

    // ==================== 瞬态字段（不入库，用于新增接收 / 详情返回） ====================

    /** 新增时接收的图片URL列表 */
    private List<String> imageUrls;

    /** 新增时接收的所属人ID列表 */
    private List<Long> userIds;

    /** 新增时接收的联系方式列表 */
    private List<ProductContact> contacts;

    /** 详情返回的图片列表 */
    private List<ProductImage> imageList;

    /** 详情返回的联系方式列表 */
    private List<ProductContact> contactList;

    /** 详情返回的所属人列表 */
    private List<ProductUserRel> userList;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setProvinceCode(String provinceCode)
    {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode()
    {
        return provinceCode;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setCityCode(String cityCode)
    {
        this.cityCode = cityCode;
    }

    public String getCityCode()
    {
        return cityCode;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentName(String agentName)
    {
        this.agentName = agentName;
    }

    public String getAgentName()
    {
        return agentName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setProductType(String productType)
    {
        this.productType = productType;
    }

    public String getProductType()
    {
        return productType;
    }

    public void setProductColor(String productColor)
    {
        this.productColor = productColor;
    }

    public String getProductColor()
    {
        return productColor;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }

    public void setCreateById(Long createById)
    {
        this.createById = createById;
    }

    public Long getCreateById()
    {
        return createById;
    }

    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }

    public String getCreateByName()
    {
        return createByName;
    }

    public List<String> getImageUrls()
    {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls)
    {
        this.imageUrls = imageUrls;
    }

    public List<Long> getUserIds()
    {
        return userIds;
    }

    public void setUserIds(List<Long> userIds)
    {
        this.userIds = userIds;
    }

    public List<ProductContact> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ProductContact> contacts)
    {
        this.contacts = contacts;
    }

    public List<ProductImage> getImageList()
    {
        return imageList;
    }

    public void setImageList(List<ProductImage> imageList)
    {
        this.imageList = imageList;
    }

    public List<ProductContact> getContactList()
    {
        return contactList;
    }

    public void setContactList(List<ProductContact> contactList)
    {
        this.contactList = contactList;
    }

    public List<ProductUserRel> getUserList()
    {
        return userList;
    }

    public void setUserList(List<ProductUserRel> userList)
    {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("provinceCode", getProvinceCode())
            .append("provinceName", getProvinceName())
            .append("cityCode", getCityCode())
            .append("cityName", getCityName())
            .append("agentId", getAgentId())
            .append("agentName", getAgentName())
            .append("productName", getProductName())
            .append("amount", getAmount())
            .append("productType", getProductType())
            .append("productColor", getProductColor())
            .append("publishTime", getPublishTime())
            .append("status", getStatus())
            .append("description", getDescription())
            .append("remark", getRemark())
            .append("isDeleted", getIsDeleted())
            .append("createById", getCreateById())
            .append("createByName", getCreateByName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

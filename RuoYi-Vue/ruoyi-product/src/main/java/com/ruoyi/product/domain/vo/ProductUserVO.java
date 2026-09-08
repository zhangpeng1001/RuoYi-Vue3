package com.ruoyi.product.domain.vo;

/**
 * 商品所属人选择弹窗返回 VO
 *
 * 说明：从 sys_user 查询的轻量结果，仅返回弹窗展示所需字段，避免泄露敏感信息
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public class ProductUserVO
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 账号 */
    private String userName;

    /** 昵称 */
    private String nickName;

    /** 部门名称 */
    private String deptName;

    /** 手机号 */
    private String phonenumber;

    /** 邮箱 */
    private String email;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}

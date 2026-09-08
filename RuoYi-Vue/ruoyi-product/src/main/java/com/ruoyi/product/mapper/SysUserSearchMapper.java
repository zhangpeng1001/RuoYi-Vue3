package com.ruoyi.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.vo.ProductUserVO;

/**
 * 系统用户轻量查询 Mapper 接口
 *
 * 说明：直查 sys_user 表，仅返回弹窗所需字段，不依赖 system:user:list 权限，
 *       保持 product 模块自包含
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface SysUserSearchMapper
{
    /**
     * 按关键字模糊查询用户（匹配 user_name / nick_name / phonenumber）
     * keyword 为空时返回近期用户
     */
    public List<ProductUserVO> selectUserList(@Param("keyword") String keyword);
}

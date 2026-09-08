package com.ruoyi.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.ProductUserRel;

/**
 * 商品所属人关联 Mapper 接口
 *
 * 说明：关联表无 is_deleted 字段，商品软删时按 product_id 物理清理
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface ProductUserRelMapper
{
    /** 按商品ID查询所属人（关联 sys_user 装配昵称/账号/部门） */
    public List<ProductUserRel> selectUsersByProductId(Long productId);

    /** 批量新增关联 */
    public int batchInsertRel(@Param("list") List<ProductUserRel> list);

    /** 按商品ID物理删除关联 */
    public int deleteByProductId(Long productId);
}

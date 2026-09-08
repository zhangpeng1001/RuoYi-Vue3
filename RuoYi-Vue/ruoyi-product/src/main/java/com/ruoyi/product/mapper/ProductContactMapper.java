package com.ruoyi.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.ProductContact;

/**
 * 商品联系方式 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface ProductContactMapper
{
    /** 按商品ID查询未删除联系方式 */
    public List<ProductContact> selectContactsByProductId(Long productId);

    /** 批量新增联系方式 */
    public int batchInsertContact(@Param("list") List<ProductContact> list);

    /** 按商品ID软删除联系方式（is_deleted=1） */
    public int softDeleteByProductId(Long productId);
}

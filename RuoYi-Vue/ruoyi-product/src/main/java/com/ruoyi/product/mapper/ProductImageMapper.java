package com.ruoyi.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.ProductImage;

/**
 * 商品图片 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface ProductImageMapper
{
    /** 按商品ID查询未删除图片 */
    public List<ProductImage> selectImagesByProductId(Long productId);

    /** 批量新增图片 */
    public int batchInsertImage(@Param("list") List<ProductImage> list);

    /** 按商品ID软删除图片（is_deleted=1） */
    public int softDeleteByProductId(Long productId);
}

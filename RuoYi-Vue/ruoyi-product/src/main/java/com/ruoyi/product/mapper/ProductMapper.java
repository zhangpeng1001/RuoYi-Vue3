package com.ruoyi.product.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.product.domain.Product;

/**
 * 商品主表 Mapper 接口
 *
 * 数据访问约定：
 *  - 列表/详情查询自动附加 is_deleted = 0 条件
 *  - 新增由 Service 填充审计字段后调用 insertProduct
 *  - 删除走 softDeleteProductByIds（更新 is_deleted=1）
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface ProductMapper
{
    /**
     * 查询商品列表（仅未删除）
     * Service 层强制 isDeleted=0 后调用
     */
    public List<Product> selectProductList(Product product);

    /**
     * 查询商品详情（仅未删除，子表由 Service 单独装配）
     */
    public Product selectProductById(Long id);

    /**
     * 新增商品主表（审计字段已由 Service 填充）
     */
    public int insertProduct(Product product);

    /**
     * 批量软删除商品（更新 is_deleted=1）
     *
     * @param ids          商品ID数组
     * @param updateUserId 操作人ID（记录到 create_by 以便追溯软删操作人）
     * @param updateName   操作人姓名
     * @param updateTime   更新时间
     */
    public int softDeleteProductByIds(@Param("ids") Long[] ids,
                                      @Param("updateUserId") Long updateUserId,
                                      @Param("updateName") String updateName,
                                      @Param("updateTime") Date updateTime);
}

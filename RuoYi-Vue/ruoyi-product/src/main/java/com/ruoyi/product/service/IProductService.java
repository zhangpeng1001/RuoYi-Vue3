package com.ruoyi.product.service;

import java.util.List;
import com.ruoyi.product.domain.Product;

/**
 * 商品 Service 业务层接口
 *
 * 核心业务规则：
 *  1. 新增事务化：主表 + 图片 + 联系方式 + 所属人关联 同事务写入，任一失败整体回滚
 *  2. 审计字段（createById/createByName/createTime）由本层自动从当前登录用户填充
 *  3. 删除走软删除（is_deleted=1），级联软删子表图片/联系方式，关联表按 product_id 物理清理
 *  4. 列表/详情查询自动过滤 is_deleted=0
 *
 * @author ruoyi
 * @date 2026-09-08
 */
public interface IProductService
{
    /** 查询商品列表（仅未删除） */
    public List<Product> selectProductList(Product product);

    /** 查询商品详情（含图片/联系方式/所属人，仅未删除） */
    public Product selectProductById(Long id);

    /** 新增商品（事务化写入主表及子表，自动填充审计字段） */
    public int insertProduct(Product product);

    /** 批量软删除商品（级联软删子表） */
    public int softDeleteProductByIds(Long[] ids, Long updateUserId, String updateName);
}

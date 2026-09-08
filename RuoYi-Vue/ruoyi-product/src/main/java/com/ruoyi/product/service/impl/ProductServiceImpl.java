package com.ruoyi.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductContact;
import com.ruoyi.product.domain.ProductImage;
import com.ruoyi.product.domain.ProductUserRel;
import com.ruoyi.product.mapper.ProductContactMapper;
import com.ruoyi.product.mapper.ProductImageMapper;
import com.ruoyi.product.mapper.ProductMapper;
import com.ruoyi.product.mapper.ProductUserRelMapper;
import com.ruoyi.product.service.IProductService;

/**
 * 商品 Service 业务层实现
 *
 * 关键实现细节：
 *  - insertProduct 为事务方法，主表写入拿到自增 id 后依次写入图片/联系方式/所属人关联
 *  - 详情装配：主表 + 3 个子表分别查询后 set 回主对象，避免复杂 JOIN resultMap
 *  - 软删除：主表 is_deleted=1 + 图片/联系方式 is_deleted=1 + 关联表按 product_id 物理删
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@Service
public class ProductServiceImpl implements IProductService
{
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private ProductContactMapper productContactMapper;

    @Autowired
    private ProductUserRelMapper productUserRelMapper;

    /**
     * 查询商品列表（自动过滤已删除数据）
     */
    @Override
    public List<Product> selectProductList(Product product)
    {
        // 强制只查询未删除数据，避免 isDeleted 被前端覆盖
        product.setIsDeleted(0);
        return productMapper.selectProductList(product);
    }

    /**
     * 查询商品详情（含子表数据装配）
     */
    @Override
    public Product selectProductById(Long id)
    {
        Product product = productMapper.selectProductById(id);
        if (product == null)
        {
            return null;
        }
        // 装配图片
        product.setImageList(productImageMapper.selectImagesByProductId(id));
        // 装配联系方式
        product.setContactList(productContactMapper.selectContactsByProductId(id));
        // 装配所属人（关联 sys_user）
        product.setUserList(productUserRelMapper.selectUsersByProductId(id));
        return product;
    }

    /**
     * 新增商品（事务化写入主表及子表）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProduct(Product product)
    {
        log.info("[新增商品] 开始写入，商品名称={}, 代理商ID={}", product.getProductName(), product.getAgentId());
        // 1. 自动填充审计字段
        Date now = DateUtils.getNowDate();
        product.setCreateById(SecurityUtils.getUserId());
        product.setCreateByName(SecurityUtils.getUsername());
        product.setCreateTime(now);
        product.setUpdateTime(now);
        product.setIsDeleted(0);
        // 2. 写入主表，拿自增 id
        int rows = productMapper.insertProduct(product);
        Long productId = product.getId();
        if (productId == null)
        {
            throw new ServiceException("新增商品失败：未获取到主键ID");
        }
        log.info("[新增商品] 主表写入成功，productId={}", productId);
        // 3. 写入图片（imageUrls → ProductImage，sort_order 从1递增）
        List<String> imageUrls = product.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty())
        {
            List<ProductImage> imageList = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++)
            {
                ProductImage img = new ProductImage();
                img.setProductId(productId);
                img.setImageUrl(imageUrls.get(i));
                img.setSortOrder(i + 1);
                img.setCreateTime(now);
                img.setIsDeleted(0);
                imageList.add(img);
            }
            productImageMapper.batchInsertImage(imageList);
            log.info("[新增商品] 图片写入完成，productId={}, 数量={}", productId, imageList.size());
        }
        // 4. 写入联系方式
        List<ProductContact> contacts = product.getContacts();
        if (contacts != null && !contacts.isEmpty())
        {
            for (ProductContact c : contacts)
            {
                c.setProductId(productId);
                c.setCreateTime(now);
                c.setIsDeleted(0);
            }
            productContactMapper.batchInsertContact(contacts);
            log.info("[新增商品] 联系方式写入完成，productId={}, 数量={}", productId, contacts.size());
        }
        // 5. 写入所属人关联
        List<Long> userIds = product.getUserIds();
        if (userIds != null && !userIds.isEmpty())
        {
            List<ProductUserRel> relList = new ArrayList<>();
            for (Long userId : userIds)
            {
                ProductUserRel rel = new ProductUserRel();
                rel.setProductId(productId);
                rel.setUserId(userId);
                rel.setCreateTime(now);
                relList.add(rel);
            }
            productUserRelMapper.batchInsertRel(relList);
            log.info("[新增商品] 所属人关联写入完成，productId={}, 数量={}", productId, relList.size());
        }
        return rows;
    }

    /**
     * 批量软删除商品（级联软删子表）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int softDeleteProductByIds(Long[] ids, Long updateUserId, String updateName)
    {
        if (ids == null || ids.length == 0)
        {
            throw new ServiceException("删除商品失败：未选择任何数据");
        }
        Date now = DateUtils.getNowDate();
        // 1. 主表软删
        int rows = productMapper.softDeleteProductByIds(ids, updateUserId, updateName, now);
        log.info("[删除商品] 主表软删完成，ids={}, 影响行数={}", ids, rows);
        // 2. 子表级联软删 + 关联表物理删
        for (Long id : ids)
        {
            productImageMapper.softDeleteByProductId(id);
            productContactMapper.softDeleteByProductId(id);
            productUserRelMapper.deleteByProductId(id);
        }
        return rows;
    }
}

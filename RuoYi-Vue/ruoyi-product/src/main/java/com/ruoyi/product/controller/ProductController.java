package com.ruoyi.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.vo.ProductUserVO;
import com.ruoyi.product.mapper.SysUserSearchMapper;
import com.ruoyi.product.service.IProductService;

/**
 * 商品管理 Controller
 *
 * 接口约定：
 *  - GET  /product/list          分页查询商品列表（自动过滤已删除）
 *  - GET  /product/{id}          查询商品详情（含图片/联系方式/所属人）
 *  - POST /product               新增商品（含子表，事务化写入）
 *  - DELETE /product/{ids}       批量软删除商品
 *  - GET  /product/user/list     所属人选择弹窗的用户查询
 *
 * 权限前缀：product:product
 * 审计字段（createById/createByName/createTime）由 Service 自动填充，前端不维护
 *
 * @author ruoyi
 * @date 2026-09-08
 */
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController
{
    @Autowired
    private IProductService productService;

    /** 直查 sys_user，用于所属人选择弹窗 */
    @Autowired
    private SysUserSearchMapper sysUserSearchMapper;

    /**
     * 查询商品列表（自动过滤已删除数据）
     */
    @PreAuthorize("@ss.hasPermi('product:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(Product product)
    {
        startPage();
        List<Product> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    /**
     * 获取商品详情（含图片/联系方式/所属人，仅未删除）
     */
    @PreAuthorize("@ss.hasPermi('product:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productService.selectProductById(id));
    }

    /**
     * 新增商品
     * 审计字段由 Service 自动填充；主表+子表事务化写入
     */
    @PreAuthorize("@ss.hasPermi('product:product:add')")
    @Log(title = "商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Product product)
    {
        return toAjax(productService.insertProduct(product));
    }

    /**
     * 删除商品（软删除：is_deleted=1，级联软删子表）
     */
    @PreAuthorize("@ss.hasPermi('product:product:remove')")
    @Log(title = "商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 软删除：记录操作人，便于追溯
        return toAjax(productService.softDeleteProductByIds(ids,
                SecurityUtils.getUserId(), SecurityUtils.getUsername()));
    }

    /**
     * 所属人选择弹窗 - 用户查询
     * 直查 sys_user，不依赖 system:user:list 权限
     */
    @GetMapping("/user/list")
    public AjaxResult userList(@RequestParam(value = "keyword", required = false) String keyword)
    {
        List<ProductUserVO> list = sysUserSearchMapper.selectUserList(keyword);
        return success(list);
    }
}

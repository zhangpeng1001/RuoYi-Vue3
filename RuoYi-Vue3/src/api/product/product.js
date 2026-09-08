import request from '@/utils/request'

// 查询商品列表
export function listProduct(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}

// 查询商品详情（含图片/联系方式/所属人）
export function getProduct(id) {
  return request({
    url: '/product/' + id,
    method: 'get'
  })
}

// 新增商品
export function addProduct(data) {
  return request({
    url: '/product',
    method: 'post',
    data: data
  })
}

// 删除商品（软删除）
export function delProduct(id) {
  return request({
    url: '/product/' + id,
    method: 'delete'
  })
}

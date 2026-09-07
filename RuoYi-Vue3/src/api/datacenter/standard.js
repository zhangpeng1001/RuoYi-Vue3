import request from '@/utils/request'

// 查询行业标准管理列表
export function listStandard(query) {
  return request({
    url: '/datacenter/standard/list',
    method: 'get',
    params: query
  })
}

// 查询行业标准管理详细
export function getStandard(id) {
  return request({
    url: '/datacenter/standard/' + id,
    method: 'get'
  })
}

// 新增行业标准管理
export function addStandard(data) {
  return request({
    url: '/datacenter/standard',
    method: 'post',
    data: data
  })
}

// 修改行业标准管理
export function updateStandard(data) {
  return request({
    url: '/datacenter/standard',
    method: 'put',
    data: data
  })
}

// 删除行业标准管理（软删除）
export function delStandard(id) {
  return request({
    url: '/datacenter/standard/' + id,
    method: 'delete'
  })
}

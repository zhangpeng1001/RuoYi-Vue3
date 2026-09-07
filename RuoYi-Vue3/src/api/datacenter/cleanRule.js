import request from '@/utils/request'

// 查询清洗规则配置列表
export function listCleanRule(query) {
  return request({
    url: '/datacenter/cleanRule/list',
    method: 'get',
    params: query
  })
}

// 查询清洗规则配置详细
export function getCleanRule(id) {
  return request({
    url: '/datacenter/cleanRule/' + id,
    method: 'get'
  })
}

// 新增清洗规则配置
export function addCleanRule(data) {
  return request({
    url: '/datacenter/cleanRule',
    method: 'post',
    data: data
  })
}

// 修改清洗规则配置
export function updateCleanRule(data) {
  return request({
    url: '/datacenter/cleanRule',
    method: 'put',
    data: data
  })
}

// 删除清洗规则配置
export function delCleanRule(id) {
  return request({
    url: '/datacenter/cleanRule/' + id,
    method: 'delete'
  })
}

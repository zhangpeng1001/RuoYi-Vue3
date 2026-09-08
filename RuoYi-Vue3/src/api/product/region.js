import request from '@/utils/request'

// 获取所有省份
export function listProvinces() {
  return request({
    url: '/region/provinces',
    method: 'get'
  })
}

// 根据省编码获取城市列表
export function listCities(parentCode) {
  return request({
    url: '/region/cities',
    method: 'get',
    params: { parentCode }
  })
}

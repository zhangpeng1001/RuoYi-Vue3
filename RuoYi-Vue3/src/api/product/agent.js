import request from '@/utils/request'

// 代理商联想搜索（仅启用且未删除）
export function searchAgent(keyword) {
  return request({
    url: '/agent/search',
    method: 'get',
    params: { keyword }
  })
}

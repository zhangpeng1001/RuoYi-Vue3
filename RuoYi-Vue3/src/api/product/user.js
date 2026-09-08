import request from '@/utils/request'

// 所属人选择弹窗 - 用户查询（直查 sys_user）
export function listProductUsers(keyword) {
  return request({
    url: '/product/user/list',
    method: 'get',
    params: { keyword }
  })
}

import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/chat/admin/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/sys/user/getInfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/sys/user/logout',
    method: 'post'
  })
}
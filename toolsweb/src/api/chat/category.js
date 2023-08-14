import request from '@/utils/request'

export function page(data) {
  return request({
    url: '/chat/admin/category/page',
    method: 'post',
    data
  })
}

export function get(data) {
  return request({
    url: '/chat/admin/category/get',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function remove(data) {
  return request({
    url: '/chat/admin/category/delete',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function update(data) {
  return request({
    url: '/chat/admin/category/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/chat/admin/category/add',
    method: 'post',
    data
  })
}

import request from '@/utils/request'

export function page(data) {
  return request({
    url: '/chat/admin/model/page',
    method: 'post',
    data
  })
}

export function get(data) {
  return request({
    url: '/chat/admin/model/get',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function remove(data) {
  return request({
    url: '/chat/admin/model/delete',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function update(data) {
  return request({
    url: '/chat/admin/model/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/chat/admin/model/add',
    method: 'post',
    data
  })
}

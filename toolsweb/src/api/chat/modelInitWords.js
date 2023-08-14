import request from '@/utils/request'

export function page(data) {
  return request({
    url: '/chat/admin/model/initWords/page',
    method: 'post',
    data
  })
}

export function get(data) {
  return request({
    url: '/chat/admin/model/initWords/get',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function remove(data) {
  return request({
    url: '/chat/admin/model/initWords/delete',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function update(data) {
  return request({
    url: '/chat/admin/model/initWords/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/chat/admin/model/initWords/add',
    method: 'post',
    data
  })
}


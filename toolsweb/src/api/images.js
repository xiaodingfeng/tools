import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/tool/images/list',
    method: 'post',
    data
  })
}

export function imageDetail(param) {
  return request({
    url: '/tool/images/detail',
    method: 'get',
    ContentType: 'application/x-www-form-urlencoded;',
    params: { id: param }
  })
}

export function deleteImage(data) {
  return request({
    url: '/tool/images/delete',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

export function updateImage(data) {
  return request({
    url: '/tool/images/update',
    method: 'post',
    data
  })
}

export function upload(data) {
  return request({
    url: '/tool/images/upload',
    method: 'post',
    data
  })
}

export function category(param) {
  return request({
    url: '/tool/images/category',
    method: 'get',
    param
  })
}

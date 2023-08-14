import request from '@/utils/request'

export function page(data) {
  return request({
    url: '/chat/admin/chat_room/page',
    method: 'post',
    data
  })
}

export function get(data) {
  return request({
    url: '/chat/admin/chat_room/get',
    method: 'post',
    ContentType: 'application/x-www-form-urlencoded;',
    data
  })
}

import { Message } from 'element-ui'

export function error(msg) {
  Message({
    message: msg || 'Error',
    type: 'error',
    duration: 5 * 1000
  })
}

export function success(msg) {
  Message({
    message: msg || 'success',
    type: 'success',
    duration: 5 * 1000
  })
}

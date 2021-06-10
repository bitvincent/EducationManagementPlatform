import request from '@/utils/request'

export function getKaptcha() {
  return request({
    url: '/kaptcha/getKaptcha',
    method: 'post',
    responseType: 'blob'
  })
}

export function verifyKaptcha(code, verify_token) {
  return request({
    url: '/kaptcha/verifyKaptcha',
    method: 'post',
    data: {
      verifyCode: code,
      verifyToken: verify_token
    }
  })
}

// 校验验证码
export function verifySMS(code, verify_token) {
  return request({
    url: '/user/verifySMS',
    method: 'post',
    data: {
      verifyCode: code,
      verifyToken: verify_token
    }
  })
}

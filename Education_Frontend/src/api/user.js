import request from '@/utils/request'

export function getInfo(type, token) {
  const url = type === 'student' ? '/student/getStudentInfo' : '/teacher/getTeacherInfo'

  return request({
    url,
    method: 'post',
    data: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}

export function loginMplx(type, data) {
  const url = type === 'student' ? '/user/loginStudent' : '/user/loginTeacher'
  return request({
    url,
    method: 'post',
    data
  })
}

export function loginSMSMplx(type, data, verify_token) {
  const url = type === 'student' ? '/user/loginStudentWithSMS' : '/user/loginTeacherWithSMS'
  return request({
    url,
    method: 'post',
    data: { ...data, verify_token }
  })
}

export function registerMplx(type, data) {
  let url
  if (type === 'student') {
    url = '/user/registerStudentWithSMS'
  } else {
    url = '/user/registerTeacherWithSMS'
  }
  return request({
    url,
    method: 'post',
    data
  })
}

export function settingMplx(type, data) {
  let url
  if (type === 'student') {
    url = '/user/updateStudentPassword'
  } else {
    url = '/user/updateTeacherPassword'
  }
  return request({
    url,
    method: 'post',
    data
  })
}

// 获取验证码
export function sendSMS(data) {
  return request({
    url: '/user/sendSMS',
    method: 'post',
    data
  })
}

// 校验验证码
export function verifySMS(data) {
  return request({
    url: '/user/verifySMS',
    method: 'post',
    data
  })
}

// 学生验证码登录
export function loginStudentWithSMS(data) {
  return request({
    url: '/user/loginStudentWithSMS',
    method: 'post',
    data
  })
}

// 教师验证码登录
export function loginTeacherWithSMS(data) {
  return request({
    url: '/user/loginTeacherWithSMS',
    method: 'post',
    data
  })
}

// 学生验证码注册
export function registerStudentWithSMS(data) {
  return request({
    url: '/user/registerStudentWithSMS',
    method: 'post',
    data
  })
}

// 教师验证码注册
export function registerTeacherWithSMS(data) {
  return request({
    url: '/user/registerStudentWithSMS',
    method: 'post',
    data
  })
}

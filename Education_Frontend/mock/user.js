
const students = {
  '13123456789': {
    password: 'qwerty',
    studentname: 'Stu'
  }
}
const teachers = {
  '13123456789': {
    password: 'qwerty',
    teachername: 'Teu'
  }
}

module.exports = [

  // user logout
  {
    url: '/vue-element-admin/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },
  // student register
  {
    url: '/user/registerStudent',
    type: 'post',
    response: config => {
      const { number, password, studentname } = config.body
      if (students[number]) {
        return {
          code: 20000,
          data: {
            status: 1 // fail, duplicated number
          }
        }
      }
      students[number] = {
        password, studentname
      }
      return {
        code: 20000,
        data: {
          status: 0
        }
      }
    }
  },
  // teacher register
  {
    url: '/user/registerTeacher',
    type: 'post',
    response: config => {
      const { number, password, teachername } = config.body
      if (teachers[number]) {
        return {
          code: 20000,
          data: {
            status: 1 // fail, duplicated number
          }
        }
      }
      teachers[number] = {
        password, teachername
      }
      return {
        code: 20000,
        data: {
          status: 0
        }
      }
    }
  },

  // student login
  {
    url: '/user/loginStudent',
    type: 'post',
    response: config => {
      const { number, password } = config.body
      if (!students[number] || students[number].password !== password) {
        return {
          code: 50000
        }
      }
      return {
        code: 20000,
        data: {
          Token: number
        }
      }
    }
  },

  // teacher login
  {
    url: '/user/loginTeacher',
    type: 'post',
    response: config => {
      const { number, password } = config.body
      if (!teachers[number] || teachers[number].password !== password) {
        return {
          code: 50000
        }
      }
      return {
        code: 20000,
        data: {
          Token: number,
          teacherid: 1
        }
      }
    }
  },

  // get user info
  {
    url: '/student/getStudentInfo',
    type: 'get',
    response: config => {
      const { token } = config.query
      const info = students[token]

      // mock error
      if (!info) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }
      return {
        code: 20000,
        data: info
      }
    }
  },

  {
    url: '/teacher/getTeacherInfo',
    type: 'get',
    response: config => {
      const { token } = config.query
      const info = teachers[token]

      // mock error
      if (!info) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }
      return {
        code: 20000,
        data: info
      }
    }
  }
]

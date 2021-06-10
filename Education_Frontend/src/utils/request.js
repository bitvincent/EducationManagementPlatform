import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import { stringify } from 'qs'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  transformRequest: data => (data instanceof FormData ? data : stringify(data)),
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.loggedIn && store.getters.token) {
      // let each request carry token if logged in
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    if (response.headers['verify-token'] || !response.data.code) {
      return response
    }
    const res = response.data
    // if the response status is not 0, it is judged as an error.
    if (typeof res.status === 'undefined' || res.status !== 0) {
      // 4001: Token expired; 4002: Illegal token;
      if (res.code === 4001 || res.code === 4002) {
        // to re-login
        store.dispatch('user/resetToken').then(() => {
          location.reload()
          Message({
            message: '登录已过期，请重新登录',
            type: 'error',
            duration: 5 * 1000
          })
        })
      } else {
        Message({
          message: res.message || 'Error',
          type: 'error',
          duration: 5 * 1000
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service

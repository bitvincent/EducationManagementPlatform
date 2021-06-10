import request from '@/utils/request'

export function getRecommendClass(data) {
  return request({
    url: '/class/getRecommendClass',
    method: 'post',
    data
  })
}

export function searchClass(data) {
  return request({
    url: '/class/searchClass',
    method: 'post',
    data
  })
}

export const getPublishedClass = () =>
  request({
    url: '/teacher/getPublishedClass',
    method: 'post'
  })

export const getSelectedClass = () =>
  request({
    url: '/student/getSelectedClass',
    method: 'post'
  })

export const getClassInfo = (id) =>
  request({
    url: '/class/getClassInfo',
    method: 'post',
    data: { classid: id }
  })

export const getStudentClassInfo = (id) =>
  request({
    url: '/class/getStudentClassInfo',
    method: 'post',
    data: { classid: id }
  })

export const selectClass = (id) =>
  request({
    url: '/student/selectClass',
    method: 'post',
    data: { classid: id }
  })

export const publishClass = (data) =>
  request({
    url: '/teacher/publishClass',
    method: 'post',
    data
  })


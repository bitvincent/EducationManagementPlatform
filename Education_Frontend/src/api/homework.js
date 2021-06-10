import request from '@/utils/request';

export const getPublishedHomework = (id) =>
  request({
    url: 'teacher/getPublishedHomework',
    method: 'post',
    data: { classid: id }
  })

export const publishHomework = (data) =>
  request({
    url: 'teacher/publishHomework',
    method: 'post',
    data
  })

export const getTeacherHomeworkInfo = (data) =>
  request({
    url: 'teacher/getTeacherHomeworkInfo',
    method: 'post',
    data
  })

export const getUnMarkedHomeworkTeacher = (classid) =>
  request({
    url: 'teacher/getUnMarkedHomework',
    method: 'post',
    data: { classid }
  })

export const markHomework = (data) =>
  request({
    url: 'teacher/markHomework',
    method: 'post',
    data
  })

export const getMarkedHomeworkTeacher = (classid) =>
  request({
    url: 'teacher/getMarkedHomework',
    method: 'post',
    data: { classid }
  })
// /////////////////////////////////////
export const getUnUploadedHomework = (classid) =>
  request({
    url: 'student/getUnUploadedHomework',
    method: 'post',
    data: { classid }
  })

export const uploadHomework = (data) =>
  request({
    url: 'student/uploadHomework',
    method: 'post',
    data
  })

export const getUnMarkedHomeworkStudent = (classid) =>
  request({
    url: 'student/getUnMarkedHomework',
    method: 'post',
    data: { classid }
  })

export const getMarkedHomeworkStudent = (classid) =>
  request({
    url: 'student/getMarkedHomework',
    method: 'post',
    data: { classid }
  })

export const getLearnProcess = (classid) =>
  request({
    url: 'student/getLearnProcess',
    method: 'post',
    data: { classid }
  })


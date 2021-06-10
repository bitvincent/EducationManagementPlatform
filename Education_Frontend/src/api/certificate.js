import request from '@/utils/request';

export function getAllCertification(data) {
  return request({
    url: '/student/getAllCertification',
    method: 'post',
    data
  });
}

export function getCertification(data) {
  return request({
    url: '/student/getCertification',
    method: 'post',
    data
  });
}

export function getAllGrade(data) {
  return request({
    url: 'teacher/getAllGrade',
    method: 'post',
    data
  });
}

export function setFinalGrade(data) {
  return request({
    url: 'teacher/setFinalGrade',
    method: 'post',
    data
  });
}

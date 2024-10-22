import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
export function sendCode(query) {
  return request({
    url: '/api/auth/phone',
    method: 'get',
    params: query
  })
}
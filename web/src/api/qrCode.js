import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
// 二维码的接口
export const getQRCode = (params) => {
    return request({
        method: "post",
        url: "/api/auth/weixin/getQRCode",
        params: params
    })
}
 
// 二维码扫码的登录轮询接口
export const checkQRCode = (data) => {
    return request({
        method: "get",
        url: `/api/auth/weixin/check`,
        params: data
    })
}

export const checkPatient = (data) => {
    return request({
        method: "get",
        url: `/api/clinic/weixin/checkPatient`,
        params: data
    })
}

export const getPayQRCode = (packageType) => {
    return request({
        method: "get",
        url: `/api/auth/weixin/clinic/pay/qrcode`,
        params: { packageType }
    })
}

export const checkPayQRCode = (orderId) => {
    return request({
        method: "get",
        url: `/api/auth/weixin/clinic/pay/WxOrder`,
        params: { orderId }
    })
}
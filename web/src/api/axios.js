/**
 * axios 封装
 * 请求拦截、响应拦截、错误统一处理
 */
import axios from 'axios';
// import store from '../store/index'; // vuex
import { ElNotification } from 'element-plus'
import { getToken } from '@/stores/UserStore.js';
import router from '@/router'

 
// 创建 axios 实例
let instance = axios.create({
//  baseURL: import.meta.env.VITE_BASE_URL,
});
// console.debug('axios实例默认：', instance.defaults);
// 设置请求超时
instance.defaults.timeout = 10000;
// 表示跨域请求时是否需要使用凭证
instance.defaults.withCredentials = true;
// 环境的切换 `baseURL` 将自动加在 `url` 前面，除非 `url` 是一个绝对 URL
/* if (process.env.NODE_ENV == 'development') {
    instance.defaults.baseURL = 'https://www.baidu.com';
}else if (process.env.NODE_ENV == 'test') {
    instance.defaults.baseURL = 'https://www.tencent.com';
}else if (process.env.NODE_ENV == 'production') {
    instance.defaults.baseURL = 'https://www.alibaba.com';
} */

// 设置 post 请求头    application/x-www-form-urlencoded;application/json;
instance.defaults.headers.common['Authorization'] = 'Bearer ' + getToken();
instance.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8;charset=UTF-8;';
instance.defaults.headers.put['Content-Type'] = 'application/json;charset=UTF-8;charset=UTF-8;';


instance.all = axios.all; // 设置 axios 并发请求

// 添加请求拦截器
instance.interceptors.request.use(config => {
    if(config.params == undefined) config.params = {}
    config.params.timestamp = Date.now()
    let token = getToken()
    if(token != null && token != undefined && token != 'null') {
        config.headers = { ...config.headers, 'Authorization': 'Bearer ' + token }
        return config;
    }
    return config;
}, error => {
    // 对请求错误做些什么
    console.error(error)
    return Promise.reject(error);
});
//判断是否包含中文
function containsChinese(str) {
    var re = /[\u4e00-\u9fa5]/;
    return re.test(str);
}


import { globalLoading, systemSwitch } from '@/stores/app.js'

// 添加响应拦截器  
instance.interceptors.response.use(response => {
    // 对响应数据做点什么
    
    // 如果返回的状态码为 200，说明接口请求成功，可以正常拿到数据，否则的话抛出错误
    if (response.status === 200) {
        let result = response.data
        if(response.status === 200 && result.code == 200 && response.data.code == 200) {
            return Promise.resolve(response);
        } else if(response.code == 401 || response.data.code == 401) {
            router.push({ path: '/clinic/login' });
            globalLoading.closeGlobalLoading()
            return Promise.resolve(response);
        } else if(response.data.code >= 400 && response.data.code < 500) {
            ElNotification({ title: '警告',  message: response.data.msg, type: 'error', })
        } else {
            if(response.headers) {
                let contentType = response.headers['content-type']
                if(contentType && contentType == 'application/octet-stream') {
                    ElNotification({ title: '成功',  message: '文件下载成功', type: 'success', })
                    return Promise.resolve(response);
                }
            }
            if(containsChinese(response.data.msg)){
                ElNotification({ title: '警告',  message: response.data.msg, type: 'error', })
                return Promise.resolve(response);
            }
            ElNotification({ title: '异常',  message: '服务器异常！请联系客服', type: 'error', })
            return Promise.resolve(response);
        }
        return Promise.resolve(response);
    }
    return Promise.reject(response);
    // return response;
}, error => { // 服务器状态码不是 200 的情况
    // 对响应错误做点什么
    
    console.error(Object.entries(error));
    const { config, code, request, response, isAxiosError, toJSON } = error;
    console.debug('[axios::response] ', response)
    if (response) {
        // 请求已发出，但是不在 2xx 的范围
        errorHandle(response.status, response.data.message);
        return Promise.reject(response);
    } else {
        // 超时提示
        if(error.message.includes('timeout')){
            ElNotification({
                title: 'Error',
                message: '请求超时，请稍后重试',
                type: 'error',
            })
            return Promise.reject(error);
        }

        /* 处理断网的情况
        eg: 请求超时或断网时，更新 state 的 network 状态
        network 状态在 app.vue 中控制着一个全局的断网提示组件的显示隐藏
        关于断网组件中的刷新重新获取数据，会在断网组件中说明 */
        if (!window.navigator.onLine) {
            // 如果断网....
            // store.commit('changeNetwork', false);
        } else {
            return Promise.reject(error);
        }
    }
});

/** 
 * 请求失败后的错误统一处理 
 * @param {Number} status 请求失败的状态码
 * 1**  信息，服务器收到请求，需要请求者继续执行操作
 * 2**  成功，操作被成功接收并处理
 * 3**  重定向，需要进一步的操作以完成请求
 * 4**  客户端错误，请求包含语法错误或无法完成请求
 * 5**  服务器错误，服务器在处理请求的过程中发生了错误
 */

/**
 * 100  继续。客户端应继续其请求
 * 101  切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议
 * 
 * 200  请求成功。一般用于GET与POST请求
 * 201  已创建。成功请求并创建了新的资源
 * 202  已接受。已经接受请求，但未处理完成
 * 203  非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
 * 204  无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
 * 205  重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
 * 206  部分内容。服务器成功处理了部分GET请求
 * 
 * 300  多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
 * 301  永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
 * 302  临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
 * 303  查看其它地址。与301类似。使用GET和POST请求查看
 * 304  未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
 * 305  使用代理。所请求的资源必须通过代理访问
 * 306  已经被废弃的HTTP状态码
 * 307  临时重定向。与302类似。使用GET请求重定向
 * 
 * 400  客户端请求的语法错误，服务器无法理解
 * 401  请求要求用户的身份认证
 * 402  保留，将来使用
 * 403  服务器理解请求客户端的请求，但是拒绝执行此请求
 * 404  服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
 * 405  客户端请求中的方法被禁止
 * 406  服务器无法根据客户端请求的内容特性完成请求
 * 407  请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
 * 408  服务器等待客户端发送的请求时间过长，超时
 * 409  服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突
 * 410  客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
 * 411  服务器无法处理客户端发送的不带Content-Length的请求信息
 * 412  客户端请求信息的先决条件错误
 * 413  由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
 * 414  请求的URI过长（URI通常为网址），服务器无法处理
 * 415  服务器无法处理请求附带的媒体格式
 * 416  客户端请求的范围无效
 * 417  服务器无法满足Expect的请求头信息
 * 
 * 500  服务器内部错误，无法完成请求
 * 501  服务器不支持请求的功能，无法完成请求
 * 502  作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应
 * 503  由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
 * 504  充当网关或代理的服务器，未及时从远端服务器获取请求
 * 505  服务器不支持请求的HTTP协议的版本，无法完成处理
 */
const errorHandle = (status, other) => {
    // 状态码判断
    switch (status) {
        // 400: 请求错误
        case 400:
        ElNotification.error(other, '提示');
        break;
        // 401: 未登录状态，跳转登录页
        case 401:
        router.push({ path: '/clinic/login', })
        break;
        // 403:  权限不足，拒绝访问
        case 403:
        ElNotification.error('权限不足，拒绝访问', '提示');
        break;
        // 404: 请求不存在
        case 404:
        ElNotification.error('请求的资源不存在或请求地址出错', '提示');
        break;
        // 500: 服务器错误
        case 500:
        ElNotification.error('服务器异常', '提示');
        break;
        // 1000001: token 异常
        case 1000001:
        ElNotification.error("token 异常，请联系管理员", '提示');
        break;
        default:
    }
}

export default instance;
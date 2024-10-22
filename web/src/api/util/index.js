import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import { ElNotification } from 'element-plus'

function getInstance(type, path, params, timeout) {
    return request({
        url: path,
        method: type,
        params: type == 'get' ? params : undefined,
        data: type != 'get' ? params : undefined,
        timeout: timeout || 0
    })
}

function send(instance, callback, isShowSubmitNotification = false, errorCallback) {
    instance.then(({status, data}) => {
        try {
            if(status == 200 && data.code == 200) {
                const response = data.data
                if(isShowSubmitNotification) ElNotification({ title: '成功', message: '信息提交成功!', type: 'success' })
                if(callback) callback(response)
            } else {
                if(errorCallback) errorCallback(data)
            }
        } catch (error) {
            ElNotification({ title: '警告', message: '请求异常，请检查网络连接状态！', type: 'error' })
            console.error(error)
        }
    }).catch(function (error) {{
        console.error(error)
        if(errorCallback) errorCallback(error)
    }})
}


export function put(path, params, callback, isShowSubmitNotification) {
    if(isShowSubmitNotification) ElNotification({ title: '通知', message: '数据提交中...', type: 'info' })
    send(getInstance('put', path, params), callback, isShowSubmitNotification)
}

export function del(path, params, callback, isShowSubmitNotification) {
    send(getInstance('delete', path, params), callback, isShowSubmitNotification)
}

export function update(path, params, callback, isShowSubmitNotification, errorCallback) {
    send(getInstance('post', path, params), callback, isShowSubmitNotification, errorCallback)
}

export function get(path, params, callback, isShowSubmitNotification, timeout) {
    send(getInstance('get', path, params, timeout), callback, isShowSubmitNotification)
}
export function NoCaer(method,path, params, callback, isShowSubmitNotification) {
    send(getInstance(method, path, params), callback, isShowSubmitNotification)
}
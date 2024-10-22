import * as util from '@/api/util'
import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例

const PATH = `/auth/user`


const userService = {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    login(params, callback) {
        return util.update(PATH, params, callback);
    },
    logout(callback) {
        return util.del(PATH, {}, callback);
    },
    updateConfig(params, callback) {
        return util.update(PATH + '/admin', params, callback, true);
    },
    getConfig(params, callback) {
        return util.get(PATH + '/admin', params, callback);
    },
    getResource(params, callback) {
        return util.get(PATH + '/resource/tree', params, callback);
    },
}

export default userService;

export function search(params, callback) { util.get(`/api/auth/list`, params, callback) }
export function loginUserInfo(params, callback) { util.get(`/api/auth`, params, callback) }
export function login(params, callback, errorCallback) { util.update(`/api/auth/login`, params, callback, false, errorCallback) }
export function searchCompanyStaff(params, callback) { util.get(`/api/auth/company/staff/list`, params, callback) }

export function resetPWD(params, callback, errorCallback) { util.update(`/api/auth/login/pwd`, params, callback, undefined, errorCallback) }

export function systemResetPWD(params, callback, errorCallback) { util.update(`/api/auth/login/pwd`, params, callback, undefined, errorCallback) }


export function register(params, callback) { util.put(`/api/auth/user`, params, callback, true) }

export function deleteUser(callback) { util.del(`/api/clinic/user`, {}, callback, true) }

export function checkUserIsRegister(params, callback) { util.get(`/api/auth/check/register`, params, callback) }

export function update(params, callback, errorCallback) { util.update(`/api/auth/user`, params, callback, true, errorCallback) }

export function bindLoginCompany(params, callback, errorCallback) { util.update(`/api/auth/login/bind/company`, params, callback, false, errorCallback) }

export function getBindCompany(callback, errorCallback) { util.get(`/api/auth/login/bind/company`, {}, callback, false, errorCallback) }

export function searchMyRouter(systemCode,time) {
    return axios.get(`/api/auth/system/router/user`, { params: { systemCode ,time} });
}

export function me(callback, errorCallback) {
    return util.get(`/api/auth/me`, {}, callback, false, errorCallback);
}

export function getInvite(callback, errorCallback) {
    return util.get(`/api/auth/invite`, {}, callback, false, errorCallback);
}

export function generateInvite(code, callback, errorCallback) {
    return util.put(`/api/auth/invite`, { code }, callback, false, errorCallback);
}

export function getInviteList(params, callback, errorCallback) {
    return util.get(`/api/auth/invite/list`, params, callback, false, errorCallback);
}

export function checkIsInvite(callback, errorCallback) {
    return util.get(`/api/auth/invite/check/is/invite`, {}, callback, false, errorCallback);
}


export function getInviteCodeList(params, callback, errorCallback) {
    return util.get(`/api/auth/invite/code/list`, params, callback, false, errorCallback);
}
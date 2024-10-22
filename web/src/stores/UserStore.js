import { reactive } from 'vue'
import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import { loginState } from './app';

/**
 * 登录用户信息与操作
 */
export const loginUserStore = reactive({
    loginFlag: 3,
    loginUser: {
        userCompanyList: []
    },
    setUser(user) {
        this.loginUser = user
    },
    getUser() {
        let user = this.loginUser
        if(user == null || user == {} || user.name == null || user.name == '') {
            user = this.loginUser = tryLogin()
        }
        return user
    },
    initUserCache() {
        this.loginUser = {}
    },
    logout() {
        this.initUserCache()
        delToken()
    },
    currentComponentIndex: 0,
    selectCompany(index) {
        this.currentComponentIndex = index
    },
    getCompany() {
        return this.loginUser && this.loginUser.userCompanyList && this.loginUser.userCompanyList[this.currentComponentIndex].company
    },
})

export function checkLogin() {
    if(localTokenEnabled()) {   // 检查本地 token 是否存在
        let user = tryLogin()   // 发送登录请求
        if(loginSuccess(user)) {    // 检查是否登录
            loadUserToLocal(user)   // 加载用户到本地
            loginState.login()
            return true
        }
    }
    loginState.noLogin()
    return false
}

function loginSuccess(user) {
    return user != null
}
function loadUserToLocal(user) {
    loginUserStore.setUser(user)
}

async function tryLogin() {
    let { status, data } = await axios.get('/api/auth/me', {})
    if(status == 200) {
        let { data: user } = data
        return user
    }
    return null
}

function localTokenEnabled() {
    let token = getToken()
    return token != undefined  && token != null && token != '' && token != 'null'
}

export function isLogin() {
    return localTokenEnabled()
}
export function isNotLogin() {
    return !(localTokenEnabled())
}

/**
 * 登录用户本地存储
 */
var tokenKey = "authorization"
export function setToken(token) {
    window.localStorage.setItem(tokenKey, token)
}
export function getToken() {
    return window.localStorage.getItem(tokenKey)
}
export function delToken() {
    window.localStorage.removeItem(tokenKey)
}
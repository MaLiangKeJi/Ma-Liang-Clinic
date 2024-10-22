import { reactive, computed } from 'vue'
import routerInstance from '@/router/index.js'
import {} from '@/stores/UserStore.js'

export const leftMenuStore = reactive({
    isHideLeftMenu: true,
    isShowLeftMenu: computed(() => { return !(leftMenuStore.isHideLeftMenu) }),
    hideLeftMenu() {
        this.isHideLeftMenu = true
    },
    showLeftMenu() {
        this.isHideLeftMenu = false
    },
})

export const globalLoading = reactive({
    loading: false,
    openGlobalLoading() {
        this.loading = true
    },
    closeGlobalLoading() {
        this.loading = false
    },
})

export const systemSwitch = reactive({
    system: null,
    selectAndToClinicSystem() {
        routerInstance.push({ path: '/clinic/home' })
    },
    selectAndToFinancialSystem() {
        this.system = 'financial'
        if(loginState.isLogin) {
            routerInstance.push({ path: '/financial/set' })
        } else {
            routerInstance.push({ path: '/financial/login' })
        }
    },
    currentIsClinicSystem() {
        return this.system && this.system == 'clinic'
    },
    currentIsFinancialSystem() {
        return this.system && this.system == 'financial'
    },
})
export const loginState = reactive({
    isLogin: false,
    login() {
        this.isLogin = true
    },
    noLogin() {
        this.isLogin = false
    }
})

import { system } from '@/stores/system.js'
import { searchMyRouter } from '@/api/user.api.js'

export const systemRouterStore = reactive({
    routers: null,
    setRouters(routerVO) {
        this.routers = routerVO
        pushRouter(routerVO.list)
    },
    getRouters() {
        return this.routers
    },
    getRouterList() {
        return this.routers ? this.routers.list : null
    },
    getRouterTree() {
        return this.routers ? this.routers.tree : null
    },
    async reloadRouter() {
        let routers = await searchMyRouter(systemSwitch.system);
        if(routers) {
            this.setRouters(routers.data.data) //保存路由信息
        }
    },
    remove() {
        this.routers = null
    }
})

const modules = import.meta.glob("@/views/**/*.vue")

export function pushRouter(routers) {
    routers.forEach(systemRouter => {
        if(systemRouter.type == 1 || systemRouter.type == 2) {    //页面 or 组件类型
            let componentPath = systemRouter.componentPath
            if(componentPath && systemRouter.componentPath.charAt(0) == '@') {
                componentPath = '/src' + systemRouter.componentPath.substr(1)
            }
            let component = modules[/* @vite-ignore */ `${componentPath}`]
            routerInstance.addRoute({ 
                path: systemRouter.path, 
                name: systemRouter.code,
                component,
                meta: { title: systemRouter.title ? systemRouter.title : '' }
            })
        }
    })
}
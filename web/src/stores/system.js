import { reactive } from 'vue'
import { loginUserStore } from '@/stores/UserStore.js'

export const system = reactive({
    currentSystem: null,
    isReloadSystem: false,
    setSystem(currentSystem) {
        let userId = loginUserStore.getUser().id
        this.currentSystem = currentSystem
        window.localStorage.setItem('system_' + userId, JSON.stringify(currentSystem))
    },
    getSystem() {
        if(this.currentSystem == null) {
            let userId = loginUserStore.getUser().id
            let currentSystemStr = window.localStorage.getItem('system_' + userId)
            if(currentSystemStr != null && currentSystemStr != '') {
                this.currentSystem = JSON.parse(currentSystemStr)
            }
        }
        return this.currentSystem
    },
})
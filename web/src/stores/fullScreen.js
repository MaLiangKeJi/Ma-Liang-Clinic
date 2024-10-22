import { reactive } from 'vue'

export const fullScreenStore = reactive({
    isFullScreen: false,
    toggleFullScreen() {
        // 是否全屏，否为null
        let isFull = document.fullscreenElement
        if(!isFull) {
            this.isFullScreen = true
            // document自带的全屏方法
            document.documentElement.requestFullscreen()
        } else {
            this.isFullScreen = false
            // document自带的推出全屏方法
            document.exitFullscreen()
        }
    }
})
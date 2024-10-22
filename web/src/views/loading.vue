<template>
    <div class="main">
        <div ref="loadingRef" style="height: 30%; width: 30%;"></div>
        <div>系统加载中...</div>
    </div>
</template>
<script setup>
import lottie from "lottie-web"; //引入动效库
import loading from "@/static/car-loading-data.json";
import { ref, onMounted } from "vue";

const loadingRef = ref()
onMounted(() => {
    lottie.loadAnimation({
        container: loadingRef.value, //选择渲染dom
        renderer: "svg", //渲染格式
        loop: true, //循环播放
        autoplay: true, //是否i自动播放,
        animationData: loading, //渲染动效json
    })
})
</script>
<style scoped>
    .main {
        height: 100vh;
        width: 100vw;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
</style>
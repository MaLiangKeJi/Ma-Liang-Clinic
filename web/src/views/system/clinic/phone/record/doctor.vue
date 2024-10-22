<template>
    <el-scrollbar height="100%">
        <el-button @click="routerInstance.back()">
            返回
        </el-button>
        <div style="font-size: 5vw; font-weight: 700; border-bottom: 1px solid #eeeeee;">
            联系医生
        </div>
    </el-scrollbar>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
const routerInstance = useRouter()



</script>
<style lang="scss" scoped>

</style>   
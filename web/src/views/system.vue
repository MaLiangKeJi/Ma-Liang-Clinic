<template>
	<div class="center">
        <el-tabs v-model="active" style="width: 100%;">
            <el-tab-pane label="全部" name="first">
                <el-card v-for="(system, index) in systemList" :key="index" style="margin-top: 2%;" @click="clickSystem(index)" class="company_card" :class="{ selected_system: isSelectedSystem(index) }">
                    <div class="company_head">
                        <div class="company_head_left">
                            <el-icon :size="25"><component :is="system.icon"></component></el-icon>
                            <div class="company_title">{{ system.name }}</div>
                        </div>
                        <el-button :icon="Tools" text style="background-color: #f5f7fa;">设置</el-button>
                    </div>
                </el-card>
            </el-tab-pane>
            <el-tab-pane label="其他" name="else"></el-tab-pane>
        </el-tabs>
        <el-pag ination v-model:current-page="current" v-model:page-size="size" :total="total" background layout="prev, pager, next" />
        <el-button @click="toSystem" type="primary" style="min-width: 300px; min-height: 60px; margin: 5% 0;">进入系统</el-button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
    Tools,
} from '@element-plus/icons-vue'
import { loginUserStore } from '@/stores/UserStore'
import { system } from '@/stores/system.js'
import { searchSystem as searchSystemAPI } from '@/api/back.api.js'

const active = ref('first')

const systemList = ref([])

const current = ref(1)
const size = 10
const total = ref(0)

function toSystem() {
    loginUserStore.loginFlag = 1
}
function clickSystem(index) {
    system.cutSystemIndex(index)
}
function isSelectedSystem(index) {
    return system.isSelectedSystem(index)
}

onMounted(() => {
    searchSystemAPI(res => systemList.value = res)
})
</script>

<style scoped>
.center {
    width: 100vw;
    height: 100vh;
    /* background-image: url('https://gd-hbimg.huaban.com/9165dc3c0f8279ae5402d1f47212847bff68b49f3cb547-QEli8R'); */
    background-size: 100% 100%;
    background-repeat: no-repeat;
    display: flex;
    flex-direction: column;
    position: absolute;
    padding: 5% 20% !important;
}
.company_card {
    min-height: 150px;
    margin: 5% 1%;
}
.company_head {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.company_head_left {
    width: 50%;
    display: flex;
    align-items: center;
}
.company_head_right {
    width: 50%;
    display: flex;
    flex-direction: row;
}
.company_title {
    height: 25px; 
    line-height: 25px; 
    width: 50%; 
    margin-left: 10px; 
    font-size: 15px;
}
.selected_system {
    box-shadow: rgba(3, 102, 214, 0.3) 0px 0px 0px 3px !important;
}
</style>
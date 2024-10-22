<template>
    <div class="container">
        <div style="height: 30vh; display: flex; justify-content: center;">
            <el-card class="price-card">
                <div class="ribbon-year-pay">
                    <span>免费体验</span>
                </div>
                <div class="price-body">
                    <div style="height: 30%;">
                        <div><el-text class="mx-1" style="font-size: 1vw; font-weight: 700; color: #000;">新用户</el-text></div>
                        <div><el-text class="mx-1" style="font-size: 0.6vw;" type="info">初次尝试，非常推荐</el-text></div>
                    </div>
                    <div style="height: 40%;">
                        <div>
                            <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700; color: #6b5acc;">￥0</el-text>
                            <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; color: #6b5acc;">/首周</el-text>
                        </div>
                    </div>
                    <div style="display: flex; justify-content: center;">
                        <el-button @click="toSystem" color="#0055ff" style="width: 80%;">免费体验</el-button>
                    </div>
                </div>
            </el-card>
            <el-card class="price-card">
                <div class="ribbon-new-user">
                    <span>新用户专享</span>
                </div>
                <div class="price-body">
                    <div style="height: 30%;">
                        <div><el-text class="mx-1" style="font-size: 1vw; font-weight: 700; color: #000;">月付</el-text></div>
                        <div><el-text class="mx-1" style="font-size: 0.6vw;" type="info">初次尝试，非常推荐</el-text></div>
                    </div>
                    <div style="height: 40%;">
                        <div>
                            <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700; color: #6b5acc;">￥30</el-text>
                            <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; color: #6b5acc;">/首月</el-text>
                        </div>
                        <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700;" tag="del" type="info">原价￥40</el-text>
                    </div>
                    <div style="display: flex; justify-content: center;">
                        <el-button @click="toSystem" color="#0055ff" style="width: 80%;">立即开始</el-button>
                    </div>
                </div>
            </el-card>
            <el-card class="price-card">
                <div class="price-body">
                    <div style="height: 30%;">
                        <div><el-text class="mx-1" style="font-size: 1vw; font-weight: 700; color: #000;">季付</el-text></div>
                        <div><el-text class="mx-1" style="font-size: 0.6vw;" type="info">更划算</el-text></div>
                    </div>
                    <div style="height: 40%;">
                        <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700; color: #6b5acc;">￥110</el-text>
                        <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; color: #6b5acc;">/季</el-text>
                        <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; margin-left: 1vw;" type="info">36/月</el-text>
                    </div>
                    <div style="display: flex; justify-content: center; ">
                        <el-button @click="toSystem" color="#0055ff" style="width: 80%;">立即开始</el-button>
                    </div>
                </div>
            </el-card>
            <el-card class="price-card">
                <div class="ribbon-new-user">
                    <span>新用户专享</span>
                </div>
                <div class="price-body">
                    <div style="height: 30%;">
                        <div><el-text class="mx-1" style="font-size: 1vw; font-weight: 700; color: #000;">年付</el-text></div>
                        <div><el-text class="mx-1" style="font-size: 0.6vw;" type="info">更划算</el-text></div>
                    </div>
                    <div style="height: 40%;">
                        <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700; color: #6b5acc;">￥365</el-text>
                        <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; color: #6b5acc;">/年</el-text>
                        <el-text class="mx-1" style="font-size: 0.6vw; font-weight: 700; margin-left: 1vw;" type="info">30/月</el-text>
                    </div>
                    <div style="display: flex; justify-content: center; ">
                        <el-button @click="toSystem" color="#0055ff" style="width: 80%;">立即开始</el-button>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>
<script setup>
import Logo from '@/static/logo.png'
import { useRouter } from 'vue-router'
const routerInstance = useRouter()
function toSystem() {
    routerInstance.push({ path: '/clinic/home' })
}
</script>
<style scoped>
.container {
    height: 100%; 
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
}

.partitions {
    height: 20%; 
    margin-top: 5%; 
    display: flex; 
    justify-content: center; 
    flex-direction: column; 
    align-items: center;
}
.partitions-title {
    font-size: 1vw;
}
.partitions-divider {
    width: 10vw; 
    border: 5px solid #6b5acc;
}
.partitions-subtitle {
    font-size: 0.8vw;
}
.price-card {
    height: 100%; 
    width: 12vw; 
    margin: 0 2vw;
    display: flex;
    flex-direction: column; 
    justify-content: center; 

    position: relative;
}
.price-body {
    height: 100%; 
    padding: 10%;
}

.ribbon-new-user { width: 140px; height: 200px; position: absolute; top: -8px; right: -8px; overflow: hidden; }
.ribbon-new-user::before { position: absolute; right: 124px; border-radius: 8px 8px 0 0; width: 16px; height: 8px; background-color: #0011cc; content: ''; }
.ribbon-new-user::after { position: absolute; right: 0; top: 124px; border-radius: 0 8px 8px 0; width: 8px; height: 16px; background-color: #0011cc; content: ''; }
.ribbon-new-user span { display: inline-block; text-align: center;  width: 200px; height: 40px; line-height: 40px; position: absolute; top: 30px; right: -50px; z-index: 2; overflow: hidden; -ms-transform: rotate(45deg); -moz-transform: rotate(45deg); -webkit-transform: rotate(45deg); -o-transform: rotate(45deg); transform: rotate(45deg); border: 1px dashed #fff; box-shadow: 0 0 0 3px #0055ff,  0 14px 7px -9px rgba(0,0,0,.6);  background-color: #0055ff; color: #fff; }


.ribbon-year-pay { width: 140px; height: 200px; position: absolute; top: -8px; right: -8px; overflow: hidden; }
.ribbon-year-pay::before { position: absolute; right: 124px; border-radius: 8px 8px 0 0; width: 16px; height: 8px; background-color: #ff4a23; content: ''; }
.ribbon-year-pay::after { position: absolute; right: 0; top: 124px; border-radius: 0 8px 8px 0; width: 8px; height: 16px; background-color: #ff4a23; content: ''; }
.ribbon-year-pay span { display: inline-block; text-align: center;  width: 200px; height: 40px; line-height: 40px; position: absolute; top: 30px; right: -50px; z-index: 2; overflow: hidden; -ms-transform: rotate(45deg); -moz-transform: rotate(45deg); -webkit-transform: rotate(45deg); -o-transform: rotate(45deg); transform: rotate(45deg); border: 1px dashed #fff; box-shadow: 0 0 0 3px #ff4a23,  0 14px 7px -9px rgba(0,0,0,.6);  background-color: #ff4a23; color: #fff; }

</style>
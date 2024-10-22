<template>
    <div class="container">
        <el-container style="height: 100%;">
            <el-header style="height: 5%;">
                <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" :ellipsis="false"
                    style="height: 100%; width: 100%; border-bottom: 1px solid var(--el-menu-border-color); padding: 0 5%; display: flex;">
                    <!-- 左边部分菜单 -->
                    <div style="display: flex; align-items: center;">
                        <el-menu-item index="0" @click="toOfficial">
                            <img :src="Logo" alt="码良云" style="width: 5vw; object-fit: contain;" />
                        </el-menu-item>
                        <el-menu-item @click="toOfficial"><span style="font-weight: 700;">首页</span></el-menu-item>
                        <!-- <el-menu-item @click="toPrice"><span style="font-weight: 700;">价格</span></el-menu-item> -->
                        <el-menu-item @click='toCompanyProfile'><span
                                style="font-weight: 700;">关于我们</span></el-menu-item>
                        <el-menu-item @click='toContactInformation'><span
                                style="font-weight: 700;">联系我们</span></el-menu-item>
                    </div>

                    <!-- 右边部分菜单 -->
                    <div style="display: flex; align-items: center;margin-left: auto;">
                        <el-menu-item @click="">
                            <el-icon>
                                <PhoneFilled />
                            </el-icon>联系电话：15993075857
                        </el-menu-item>
                        <div style="height: 100%; display: flex; align-items: center; margin-left: 20px;">
                            <el-button style="height: 80%; font-size: 0.6vw;"
                                @click="() => systemSwitch.selectAndToClinicSystem()" type="success">
                                登录/注册
                            </el-button>
                        </div>
                    </div>
                </el-menu>
            </el-header>
            <el-main style="height: 95%;">
                <el-scrollbar height="100%">
                    <div v-show="currentModel == 'official'" style="height: 100%;">
                        <OfficialView />
                    </div>
                    <!-- <div v-show="currentModel == 'price'" style="height: 100%;">
                        <PriceView />
                    </div> -->
                    <div v-show="currentModel == 'companyProfile'" style="height: 100%;">
                        <CompanyProfileView />
                    </div>
                    <div v-show="currentModel == 'contactInformation'" style="height: 100%;">
                        <ContactInformationView />
                    </div>
                </el-scrollbar>
            </el-main>
        </el-container>
    </div>
</template>
<script setup>
import { ref } from 'vue'
import Logo from '@/static/logo.png'

import OfficialView from '@/views/official/official.vue'
import PriceView from '@/views/official/price.vue'
import CompanyProfileView from '@/views/official/companyProfile.vue'
import ContactInformationView from '@/views/official/contactInformation.vue'

import { systemSwitch } from '@/stores/app.js'

const currentModel = ref('official')
function toOfficial() {
    currentModel.value = 'official'
}
function toForum() {
    window.location.href = 'https://forum.maliang.work/'
}
function toPrice() {
    currentModel.value = 'price'
}
function toCompanyProfile() {
    currentModel.value = 'companyProfile'
}
function toContactInformation() {
    currentModel.value = 'contactInformation'
}
</script>
<style scoped>
.container {
    height: 100%;
    background-color: #fff;
}

:deep(.el-header) {
    padding: 0;
}

.flex-grow {
    flex-grow: 1;
}

.inform {
    background-color: #000;
    color: #ffffff;
    text-align: center;
    font-size: 1.4;
    line-height: 60px;
}

.text-linear {
    background-image: linear-gradient(to right, #8781ff, #00ff95);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

:deep(.el-main) {
    height: 100%;
}

:deep(.el-carousel__container) {
    height: 100%;
    text-align: center;
}

.el-menu--horizontal>.el-menu-item:nth-child(1) {
    margin-right: auto;
}

:deep(.el-scrollbar__view) {
    height: 100%;
}

:deep(.el-card__body) {
    height: 90%;
    padding: 0;
}

:deep(.el-card__footer) {
    height: 10%;
    padding: 0;
}

:deep(.footer_btn.el-button) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}

:deep(.footer_btn.el-button:active) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}

:deep(.footer_btn.el-button:hover) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}

:deep(.first-video .el-card__body) {
    height: 100%;
    padding: 0;
}

:deep(.el-scrollbar__view) {
    height: 100%;
}

.first {
    height: 60%;
    width: 100%;

    background-image: url('../static/pexels-pixabay-40568.jpg');
    /* 背景图垂直、水平均居中 */
    background-position: center center;
    /* 背景图不平铺 */
    background-repeat: no-repeat;
    /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
    background-attachment: fixed;
    /* 让背景图基于容器大小伸缩 */
    background-size: cover;

    border-radius: 0 0 80% 80%;
}

.first-body {
    height: 100%;
    width: 100%;

    background-color: rgba(255, 255, 255, 0.25);
    backdrop-filter: blur(6px);
    -webkit-backdrop-filter: blur(6px);
    border: 0.666667px solid rgba(255, 255, 255, 0.18);
    box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
    -webkit-box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
    border-radius: 12px;
    -webkit-border-radius: 12px;
    color: rgba(255, 255, 255, 0.75);

    border-radius: 0 0 80% 80%;
}

:deep(.el-carousel__item) {
    padding: 0 !important;
}

.timestamp {
    height: 100%;
    width: 60%;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    flex-wrap: wrap;
    height: 5vh;
}

.timestamp_time {
    font-size: 1.5em;
    height: 50% !important;
    width: min-content;
}

.timestamp_date {

    height: 50% !important;
    width: 100%;
}



.slide-fade-enter-active {
    transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
    transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
    transform: translateX(20px);
    opacity: 0;
}

:deep(.el-carousel__container) {
    height: 100% !important;
}

:deep(.el-carousel__item) {
    height: 100%;
}

:deep(.el-image) {
    height: 100%;
}

.title {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 30vh;
}

.title-text {
    height: 50%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.title-text-title {
    text-align: center;
    font-size: 1.5vw;
    font-weight: 700;
    color: #000;
}

.title-text-subtitle {
    font-size: 1vw;
}

.title-entrance-btn {
    background-color: #6b5acc;
    color: #fff;
    height: 5vh;
    width: 8%;
    border-radius: 15px;
}

.title-entrance-btn span {
    font-size: 1vw;
}

.introduction {
    height: 100vh;
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
</style>
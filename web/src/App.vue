<template>
    <div id="app" style="width: 100%; height: 100%;">
        <div v-if="load">
            <Loading />
        </div>
        <el-container v-else style="user-select: none; height: 100%; width: 100vw; background-color: #f2f1ff;">
            <el-container style="height: 100%;">
                <div 
                    v-if="leftMenuStore.isShowLeftMenu"
                    style="height: 100%; background-color: #6A5ACD;"
                    :style="{ 'width': isCollapse ? '64px' : '200px' }"
                >
                    <div style="height: 10%; width: 100%; display: flex; align-items: center;" v-if="isCollapse">
                        <el-button @click="isCollapse = false" link style="width: 100%; height: 64px;">
                            <el-icon style="font-size: 2em; color: #fff;">
                                <Expand />
                            </el-icon>
                        </el-button>
                    </div>
                    <div style="height: 10%; width: 100%; display: flex; align-items: center;" v-else>
                        <img
                            :src="Logo2"
                            alt="码良云"
                            style="width: 80%; object-fit: contain; cursor: pointer;"
                            @click="routerInstance.push('/')"
                        />
                        <el-button  @click="isCollapse = true" link style="height: 56px;">
                            <el-icon style="font-size: 2em; color: #fff;">
                                <Fold />
                            </el-icon>
                        </el-button>
                    </div>
                    <div style="height: 90%;">
                        <el-scrollbar height="100%;">
                            <el-menu
                                default-active="1"
                                :router="true"
                                :collapse="isCollapse"
                                :collapse-transition="false"
                                style="height: 100%; border:0 !important; background-color: #6A5ACD;"
                                :unique-opened="true"
                            >
                                <div 
                                    v-for="(systemRouter, index) of systemRouterStore.getRouterTree()" 
                                    :key="index"
                                >
                                    <el-sub-menu v-if="systemRouter.type == 0" :index="''+ (index + 1)" 
                                        :class="{ 'menu-collapse': isCollapse }" 
                                        :style="{ 'background-color': systemRouter.isMouse ? '#fff': '#6A5ACD' }"
                                    >
                                        <template #title>
                                            <el-icon v-if="systemRouter.iconName">
                                                <component 
                                                    style="font-size: 1.5em;" 
                                                    :style="{ 'color': systemRouter.isMouse ? '#6A5ACD' : '#fff' }" 
                                                    :is="systemRouter.iconName" 
                                                />
                                            </el-icon>
                                            <span 
                                                v-show="!isCollapse"
                                                :style="{ 'color': systemRouter.isMouse ? '#6A5ACD' : '#fff' }"
                                                style="font-weight: 700; margin-left: 20px;"
                                            >{{ systemRouter.title }}</span>
                                        </template>
                                        <el-menu-item v-show="!isCollapse" v-for="(children, childrenIndex) of systemRouter.children" :key="childrenIndex" :index="children.path"
                                            style="background-color: #6A5ACD; color: #fff; font-weight: 700"
                                        >
                                            {{ children.title }}
                                        </el-menu-item>
                                    </el-sub-menu>
                                    <el-menu-item v-else-if="systemRouter.type == 1" :index="systemRouter.path" 
                                        :class="{ 'menu-collapse': isCollapse }" 
                                        :style="{ 'background-color': systemRouter.isMouse ? '#fff': '#6A5ACD' }"
                                    >
                                        <el-icon>
                                            <component 
                                                style="font-size: 1.5em;" 
                                                :style="{ 'color': systemRouter.isMouse ? '#6A5ACD' : '#fff' }" 
                                                :is="systemRouter.iconName" 
                                            />
                                        </el-icon>
                                        <span 
                                            v-if="!isCollapse"
                                            :style="{ 'color': systemRouter.isMouse ? '#6A5ACD' : '#fff' }"
                                            style="font-weight: 700; margin-left: 20px;"
                                        >{{ systemRouter.title }}</span>
                                    </el-menu-item>
                                    <div class="flex-grow" />
                                </div>
                            </el-menu>
                        </el-scrollbar>
                    </div>
                </div>
                <el-main :style="{ width: isCollapse ? '85%' : '100vw'}">
                    <RouterView />
                </el-main>
            </el-container>
            <div>
                <el-popover
                    v-if='isShowFeed'
                    placement="left"
                    :width="600"
                    trigger="hover"
                    popper-class="popover"
                >
                    <template #reference>
                        <el-button @click="() => feedbackDialogVisible = true" style="width: 3vw; height: 3vw; position: fixed; right: 1vw; bottom: 20vh; z-index: 9999;" :icon="Service" type="primary" plain circle>
                            <el-icon size="2vw"><Service /></el-icon>
                        </el-button>
                    </template>
                    <div style="padding: 5%;">
                        <div style="display: flex; margin-bottom: 1vh">
                            <el-icon color="#6a5acd" size="1.5vw" style="margin-right: 0.5vw;"><Service /></el-icon>
                            <el-text class="mx-1" style="font-size: 1vw; color: #6a5acd;">联系客服</el-text>
                        </div>
                        <div style="margin-bottom: 1vh">
                            <el-text class="mx-1" style="font-size: 0.8vw;" type="info">专业工程师在线解决问题</el-text>
                        </div>
                        <div>
                            <el-button @click="() => feedbackDialogVisible = true" style="width: 100%; font-size: 0.8vw;" type="primary">联系</el-button>
                        </div>
                    </div>
                </el-popover>
            </div>
            <el-dialog v-model="feedbackDialogVisible" title="问题与反馈" width="1200">
                <div style="height: 60vh; padding: 5%;">
                    <div>
                        <el-input
                            v-model="feedback"
                            style="width: 100%; font-size: 1vw;"
                            :rows="10"
                            type="textarea"
                            placeholder="问题与意见"
                        />
                    </div>
                    <div style="margin-top: 1vw;">
                        <div style="margin-bottom: 0.5vw;">
                            <el-text class="mx-1" style="font-size: 0.8vw;" type="info">留下电话，客服会稍后联系您</el-text>
                        </div>
                        <div>
                            <el-input v-model="phone" style="width: 30%; height: 2vw; font-size: 1vw;" placeholder="联系电话" />
                        </div>
                        <div style="display: flex; justify-content: center; margin-top: 2vw;">
                            <el-button @click="sendFeedback" style="height: 3vw; width: 30%; font-size: 0.8vw;" type="primary">发送</el-button>
                        </div>
                    </div>
                </div>
            </el-dialog>
        </el-container>
    </div>
</template>
<script setup>
import { onBeforeMount, watch } from 'vue'
import { checkLogin } from '@/stores/UserStore'
import { leftMenuStore, globalLoading } from '@/stores/app.js'
import { ref } from 'vue'
import _ from "lodash";
import { Expand, Fold } from '@element-plus/icons-vue'
import { RouterView, useRouter } from 'vue-router'
import { systemRouterStore } from '@/stores/app.js'
import Logo2 from '@/static/logo2.png'
import Loading from '@/views/loading.vue'
import { ElNotification, } from 'element-plus';
import { notCheckLoginPath, notContactServicePath } from '@/router/index.js'
import { add as addFeedAPI, } from '@/api/clinic/feedback.api.js';


const isShowFeed = ref(false);
const load = ref(false)
watch(
    () => {
        showFeed();
        return globalLoading.loading;
    },
    value => {
        load.value = value;
    },
    { deep: true, immediate: true }
)

const routerInstance = useRouter();
watch(
    () => routerInstance.currentRoute.value.path,
    value => showFeed(value),
    { deep: true, immediate: true }
)

function showFeed(value) {
    if (notContactServicePath.includes(value))
        isShowFeed.value = false;
    else
        isShowFeed.value = true;
}


const isCollapse = ref(false)

onBeforeMount(async () => {
    let path = window.location.pathname
    if(path == '/') {   //如果是官网，则需要检查是否登录，但不需要阻塞系统加载（直接进入）
        checkLogin()
    } else if(notCheckLoginPath.includes(path)) { 
    } else if(!path.includes("/login")) {   //如果其他页面（系统内），则需要阻塞并检查是否登录（排除登录页面）
        globalLoading.openGlobalLoading()
        if(!await checkLogin()) {
            routerInstance.push({ path: '/login' });
        }
    }
})

const feedbackDialogVisible = ref(false)
const feedback = ref('')
const phone = ref('')
function sendFeedback() {
    if (phone.value != undefined && phone.value != null && phone.value.length > 0) {
        let regPhone = /^1[3456789]\d{9}$/;
        if (!regPhone.test(phone.value)) {
            ElNotification({
                title: '问题与反馈',
                message: '请输入正确的手机号',
                type: 'warning',
            })
            return;
        }
    }

    addFeedAPI({
        feedback: feedback.value,
        phone: phone.value
    }, resp => {
        if (resp) {
            feedback.value = "";
            phone.value = "";
            feedbackDialogVisible.value = false;
        }
    })
}
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}

:deep(.el-menu-item.is-active) {
    background-color: #ecf5ff;
}
.item {
  width: 100%; height: 60px; line-height: 60px; display: flex; justify-content: space-between; align-items: center;
}
.item:hover {
  color: #438ee4;
  cursor: pointer;
}
.collapse_btn {
    width: 100%; 
    height: 10%; 
    line-height: 10%; 
    text-align: center; 
    background-color: #ffffff; 
    display: flex; 
    justify-content: space-around; 
    align-items: center; 
}
.el-container{height:100%;padding:0;margin:0;width:100%;}
header {
    line-height: 1.5;
    width: 100%;
    max-height: 100vh;
    padding: 0;
}

.logo {
display: block;
margin: 0 auto 2rem;
}

nav {
width: 100%;
font-size: 12px;
text-align: center;
margin-top: 2rem;
}

nav a.router-link-exact-active {
color: var(--color-text);
}

nav a.router-link-exact-active:hover {
background-color: transparent;
}

nav a {
display: inline-block;
padding: 0 1rem;
border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
border: 0;
}

@media (min-width: 1024px) {
    header {
        display: flex;
        place-items: center;
    }

    .logo {
        margin: 0 2rem 0 0;
    }

    header .wrapper {
        display: flex;
        place-items: flex-start;
        flex-wrap: wrap;
    }

    nav {
        text-align: left;
        margin-left: -1rem;
        font-size: 1rem;

        padding: 1rem 0;
        margin-top: 1rem;
    }
}
.slide-fade-enter-active {
    transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
    transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from, .slide-fade-leave-to {
    transform: translateX(20px);
    opacity: 0;
}
.v-enter-active,
.v-leave-active {
    transition: opacity 0.5s ease;
}

.v-enter-from, .v-leave-to {
    opacity: 0;
}
.popover {
    display: none !important;
}
</style>

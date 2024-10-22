<template>
    <div class="page">
        <div class="deciduous_background">

        </div>
        <el-scrollbar style="width: 100%; height: 80%;">
            <div style="display: flex; background-color: #fff;">
                <div style="width: 20%; padding-top: 2%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                </div>
                <div style="height: 100%; width: 60%; display: flex; flex-direction: column; align-items: center; margin-bottom: 5%;">
                    <div id="invite-container" style="height: 10vh; width: 100%; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 1.5vw; font-weight: 700;">邀请使用</span>
                    </div>
                    <el-card style="height: 60vh; width: 100%; border-radius: 15px;">
                        <div style="height: 100%; padding: 2% 5%;">
                            <div style="height: 60%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                                <div>
                                    <el-input v-model="inviteCode" style="width: 20vw; height: 5vh;" placeholder="">
                                        <template #append>
                                            <el-button @click="copyPath(inviteCode)" :icon="DocumentCopy" />
                                        </template>
                                    </el-input>
                                </div>
                                <el-text class="mx-1" type="info" style="font-size: 0.8vw; margin: 1vw 0">将链接发送给朋友，使用电脑浏览器打开即可</el-text>
                            </div>
                            <div style="height: 40%;">
                                <el-table :data="inviteList" style="width: 100%; height: 60%;">
                                    <el-table-column prop="invitedUserName" label="受邀用户">
                                        <template #default="{ row }">
                                            {{ row.invitedUser.name }}
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="invitedUserPhone" label="手机号">
                                        <template #default="{ row }">
                                            {{ row.invitedUser.phone }}
                                        </template>
                                    </el-table-column>
                                </el-table>
                                <el-pagination
                                    v-model:current-page="inviteListCurrent"
                                    v-model:page-size="inviteListSize"
                                    v-model:total="inviteListTotal"
                                    :page-sizes="[10, 20, 50, 100, 500]"
                                    @current-change="getInviteList"
                                    @size-change="getInviteList"
                                    style="height: 40%; padding: 0 5%; font-size: 1.2vh"
                                    background
                                    layout="total, sizes, ->, prev, pager, next, jumper"
                                    size="large"
                                />
                            </div>
                        </div>
                    </el-card>
                    <div id="subscription-container" style="height: 10vh; width: 100%; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 1.5vw; font-weight: 700;">我的订阅</span>
                    </div>
                    <el-card class="subscription-card" style="width: 100%;">
                        <div style="height: 100%; padding: 3% 5%; ">
                            <div style="height: 50%; display: flex; justify-content: space-between; align-items: center;">
                                <span style="font-size: 1vw; font-weight: 700;">码良云诊所（试用版）</span>
                                <div style="width: 20%; display: flex; align-items: center;">
                                    <div class="status-point" style="background-color: #67C23A" />
                                    <span style="margin-left: 1vw; font-size: 0.8vw; font-weight: 700;">已激活</span>
                                </div>
                            </div>
                            <div style="height: 50%; display: flex; justify-content: space-between;">
                                <div style="width: 0;"></div>
                                <div style="width: 20%; height: 100%; display: flex; flex-direction: column; justify-content: space-between;">
                                    <el-text style="width: 100%; font-size: 0.8vw;" class="mx-1" type="info">将于 2024/9/24 日过期</el-text>
                                    <el-button @click="routerInstance.push({ path: '/clinic/renew' })" link style="width: 30%; font-size: 0.8vw; text-align: start;" type="primary">
                                        立即续费
                                    </el-button>
                                </div>
                            </div>
                        </div>
                    </el-card>
                    <el-card class="subscription-card">
                        <div style="height: 100%; padding: 3% 5%;">
                            <div style="height: 50%; display: flex; justify-content: space-between; align-items: center;">
                                <span style="font-size: 1vw; font-weight: 700;">码良云诊所（年费版）</span>
                                <div style="width: 20%; display: flex; align-items: center;">
                                    <div class="status-point" style="background-color: #dcdfe6" />
                                    <span style="margin-left: 1vw; font-size: 0.8vw; font-weight: 700;">未拥有</span>
                                </div>
                            </div>
                            <div style="height: 50%; display: flex; justify-content: space-between;">
                                <div style="width: 20%;">
                                    <el-button @click="() => anchorRef.scrollTo('#task-container')" link>
                                        <el-text style="width: 100%; font-size: 0.8vw;" class="mx-1" type="success">

                                        <a href="#task-container">完成任务免费领取</a>
                                        </el-text>
                                    </el-button>
                                </div>
                                <div style="width: 20%; height: 100%; display: flex; flex-direction: column; justify-content: space-between;">
                                    <el-text style="width: 100%; font-size: 0.8vw;" class="mx-1" type="info">共 365 天可用</el-text>
                                </div>
                            </div>
                        </div>
                    </el-card>
                    <div id="pay-log-container" style="height: 10vh; width: 100%; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 1.5vw; font-weight: 700;">付款记录</span>
                    </div>
                    <el-card style="height: 40vh; width: 100%; border-radius: 15px;">
                        <div style="height: 100%; width: 100%; padding: 2%; display: flex; flex-direction: column; justify-content: space-between;">
                            <el-table :data="payLogs" style="width: 100%;">
                                <el-table-column prop="createTimeStr" label="购买日期" />
                                <el-table-column prop="orderId" label="订单号" />
                                <el-table-column prop="packageType" label="类型">
                                    <template #default="{ row }">
                                        <div>{{ packageType(row) }}</div>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination
                                v-model:current-page="current"
                                v-model:page-size="size"
                                v-model:total="total"
                                :page-sizes="[10, 20, 50, 100, 500]"
                                @current-change="handle.pageCurrentChange"
                                @size-change="handle.pageCurrentChange"
                                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                                background
                                layout="total, sizes, ->, prev, pager, next, jumper"
                                size="large"
                            />
                        </div>
                    </el-card>
                    <div style="height: 10vh; width: 100%; display: flex; justify-content: flex-end; align-items: center;">
                        <el-button type="primary" link>
                            <el-text @click="contactDialog = true" class="mx-1" type="danger" style="font-size: 0.8vw;">订单有疑问？联系专属客服</el-text>
                        </el-button>
                    </div>
                    <div id="task-container" style="height: 10vh; width: 100%; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 1.5vw; font-weight: 700;">奖励任务</span>
                    </div>
                    <el-card style="height: 40vh; width: 100%; border-radius: 15px;">
                        <div style="height: 100%; padding: 2% 5%;">
                            <div style="height: 10vh; display: flex; justify-content: space-between; align-items: center;">
                                <div style="height: 100%; display: flex; align-items: center;">
                                    <el-icon color="#67c23a" size="1vw" style="margin-right: 0.5vw;"><CircleCheckFilled /></el-icon>
                                    <span style="font-size: 0.8vw;">邀请 1 家诊所使用</span>
                                </div>
                                <div style="display: flex; align-items: center;">
                                    <div style="height: 100%; display: flex; align-items: center;">
                                        <el-icon color="#67c23a" size="1vw" style="margin-right: 0.5vw;"><Present /></el-icon>
                                        <el-text class="mx-1" type="danger" style="font-size: 1vw; margin-right: 0.5vw;">35</el-text>
                                        <el-text class="mx-1" style="font-size: 0.8vw;">元现金奖励</el-text>
                                    </div>
                                    <span style="font-size: 0.8vw; margin: 0 1vw;">或</span>
                                    <div style="height: 100%; display: flex; align-items: center;">
                                        <el-text class="mx-1" type="success" style="font-size: 1vw; margin-right: 0.5vw;">1个月</el-text>
                                        <el-text class="mx-1" type="success" style="font-size: 0.8vw;">订阅卷</el-text>
                                    </div>
                                </div>
                                <el-button @click="getInviteFirstTaskReward" style="width: 5vw;" type="success" v-if="inviteFirstTaskRewardState == -1">领取</el-button>
                                <el-button @click="() => anchorRef.scrollTo('#invite-container')" style="width: 5vw;" type="info" v-else-if="inviteFirstTaskRewardState == 0">
                                    <a href="#invite-container" style="color: #fff;">去完成</a>
                                </el-button>
                                <el-button style="width: 5vw;" type="info" v-else-if="inviteFirstTaskRewardState == 1">
                                    已完成
                                </el-button>
                            </div>
                        </div>
                    </el-card>
                    <div style="height: 10vh; width: 100%; display: flex; justify-content: flex-end; align-items: center;">
                        <el-button type="primary" link>
                            <el-text @click="contactDialog = true" class="mx-1" type="danger" style="font-size: 0.8vw;">订单有疑问？联系专属客服</el-text>
                        </el-button>
                    </div>
                    
                </div>
            </div>
        </el-scrollbar>
        <div style="width: 20vw; position: fixed; left: 0; top: 30%; display: flex; flex-direction: column; justify-content: space-between; align-items: center;">
            <el-button @click="routerInstance.push({ path: '/clinic/home' })" link style="margin-bottom: 5vh;">
                <el-icon size="1.5vw" style="margin-right: 1vw;"><ArrowLeftBold /></el-icon>
                <span style="font-size: 1.2vw;">回到系统</span>
            </el-button>
            <el-anchor ref="anchorRef" type="underline" :offset="70">
                <el-anchor-link style="margin: 2vh 0;" href="#invite-container">
                    <span style="font-size: 2vh;">邀请使用</span>
                </el-anchor-link>
                <el-anchor-link style="margin: 2vh 0;" href="#subscription-container">
                    <span style="font-size: 2vh;">我的订阅</span>
                </el-anchor-link>
                <el-anchor-link style="margin: 2vh 0;" href="#pay-log-container">
                    <span style="font-size: 2vh;">付款记录</span>
                </el-anchor-link>
                <el-anchor-link style="margin: 2vh 0;" href="#task-container">
                    <span style="font-size: 2vh;">奖励任务</span>
                </el-anchor-link>
            </el-anchor>
        </div>
        <el-dialog v-model="contactDialog" :show-close="false" width="500">
            <div style="display: flex; flex-direction: column; align-items: center;">
                <el-image :src="contactQRCode"></el-image>
                <el-text class="mx-1" type="info" style="font-size: 0.8vw; margin: 1vw 0">使用微信扫一扫，关注微信号后留言</el-text>
            </div>
        </el-dialog>
        <el-backtop @click="contactDialog = true" :bottom="100" :visibility-height="0" style="width: 8vh; height: 8vh;">
            <div style="width: 100%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                <el-icon size="3vh"><Service /></el-icon>
                <span style="font-size: 0.6vw;">联系客服</span>
            </div>
        </el-backtop>
    </div>
</template>
<script setup>
import { search as searchRenewLogAPI } from '@/api/clinic/renewLog.api.js'
import { ref, onMounted } from 'vue'
import { DocumentCopy } from '@element-plus/icons-vue'
import { getInvite as getInviteAPI, getInviteList as getInviteListAPI, checkIsInvite as checkIsInviteAPI } from '@/api/user.api.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { Service } from '@element-plus/icons-vue'
import { getInviteFirstTaskReward as getInviteFirstTaskRewardAPI } from '@/api/clinic/reward.api.js'

const routerInstance = useRouter()

const currentModel = ref('mySubscribe')

onMounted(() => {
    let query = routerInstance.currentRoute.value.query
    if(query && query.code) {
        currentModel.value = 'guest'
    }
    getInvite(() => currentModel.value = 'invitation')
    getInviteList()
    checkIsInvite()
})

const anchorRef = ref()

const payLogs = ref([])
const current = ref(1)
const size = ref(10)
const total = ref(0)


const inviteListCurrent = ref(1)
const inviteListSize = ref(10)
const inviteListTotal = ref(0)
function packageType(row) {
    switch(row.packageType) {
        case 1:
            return '月付'
        case 2:
            return '季付'
        case 3:
            return '年付'
    }
}
const inviteCode = ref()

const baseUrl = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '') + '/clinic/login?code=';
function getInvite(callback) {
    getInviteAPI(res => {
        inviteCode.value = baseUrl + res
        if(callback && typeof callback === "function") callback()
    })
}
const inviteList = ref([])
function getInviteList() {
    getInviteListAPI({ current: inviteListCurrent.value, size: inviteListSize.value, }, res => {
        inviteList.value = res.records
        inviteListTotal.value = res.total
    })
}
function searchPayLog(callback) {
    searchRenewLogAPI(current, size, res => {
        payLogs.value = res.records
        total.value = res.total
        if(callback && typeof callback === "function") callback()
    })
}
const inviteFirstTaskRewardState = ref(false)
function checkIsInvite() {
    checkIsInviteAPI(res => {
        inviteFirstTaskRewardState.value = res
    })
}
const handle = {
    pageCurrentChange() {
        searchPayLog()
    },
}
function copyPath(text) {
    copy(text)
}
function copy(text) {
    // 添加一个input元素放置需要的文本内容
    const input = document.createElement("input");
    input.value = text;
    document.body.appendChild(input);
    // 选中并复制文本到剪切板
    input.select();
    document.execCommand("copy");
    // 移除input元素
    document.body.removeChild(input);
    ElMessage({
        message: "复制成功",
        type: "success",
    });
}


function getInviteFirstTaskReward() {
    getInviteFirstTaskRewardAPI(() => {
        inviteFirstTaskRewardState.value = 1
        ElMessage({
            message: '奖励已发送',
            type: 'success',
        })
    })
}

import contactQRCode from '@/static/qrcode_for_gh_2ef329281494_344.jpg'
const contactDialog = ref(false)
</script>
<style scoped>
.page {
    height: 100vh;
    width: 100%;
    background-color: #f9f9f9;
}
.subscription-card {
    width: 60vw; 
    height: 20vh; 
    border-radius: 15px;
    margin-bottom: 5vh;
}
:deep(.el-card__body) {
    padding: 0;
    height: 100%;
}
.deciduous_background {
    height: 20%;
    width: 100%;
    background-image: url('../../../../static/deciduousBackground.png');
    /* 背景图垂直、水平均居中 */
    background-position: center center;
    /* 背景图不平铺 */
    background-repeat: no-repeat;
    /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
    background-attachment: fixed;
    /* 让背景图基于容器大小伸缩 */
    background-size: cover;
}
.status-point {
    display: inline-block;
    width: 12px;
    height: 12px;
    border-radius: 50%;
}
.card {
    width: 60vw; 
    height: 20vh; 
    margin-bottom: 5vh;

    -webkit-mask-image: radial-gradient(circle at right 169px bottom 10px, transparent 10px, red 10.5px), linear-gradient(transparent 25%, red 0, red 75%, transparent 0);
    -webkit-mask-size: 100%, 5px 16px;
    -webkit-mask-repeat: repeat, repeat-y;
    -webkit-mask-position: 0 10px, calc(100% - 166.5px);
    -webkit-mask-composite: source-out;
    mask-composite: subtract;
}
         
.task-one {
    background-image: linear-gradient(to right, #4CB8C4 0%, #3CD3AD  51%, #4CB8C4  100%);
    margin: 10px;
    padding: 15px 45px;
    text-align: center;
    text-transform: uppercase;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;            
    box-shadow: 0 0 20px #eee;
    border-radius: 10px;
    display: block;
}

.task-one:hover {
    background-position: right center; /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
}


         
.task-tow {
    background-image: linear-gradient(to right, #FF8008 0%, #FFC837  51%, #FF8008  100%);
    margin: 10px;
    padding: 15px 45px;
    text-align: center;
    text-transform: uppercase;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;            
    box-shadow: 0 0 20px #eee;
    border-radius: 10px;
    display: block;
}

.task-tow:hover {
    background-position: right center; /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
}

         
.task-three {
    background-image: linear-gradient(to right, #AA076B 0%, #61045F  51%, #AA076B  100%);
    margin: 10px;
    padding: 15px 45px;
    text-align: center;
    text-transform: uppercase;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;            
    box-shadow: 0 0 20px #eee;
    border-radius: 10px;
    display: block;
}

.task-three:hover {
    background-position: right center; /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
}
 
 
:deep(.el-scrollbar__view) {
    height: 100%;
}
</style>
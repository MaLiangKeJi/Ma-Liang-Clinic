<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">个人信息</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%;">
            <div style="height: 100%; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <el-descriptions
                        class="margin-top"
                        title="我的"
                        :column="2"
                        :border="true"
                        size="large"
                    >
                        <template #extra>
                                <el-button :icon="Setting" @click="clickEdit">
                                    修改
                                </el-button>
    
                                <el-button :icon='CirclePlus' @click='invite()'>
                                    邀请
                                </el-button>
                        </template>
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><User /></el-icon>
                                    姓名
                                </div>
                            </template>
                            <el-input v-if="isEditUser" v-model="user.name" placeholder="请输入姓名" />
                            <span v-else>{{ user.name ? user.name : user.phone }}</span>
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><Iphone /></el-icon>
                                    手机号
                                </div>
                            </template>
                            <div v-if="isEditUser" style="display: flex;">
                                <el-input v-model="user.phone" placeholder="请输入手机号" style="width: 80%;" />
                                <el-input v-model="user.code" placeholder="验证码" style="width: 20%; margin: 0 5px 0 20px;" />
                                <el-button :disabled="isSendCode" @click="sendCode" type="primary">{{ isSendCode ? codeTime + 's' : '发送验证码'}}</el-button>
                            </div>
                            <span v-else>{{ user.phone }}</span>
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><MilkTea /></el-icon>
                                    性别
                                </div>
                            </template>
                            <el-select
                                v-if="isEditUser"
                                v-model="user.sex"
                                size="large"
                            >
                                <el-option label="男" :value="1" />
                                <el-option label="女" :value="0" />
                            </el-select>
                            <span v-else>{{ user.sex ? (user.sex == 1 ? '男' : '女') : '' }}</span>
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><Postcard /></el-icon>
                                    身份证号
                                </div>
                            </template>
                            <el-input v-if="isEditUser" v-model="user.idCard" placeholder="请输入身份证号" />
                            <span v-else>{{ user.idCard }}</span>
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-descriptions
                        class="margin-top"
                        title="密码"
                        :column="2"
                        :border="true"
                        size="large"
                    >
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><Postcard /></el-icon>
                                    密码
                                </div>
                            </template>
                            <el-button @click="dialogResetPWDVisible = true" style="width: 100%; height: 100%;" text type="danger">
                                重设密码
                            </el-button>
                        </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions
                        class="margin-top"
                        title="营业"
                        :column="3"
                        :border="true"
                        size="large"
                    >
                        <el-descriptions-item>
                            <template #label>
                                <div class="cell-item">
                                    <el-icon :style="iconStyle"><Postcard /></el-icon>
                                    营业状态
                                </div>
                            </template>

                            <el-button v-if='isWorkByUI=="true"' @click='resetWork()' style="width: 100%; height: 100%;" text type="success">
                                开始营业
                            </el-button>
                            <el-button v-else-if='isWorkByUI=="false"' @click='resetWork()' style="width: 100%; height: 100%;" text type="danger">
                                停止营业
                            </el-button>
                        </el-descriptions-item>
                    </el-descriptions>
                </div>
                <div style="width: 100%; display: flex; justify-content: center; margin-top: 4%;">
                    <el-button @click="editUser" type="primary" style="width: 200px; height: 50px;" >
                        保存
                    </el-button>
                    <!-- <el-button @click="deleteUser" type="danger" style="width: 200px; height: 50px;" >
                        注销
                    </el-button> -->
                </div>
            </div>
        </el-card>
        <el-dialog v-model="dialogResetPWDVisible" title="重置密码" width="800">
            <div v-if="resetIsNotOK" style="padding: 10%;">
                <el-form :model="resetPWDForm" label-width="auto">
                    <el-form-item label="新密码">
                        <el-input v-model="resetPWDForm.password" show-password />
                    </el-form-item>
                    <el-form-item label="再次输入密码">
                        <el-input v-model="resetPWDForm.retypePassword" show-password />
                    </el-form-item>
                    <el-form-item label="验证码">
                        <el-input v-model="resetPWDForm.code" type="text"
                            style="width: 100%;" 
                            clearable 
                        >
                            <template #prefix>
                                <el-icon style="font-size: 20px;">
                                    <Iphone />
                                </el-icon>
                            </template>0
                            <template #suffix>
                                <el-button link>
                                    <span 
                                        @click="sendCode" 
                                        style="font-weight: 700;"
                                        :style="{ 'color': isSendCode ? '#909399' : '#67c23a' }"
                                    >{{ isSendCode ? codeTime + 's' : '发送验证码'}}</span>
                                </el-button>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <div style="width: 100%; display: flex; justify-content: center;">
                            <el-button @click="dialogResetPWDVisible = false" style="width: 200px; height: 50px;" >
                                取消
                            </el-button>
                            <el-button @click="resetPWD" type="danger" style="width: 200px; height: 50px;" >
                                重置
                            </el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
            <div v-else style="padding: 10%;">
                <el-result
                    icon="success"
                    title="成功"
                    sub-title="密码重置成功，需要重新登录！"
                >
                    <template #extra>
                        <el-button type="primary" @click="dialogResetPWDVisible = false">完成</el-button>
                    </template>
                </el-result>
            </div>
        </el-dialog>
    </div>

    <el-dialog v-model='isShowInvite' title="邀请链接" align-center="true"
        append-to-body style="width: 42vh; height:20vh;">
		    <view style="align-items: center; justify-content: center; flex-direction:column; display:flex;">
                <el-input v-model='inviteText' id='inviteId' readonly='true' style="height: 5vh;width: 100%;"/>
                <el-button type="primary" @click='inviteCopy()' style="height: 5vh;width: 100%;margin-top: 2vh;">复制链接</el-button>
		    </view>
  	</el-dialog>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import {
    Iphone,
    OfficeBuilding,
    User,
    Setting,
    Postcard,
    MilkTea,
    CirclePlus,
} from '@element-plus/icons-vue'
import { ElNotification } from 'element-plus'

import { reverseBusiness as resetAPI, } from '@/api/clinic/home.api.js';
import { deleteUser as deleteUserAPI, update as updateUserAPI, getBindCompany as getBindCompanyAPI, getInvite as getInvAPI, } from '@/api/user.api.js'
import { loginUserStore } from '@/stores/UserStore'
import { sendCode as sendCodeAPI } from '@/api/code.api.js'
import routerInstance from '@/router'
import { systemResetPWD } from '@/api/user.api.js'

const isWorkByUI = ref(false);
const workStr = ref("停止营业");
onMounted(async () => {
    isWorkByUI.value = routerInstance.currentRoute.value.params.isWork;
    if (!isWorkByUI.value)
        workStr.value = "开始营业";

    Object.assign(user, await loginUserStore.getUser())
    getBindCompanyAPI(res => {
        Object.assign(company, res)
    })
})

const user = reactive({})
const company = reactive({})

const isEditUser = ref(false)

const dialogResetPWDVisible = ref(false)
const resetPWDForm = reactive({
    password: null,
    retypePassword: null,
})

function clickEdit() {
    isEditUser.value = !isEditUser.value
    oldPhone.value = user.phone
}

const isShowInvite = ref(false);
const inviteText = ref("");
function invite() {
    if (inviteText.value.length < 1)
        getInvAPI(resp => {
            inviteText.value = window.location.origin + "/inviteCode=" + resp;
            isShowInvite.value = true;
        })
    else
        isShowInvite.value = true;
}

function inviteCopy() {
    const inputElement = document.querySelector('#inviteId');
    inputElement.select();
    document.execCommand('copy');

    ElNotification({
        title: '复制结果',
        message: '邀请链接复制成功',
        type: 'success',
    })
}

const oldPhone = ref(user.phone)

function editUser() {
    if(user.phone == oldPhone.value) user.phone = undefined
    if(user.phone && user.phone != '') user.phone = parseInt(user.phone)
    if(user.code && user.code != '') user.code = parseInt(user.code)
    updateUserAPI(user, res => {
        Object.assign(user, res)
        isEditUser.value = false
    }, error => {
    })
}
function deleteUser() {
    deleteUserAPI(() => {
        routerInstance.push({ path: "/clinic/login" })
    })
}
const size = ref('default')
const iconStyle = computed(() => {
  const marginMap = {
    large: '8px',
    default: '6px',
    small: '4px',
  }
  return {
    marginRight: marginMap[size.value] || marginMap.default,
  }
})
const isSendCode = ref(false)
const codeTime = ref(60)
function sendCode() {
	if(isSendCode.value == false && user.phone != null) {
		isSendCode.value = true
		let time = 59;
		sendCodeAPI({ phone: user.phone }).then(() => {
		})
		let timer = setInterval(function () {
			if (time == 0) {
				// 清除定时器和复原按钮
				clearInterval(timer);
				codeTime.value = 60
				isSendCode.value = false
			} else {
				codeTime.value = time;
				time--;
			}
		}, 1000);
	}
}

const resetIsNotOK = ref(true)
function resetPWD() {
    if(resetPWDForm.password == resetPWDForm.retypePassword) {
        systemResetPWD(resetPWDForm, () => {
            resetIsNotOK.value = false
        })
    } else {
        ElNotification({
            title: '错误',
            message: '两次密码输入不一致',
            type: 'error',
        })
    }
}

/**更新营业状态 */
function resetWork() {
    let flag = -1;
    if (isWorkByUI.value == "true")
        flag = 1;
    else if (isWorkByUI.value == "false")
        flag = 0;

    resetAPI({ workFlag: flag }, (res) => {
        if (res) {
            if (isWorkByUI.value == "true")
                isWorkByUI.value = "false";
            else if (isWorkByUI.value == "false")
                isWorkByUI.value = "true";

            ElNotification({
                title: '营业状态',
                message: '营业状态更新完毕',
                type: 'success',
            })
        }
    })
}
</script>

<style scoped>
.el-descriptions {
margin-top: 20px;
}
.cell-item {
display: flex;
align-items: center;
}
.margin-top {
margin-top: 20px;
}
:deep(.el-card__body) {
    height: 100%;
}
</style>
  
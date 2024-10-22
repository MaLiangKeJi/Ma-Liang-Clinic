<template>
	<img src="@/static/loginBackground.svg" alt="Wave" class="wave-svg"/>
	<div style="height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
		<el-card style="width: 80%; height: 60%; z-index: 1;">
			<div style="height: 100%; display: flex;">
				<div style="width: 50%; height: 100%;">
					<el-image style="height: 100%;" :src="image" fit="contain" />
				</div>
				<div style="width: 40%; height: 100%; padding: 2% 5%">
					<div v-if="step == 0" style="height: 100%; display: flex; flex-direction: column; justify-content: space-around;"
						:class="{ 'test1' : inputPadding, 'test2': !inputPadding }"
					>
						<el-alert v-if="needBindPhone == true" type="error" :closable="false" style="height: 50px;">
							<template #title>
								<div style="display: flex; align-items: center;">
									<el-icon style="font-size: 30px;"><Warning /></el-icon>
									<span style="font-size: 1.2em; margin-left: 10px;">
										需要先绑定您的手机号
									</span>
								</div>
							</template>
						</el-alert>
						<div v-if="form.loginType == 'qrCode' && needBindPhone == false"
							style="height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;"
						>
							<el-card v-loading="qrCodeLoading" class="qrcode_card" style="width: 80%; height: 40% !important;" shadow="always" scrolling="no">
									<div style="height: 100%; display: flex; justify-content: space-around; align-items: center;" :class="!showPrivacyPolicy ? 'image-container':''">
										<img :src="qrCodeSrc" height="100%" width="100%" style="object-fit: contain;"  />
										<div class="mask" v-if="!showPrivacyPolicy">
											<div class="mask-text">请阅读并同意条款</div>
										</div>
									</div>
							</el-card>
							<div style="display: flex; margin-top: 50px;">
								<el-text class="mx-1" type="info">打开微信，点击右上角加号，选择扫一扫</el-text>
								<el-button text @click="reloadQrCode">
									<span style="font-weight: 700;">刷新</span>
								</el-button>
							</div>
							<!-- 免责说明 -->
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="showPrivacyPolicy = !showPrivacyPolicy">
									<el-icon v-if="showPrivacyPolicy" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span style="font-weight: 700;">阅读并接受<el-link href="/clinic/user/userterms" target="_blank" @click="handleLinkClick">用户协议</el-link>和<el-link @click="handleLinkClick" href="/clinic/user/privacy" target="_blank">隐私政策</el-link></span>
								</el-button>
							</div>
						</div>
						<div v-else-if="form.loginType == 'qrCode' && needBindPhone == true"
							style="display: flex; flex-direction: column; justify-content: space-around; align-items: center;"
						>
							<el-form ref="formRef" :model="form" :rules="rules" label-width="0" style="width: 100%;" class="login-from">
								<el-form-item ref="qrCodePhoneItemRef" prop="phone">
									<el-input v-model="form.phone" type="text" placeholder="手机号" 
										style="width: 100%; height: 60px;" 
										clearable 
									>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Iphone />
											</el-icon>
										</template>
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
								<el-form-item prop="code">
									<el-input v-model="form.code" type="password" placeholder="验证码" 
										style="width: 100%; height: 60px;" 
										clearable 
									>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Message />
											</el-icon>
										</template>
									</el-input>
								</el-form-item>
							</el-form>
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="form.noLogin = !form.noLogin">
									<el-icon v-if="form.noLogin" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span :style="{ color: form.noLogin ? '#67c23a' : '#6c6e72' }" style="margin-left: 5px; font-weight: 700;">30 天免登录</span>
								</el-button>
							</div>
						</div>
						<div v-else-if="form.loginType == 'phoneCode'"
							style="display: flex; flex-direction: column; justify-content: space-around; align-items: center;"
						>
							<el-form ref="formRef" :model="form" :rules="rules" label-width="0" style="width: 100%;" class="login-from">
								<el-form-item ref=loginPhoneItemRef prop="phone">
									<el-input v-model="form.phone" type="text" placeholder="手机号" 
										style="width: 100%; height: 60px;" 
										clearable 
									>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Iphone />
											</el-icon>
										</template>
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
								<el-form-item prop="code">
									<el-input v-model="form.code" placeholder="验证码" 
										style="width: 100%; height: 60px;" 
										clearable 
									>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Message />
											</el-icon>
										</template>
									</el-input>
								</el-form-item>
							</el-form>
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="form.noLogin = !form.noLogin">
									<el-icon v-if="form.noLogin" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span :style="{ color: form.noLogin ? '#67c23a' : '#6c6e72' }" style="margin-left: 5px; font-weight: 700;">30 天免登录</span>
								</el-button>
							</div>
							<!-- 免责说明 -->
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="showPrivacyPolicy = !showPrivacyPolicy">
									<el-icon v-if="showPrivacyPolicy" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span style="font-weight: 700;">阅读并接受<el-link href="/clinic/user/userterms" target="_blank" @click="handleLinkClick">用户协议</el-link>和<el-link @click="handleLinkClick" href="/clinic/user/privacy" target="_blank">隐私政策</el-link></span>
								</el-button>
							</div>
						</div>
						<div v-else-if="form.loginType == 'accountPassword'"
							style="display: flex; flex-direction: column; justify-content: space-around; align-items: center;"
						>
							<el-form ref="formRef" :model="form" :rules="rules" label-width="0" style="width: 100%;" class="login-from">
								<el-form-item prop="phone">
									<el-input v-model="form.phone" type="text" placeholder="手机号" 
										style="width: 100%; height: 60px;" 
										clearable 
									>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Iphone />
											</el-icon>
										</template>
									</el-input>
								</el-form-item>
								<el-form-item prop="password">
									<el-input v-model="form.password" type="password" placeholder="密码" 
										style="width: 100%; height: 60px;" 
										clearable show-password
										>
										<template #prefix>
											<el-icon style="font-size: 20px;">
												<Lock />
											</el-icon>
										</template>
									</el-input>
								</el-form-item>
							</el-form>
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="form.noLogin = !noLogin">
									<el-icon v-if="form.noLogin" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span :style="{ color: form.noLogin ? '#67c23a' : '' }" style="margin-left: 5px; font-weight: 700;">30 天免登录</span>
								</el-button>
								<el-button @click="step = 3" link>
									<span style="font-weight: 700;">忘记密码</span>
								</el-button>
							</div>
							<!-- 免责说明 -->
							<div style="width: 100%; display: flex; justify-content: space-between;">
								<el-button link @click="showPrivacyPolicy = !showPrivacyPolicy">
									<el-icon v-if="showPrivacyPolicy" style="font-size: 20px;" color="#67c23a"><CircleCheckFilled /></el-icon>
									<el-icon v-else style="font-size: 20px;"><CircleCheck /></el-icon>
									<span style="font-weight: 700;">阅读并接受<el-link href="/clinic/user/userterms" target="_blank" @click="handleLinkClick">用户协议</el-link>和<el-link @click="handleLinkClick" href="/clinic/user/privacy" target="_blank">隐私政策</el-link></span>
								</el-button>
							</div>
						</div>
						<div v-show="form.loginType == 'qrCode' && needBindPhone == true" style="margin-top: 10%; display: flex; justify-content: center;">
							<el-button 
								@click="Signin" :loading="submitBtnLoading"
								type="primary" style="width: 200px; height: 50px; border-radius: 25px; font-size: 15px;"
							>
								绑定手机号
							</el-button>
						</div>
						<div v-show="form.loginType != 'qrCode'" style="margin-top: 10%; display: flex; justify-content: center;">
							<el-button 
								:disabled="!showPrivacyPolicy"
								@click="Signin" :loading="submitBtnLoading"
								type="primary" style="width: 200px; height: 50px; border-radius: 25px; font-size: 15px;"
							>
								{{ form.loginType == 'phoneCode' ? '登录 / 注册' : '登录' }}
							</el-button>
						</div>
					</div>
					<div v-if="step == 1" style="height: 100%; padding: 8% 2%; display: flex; flex-direction: column; justify-content: space-around;">
						<el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="height: 100%; width: 100%;" class="login-from" label-position="top">
							<div style="height: 100%; display: flex; flex-direction: column; justify-content: space-around; align-items: center;">
								<div style="width: 100%; display: flex; flex-direction: column; align-items: center;">
									<h2>设置基本信息</h2>
									<el-divider />
								</div>
								<el-scrollbar height="80%" style="width: 100%;">
									<el-form-item prop="clinicName" label="诊所名称">
										<el-input v-model="form.clinicName" style="width: 100%; height: 60px; font-size: 1.2em" placeholder="" />
									</el-form-item>
									<el-form-item prop="physician" label="医师姓名" style="margin-top: 40px;">
										<el-input v-model="form.physician" style="width: 100%; height: 60px; font-size: 1.2em" placeholder="" />
									</el-form-item>
								</el-scrollbar>
								<el-button
									@click="createSetting" :loading="submitBtnLoading"
									type="primary" style="width: 200px; height: 10%; border-radius: 25px; font-size: 15px; margin-top: 10%;"
								>
									进入系统
								</el-button>
							</div>
						</el-form>
					</div>
					<div v-if="step == 3" style="height: 100%; padding: 15% 2%; display: flex; flex-direction: column; justify-content: space-around;">
						<div style="margin-bottom: 5%">
							<el-button text bg @click="step = 0">
								<el-icon style="font-size: 20px;"><ArrowLeftBold /></el-icon>
								<span style="font-size: 1.2em;">返回</span>
							</el-button>
						</div>
						<el-form ref="formRef" :model="form" :rules="rules" label-width="0" style="width: 100%;" class="login-from">
							<el-form-item ref="resetPWDPhoneItemRef" prop="phone">
								<el-input v-model="form.phone" type="text" placeholder="手机号" 
									style="width: 100%; height: 60px;" 
									clearable 
								>
									<template #prefix>
										<el-icon style="font-size: 20px;">
											<Iphone />
										</el-icon>
									</template>
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
							<el-form-item prop="code">
								<el-input v-model="form.code" placeholder="验证码" 
									style="width: 100%; height: 60px;" 
									clearable 
								>
									<template #prefix>
										<el-icon style="font-size: 20px;">
											<Message />
										</el-icon>
									</template>
								</el-input>
							</el-form-item>
							<el-form-item prop="password">
								<el-input v-model="form.password" type="password" placeholder="密码" 
									style="width: 100%; height: 60px;" 
									clearable show-password
									>
									<template #prefix>
										<el-icon style="font-size: 20px;">
											<Lock />
										</el-icon>
									</template>
								</el-input>
							</el-form-item>
							<el-form-item prop="reinputPassword">
								<el-input v-model="form.reinputPassword" type="password" placeholder="再次输入密码" 
									style="width: 100%; height: 60px;" 
									clearable show-password
									>
									<template #prefix>
										<el-icon style="font-size: 20px;">
											<Lock />
										</el-icon>
									</template>
								</el-input>
							</el-form-item>
						</el-form>
						<div style="margin-top: 10%; display: flex; justify-content: center;">
							<el-button 
								@click="resetPWD" :loading="submitBtnLoading"
								type="primary" style="width: 200px; height: 50px; border-radius: 25px; font-size: 15px;"
							>
								重设密码
							</el-button>
						</div>
					</div>
				</div>
				<div style="width: 10%; display: flex; flex-direction: column;">
					<el-button link
						@click="() => { switchLoginType('qrCode') }"
						:style="{ 
							'border-right-color': form.loginType == 'qrCode' ? '#605bff' : '',
						}" 
						style="width: 100%; height: 33%; border-right-width: 10px; border-right-style: solid; border-radius: 0;"
					>
						<div style="display: flex; flex-direction: column; align-items: center;">
							<el-icon style="font-size: 40px;"><WXLogo /></el-icon>
							<span style="margin-top: 20px; font-size: 100%; font-weight: 700;">微信扫码</span>
						</div>
					</el-button>
					<el-button link
						@click="() => switchLoginType('phoneCode')"
						:style="{ 
							'border-right-color': form.loginType == 'phoneCode' ? '#605bff' : '',
							'background-color': form.loginType == 'phoneCode' ? '#fff' : '' ,
						}" 
						style="width: 100%; height: 33%; border-right-width: 10px; border-right-style: solid; border-radius: 0;"
					>
						<div style="display: flex; flex-direction: column; align-items: center;">
							<el-icon style="font-size: 40px;"><Iphone /></el-icon>
							<span style="margin-top: 20px;font-size: 100%; font-weight: 700;">短信验证码</span>
						</div>
					</el-button>
					<el-button link
						@click="() => switchLoginType('accountPassword')"
						:style="{ 
							'border-right-color': form.loginType == 'accountPassword' ? '#605bff' : '',
							'background-color': form.loginType == 'accountPassword' ? '#fff' : '' ,
						}" 
						style="width: 100%; height: 33%; border-right-width: 10px; border-right-style: solid; border-radius: 0;"
					>
						<div style="display: flex; flex-direction: column; align-items: center;">
							<el-icon style="font-size: 40px;"><Lock /></el-icon>
							<span style="margin-top: 20px; font-size: 100%; font-weight: 700;">账号密码</span>
						</div>
					</el-button>
				</div>
			</div>
		</el-card>
	</div>

	<el-dialog v-model='isShowInvite' title="邀请二维码" :before-close='closeInvite' align-center="true"
    append-to-body style="width: 28vh; height:37vh;">
		<view style="align-items: center; justify-content: center; flex-direction:column; display:flex;">
			<VueQrcode :value='inviteUrl' width="260"/>
			<el-button @click='inviteBack()' type="primary" style="width: 200px; height: 50px; font-size: 15px;">
				返回
			</el-button>
		</view>
  	</el-dialog>
</template>
 
<script setup>
import { ref, reactive, defineEmits, onMounted, watch } from 'vue'
import { Iphone, Lock, CircleCheckFilled, CircleCheck, Message, Refresh } from '@element-plus/icons-vue'

import VueQrcode from 'vue-qrcode';

import { 
	login, 
	resetPWD as resetPWDAPI, 
	register as registerUserAPI, 
	checkUserIsRegister as checkUserIsRegisterAPI,
} from '@/api/user.api.js'
import { sendCode as sendCodeAPI } from '@/api/code.api.js'
import { setToken, loginUserStore, isNotLogin } from '@/stores/UserStore'
import { ElMessage, ElMessageBox, ElNotification, } from 'element-plus'
import { create as createCompanyAPI, search as searchCompanyAPI } from '@/api/company.api.js'
import WXLogo from '@/views/wxLogo.vue'
import {
	Back,
} from '@element-plus/icons-vue'
import { settingService } from '@/api/clinic/index.js'
import routerInstance from '@/router'
import image from '@/static/blood-donation-vector-illustration.png'
import { getQRCode, checkQRCode } from '@/api/qrCode.js'
import { systemRouterStore } from '@/stores/app.js'
import { invite } from '@/stores/clinic/invite.js'
import { searchProvince } from '@/api/clinic/province.api.js'

const isShowInvite = ref(false);
const inviteUrl = ref()
onMounted(async () => {
	if(isNotLogin()) {	//登录未完成情况下，再进行轮询（排除登录后，Vue router 重刷页面导致的再轮询）
		vxLogin()
	} else {
		let searchSettingResult = await searchSetting()
		if(searchSettingResult) {
			toStream()
		}
	}
	let query = routerInstance.currentRoute.value.query
	if(query && query.code) {
		form.inviteCode = query.code
	}
})

const closeInvite = (done) => {
	ElNotification({ title: '关闭失败', message: '请点击返回', type: 'warning' })
}

function inviteBack() {
	routerInstance.go(-1);
}

const isResetPWD = ref(false)
const isRegister = ref(false)
//免责说明
const showPrivacyPolicy = ref(false)

const event = defineEmits(['login'])
const step = ref(0)
const formRef = ref()
const form = reactive({
	noLogin: false,
    phone: '',
    password: '',
	reinputPassword: '',
	code: '',
    loginType: 'qrCode', //默认密码登录,
	searchCompany: true,
	checkCompanyStructure: false,
})

const qrCodeSrc = ref()
const qrCodeLoading = ref(true)
const needBindPhone = ref(false)

const inputPadding = ref(false)
watch(
	() => form.loginType,
	value => {
		inputPadding.value = (value == 'qrCode' && needBindPhone.value == false)
	},
	{ immediate: true }
)
function switchLoginType(code) {
	form.code = null
	form.password = null
	form.loginType = code
}
function handleLinkClick(event) {
  // 阻止事件冒泡
  event.stopPropagation();
}
const settingData = ref()
async function searchSetting() {
	let res = await settingService.asyncSearch()
	if(res.data.data != null) {
		settingData.value = res.data.data
		let id = settingData.value.id
		if(id && id != undefined && id != null && id > 0) {
			return true
		}
	}
	submitBtnLoading.value = false
	step.value = 1
	return false
}
let timerList = [];
async function vxLogin() {
	let ticket = await (await getQRCode()).data.data.ticket
	//固定链接+拼接ticket,获取二维码图片
	qrCodeSrc.value = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}`;
	qrCodeLoading.value = false
	if (qrCodeSrc.value) {
		//设置定时器轮询check()轮询登录接口
		timerList.push(window.setInterval(async () => {
			if(form.loginType != 'qrCode') { //如果切换登录类型，则取消扫码轮询
				clearTimer()
				return
			}
			let res = await checkQRCode({ ticket, timestamp: new Date().getTime() })
			let response = res.data.data
			//条件判断，!= -1 表示关注成功
			if (response.scanResult != -1) {
				//停止轮询
				clearTimer()
				if (response.scanResult == 0) {
					// 需要绑定手机号
					form.openId = response.openId
					needBindPhone.value = true
				} else {
					clearTimer()
					setToken(response.token)
					loginUserStore.setUser(response.user)
					if(await searchSetting()) {
						toStream()
					}
				}
			} else if (response.scanResult == -2) {
				//== -2 二维码过期
				clearTimer()
			} else if (response.scanResult == -1) {
				//== -2 二维码过期
				let path = window.location.pathname
				if(path != '/clinic/login') {
					clearTimer()
				}
			} 
		}, 3000));
	}
}

async function clearTimer() {
	if (timerList && timerList.length > 0) {
		timerList.forEach((item, index) => {
			window.clearInterval(item);
		})
		timerList = [];
	}
}

async function reloadQrCode() {
	let ticket = await (await getQRCode()).data.data.ticket
	//固定链接+拼接ticket,获取二维码图片
	qrCodeSrc.value = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}`;
}
function Signin() {
    if(submitBtnLoading.value == false) {
        switchLoading()
		form.expireNumber = 30
        setTimeout(() => login(
			{ ...form, timestamp: Date.now() }, 
			async res => {
				loginUserStore.setUser(res)
				setToken(res.token)
				if(await searchSetting()) {
					toStream()
				}
			},
			(res) => {
				if(res.code == 401) {
					isRegister.value = true
				}
				submitBtnLoading.value = false
		}), 200)
    }
}
async function createSetting(){
	if (!formRef.value) return
	await formRef.value.validate((valid, fields) => {
		if (valid) {
			settingService.addByAxios({
				clinicName: form.clinicName,//诊所名称
				alternateName: form.physician,	//医师姓名
				physician: form.physician,	//医师姓名
				division:form.division,	//科别：内科，中西医结合，中医
				provinceId: form.province && form.province.length ? form.province[form.province.length -1] : undefined,	//诊所地址
				addr:form.addr,	//详细地址
				inviteCode:invite.getInvite(),
			}).then(({data})=>{
				let respCode = data.code;
				switch(respCode){
					case 200:
						ElNotification({ title: '配置成功', message: '即将进入系统', type: 'success', })
						invite.delInvite();
					routerInstance.push({ path: '/clinic/home' }) // 跳转到【首页】
						break;
					case 500:
						ElNotification({ title: '配置失败', message: data.msg, type: 'warning' })
						break;
				}
			});
		} else {
			ElNotification({ title: '警告', message: '您需要填写基本信息', type: 'warning', })
		}
	})
}

const isSendCode = ref(false)
const codeTime = ref(60)
const qrCodePhoneItemRef = ref()
const loginPhoneItemRef = ref()
const resetPWDPhoneItemRef = ref()
function sendCode() {
	if(step.value == 0) {
		if(form.loginType == 'qrCode') {
			// 二维码扫码后，需要填写手机号时的验证
			qrCodePhoneItemRef.value.validate()
		} else if(form.loginType == 'phoneCode') {
			// 手机号登录时的验证
			loginPhoneItemRef.value.validate()
		}
	} else if(step.value == 3) {
		// 重设密码时的验证
		resetPWDPhoneItemRef.value.validate()
	}
	if(isSendCode.value == false) {
		isSendCode.value = true
		let time = 59;
		sendCodeAPI({ phone: form.phone, timestamp: Date.now() }).then(() => {
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
		})
	}
}
function toStream() {
	routerInstance.push({ path: '/clinic/home' })
}
let submitBtnLoading = ref(false)
function switchLoading() {
    submitBtnLoading.value = !submitBtnLoading.value
}

const rules = reactive({
	phone: [
		{ required: true, message: '必须输入手机号', trigger: 'blur' },
		{ min: 11, max: 11, message: '手机号格式有误', trigger: 'blur' },
	],
	code: [
		{ required: true, message: '必须输入验证码', trigger: 'blur' },
		{ min: 4, max: 4, message: '验证码是 4 位数字，请检查格式', trigger: 'blur' },
	],
	password: [
		{ required: true, message: '必须输入密码', trigger: 'blur' },
		{ pattern: /^[A-Za-z0-9]{8,16}$/, message: '密码格式 8 ~ 16 位，请检查格式', trigger: 'blur' },
	],
	reinputPassword: [
		{ validator: validateReinputPassword, trigger: 'blur' }
	],
	clinicName: [
		{ required: true, message: '必须输入诊所名称', trigger: 'blur' },
	],
	physician: [
		{ required: true, message: '必须输入医师姓名', trigger: 'blur' },
	],
})

function validateReinputPassword (rule, value, callback) {
	if (value === '') {
		callback(new Error('再次输入密码'))
	} else if (!(/^[A-Za-z0-9]{8,16}$/.test(value))) {
		callback(new Error('密码格式 8 ~ 16 位，请检查格式'))
	} else if (value !== form.password) {
		callback(new Error("两次输入的密码不一致"))
	} else {
		callback()
	}
}

async function resetPWD() {
	if (!formRef.value) return
  	await formRef.value.validate((valid) => {
		if (valid) {
			resetPWDAPI(form, () => {
				ElMessageBox.alert('密码重置成功！', '成功', {
					confirmButtonText: '前往登录',
					callback: () => {
						step.value = 0
						isResetPWD.value = false
					},
				})
			})
		} else {
			ElMessage({
				message: '请检查信息，是否按要求填写',
				type: '警告',
			})
		}
	})
}

const provinceCascaderProps = {
	lazy: true,
	lazyLoad(node, resolve) {
		searchProvince({ id: node.value ? node.value : undefined }, res => {
			resolve(res)
		})
	},
}
</script>
	
<style scoped>
.test1 {
	padding: 0 10%;
}
.test2 {
	padding: 40% 2%;
}
.center {
	width: 100vw;
	height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
	background: #FC466B;  /* fallback for old browsers */
	background: -webkit-linear-gradient(to right, #3F5EFB, #FC466B);  /* Chrome 10-25, Safari 5.1-6 */
	background: linear-gradient(to right, #3F5EFB, #FC466B); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

}

:deep(h1) {
	font-size: 30px;
	color: black;
}

.logon {
	background-color: #fff;
	border-radius: 10px;
	box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
	/* position: relative;
	overflow: hidden; */
	width: 768px;
	max-width: 100%;
	min-height: 480px;
	margin-top: 20px;
	display: flex;
	background: -webkit-linear-gradient(right, #4284db, #29eac4);
}

.overlaylong {
	border-radius: 10px 0 0 10px;
	width: 50%;
	height: 100%;
	background-color: #fff;
	display: flex;
	align-items: center;
	justify-content: center;
}
.overlaytitle {
	border-radius: 0px 10px 10px 0px;
	width: 50%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0);
	display: flex;
	align-items: center;
	justify-content: center;
}

.overlaytitle-Signin {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

.overlaylong-Signin {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

:deep(.overlaylong-Signup) {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

:deep(.overlaylong-Signin input) {
	background-color: #eee;
	border: none;
	padding: 12px 15px;
	margin: 10px 0;
	width: 240px;
}
:deep(h3) {
	font-size: 10px;
	margin-top: 10px;
	cursor: pointer;
}
.inupbutton {
	background-color: #408ed9;
	border: none;
	width: 180px;
	height: 40px;
	border-radius: 50px;
	font-size: 14px;
	color: #fff;	
	text-align: center;
	line-height: 40px;
	margin-top: 30px;
}
:deep(.el-input-group__append) {
	background-color: #67c23a;
	color: #fff;
	cursor: pointer;
}
:deep(.el-card__body) {
	padding: 0;
	width: 100%;
	height: 100%;
}
.wave-svg {
	position: fixed; 
	bottom: 0;
}
:deep(.login_type_button_group .el-button) {
	padding: 0;
}
:deep(.el-button + .el-button) {
	margin: 0;
}
:deep(.el-input__wrapper) {
	height: 100%;
}
:deep(.el-input) {
	height: 100%;
}
.image-container {
  position: relative;
  display: inline-block;
}

.mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgb(12, 12, 12); /* 半透明黑色遮罩 */
  z-index: 1; /* 确保遮罩层在图像之上 */
  display: flex;
  justify-content: center;
  align-items: center;
}
.mask-text {
  color: white;
  font-size: 10vm;
  text-align: center;
}
</style>
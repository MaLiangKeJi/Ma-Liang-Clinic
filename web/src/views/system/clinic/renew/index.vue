<template>
    <el-card style="height: 100%; padding: 2%">
        <div v-if="step < 2" style="height: 100%; padding: 5%; display: flex; justify-content: center;">
            <div style="width: 50%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                <img :src="WorkPNG" alt="码良云" style="width: 80%; object-fit: contain;" />
            </div>
            <div v-if="step == 0" style="width: 50%; height: 100%; padding: 5%; display: flex; flex-direction: column; justify-content: space-around; align-items: center;">
                <div 
                    class="border-box border-bg" :style="{ 'background': payment == 3 ? 'linear-gradient(to right, #8f41e9, #578aef)' : '', 'border': payment != 3 ? '3px solid #dfe1e5' : '3px solid #fff' }"
                    @click="switchYearPayment"
                >
                    <div class="content" style="display: flex;">
                        <div style="width: 60%; display: flex; flex-direction: column; justify-content: center; align-items: flex-start;">
                            <div>
                                <span style="font-size: 2em; font-weight: 700; background-image: linear-gradient(to bottom,#8f41e9, #578aef); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                                    ￥365
                                </span>
                                <span style="font-size: 1em; font-weight: 700; background-image: linear-gradient(to bottom,#8f41e9, #578aef); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">/年</span>
                            </div>
                            <el-text class="mx-1" type="info" style="font-size: 1.2em; width: 100%;">每月 22.15</el-text>
                        </div>
                        <div style="width: 40%; display: flex; flex-direction: column; justify-content: center; text-align: right;">
                            <span>按年支付</span>
                        </div>
                    </div>
                </div>
                <div 
                    class="border-box border-bg" :style="{ 'border': payment == 2 ? '3px solid #aeb5bd' : '3px solid #dfe1e5' }"
                    @click="switchQuarterPayment"
                >
                    <div class="content" style="display: flex;">
                        <div style="width: 60%; display: flex; flex-direction: column; justify-content: center; align-items: flex-start;">
                            <div style="color: #000;">
                                <span style="font-size: 2em; font-weight: 700;">
                                    ￥110
                                </span>
                                <span style="font-size: 1em; font-weight: 700;">/季</span>
                            </div>
                            <el-text class="mx-1" type="info" style="font-size: 1.2em; width: 100%;">可随时取消</el-text>
                        </div>
                        <div style="width: 40%; display: flex; flex-direction: column; justify-content: center; text-align: right;">
                            <span>连续包季</span>
                        </div>
                    </div>
                </div>
                <div 
                    class="border-box border-bg" :style="{ 'border': payment == 1 ? '3px solid #aeb5bd' : '3px solid #dfe1e5' }"
                    @click="switchMonthPayment"
                >
                    <div class="content" style="display: flex;">
                        <div style="width: 60%; display: flex; flex-direction: column; justify-content: center; align-items: flex-start;">
                            <div style="color: #000;">
                                <span style="font-size: 2em; font-weight: 700;">
                                    ￥40
                                </span>
                                <span style="font-size: 1em; font-weight: 700;">/月</span>
                            </div>
                            <el-text class="mx-1" type="info" style="font-size: 1.2em; width: 100%;">可随时取消</el-text>
                        </div>
                        <div style="width: 40%; display: flex; flex-direction: column; justify-content: center; text-align: right;">
                            <span>连续包月</span>
                        </div>
                    </div>
                </div>
                <div 
                    style="width: 100%; height: 20%; border-radius: 15px; background: linear-gradient(to right, #8f41e9, #578aef); display: flex; justify-content: center; align-items: center;"
                    @click="switchQRCode"
                >
                    <span style="font-size: 1.5em; color: #fff;">立刻升级</span>
                </div>
            </div>
            <div v-else-if="step == 1" style="width: 50%; height: 100%; padding: 5%; display: flex; flex-direction: column; justify-content: space-around; align-items: center;">
                <el-card v-loading="qrCodeLoading" class="qrcode_card" style="width: 60%; height: 60% !important;" shadow="always" scrolling="no">
                    <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                        <img :src="qrCodeSrc" height="100%" width="100%" style="object-fit: contain;" />
                    </div>
                </el-card>
                <el-text class="mx-1" type="info" style="margin-top: 50px;">打开微信，点击右上角加号，扫一扫</el-text>
                <el-text class="mx-1" type="info" style="margin-top: 10px;">（支付异常，联系 <span style="user-select:text">15656561521</span> 或 <span style="user-select:text">17634860778</span>）</el-text>
            </div>
        </div>
        <div v-if="step == 2" style="height: 80%; padding: 5%; display: flex; justify-content: center;">
            <el-result
                icon="success"
                title="付款成功"
                sub-title=""
            >
                <template #extra>
                    <el-button type="primary" @click="backHome">开始使用</el-button>
                </template>
            </el-result>
        </div>
    </el-card>
</template>
<script setup>
import { ref } from 'vue'
import WorkPNG from '@/static/work.png'
import { getPayQRCode as getPayQRCodeAPI, checkPayQRCode as checkPayQRCodeAPI } from '@/api/qrCode.js'
import QRCode from 'qrcode'
import { useRouter } from 'vue-router'

const routerInstance = useRouter()

const payment = ref(3)
function switchYearPayment() { payment.value = 3 }
function switchQuarterPayment() { payment.value = 2 }

function switchMonthPayment() { payment.value = 1 }

const step = ref(0)
const qrCodeSrc = ref(null)
const qrCodeLoading = ref(false)

function switchQRCode() {
    step.value = 1
    getPayQRCode()
}

async function getPayQRCode() {
    qrCodeLoading.value = true
    let response = await getPayQRCodeAPI(payment.value)
	let { codeUrl, orderId } = response.data.data
    let img = QRCode.toDataURL(codeUrl)
    img.then(t => {
        qrCodeSrc.value = t
    })
	//固定链接+拼接ticket,获取二维码图片
	qrCodeLoading.value = false
	//设置定时器轮询check()轮询登录接口
    let loginTimer = setInterval(async () => {
        let res = await checkPayQRCodeAPI(orderId)
        if(res.status == 200) {
            let data = res.data
            if(data && data.code == 200) {
                let { tradeState } = data.data
                if(tradeState == 'SUCCESS') {
                    step.value = 2
                    clearInterval(loginTimer);
                }
            }
        }
    }, 3000);
}
function backHome() {
    routerInstance.push({
        path: "/clinic/home"
    })
}
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
}
.border-box {
    width: 100%;
    height: 30%;
    margin: 25px 0;
}

.border-bg {
    padding: 3px;
    border-radius: 15px;
}

.content {
    height: 100%;
    background: #fff;
    border-radius: 15px; /*trciky part*/
    padding: 5%;
}
</style>
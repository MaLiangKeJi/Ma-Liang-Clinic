<template>
    <el-card style="height: 100%; margin-bottom: 2%; width: 100%; border-radius: 15px;">
        <template #header>
            <div class="card-header" style="height: 100%; padding: 1% 1.5%; display: flex;">
                <span style="font-size: 1.2em; font-weight: 700;">收费</span>
            </div>
        </template>
        <div style="height: 100%; padding: 2% 2% 0 2%; display: flex; flex-direction: column; align-items: center;"
            v-if="!openQrCode">
            <!-- 收费表单 -->
            <el-form :model="form" label-width="120px" style="height: 60%; width: 50%;" v-if="props.admission.isPharmacyPay==0">
                <el-row :gutter="24">
                    <el-col :span="8">
                        <el-form-item label="是否已经支付" style="min-width: 300px;">
                            <el-text v-if="state === 1" class="mx-1" style="font-size: 1vw;"
                                type="success">已支付</el-text>
                            <el-text v-else class="mx-1" type="danger">未支付</el-text>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="24">
                    <el-col :span="6">
                        <el-form-item label="总计" style="min-width: 300px;">
                            {{ totalPrice }} 元
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item label="收费方式" style="min-width: 500px;">
                            <el-radio-group v-model="payment" size="large">
                                <el-radio-button :value="1">微信</el-radio-button>
                                <el-radio-button :value="2">支付宝</el-radio-button>
                                <el-radio-button :value="3">挂账</el-radio-button>
                                <el-radio-button :value="4">现金</el-radio-button>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item label="备注">
                            <el-input v-model="remark" maxlength="30" show-word-limit type="textarea" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 按钮组 -->
            <div style="text-align: center; height: 20%; display: flex; align-items: center;">
                <el-button @click="event('back')">返回</el-button>
                <el-button v-if="props.admission.isPharmacyPay==0" @click="handle.saveClick()" type="primary">
                    <span v-if="state == 0">收费</span>
                    <span v-else>保存</span>
                </el-button>
                <el-button @click="showPrintDialog()" type="success" plain>打印处方</el-button>
                <el-button v-if="state == 1 && (!props.admission.patient.openId)" type="success" @click="vxScan()">
                    客户绑定微信
                </el-button>
            </div>
        </div>
        <!-- 显示二维码 -->
        <div else
            style="height: 100%; padding: 2% 2% 0 2%; display: flex; flex-direction: column; align-items: center;">
            <img :src="qrCodeSrc" style="object-fit: contain;" />
            <el-button type="info" @click="openQrCode = false">返回</el-button>
        </div>
    </el-card>
    <!-- 引入打印弹窗组件 -->
    <PrintDialog ref="printDialog" />
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, onMounted, defineExpose, watch } from 'vue'
import { ElNotification } from 'element-plus'

import { payService } from '@/api/clinic/index.js'
import { getQRCode, checkPatient } from '@/api/qrCode.js'
// 引入打印弹窗组件
import PrintDialog from '@/views/system/clinic/cure/dialog/PrintDialog.vue'

const props = defineProps(['admission', 'totalPrice'])

const event = defineEmits(['submit', 'back'])

const form = reactive({})

const tableData = ref([])
const state = ref(0)
const totalPrice = ref(props.totalPrice || 0)
const payment = ref(1)
const remark = ref("")
const price = ref(0)
const surplu = ref(0)

// 监听总价变化
watch(
    () => props.totalPrice,
    () => {
        totalPrice.value = props.totalPrice
    }
)

const openQrCode = ref(false)
const openId = ref()

const qrCodeSrc = ref()
const qrCodeLoading = ref(true)
const printDialog = ref(null)

// 生成微信扫描二维码
async function vxScan() {
    openQrCode.value = true
    let phone = props.admission.patient.phone
    let ticket = await (await getQRCode({ phone })).data.data.ticket
    // 固定链接 + 拼接 ticket，获取二维码图片
    qrCodeSrc.value = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}`;
    qrCodeLoading.value = false
    let admissionId = props.admission.id
    if (qrCodeSrc.value && phone) {
        // 设置定时器轮询检查登录状态
        let loginTimer = setInterval(async () => {
            let res = await checkPatient({ ticket, phone: phone, isEnd: true })
            let response = res.data.data
            // 条件判断，!= -1 表示关注成功
            if (response.scanResult != -1) {
                // 停止轮询
                clearInterval(loginTimer);
                openId.value = response.openId
            } else if (response.scanResult == -2) {
                // == -2 二维码过期
                clearInterval(loginTimer);
            }
        }, 3000);
    }
}

// 处理按钮点击事件
const handle = {
    saveClick() {
        ElNotification({ title: '通知', message: '数据提交中...', type: 'info' })
        payService.addRecord({
            admissionId: props.admission.id,
            id: getPayId(),
            fee: totalPrice.value,
            way: payment.value,
            remark: remark.value,
        }, (response) => {
            searchRecord(props.admission.payId)
            ElNotification({ title: '成功', message: '信息提交成功!', type: 'success' })
            event('submit')
        })
    },
    addDrugClick(row) {
        row.add = true
        tableData.value.push({})
    },
}

// 价格变化处理
function priceChange() {
    let totalFee = 0
    tableData.value.forEach(element => {
        if (element.fee) {
            totalFee += parseInt(element.fee)
        }
    });
    totalPrice.value = totalFee
}

// 显示打印弹窗
function showPrintDialog() {
    let admissionId = props.admission.id
    // 调用 showDialog 方法
    printDialog.value.showDialog(admissionId)
}

function getPayId() {
    return props.admission.payId
}

function searchRecord(payId) {
    payService.searchPayRecord({ id: payId }, (response) => {
        tableData.value = []
        response.payRecordDtos.forEach(payRecord => {
            tableData.value.push({
                add: true,
                ...payRecord,
            })
            tableData.value.push({
                add: true,
                fee: 0,
            })
            tableData.value.push({
                add: true,
                fee: 0,
            })
            tableData.value.push({})
        })
        state.value = response.state
        payment.value = response.way
        remark.value = response.remark
        totalPrice.value = response.fee
    })
}

// 组件加载时初始化数据
onMounted(() => {
    if (props.admission.payId) searchRecord(props.admission.payId)
})

defineExpose({
    reload: searchRecord,
})
</script>

<style></style>
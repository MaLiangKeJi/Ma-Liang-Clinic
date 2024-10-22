<template>
    <view>
        <el-text v-if='drugArr.length > 0' class='topTip'>最新处方时间: {{
            lastTimeStr }}</el-text>
        <el-text v-else class='topTip'>{{ failTip }}</el-text>
    </view>

    <el-scrollbar v-loading='loadByDrug' max-height="100%" always="true" class='drugFrame'>
        <div v-for='drug in drugArr' class='theDrug'>
            <el-card @click='openDetailDrug(drug)'
                :class='{ noPay: drug.payStatus === 0, payStyle: drug.payStatus === 1 }'>

                <!-- 卡片内第一行 -->
                <el-row :gutter="24">
                    <el-col :span="11">
                        <el-form-item label="姓名">
                            <el-text class='itemByDrug'>{{ drug.name }}</el-text>
                        </el-form-item>
                    </el-col>

                    <el-col :span="13">
                        <el-form-item label="性别">
                            <el-text v-if='drug.sexStatus === 1' class='itemByDrug'>男</el-text>
                            <el-text v-else-if='drug.sexStatus === 0' class='itemByDrug'>女</el-text>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 卡片内第二行 -->
                <el-row :gutter="24">
                    <el-col :span="11">
                        <el-form-item label="时间">
                            <el-text class='itemByDrug'>{{ formatDateTime(new Date(drug.time), 'HH:mm:ss')
                                }}</el-text>
                        </el-form-item>
                    </el-col>

                    <el-col :span="13">
                        <el-form-item label="手机号">
                            <el-text class='itemByDrug'>{{ drug.phone }}</el-text>
                        </el-form-item>
                    </el-col>
                </el-row>

                <!-- 卡片内第三行 -->
                <el-row :gutter="24">
                    <el-col :span="11">
                        <el-form-item label="费用">
                            <el-text class='itemByDrug'>{{ drug.fee }} 元</el-text>
                        </el-form-item>
                    </el-col>

                    <el-col :span="13">
                        <el-form-item label="收费状态">
                            <el-text v-if='drug.payStatus === 0' type='danger'>未收费</el-text>
                            <el-text v-else-if='drug.payStatus === 1' type='info'>已收费</el-text>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-card>
        </div>
    </el-scrollbar>

    <el-drawer direction="btt" v-model='isShowDetail' :title='drugTitle' align-center="true" append-to-body size='90%'>
        <OpenDrugMoreView :key='showDetailF5' :inDrugs='drugDetailArr' :inPayId='payIdByDetail'
            :inAdmissId='admissIdByDetail' @backDrugList='outHideDetail' @updDrugByPay='outUpdDrugByDetail' />
    </el-drawer>
</template>

<script setup>
import OpenDrugMoreView from './openDrugDetail.vue';

import { ref, onMounted, } from 'vue';
import { useRouter } from 'vue-router';

import webSocketUtils from '@/utils/webSocketUtils.js';
import { formatDateTime, } from '@/utils/dateUtils.js';

import drugAPI from '@/api/clinic/drug.api.js';



const lastTimeStr = ref('');
const failTip = ref('');
const drugArr = ref([]);
const routerInstance = useRouter();
const loadByDrug = ref(false);
onMounted(() => {
    loadByDrug.value = true;

    let uid = routerInstance.currentRoute.value.query.clinicBindUid;//诊所绑定医生id
    drugAPI.searchOpenDrug({ uid: uid }, resp => {

        //无处方
        if (resp.length < 1) {
            drugArr.value.length = 0;
            failTip.value = "暂无处方";
            loadByDrug.value = false;
            return;
        }
        failTip.value = "";

        //处方列表赋值并排序
        resp.forEach(d => {
            drugArr.value.push(d);
        })
        drugArr.value.sort((l, r) => r.time - l.time);

        //最新时间
        let lastDate = new Date(drugArr.value[0].time);
        lastTimeStr.value = formatDateTime(lastDate, 'hh:mm:ss a');

        loadByDrug.value = false;
    });

    webSocketUtils.Init(8081, window.location.hostname, "/clinic/openDrug", uid)
    webSocketUtils.setMessageCallback(getMessageCallback)
})

// let aaa=null;

const getMessageCallback = (message) => {
    let obj = JSON.parse(message, function (key, value) {
        if (key == "time") {//更新最新处方时间
            let lastDate = new Date(value);
            lastTimeStr.value = formatDateTime(lastDate, 'hh:mm:ss a');
        }
        return value;
    });

    //更新已有处方
    let tmpArr = drugArr.value.filter(d => d.admissId !== obj.admissId);
    if (drugArr.value.length != tmpArr.length) {
        drugArr.value = tmpArr;

        if (nowAdmissId.value > 0) {

        }
        // let a = ElNotification({
        // title: '当前处方已更新',
        // message: '点击我关闭',
        // duration: 0,
        // onClick: function () {
        //     bbb();
        // }
        //        aaa=a;
        // })
    }

    drugArr.value.unshift(obj);
}



const isShowDetail = ref(false);
const nowAdmissId = ref(0);

const admissIdByDetail = ref(0);
const payIdByDetail = ref(0);
const drugDetailArr = ref([]);
const drugTitle = ref('');
const showDetailF5 = ref(false);
function openDetailDrug(drug) {
    showDetailF5.value = !showDetailF5.value;

    drugTitle.value = drug.name;
    drugDetailArr.value = drug.drugs;
    payIdByDetail.value = drug.payId;
    admissIdByDetail.value = drug.admissId;

    nowAdmissId.value = drug.admissId;
    isShowDetail.value = true;
}

function bbb() {
    aaa.close();
}



function outHideDetail() {
    isShowDetail.value = false;
}

function outUpdDrugByDetail(admissId) {
    drugArr.value.forEach(d => {
        if (d.admissId === admissId) {
            d.payStatus = 1;
            outHideDetail();
        }
    })
}
</script>

<style scoped>
.topTip {
    font-size: 2vh;

    height: 3vh;
    margin-top: 1vh;

    display: flex;
    align-items: center;
    justify-content: center;

    text-align: center;
}

.drugFrame {
    height: 93%;
    margin-top: 3vh;
}

.theDrug {
    margin-bottom: 2vh;
}

.noPay {
    border-color: #F56C6C;
}

.payStyle {
    border-color: #b1b3b8;
}

.itemByDrug {
    font-size: 2vh;
}
</style>
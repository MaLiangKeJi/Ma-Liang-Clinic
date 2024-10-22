<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">设置</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <div style="height: 90%">
                <el-scrollbar height="100%">
                    <el-form ref="formRef" :model="setting" style="padding: 2%">
                        <div style="margin-bottom: 50px;">
                                <h2>诊所</h2>
                                <el-divider />
                                <el-row :gutter="24">
                                    <el-col :span="8">
                                        <el-form-item prop="clinicName" label="诊所名称">
                                            <el-input v-model="setting.clinicName" />
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="4">
                                        <el-form-item prop="division" label="科别">
                                            <el-select v-model="setting.division" placeholder="科别">
                                                <el-option label="内科" value="内科" />
                                                <el-option label="中西医结合" value="中西医结合" />
                                                <el-option label="中医" value="中医" />
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="4">
                                        <el-form-item prop="physician" label="医师姓名">
                                            <el-input v-model="setting.physician" style="width: 300px" placeholder="请输入医师姓名,不填会使用用户名" />
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="4">
                                        <el-form-item prop="addr" label="诊所地址">
                                            <el-input v-model="setting.addr" style="width: 300px" placeholder="请输入诊所详细地址" />
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                        </div>
                        <div style="margin-bottom: 50px;">
                            <h2>营业时间</h2>
                            <el-divider />
                            <div style="width: 100%;">
                                <el-form-item label="营业天数">
                                    <el-checkbox-group v-model='setting.businessDayList'>
                                        <el-checkbox label="周一" value="1" />
                                        <el-checkbox label="周二" value="2" />
                                        <el-checkbox label="周三" value="3" />
                                        <el-checkbox label="周四" value="4" />
                                        <el-checkbox label="周五" value="5" />
                                        <el-checkbox label="周六" value="6" />
                                        <el-checkbox label="周日" value="7" />
                                    </el-checkbox-group>
                                </el-form-item>
                                
                                <view style="display: flex;">
                                    <el-form-item label="上午营业时间" style='margin-right: 3vh;'>
                                        <el-time-picker
                                            v-model='upBusinessTime'
                                            is-range
                                            range-separator="到"
                                            start-placeholder=""
                                            end-placeholder=""/>
                                    </el-form-item>
            
                                    <el-form-item label="下午营业时间">
                                        <el-time-picker
                                            v-model='downBusinessTime'
                                            is-range
                                            range-separator="到"
                                            start-placeholder=""
                                            end-placeholder=""/>
                                    </el-form-item>
                                </view>       
                            </div>
                    </div>
                    <div style="margin-bottom: 50px;">
                        <h2>是否由药房收费</h2>
                        <el-form-item prop="isPharmacyPay">
                            <el-radio-group v-model="setting.isPharmacyPay">
                                <el-radio value="0">否</el-radio>
                                <el-radio value="1">是</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </div>
                        <div style="margin-bottom: 50px; ">
                                <h2>提醒：库存不足</h2>
                                <el-divider />
                                <div style="width: 100%; display: flex; justify-content: flex-start;">
                                    <el-form-item prop="stateCountRule">
                                        按
                                        <el-select v-model="setting.stateCountRule" style="width: 150px; margin: 0 10px;">
                                            <el-option label="百分比" :value="0" />
                                            <el-option label="数量单位" :value="1" />
                                        </el-select>统计，
                                    </el-form-item>    
                                    <el-form-item prop="countVal">
                                        不足
                                        <el-select v-if="setting.stateCountRule == 0" v-model="setting.countVal" style="width: 100px; margin: 0 10px;">
                                            <el-option label="10%" :value="10" />
                                            <el-option label="20%" :value="20" />
                                            <el-option label="30%" :value="30" />
                                            <el-option label="40%" :value="40" />
                                            <el-option label="50%" :value="50" />
                                        </el-select>
                                        <el-input-number v-else v-model="setting.countVal" :min="1" style="width: 150px;" />
                                    </el-form-item>
                                    <el-form-item prop="countUnit">
                                        <el-select v-model="setting.countUnit" class="m-2" style="width: 100px; margin-right: 10px;">
                                            <el-option label="最小单位" :value="0" />
                                            <el-option label="最大单位" :value="1" />
                                        </el-select>数量时，提醒我
                                    </el-form-item>
                                </div>
                                <el-form-item>
                                    <el-alert title="如：当药品库存数量，不足 50 瓶时，提醒我" type="success" :closable="false" />
                                </el-form-item>
                        </div>
                        <div style="margin-bottom: 50px;">
                                <h2>提醒：药品过期</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <el-form-item prop="expiryAlertMonth">
                                        <span>药品剩余</span>
                                        <el-input-number v-model="setting.expiryAlertMonth" :min="1" style="width: 120px; margin: 0 10px;" />
                                        <span>个月时，进行提醒</span>
                                    </el-form-item>
                                </div>
                        </div>
                    </el-form>
                </el-scrollbar>
            </div>
            <div style="text-align: center; height: 10%; padding: 0 5%; font-size: 1.2vh">
                <el-button @click="handle.saveClick(formRef)" type="primary" style="width: 200px; height: 100%;" >
                    <span>保存</span>
                </el-button>
            </div>
        </el-card>
    </div>   
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'

import { ElNotification, } from 'element-plus';

import { getTimeStr, } from '@/utils/dateUtils.js';
import { settingService } from '@/api/clinic/index.js'
import { object } from 'vue-types';
// import { loginUserStore } from '@/stores/UserStore'
// const user = loginUserStore.getUser();
const setting = reactive({
    isPharmacyPay: "0",
            stateCountRule: 0,
            countVal: 50,
            countUnit: 0,
            expiryAlertMonth: 3,
})

const upBusinessTime = ref([]);
const downBusinessTime = ref([]);
const handle = {
    saveClick() {
        // if (!verifyTime())
        //     return;
        // else
        initTime();
        settingService.update(setting, (response) => {
        })
    },
} 

onMounted(() => {
    settingService.search((res) => {
        if (res) {
            initDefault()
            res.isPharmacyPay = res.isPharmacyPay.toString();
            Object.assign(setting, res)
            
            initBusinessDay(setting.businessDayList)
            initBusinessTime(setting.businessTimeList);
        } else
            initDefaultDay();
    })
})
//处理默认参数
function initDefault() {
    if(setting){
        Object.assign(setting, {
            isPharmacyPay: "0",
            stateCountRule: 0,
            countVal: 50,
            countUnit: 0,
            expiryAlertMonth: 3,
        })
    }
    if(isBlank(setting.isPharmacyPay)){
        setting.isPharmacyPay = "0"
    }
}

function isBlank(value){
    return value == null || value == undefined || value == ''
}

/**初始化营业天数 */
function initBusinessDay(busDayList) {
    if (!isArr(busDayList))
        initDefaultDay();
}

/**初始化默认营业天数 */
function initDefaultDay() {
    setting.businessDayList = ["1", "2", "3", "4", "5"];
}

/**
 * 初始化所有时间段营业时间
 * @param {所有时间段营业起始、结束时间映射列表} busTimeList 
 */
function initBusinessTime(busTimeList) {
    if (busTimeList.length < 1) {
        let nowDateStr = new Date().toLocaleDateString();
        upBusinessTime.value = [
            convertTimeStringToFullDate("080000", nowDateStr),
            convertTimeStringToFullDate("120000", nowDateStr),
        ];

        downBusinessTime.value = [
            convertTimeStringToFullDate("140000", nowDateStr),
            convertTimeStringToFullDate("180000", nowDateStr),
        ];
        return;
    }

    if (busTimeList) {
        let now = new Date();
        let nowDateStr = now.toLocaleDateString();

        let upBusinessArr = busTimeList[0][1]

        let downBusinessArr = busTimeList[1][2]

        upBusinessTime.value = [
            convertTimeStringToFullDate(upBusinessArr[0], nowDateStr),
            convertTimeStringToFullDate(upBusinessArr[1], nowDateStr),
        ];

        downBusinessTime.value = [
            convertTimeStringToFullDate(downBusinessArr[0], nowDateStr),
            convertTimeStringToFullDate(downBusinessArr[1], nowDateStr),
        ];
    }
}
function convertTimeStringToFullDate(timeStr, dateStr) {
    // 解析时间字符串
    const hours = parseInt(timeStr.substring(0, 2), 10);
    const minutes = parseInt(timeStr.substring(2, 4), 10);
    const seconds = timeStr.length > 4 ? parseInt(timeStr.substring(4, 6), 10) : 0;

    // 解析日期字符串
    const [year, month, day] = dateStr.split('/').map(Number);

    // 创建 Date 对象
    const dateObj = new Date(year, month - 1, day, hours, minutes, seconds);

    return dateObj;
}

/**初始化营业时间 */
function initTime() {
    if (!isArr(setting.businessTimes))
        setting.businessTimes = [];

    setting.businessTimes.length = 0;

    if (isArr(upBusinessTime.value))//上午营业时间
        setting.businessTimes.push({ 1: initTimeByDateOfArr(upBusinessTime.value) });

    if (isArr(downBusinessTime.value))//下午营业时间
        setting.businessTimes.push({ 2: initTimeByDateOfArr(downBusinessTime.value) })
}

/**是否有效数组 */
function isArr(arr) {
    return arr != null && arr != undefined && arr.length > 1;
}

/**根据日期列表，初始化时间戳列表 */
function initTimeByDateOfArr(dateArr) {
    let leftTimeStr = getTimeStr(dateArr[0]);
    let rightTimeStr = getTimeStr(dateArr[1]);
    return [leftTimeStr, rightTimeStr];
}

/**校验营业时间 */
function verifyTime() {
    let isDone = isArr(upBusinessTime.value) || isArr(downBusinessTime.value);
    if (!isDone)
        ElNotification({ title: '保存失败', message: '上午或下午必须填营业时间', type: 'warning', })
    return isDone;
}
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
}
</style>
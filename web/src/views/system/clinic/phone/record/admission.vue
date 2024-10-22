<template>
    <el-scrollbar height="100%">
        <el-button @click="routerInstance.back()">
            返回
        </el-button>
        <div style="font-size: 5vw; font-weight: 700; border-bottom: 1px solid #eeeeee;">
            门诊日志详情
        </div>
        <div>
            <span style="font-size: 4.5vw;">{{ admissionLog }}</span>
            <div v-for="(drug, index) in admissionLog.prescriptionDrugs" :key="index" style="display: flex; flex-direction: column; margin: 2vh 0;">
                <span style="width: auto; font-size: 4.5vw;">
                    {{ index + 1}}. {{ drug.name }} ({{ drug.spec }})
                </span>
                <span style="padding-left: 10vw;">
                    {{ drug.drugUsage }} {{ drug.frequency }} 一次 {{ drug.quantity }}{{ drug.quantityUnit }}
                </span>
            </div>
        </div>
    </el-scrollbar>
</template>
<script setup>
import openPhoneService from '@/api/clinic/open/phone.api.js'
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
const routerInstance = useRouter()

const admissionLog = ref({})

onMounted(() => {
    let query = routerInstance.currentRoute.value.query
    if(query) {
        if(query.id) {
            openPhoneService.searchOpenPrescription(query.id, res => {
                admissionLog.value = res
            })
        }
    }
})

</script>
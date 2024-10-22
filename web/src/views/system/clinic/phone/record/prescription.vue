<template>
    <el-scrollbar height="100%">
        <el-button @click="routerInstance.back()">
            返回
        </el-button>
        <div style="font-size: 5vw; font-weight: 700; border-bottom: 1px solid #eeeeee;">
            全部处方
        </div>
        <div v-for="(prescription, index) in prescriptionList" :key="index" style="display: flex; flex-direction: column; margin: 2vh 0;">
            <p style="width: auto; font-size: 4.5vw;">
                {{ prescription }}
            </p>
        </div>
    </el-scrollbar>
</template>
<script setup>
import openPhoneService from '@/api/clinic/open/phone.api.js'
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
const routerInstance = useRouter()

const prescriptionList = ref([])

onMounted(() => {
    let query = routerInstance.currentRoute.value.query
    if(query) {
        if(query.id) {
            openPhoneService.searchOpenAllPrescription(query.id, res => {
                prescriptionList.value = res
            })
        }
    }
})


</script>
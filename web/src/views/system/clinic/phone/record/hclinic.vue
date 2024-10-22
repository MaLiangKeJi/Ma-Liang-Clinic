<template>
    <el-scrollbar height="100%">
        <el-button @click="routerInstance.back()">
            返回
        </el-button>
        <div style="font-size: 5vw; font-weight: 700; border-bottom: 1px solid #eeeeee;">
            历史就诊诊所
        </div>
        <el-table :data="clinicList" style="width: 100%">
        <el-table-column label="日期">
            <template #default="{ row }">
                {{ convertDate(row.createTime) }}
            </template>
        </el-table-column>
        <el-table-column prop="chiefComplaint" label="诊断"/>
        <el-table-column prop="clinicName" label="诊所"/>
        <el-table-column prop="physician" label="医生"/>
        <el-table-column fixed="right" label="操作">
            <template #default="{ row }">
                <el-button
                link
                type="primary"
                size="small"
                @click.prevent="toAdmission(row)"
                >
                查看详情
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    </el-scrollbar>
</template>
<script setup>
import openPhoneService from '@/api/clinic/open/phone.api.js'
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { convertDate, convertSex, convertName, convertDiagnosis, convertIsFirst, convertAge, convertPhone, } from '@/utils/converter.js'

const routerInstance = useRouter()

const clinicList = ref([])

onMounted(() => {
    let query = routerInstance.currentRoute.value.query
    if(query) {
        if(query.openId) {
            openPhoneService.searchOpenClinic(query.openId, res => {
                clinicList.value = res
            })
        }
    }
})


const toAdmission = (row) => {
    routerInstance.push({
        name: 'patientPhoneAdmission',
        query: {
            id: row.id
        }
    })
}
</script>
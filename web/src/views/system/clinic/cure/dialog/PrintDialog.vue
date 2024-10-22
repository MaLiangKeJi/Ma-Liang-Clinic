<template>
    <el-dialog v-model="printPreviewAbnormal">
        <template #header>
            <el-row justify="space-between">
                <el-col :span="6"></el-col>
                <el-col :span="6">
                    <el-form-item label="模板选择">
                        <el-select v-model="template" placeholder="请选择处方模板！">
                            <el-option v-for="(name, index) in templateNames" :key="index" :label="name"
                                :value="index" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6"></el-col>
            </el-row>
        </template>
        <div>
            <el-row :gutter="24">
                <el-col :span="6"></el-col>
                <el-col :span="12">
                    <el-card>
                        <el-image :src="image" />
                    </el-card>
                </el-col>
                <el-col :span="6"></el-col>
            </el-row>
        </div>
        <template #footer>
            <div style="text-align: center;">
                <el-button @click="closeDialog" style="width: 200px; height: 50px;">取消</el-button>
                <el-button type="primary" @click="showFile" style="width: 200px; height: 50px;">预览并打印</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { prescriptionService as service } from '@/api/clinic/index.js'
import { ref, onMounted } from 'vue'
import axios from '@/api/axios.js'
import { getToken } from '@/stores/UserStore.js';
import image from '@/static/template.png'
//处方预览打印弹窗
const printPreviewAbnormal = ref(false)
// 处方预览打印
const template = ref(0)
const templateNames = ref([])
const admissionId = ref(null)
onMounted(() => {
    service.searchTemplateName({}, (res) => {
        templateNames.value = res
    })
})
const clearData = () => {
    admissionId.value = null
}
const closeDialog = () => {
    clearData()
    printPreviewAbnormal.value = false

}

const showFile = async () => {
    axios.get('/api/clinic/prescription/file', {
        timeout: 9000,
        params: { admissionLogId: admissionId.value, templateIndex: template.value, },
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Content-Type': 'application/json; application/octet-stream'
        },
        responseType: 'arraybuffer'
    }).then(function ({ data }) {
        window.open(URL.createObjectURL(new Blob([data], { type: 'application/pdf' })))
    })
}

function showDialog(id) {
    admissionId.value = id
    printPreviewAbnormal.value = true
}
//暴露方法
defineExpose({
    showDialog
});
</script>
<style scoped></style>
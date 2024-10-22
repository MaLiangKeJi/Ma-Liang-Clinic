<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">新增证明</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <div style="height: 90%; padding: 2%;">
                <el-scrollbar height="100%">
                    <el-form ref="formRef" :rules="rules" :model="form" label-width="10vw" style="height: 100%; padding: 2%">
                        <el-row :gutter="24">
                            <el-col :span="8">
                                <el-form-item prop="name" label="姓名">
                                    <el-input v-model="form.name" :disabled="!isNotSubmit"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item prop="sex" label="性别">
                                    <el-radio-group v-model="form.sex" :disabled="!isNotSubmit">
                                        <el-radio :value="1">男</el-radio>
                                        <el-radio :value="0">女</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="8">
                                <el-form-item prop="age" label="年龄">
                                    <el-input v-model="form.age" :disabled="!isNotSubmit">
                                        <template #append>岁</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="address" label="单位或住址">
                                    <el-input v-model="form.address"  :disabled="!isNotSubmit"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="8">
                                <el-form-item prop="visitDate" label="就诊日期">
                                    <el-date-picker v-model="form.visitDate" type="visitDate"  :disabled="!isNotSubmit"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="diagnosis" label="诊断意见">
                                    <el-input v-model="form.diagnosis" :disabled="!isNotSubmit"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="24">
                            <el-col :span="18">
                                <el-form-item prop="dealWith" label="治疗过程及建议">
                                    <el-input v-model="form.dealWith" :disabled="!isNotSubmit"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </el-scrollbar>
            </div>
            <div style="height: 10%; text-align: center;">
                <div v-if="isNotSubmit">
                    <el-button @click="handle.saveClick(formRef)" type="primary"  style="width: 200px; height: 50px;" >
                        保存
                    </el-button>
                    <el-button @click="handle.resetClick" style="width: 200px; height: 50px;">
                    重置
                    </el-button>
                </div>
                <div v-if="!isNotSubmit">
                    <el-button type="primary" @click="showFile" style="width: 200px; height: 50px;" >预览并打印</el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>
<script setup>
import { ref, reactive, onMounted ,watch} from 'vue'
import { proofService as service } from '@/api/clinic/index.js'
import axios from '@/api/axios.js'
import { getToken } from '@/stores/UserStore.js';
import { useRoute }from'vue-router'
const route = useRoute()

const routerQuery = route.query
const form =  reactive({
    visitDate: new Date()
})
const formRef = ref()
const isNotSubmit = ref(true)

onMounted(async() => {
    form.id = routerQuery.id
    if(form.id!=null||form.id!=undefined){
        await getOneData()
        isNotSubmit.value = false
    }
})

const rules = reactive({
  name: [
    { required: true, message: '姓名必须输入', trigger: 'blur' },
  ],
  sex: [
    { required: true, message: '性别必须选择', trigger: 'blur' },
  ],
  age: [
    { required: true, message: '年龄必须选择', trigger: 'blur' },
  ],
  visitDate: [
    { required: true, message: '就诊日期必须选择', trigger: 'blur' },
  ],
  diagnosis: [
    { required: true, message: '诊断意见必须输入', trigger: 'blur' },
  ],
})

watch(() => route.query.id, (newValue, oldValue) => {
    formRef.value.resetFields()
    isNotSubmit.value = true
})

async function getOneData() {
    service.getOne({id:form.id}, (response) => {
        Object.assign(form, response)
    })
}



const showFile = async () => {
  axios.get('/api/clinic/proof/file', {
    timeout: 5000,
    params: { id: form.id},
    headers: {
      'Authorization': 'Bearer ' + getToken(),
      'Content-Type': 'application/json; application/octet-stream'
    },
    responseType: 'arraybuffer'
  }).then(function ({ data }) {
    window.open(URL.createObjectURL(new Blob([data], { type: 'application/pdf' })))
  })
}

const mehtod = {
    submit(callback) {
        service.add({
            "name":form.name,
            "visitDate":form.visitDate,
            "sex":form.sex,
            "age":form.age,
            "address":form.address,
            "diagnosis":form.diagnosis,
            "dealWith":form.dealWith,
        }, (response) => { callback(response) })
    },
}

const handle = {
    async saveClick(formEl) {
        if (!formEl) return
        await formEl.validate((valid, fields) => {
            if (valid) {
                mehtod.submit((response) => {  
                    form.id = response
                    isNotSubmit.value = false
                })
            } else {
            }
        })
    },
}
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 50px;
    margin-left: 10px;
}
.exporttable {
    border: solid 1px #000000;
}
:deep(.el-descriptions__label) {
    width: 120px;
}
:deep(.el-descriptions__body .el-descriptions__table.is-bordered .el-descriptions__cell) {
    border-color: #000000;
}
:deep(.el-input, .el-input__wrapper) {
    height: 100%;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 50px;
    line-height: 50px;
}
</style>
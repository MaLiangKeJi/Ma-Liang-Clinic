<template>
    <el-form
        ref="formRef"
        style="max-width: 600px"
        :model="form"
        :rules="rules"
        label-width="auto"
        :size="formSize"
        status-icon
    >
        <el-form-item label="公司名称" prop="name">
            <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="过期时间" prop="expiration">
            <el-input v-model="form.expiration" />
        </el-form-item>
        <el-form-item label="Activity zone" prop="region">
            <el-select v-model="form.region" placeholder="Activity zone">
                <el-option label="Zone one" value="shanghai" />
                <el-option label="Zone two" value="beijing" />
            </el-select>
        </el-form-item>
        <el-form-item label="Activity count" prop="count">
        <el-select-v2
            v-model="form.count"
            placeholder="Activity count"
            :options="options"
        />
        </el-form-item>
        <el-form-item label="Activity time" required>
        <el-col :span="11">
            <el-form-item prop="date1">
            <el-date-picker
                v-model="form.date1"
                type="date"
                aria-label="Pick a date"
                placeholder="Pick a date"
                style="width: 100%"
            />
            </el-form-item>
        </el-col>
        <el-col class="text-center" :span="2">
            <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
            <el-form-item prop="date2">
            <el-time-picker
                v-model="form.date2"
                aria-label="Pick a time"
                placeholder="Pick a time"
                style="width: 100%"
            />
            </el-form-item>
        </el-col>
        </el-form-item>
        <el-form-item label="Instant delivery" prop="delivery">
        <el-switch v-model="form.delivery" />
        </el-form-item>
        <el-form-item label="Activity location" prop="location">
        <el-segmented v-model="form.location" :options="locationOptions" />
        </el-form-item>
        <el-form-item label="Activity type" prop="type">
        <el-checkbox-group v-model="form.type">
            <el-checkbox value="Online activities" name="type">
            Online activities
            </el-checkbox>
            <el-checkbox value="Promotion activities" name="type">
            Promotion activities
            </el-checkbox>
            <el-checkbox value="Offline activities" name="type">
            Offline activities
            </el-checkbox>
            <el-checkbox value="Simple brand exposure" name="type">
            Simple brand exposure
            </el-checkbox>
        </el-checkbox-group>
        </el-form-item>
        <el-form-item label="Resources" prop="resource">
        <el-radio-group v-model="form.resource">
            <el-radio value="Sponsorship">Sponsorship</el-radio>
            <el-radio value="Venue">Venue</el-radio>
        </el-radio-group>
        </el-form-item>
        <el-form-item label="Activity form" prop="desc">
        <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
        <el-form-item>
        <el-button type="primary" @click="submitForm(formRef)">
            Create
        </el-button>
        <el-button @click="resetForm(formRef)">Reset</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref } from 'vue'
const formSize = ref('default')
const formRef = ref()
const form = reactive({
    name: 'Hello',
    region: '',
    count: '',
    date1: '',
    date2: '',
    delivery: false,
    location: '',
    type: [],
    resource: '',
    desc: '',
})

const locationOptions = ['Home', 'Company', 'School']

const rules = reactive({
    name: [
        { required: true, message: 'Please input Activity name', trigger: 'blur' },
        { min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' },
    ],
    region: [
        {
        required: true,
        message: 'Please select Activity zone',
        trigger: 'change',
        },
    ],
    count: [
        {
        required: true,
        message: 'Please select Activity count',
        trigger: 'change',
        },
    ],
    date1: [
        {
        type: 'date',
        required: true,
        message: 'Please pick a date',
        trigger: 'change',
        },
    ],
    date2: [
        {
        type: 'date',
        required: true,
        message: 'Please pick a time',
        trigger: 'change',
        },
    ],
    location: [
        {
        required: true,
        message: 'Please select a location',
        trigger: 'change',
        },
    ],
    type: [
        {
        type: 'array',
        required: true,
        message: 'Please select at least one activity type',
        trigger: 'change',
        },
    ],
    resource: [
        {
        required: true,
        message: 'Please select activity resource',
        trigger: 'change',
        },
    ],
    desc: [
        { required: true, message: 'Please input activity form', trigger: 'blur' },
    ],
})

const submitForm = async (formEl) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
    })
}

const resetForm = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}

const options = Array.from({ length: 10000 }).map((_, idx) => ({
    value: `${idx + 1}`,
    label: `${idx + 1}`,
}))
</script>

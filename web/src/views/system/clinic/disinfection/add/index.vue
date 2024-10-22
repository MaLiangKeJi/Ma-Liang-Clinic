<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">新增消杀</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <div v-if="isNotSubmit" style="height: 100%; padding: 1%;">
                <div style="height: 90%;">
                    <el-scrollbar height="100%">
                        <el-form ref="formRef" :model="form" label-width="120px" style="padding: 2%">
                            <h1>消杀日期</h1>
                            <el-divider />
                            <el-row :gutter="20">
                                <el-col :span="10">
                                    <el-form-item prop="disinfectionTime">
                                        <el-date-picker
                                            v-model="form.disinfectionTime"
                                            type="date"
                                            placeholder="选择日期"
                                            class="search_input" style="width: 300px !important;"
                                        />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消杀内容</h1>
                                <el-button @click="() => form.content = form.content && form.content.length > 0 ? [] : ['灭蝇', '灭蚊', '灭鼠', '灭蟑螂', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="content" style="width: 100%;">
                                <el-checkbox-group v-model="form.content" style="width: 100%;" size="large" class="checkbox">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox label="灭蝇" value="灭蝇" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox label="灭蚊" value="灭蚊" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox label="灭鼠" value="灭鼠" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox label="灭蟑螂" value="灭蟑螂" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox label="其他" value="其他" size="large" />
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消杀药品、器械</h1>
                                <el-button @click="() => form.items = form.items && form.items.length > 0 ? [] : ['灯', '笼', '板', '灭害灵', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="items" style="width: 100%;">
                                <el-checkbox-group v-model="form.items" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="spray">
                                                <el-checkbox label="灯" value="灯" size="large" />
                                            </el-form-item> 
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="wipe">
                                                <el-checkbox label="笼" value="笼" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox label="板" value="板" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox label="灭害灵" value="灭害灵" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox label="其他" value="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                
                            <h1>消杀时间（营业时间）</h1>
                            <el-divider />
                            <el-row :gutter="20">
                                <el-col :span="10">
                                    <el-form-item prop="timeRange">
                                        <el-time-picker
                                            v-model="form.timeRange"
                                            is-range
                                            range-separator="~"
                                            start-placeholder="开始营业"
                                            end-placeholder="营业结束"
                                            class="search_input" style="width: 300px !important;"
                                        />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消杀地点</h1>
                                <el-button @click="() => form.spot = form.spot && form.spot.length > 0 ? [] : ['店内', '店内及门口', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="spot" style="width: 100%;">
                                <el-checkbox-group v-model="form.spot" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="chlorine">
                                                <el-checkbox v-model="form.chlorine" label="店内" value="店内" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" label="店内及门口" value="店内及门口" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" label="其他" value="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                            
                            <el-row :gutter="20" style="width: 100%;" justify="end">
                                <el-col :span="4" :pull="6" style="min-width: 300px;">
                                    <el-form-item prop="executor" label="消杀人" style="width: 100%; margin-top: 100px;">
                                        <el-input v-model="form.executor" class="search_input" style="width: 300px !important;" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-form>
                    </el-scrollbar>
                </div>
                <div style="height: 10%; text-align: center; justify-content: center; align-items: center; display: flex;">
                    <el-button @click="handle.saveClick(formRef)" type="primary"  style="width: 200px; height: 65px;" >
                        保存
                    </el-button>
                    <el-button @click="handle.resetClick" style="width: 200px; height: 65px;">
                        重置
                    </el-button>
                </div>
            </div>
            <el-result
                v-else
                icon="success"
                title="记录保存成功！"
            >
            <template #extra>
                <el-button @click="handle.continueClick" type="primary">继续添加</el-button>
            </template>
            </el-result>
        </el-card>
    </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { disinfectionService as service } from '@/api/clinic/index.js'

const form = reactive({
    disinfectionTime: new Date(),
    timeRange: [
        new Date(2017,5,2,8,0,0),
        new Date(2017,5,2,18,0,0),
    ],
})
const formRef = ref()
const isNotSubmit = ref(true)

const mehtod = {
    submit(callback) {
        service.add({ 
            disinfectionTime: form.disinfectionTime, 
            content: JSON.stringify(form.content), 
            items: JSON.stringify(form.items),
            startTimeRange: form.timeRange[0],
            endTimeRange: form.timeRange[1],
            spot: JSON.stringify(form.spot),
            executor: form.executor,
        }, (response) => { callback(response) })
    },
}

const handle = {
    async saveClick(formEl) {
        if (!formEl) return
        await formEl.validate((valid, fields) => {
            if (valid) {
                mehtod.submit((response) => { isNotSubmit.value = false })
            } else {
            }
        })
    },
    resetClick() {
        formRef.value.resetFields()
    },
    continueClick() {
        isNotSubmit.value = true
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
:deep(.el-checkbox) {
    zoom: 200%;
}
:deep(.el-checkbox.el-checkbox--large .el-checkbox__label) {
    font-size: 0.6em;
}
</style>
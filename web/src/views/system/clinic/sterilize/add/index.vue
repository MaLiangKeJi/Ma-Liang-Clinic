<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 20%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">{{ currentMethod == 'index' ? '新增消毒' : '定时记录消毒'}}</el-text>
            </div>
            <!-- <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-button v-if="currentMethod == 'index'" @click="currentMethod = 'task'" type="primary" style="width: 200px; height: 5vh;">
                    <el-icon size="2vh" style="margin-right: 1vh;"><Clock /></el-icon>
                    <span>自动记录</span>
                </el-button>
            </div> -->
        </el-form>
        <el-card v-if="currentMethod == 'index'" style="height: 85%; border-radius: 15px;">
            <div v-if="isNotSubmit" style="height: 100%; padding: 1%;">
                <div style="height: 90%;">
                    <el-scrollbar height="100%">
                        <el-form ref="formRef" :model="form" label-width="120px" style="padding: 2%">
                            <h1>消毒日期</h1>
                            <el-divider />
                            <el-row :gutter="20">
                                <el-col :span="10">
                                    <el-form-item prop="sterilizeTime">
                                        <el-date-picker
                                            v-model="form.sterilizeTime"
                                            type="date"
                                            placeholder="选择日期"
                                            class="search_input" style="width: 300px !important;"
                                        />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒位置</h1>
                                <el-button @click="() => form.content = form.content && form.content.length > 0 ? [] : ['听诊器', '诊床', '医疗废物桶', '门把手', '墙面', '诊桌', '血压计', '药房', '诊室', '观察室', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="content" style="width: 100%;">
                                <el-checkbox-group v-model="form.content" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.stethoscope" value="听诊器" label="听诊器" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.bed" value="诊床" label="诊床" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.clinicalWastePail" value="医疗废物桶" label="医疗废物桶" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.doorknob" value="门把手" label="门把手" size="large" />
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.wall" value="墙面" label="墙面" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.desk" value="诊桌" label="诊桌" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.sphygmomanometer" value="血压计" label="血压计" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.pharmacy" value="药房" label="药房" size="large" />
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.consultingRoom" value="诊室" label="诊室" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.observationRoom" value="观察室" label="观察室" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.observationRoom" value="其他" label="其他" size="large" />
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒方法</h1>
                                <el-button @click="() => form.method = form.method && form.method.length > 0 ? [] : ['喷洒', '擦拭', '紫外线灯消毒', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="method" style="width: 100%;">
                                <el-checkbox-group v-model="form.method" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="spray">
                                                <el-checkbox v-model="form.spray" value="喷洒" label="喷洒" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="wipe">
                                                <el-checkbox v-model="form.wipe" value="擦拭" label="擦拭" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox v-model="form.ultroviolet" value="紫外线灯消毒" label="紫外线灯消毒" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox v-model="form.ultroviolet" value="其他" label="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                
                
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒剂</h1>
                                <el-button @click="() => form.disinfector = form.disinfector && form.disinfector.length > 0 ? [] : ['含氯制剂（84消毒液、优氯净、益康等）', '75%以上酒精', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="disinfector" style="width: 100%;">
                                <el-checkbox-group v-model="form.disinfector" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="10">
                                            <el-form-item prop="chlorine">
                                                <el-checkbox v-model="form.chlorine" value="含氯制剂（84消毒液、优氯净、益康等）" label="含氯制剂（84消毒液、优氯净、益康等）" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" value="75%以上酒精" label="75%以上酒精" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="10">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" value="其他" label="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                            
                            <el-row :gutter="20" style="width: 100%;" justify="end">
                                <el-col :span="4" :pull="6" style="min-width: 300px;">
                                    <el-form-item prop="executor" label="消毒人" class="search_input" style="width: 300px !important;">
                                        <el-input v-model="form.executor" />
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
        <el-card v-else-if="currentMethod == 'task'" style="height: 85%; border-radius: 15px;">
            <div v-if="step == 0" style="height: 100%; padding: 1%;">
                <div style="height: 90%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                    <div style="display: flex; justify-content: center; align-items: center;">
                        <span style="font-size: 1vw; margin-right: 1vw;">定时在</span>
                        <el-select v-model="isCustomTime" size="large" style="width: 10vw; height: 5vh; margin-right: 2vw">
                            <el-option label="营业时间" :value="false" />
                            <el-option label="自定义时间" :value="true" />
                        </el-select>
                        <el-time-picker v-model="execTime" arrow-control placeholder="Arbitrary time" style="width: 10vw; height: 5vh" />
                        <span style="font-size: 1vw; margin-left: 1vw;">自动生成消毒记录</span>
                    </div>
                    <div v-if="isCustomTime" style="height: 10vh; display: flex; justify-content: center; align-items: center;">
                        <el-checkbox-group v-model="executionDateRange">
                            <el-checkbox label="周一" :value="1" />
                            <el-checkbox label="周二" :value="2" />
                            <el-checkbox label="周三" :value="3" />
                            <el-checkbox label="周四" :value="4" />
                            <el-checkbox label="周五" :value="5" />
                            <el-checkbox label="周六" :value="6" />
                            <el-checkbox label="周日" :value="7" />
                        </el-checkbox-group>
                    </div>
                    <div v-else style="height: 10vh; width: 0;">

                    </div>
                    <div style="height: 5vh;">
                        <el-text class="mx-1" type="info" style="font-size: 0.8vw;">注意: 本功能为辅助记录。同时，您需要按当地卫生局管理要求，进行消毒、消杀工作</el-text>
                    </div>
                </div>

                <div style="height: 10%; text-align: center; justify-content: center; align-items: center; display: flex;">
                    <el-button @click="currentMethod = 'index'" style="width: 200px; height: 65px;">
                        取消
                    </el-button>
                    <el-button @click="step++" type="primary"  style="width: 200px; height: 65px;" >
                        下一步
                    </el-button>
                </div>
            </div>
            <div v-else-if="step == 1" style="height: 100%; padding: 1%;">
                <div style="height: 90%;">
                    <el-scrollbar height="100%">
                        <el-form ref="formRef" :model="form" label-width="120px" style="padding: 2%">
                            <h1>消毒日期</h1>
                            <el-divider />
                            <el-row :gutter="20">
                                <el-col :span="10">
                                    <el-form-item prop="sterilizeTime">
                                        <el-date-picker
                                            v-model="form.sterilizeTime"
                                            type="date"
                                            placeholder="选择日期"
                                            class="search_input" style="width: 300px !important;"
                                        />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒位置</h1>
                                <el-button @click="() => form.content = form.content && form.content.length > 0 ? [] : ['听诊器', '诊床', '医疗废物桶', '门把手', '墙面', '诊桌', '血压计', '药房', '诊室', '观察室', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="content" style="width: 100%;">
                                <el-checkbox-group v-model="form.content" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.stethoscope" value="听诊器" label="听诊器" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.bed" value="诊床" label="诊床" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.clinicalWastePail" value="医疗废物桶" label="医疗废物桶" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.doorknob" value="门把手" label="门把手" size="large" />
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.wall" value="墙面" label="墙面" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.desk" value="诊桌" label="诊桌" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.sphygmomanometer" value="血压计" label="血压计" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.pharmacy" value="药房" label="药房" size="large" />
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.consultingRoom" value="诊室" label="诊室" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.observationRoom" value="观察室" label="观察室" size="large" />
                                        </el-col>
                                        <el-col :span="5">
                                            <el-checkbox v-model="form.observationRoom" value="其他" label="其他" size="large" />
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒方法</h1>
                                <el-button @click="() => form.method = form.method && form.method.length > 0 ? [] : ['喷洒', '擦拭', '紫外线灯消毒', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="method" style="width: 100%;">
                                <el-checkbox-group v-model="form.method" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="spray">
                                                <el-checkbox v-model="form.spray" value="喷洒" label="喷洒" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="wipe">
                                                <el-checkbox v-model="form.wipe" value="擦拭" label="擦拭" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox v-model="form.ultroviolet" value="紫外线灯消毒" label="紫外线灯消毒" size="large" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="5">
                                            <el-form-item prop="ultroviolet">
                                                <el-checkbox v-model="form.ultroviolet" value="其他" label="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                
                
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h1>消毒剂</h1>
                                <el-button @click="() => form.disinfector = form.disinfector && form.disinfector.length > 0 ? [] : ['含氯制剂（84消毒液、优氯净、益康等）', '75%以上酒精', '其他']" class="search_button" type="primary" plain>全选</el-button>
                            </div>
                            <el-divider />
                            <el-form-item prop="disinfector" style="width: 100%;">
                                <el-checkbox-group v-model="form.disinfector" style="width: 100%;">
                                    <el-row :gutter="20">
                                        <el-col :span="10">
                                            <el-form-item prop="chlorine">
                                                <el-checkbox v-model="form.chlorine" value="含氯制剂（84消毒液、优氯净、益康等）" label="含氯制剂（84消毒液、优氯净、益康等）" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="5">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" value="75%以上酒精" label="75%以上酒精" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="10">
                                            <el-form-item prop="alcohol">
                                                <el-checkbox v-model="form.alcohol" value="其他" label="其他" size="large" />
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-checkbox-group>
                            </el-form-item>
                            
                            <el-row :gutter="20" style="width: 100%;" justify="end">
                                <el-col :span="4" :pull="6" style="min-width: 300px;">
                                    <el-form-item prop="executor" label="消毒人" class="search_input" style="width: 300px !important;">
                                        <el-input v-model="form.executor" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-form>
                    </el-scrollbar>
                </div>

                <div style="height: 10%; text-align: center; justify-content: center; align-items: center; display: flex;">
                    <el-button @click="currentMethod = 'index'" style="width: 200px; height: 65px;">
                        取消
                    </el-button>
                    <el-button @click="step--" style="width: 200px; height: 65px;" >
                        上一步
                    </el-button>
                    <el-button @click="step++" type="primary"  style="width: 200px; height: 65px;" >
                        完成
                    </el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { sterilizeService as service } from '@/api/clinic/index.js'

const form = reactive({
    sterilizeTime: new Date()
})
const formRef = ref()
const isNotSubmit = ref(true)

const currentMethod = ref('index')
const isCustomTime = ref(false)
const execTime = ref(new Date())
const executionDateRange = ref([])
const step = ref(0)

const mehtod = {
    submit(callback) {
        service.add({ 
            sterilizeTime: form.sterilizeTime, 
            content: JSON.stringify(form.content), 
            method: JSON.stringify(form.method),
            disinfector: JSON.stringify(form.disinfector),
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
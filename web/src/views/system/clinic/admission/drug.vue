<template>
    <el-card class="drugFrame">
        <template #header>
            <div class="card-header"
                style="height: 100%; padding: 1% 1.5%; display: flex; justify-content: space-between;">
                <span style="font-size: 1.2em; font-weight: 700;">处方</span>
            </div>
        </template>

        <el-scrollbar max-height="75vh" always="true">
            <div v-for='drug in drugArr' style="height: 100%; padding: 2% 2% 0 2%;">
                <el-descriptions class="margin-top" :title='drug.name' :column="1" :border="true" size="large">
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon :style="iconStyle"></el-icon>
                                单次剂量
                            </div>
                        </template>
                        <el-text>{{ drug.singleDose }}{{ drug.singleDoseUnit }}</el-text>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon :style="iconStyle"></el-icon>
                                用法
                            </div>
                        </template>
                        <el-text>{{ drug.drugUsage + " | " }}{{ drug.frequency }}</el-text>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon :style="iconStyle"></el-icon>
                                天数
                            </div>
                        </template>
                        <el-text>{{ drug.period }}{{ drug.periodUnit }}</el-text>
                    </el-descriptions-item>
                </el-descriptions>
            </div>
        </el-scrollbar>

    </el-card>
</template>

<script setup>
import { ref, onMounted, } from 'vue';
import { useRouter } from 'vue-router';
import drugAPI from '@/api/clinic/drug.api.js';



const routerInstance = useRouter()
const drugArr = ref([]);
onMounted(() => {
    let presId = routerInstance.currentRoute.value.query.presId;//处方id
    drugAPI.getDrugArr(presId, data => {
        if (data)
            data.forEach(d => drugArr.value.push(d))
    })
})
</script>

<style scoped>
.drugFrame {
    width: 86%;
    height: 89%;
    border-radius: 15px;

    margin: auto;
    margin-top: 4vh;
}
</style>
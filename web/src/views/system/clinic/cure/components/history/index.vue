<template>
    <el-timeline class="timeline">
        <el-timeline-item center placement="top" :hide-timestamp="true" type="primary" 
            v-for="(log, index) in operationLogs"  :key="index"
            @click="() => toCure(log)" 
        >
            <el-card
                v-if="!log.isFill"
                :shadow="log.isOpen ? 'always' : 'hover'"
                class="card"
                :style="{ 'height': (!log.isFill) && log.isOpen ? '' : '5vh' }"
            >
                <div v-if="!log.isOpen" class="content close">
                    <div class="timestamp">
                        <div class="timestamp_date"><span>{{ log.isCurrentDay ? log.createHMSTwelveHourlySystem : log.createHMS }}</span></div>
                    </div>
                    <div class="info">
                        <el-text class="mx-1 timestamp_time" type="info">{{ getTime(log) }}</el-text>
                        <el-text class="mx-1 info_title">{{ log.service }}</el-text>
                        <el-text class="mx-1 info_description" type="info">{{ log.about }}</el-text>
                    </div>
                </div>
                <div v-else class="content open">
                    <div v-if="log.info.dossier" class="case">
                        <div v-if="isNotEmpty(log.info.dossier.chiefComplaint)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">主诉</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.chiefComplaint }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.historyPresentIllness)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">现病史</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.historyPresentIllness }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.pastMedicalHistory)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">既往史</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.pastMedicalHistory }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.checkup)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">查体</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.checkup }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.diagnosis)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">诊断</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.diagnosis }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.dealWith)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">处理</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.dealWith }}</el-text>
                        </div>
                        <div v-if="isNotEmpty(log.info.dossier.notes)" 
                            class="item"
                        >
                            <el-text class="mx-1 label">备注</el-text>
                            <el-text class="mx-1 description" type="info">{{ log.info.dossier.notes }}</el-text>
                        </div>
                    </div>
                    <el-divider v-if="log.info.dossier" border-style="dashed" />
                    <div v-if="log.info.prescriptionDrugs.length > 0 " v-for="(drug, index) in log.info.prescriptionDrugs"  :key="index" 
                        style="width: 100%; padding-left: 20px;" 
                        class="prescription"
                    >
                        <div>
                            <el-text class="mx-1 drug-name">{{ index + 1 }}. {{ drug.name }}({{ drug.spec }})</el-text>
                        </div>
                        <div>
                            <el-text class="mx-1 curative-time" type="info">{{ drug.frequency }} 共 {{ drug.period }} {{ drug.periodUnit }}</el-text>
                        </div>
                    </div>
                    <el-text v-else class="mx-1" type="info" style="width: 100%; display: flex; margin: 1vh 0;">无处方</el-text>
                </div>
                <template v-if="log.isOpen" #header>
                    <div class="header">
                        <div class="timestamp_date">{{ log.createYMD }}</div>
                        <div class="timestamp_time">{{ log.createHMS }}</div>
                    </div>
                </template>
                <template v-if="log.isOpen" #footer>
                    <el-button @click="(event) => { log.isOpen = false; event.stopPropagation(); }" :icon="CaretTop" text bg style="width: 100%; height: 100%; min-height: 60px;">
                        <el-text class="mx-1" type="info" style="font-size: 1.2em;">收缩</el-text>
                    </el-button>
                </template>
            </el-card>
        </el-timeline-item>
    </el-timeline>


    <el-drawer
        v-model="drawer"
        direction="rtl"
        size="40%"
    >   
        <dev style="height: 100%; display: flex;flex-direction: column;">
            <el-row :gutter="30" style="height: 40%;">
                <el-col :span="10">
                    <dev class="drawerFont">主诉:{{ drawerData.dossier?drawerData.dossier.chiefComplaint :'无'}}</dev>
                </el-col>
                <el-col :span="10">
                    <dev class="drawerFont">诊断: {{ drawerData.diagnosis || '无'}}</dev>
                </el-col>
                <el-col :span="10">
                    <dev class="drawerFont">处理: {{ drawerData.dossier?drawerData.dossier.dealWith : '无'}}</dev>
                </el-col>
                <el-col :span="10">
                    <dev class="drawerFont">辅助检查: {{ drawerData.dossier?drawerData.dossier.auxiliaryCheckup : '无'}}</dev>
                </el-col>
                <el-col :span="10">
                    <dev class="drawerFont">总价: {{ drawerData.pay? drawerData.pay.fee+"元":'无' }}</dev>
                </el-col>
                <el-col :span="10">
                    <dev class="drawerFont">支付状态: {{ drawerData.pay?drawerData.pay.state==0?"未支付":"已支付":'无' }} {{ drawerData.pay?getWay(drawerData.pay.way):""}}</dev>
                </el-col> 
            </el-row>
            <el-table :data="drawerData.prescriptionDrugs" class="drawerFont" style="height: 60%;">
                <el-table-column label="药品名称" property="name" width="240" />
                <el-table-column label="规格" property="spec" width="150"/>
                <el-table-column label="用法用量">
                    <template #default="{row}">
                        {{ row.drugUsage+row.frequency +"一次"+row.singleDose+row.singleDoseUnit+"共"+row.period+row.periodUnit}}
                    </template>
                </el-table-column>
            </el-table>
        </dev>    
    </el-drawer>
</template>
<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { CaretTop } from '@element-plus/icons-vue'
import logService from '@/api/clinic/log.api.js'
import { asyncSearchJoin } from '@/api/clinic/admission.api.js'
const props = defineProps(['patient'])

const operationLogs = ref([])

const drawer = ref(false)


onMounted(() => {
    logService.searchOperationList({patientId:props.patient.id,current:1,size:10}, res => {
        res[res.length -1].isLast = true
        operationLogs.value = [{ isFill: true }, ...res, { isFill: true }, ]
    })
})
const drawerData = ref({})

async function toCure(operationLog) {
    let res = await asyncSearchJoin({ id: operationLog.param });
    operationLog.info = res.data.data
    operationLog.isOpen = true
}

const getWay = (way)=>{
    switch (way) {
        case 1:
            return "微信"
        case 2:
            return "支付宝"
        case 3:
            return "挂账"
        case 4:
            return "现金"
        default:
            return ""
    }
}
function getTime(log) {
    if(log.isCurrentYear) {
        if(log.isCurrentDay) {
            return '今天'
        } else {
            return log.createMD	
        }
    } else {
        return log.createYMD
    }
}
function isNotEmpty(val) {
    return val && val != ''
}
</script>
<style scoped lang="scss">
.timeline {
    width: 100%; 
    padding-left: 1vw;
    
    .card {
        width: 90%; 
        cursor: pointer;
        
        .content {
            height: 100%;
            display: flex;
            padding-left: 20px;
        }
        .close {
            .timestamp {
                display: flex;
                align-items: center !important;
                
                .timestamp_date{
                    height: 100%;
                    font-size: 2vh;
                    display: flex;
                    align-items: center;
                }   
            }
            .info {
                display: flex;
                align-items: center;

                .timestamp_time {
                    height: 100%;
                    font-size: 2vh;
                    display: flex;
                    align-items: center;
                    margin: 0 1vw;
                }
                
                .info_title {
                    margin: 0 1vw;
                    font-size: 1.6vh;
                }
                .info_description {
                    font-size: 1.6vh;
                }
            }
        }
        .open {
            display: flex;
            flex-direction: column;
            height: 100%; 
            .case {
                height: 40%;
                padding: 0 5%;
                display: flex; 
                flex-direction: column; 
                .item {
                    margin: 1vh 0;
                    .label {
                        margin-right: 1vh;
                    }
                    .description {
                    }
                }
            }
            .prescription {
                margin: 1vh 0;
                display: flex;
                flex-direction: column; 
                align-items: flex-start;
                .drug-name {
                            
                }
                .curative-time {
                    padding-left: 2vh;
                }
            } 
        }
        :deep(.el-card__header) {
            height: 6vh;
        }
        :deep(.el-card__footer) {
            height: 6vh;
        }
        .header {
            height: 100%; 
            width: 100%; 
            padding-left: 20px; 
            display: flex; 
            align-items: center;
            background-color: #6a5acd;
            color: #fff;
            font-size: 2vh;
            .timestamp_date {
                margin-right: 1vh;
            }
            .timestamp_time {
            }
        }
    }
}
.drawerFont {
    font-size: 1.5em;
}
.el-divider--vertical {
    height: none !important;
}
:deep(.el-card__footer) {
    padding: 0;
}
:deep(.el-card__header) {
    padding: 0;
}
:deep(.el-card__body) {
    padding: 0;
}
:deep(.el-card__footer.el-card__header) {
    height: 20% !important;
}
:deep(.el-card__body) {
    height: 60% !important;
}
:deep(.el-card) {
    border: none !important;
}
:deep(.el-timeline) {
    padding-left: 10px !important;
}
:deep(.el-card__body) {
    height: 100% !important;
}
</style>
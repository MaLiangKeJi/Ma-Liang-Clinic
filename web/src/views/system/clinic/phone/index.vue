<template>
    <div class="container">
        <el-container style="height: 100%;">
            <el-header>
                <div>
                    <el-text class="mx-1" style="font-size: 6vw; font-weight: 700;">个人中心</el-text>
                </div>
            </el-header>
            <el-main  style="height: 90%;">
                <div class="row">
                    <el-button class="btn" text @click="toPrescription">
                        <div class="btn-content">
                            <el-icon size="10vw" color="#8a81f8" class="icon"><Document /></el-icon>
                            <span class="span">历史处方</span>
                        </div>
                    </el-button>
                    <el-button class="btn" text @click="toHistoryClinic">
                        <div class="btn-content">
                            <el-icon size="10vw" color="#8a81f8" class="icon"><House /></el-icon>
                            <span class="span">去过的诊所</span>
                        </div>
                    </el-button>
                </div>
                <div class="row">
                    <el-button class="btn" text @click="toDoctor">
                        <div class="btn-content">
                            <el-icon size="10vw" color="#8a81f8" class="icon"><Iphone /></el-icon>
                            <span class="span">联系医生</span>
                        </div>
                    </el-button>
                </div>
            </el-main>
            <div class="custom-style">
                <el-segmented v-model="currentSelectOption" :options="options" style="width: 100%; height: 5vh;" @change = "toClinicIndex"/>
            </div>
        </el-container>
    </div>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
const routerInstance = useRouter()
const openId = ref('oPAMN6qXO-TXfqMOwvC8OI-osd-g')
const menuDrawer = ref(false)
onMounted(() => {
     
})

const toHistoryClinic = () => {
    routerInstance.push({
        name: 'patientPhoneHistoryClinic',
        query: {
            openId: openId.value
        }
    })
}
const toPrescription = () => {
    routerInstance.push({
        name: 'patientHistoryPrescription',
        query: {
            openId: openId.value
        }
    })
}
const toDoctor = () => {
    routerInstance.push({
        name: 'patientPhoneDoctor',
        query: {
            openId: openId.value
        }
    })
}



function toClinicIndex(){
    routerInstance.push({
        name: 'patientPhoneClinic',
        query: {
            openId: openId.value
        }
   
    })
    menuDrawer.value = false
}

const currentSelectOption = ref('个人中心')
const options = ['个人中心', '找诊所']

</script>
<style lang="scss" scoped>
.custom-style .el-segmented {
    --el-segmented-item-selected-color: var(--el-text-color-primary);
    --el-segmented-item-selected-bg-color: #8a81f8;
    --el-border-radius-base: 16px;
}
.custom-style .el-segmented .is-selected  {
    color: #fff !important;
}
.container {
    height: 100%; 
    padding: 5vw; 
    background-color: #fff;
    .row {
        display: flex; 
        justify-content: space-between; 
        margin: 5vh 0;
        .btn {
            width: 40vw; 
            height: 40vw;
            .btn-content {
                display: flex; 
                flex-direction: column; 
                align-items: center;
                .icon {
                    margin-bottom: 2vh;
                }
                .span {

                }
            }
        }
    }
}
</style>
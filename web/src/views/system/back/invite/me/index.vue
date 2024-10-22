<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 30%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">我的邀请（营销）</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">患者姓名</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" placeholder="姓名/电话" 
                        @change="handle.search"
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">就诊日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="createTime" 
                        v-model="form.createTime"
                        value-format="YYYY-MM-DD"
                        type="date"
                        style="width: 60%;"
                        class="search_input"
                        @change="() => method.search()"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="handle.resetClick" style="width: 10vw; border-radius: 5px; height: 6vh !important;"  type="primary" plain>
                        重置
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
        <el-card style="width: 100%; height: 16%; border-radius: 15px; margin: 2% 0;">
            <div style="height: 100%; display: flex; align-items: center;">
                <el-button 
                    @click="() => method.switchMyInvite()"
                    link style="width: 10%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;"
                >
                    <span style="font-size: 1vw;">邀请历史</span>
                </el-button>
                <el-divider direction="vertical" style="height: 60%; border-width: 2px;" />
                <el-button 
                    @click="() => method.switchMyInviteCode()"
                    link style="width: 10%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;"
                >
                    <span style="font-size: 1vw;">邀请码</span>
                </el-button>
                <el-divider direction="vertical" style="height: 60%; border-width: 2px;" />
                <el-button 
                    @click="() => method.switchMeInviteClientPaymentRecord()"
                    link style="width: 10%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;"
                >
                    <span style="font-size: 1vw;">客户付款记录</span>
                </el-button>
                <el-divider direction="vertical" style="height: 60%; border-width: 2px;" />
                <el-button 
                    @click="() => method.switchMeInviteClientCumulativePaymentRecord()"
                    link style="width: 10%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;"
                >
                    <span style="font-size: 1vw;">客户付款累计</span>
                </el-button>
            </div>
        </el-card>
        <el-card v-if="currentModel == model.myInvite" style="height: 65%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                height="100%"
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
            >
                <el-table-column align="center" prop="createTimeStr" label="注册日期">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.createTimeStr }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertName(row.invitedUser.name) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="phone" label="手机号">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.invitedUser.phone }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="phone" label="邀请码">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.inviteCode }}</span>
                   </template>
                </el-table-column>
            </el-table>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
        <el-card v-else-if="currentModel == model.myInviteClientPaymentRecord" style="height: 65%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                height="100%"
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
            >
                <el-table-column align="center" prop="createTimeStr	" label="付款日期">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.createTimeStr }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertName(row.user.name) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="phone" label="手机号">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.user.phone }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="packageType" label="套餐">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ method.convertPackageType(row.packageType) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="money" label="金额">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.money }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="orderId" label="订单号"/>
            </el-table>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
        <el-card v-else-if="currentModel == model.myInviteClientCumulativePaymentRecord" style="height: 65%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                height="100%"
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
            >
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertName(row.user.name) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="phone" label="手机号">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.user.phone }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="money" label="累计金额">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.money }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="payNumber" label="付款次数">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.payNumber }}</span>
                   </template>
                </el-table-column>
            </el-table>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
        <el-card v-else-if="currentModel == model.myInviteCode" style="height: 65%; border-radius: 15px;">
            <div style="height: 100%; padding: 2%; display: flex;">
                <div style="height: 100%; width: 50%;">
                    <el-table 
                        :data="tableData" 
                        height="100%"
                        style="height: 90%; width: 100%; font-size: 1.5vh;" 
                        :row-style="{ height: '5vh', }" 
                        scrollbar-always-on
                    >
                        <el-table-column align="center" prop="inviteCode" label="邀请码">
                            <template #default="{ row }">
                                <div>
                                    <span style="font-weight: 700;">{{ row.inviteCode }}</span>
                                    <el-button link type="primary" size="small" @click="() => method.copy(row.inviteCode)" style="margin-left: 1vw;">
                                        <el-icon size="1vw"><DocumentCopy /></el-icon>
                                    </el-button>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column align="center" prop="createTimeStr" label="创建时间">
                            <template #default="{ row }">
                                <span style="font-weight: 700;">{{ convertDate(row.createTime) }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination
                        v-model:current-page="current"
                        v-model:page-size="size"
                        v-model:total="total"
                        :page-sizes="[10, 20, 50, 100, 500]"
                        @current-change="handle.pageCurrentChange"
                        @size-change="handle.pageCurrentChange"
                        style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                        background
                        layout="total, sizes, ->, prev, pager, next, jumper"
                        size="large"
                    />
                </div>
                <div style="width: 50%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                    <el-input v-model="inviteCode" style="width: 20vw; height: 5vh;" placeholder="">
                        <template #append>
                            <el-button @click="() => method.randomGenerationInviteCode()">随机生成</el-button>
                        </template>
                    </el-input>
                    <el-button type="primary" style="height: 5vh; width: 10vw; margin-top: 5vh;" @click="() => method.generateInviteCode()">创建邀请码</el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>
<script setup>
import { DocumentCopy } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { convertDate, convertName } from '@/utils/converter.js'
import { ElMessage } from 'element-plus'

const routerInstance = useRouter()

import { getInviteList as getInviteListAPI, getInviteCodeList as getInviteCodeListAPI, generateInvite as generateInviteAPI } from '@/api/user.api.js'
import { searchMeInviteClientCumulativePaymentRecord as searchMeInviteClientCumulativePaymentRecordAPI, searchMeInviteClientPaymentRecord as searchMeInviteClientPaymentRecordAPI } from '@/api/back.api.js'

const formRef = ref()

const form = reactive({})
const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

const model = {
    myInvite: 'myInvite',
    myInviteCode: 'myInviteCode',
    myInviteClientPaymentRecord: 'myInviteClientPaymentRecord',
    myInviteClientCumulativePaymentRecord: 'myInviteClientCumulativePaymentRecord',
}

const currentModel = ref('myInvite')

const inviteCode = ref()

watch(
    () => form.val,
    () => {
        method.search()
    }
)

const method = {
    setPage(page) {
        current.value =parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {
        switch (currentModel.value) {
            case model.myInvite:
                method.searchMyInvite()
                break
            case model.myInviteClientPaymentRecord:
                method.searchMeInviteClientPaymentRecord()
                break
            case model.myInviteClientCumulativePaymentRecord:
                method.searchMeInviteClientCumulativePaymentRecord()
                break
        }
    },
    switchMyInvite() {
        this.searchMyInvite(() => currentModel.value = model.myInvite)
    },
    searchMyInvite(callback) {
        getInviteListAPI({ ...this.getPage(), val: form.val ,createTime: form.createTime ? new Date(form.createTime).getTime() : null }, res => {
            tableData.value = res.records
            total.value = res.total
            if(callback) callback()
        })
    },
    switchMyInviteCode() {
        this.searchMeInviteCodeList(() => currentModel.value = model.myInviteCode)
    },
    searchMeInviteCodeList(callback) {
        getInviteCodeListAPI({ ...this.getPage() }, (response) => {
            tableData.value = response.records
            this.setPage(response)
            if(callback) callback()
        })
    },

    switchMeInviteClientPaymentRecord() {
        this.searchMeInviteClientPaymentRecord(() => currentModel.value = model.myInviteClientPaymentRecord)
    },
    searchMeInviteClientPaymentRecord(callback) {
        searchMeInviteClientPaymentRecordAPI({ ...this.getPage(), val: form.val ,createTime: form.createTime ? new Date(form.createTime).getTime() : null }, (response) => {
            tableData.value = response.records
            this.setPage(response)
            if(callback) callback()
        })
    },
    switchMeInviteClientCumulativePaymentRecord() {
        this.searchMeInviteClientCumulativePaymentRecord(() => currentModel.value = model.myInviteClientCumulativePaymentRecord)
    },
    searchMeInviteClientCumulativePaymentRecord(callback) {
        searchMeInviteClientCumulativePaymentRecordAPI({ ...this.getPage(), val: form.val, createTime: form.createTime ? new Date(form.createTime).getTime() : null }, (response) => {
            tableData.value = response.records
            this.setPage(response)
            if(callback) callback()
        })
    },
    convertPackageType(packageType) {
        switch(packageType) {
            case 1:
                return '月付'
            case 2:
                return '季付'
            case 3:
                return '年付'
            default:
                return '其他'
        }
    },
    generateInviteCode() {
        if(inviteCode.value) {
            generateInviteAPI(inviteCode.value, () => {
                method.searchMeInviteCodeList()
            })
        }
    },
    randomGenerationInviteCode() {
        let randomNum = Math.floor(100000000 * Math.random());
        inviteCode.value = randomNum.toString().padStart(8, '0');
    },
    copy(text) {
        // 添加一个input元素放置需要的文本内容
        const input = document.createElement("input");
        input.value = text;
        document.body.appendChild(input);
        // 选中并复制文本到剪切板
        input.select();
        document.execCommand("copy");
        // 移除input元素
        document.body.removeChild(input);
        ElMessage({
            message: "复制成功",
            type: "success",
        });
    }
}

const handle = {
    searchClick() {
        method.search()
    },
    resetClick() {
        formRef.value.resetFields()
        form.createTime = null
        form.val = null
        method.search()
    },
    pageCurrentChange() {
        method.search()
    },
}

onMounted(() => {
    let routerQuery = routerInstance.currentRoute.value.query
    if(routerQuery && routerQuery.val) {
        form.val = routerQuery.val
    }
    method.search()
})
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
    width: 100%;
}
:deep(.el-table__header) {
    height: 6vh;
    background-color: #6a5acd;
}
:deep(.el-table__header .cell) {
    font-weight: 700;
}
:deep(.el-form-item) {
    margin-bottom: 0;
}
</style>
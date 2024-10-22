<template>
    <SelectedAccountView ref="selectedAccountViewRef" @selected="selectedAccount" @add="showAddAccounDialog" />
    <el-dialog v-model="dialogAddAccountVisible" width="60%" style="max-height: 900px" align-center>
        <AddAccountView @add="addAccounHandle" />
    </el-dialog>
</template>
<script setup>
import { ref, defineEmits } from 'vue'
import SelectedAccountView from '@/views/system/financial/certificate/add/selectedAccount.vue';
import AddAccountView from '@/views/system/financial/certificate/add/addAccount.vue';

const event = defineEmits(["select"])

const dialogAddAccountVisible = ref(false)

const selectedAccountViewRef = ref()

// 选择科目
function selectedAccount(item) {
    event("select", item)
}

// 关闭科目查询弹窗，打开新增科目弹窗
function showAddAccounDialog() {
    dialogAddAccountVisible.value = true
}

// 新增科目后刷新科目列表
function addAccounHandle() {
    dialogAddAccountVisible.value = false
    selectedAccountViewRef.value.search()
}
</script>
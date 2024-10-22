<template>
    <el-tabs type="border-card">
        <el-tab-pane label="待收费">
            <Undone />
        </el-tab-pane>
        <el-tab-pane label="收费列表">
            <Pay />
        </el-tab-pane>
        <!-- <el-tab-pane label="退费列表">
            <Back />
        </el-tab-pane> -->
    </el-tabs>
</template>
<script setup>
import Undone from '@/views/system/clinic/pay/components/undone/index.vue'
import Pay from '@/views/system/clinic/pay/components/pay/index.vue'
// import Back from '@/views/system/clinic/pay/components/back/index.vue'
</script>
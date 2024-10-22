<template>
    <!-- 病人信息卡片 -->
    <PatientCard  />

    <!-- 部分病历卡片 -->
    <CaseCard />
</template>
<script setup>
import CaseCard from '@/views/system/clinic/case/components/card.vue'
import PatientCard from '@/views/system/clinic/patient/card.vue'
</script>
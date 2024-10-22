<template>
    <!-- currentTab 改变时组件也改变 -->
    <component :is="systemComponent"></component>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue';
import { system } from '@/stores/system.js'

const systemComponent = ref()

onBeforeMount(() => {
    let currentSystem = system.currentSystem()
    if(currentSystem != null) {
        systemComponent.value = currentSystem.component
        system.addSystemCutEventHandle((index, system) => {
            systemComponent.value = system.component
        })
    }
})
</script>
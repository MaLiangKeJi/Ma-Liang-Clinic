<template>
  <div style="width: 100%; height: 100%; display: flex; justify-content: space-between; background-color: #ffffff; border-bottom: 1px solid #e7e7e7;">
    <div style="height: 100%; line-height: 60px; width: 300px; text-align: center; display: flex; justify-content: center; align-items: center">
      <el-icon style="font-size: 30px; margin-right: 10px;"><OfficeBuilding /></el-icon>
      <div>{{ loginUserStore.getCompany().company.name }}</div>
    </div>
    <div style="height: 100%; width: 60px; display: flex; flex-direction: column; justify-content: center;">
      <el-popover
        placement="bottom-start"
        :width="200"
        trigger="hover"
        style="padding: 10px;"
      >
        <template #reference>
          <el-icon style="font-size: 30px;"><Setting /></el-icon>
        </template>
        <div @click="cutSystem" class="item">
          <div>切换系统</div>
          <el-icon style="font-size: 28px;"><Menu /></el-icon>
        </div>
        <el-divider style="margin: 0;" />
        <div @click="logout" class="item">
          <div>退出登录</div>
          <MaterialSymbolsLightExitToAppIcon />
        </div>
      </el-popover>
    </div>
  </div>
</template>

<script setup>
import { loginUserStore } from '@/stores/UserStore'
import MaterialSymbolsLightExitToAppIcon from './MaterialSymbolsLightExitToApp.vue'

import {
  Setting,
  Menu,
  OfficeBuilding,
} from '@element-plus/icons-vue'


function logout() {
  loginUserStore.logout()
}
function cutSystem() {
  loginUserStore.loginFlag = 2
}
</script>

<style scoped>
.item {
  height: 60px; line-height: 60px; display: flex; justify-content: space-around; align-items: center;
}
.item:hover {
  color: #438ee4;
  cursor: pointer;
}
</style>

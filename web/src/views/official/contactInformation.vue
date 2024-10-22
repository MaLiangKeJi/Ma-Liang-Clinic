<template>
  <div style="height: 80%;">
    <div style="height: 80%; width: 100%; display: flex;">
      <div style="height: 100%; width: 50%; display: flex; flex-direction: column; justify-content: center; align-items: flex-end;">
        <div style="height: 30vh;">
          <el-image :src="Qrcode" fit="contain" />
        </div>
      </div>
      <div style="height: 100%; width: 50%; display: flex; flex-direction: column; justify-content: center;">
        <div style="margin-bottom: 2vh;">
          <el-text class="mx-1" style="font-size: 0.8vw;">更多联系方式</el-text>
        </div>
        <div style="margin: 1vh 0; display: flex; align-items: center;">
          <el-icon size="1.2vw" style="margin-right: 1vw;"><Message /></el-icon>
          <el-text class="mx-1" style="font-size: 0.8vw;">邮箱: message@maliang.work</el-text>
        </div>
        <div style="margin: 1vh 0; display: flex; align-items: center;">
          <el-icon size="1.2vw" style="margin-right: 1vw;"><Iphone /></el-icon>
          <el-text class="mx-1" style="font-size: 0.8vw;">电话: 15993075857</el-text>
        </div>
      </div>
    </div>
    <div style="height: 20%;">
      <div class="partitions">
        <el-divider class="partitions-divider" />
        <div style="display: flex; align-items: center;">
          <el-icon size="1.5vw" style="margin-right: 1vw;"><LocationInformation /></el-icon>
          <el-text class="mx-1" style="font-size: 1vw;">位置：河南省济源市高新区综合服务区3-4号楼C07</el-text>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Qrcode from '@/static/qrcode3.png'
</script>

<style scoped>
.menu {
  background-color: #282c34;
  color: white;
}
.card {
  margin: 15px 0;
}
h2, h3 {
  color: #333;
}
.partitions {
  margin: 5% 0; 
  display: flex; 
  justify-content: center; 
  flex-direction: column; 
  align-items: center;
  .partitions-divider {
      width: 20vw; 
      border: 2px solid #6b5acc;
  }
  .partitions-subtitle {
      font-size: 0.6vw;
  }
}
</style>
  
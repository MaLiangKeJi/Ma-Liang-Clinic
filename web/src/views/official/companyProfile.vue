<template>
    <el-container>
      <!-- 公司介绍 -->
      <el-main>
        <div style="width: 100%; min-height: 50vh; margin-bottom: 10vh; display: flex; flex-direction: column; justify-content: center; text-align: center;">
            <div class="partitions">
              <el-text class="mx-1 partitions-title">公司简介</el-text>
              <el-divider class="partitions-divider" />
            </div>
            <p style="padding-left: 5%; font-size: 0.8vw;">
                河南省码良互联网科技有限公司成立于 2024 年 3 月 27 日，是一家专注于为中小型医疗机构提供智能办公系统的科技公司。
            </p>
            <p style="font-size: 0.8vw;">
              公司自成立以来，已与多家中小型医疗机构（含诊所、卫生室、卫生院等）建立了合作关系，致力于为其提供高效、智能化的办公解决方案。公司核心产品“码良诊所系统”在帮助这些机构提升运营效率和服务质量方面表现优异。
            </p>
        </div>
  
        <div style="width: 100%; min-height: 50vh; margin: 10vh 0; display: flex; flex-direction: column; justify-content: center; align-items: center;">
          <div class="partitions">
            <el-text class="mx-1 partitions-title">公司历程</el-text>
            <el-divider class="partitions-divider" />
          </div>
          <el-steps align-center :active="5">
            <el-step title="2024 年 3 月"> 
              <template #description>
                <div style="padding: 0 20%;">
                  <el-image style="height: 100px" :src="LogoPNG" fit="contain" />
                </div>
                <el-text class="mx-1">公司正式成立</el-text>
              </template>
            </el-step>
            <el-step title="2024 年 8 月"> 
              <template #description>
                <div style="padding: 0 20%;">
                  <el-image style="height: 100px" :src="LogoPNG3" fit="contain" />
                </div>
                <el-text class="mx-1">码良云诊所上线！</el-text>
              </template>
            </el-step>
            <el-step title="2024 年 8 月"> 
              <template #description>
                <div style="padding: 0 20%;">
                  <el-image style="height: 100px" :src="LogoPNG1" fit="contain" />
                </div>
                <el-text class="mx-1">全国创新创业大赛，荣获市级二等奖</el-text>
              </template>
            </el-step>
            <el-step title="2024 年 8 月"> 
              <template #description>
                <div style="padding: 0 20%;">
                  <el-image style="height: 100px" :src="LogoPNG4" fit="contain" />
                </div>
                <el-text class="mx-1">首家诊所开始使用！</el-text>
              </template>
            </el-step>
            <el-step title="2024 年 9 月"> 
              <template #description>
                <div style="padding: 0 20%; width: 20vw; ">
                  <el-image style="height: 100px" :src="LogoPNG2" fit="contain" />
                </div>
                <el-text class="mx-1">省赛前 50 强，成功进入国赛</el-text>
              </template>
            </el-step>
          </el-steps>
        </div>
  
        <!-- 核心团队介绍 -->
        <div style="width: 100%; min-height: 50vh; margin: 10vh 0; display: flex; flex-direction: column; justify-content: center; text-align: center;">
            <div class="partitions">
              <el-text class="mx-1 partitions-title">核心团队</el-text>
              <el-divider class="partitions-divider" />
            </div>
            <div style="display: flex; justify-content: center;">
              <el-card style="height: 30vh; width: 15vw; margin: 5vw;">
                <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center;">
                  <el-image style="height: 100px" :src="LogoPNG5" fit="contain" />
                  <el-text class="mx-1" style="font-size: 1vw;">陈阳 - CEO</el-text>
                  <el-text class="mx-1" type="info" style="font-size: 1vw;">公司创始人兼首席执行官，负责公司整体战略规划和业务拓展</el-text>
                </div>
              </el-card>
              <el-card style="height: 30vh; width: 15vw; margin: 5vw;">
                <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center;">
                  <el-image style="height: 100px" :src="LogoPNG6" fit="contain" />
                  <el-text class="mx-1" style="font-size: 1vw;">路晨霖 - 技术负责人</el-text>
                  <el-text class="mx-1" type="info" style="font-size: 1vw;">公司首席技术官，负责公司核心产品的技术研发与系统架构设计</el-text>
                </div>
              </el-card>
              <el-card style="height: 30vh; width: 15vw; margin: 5vw;">
                <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center;">
                  <el-image style="height: 100px" :src="LogoPNG7" fit="contain" />
                  <el-text class="mx-1" style="font-size: 1vw;">卫梦瑶 - 高级研发工程师</el-text>
                  <el-text class="mx-1" type="info" style="font-size: 1vw;">资深工程师，负责核心模块的开发与优化</el-text>
                </div>
              </el-card>
              <el-card style="height: 30vh; width: 15vw; margin: 5vw;">
                <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center;">
                  <el-image style="height: 100px" :src="LogoPNG8" fit="contain" />
                  <el-text class="mx-1" style="font-size: 1vw;">刘俊杰 - 高级研发工程师</el-text>
                  <el-text class="mx-1" type="info" style="font-size: 1vw;">资深开发人员，主要负责系统稳定性和新功能的研发。</el-text>
                </div>
              </el-card>
            </div>
        </div>
      </el-main>
    </el-container>
</template>
<script setup>
import LogoPNG from '@/static/vx_20240926145919.jpg'
import LogoPNG1 from '@/static/vx_20240926145900.jpg'
import LogoPNG2 from '@/static/vx_20240926145909.jpg'
import LogoPNG3 from '@/static/work.png'
import LogoPNG4 from '@/static/08c1c9f8611a7a1915aa05f05750175.jpg'
import LogoPNG5 from '@/static/icon1.png'
import LogoPNG6 from '@/static/icon2.png'
import LogoPNG7 from '@/static/icon3.png'
import LogoPNG8 from '@/static/icon4.png'
</script>

<style scoped>
h1 {
text-align: center;
margin-top: 20px;
}

h2 {
margin-top: 10px;
font-size: 22px;
}

h3 {
font-size: 18px;
margin-bottom: 10px;
}

.box-card {
margin-bottom: 20px;
}

.timeline-image {
  
}
:deep(.el-step__description) {
  width: 100%;
  display: flex; 
  flex-direction: column; 
  justify-content: center; 
  align-items: center;
}
.partitions {
  height: 20%; 
  margin: 5% 0; 
  display: flex; 
  justify-content: center; 
  flex-direction: column; 
  align-items: center;
  .partitions-title {
      font-size: 1vw;
  }
  .partitions-divider {
      width: 10vw; 
      border: 3px solid #6b5acc;
  }
  .partitions-subtitle {
      font-size: 0.8vw;
  }
}
</style>
  
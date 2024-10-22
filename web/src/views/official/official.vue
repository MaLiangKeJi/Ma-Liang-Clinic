<template>
    <div class="first">
        <div class="title">
            <div class="title-text">
                <el-text class="mx-1 title-text-title">
                    免费、高效、智能的诊所系统
                </el-text>
                <div>
                    <el-text class="mx-1 title-text-subtitle">无需手写病例</el-text>
                    <el-divider direction="vertical" />
                    <el-text class="mx-1 title-text-subtitle">接诊药房分离</el-text>
                    <el-divider direction="vertical" />
                    <el-text class="mx-1 title-text-subtitle">消毒消杀</el-text>
                </div>
            </div>
            <el-button @click="() => systemSwitch.selectAndToClinicSystem()" round class="title-entrance-btn">
                <span>免费使用</span>
            </el-button>
        </div>
        <div class="demo">
            <div class="demo-png">
                <el-image :src="Demonstration" fit="contain"></el-image>
            </div>
        </div>
    </div>
    <div class="partitions">
        <el-text class="mx-1 partitions-title">更实用</el-text>
        <el-divider class="partitions-divider" />
        <el-text class="mx-1 partitions-subtitle" type="info">根据实际诊断流程设计</el-text>
    </div>
    <div style="height: 90vh; width: 100%; display: flex; padding: 0 15vw; margin: 10vh 0;">
        <div style="width: 48%; height: 100%; display: flex; align-items: center; justify-content: center;">
            <div style="margin-bottom: 10vh; color: #000;">
                <div style="display: flex; align-items: center;">
                    <el-icon size="3vw" color="#6b5acc" style="margin-right: 1vw"><Clock /></el-icon>
                    <div style="display: flex; align-items: center; width: 90%;">
                        <el-text style="font-size: 1.5vw;">时间顺序，查看记录</el-text>
                    </div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <el-text style="font-size: 1.2vw; font-weight: 700; margin-right: 0.5vw;">查</el-text>
                    <el-text style="font-size: 1vw;">挂号、接诊</el-text>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <el-text style="font-size: 1.2vw; font-weight: 700; margin-right: 0.5vw;">查</el-text>
                    <el-text style="font-size: 1vw;">病例、处方</el-text>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <el-text style="font-size: 1.2vw; font-weight: 700; margin-right: 0.5vw;">查</el-text>
                    <el-text style="font-size: 1vw;">消毒、消杀</el-text>
                </div>
            </div>
        </div>
        <div style="width: 52%; height: 100%; display: flex; align-items: center;">
            <el-timeline style="width: 90%; padding-left: 1vw; display: flex; flex-direction: column; justify-content: center;">
                <el-timeline-item center placement="top" :hide-timestamp="true" type="primary" 
                    v-for="(operationLog, index) in operationLogs"  :key="index"
                >
                    <el-card
                        v-if="!operationLog.isFill"
                        style="width: 90%; height: 6vh; cursor: pointer;" 
                        shadow="always"
                        :style="{ height: operationLog.isOpen ? '60vh' : '8vh' }"
                    >
                        <template v-if="operationLog.isOpen" #header>
                            <div style="height: 100%; min-height: 60px; width: 100%; padding-left: 20px; display: flex; justify-content: space-between; align-items: center;">
                                <div style="font-weight: 700;">
                                    <div class="timestamp_time" style="font-size: 1.4vw;">{{ operationLog.createHMS }}</div>
                                    <el-text class="mx-1" type="info" style="font-size: 1vw;">{{ operationLog.createYMD }}</el-text>
                                </div>
                                <div style="width: 20%; text-align: center; font-size: 1vw;">
                                    接诊病人
                                </div>
                                <div style="width: 40%; font-size: 1vw;">
                                    <span style="margin: 0 0.5vw;">李二</span>
                                    <span style="margin: 0 0.5vw;">24岁</span>
                                    <span style="margin: 0 0.5vw;">男</span>
                                </div>
                            </div>
                        </template>
                        <div v-if="operationLog.isOpen" style="height: 100%;">
                            <div style="width: 100%; font-size: 0.8vw; padding-left: 2vw">
                                <div style="margin: 1vw 0;">
                                    <span style="margin-right: 1vw;">主诉:</span>
                                    <span>{{ operationLog.chiefComplaint }}</span>
                                </div>
                                <div style="margin: 1vw 0;">
                                    <span style="margin-right: 1vw;">查体:</span>
                                    <span>{{ operationLog.checkup }}</span>
                                </div>
                                <div style="margin: 1vw 0;">
                                    <span style="margin-right: 1vw;">诊断:</span>
                                    <span>{{ operationLog.diagnosis }}</span>
                                </div>
                            </div>
                            <el-divider border-style="dashed" />
                            <el-descriptions :column="1" style="padding-left: 20px;" />
                            <div 
                                v-if="operationLog.prescriptionDrugs && operationLog.prescriptionDrugs.length > 0 " 
                                v-for="(drug, index) in operationLog.prescriptionDrugs"  :key="index" 
                                style="width: 100%; margin: 1vw 0 1vw 2vw; font-size: 0.8vw; display: flex; align-items: center;" 
                            >
                                <div style="width: 30%;">
                                    <el-text class="mx-1" style="font-size: 0.8vw;">{{ index+1}}.</el-text>
                                    <el-text class="mx-1" style="font-size: 0.8vw; margin-left: 0.5vw;">{{ drug.text }}</el-text>
                                </div>
                                <el-text class="mx-1" type="info" style="font-size: 0.8vw; margin-left: 2vw;">{{ drug.text2 }}</el-text>
                            </div>
                            <div v-else style="width: 100%; padding-left: 20px; display: flex;">
                                无处方
                            </div>
                        </div>
                        <div v-else class="content" style="height: 100%; display: flex; justify-content: space-between; align-items: center;">
                            <div class="timestamp" style="width: 20%; padding-left: 1vw">
                                <span class="timestamp_time">{{ operationLog.createHMS }}</span>
                                <el-text class="mx-1 timestamp_date" type="info">{{ operationLog.isCurrentDay ? '今天' : operationLog.createYMD }}</el-text>
                            </div>
                            <span class="info_title" style="font-size: 1vw;">{{ operationLog.service }}</span>
                            <el-text class="mx-1" type="info" style="font-size: 1vw;">{{ operationLog.about }}</el-text>
                            <el-divider direction="vertical" style="margin: 0 20px; height: 0;" />
                        </div>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
        </div>
    </div>
    <div class="partitions">
        <el-text class="mx-1 partitions-title">更方便</el-text>
        <el-divider class="partitions-divider" />
        <el-text class="mx-1 partitions-subtitle" type="info">只要鼠标点点，不手写</el-text>
    </div>
    <div style="height: 80%; padding: 0 10%; border-radius: 20px; display: flex; justify-content: space-around;">
        <div style="width: 20vw; height: 50vh; transition: transform 0.2s;" :style="{ 'transform': mouseHover == 0 ? 'translateY(-10px)' : '' }" @mouseenter="mouseHover = 0" @mouseleave="mouseAway">
            <el-card  style="width: 20vw; height: 50vh; border-radius: 15px; border: 3px solid #dde0e6; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" shadow="hover">
                <div style="height: 100%;">
                    <div style="height: 70%; display: flex; justify-content: center;">
                        <el-timeline style="width: 90%; padding-left: 1vw; display: flex; flex-direction: column; justify-content: center;">
                            <el-timeline-item center placement="top" :hide-timestamp="true" type="primary" 
                                v-for="(operationLog, index) in operationLogs"  :key="index"
                            >
                                <el-card
                                    v-if="!operationLog.isFill"
                                    style="width: 90%; height: 5vh; cursor: pointer;" 
                                    shadow="always"
                                >
                                    <div v-if="!operationLog.isOpen" class="content" style="height: 100%; padding-left: 20px; display: flex;">
                                        <div class="timestamp">
                                            <div class="timestamp_time">{{ operationLog.createHMS }}</div>
                                            <el-text class="mx-1 timestamp_date" type="info">{{ operationLog.isCurrentDay ? '今天' : operationLog.createYMD }}</el-text>
                                        </div>
                                        <div class="info" style="height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: flex-start;">
                                            <span class="info_title" style="font-size: 1vw; ">{{ operationLog.service }}</span>
                                        </div>
                                        <el-divider direction="vertical" style="margin: 0 20px; height: 0;" />
                                    </div>
                                </el-card>
                            </el-timeline-item>
                        </el-timeline>
                    </div>
                    <div style="height: 30%; text-align: center; display: flex; flex-direction: column;">
                        <el-text style="font-size: 1.5vw;" class="mx-1">病人档案</el-text>
                        <el-text style="font-size: 1vw;" class="mx-1" type="info">快速查询、便于更新、提高效率</el-text>
                    </div>
                </div>
                <template #footer>
                    <el-button @click="() => systemSwitch.selectAndToClinicSystem()" class="footer_btn" text style="height: 100%; width: 100%;">
                        <div style="width: 100%; text-align: center; font-size: 1vw;">了解更多</div>
                    </el-button>
                </template>
            </el-card>
        </div>
        <div style="width: 20vw; height: 50vh; transition: transform 0.2s;" :style="{ 'transform': mouseHover == 1 ? 'translateY(-10px)' : '' }" @mouseenter="mouseHover = 1" @mouseleave="mouseAway">
            <el-card  style="width: 20vw; height: 50vh; border-radius: 15px; border: 3px solid #dde0e6; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" shadow="hover">
                <div style="height: 100%;">
                    <div style="height: 70%; display: flex; flex-direction: column; justify-content: center;">
                        <el-image :src="caseKeywordRecommendation"></el-image>
                    </div>
                    <div style="height: 30%; text-align: center; display: flex; flex-direction: column;">
                        <el-text style="font-size: 1.5vw;" class="mx-1">病例、处方</el-text>
                        <el-text style="font-size: 1vw;" class="mx-1" type="info">一键推荐，全程鼠标</el-text>
                    </div>
                </div>
                <template #footer>
                    <el-button @click="() => systemSwitch.selectAndToClinicSystem()" class="footer_btn" text style="height: 100%; width: 100%;">
                        <div style="width: 100%; text-align: center; font-size: 1vw;">了解更多</div>
                    </el-button>
                </template>
            </el-card>
        </div>
        <div style="width: 20vw; height: 50vh; transition: transform 0.2s;" :style="{ 'transform': mouseHover == 2 ? 'translateY(-10px)' : '' }" @mouseenter="mouseHover = 2" @mouseleave="mouseAway">
            <el-card  style="width: 20vw; height: 50vh; border-radius: 15px; border: 3px solid #dde0e6; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" shadow="hover">
                <div style="height: 100%;">
                    <div ref="chartContainer" style="height: 70%;" />
                    <div style="height: 30%; text-align: center; display: flex; flex-direction: column;">
                        <el-text style="font-size: 1.5vw;" class="mx-1">库存统计</el-text>
                        <el-text style="font-size: 1vw;" class="mx-1" type="info">数据转图表，一眼看懂</el-text>
                    </div>
                </div>
                <template #footer>
                    <el-button @click="() => systemSwitch.selectAndToClinicSystem()" class="footer_btn" text style="height: 100%; width: 100%;">
                        <div style="width: 100%; text-align: center; font-size: 1vw;">了解更多</div>
                    </el-button>
                </template>
            </el-card>
        </div>
    </div>
    <div style="height: 80vh; width: 100%; display: flex; padding: 0 10vw; ">
        <div style="width: 50%; height: 100%;">
            <el-carousel height="auto" :autoplay="false" style="height: 100%;">
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div style="width: 50%; height: 100%; display: flex; align-items: center; justify-content: center;">
            <div style="margin-bottom: 10vh;">
                <div style="margin: 5vh 0; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 5vw; font-size: 1.5vw; margin-right: 2vw;">实用</div>
                    <span style="font-size: 1vw;"></span>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 6vw; font-size: 1.2vw; margin-right: 1vw; font-weight: 700;">接诊记录</div>
                    <div style="font-size: 1vw;">查询更方便</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 6vw; font-size: 1.2vw; margin-right: 1vw; font-weight: 700;">病人档案</div>
                    <div style="font-size: 1vw;">查历史、病例、处方</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 6vw; font-size: 1.2vw; margin-right: 1vw; font-weight: 700;">消毒、消杀</div>
                    <div style="font-size: 1vw;">一键完成，无需手写</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 6vw; font-size: 1.2vw; margin-right: 1vw; font-weight: 700;">库存管理</div>
                    <div style="font-size: 1vw;">管理更简单，自动提醒过期、补货</div>
                </div>
            </div>
        </div>
    </div>
    <div style="height: 80vh; width: 100%; display: flex; padding: 0 10vw; ">
        <div style="width: 50%; height: 100%;">
            <el-carousel height="auto" :autoplay="false" style="height: 100%;">
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
                <el-carousel-item>
                    <el-image :src="image_1" fit="contain"></el-image>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div style="width: 50%; height: 100%; display: flex; align-items: center; justify-content: center;">
            <div style="margin-bottom: 10vh;">
                <div style="margin: 5vh 0; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="width: 5vw; font-size: 1.5vw; margin-right: 2vw;">易上手</div>
                    <span style="font-size: 1vw;"></span>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="font-size: 1vw;">比手写，更方便！</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="font-size: 1vw;">比 Excel 表格，更简单！</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="font-size: 1vw;">拼音、首字母、常用名搜索</div>
                </div>
                <div style="margin: 5vh 0 5vh 7vw; border-bottom: 2px solid #dde0e6; display: flex; align-items: center;">
                    <div style="font-size: 1vw;">管理更简单，自动提醒过期、补货</div>
                </div>
            </div>
        </div>
    </div>
    <div style="height: 20vh; width: 100%; display: flex; padding: 0 10vw;">
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Document /></el-icon>
            <span>门诊日志</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Avatar /></el-icon>
            <span>病人档案</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Coin /></el-icon>
            <span>收费计费</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><ShoppingCart /></el-icon>
            <span>药品零售</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><MessageBox /></el-icon>
            <span>库存管理</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Brush /></el-icon>
            <span>消杀</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Timer /></el-icon>
            <span>消毒</span>
        </div>
        <div style="width: 10vw; height: 20vh; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <el-icon size="5vh"><Stamp /></el-icon>
            <span>诊断证明</span>
        </div>
    </div>
    <el-footer style="height: 10%;">
        <div style="text-align: center; height: 30px; line-height: 50px;">
            <el-text class="mx-1" type="info">Copyright © 2024 码良科技（河南省码良互联网科技有限公司）</el-text>
            <el-text class="mx-1" type="info">  联系方式：176-3486-0778</el-text>
        </div>
        <div style="text-align: center; height: 30px; line-height: 50px;">
            <el-text class="mx-1" type="info">  备案号：<a href="https://beian.miit.gov.cn/#/Integrated/index">豫 ICP 备 2024079709 号 - 1</a></el-text>
        </div>
    </el-footer>
</template>
<script setup>
import { ref, onMounted, inject } from 'vue'
import Demonstration from '@/static/demonstration.png'
import caseKeywordRecommendation from '@/static/1725539223684.jpg';
import image_1 from '@/static/1725604694244.jpg';


import { useRouter } from 'vue-router'
const routerInstance = useRouter()

import OfficialView from '@/views/official/official.vue'
import PriceView from '@/views/official/price.vue'

const currentModel = ref('official')
function toOfficial() {
    currentModel.value = 'official'
}
function toForum() {
    window.location.href = 'https://forum.maliang.work/'
}
function toPrice() {
    currentModel.value = 'price'
}
import { systemSwitch, loginState } from '@/stores/app.js'

const Echarts = inject('echarts');


const mouseHover = ref(null)
function mouseAway() {
    mouseHover.value = null;
}

const operationLogs = [
    {
        "createTime":"2024-08-29T09:53:23.000+00:00",
        "createYMD":"2024-08-29",
        "createHMS":"13:24:03",
        "isCurrentDay":false,
        "service":"就诊",
        "about":"接诊病人",
    },
    {
        "createTime":"2024-08-29T09:53:23.000+00:00",
        "createYMD":"2024-08-29",
        "createHMS":"14:06:33",
        "isCurrentDay":false,
        "service":"零售",
        "about":"售卖药品",
    },
    {
        "createTime":"2024-08-29T09:53:23.000+00:00",
        "createYMD":"2024-08-29",
        "createHMS":"14:08:21",
        "isCurrentDay":false,
        "service":"接诊",
        "about":"接诊病人",
    },
    {
        isOpen: true,
        "createTime":"2024-08-29T09:53:23.000+00:00",
        "createYMD":"2024-08-29",
        "createHMS":"14:32:26",
        "isCurrentDay":false,
        "service":"接诊",
        "about":"接诊病人",
        chiefComplaint: '咳嗽，有痰，持续一周',
        checkup: '咽部两侧红肿，左侧扁桃体可见红肿',
        diagnosis: '急性咽炎',
        prescriptionDrugs: [
            {
                text: '阿奇霉素分散片(0.5g)',
                text2: '1 天三次 共 3 天',
            },
            {
                text: '布洛芬片',
                text2: '1 天两次 共 3 天',
            },
            {
                text: '美洛昔康',
                text2: '1 天两次 共 6 天',
            },
        ]
    },
]

const chartContainer = ref()
onMounted(() => {
    let charts = Echarts.init(chartContainer.value);
    charts.setOption({
        tooltip: {
            trigger: 'item'
        },
        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: [
                    { value: 1048, name: '阿莫西林胶囊' },
                    { value: 735, name: '醋酸泼尼松片' },
                    { value: 580, name: '布洛芬片' },
                    { value: 484, name: '奥美拉唑肠溶片' },
                    { value: 300, name: '双氯芬酸钠缓释胶囊' }
                ],
                emphasis: {
                    itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    });
    // 根据页面大小自动响应图表大小
    window.addEventListener("resize", () => charts.resize());
})
</script>
<style lang="scss" scoped>
.container {
    height: 100%; 
    background-color: #fff;
}
:deep(.el-header) {
    padding: 0;
}

.flex-grow {
  flex-grow: 1;
}
.inform {
    background-color: #000; 
    color: #ffffff;
    text-align: center;
    font-size: 1.4;
    line-height: 60px;
}
.text-linear {
  background-image: linear-gradient(to right, #8781ff, #00ff95);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
:deep(.el-main) {
    height: 100%;
}
:deep(.el-carousel__container) {
  height: 100%;
  text-align: center;
}
.el-menu--horizontal > .el-menu-item:nth-child(1) {
    margin-right: auto;
}
:deep(.el-scrollbar__view) {
    height: 100%;
}
:deep(.el-card__body) {
    height: 90%;
    padding: 0;
}   
:deep(.el-card__footer) {
    height: 10%;
    padding: 0;
}
:deep(.footer_btn.el-button) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}
:deep(.footer_btn.el-button:active) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}
:deep(.footer_btn.el-button:hover) {
    background-color: #8781ff;
    color: #fff;
    font-size: 1.5em;
}
:deep(.first-video .el-card__body) {
    height: 100%;
    padding: 0;
}
.first {
    height: 60%;
    width: 100%;

    background-image: url('../static/pexels-pixabay-40568.jpg');
    /* 背景图垂直、水平均居中 */
    background-position: center center;
    /* 背景图不平铺 */
    background-repeat: no-repeat;
    /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
    background-attachment: fixed;
    /* 让背景图基于容器大小伸缩 */
    background-size: cover;

    border-radius: 0 0 80% 80%;
}
.first-body {
    height: 100%;
    width: 100%;

    background-color: rgba(255, 255, 255, 0.25);
    backdrop-filter: blur(6px);
    -webkit-backdrop-filter: blur(6px);
    border: 0.666667px solid rgba(255, 255, 255, 0.18);
    box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
    -webkit-box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
    border-radius: 12px;
    -webkit-border-radius: 12px;
    color: rgba(255, 255, 255, 0.75);

    border-radius: 0 0 80% 80%;
}
:deep(.el-carousel__item) {
    padding: 0 !important;
}
.timestamp {
    height: 100%;
    width: 60%; 
    display: flex; 
    flex-direction: column;
    align-items: flex-start;
    flex-wrap: wrap;
    height: 5vh;
}
.timestamp_time {
    font-size: 1vw; 
    height: 60% !important;
    width: min-content;
}
.timestamp_date {
    font-size: 0.6vw; 
    height: 40% !important;
    width: 100%;
}



.slide-fade-enter-active {
    transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
    transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from, .slide-fade-leave-to {
    transform: translateX(20px);
    opacity: 0;
}
:deep(.el-carousel__container) {
    height: 100% !important;
}
:deep(.el-carousel__item) {
    height: 100%;
}
:deep(.el-image) {
    height: 100%;
}
.first {
    height: 60vh;
    width: 100%;
    padding: 0 20vw; 
    display: flex;
    align-items: center; 
    .title {
        display: flex; 
        flex-direction: column; 
        justify-content: space-around; 
        align-items: center; 
        height: 30%;
        width: 50vw;
        .title-text {
            display: flex; 
            flex-direction: column; 
            justify-content: center; 
            align-items: center; 
            .title-text-title {
                text-align: center; 
                font-size: 1.5vw; 
                font-weight: 700; 
                color: #000;
            }
            .title-text-subtitle {
                font-size: 0.8vw;
            }
        }
        .title-entrance-btn {
            background-color: #6b5acc; 
            color: #fff; 
            height: 5vh;
            width: 10vw; 
            border-radius: 15px;
            span {
                font-size: 1vw;
            }
        }
    }
    .demo {
        width: 50vw;
        height: 100%;
        display: flex;
        justify-content: center; 
        flex-direction: column; 
        align-items: center;
        .demo-png {
            width: 30vw;
        }
    }
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
.price-card {
    height: 100%; 
    width: 12vw; 
    margin: 0 2vw;
    display: flex;
    flex-direction: column; 
    justify-content: center; 

    position: relative;
}
.price-body {
    height: 100%; 
    padding: 10%;
}
.ribbon-new-user { width: 140px; height: 200px; position: absolute; top: -8px; right: -8px; overflow: hidden; }
.ribbon-new-user::before { position: absolute; right: 124px; border-radius: 8px 8px 0 0; width: 16px; height: 8px; background-color: #0011cc; content: ''; }
.ribbon-new-user::after { position: absolute; right: 0; top: 124px; border-radius: 0 8px 8px 0; width: 8px; height: 16px; background-color: #0011cc; content: ''; }
.ribbon-new-user span { display: inline-block; text-align: center;  width: 200px; height: 40px; line-height: 40px; position: absolute; top: 30px; right: -50px; z-index: 2; overflow: hidden; -ms-transform: rotate(45deg); -moz-transform: rotate(45deg); -webkit-transform: rotate(45deg); -o-transform: rotate(45deg); transform: rotate(45deg); border: 1px dashed #fff; box-shadow: 0 0 0 3px #0055ff,  0 14px 7px -9px rgba(0,0,0,.6);  background-color: #0055ff; color: #fff; }


.ribbon-year-pay { width: 140px; height: 200px; position: absolute; top: -8px; right: -8px; overflow: hidden; }
.ribbon-year-pay::before { position: absolute; right: 124px; border-radius: 8px 8px 0 0; width: 16px; height: 8px; background-color: #ff4a23; content: ''; }
.ribbon-year-pay::after { position: absolute; right: 0; top: 124px; border-radius: 0 8px 8px 0; width: 8px; height: 16px; background-color: #ff4a23; content: ''; }
.ribbon-year-pay span { display: inline-block; text-align: center;  width: 200px; height: 40px; line-height: 40px; position: absolute; top: 30px; right: -50px; z-index: 2; overflow: hidden; -ms-transform: rotate(45deg); -moz-transform: rotate(45deg); -webkit-transform: rotate(45deg); -o-transform: rotate(45deg); transform: rotate(45deg); border: 1px dashed #fff; box-shadow: 0 0 0 3px #ff4a23,  0 14px 7px -9px rgba(0,0,0,.6);  background-color: #ff4a23; color: #fff; }

</style>
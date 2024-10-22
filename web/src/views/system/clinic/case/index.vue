<template>
  <el-form ref="formRef" :model="form" :rules="rules" v-loading="loading" element-loading-text="数据提交中..." label-width="10vw" class="box-card" style="height: 100%;">
    <div style="height: 90%; display: flex;">
      <div style="width: 100%; height: 100%; padding-right: 5%;">
        <el-form-item prop="chiefComplaint">
          <template #label>
              <span style="font-size: 1.2em;">主诉</span>
          </template>
          <el-popover
            placement="bottom"
            width="600"
            :hide-after="0"
            trigger="hover"
          >
            <template #reference>
              <el-input v-model="form.chiefComplaint" @focus="chiefComplaintPopoverVisible = true" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
            </template>
            <div style="height: 40vh;">
              <div style="display: flex; height: 100%;">
                <div style="width: 20%">
                  <el-scrollbar height="100%">
                    <div 
                      v-for="(recommendKeyword, index) in recommendChiefComplaintKeywordList" :key="index" 
                      @click="currentRecommendKeywordTiredIndex = index" 
                      style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                      :style="{ 
                        'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                        'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                      }"
                    >
                      {{ recommendKeyword }}
                    </div>
                  </el-scrollbar>
                </div>
                <div style="width: 80%; padding: 5%;">
                  <div v-for="(recommendKeyword, index) in recommendChiefComplaintKeywords" :key="index">
                    <div v-if="currentRecommendKeywordTiredIndex == index">
                      <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                        <span>{{ recommendKeyword.name }}</span>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                        <div v-if="index > 0" style="width: 10%; padding-left: 2%;">
                          <el-text class="mx-1" type="info">部位</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.chiefComplaint = form.chiefComplaint ?  form.chiefComplaint + (', ' + part) : part"
                          >{{part}}</el-button>
                        </div>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.symptom && recommendKeyword.keywords.symptom.length > 0" style="margin: 2% 0; display: flex;">
                        <div v-if="index > 0" style="width: 10%; padding-left: 2%;">
                          <el-text class="mx-1" type="info">症状</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(symptom, index) in recommendKeyword.keywords.symptom" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.chiefComplaint = form.chiefComplaint ?  form.chiefComplaint + (', ' + symptom) : symptom"
                          >{{symptom}}</el-button>
                        </div>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.level && recommendKeyword.keywords.level.length > 0" style="margin: 2% 0; display: flex;">
                        <div v-if="index > 0" style="width: 10%; padding-left: 2%;">
                          <el-text class="mx-1" type="info">程度</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(level, index) in recommendKeyword.keywords.level" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.chiefComplaint += level"
                          >{{level}}</el-button>
                        </div>
                      </div>
                      <div style="margin: 2% 0; display: flex;">
                        <div v-if="index > 0" style="width: 10%; padding-left: 2%;">
                          <el-text class="mx-1" type="info">时间</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(time, index) in times" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.chiefComplaint += time"
                          >{{time}}</el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-popover>
        </el-form-item>
        <el-form-item prop="historyPresentIllness">
          <template #label>
            <span style="font-size: 1.2em;">现病史</span>
        </template>
          <el-input v-model="form.historyPresentIllness" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
        </el-form-item>
        <el-form-item prop="pastMedicalHistory">
          <template #label>
            <div style="text-align: right;">
              <div style="font-size: 1.2em;">既往史</div>
            </div>
          </template>
          <el-input v-model="form.pastMedicalHistory" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
        </el-form-item>
        <el-form-item prop="checkup">
          <template #label>
            <span style="font-size: 1.2em;">查体</span>
          </template>
          <el-popover
            placement="bottom"
            width="600"
            :hide-after="0"
            trigger="hover"
          >
            <template #reference>
              <el-input v-model="form.checkup" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
            </template>
            <div style="height: 40vh;">
              <div style="display: flex; height: 100%;">
                <div style="width: 20%">
                  <el-scrollbar height="100%">
                    <div 
                      v-for="(recommendKeywordTired, index) in recommendCheckupKeyKeywordList" :key="index" 
                      @click="currentRecommendKeywordTiredIndex = index" 
                      style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                      :style="{ 
                        'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                        'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                      }"
                    >
                      {{ recommendKeywordTired }}
                    </div>
                  </el-scrollbar>
                </div>
                <div style="width: 80%; padding: 5%;">
                  <div v-for="(recommendKeyword, index) in recommendCheckupKeywords" :key="index">
                    <div v-if="currentRecommendKeywordTiredIndex == index">
                      <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                        <span>{{ recommendKeyword.name }}</span>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                        <div v-if="index > 0" style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                          <el-text class="mx-1" type="info">部位</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.checkup = form.checkup ?  form.checkup + (', ' + part) : part"
                          >{{part}}</el-button>
                        </div>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.symptom && recommendKeyword.keywords.symptom.length > 0" style="margin: 2% 0; display: flex;">
                        <div style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                          <el-text class="mx-1" type="info">症状</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(symptom, index) in recommendKeyword.keywords.symptom" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.checkup += symptom"
                          >{{symptom}}</el-button>
                        </div>
                      </div>
                      <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.level && recommendKeyword.keywords.level.length > 0" style="margin: 2% 0; display: flex;">
                        <div style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                          <el-text class="mx-1" type="info">程度</el-text>
                        </div>
                        <div style="max-width: 90%;">
                          <el-button 
                            v-for="(level, index) in recommendKeyword.keywords.level" :key="index" 
                            type="info" plain style="margin: 4px"
                            @click="form.checkup += level"
                          >{{level}}</el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-popover>
        </el-form-item>
        <el-form-item prop="diagnosis">
          <template #label>
            <span style="font-size: 1.2em;">诊断</span>
          </template>
          <el-popover
            placement="bottom"
            width="600"
            :hide-after="0"
            trigger="hover"
          >
            <template #reference>
              <el-input v-model="form.diagnosis" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
            </template>
            <div style="display: flex; height: 100%;">
              <div style="width: 20%">
                <el-scrollbar height="100%">
                  <div 
                    v-for="(recommendKeywordTired, index) in recommendDiagnosisKeywordList" :key="index" 
                    @click="currentRecommendKeywordTiredIndex = index" 
                    style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                    :style="{ 
                      'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                      'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                    }"
                  >
                    {{ recommendKeywordTired }}
                  </div>
                </el-scrollbar>
              </div>
              <div style="width: 80%; padding: 5%;">
                <div v-for="(recommendKeyword, index) in recommendDiagnosisKeywords" :key="index">
                  <div v-if="currentRecommendKeywordTiredIndex == index">
                    <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                      <span>{{ recommendKeyword.name }}</span>
                    </div>
                    <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                      <div style="max-width: 90%;">
                        <el-button 
                          v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                          type="info" plain style="margin: 4px"
                          @click="form.diagnosis = form.diagnosis ?  form.diagnosis + (', ' + part) : part"
                        >{{part}}</el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-popover>
        </el-form-item>
        <el-form-item prop="dealWith">
          <template #label>
            <span style="font-size: 1.2em;">处理</span>
          </template>
          <el-input v-model="form.dealWith" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
        </el-form-item>
        <el-form-item prop="notes">
          <template #label>
            <span style="font-size: 1.2em;">备注</span>
          </template>
          <el-input v-model="form.notes" type="textarea" style="font-size: 1.2em; width: 85%;" clearable />
        </el-form-item>
      </div>
    </div>
  </el-form>



  
  <Transition name="slide-fade">
    <el-card v-if="currentDrawer == 'chiefComplaint'" style="width: 40vw; height: 100vh; position: fixed; top: 0; right: 0; z-index: 999;">
        <div style="display: flex; height: 100%;">
          <div style="width: 20%">
            <el-scrollbar height="100%">
              <div 
                v-for="(recommendKeyword, index) in recommendChiefComplaintKeywordList" :key="index" 
                @click="currentRecommendKeywordTiredIndex = index" 
                style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                :style="{ 
                  'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                  'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                }"
              >
                {{ recommendKeyword }}
              </div>
            </el-scrollbar>
          </div>
          <div style="width: 80%; padding: 5%;">
            <div v-for="(recommendKeyword, index) in recommendChiefComplaintKeywords" :key="index">
              <div v-if="currentRecommendKeywordTiredIndex == index">
                <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                  <span>{{ recommendKeyword.name }}</span>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                  <div v-if="index > 0" style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">部位</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.chiefComplaint = form.chiefComplaint ?  form.chiefComplaint + (', ' + part) : part"
                    >{{part}}</el-button>
                  </div>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.symptom && recommendKeyword.keywords.symptom.length > 0" style="margin: 2% 0; display: flex;">
                  <div v-if="index > 0" style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">症状</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(symptom, index) in recommendKeyword.keywords.symptom" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.chiefComplaint = form.chiefComplaint ?  form.chiefComplaint + (', ' + symptom) : symptom"
                    >{{symptom}}</el-button>
                  </div>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.level && recommendKeyword.keywords.level.length > 0" style="margin: 2% 0; display: flex;">
                  <div v-if="index > 0" style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">程度</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(level, index) in recommendKeyword.keywords.level" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.chiefComplaint += level"
                    >{{level}}</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style="width: 100%; display: flex; justify-content: center;">
          <el-button @click="() => currentDrawer = null" type="primary" style="width: 200px; height: 50px;">关闭</el-button>
        </div>
  </el-card>
  </Transition>
  <Transition name="slide-fade">
    <el-card v-if="currentDrawer == 'checkup'" style="width: 40vw; height: 100vh; position: fixed; top: 0; right: 0; z-index: 999;">
        <div style="display: flex; height: 100%;">
          <div style="width: 20%">
            <el-scrollbar height="100%">
              <div 
                v-for="(recommendKeywordTired, index) in recommendCheckupKeyKeywordList" :key="index" 
                @click="currentRecommendKeywordTiredIndex = index" 
                style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                :style="{ 
                  'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                  'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                }"
              >
                {{ recommendKeywordTired }}
              </div>
            </el-scrollbar>
          </div>
          <div style="width: 80%; padding: 5%;">
            <div v-for="(recommendKeyword, index) in recommendCheckupKeywords" :key="index">
              <div v-if="currentRecommendKeywordTiredIndex == index">
                <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                  <span>{{ recommendKeyword.name }}</span>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                  <div v-if="index > 0" style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">部位</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.checkup = form.checkup ?  form.checkup + (', ' + part) : part"
                    >{{part}}</el-button>
                  </div>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.symptom && recommendKeyword.keywords.symptom.length > 0" style="margin: 2% 0; display: flex;">
                  <div style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">症状</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(symptom, index) in recommendKeyword.keywords.symptom" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.checkup += symptom"
                    >{{symptom}}</el-button>
                  </div>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.level && recommendKeyword.keywords.level.length > 0" style="margin: 2% 0; display: flex;">
                  <div style="width: 10%; padding-left: 2%; display: flex; align-items: center;">
                    <el-text class="mx-1" type="info">程度</el-text>
                  </div>
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(level, index) in recommendKeyword.keywords.level" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.checkup += level"
                    >{{level}}</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style="width: 100%; display: flex; justify-content: center;">
          <el-button @click="() => currentDrawer = null" type="primary" style="width: 200px; height: 50px;">关闭</el-button>
      </div>
  </el-card>
  </Transition>
  <Transition name="slide-fade">
    <el-card v-if="currentDrawer == 'diagnosis'" style="width: 40vw; height: 100vh; position: fixed; top: 0; right: 0; z-index: 999;">
        <div style="display: flex; height: 100%;">
          <div style="width: 20%">
            <el-scrollbar height="100%">
              <div 
                v-for="(recommendKeywordTired, index) in recommendDiagnosisKeywordList" :key="index" 
                @click="currentRecommendKeywordTiredIndex = index" 
                style="margin: 10% 0; height: 3em; display: flex; flex-direction: column; justify-content: center; align-items: center; cursor: pointer;"
                :style="{ 
                  'border-left': currentRecommendKeywordTiredIndex == index ? '4px solid #6a5acd' : 'none' ,
                  'font-weight': currentRecommendKeywordTiredIndex == index ? '700' : '' ,
                }"
              >
                {{ recommendKeywordTired }}
              </div>
            </el-scrollbar>
          </div>
          <div style="width: 80%; padding: 5%;">
            <div v-for="(recommendKeyword, index) in recommendDiagnosisKeywords" :key="index">
              <div v-if="currentRecommendKeywordTiredIndex == index">
                <div style="height: 5%; margin-bottom: 2%; display: flex; align-items: center; font-size: 1.2em;">
                  <span>{{ recommendKeyword.name }}</span>
                </div>
                <div v-if="recommendKeyword.keywords && recommendKeyword.keywords.part && recommendKeyword.keywords.part.length > 0" style="margin: 2% 0; display: flex;">
                  <div style="max-width: 90%;">
                    <el-button 
                      v-for="(part, index) in recommendKeyword.keywords.part" :key="index" 
                      type="info" plain style="margin: 4px"
                      @click="form.diagnosis = form.diagnosis ?  form.diagnosis + (', ' + part) : part"
                    >{{part}}</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div style="width: 100%; display: flex; justify-content: center;">
          <el-button @click="() => currentDrawer = null" type="primary" style="width: 200px; height: 50px;">关闭</el-button>
      </div>
  </el-card>
  </Transition>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { ref, reactive, defineProps, defineEmits, defineExpose, onMounted } from 'vue'

import { verify } from './index.js'

const props = defineProps(['admission'])

const event = defineEmits(['submit','update'])

const form = reactive({})

const formRef = ref()
const loading = ref(false)
const currentDrawer = ref(null)
const currentRecommendKeywordTiredIndex = ref(0)
const times = [
  '1天', '2天', '3天', '4天', '5天', '6天', '约1周', '约2周', '约三周', '半月', '持续一个月', '持续两个月', '持续三个月', '持续约半年', '持续约一年', '持续约二年', '持续约三年'
]
const recommendChiefComplaintKeywordList = [ '常用', '鼻', '咳', '痰', '喘', '咽', '口腔溃疡', '呕吐', '牙', '头', ]
const recommendChiefComplaintKeywords = [
  {
    name: '常用',
    keywords: {
      symptom: [
        '流涕', 
        '流黄涕',
        '鼻塞', 
        '伴鼻塞',
      ],
    }
  },
  {
    name: '鼻',
    keywords: {
      symptom: [
        '流涕', 
        '流黄涕',
        '鼻塞', 
        '伴鼻塞',
      ],
    }
  },
  {
    name: '咳',
    keywords: {
      symptom: [
        '咳嗽', 
        '咳嗽减轻',
        '无明显咳嗽', 
        '伴咳嗽',
        '晨起咳嗽', 
        '今日咳嗽',
        '偶尔咳嗽', 
        '感冒后咳嗽',
      ],
    }
  },
  {
    name: '痰',
    keywords: {
      symptom: [
        '有痰', 
        '无痰',
      ],
    }
  },
  {
    name: '喘',
    keywords: {
      symptom: [
        '喘息', 
        '伴喘息',
      ],
    }
  },
  {
    name: '咽',
    keywords: {
      symptom: [
        '咽痛', 
        '咽好转',
        '咽部不利', 
      ],
    }
  },
  {
    name: '口腔溃疡',
    keywords: {
      symptom: [
        '口腔溃疡', 
      ],
    }
  },
  {
    name: '呕吐',
    keywords: {
      symptom: [
        '呕吐', 
      ],
    }
  },
  {
    name: '牙',
    keywords: {
      symptom: [
        '牙疼', 
      ],
    }
  },
  {
    name: '头',
    keywords: {
      symptom: [
        '头疼', 
        '头晕', 
      ],
    }
  },
]
const recommendCheckupKeyKeywordList = [
  '常用', '精神', '口腔', '咽', '扁桃体', '肺部', '胸部', '腹部', '腰部', '臀部'
]
const recommendCheckupKeywords = [
  {
    name: '常用',
    keywords: {
      part: [
        '精神好', 
        '神志清',
        '体温',
        '体重',
        '血压',
        '心率',
        '次/分',
        '血糖',
        '血氧',
        '°C',
        'kg',
        'mm Hg',
        'Mmol/L',
      ],
    }
  },
  {
    name: '精神',
    keywords: {
      part: [
        '精神好', 
        '神志清'
      ],
    }
  },
  {
    name: '口腔',
    keywords: {
      part: [
        '口腔', 
      ],
      symptom: [
        '疼痛', 
        '内侧可见溃疡、溃烂', 
        '内侧红肿', 
        '内侧可见疱疹', 
        '内侧可见多个红色疹', 
        '被覆脓苔', 
        '白色白苔', 
      ],
    }
  },
  {
    name: '咽',
    keywords: {
      part: [
        '口腔', 
      ],
      symptom: [
        '充血', 
        '红肿', 
        '内侧红肿', 
        '充血红肿', 
        '无异常', 
      ],
    }
  },
  {
    name: '扁桃体',
    keywords: {
      part: [
        '扁桃体', 
        '左侧扁桃体', 
        '右侧扁桃体', 
        '双侧扁桃体', 
      ],
      symptom: [
        '红肿', 
        '可见白点', 
        '可见红色溃烂面', 
        '可见脓苔', 
      ],
    }
  },
  {
    name: '肺',
    keywords: {
      part: [
        '双肺', 
        '左肺', 
        '右肺', 
      ],
      symptom: [
        '呼吸音', 
        '哮鸣音', 
        '湿啰音', 
        '痰鸣音', 
      ],
      level: [
        '稍粗', 
        '较清', 
        '大量', 
        '中等量', 
        '少量', 
        '无异常', 
      ]
    }
  },
  {
    name: '胸部',
    keywords: {
      part: [
        '前胸', 
        '前胸及后背', 
      ],
      symptom: [
        '胸闷', 
        '针尖样皮疹', 
        '皮炎', 
        '体癣', 
      ],
    }
  },
  {
    name: '腹部',
    keywords: {
      part: [
        '腹部', 
        '中腹', 
        '左上腹', 
        '左下腹', 
        '右上腹', 
        '右下腹', 
      ],
      symptom: [
        '腹痛', 
        '腹泻', 
        '腹胀', 
        '轻压痛', 
      ],
    }
  },
  {
    name: '腰部',
    keywords: {
      part: [
        '腰部', 
        '左侧腰部', 
        '右侧腰部', 
      ],
      symptom: [
        '疼痛', 
        '出疹', 
      ],
    }
  },
  {
    name: '臀部',
    keywords: {
      part: [
        '臀部', 
        '左侧臀部', 
        '右侧臀部', 
      ],
      symptom: [
        '疼痛', 
        '出疹', 
        '瘙痒', 
        '可见红色点状皮疹', 
        '疖', 
      ],
    }
  },
]
const recommendDiagnosisKeywordList = [ '常用', '上呼吸道感染', '下呼吸道感染', '消化系统疾病', '皮肤病', '耳鼻喉疾病', '泌尿系统疾病', '炎症' ]
const recommendDiagnosisKeywords = [
  {
    name: '常用',
    keywords: {
      part: [
        "急性咽炎",
        "幼儿急疹",
        "感冒后咳嗽",
        "过敏性咽炎",
        "过敏性支气管炎",
        "过敏性鼻炎",
        "过敏性皮疹",
        "喘息性支气管哮喘",
      ],
    }
  },
  {
      "name": "上呼吸道感染",
      "keywords": {
          "part": [
              "急性咽炎",
              "感冒后咳嗽",
              "急性鼻炎",
              "急性上呼吸道感染",
              "化脓性扁桃体炎",
              "疱疹性咽峡炎",
              "过敏性鼻炎"
          ]
      }
  },
  {
      "name": "下呼吸道感染",
      "keywords": {
          "part": [
              "支气管炎",
              "支气管哮喘",
              "喘息型支气管炎",
              "肺炎",
              "支原体肺炎"
          ]
      }
  },
  {
      "name": "消化系统疾病",
      "keywords": {
          "part": [
              "急性胃炎",
              "急性肠炎",
              "功能性消化不良",
              "急性胃肠炎",
              "口腔溃疡"
          ]
      }
  },
  {
      "name": "皮肤病",
      "keywords": {
          "part": [
              "体癣",
              "手癣",
              "足癣",
              "皮炎"
          ]
      }
  },
  {
      "name": "耳鼻喉疾病",
      "keywords": {
          "part": [
              "急性咽炎",
              "急性鼻炎",
              "过敏性鼻炎",
              "过敏性结膜炎",
              "化脓性扁桃体炎"
          ]
      }
  },
  {
      "name": "泌尿系统疾病",
      "keywords": {
          "part": [
              "尿路感染"
          ]
      }
  },
  {
      "name": "炎症",
      "keywords": {
          "part": [
            "急性咽炎",
            "支气管炎",
            "化脓性扁桃体炎",
            "急性鼻炎",
            "中耳炎",
            "疱疹性咽峡炎",
            "牙周炎",
            "皮炎",
            "结膜炎",
            "乳腺炎症",
            "肺炎",
            "支原体肺炎",
            "过敏性结膜炎",
            "过敏性咽炎",
            "过敏性支气管炎",
            "过敏性鼻炎",
            "喘息性支气管炎",
            "喘息性支气管哮喘"
        ]
      }
  },
]

const chiefComplaintPopoverVisible = ref(false)
function chiefComplaintPopoverBlur() {
  console.log('blur')
  chiefComplaintPopoverVisible.value = false
}

const getDossier = () => props.admission.dossier || null

onMounted(() => {
  Object.assign(form, getDossier())
})

const rules = reactive({
  diagnosis: [
    { required: true, message: '必须填写诊断信息', trigger: 'change' },
  ],
  chiefComplaint: [
    { required: true, message: '必须填写主诉信息', trigger: 'change' },
  ],
})

async function execVerify() {
  let verifyResult = await verify(formRef.value).catch((errors) => {
    for (let error in errors) {
      errors[error].forEach(errorMessage => {
        console.error(errorMessage)
      });
    }
  });
  if(verifyResult) {
      return form
  } else {
    ElNotification({ title: '通知', message: '病例信息不完善，请检查', type: 'error' })
  }
  return undefined
}

defineExpose({
  getForm() {
    return form
  },
  execVerify,
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 90%;
  margin-bottom: 1%;
  margin-left: 5%;
}

.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.el-space__item {
  line-height: 1.6;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  font-size: 15px;
  text-rendering: optimizeLegibility;
  font-weight: normal;
}
:deep(.el-card__body) {
  height: 100%;
}
:deep(.el-drawer__header) {
  height: 0px !important;
  margin: 0px !important;
  padding: 0px !important;
}
:deep(.el-drawer .el-drawer__body) {
  padding: 0px !important;
}
:deep(.el-card__body) {
  height: 100%;
}
.slide-fade-enter-active {
  transition: all 0.2s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.2s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
:deep(.el-popover) {
  padding: 0 !important;
}
</style>
<template>
  <div class="patient-container">
    <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
      <div
        style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
        <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">新增患者</el-text>
      </div>
    </el-form>
    <el-card class="main-card">
      <el-row :gutter="24" style="height: 100%;">

        <!-- 左侧：el-form -->
        <el-col :span="17" style="height: 100%;">
          <el-form ref="formRef" :model="patient" :rules="rules" label-width="120px" label-position="top" style="height: 100%;">

            <!-- 姓名和性别 -->
            <el-row :gutter="24" class="form-row" style="height: 100%;">
              <el-col :span="9">
                <el-form-item prop="name">
                  <template #label><span class="label-text default-font-size">姓名</span></template>
                  <el-autocomplete v-model="patient.name" :fetch-suggestions="handle.searchPatient"
                    :trigger-on-focus="false" clearable @select="handle.patientChecked" :fit-input-width="true"
                    class="search-input">
                    <template #default="{ item }">
                      <div>
                        {{ item.name }}
                        {{ (item.age ? item.age + '岁' + ' ' : '') }}
                        {{ item.sex == 1 ? '男' : '女' }}
                        {{ item.phone }}
                      </div>
                      <span class="small-text">最近就诊：{{ item.lastVisit }}</span>
                    </template>
                  </el-autocomplete>
                </el-form-item>
              </el-col>
              <el-col :span="1"></el-col>
              <el-col :span="6">
                <el-form-item prop="sex">
                  <template #label><span class="label-text">性别</span></template>
                  <el-radio-group v-model="patient.sex">
                    <el-radio :value="1">男</el-radio>
                    <el-radio :value="0">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item prop="isFirst">
                  <template #label><span class="label-text">就诊类型</span></template>
                  <el-radio-group v-model="patient.isFirst" class="search-input">
                    <el-radio :value="0">初诊</el-radio>
                    <el-radio :value="1">复诊</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>

            <!-- 年龄、出生日期 -->
            <el-row :gutter="24" class="form-row">
              <el-col :span="9">
                <el-form-item prop="birthday">
                  <template #label><span class="label-text">出生日期</span></template>
                  <el-date-picker v-model="patient.birthday" type="date" @change="handle.birthdayChange"
                    class="search-input" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="1"></el-col>
              <el-col :span="13">
                <div class="age-input-container">
                  <el-form-item prop="age" class="el-input-width-100">
                    <template #label><span class="label-text">年龄</span></template>
                    <div class="age-input-group">
                      <el-form-item style="width: 30%;">
                        <el-input v-model="patient.age" @change="handle.yearChange" class="search-input">
                          <template #append>岁</template>
                        </el-input>
                      </el-form-item>
                      <el-form-item prop="month" style="width: 30%;">
                        <el-input v-model="patient.month" @change="handle.monthChange" class="search-input">
                          <template #append>月</template>
                        </el-input>
                      </el-form-item>
                      <el-form-item prop="day" style="width: 30%;">
                        <el-input v-model="patient.day" @change="handle.dayChange" class="search-input">
                          <template #append>天</template>
                        </el-input>
                      </el-form-item>
                    </div>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>

            <!-- 联系电话、身份证 -->
            <el-row :gutter="24" class="form-row">

              <el-col :span="9">
                <el-form-item prop="phone">
                  <template #label>
                    <span class="label-text">联系电话</span>&nbsp;&nbsp;&nbsp;
                    <el-tooltip class="box-item" effect="dark" content="患者微信扫码绑定" placement="top-start">
                      <el-button :icon="FullScreen" size="small" circle @click="openVxScan"
                        v-if="isPhoneNumber(patient.phone)" />
                    </el-tooltip>
                  </template>
                  <el-input v-model="patient.phone" class="search-input" />
                </el-form-item>
              </el-col>
              <el-col :span="1"></el-col>
              <el-col :span="13">
                <el-form-item prop="idCard">
                  <template #label><span class="label-text">居民身份证</span></template>
                  <el-input v-model="patient.idCard" class="search-input" style="width:  100%;" />
                </el-form-item>

              </el-col>
            </el-row>

            <!-- 住址 -->
            <el-row :gutter="24" class="form-row">
              <el-col :span="23">
                <el-form-item prop="address">
                  <template #label><span class="label-text">住址</span></template>
                  <el-input v-model="patient.address" class="search-input" />
                </el-form-item>
              </el-col>
            </el-row>

            <!-- 备注 -->
            <el-row :gutter="24" class="form-row">
              <el-col :span="23">
                <el-form-item prop="remark">
                  <template #label><span class="label-text">备注</span></template>
                  <el-input v-model="patient.remark" :autosize="{ minRows: 4, maxRows: 4 }" type="textarea"
                    class="search-input" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 保存、清空按钮 -->
            <div class="button-row">
              <el-button @click="handle.onSubmit(formRef)" :loading="submitLoading.loading.value" type="primary"
                class="submit-button" style="height: 5vh;">保存</el-button>
              <el-button @click="handle.resetForm(formRef)" type="info" class="reset-button" plain style="height: 5vh;">清空</el-button>
            </div>
          </el-form>
        </el-col>
        <!-- 中间分割 -->
        <el-col :span="1" class="Border_l"></el-col>
        <!-- 右侧：el-table -->
        <el-col :span="6">
          <el-table :data="tableData" :height="tableHeight" @scroll="handleScroll" :row-style="{ height: '5vh' }" >
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="phone" label="电话"></el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="success" :icon="Check" circle color="#626aef" plain
                  @click="handle.activeUser(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-card>
  </div>
  <openVxDialog ref="openVxRef" />
</template>


<script setup>
import { ref, reactive, onMounted } from 'vue'
import { patientService, admissionService } from '@/api/clinic/index.js'
import { ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { FullScreen } from '@element-plus/icons-vue'

import {
  Check
} from '@element-plus/icons-vue'
import openVxDialog from './openVxDialog.vue'


const routerInstance = useRouter()

const patient = reactive({})

const formRef = ref()

const rules = reactive({
  name: [
    { required: true, message: '必须输入患者姓名', trigger: 'blur' },
    { min: 1, max: 50, message: '姓名长度需要控制在 1~50 个字符', trigger: 'blur' },
  ],
  sex: [
    {
      required: true,
      message: '必须选择患者性别',
      trigger: 'change',
    },
  ],
  age: [
    {
      required: true,
      message: '必须输入患者年龄',
      trigger: 'change',
    },
  ],
  phone: [
    {
      required: true,
      message: '必须输入联系人手机号',
      trigger: 'change',
    },
  ],
  address: [
    { min: 1, max: 100, message: '地址长度需要控制在 1~100 个字符', trigger: 'blur' },
  ],
  idCard: [

  ],
  isFirst: [
    {
      required: true,
      message: '必须选择就诊类型',
      trigger: 'change',
    },
  ],
})


//打开openVxDialog扫码弹窗
const openVxRef = ref(null)
function openVxScan() {
  console.log("打开");
  //调用openDialog方法
  openVxRef.value.openDialog(patient.phone)
}

function isPhoneNumber(phoneNumber) {
  // 正则表达式匹配中国大陆的手机号码
  const regex = /^1[3-9]\d{9}$/;
  return regex.test(phoneNumber);
}


const submitLoading = {
  loading: ref(false),
  open: function () {
    this.loading.value = true
    setTimeout(() => submitLoading.close(), 5000)
  },
  close: function () { this.loading.value = false },
}

const handle = {
  birthdayChange(val) {
    method.birthToAge(val)
  },
  searchPatient: (queryString, cb) => {
    patientService.selectPatient({ name: queryString, current: 1, size: 10 }, ({ records }) => cb(records))
  },
  patientChecked: (item) => {
    admissionService.add({ patientId: item.id }, (response) => {//openId: openId.value
      let routeData = routerInstance.resolve({
        path: "/clinic/cure/" + response,
        query: {
          title: item.name,
          index: response
        }
      });
      window.open(routeData.href, "_blank");
      submitLoading.close()
    })
  },
  onSubmit: (formEl) => {
    submitLoading.open()
    if (!formEl) return
    formEl.validate((valid, fields) => {
      if (valid) {
        method.addPatient()
      } else {
        ElNotification({ title: '通知', message: '病人信息不完善! ', type: 'error' })
      }
      submitLoading.close()
    })
  },
  resetForm: (formEl) => {
    if (!formEl) return
    formEl.resetFields()
  },
  yearChange: () => method.calculateAgeToBirth(patient.age, patient.month, patient.day),
  monthChange: () => method.calculateAgeToBirth(patient.age, patient.month, patient.day),
  dayChange: () => method.calculateAgeToBirth(patient.age, patient.month, patient.day),
  //应用用户信息
  activeUser(item) {
    Object.assign(patient, item);
    patient.birthday = item.birthDate;
    method.birthToAge(new Date(patient.birthday))
  }


}
/**
   * 获取历史数据
   */
const tableData = ref([]);
const loading = ref(false);
const hasMore = ref(true);
const page = reactive({
  current: 1,
  size: 10
});

const handleScroll = (event) => {
  const scrollTop = event.target.scrollTop;
  const scrollHeight = event.target.scrollHeight;
  const clientHeight = event.target.clientHeight;

  if (scrollTop + clientHeight >= scrollHeight) {
    if (!loading.value && hasMore.value) {
      loadMoreData();
    }
  }
};

const loadMoreData = async () => {
  loading.value = true;
  try {
    await patientService.selectPatient({
      ...page
    }, (response) => {
      tableData.value = [...tableData.value, ...response.records];
      page.current += 1;
      if (response.records.length < page.size) {
        hasMore.value = false;
      }
    });
  } catch (error) {
    ElNotification({
      title: '加载失败',
      message: '无法加载更多数据',
      type: 'error'
    });
    hasMore.value = false;
  } finally {
    loading.value = false;
  }
};


// 初始化加载数据
loadMoreData();



const method = {

  addPatient() {
    admissionService.add({
      patientId: patient.id ? patient.id : undefined,
      patient: {
        ...patient,
        age: patient.age,
        birthDate: patient.birthday
      }
    }, (response) => {//openId: openId.value
      let routeData = routerInstance.resolve({
        path: "/clinic/cure/" + response,
        query: {
          title: patient.name,
          index: response
        }
      });
      window.open(routeData.href, "_blank");
      submitLoading.close()
    })
  },
  /**
   * 计算并获取生日
   */
  calculateAgeToBirth(ageYear = 0, ageMonth = 0, ageDay = 0) {
    let subYear = parseInt(ageYear);
    let subMonth = parseInt(ageMonth);
    let subDay = parseInt(ageDay);
    let now = new Date();
    let nowYear = now.getFullYear();
    let nowMonth = now.getMonth() + 1;
    let nowDay = now.getDate(); // 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
    let day = nowDay - subDay;
    let month = nowMonth - subMonth;
    let year = nowYear - subYear; // 检查是否溢出
    if (day <= 0) { // 获得上月的天数
      let lastMonth = nowMonth - 1;
      let lastMonthOfYear = nowYear;
      if (lastMonth <= 0) {
        lastMonth = lastMonth + 12 //(lastMonth + 12) % 12;
        lastMonthOfYear = lastMonthOfYear - 1;
      }
      day = day + new Date(lastMonthOfYear, lastMonth, 0).getDate();
      month = month - 1;
    }
    if (month <= 0) {
      month = month + 12 //(month + 12) % 12;
      year--;
    }
    if (month < 10) {
      month = '0' + month
    }
    if (day < 10) {
      day = '0' + day
    }
    patient.birthday = year + '-' + month + '-' + day
  },
  /**
   * 计算并获取年龄
   */
  birthToAge(birthday) {
    if (birthday) {
      let birthdayStr = birthday.getFullYear() + '-' + birthday.getMonth() + '-' + birthday.getDay()
      let birthdayArr = birthdayStr.split('-');
      // 新建日期对象
      let date = new Date();
      // 今天日期，数组，同 birthday
      let today = [date.getFullYear(), date.getMonth() + 1, date.getDate()];
      // 分别计算年月日差值
      let age = today.map((val, index) => {
        return val - birthdayArr[index]
      })
      // 当天数为负数时，月减 1，天数加上月总天数
      if (age[2] < 0) {
        // 简单获取上个月总天数的方法，不会错
        let lastMonth = new Date(today[0], today[1], 0)
        age[1]--
        age[2] += lastMonth.getDate()
      }
      // 当月数为负数时，年减 1，月数加上 12
      if (age[1] < 0) {
        age[0]--
        age[1] += 12
      }
      patient.age = age[0];
      patient.month = age[1];
      patient.day = age[2];
    }
  },


}
</script>

<style scoped>
.patient-container {
  height: 100%;
  padding: 2%;
}

.header-form {
  height: 10%;
  display: flex;
  justify-content: space-between;
}

.header-title {
  width: 10%;
  height: 100%;
  padding-left: 20px;
  text-align: center;
  display: flex;
  align-items: center;
}

.qr-code-container {
  float: right;
  height: 25%;
  text-align: center;
  overflow: hidden;
}

.qr-code-image {
  height: 100%;
  width: 100%;
  object-fit: contain;
}

.main-card {
  height: 85%;
  border-radius: 15px;
  padding: 2%;
}

.form-row {
  margin-bottom: 10px;
}

.label-text {
  font-size: 1.2em;
}

.small-text {
  font: 0.7em sans-serif;
}

.search-input {
  width: 100% !important;
}

.age-input-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.age-input-group {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.button-row {
  text-align: center;
  height: 10%;
}

.submit-button,
.reset-button {
  width: 200px;
}

.el-card__body {
  height: 100%;
}

.el-form-item__label {
  height: 5vh;
  margin-left: 10px;
}

.el-input,
.el-textarea__inner {
  height: 100%;
  font-size: 1.2em;
}

.Border_l {
  border-left: 1px solid #eee;
  height: 100%;
  min-height: calc(100vh - 150px);
}

::v-deep(.search-input .el-textarea__inner) {
  width: 100% !important;
}
:deep(.el-card__body.el-col) {
  height: 100%;
} 
</style>
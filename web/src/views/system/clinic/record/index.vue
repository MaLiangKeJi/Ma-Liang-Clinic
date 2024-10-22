<template>
  <el-form :model="searchForm" label-width="100px">
    <el-row :gutter="24">
      <el-col :span="4" style="min-width: 250px;">
        <el-form-item label="姓名">
            <el-input v-model="searchForm.name" />
          </el-form-item>
        </el-col>
      <el-col :span="4" style="min-width: 250px;">
        <el-form-item label="年龄">
          <el-input v-model="searchForm.age" />
        </el-form-item>
      </el-col>
      <el-col :span="4" style="min-width: 250px;">
        <el-form-item label="性别">
          <el-radio-group v-model="searchForm.sex">
            <el-radio style="margin-right: 16px;" :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="4" style="min-width: 250px;">
        <el-form-item label="电话">
          <el-input v-model="searchForm.phone" />
        </el-form-item>
      </el-col>
      <el-col :span="6" style="min-width: 250px;">
        <el-form-item label="地址">
          <el-input v-model="searchForm.address" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-row justify="space-between" style="margin-top: 20px; margin-bottom: 50px;">
    <el-col :span="6">
      <el-button type="primary" @click="getData">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </el-col>
  </el-row>

  <el-table :data="table.data" v-loading="table.loading" stripe style="width: 100%">
    <el-table-column prop="lastVisit" label="最近就医日期" min-width="90" />
    <el-table-column prop="name" label="姓名" min-width="60" />
    <el-table-column prop="sex" label="性别" min-width="60" :formatter="sexFormatter" />
    <el-table-column prop="age" label="年龄" min-width="60" />
    <el-table-column prop="phone" label="电话" min-width="60" />
    <el-table-column prop="address" label="住址" min-width="180" />
    <el-table-column prop="remark" label="备注" min-width="60" />
    <el-table-column fixed="right" label="操作" min-width="120">
      <template #default="scope">
        <el-button link type="success" size="small" @click="toCase(scope.$index, scope.row)">快速接诊</el-button>
        <el-button link type="primary" size="small" @click="edit(scope.$index, scope.row)">病人信息</el-button>
        <el-button link type="primary" size="small" @click="toCase(scope.$index, scope.row, true)">历史病历</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页 -->
  <el-pagination
      v-model:current-page="current"
      v-model:page-size="size"
      v-model:total="total"
      style="margin-top: 20px;"
      background
  />
</template>

<script setup>

import { ref, reactive, inject, watch, onMounted } from 'vue'

import { patientService } from '@/api/clinic/index.js'
const selectedPatientForm = reactive({})

const visible = ref({
    add: false
})
const componentType = ref('add')
const componentTitle = ref('')
const edit = (index, row) => {
    selectedPatientForm.value = { ...row } 
    componentType.value = 'edit'
    componentTitle.value = '修改病人信息'
    visible.value.add = true
}

const selectDate = ref([])

const current = ref(1)
const size = ref(10)
const total = ref(0)

const searchForm = reactive({
  id: null,
  name: null,
  age: null,
  phone: null,
  address: null,
  sex: null,
  remark:null,
  start: '',
  end: '',
  current: 1,
  size: 10,
  total: 1,
})

const table = reactive({
  data: [],
  loading: false,
})

const formatDateTime = function (date) { 
  var y = date.getFullYear(); 
  var m = date.getMonth() + 1;  
      m = m < 10 ? ('0' + m) : m;  
  var d = date.getDate();  
      d = d < 10 ? ('0' + d) : d;  
  var h = date.getHours();  
      h=h < 10 ? ('0' + h) : h;  
  var minute = date.getMinutes();  
      minute = minute < 10 ? ('0' + minute) : minute;  
  var second=date.getSeconds();  
      second=second < 10 ? ('0' + second) : second;  
  return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
}

const reset = () => {
  searchForm.id = null
  searchForm.name = null
  searchForm.age = null
  searchForm.phone = null
  searchForm.address = null
  searchForm.sex = null
  searchForm.remark = null
  searchForm.start = ''
  searchForm.end = ''
  current.value = 1
  total.value = 1
  getData()
}
const getData = () => {
  table.loading = true
  if(selectDate.value.length == 2) {
    searchForm.start = formatDateTime(selectDate.value[0])
    searchForm.end = formatDateTime(selectDate.value[1])
  }
  setTimeout(() => { if(table.loading) table.loading = false }, 5000)
  patientService.selectPatient(searchForm, (response) => {
    total.value = response.total
    table.data = response.records
    table.loading = false
  })
}

const sexFormatter = (row, column, cellValue, index) =>{
  return cellValue == 1 ?'男':'女'
}
const isShowAllCase = inject('isShowAllCase')
const toCase = (index, row, isShowHistoryCase = false) => {
  setPatient(row)
  router.push({ name: 'Case' })
  if(isShowHistoryCase) isShowAllCase.value = true
}
onMounted(() => {
  getData()
})
watch(
  () => current, 
  async (newQuestion, oldQuestion) => {
    getData()
  },
  { deep: true }
)
</script>

<style scoped>
.el-input {
  width: 100% !important;
}
</style>
  
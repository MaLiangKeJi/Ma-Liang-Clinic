<template>
  <div>
    <!-- 使用自定义弹窗组件 -->
    <mlDialog v-model:isOpen="dialogVisible" :title="dialogTitle" :widthKey="dialogWidthKey"
      :confirmType="dialogConfirmType" :isSubmit="dialogIsSubmit" :submitText="submitBuName" @confirm="handleConfirm">
      <!-- 自定义内容 -->
      <el-table v-if="payForm.prescription && payForm.prescription.drugs" :data="payForm.prescription.drugs"
        style="width: 100%; max-width: 800px;" border>
        <el-table-column prop="name" label="药品名称" width="180">
          <template #default="{ row }">
            {{ row.name }} ({{ row.spec }})
          </template>
        </el-table-column>
        <el-table-column prop="drugUsage" label="用法" width="180">
          <template #default="{ row }">
            {{ row.drugUsage }}
          </template>
        </el-table-column>
        <el-table-column prop="frequency" label="频率" width="180">
          <template #default="{ row }">
            {{ row.frequency }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="180">
          <template #default="{ row }">
            {{ row.quantity }}{{ row.quantityUnit }}
          </template>
        </el-table-column>
      </el-table>

      <div v-if="payForm.state == 0" style="margin-top: 20px;">
        <el-form :model="payForm" label-width="80px" style="width: 100%;">
          <el-form-item label="费用" style="min-width: 300px;">
            <div style="display: flex; align-items: center;">
              <span style="font-size: 18px; font-weight: bold;">{{ payForm.fee }} 元</span>
            </div>
          </el-form-item>
          <el-form-item label="收费方式" style="width: 100%;text-align: center; margin: 0 auto 0  20px;">
            <el-radio-group v-model="payForm.way" size="large">
              <el-radio-button :value="1">微信</el-radio-button>
              <el-radio-button :value="2">支付宝</el-radio-button>
              <el-radio-button :value="3">挂账</el-radio-button>
              <el-radio-button :value="4">现金</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </mlDialog>
  </div>
</template>

<script setup>
import { ElNotification } from 'element-plus'
import { ref, defineEmits, watch, onMounted } from 'vue'
import { payService } from '@/api/clinic/index.js'
// 导入自定义弹窗组件
import mlDialog from '@/components/Dialog/mlDialog.vue';
// 弹窗状态变量
const dialogVisible = ref(false);
const dialogTitle = ref('处方详情');
const dialogWidthKey = ref('medium');
const dialogConfirmType = ref('primary');
const dialogIsSubmit = ref(true);
const submitBuName = ref('收费');
//自定义变量
const event = defineEmits(['create'])
const payForm = ref(null);

watch(() => payForm.way, (newValue) => {
  if (!newValue) {
    payForm.value = 1
  }
})
// 打开弹窗
function openDialog(item) {
  payForm.value = item
  if (payForm.value.way == null || payForm.value.way == '' || payForm.value.way == undefined) {
    payForm.value.way = 1
  }
  //如果已收费，不展示提交按钮
  if(payForm.value.state==1){
    dialogIsSubmit.value = false
  }
  dialogVisible.value = true;
}

// 确认按钮点击后的处理方法
const handleConfirm = () => {
  payService.addRecord({
    admissionId: payForm.value.admissionId,
    id: payForm.value.id,
    fee: payForm.value.fee,
    way: payForm.value.way,
  }, (response) => {
    ElNotification({ title: '成功', message: '信息提交成功!', type: 'success' })
    event('create')
    dialogVisible.value = false;
  })
};

//暴露方法
defineExpose({
  openDialog,
});
</script>

<style scoped>
.loading {
  text-align: center;
  padding: 20px;
}
</style>
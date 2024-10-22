<template>
  <el-collapse v-model='collapseVal' accordion="true">
    <el-collapse-item title="处方" name="1">
      <el-scrollbar max-height="61vh" always="true" class='detailFrame'>
        <div v-for='(drug, index) in drugArr' class='drugFrame'>
          <el-card>
            <el-text class='drugCol master'>
              {{ index + 1 }}.
              {{ drug.name }},
              一次{{ drug.singleDose }}{{ drug.singleDoseUnit }},
              {{ drug.frequency }},
              {{ drug.period }}{{ drug.periodUnit }}
            </el-text>

            <el-text class='drugCol left'>规格：{{ drug.spec }}</el-text>
            <el-text class='drugCol left'>厂商：{{ drug.manufacturer }}</el-text>
          </el-card>
        </div>
      </el-scrollbar>
    </el-collapse-item>

    <el-collapse-item title="收费" @click='payWindows()' name="2">
      <div v-loading='loadByPay'>
        <el-form-item label="总计">
          {{ totalPrice }} 元
        </el-form-item>

        <el-form-item label="收费方式">
          <el-radio-group v-model='payment' size="large">
            <el-radio-button :value="1">微信</el-radio-button>
            <el-radio-button :value="2">支付宝</el-radio-button>
            <el-radio-button :value="3">挂账</el-radio-button>
            <el-radio-button :value="4">现金</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model='remark' type="textarea" maxlength="30" show-word-limit />
        </el-form-item>

        <view>
          <el-button @click='hideDetail()' type="info" plain class='detailDialogButton'>返回</el-button>

          <el-button v-if='payStatus === 0' @click='payEvent()' type="primary" class='detailDialogButton'>收费</el-button>
          <el-button v-else-if='payStatus === 1' type="primary" disabled="true" plain
            class='detailDialogButton'>已收费</el-button>
        </view>
      </div>
    </el-collapse-item>
  </el-collapse>
</template>

<script setup>
import { ref, onMounted, defineProps, defineEmits, } from 'vue';

import payService from '@/api/clinic/pay.api.js'



const collapseVal = ref("1");



const drugArr = ref([]);
const props = defineProps(['inDrugs', 'inPayId', 'inAdmissId',]);
onMounted(() => {
  drugArr.value = props.inDrugs;
})



const remark = ref();
const payment = ref(1);
const payStatus = ref(0);
const totalPrice = ref(0);

const loadByPay = ref(false);
const hasPayData = ref(false);
function payWindows() {
  if (hasPayData.value)
    return;

  loadByPay.value = true;
  payService.searchPayRecord({ id: props.inPayId }, resp => {
    totalPrice.value = resp.fee;
    payStatus.value = resp.state;

    if (resp.way != undefined && resp.way != null)
      payment.value = resp.way;

    if (resp.remark != undefined && resp.remark != null)
      remark.value = resp.remark;

    hasPayData.value = true;
    loadByPay.value = false;
  })
}



const event = defineEmits(['backDrugList', 'updDrugByPay',]);
function hideDetail() {
  event('backDrugList');
}

function payEvent() {
  let addParams = {
    admissionId: props.inAdmissId,
    id: props.inPayId,
    fee: totalPrice.value,
    way: payment.value - 1,
    remark: remark.value
  };

  payService.addRecordByUid(85, addParams, (resp) => {
    if (resp)
      event('updDrugByPay', props.inAdmissId);
  })
}
</script>

<style scoped>
.detailFrame {
  margin-bottom: 3vh;
}

.drugFrame {
  margin-bottom: 3vh;
}

.drugCol {
  font-size: 2vh;

  width: 100%;
  height: 6vh;

  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;

  text-align: center;
  vertical-align: middle;
}

.drugCol.master {
  background: #F5F7FA;
}

.drugCol.left {
  align-items: left;
  justify-content: left;

  text-align: left;
}

.detailDialogButton {
  height: 5vh;
  width: 30%;
}
</style>
<template>
  <div>
    <!-- 使用自定义弹窗组件 -->
    <MLDialog v-model:isOpen="dialogVisible" :title="dialogTitle" :widthKey="dialogWidthKey"
      :confirmType="dialogConfirmType" :isSubmit="dialogIsSubmit">
      <!-- 自定义内容 -->
      <VueQrcode :value='drugPhoneUrl' style="width: 100%;" />
    </MLDialog>
  </div>
</template>

<script setup>
// 导入自定义弹窗组件
import MLDialog from '@/components/Dialog/mlDialog.vue';

import { ref, defineExpose } from 'vue';

// 链接生成二维码
import VueQrcode from 'vue-qrcode';

import { loginUserStore, } from '@/stores/UserStore';



// 定义状态变量
const dialogTitle = ref('请使用手机扫码打开');
const dialogWidthKey = ref('small');
const dialogConfirmType = ref('primary');
const dialogIsSubmit = ref(false);



const dialogVisible = ref(false);
function openDialog() {
  dialogVisible.value = true;
  generateQRCode();
}

const drugPhoneUrl = ref('');
function generateQRCode() {
  loginUserStore.getUser().then(data => {
    let url = window.location.origin
      + "/clinic/open/drug" + "?clinicBindUid=" + data.id;
    drugPhoneUrl.value = url;
  });
}



//暴露方法
defineExpose({
  openDialog,
});
</script>

<style scoped></style>
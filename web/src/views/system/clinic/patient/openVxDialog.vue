<template>
    <div>
      <!-- 使用自定义弹窗组件 -->
      <mlDialog
        v-model:isOpen="dialogVisible"
        :title="dialogTitle"
        :widthKey="dialogWidthKey"
        :confirmType="dialogConfirmType"
        :isSubmit="dialogIsSubmit"
        @confirm="handleConfirm"
      >
        <!-- 自定义内容 -->
        <img v-if="qrCodeSrc" :src="qrCodeSrc" alt="QR Code" />
        <div v-else-if="qrCodeLoading" class="loading">加载中...</div>
      </mlDialog>
    </div>
  </template>
  
  <script setup>
  import { ref, defineExpose  } from 'vue';
  import { getQRCode, checkPatient } from '@/api/qrCode.js'
  // 导入自定义弹窗组件
  import mlDialog from '@/components/Dialog/mlDialog.vue';
  // 定义状态变量
  const dialogVisible = ref(false);
  const dialogTitle = ref('患者微信扫码绑定');
  const dialogWidthKey = ref('medium');
  const dialogConfirmType = ref('primary');
  const dialogIsSubmit = ref(false);
  const openId = ref('');
  const qrCodeSrc = ref('');
  const qrCodeLoading = ref(true);
  const phone = ref('');
  
  // 打开弹窗
  function openDialog(phoneValue) {
    dialogVisible.value = true;
    phone.value = phoneValue;
    generateQRCode();
  }
  
  // 生成二维码
  async function generateQRCode() {
    try {
       // 示例手机号       
      const response = await getQRCode({phone:phone.value});
      const ticket = response.data.data.ticket;
      qrCodeSrc.value = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}`;
      qrCodeLoading.value = false;
      startPolling(ticket, phone);
    } catch (error) {
      console.error('生成二维码失败:', error);
    }
  }
  
  // 开始轮询
  async function startPolling(ticket, phone) {
    let loginTimer = setInterval(async () => {
      try {
        const response = await checkPatient({ ticket, phone, isEnd: false });
        const scanResult = response.data.data.scanResult;
  
        if (scanResult !== -1) {
          clearInterval(loginTimer);
          openId.value = response.data.data.openId;
          console.log('关注成功，OpenID:', openId.value);
        } else if (scanResult === -2) {
          clearInterval(loginTimer);
          console.log('二维码过期');
        }
      } catch (error) {
        console.error('轮询失败:', error);
      }
    }, 3000);
  }
  
  // 确认按钮点击后的处理方法
  const handleConfirm = () => {
    console.log('确认按钮被点击');
  };
  
 //暴露方法
 defineExpose({
    openDialog,
    getOpenId: () => openId.value
  });
  </script>
  
  <style scoped>
  .loading {
    text-align: center;
    padding: 20px;
  }
  </style>
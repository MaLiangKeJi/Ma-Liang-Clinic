<template>
  <el-dialog v-model="props.isOpen" :title="title" :width="selectedWidth" @close="handleClose">
    <br>
    <slot></slot> <!-- 用于插入用户自定义的内容 -->
    <br>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button :type="confirmType" v-if="isSubmit" @click="$emit('confirm')">{{ props.submitText }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

const props = defineProps({
  // 是否显示对话框
  isOpen: {
    type: Boolean,
    default: false
  },
  // 标题
  title: {
    type: String,
    default: '提示'
  },
  // 宽度
  widthKey: {
    type: String,
    default: 'medium'
  },
  // 确定按钮文字
  submitText:{
    type: String,
    default: '提交'
  },
  // 确定按钮类型
  confirmType: {
    type: String,
    default: 'primary'
  },
  // 是否显示确定按钮
  isSubmit: {
    type: Boolean,
    default: false
  },
  // 关闭弹窗的回调方法
  closeCallBack: {
    type: Function,
    default: null
  }
});

const emit = defineEmits(['update:isOpen', 'confirm']);

const selectedWidth = computed(() => {
  const widths = {
    "small": '500px',
    "medium": '666px',
    "large": '888px',
    'extra-large': '1088px'
  };
  return widths[props.widthKey] || '500px'; // 默认值为 'small'
});
// 默认关闭方法
const handleClose = () => {
  if (props.closeCallBack) {
    props.closeCallBack();
  }
  emit('update:isOpen', false);
};

watch(() => props.isOpen, (newVal) => {
  if (newVal === false) {
    emit('update:isOpen', newVal);
  }
});
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
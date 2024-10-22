<template>
    <div id="Calculator">
        <el-row id="result">
        <el-input
            v-model="result"
            style="font-size: 1.5em; font-weight: bold;"
            clearable
        >
        </el-input>
        </el-row>
        <el-row :gutter="10">
        <el-col :span="6"><div @click="getResult(7)" class="grid-content bg-purple">7</div></el-col>
        <el-col :span="6"><div @click="getResult(8)" class="grid-content bg-purple">8</div></el-col>
        <el-col :span="6"><div @click="getResult(9)" class="grid-content bg-purple">9</div></el-col>
        <el-col :span="6"><div @click="getResult('/')" class="grid-content bg-purple">/</div></el-col>
        <el-col :span="6"><div @click="getResult(4)" class="grid-content bg-purple">4</div></el-col>
        <el-col :span="6"><div @click="getResult(5)" class="grid-content bg-purple">5</div></el-col>
        <el-col :span="6"><div @click="getResult(6)" class="grid-content bg-purple">6</div></el-col>
        <el-col :span="6"><div @click="getResult('*')" class="grid-content bg-purple">*</div></el-col>
        <el-col :span="6"><div @click="getResult(1)" class="grid-content bg-purple">1</div></el-col>
        <el-col :span="6"><div @click="getResult(2)" class="grid-content bg-purple">2</div></el-col>
        <el-col :span="6"><div @click="getResult(3)" class="grid-content bg-purple">3</div></el-col>
        <el-col :span="6"><div @click="getResult('+')" class="grid-content bg-purple">+</div></el-col>
        <el-col :span="6"><div @click="getResult(0)" class="grid-content bg-purple">0</div></el-col>
        <el-col :span="6"><div @click="getResult('.')" class="grid-content bg-purple">.</div></el-col>
        <el-col :span="6"><div @click="getResult('=')" class="grid-content bg-purple">=</div></el-col>
        <el-col :span="6"><div @click="getResult('-')" class="grid-content bg-purple">-</div></el-col>
        <div style="display: flex; justify-content: center; width: 100%; margin-top: 5%;">
            <el-button @click="cancel" type="info" style="width: 100px; height: 50px;" plain>取消</el-button>
            <el-button @click="success" type="success" style="width: 100px; height: 50px;" >确定</el-button>
        </div>
        </el-row>
    </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'

const event = defineEmits(['cancel', 'success'])

const result = ref('')

function sendMessage(message) {
    ElMessage({
        message,
        type: 'warning',
    })
}
function getResult(e) {
    // 不可以连续输入 小数点
    if (e === '.' && result.value.split('').reverse().join('').indexOf('.') === 0) {
        sendMessage('请输入正确的浮点数1')
        return false
    }
    // 如果第一次 输入的是 运算符号，则提示
    if (['+', '-', '*', '/', '%', '.'].indexOf(e) > -1 && result.value.length === 0) {
        sendMessage('请先输入数字')
        return false
    }
    // 如果第一次 输入的是 = 号，则直接返回
    if (e === '=' && result.value.length === 0) {
        return false
    }
    // 出现 = 号则表示已经执行过计算，需要清空
    if (result.value.indexOf('=') > -1) {
        result.value = ''
    }
    switch (e) {
        case '=':
        // eslint-disable-next-line no-eval
        // result.value += '=' + eval(result.value)
        try {
            result.value = eval(result.value).toString()
        } catch(err) {
            ElMessage.error('无法计算，请检查计算器公式')
        }
        break
        default:
        result.value += e
    }
}
function success() { event('success', result.value) }
function cancel() { event('cancel') }
</script>

<style scoped>
#result{
    margin-bottom: 10px;
}
.grid-content{
    text-align: center;
    height: 40px;
    border:solid 1px #e6e6e6;
    line-height: 40px;
    margin-bottom: 10px;
    font-weight: bold;
    border-radius: 5px;
    cursor: pointer;
}
.grid-content:hover{
    background-color: #f5f5f5;
}
</style>
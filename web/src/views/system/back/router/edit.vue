<template>
    <div style="height: 100%; flex-direction: column; justify-content: space-between;">
        <el-form ref="formRef" :model="form" label-width="auto" style="height: 80%;">
            <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" placeholder="显示在系统左上角" />
            </el-form-item>
            <el-form-item label="类型">
                <el-select v-model="form.type">
                    <el-option label="菜单组" :value="0" />
                    <el-option label="页面(菜单)" :value="1" />
                    <el-option label="页面(组件, 不展示在左侧菜单)" :value="2" />
                </el-select>
            </el-form-item>
            <el-form-item label="归属系统">
                <el-select v-model="form.systemId" >
                    <el-option v-for="(system, index) in systems" :key="index" :label="system.name" :value="system.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="父级"  prop="parent.title">
                <el-select v-model="form.parentId" >
                    <el-option label="无" :value="0" />
                    <el-option v-for="(router, index) in routers" :key="index" :label="router.title" :value="router.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="编码" prop="code">
                <el-input v-model="form.code" placeholder="驼峰格式 首字母小写" />
            </el-form-item>
            <el-form-item label="路由路径"  prop="path">
                <el-input v-model="form.path" placeholder="页面访问路径，以 / 开头" />
            </el-form-item>
            <el-form-item label="文件绝对路径"  prop="componentPath">
                <el-input v-model="form.componentPath" placeholder="以 @ 开头，@ 对应 /src" />
            </el-form-item>
            <el-form-item label="Icon 图标" prop="iconName">
                <el-input v-model="form.iconName" />
            </el-form-item>
            <el-form-item label="顺序" prop="weight">
                <el-input v-model="form.weight" />
            </el-form-item>
        </el-form>
        <div style="height: 20%; display: flex; justify-content: center; padding: 20px 0;">
            <el-button  @click="event('close')" style="width: 200px; height: 50px;" >
                关闭
            </el-button>
            <el-button  @click="updateRouter" type="primary"  style="width: 200px; height: 50px;" >
                保存
            </el-button>
        </div>
    </div>
</template>
<script setup>
import { ref, reactive, onMounted, defineEmits, defineProps, watch } from 'vue';
import { 
    searchSystem as searchSystemAPI, 
    updateSystemRouter as updateSystemRouterAPI, 
    searchSystemRouterList as searchSystemRouterListAPI,
    searchSystemRouter as searchSystemRouterAPI,
} from '@/api/back.api.js'
const event = defineEmits(['close'])
const props = defineProps(['id'])

watch(
    () => props.id,
    () => research()
)

const form = reactive({
    parent: {}
})
const formRef = ref()
const systems = ref([])
const routers = ref([])

function researchSystem() {
    searchSystemAPI(res => {
        systems.value = res
        if(props.systemId > 0) {
            form.systemId = props.systemId
        }
    })
}
function researchRouterList() {
    searchSystemRouterListAPI(props.systemId, res => routers.value = res)
}
function updateRouter() {
    updateSystemRouterAPI(form, () => event('close'))
}
function research() {
    searchSystemRouterAPI(props.id, res => {
        Object.assign(form, res)
        researchSystem()
        researchRouterList()
    })
}
onMounted(() => {
    research()
})
</script>
<style scoped>
:deep(.el-dialog__body) {
    height: 100% !important;
}
</style>
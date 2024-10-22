<template>
    <div style="height: 100%; flex-direction: column; justify-content: space-between;">
        <el-form ref="formRef" :model="form" label-width="auto">
            <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" placeholder="显示在系统左上角" />
            </el-form-item>
            <el-form-item label="类型">
                <el-select v-model="form.type" @change="value => isNotPageGroup = value > 0" >
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
            <el-form-item label="父级" v-show="isNotPageGroup" prop="parent.title">
                <el-select v-model="form.parentId" >
                    <el-option v-for="(router, index) in routers" :key="index" :label="router.title" :value="router.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="编码" prop="code">
                <el-input v-model="form.code" placeholder="驼峰格式 首字母小写">
                    <template v-if="props.parent.code" #prepend>
                        <span>{{ props.parent.code }}</span>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="路由路径" v-show="isNotPageGroup" prop="path">
                <el-input v-model="form.path" placeholder="页面访问路径，以 / 开头">
                    <template v-if="props.parent.code" #prepend>
                        <span>/{{ props.parent.code }}</span>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="文件绝对路径" v-show="isNotPageGroup" prop="componentPath">
                <el-input v-model="form.componentPath" placeholder="以 @ 开头，@ 对应 /src" />
            </el-form-item>
            <el-form-item label="Icon 图标" prop="iconName">
                <el-input v-model="form.iconName" />
            </el-form-item>
        </el-form>
        <div style="display: flex; justify-content: center; margin-top: 20px;">
            <el-button  @click="createRouter" type="primary"  style="width: 200px; height: 50px;" >
                保存
            </el-button>
        </div>
    </div>
</template>
<script setup>
import { ref, reactive, onMounted, defineEmits, defineProps, watch } from 'vue';
import { searchSystem as searchSystemAPI, addSystemRouter as addSystemRouterAPI, searchSystemRouterList as searchSystemRouterListAPI } from '@/api/back.api.js'
const event = defineEmits(['create'])
const props = defineProps({
    parent: {
        required: false,
        default(rawProps) {
            return { 
                id: 0,
                title: '无',
            }
        }
    },
    systemId: {
        required: false,
        default: 0
    },
})

watch(
    () => props.parent.id,
    () => { form.parentId = props.parent.id }
)

const isNotPageGroup = ref(false)
const form = reactive({
    type: 0,
    parentId: props.parent.id,
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
function createRouter() {
    if(props.parent.code) form.path  = '/' + props.parent.code + form.path
    addSystemRouterAPI(form, () => {
        formRef.value.resetFields()
        event('create')
    })
}
onMounted(() => {
    if(props.parent.id > 0) {
        form.type = props.parent.type == 0 ? 1 : 0
        isNotPageGroup.value = true
    }
    researchSystem()
    researchRouterList()
})
</script>
<style scoped>
:deep(.el-dialog__body) {
    height: 100% !important;
}
</style>
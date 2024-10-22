<template>
    <el-card>
        <el-segmented v-model="selectSystemId" :options="systems" block @change="researchRouter" style="height: 50px;" />
        <div style="height: 40px; display: flex; justify-content: space-between; margin: 20px 0;">
            <div>
            </div>
            <div>
                <el-button @click="clickAddChildPage" type="primary" plain style="width: 80px; height: 40px;">添加</el-button>
                <el-cascader :disabled="!isSelectRouter" v-model="batchOption" :options="options" placeholder="选择操作" style="height: 40px; margin: 0 5px 0 20px;" />
                <el-button @click="execBatchOption" :disabled="!isSelectRouter" style="width: 160px; height: 40px;" type="primary">执行批量操作</el-button>
                <el-popover :width="300">
                    <template #reference>
                        <el-button type="info" :icon="Tools" style="font-size: 1.2em; width: 50px; height: 40px;" plain />
                    </template>
                    <el-checkbox v-model="selectIsAssociatChild" label="选中某个节点同时, 勾选它的全部子节点" size="large" />
                </el-popover>
            </div>
        </div>
        <el-tree
            ref="treeRef"
            :data="treeData"
            :props="defaultProps"
            :default-expand-all="true"
            :highlight-current="true"
            show-checkbox
            :expand-on-click-node="false"
            @check-change="handleCheckChange"
            draggable
            @node-drop="nodeDrop"
            :check-strictly="!selectIsAssociatChild"
        >
            <template #default="{ data }">
                <div @dblclick.stop="editRouter(data)" style="width: 100%; display: flex; justify-content: space-between;" :class="{ menuGroup: data.parentId == 0 }">
                    <div>
                        <span>{{ data.weight }}.</span>
                        <span>{{ data.title }}</span>
                    </div>
                    <div style="width: 1200px; display: flex; justify-content: space-between;">
                        <div style="width: 100px;">
                            {{ data.iconName }}
                        </div>
                        <div @click.stop="copyPath(data)" @mouseenter="data.mouseenter = true" @mouseout="data.mouseenter = false" style="width: 200px;">
                            <el-text v-if="data.mouseenter" class="mx-1" type="success" style="text-align: center;">
                                <el-icon style="margin: 5px;"><CopyDocument /></el-icon>点击复制
                            </el-text>
                            <div v-else style="text-align: left;">{{ data.path }}</div>
                        </div>
                        <div style="width: 300px;">
                            {{ data.componentPath }}
                        </div>
                        <div>
                            <el-button link :icon="CirclePlus" @click="clickAddChildPage(data)" />
                            <el-button link :icon="Edit" @click="editRouter(data)" />
                            <el-button link :icon="Delete" @click="delSystemRouterAPI(data.id, researchRouter)" />
                        </div>
                    </div>
                </div>
        </template>
        </el-tree>
    </el-card>

    <el-dialog v-model="dialogAddVisible" width="80vw" style="height: 80vh;" @close="closeAddDialog">
        <AddRouterView :systemId="selectSystemId" :parent="parent" @create="closeAddDialog" />
    </el-dialog>
    <el-dialog v-model="dialogEditVisible" width="80vw" style="height: 80vh;" @close="researchRouter">
        <EditRouterView :id="editId" @close="closeEditDialog" />
    </el-dialog>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { 
    searchSystem as searchSystemAPI, 
    searchSystemRouterTree as searchSystemRouterTreeAPI,
    delSystemRouter as delSystemRouterAPI,
    moveSystemRouter as moveSystemRouterAPI,
    batchUpdateSystemRouterType as batchUpdateSystemRouterTypeAPI,
 } from '@/api/back.api.js'
import AddRouterView from './add.vue'
import EditRouterView from './edit.vue'
import { ElMessage } from 'element-plus'
import { Tools, Edit, CirclePlus, Delete, CopyDocument, } from '@element-plus/icons-vue'

const customNodeClass = (data) => {
  if (data.parentId == 0) {
    return 'is-menu-group'
  }
  return null
}


const treeData = ref([])
const defaultProps = {
  children: 'children',
  label: 'title',
  class: customNodeClass
}
const systems = ref([])
const selectSystemId = ref()

const dialogAddVisible = ref(false)
const parent = ref()
function research() {
    researchSystem()
}
function clickAddChildPage(data) {
    if(data == undefined && isSelectRouter.value == false) {
        ElMessage.error('必须选择一个路由节点')
        return
    }
    if(data == undefined && checkRouters.value.length != 1) {
        ElMessage.error('只能同时选择一个路由节点, 进行添加子节点操作')
        return
    }
    parent.value = data != undefined ? data : checkRouters.value[0]; 
    dialogAddVisible.value = true;
}
function researchRouter() {
    searchSystemRouterTreeAPI({ systemId: selectSystemId.value }).then(({ data }) => treeData.value = data.data)
}
function researchSystem() {
    searchSystemAPI(res => {
        systems.value = res.map(system => {
            return {
                label: system.name,
                value: system.id,
            }
        })
        if(selectSystemId.value == null) {
            selectSystemId.value = systems.value[0].value
        }
        researchRouter()
    })
}
function closeAddDialog() {
    researchRouter()
    dialogAddVisible.value = false; 
}
const checkRouters = ref([])
const isSelectRouter = ref(false)
function handleCheckChange(router, isCheck, childIsCheck) {
    if(isCheck) {
        checkRouters.value.push(router)
    } else {
        checkRouters.value = checkRouters.value.filter(checkRouter => checkRouter.id != router.id)
    }
    isSelectRouter.value = checkRouters.value.length > 0
}
function nodeDrop({ data: router }, { data: toRouter }, location) {
    if(location == 'inner') {
        moveSystemRouterAPI(router.id, toRouter.id, researchRouter)
    }
}


// 批量操作
const options = [
    {
        value: '0',
        label: '批量修改',
        children: [
            {
                value: 'type',
                label: '类型',
                children: [
                    {
                        value: 0,
                        label: '菜单组',
                    },
                    {
                        value: 1,
                        label: '页面(菜单)',
                    },
                    {
                        value: 2,
                        label: '页面(组件, 不展示在左侧菜单)',
                    },
                ],
            },
        ]
    }
]

const batchOption = ref()
function execBatchOption() {
    batchUpdateSystemRouterTypeAPI(checkRouters.value.map(checkRouter => checkRouter.id), batchOption.value[2], researchRouter)
}

const selectIsAssociatChild = ref(true)
const treeRef = ref()



//编辑
const dialogEditVisible = ref(false)
const editId = ref()
function editRouter(router) {
    editId.value = router.id
    dialogEditVisible.value = true
}

function copyPath(data) {
        // 获取需要复制的元素以及元素内的文本内容
    const text = data.path;
    // 添加一个input元素放置需要的文本内容
    const input = document.createElement("input");
    input.value = text;
    document.body.appendChild(input);
    // 选中并复制文本到剪切板
    input.select();
    document.execCommand("copy");
    // 移除input元素
    document.body.removeChild(input);
    ElMessage({
        message: "复制成功",
        type: "success",
    });
}
function closeEditDialog() {
    researchRouter()
    dialogEditVisible.value = false;
}
onMounted(() => {
    research()
})
</script>
<style scoped>
:deep(.el-input__wrapper) {
    height: 40px;
}
:deep(.is-menu-group > .el-tree-node__content) {
    background-color: #f5f7fa;
}
</style>
<template>
    <div style="height: 90vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">全部患者</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">患者姓名</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%; font-size: 0.8vw;" class="search_input" v-model="form.val" placeholder="姓名/电话"
                        @change="handle.search"
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">建档时间</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="createTime" 
                        v-model="form.createTime"
                        value-format="YYYY-MM-DD"
                        type="date"
                        style="width: 60%; font-size: 0.8vw;"
                        class="search_input"
                        @change="() => method.search()"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="handle.resetClick" style="width: 5vw; border-radius: 15px; height: 5vh !important;"  type="primary" plain>
                        重置
                    </el-button>

                    <!-- <el-button class="search_button" @click='handle.backVisit()' style="width: 10vw; border-radius: 15px; height: 5vh !important;"  type="primary" plain>
                        回访
                    </el-button> -->
                </el-form-item>
            </div>
        </el-form>
        <el-card style="height: 90%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                style="height: 90%; width: 100%; font-size: 1.5vh;"
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
            >
                <el-table-column align="center" width="160" prop="createTime" label="建档时间">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertDate(row.createTime) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.name}}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="sex" label="性别" :formatter="convertSex" />
                <el-table-column align="center" prop="age" label="年龄" :formatter="convertAge" />
                <el-table-column align="center" prop="phone" label="手机" />
                <el-table-column align="center" prop="address" label="住址" />
                <el-table-column align="center" label="操作" >
                    <template #default="{ row }">
                        <el-button @click="openVxScan(row.phone)"  type="primary" plain :disabled="row.openId != null">
                            患者扫码
                        </el-button>
                    </template>
                </el-table-column>
                <!-- <el-table-column align="center" fixed="right">
                    <template #default="{ row }">
                        <el-popover
                            placement="right-start"
                            :width="300"
                            trigger="hover"
                        >
                            <template #default>
                                <div style="display: flex; align-items: center;">
                                    <el-icon color="#8cc63f" style="font-size: 2em; border: 2px solid #8cc63f; border-radius: 25px; padding: 5px;">
                                        <ArrowRightBold />
                                    </el-icon>
                                    <span style="margin-left: 20px; font-size: 1.5em;">查看接诊记录</span>
                                </div>
                            </template>
                            <template #reference>
                                <el-button 
                                    @click.prevent="method.toCure(row)"
                                    :color="row.color == undefined ? '' : row.color" 
                                    circle
                                    style="border: 2px solid #8cc63f; font-size: 20px; width: 36px; height: 36px;"
                                >
                                    <el-icon color="#8cc63f">
                                        <ArrowRightBold />
                                    </el-icon>
                                </el-button>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column> -->
            </el-table>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
    </div>
    <openVxDialog ref="openVxRef" />

    <el-dialog title="留言内容" v-model='openVisit' append-to-body
        style="width: 120vh;height: 65vh; border-radius: 15px;">
        <div style="border: 1px solid #ccc; margin-bottom: 2vh;">
            <Toolbar
                mode="simple"
                :editor='editorRef'
                style="border-bottom: 1px solid #ccc"
            />
            <Editor
                v-model='valueHtml'
                mode="simple"
                :defaultConfig='editorConfig'
                @onCreated='createdEditor'
                style="height: 500px; overflow-y: hidden;"
            />
        </div>

        <div slot="footer" class="dialog-footer" style="display: flex; justify-content: center;">
            <el-button type="primary" @click='' style="height: 50px; width: 200px;">确 定</el-button>
            <el-button @click='' style="height: 50px; width: 200px;">取 消</el-button>
        </div>
    </el-dialog>
</template>
<script setup>
import { ElNotification, } from 'element-plus';

import { ref, reactive, onMounted, watch, shallowRef, } from 'vue'
import { useRouter } from 'vue-router'
import { convertDate, convertSex, convertName, convertDiagnosis, convertIsFirst, convertAge } from '@/utils/converter.js'
import openVxDialog from './openVxDialog.vue'

import '@wangeditor/editor/dist/css/style.css';/**富文本样式 */
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';/**富文本逻辑代码 */

const routerInstance = useRouter()

import patientService from '@/api/clinic/patient.api.js'

const formRef = ref()

const form = reactive({})
const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

//打开openVxDialog扫码弹窗
const openVxRef = ref(null)
function openVxScan(phone) {
  console.log("打开");
  //调用openDialog方法
  openVxRef.value.openDialog(phone)
}

watch(
    () => form.val,
    () => {
        method.search()
    }
)

const method = {
    setPage(page) {
        current.value =parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {
        patientService.selectPatient({ ...this.getPage() }, (response) => {
            tableData.value = response.records
            this.setPage(response)
        })
    },
    toCure(row) {
        let routeData = routerInstance.resolve({
            path: `/clinic/cure/` + row.id,
            query: { 
                title: row.name,
                index: row.id
            }
        });
        window.open(routeData.href, "_blank");
    }
}

const handle = {
    searchClick() {
        method.search()
    },
    resetClick() {
        formRef.value.resetFields()
        form.createTime = null
        form.val = null
        method.search()
    },
    pageCurrentChange() {
        resetVisit()
        method.search()
    },
    backVisit() {
        if (backVisitArr.value.length < 1) {
            ElNotification({
                title: '回访', message: '请勾选当前分页患者回访', type: 'warning',
            })
            return;
        }

        let patIdArr = new Array();
        backVisitArr.value.forEach(v => {
            patIdArr.push(v.id);
        });


        openVisit.value=true;
    },
}

onMounted(() => {
    let routerQuery = routerInstance.currentRoute.value.query
    if(routerQuery && routerQuery.val) {
        form.val = routerQuery.val
    }
    method.search()
})



//回访id列表相关
const isAllBack = ref(false);/**当前分页是否全部回访 */
const backVisitArr = ref([]);/**回访患者数组 */
/**当前分页勾选所有 */
function allBack(selection) {
    backVisitArr.value = selection;
    isAllBack.value = selection.length - tableData.value.length > -1;
}

/**
 * 当前分页单个勾选
 */
function oneBack(selection) {
    allBack(selection);
}

/**重置回访相关变量 */
function resetVisit() {
    isAllBack.value = false;
    backVisitArr.value.length = 0;
}



//回访富文本相关
const openVisit = ref(false);
const editorRef = shallowRef();
const valueHtml = ref("");
const editorConfig = { placeholder: '请输入回访内容' }

function createdEditor(editor) {
    editorRef.value = editor // 记录 editor 实例，重要！
}

/**销毁富文本编辑器 */
function destroyEditor() {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
}
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
}
:deep(.el-table__header) {
    height: 6vh;
    background-color: #6a5acd;
}
:deep(.el-table__header .cell) {
    font-weight: 700;
}
:deep(.el-form-item) {
    margin-bottom: 0;
}
</style>
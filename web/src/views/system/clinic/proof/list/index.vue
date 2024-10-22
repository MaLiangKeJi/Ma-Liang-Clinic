 <template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">历史证明</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" placeholder="姓名/电话" 
                        @change="method.search"
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">诊断日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="createTime" 
                        v-model="form.createTime"
                        value-format="YYYY-MM-DD"
                        type="date"
                        style="width: 60%;"
                        class="search_input"
                        @change="() => method.search()"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="handle.resetClick" style="width: 10vw; border-radius: 5px; height: 6vh !important;"  type="primary" plain>
                        重置
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                height="100%"
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
            >
                <el-table-column align="center" prop="visitDate" label="就诊日期" :formatter="method.convertDate" />
                <el-table-column align="center" prop="name" label="姓名" />
                <el-table-column align="center" prop="sex" label="性别" :formatter="method.convertSex" />
                <el-table-column align="center" prop="age" label="年龄" :formatter="method.convertAge" />
                <el-table-column align="center" prop="address" label="家庭住址或工作单位" />
                <el-table-column align="center" prop="diagnosis" label="诊断意见" />
                <el-table-column align="center" prop="dealWith" label="治疗经过及建议" />
                <el-table-column>
                    <template #default="scope">
                        <el-button type="text" @click="handle.edit(scope.row)">查看</el-button>
                        <el-button type="text" @click="handle.delete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
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
</template>
<script setup>
import { ref, onMounted, reactive } from 'vue'
import { proofService as service } from '@/api/clinic/index.js'
import { useRouter } from 'vue-router'

const router = useRouter()


const formRef = ref()

const form = reactive({})
const tableData = ref([])

//分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)


const handle = {
    pageCurrentChange() {
        method.search()
    },
    edit(row) {
        router.push({
            path: '/clinic/proof/add',
            query: {
                id: row.id
            }
        })
    },
    delete(row) {
        service.delete({id:row.id}, (response) => {
            method.search()
        })
    },
    resetClick() {
        formRef.value.resetFields()
        form.createTime = null
        form.val = null
        method.search()
    },
}

const method = {
    setPage(page) {
        current.value = parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {
        service.search({ ...this.getPage(), value: form.val ,createTime: form.createTime}, (response) => {
            tableData.value = response.records
            this.setPage(response)
        })
    },
    convertDate(row, column, cellValue, index){   //定义日期格式转换函数
        let date = new Date(cellValue)
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');
        let dateString = `${year}-${month}-${day}`;
        return dateString  //拼接成yyyy-mm-dd形式的字符串
    },
    convertSex(row, column, cellValue, index) {
        return cellValue == 0 ? '女' : '男'
    },
    convertAge(row, column, cellValue, index) {
        return cellValue + '岁'
    },
    convertAddr(row, column, cellValue, index) {
        return cellValue ? cellValue : '无'
    },
}


onMounted(() => {
    method.search()
})
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

.exporttable {
    border: solid 1px #000000;
}
:deep(.el-descriptions__label) {
    width: 120px;
}
:deep(.el-descriptions__body .el-descriptions__table.is-bordered .el-descriptions__cell) {
    border-color: #000000;
}
:deep(.el-input, .el-input__wrapper) {
    height: 100%;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
</style>
<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 30%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">上架</el-text>
            </div>
        </el-form>
        <el-card style="height: 80%; border-radius: 15px; padding: 2%;">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="药品" name="drug">
                    <el-row justify="end" :gutter="24">
                        <el-col :span="2">
                            <el-button @click="onAddItem" type="success" plain>
                                <el-icon class="icon-text-gap"><Plus /></el-icon>
                                添加
                            </el-button>
                        </el-col>
                        <el-col :span="2">
                            <el-button type="primary" plain>
                                <el-icon class="icon-text-gap"><Document /></el-icon>
                                批量上传
                            </el-button>
                        </el-col>
                    </el-row>
                    <el-row justify="space-between" :gutter="24">
                        <el-col :span="24">
                            <el-table :data="tableData" style="width: 100%">
                                <el-table-column fixed prop="date" label="封面图">
                                    <template #default="{ row }">
                                        <el-upload
                                            class="upload-demo"
                                            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                                            :before-upload="file => beforeUploadDrugCover(file, row)"
                                        >
                                            <el-image v-if="row.imageUrl" :src="row.imageUrl" @click="file => beforeUploadDrugCover(file, row)" style="width: 50px; height: 50px;" />
                                            <el-button v-else type="primary">未上传封面图</el-button>
                                        </el-upload>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="approvalNumber" label="国药准字">
                                    <template #default="{ row }">
                                        <el-input v-model="row.approvalNumber" placeholder="从字母开始（含字母）" />
                                    </template>
                                </el-table-column>
                                <el-table-column prop="name" label="标题（默认：厂商名称 药品名规格）" width="300">
                                    <template #default="{ row }">
                                        <el-input v-model="row.name" placeholder="维宏 阿奇霉素分散片0.25g*12片" />
                                    </template>
                                </el-table-column>
                                <el-table-column prop="batchNo" label="批次号">
                                    <template #default="{ row }">
                                        <el-input v-model="row.batchNo" placeholder="" />
                                    </template>
                                </el-table-column>
                                <el-table-column prop="expirationTime" label="过期时间">
                                    <template #default="{ row }">
                                        <el-date-picker
                                            v-model="row.expirationTime"
                                            style="width: 100%;"
                                            type="date"
                                        />
                                    </template>
                                </el-table-column>
                                <el-table-column prop="isShelf" label="是否直接上架">
                                    <template #default="{ row }">
                                        <el-checkbox v-model="row.isShelf" label="直接上架" size="large" />
                                    </template>
                                </el-table-column>
                                <el-table-column prop="price" label="价格">
                                    <template #default="{ row }">
                                        <el-input v-model="row.price" placeholder="" />
                                    </template>
                                </el-table-column>
                                <el-table-column fixed="right" label="操作" min-width="120">
                                  <template #default="{ $index }">
                                    <el-button
                                      link
                                      type="danger"
                                      size="small"
                                      @click.prevent="() => deleteRow($index)"
                                    >
                                      删除
                                    </el-button>
                                  </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                    <el-row justify="center" :gutter="24" style="margin-top: 5vh;">
                        <el-col :span="4">
                            <el-button @click="upload" style="width: 100%; height: 5vh;" type="primary" plain>上传</el-button>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="器械" name="instrument">器械</el-tab-pane>
              </el-tabs>
        </el-card>
    </div>
</template>
<script setup>
import { DocumentCopy } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { convertDate, convertName } from '@/utils/converter.js'
import { ElMessage } from 'element-plus'

const routerInstance = useRouter()

const formRef = ref()
const activeName = ref('drug')

// 存储 base64 编码的图片
const imageUrl = ref('');
const tableData = ref([{}])
function beforeUploadDrugCover(file, row) {
     // 检查文件是否为图片
    if (!file.type.startsWith('image/')) {
        return false;
    }

    // 将图片转换为 base64 编码
    const reader = new FileReader();
    reader.onload = (e) => {
        row.imageUrl = e.target.result;
    };
    reader.readAsDataURL(file);

    // 返回 false 表示阻止文件上传
    return false;
}
function onAddItem() {
  tableData.value.push({})
}
function deleteRow(index) {
  tableData.value.splice(index, 1)
}
function upload() {

}
onMounted(() => {
})
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
    width: 100%;
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
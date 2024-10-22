<template>
    <el-form :model="form" :rules="rules" label-width="120px" style="width: 50%; margin: 0 25%">
        <el-form-item label="资产类别名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入资产类别名称" />
        </el-form-item>
        <el-form-item label="折旧方法" prop="depreciationMethod">
            <el-select v-model="form.depreciationMethod">
                <el-option label="不提折旧" :value="0" />
                <el-option label="平均年限法" :value="1" />
                <el-option label="双倍余额递减法" :value="2" />
            </el-select>
        </el-form-item>
        <el-form-item label="预计使用年限" prop="durableYears">
            <el-input v-model="form.durableYears" placeholder="请输入预计使用年限" />
        </el-form-item>
        <el-form-item label="预计残值率" prop="ratioRemaining">
            <el-input v-model="form.ratioRemaining" placeholder="请输入预计残值率" />
        </el-form-item>
        <el-form-item label="固定资产科目" prop="fixedAssetsAccountId">
            <el-select-v2
                v-model="form.fixedAssetsAccountId"
                :filterable="true"
                :remote="true"
                :options="accountOptions"
                :props="props"
                :remote-method="searchAccount"
                :loading="searchLoading"
                loading-text="搜索中..."
                placeholder="请选择固定资产科目"
                style="position: relative; top: -4px;"
                placement="bottom"
            >
                <template #default="{ item }">
                    <div style="display: flex; justify-content: space-between; color: #000;">
                        <div>
                            {{ item.accountText }}
                        </div>
                        <div>
                            {{ item.accountSortText }}
                        </div>
                    </div>
                </template>
                <template #footer>
                    <el-button @click="showSearchFixedAssetsAccount($index)" :icon="Search" text style="width: 100%;" >查询更多</el-button>
                </template>
            </el-select-v2>
        </el-form-item>
        
        <el-form-item label="折旧科目" prop="depreciationAccountId">
            <el-select-v2
                v-model="form.depreciationAccountId"
                :filterable="true"
                :remote="true"
                :options="accountOptions"
                :props="props"
                :remote-method="searchAccount"
                :loading="searchLoading"
                loading-text="搜索中..."
                placeholder="请选择折旧科目"
                style="position: relative; top: -4px;"
                placement="bottom"
            >
                <template #default="{ item }">
                    <div style="display: flex; justify-content: space-between; color: #000;">
                        <div>
                            {{ item.accountText }}
                        </div>
                        <div>
                            {{ item.accountSortText }}
                        </div>
                    </div>
                </template>
                <template #footer>
                    <el-button @click="showSearchDepreciationAccount($index)" :icon="Search" text style="width: 100%;" >查询更多</el-button>
                </template>
            </el-select-v2>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
    </el-form>
    <div style="width: 100%; height: 10%; display: flex; justify-content: center;">
        <el-button @click="cancel" style="width: 200px; height: 50px;" >
            取消
        </el-button>
        <el-button @click="create" type="primary" style="width: 200px; height: 50px;" >
            创建
        </el-button>
    </div>
</template>
<script setup>
import { reactive, defineEmits, ref } from 'vue'
import { addType as addTypeAPI } from '@/api/assetType.api.js'
import { joinListAccount } from "@/api/account.js";
import { Search, } from '@element-plus/icons-vue'

const event = defineEmits(['cancel', 'create'])
const form = reactive({
    depreciationMethod: 0,
})
const accountOptions = ref([])
const props = {
    value: 'id',
    label: 'accountText',
}
// 表单校验
const rules = {
    name: [
        { required: true, message: "资产类别名称不能为空", trigger: "blur" }
    ],
}

const searchLoading = ref(false)
function searchAccount(query) {
  searchLoading.value = true
  let param = {
      current: 1,
      size: 50,
  }
  if (query !== '') {
      if (!isNaN(Number(query))) {
          param.no = query
      } else {
          param.name = query
      }
  }
  joinListAccount(param).then(({ data }) => {
      searchLoading.value = false
      let response = data.data
      accountOptions.value = response.records.map(item => {
          item.accountText = item.no + ' ' + item.name
          item.accountSortText = (item.accountSort ? item.accountSort : '') + (item.direction ? '(' + item.direction + ')' : '')
          return item
      })
  });
}
function cancel() {
    event('cancel')
}
function create() {
    let param = {
        ...form,
    }
    addTypeAPI(param).then(({ data }) => {
        param.id = data.data
        event('create', param)
    })
}
</script>
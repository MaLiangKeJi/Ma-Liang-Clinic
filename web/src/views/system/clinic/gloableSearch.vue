<template>
    <div style="padding: 5vh; height: 70vh;">
        <el-input
            ref="searchRef"
            v-model="searchVal"
            size="large"
            placeholder="搜姓名、手机号、住址、药品、库存、论坛..."
            style="height: 10%; font-size: 1.2em;"
            clearable
            class="gloable_search_input"
            @clear="() => { clearSearchResult(); isSearch = false; }"
        >
            <template #append>
                <el-button @click="globalSearch" style="width: 5vw;">
                    <el-icon size="1.2em" ><Search /></el-icon>
                    <span style="font-weight: 700;">搜索</span>
                </el-button>
            </template>
        </el-input>
        <div v-show="isSearch" style="height: 88%; margin-top: 2vh; position: relative">
            <el-scrollbar v-show="!chart" height="100%">
                <div>
                    <el-card shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>AI 回答</h2>
                            </div>
                        </template>
                        <transition v-if="aiLoading" name="el-fade-in-linear">
                            <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                                <div class="spinner" style="width: 60px; height: 60px;">
                                    <div class="double-bounce1"></div>
                                    <div class="double-bounce2"></div>
                                </div>
                            </div>
                        </transition>
                        <div v-else style="height: 100%;">
                            <span style="font-size: 1.2em;">{{ searchAI }}</span>
                        </div>
                    </el-card>

                    <el-card v-if="searchPatients.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>患者</h2>
                            </div>
                        </template>
                        <el-table :data="searchPatients" style="width: 100%;"  max-height="300">
                            <el-table-column prop="name" label="姓名" />
                            <el-table-column prop="sex" label="性别" />
                            <el-table-column prop="phone" label="手机号" />
                            <el-table-column prop="address" label="住址" />
                            <el-table-column align="right" width="400">
                                <template #default="{ $index, row }">
                                    <el-button size="small" type="primary" plain style="font-size: 12px;">新增接诊</el-button>
                                    <el-button size="small" plain @click="() => toAdmissionLog(row)">就诊历史</el-button>
                                    <el-button size="small" type="danger" @click="handleDelete($index, row)">
                                        删除档案
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
    
                    <el-card v-if="searchDrugs.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>药品</h2>
                            </div>
                        </template>
                        <el-table :data="searchDrugs" style="width: 100%;" max-height="300">
                            <el-table-column prop="name" label="药品" />
                            <el-table-column prop="type" label="剂型" />
                            <el-table-column prop="approvalNumber" label="批准文号" />
                            <el-table-column prop="manufacturer" label="生产单位" />
                        </el-table>
                    </el-card>
    
                    <el-card v-if="stockBatches.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>库存</h2>
                            </div>
                        </template>
                        <el-table :data="stockBatches" style="width: 100%;"  max-height="300">
                            <el-table-column prop="name" label="药品">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.stock.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="spec" label="规格" />
                            <el-table-column prop="dosageForm" label="剂型" />
                            <el-table-column prop="manufacturer" label="生产单位" />
                            <el-table-column prop="number" label="库存" >
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.number + row.unit.name }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="searchQuestion.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>论坛: 相关问题</h2>
                            </div>
                        </template>
                        <el-table :data="searchQuestion" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="问题">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 700;"><a :href="'https://forum.maliang.work/questions/'+object.question_id" target="_blank">{{ object.title }}</a></span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="摘要">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 500;">{{ object.excerpt }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="标签">
                                <template #default="{ row: { object } }">
                                    <el-tag v-for="(tag, index) in object.tags" :key="index" type="primary" effect="plain">
                                        <span style="font-weight: 700;">{{ tag.display_name }}</span>
                                    </el-tag>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="underStockDurgs.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>库存不足药品</h2>
                            </div>
                        </template>
                        <el-table :data="underStockDurgs" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="问题">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 700;"><a :href="'https://forum.maliang.work/questions/'+object.question_id" target="_blank">{{ object.title }}</a></span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="摘要">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 500;">{{ object.excerpt }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="标签">
                                <template #default="{ row: { object } }">
                                    <el-tag v-for="(tag, index) in object.tags" :key="index" type="primary" effect="plain">
                                        <span style="font-weight: 700;">{{ tag.display_name }}</span>
                                    </el-tag>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="expireStockDurgs.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>已过期库存药品</h2>
                            </div>
                        </template>
                        <el-table :data="expireStockDurgs" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="名称">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.stock.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="批次">
                                <template #default="{ row }">
                                    <span style="font-weight: 500;">{{ row.batchNumber	 }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="库存量">
                                <template #default="{ row }">
                                    <span style="font-weight: 500;">{{ row.number + row.unit.name }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="aboutExpireStockDurgs.length > 0" shadow="never" style="margin-top: 20px;">
                        <template #header>
                            <div class="card-header">
                                <h2>临期库存药品</h2>
                            </div>
                        </template>
                        <el-table :data="aboutExpireStockDurgs" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="问题">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 700;"><a :href="'https://forum.maliang.work/questions/'+object.question_id" target="_blank">{{ object.title }}</a></span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="摘要">
                                <template #default="{ row: { object } }">
                                    <span style="font-weight: 500;">{{ object.excerpt }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="标签">
                                <template #default="{ row: { object } }">
                                    <el-tag v-for="(tag, index) in object.tags" :key="index" type="primary" effect="plain">
                                        <span style="font-weight: 700;">{{ tag.display_name }}</span>
                                    </el-tag>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="searchAdmissionLogs && searchAdmissionLogs.length > 0">
                        <template #header>
                            <div class="card-header">
                                <h2>以下是最近3天的全部就诊记录</h2>
                            </div>
                        </template>
                        <el-table :data="searchAdmissionLogs" @row-click="(row) => toCure(row)" style="width: 100%; height: 100%" row-style="cursor: pointer;">
                            <el-table-column prop="title" label="就诊日期">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ formatDate(row.createTime) }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="title" label="姓名">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="诊断">
                                <template #default="{ row }">
                                    <span style="font-weight: 500;">{{ row.diagnosis }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="searchUnPays && searchUnPays.length > 0">
                        <template #header>
                            <div class="card-header">
                                <h2>以下是全部欠费记录</h2>
                            </div>
                        </template>
                        <el-table :data="searchUnPays" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="就诊日期">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ formatDate(row.createTime) }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="title" label="姓名">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.patient.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="金额">
                                <template #default="{ row }">
                                    <span style="font-weight: 500; color: red;">{{ row.fee }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column align="right" width="400">
                                <template #default="{ $index, row }">
                                    <el-button @click="() => unPayReceipt(row)" size="small" type="primary" plain style="font-size: 12px;">收款</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="searchUnderStockDrugs && searchUnderStockDrugs.length > 0">
                        <template #header>
                            <div class="card-header">
                                <h2>以下是全部的库存不足药品</h2>
                            </div>
                        </template>
                        <el-table :data="searchUnderStockDrugs" style="width: 100%; height: 100%">
                            <el-table-column prop="title" label="药品名称">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.stock.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="title" label="规格">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.spec }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="title" label="厂商">
                                <template #default="{ row }">
                                    <span style="font-weight: 700;">{{ row.manufacturer }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="type" label="过期时间">
                                <template #default="{ row }">
                                    <span style="font-weight: 500;">{{ formatDate(row.expiryDate) }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column align="right" width="400">
                                <template #default="{ $index, row }">
                                    <el-button @click="() => replenishOption(row)" size="large" type="primary" plain style="font-size: 1.2em;">补货</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-card v-if="searchExpireStockDrugs && searchExpireStockDrugs.length > 0">
                        <template #header>
                            <div class="card-header">
                                <h2>以下是全部的过期、临期药品</h2>
                            </div>
                        </template>
                        <div style="min-height: 30vh;">
                            <div v-for="(searchExpireStockDrug, index) in searchExpireStockDrugs" :key="index" 
                                style="height: 5vh; display: flex;" 
                                :style="{ 'border-bottom': index < searchExpireStockDrugs.length -1 ? '1px solid #dadada' : ''}"
                            >
                                <div style="width: 80%; height: 100%; display: flex; align-items: center;">
                                    <span style="font-size: 1.2em;">{{ searchExpireStockDrug.name }}</span>
                                    <el-tag v-if="searchExpireStockDrug.isExistsSameManufacturerDifferentBatchDrugs" type="info" round style="height: 50%; margin-left: 2%;">同厂商，其他批次药品可用</el-tag>
                                    <el-tag v-else-if="searchExpireStockDrug.isExistsDifferentManufacturerDrugs" type="warning" round style="height: 50%; margin-left: 2%;">有其他厂商同名药品</el-tag>
                                    <el-tooltip v-else content="相同名称药品，没有库存" placement="right">
                                        <el-tag type="danger" effect="light" round style="height: 50%; margin-left: 2%;">库存不足</el-tag>
                                    </el-tooltip>
                                </div>
                                <div style="width: 20%; height: 100%; display: flex; justify-content: flex-end; align-items: center;">
                                    <el-button @click="() => replenishOption(searchExpireStockDrug)" size="large" type="primary" plain style="font-size: 1.2em;">补货</el-button>
                                </div>
                            </div>
                        </div>
                    </el-card>
                    <transition name="el-fade-in-linear">
                        <div v-if="replenishs && replenishs.length > 0" style="height: 50vh; margin-top: 40px;">
                            <el-card v-for="(row, index) in replenishs" style="height: 100%;">
                                <template #header>
                                    <div class="card-header">
                                        <h2>智能补货：{{ row.stock.name }}</h2>
                                    </div>
                                </template>
                                <div v-if="row.step >= 0">
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >= 0" style="display: flex; align-items: center; margin-bottom: 0.8em;">
                                            <el-icon v-if="row.stepResult[0]" size="2em" color="#67c23a"><CircleCheck /></el-icon>
                                            <div v-else class="spinner" style="width: 2em; height: 2em;">
                                                <div class="double-bounce1"></div>
                                                <div class="double-bounce2"></div>
                                            </div>
                                            <el-text class="mx-1" style="margin-left: 1em;">输入补货数量</el-text>
                                            <el-input v-model="row.replenishNumber" style="width: 5%; min-width: 80px; height: 3em; margin: 0 2%;" />
                                            <el-select
                                                v-model="row.replenishUnit"
                                                placeholder="单位"
                                                size="large"
                                                style="width: 5%; min-width: 80px; height: 3em; margin-right: 2%;"
                                            >
                                                <el-option label="箱" :value="0" />
                                            </el-select>
                                            <el-button v-if="row.step == 0" @click="() => startTime(row)" type="success" plain style="height: 3em;">确定</el-button>
                                        </div>
                                    </transition>
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >= 1" style="display: flex; align-items: center; margin-bottom: 0.8em;">
                                            <el-icon v-if="row.stepResult[1]" size="2em" color="#67c23a"><CircleCheck /></el-icon>
                                            <div v-else class="spinner" style="width: 2em; height: 2em;">
                                                <div class="double-bounce1"></div>
                                                <div class="double-bounce2"></div>
                                            </div>
                                            <el-text class="mx-1" style="margin-left: 1em;">检索可用的补货渠道...</el-text>
                                        </div>
                                    </transition>
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >= 2" style="display: flex; align-items: center; margin-bottom: 1em;">
                                            <el-icon size="2em" color="#67c23a"><CircleCheck /></el-icon>
                                            <el-text class="mx-1" style="margin-left: 1em;">查询到共<span style="font-weight: 700;"> 6 个补货渠道</span></el-text>
                                        </div>
                                    </transition>
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >= 3" style="display: flex; align-items: center; margin-bottom: 1em;">
                                            <el-icon v-if="row.stepResult[3]" size="2em" color="#67c23a"><CircleCheck /></el-icon>
                                            <div v-else class="spinner" style="width: 2em; height: 2em;">
                                                <div class="double-bounce1"></div>
                                                <div class="double-bounce2"></div>
                                            </div>
                                            <el-text class="mx-1" style="margin-left: 1em;">正在比较价格中...</el-text>
                                        </div>
                                    </transition>
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >=4" style="display: flex; align-items: center; margin-bottom: 1em;">
                                            <el-icon size="2em" color="#67c23a"><CircleCheck /></el-icon>
                                            <el-text class="mx-1" style="margin-left: 1em; font-weight: 700;">最低渠道单价 1.36，最高 2.12 元</el-text>
                                        </div>
                                    </transition>
                                    <transition name="el-fade-in-linear">
                                        <div v-if="row.step >= 4" style="display: flex; align-items: center; margin-bottom: 1em;">
                                            <el-button plain>展示全部渠道</el-button>
                                            <el-button type="success" plain @click="() => replenish(row)">最低价渠道补货（1.36 元）</el-button>
                                        </div>
                                    </transition>
                                </div>
                                <el-result
                                        v-else
                                        icon="success"
                                        title="已下单"
                                        sub-title="您可以在【采购】功能查看进度"
                                        style="height: 100%; z-index: 999;"
                                    >
                                        <template #extra>
                                            <el-button type="primary" @click="() => replenishs = []">完成</el-button>
                                        </template>
                                    </el-result>
                            </el-card>
                        </div>
                    </transition>
                    <el-card 
                        v-if="searchResultIsNull" style="width: 100%; height: 100%; border-radius: 15px;" class="no_search_result">
                        <div style="display: flex; height: 100%;">
                            <div style="width: 60%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                                <el-button link style="height: 20%; font-size: 2em;">
                                    没有搜索到内容... 试试问 AI 吧
                                </el-button>
                                <el-button text style="width: 60%; height: 60%; border-radius: 15px;">
                                    <el-icon size="20em">
                                        <StarIcon />
                                    </el-icon>
                                </el-button>
                            </div>
                            <div style="width: 40%; height: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                                <div style="width: 100%; height: 20%; display: flex; align-items: center; border-bottom: 1px solid #dadada;">
                                    <el-text class="mx-1" type="info" style="font-size: 1.2em;">布洛芬都有哪些规格？</el-text>
                                </div>
                                <div style="width: 100%; height: 20%; display: flex; align-items: center; border-bottom: 1px solid #dadada;">
                                    <el-text class="mx-1" type="info" style="font-size: 1.2em;">英太青的不良反应都有什么？</el-text>
                                </div>
                                <div style="width: 100%; height: 20%; display: flex; align-items: center; border-bottom: 1px solid #dadada;">
                                    <el-text class="mx-1" type="info" style="font-size: 1.2em;">上呼吸道感染的症状有哪些？</el-text>
                                </div>
                                <div style="width: 100%; height: 20%; display: flex; align-items: center; border-bottom: 1px solid #dadada;">
                                    <el-text class="mx-1" type="info" style="font-size: 1.2em;">过敏性皮炎都有哪些症状？</el-text>
                                </div>
                            </div>
                        </div>
                    </el-card>
                </div>
            </el-scrollbar>
            <el-card v-show="chart" style="width: 100%; height: 50vh">
                <div id="echarts6" style="height: 50vh; width: 50vw;"></div>
            </el-card>
            <transition name="el-fade-in-linear">
                <el-card v-show="globalSearchLoading" style="width: 100%; height: 50vh; z-index: 999; position: absolute; top: 0;">
                    <div style="height: 100%; width: 100%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                        <div class="spinner" style="width: 60px; height: 60px;">
                            <div class="double-bounce1"></div>
                            <div class="double-bounce2"></div>
                        </div>
                    </div>
                </el-card>
            </transition>
        </div>
        <div v-show="!isSearch">
            <!-- <el-card
                style="height: 30%; margin-top: 2%; display: flex; border-radius: 8px;"
            >
                <div style="display: flex; justify-content: space-between;">
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;" >
                        <el-icon size="2em" color="#f56c6c"><FirstAidKit /></el-icon>
                    </div>
                    <div style="width: 90%; height: 100%; display: flex; flex-direction: column; align-items: flex-start;">
                        <span style="color: #f56c6c; font-size: 1.2em; font-weight: 700;">
                            有库存不足的药品
                        </span>
                        <el-text class="mx-1" type="info" style="width: 100%;">及时补货、优先使用</el-text>
                    </div>
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;">
                        <el-icon size="2em" color="#898795" style="transform: rotate(90deg);"><MoreFilled /></el-icon>
                    </div>
                </div>
            </el-card> -->
            <!-- <el-card style="height: 30%; margin-top: 2%; display: flex; border-radius: 8px;">
                <div style="display: flex;">
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;" >
                        <el-icon size="2em" color="#898795"><ShoppingCart /></el-icon>
                    </div>
                    <div style="width: 90%; height: 100%; display: flex; flex-direction: column; align-items: flex-start;">
                        <span style="font-size: 1.2em; font-weight: 700;">
                            零售
                        </span>
                        <el-text class="mx-1" type="info" style="width: 100%;">单独售卖药品、查询零售记录</el-text>
                    </div>
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;">
                        <el-icon size="2em" color="#898795" style="transform: rotate(90deg);"><MoreFilled /></el-icon>
                    </div>
                </div>
            </el-card> -->
            <!-- <el-card style="height: 30%; margin-top: 2%; display: flex; border-radius: 8px;">
                <div style="display: flex;">
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;" >
                        <el-icon size="2em" color="#898795"><Coin /></el-icon>
                    </div>
                    <div style="width: 90%; height: 100%; display: flex; flex-direction: column; align-items: flex-start;">
                        <span style="font-size: 1.2em; font-weight: 700;">
                            待付款
                        </span>
                        <el-text class="mx-1" type="info" style="width: 100%;">赊账的病人、顾客</el-text>
                    </div>
                    <div style="width: 5%; display: flex; justify-content: flex-start; align-items: center;">
                        <el-icon size="2em" color="#898795" style="transform: rotate(90deg);"><MoreFilled /></el-icon>
                    </div>
                </div>
            </el-card> -->
        </div>
    </div>
</template>
<script setup>
import { CircleCheck } from '@element-plus/icons-vue'
import { ref, inject, nextTick, defineExpose } from 'vue'
import { search as searchAPI, searchAi as searchAiAPI, searchUnderStock as searchUnderStockAPI, searchExpiredDrug as searchExpiredDrugAPI } from '@/api/clinic/search.api.js'
import answerApi from '@/api/clinic/answer.api.js'
import StarIcon from './starIcon.vue'
import { useRouter } from 'vue-router'
import payService from '@/api/clinic/pay.api.js'

const searchRef = ref()

async function inputFocus() {
    await nextTick()
    searchRef.value.focus()
}

defineExpose({
    inputFocus
})

const searchVal = ref('')

const routerInstance = useRouter()

const Echarts = inject('echarts');


const isClickSearchBtn = ref(false)
const underStockDurgs = ref([])
const aboutExpireStockDurgs = ref([])
const expireStockDurgs = ref([])

const globalSearchLoading = ref(false)
const searchPatients = ref([])
const searchDrugs = ref([])
const stockBatches = ref([])
const searchQuestion = ref([])
const searchAdmissionLogs = ref([])
const searchUnPays = ref([])
const searchUnderStockDrugs	= ref([])
const searchExpireStockDrugs = ref([])
const searchAI = ref()

const aiLoading = ref(false)

let chart = ref(null)
const searchResultIsNull = ref(false)
const isSearch = ref(false)

const needReplenishDrugs = ref([])

function globalSearch() {
    isSearch.value = true
    globalSearchLoading.value = true
    aiLoading.value = true
    if(isClickSearchBtn.value) return 
    let val = searchVal.value
    // if(val == "我需要最近3天的就诊记录") {
    //     searchAPI("我需要最近3天的就诊记录", res => {
    //         clearSearchResult()
    //         searchAdmissionLogs.value = res.admissionLogs || []
    //     })
    //     return
    // } else if(val == '全部欠费记录') {
    //     searchAPI("全部欠费记录", res => {
    //         clearSearchResult()
    //         searchUnPays.value = res.unPays || []
    //     })
    //     return
    // } else if(val == '给我今年的药品使用情况') {
    //     searchAPI("给我今年的药品使用情况", res => {
    //         clearSearchResult()
    //         if(chart.value == null) {
    //             chart.value = Echarts.init(document.getElementById("echarts6"));
    //         }
    //         const chartOption = {
    //             title: {
    //                 text: '以下是今年的药品使用情况，并以图表的方式展示',
    //                 left: 'center'
    //             },
    //             tooltip: {
    //                 trigger: 'item'
    //             },
    //             legend: {
    //                 orient: 'vertical',
    //                 left: 'left'
    //             },
    //             series: [
    //                 {
    //                     name: '今年的药品使用情况',
    //                     type: 'pie',
    //                     radius: '50%',
    //                     data: res.useList,
    //                     emphasis: {
    //                         itemStyle: {
    //                             shadowBlur: 10,
    //                             shadowOffsetX: 0,
    //                             shadowColor: 'rgba(0, 0, 0, 0.5)'
    //                         }
    //                     }
    //                 }
    //             ]
    //         };
    //         chart.value.setOption(chartOption);
    //         // 根据页面大小自动响应图表大小
    //         window.addEventListener("resize", function () {
    //             chart.value.resize();
    //         })
    //         globalSearchLoading.value = false
    //     })
    //     return
    // } else if(val == '库存不足') {
    //     searchAPI("库存不足", res => {
    //         clearSearchResult()
    //         searchUnderStockDrugs.value = res.underStockDrugs || []
    //         globalSearchLoading.value = false
    //     })
    //     return
    // } else if(val == '即将过期') {
    //     searchAPI("即将过期", res => {
    //         clearSearchResult()
    //         searchExpireStockDrugs.value = res || []
    //     })
    //     return
    // }
    if(val && val != '') {
        let execEndOrder = 0
        searchAPI(searchVal.value, res => {
            if(execEndOrder == 0) { //判断是否先执行完毕
                execEndOrder = 1
                clearSearchResult() //先执行完毕的请求，需要先 clear 一下
            }
            searchPatients.value = res.patients || []
            searchDrugs.value = res.drugs || []
            stockBatches.value = res.stockBatches || []
            if(execEndOrder == 2) {
                globalSearchLoading.value = false
            }
        })
        searchAI.value = ''
        searchAiAPI(searchVal.value, res => {
            searchAI.value = res
            aiLoading.value = false
        })
        answerApi.search({
            q: "is:question" + val,
            order: 'relevance',
            page: 1,
            size: 10,
        }, ({ count, list }) => {
            if(execEndOrder == 0) { //
                execEndOrder = 2
                clearSearchResult()
            }
            searchQuestion.value = list || []
            if(execEndOrder == 1) {
                globalSearchLoading.value = false
            }
        })
    }
}

function searchUnderStockDurgs() {
    isClickSearchBtn.value = true
    searchVal.value = '库存不足药品'
    searchUnderStockAPI(stockShortage => {
        clearSearchResult()
        underStockDurgs.value = stockShortage
        isClickSearchBtn.value = false
    })
}
function searchExpiredDrugs() {
    isClickSearchBtn.value = true
    searchVal.value = '临期、过期药品'
    searchExpiredDrugAPI(({  aboutExpires, expires }) => {
        clearSearchResult()
        aboutExpireStockDurgs.value = aboutExpires
        expireStockDurgs.value = expires
        isClickSearchBtn.value = false
    })
}
function clearSearchResult() {
    chart.value = null
    searchAdmissionLogs.value = []
    searchUnPays.value = []
    searchPatients.value = []
    searchDrugs.value = []
    searchQuestion.value = []
    underStockDurgs.value = []
    aboutExpireStockDurgs.value = []
    expireStockDurgs.value = []
    searchAI.value = null
}
function formatDate(dateStr) {
    let date = new Date(dateStr)
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2); // 加1是因为 getMonth() 返回的是从0开始的月份
    var day = ('0' + date.getDate()).slice(-2);
    return year + '-' + month + '-' + day;
}
function toCure(row) {
    let routeData = routerInstance.resolve({
        path: `/clinic/cure/` + row.id,
        query: { 
            title: row.name,
            index: row.id
        }
    });
    window.open(routeData.href, "_blank");
}
function toAdmissionLog(row) {
    routerInstance.push({
        path: `/clinic/admission`,
        query: { 
            val: row.name,
        }
    })
}
function unPayReceipt(row) {
    payService.unPayReceipt(row.id, res => {
        searchUnPays.value = res || []
    })
}
const replenishs = ref([])
function replenishOption(row) {
    row.step = 0
    row.stepResult = [false, false, false, false, false, false]
    row.isLoading = true
    row.replenishNumber = 1
    row.replenishUnit = 0
    replenishs.value.push(row)
}
var timer = null
function replenish(row) {
    row.step = -1
    clearInterval(timer)
}
function startTime(row) {
    row.stepResult[0] = true
    row.step++
    timer = setInterval(() => {
        if(row.step == -1) {
            clearInterval(timer)
            return
        }
        row.step++
        if(row.step < 6) {
            row.stepResult[row.step -1] = true
        } else {
            clearInterval(timer)
        }
    }, 2000);
}
</script>
<style scoped>
.spinner {
    position: relative;
}

.double-bounce1, .double-bounce2 {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #67CF22;
    opacity: 0.6;
    position: absolute;
    top: 0;
    left: 0;
        
    -webkit-animation: bounce 2.0s infinite ease-in-out;
    animation: bounce 2.0s infinite ease-in-out;
    }

.double-bounce2 {
    -webkit-animation-delay: -1.0s;
    animation-delay: -1.0s;
}

@-webkit-keyframes bounce {
    0%, 100% { -webkit-transform: scale(0.0) }
    50% { -webkit-transform: scale(1.0) }
}

@keyframes bounce {
    0%, 100% {
        transform: scale(0.0);
        -webkit-transform: scale(0.0);
    } 50% {
        transform: scale(1.0);
        -webkit-transform: scale(1.0);
    }
}
.card-header {
    color: #fff;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
:deep(.gloable_search_input .el-input__wrapper) {
    border-top-left-radius: 30px;
    border-bottom-left-radius: 30px;
}
:deep(.gloable_search_input .el-input-group__append) {
    width: 15%;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
}
:deep(.el-radio.el-radio--large .el-radio__inner) {
    height: 20px;
    width: 20px;
    border-width: 2px;
}
</style>
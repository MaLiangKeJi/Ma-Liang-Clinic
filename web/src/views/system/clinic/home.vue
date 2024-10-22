<template>
    <el-scrollbar height="100%">
        <div v-if="layout == 0" style="height: 100%; display: flex; flex-direction: column; justify-content: space-between; min-width: 1375px;">
            <div style="height: 10%">
                <el-card class="box-card" style="border-radius: 15px; height: 100%; min-width: 1375px;" body-style="display: flex;">
                    <div @click="toProgress" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 5% 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><FirstAidKit /></el-icon>
                        </div>
                        <div style="width: 60px;">开始接诊</div>
                    </div>
                    <div @click="toPayList" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><Coin /></el-icon>
                        </div>
                        <div style="width: 85px;">收费列表</div>
                    </div>
                    <div @click="toAddRetail" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><ShoppingCart /></el-icon>
                        </div>
                        <div style="width: 85px;">新增零售</div>
                    </div>
                    <div @click="toStockList" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><Box /></el-icon>
                        </div>
                        <div style="width: 85px;">全部库存</div>
                    </div>
                    <div @click="toAddDisinfection" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><Brush /></el-icon>
                        </div>
                        <div style="width: 85px;">新增消杀</div>
                    </div>
                    <div @click="toAddSterilize" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><Timer /></el-icon>
                        </div>
                        <div style="width: 85px;">新增消毒</div>
                    </div>
                    <div @click="toAddProof" style="display: flex; align-items: center; margin-right: 3%; cursor: pointer;">
                        <div style="width: 60px; padding: 10px 10px 5px 10px; background-color: #f6f6f6; border-radius: 15px; margin-right: 10px;">
                            <el-icon style="font-size: 40px;"><Stamp /></el-icon>
                        </div>
                        <div style="width: 85px;">创建证明</div>
                    </div>
                </el-card>
            </div>
            <div style="height: 10%; display: flex; justify-content: space-between;">
                <el-card class="box-card index_card" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <StethoscopeIcon />
                        </el-icon>
                        <el-statistic title="正在接诊" :value="data.queueNumber" suffix="人" />
                    </div>
                </el-card>
                <el-card class="box-card index_card" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <UserIcon />
                        </el-icon>
                        <el-statistic title="今日总接诊数" :value="data.currentDayTotalReceptionNumber" suffix="人" />
                    </div>
                </el-card>
                <el-card class="box-card index_card" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <PayIcon />
                        </el-icon>
                        <el-statistic title="今日收益" :value="data.currentDayEarnings" suffix="元" />
                    </div>
                </el-card>
            </div>
            <div style="height: 10%; display: flex; justify-content: space-between;">
                <el-card class="box-card index_card_2" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <AgentiaIcon />
                        </el-icon>
                        <el-text class="mx-1" :type="!data.existUnderStockDrug ? 'success' : 'danger'" style="height: 40px; line-height: 40px;">
                            {{ data.existUnderStockDrug ? data.existUnderStockDrugNumber + '批次药品库存不足' : '库存正常' }}
                        </el-text>
                    </div>
                </el-card>
                <el-card class="box-card index_card_2" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <DelIcon />
                        </el-icon>
                        <el-text class="mx-1" :type="!data.existCriticalDrug ? 'success' : 'danger'" style="height: 40px; line-height: 40px;">
                            {{ data.existCriticalDrug ? data.existCriticalDrugNumber + '个临期药品' : '无临期药品' }}
                        </el-text>
                    </div>
                </el-card>
                <el-card class="box-card index_card_2" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <LaboratoryIcon />
                        </el-icon>
                        <el-text class="mx-1" :type="data.isDisinfection ? 'success' : 'danger'" style="height: 40px; line-height: 40px;">
                            今日{{ data.isDisinfection ? '已' : '未' }}消杀
                        </el-text>
                    </div>
                </el-card>
                <el-card class="box-card index_card_2" shadow="always">
                    <div style="height: 100%; display: flex; align-items: center;">
                        <el-icon style="font-size: 4em; margin-right: 10px;">
                            <BacteriaIcon />
                        </el-icon>
                        <el-text class="mx-1" :type="data.isSterilize ? 'success' : 'danger'" style="height: 40px; line-height: 40px;">
                            今日{{ data.isSterilize ? '已' : '未' }}消毒
                        </el-text>
                    </div>
                </el-card>
            </div>
            <div style="height: 60%; display: flex; justify-content: space-between;">
                <el-card style="width: 49.5%; border-radius: 15px;">
                    <div style="display: flex; justify-content: space-between; height: 10%;">
                        <el-text class="mx-1" size="large" style="font-weight: 700;">本月接诊人数</el-text>
                    </div>
                    <div id="echarts1" ref="receptionPeopleNumberChartContainer" style="height: 90%;"></div>
                </el-card>
                <el-card style="width: 49.5%; border-radius: 15px;">
                    <div style="display: flex; justify-content: space-between; height: 10%;">
                        <el-text class="mx-1" size="large" style="font-weight: 700;">本月销售额</el-text>
                    </div>
                    <div id="echarts1" ref="salesChartContainer" style="height: 90%;"></div>
                </el-card>
            </div>
        </div>
        <div v-if="layout == 1" style="height: 100%; width: 100%; display: flex; justify-content: space-between;">
            <el-scrollbar height="100%" style="width: 100%; padding: 1% 2%;">
                <div style="height: 10%; font-size: 2em; font-weight: 600; display: flex; justify-content: space-between;">
                    <div style="width: 40%; height: 100%; font-weight: 700; display: flex; align-items: center;">
                        <el-input
                            v-model="searchVal"
                            size="large"
                            placeholder="搜姓名、手机号、住址、药品、论坛..."
                            class="search_input"
                            :prefix-icon="Search"
                            style="width: 100% !important; font-size: 20px !important;"
                            @focus="() => { dialogSearchVisible = true; }"
                        />
                    </div>
                    <div style="width: 40%; height: 100%; display: flex; justify-content: flex-end; align-items: center;">
                        <!-- 进入论坛 -->
                        <el-popover
                            placement="top-start"
                            title="Tip：进入论坛"
                            trigger="hover"
                            content="点击进入码良论坛页面"
                        >
                            <template #reference>
                                <el-button @click="openForumLink()" circle style="width: 50px; height: 50px; font-size: 25px; margin-right: 25px; border-color: #605bff">
                                    <el-icon size="25px" color="#605bff"><ChatLineRound /></el-icon>
                                </el-button>
                            </template>
                        </el-popover>
                        <!-- 全屏按钮 -->
                        <el-popover
                            placement="top-start"
                            title="Tip：全屏"
                            trigger="hover"
                            content="开启全屏显示，再次点击退出全屏"
                        >
                            <template #reference>
                                <el-button @click="fullScreenStore.toggleFullScreen()" circle style="width: 50px; height: 50px; font-size: 25px; margin-right: 25px; border-color: #605bff">
                                    <el-icon size="25px" color="#605bff"><FullScreen /></el-icon>
                                </el-button>
                            </template>
                        </el-popover>
                        <!-- 提醒 -->
                        <el-button @click="informDrawer = true" circle style="width: 50px; height: 50px; font-size: 25px; margin-right: 50px; border-color: #605bff">
                            <el-badge :is-dot="isNotRead" class="item">
                                <el-icon size="25px" color="#605bff"><Bell/></el-icon>
                            </el-badge>
                        </el-button>
                        
                        <el-popover width="300"
                            placement="top-start"
                            trigger="hover"
                        >
                            <template #reference>
                                <div style="margin-right: 20px; display: flex; align-items: center;">
                                    <div style="display: flex; flex-direction: column;">
                                        <div style="font-size: 20px; font-weight: 700; min-width: 110px; text-align: right;">{{ userName }} 医生</div>
                                        <div style="display: flex; align-items: center;">
                                            <div v-if='isBusiness' class="status-point" style="background-color:#67C23A" />
                                            <div v-else class="status-point" style="background-color:#808080" />
                                            <el-text style="height: 20px; line-height: 20px; margin-left: 10px;" class="mx-1" type="info">{{ businessStr }}</el-text>
                                        </div>
                                    </div>
                                    <el-avatar :size="50" :src="circleUrl" style="margin-left: 20px;" />
                                </div>
                            </template>
                            <div style="padding: 0 5px;">
                                <div class="item" @click="toMePageByWork(isBusiness)" style="border-bottom: 1px solid rgba(0, 0, 0, 0.1);">
                                    <div>个人信息</div>
                                    <el-icon style="font-size: 28px;" color="#605bff"><User /></el-icon>
                                </div>
                                <!-- <div class="item" @click="toRenew" style="border-bottom: 1px solid rgba(0, 0, 0, 0.1);">
                                    <div>会员中心</div>
                                    <el-icon style="font-size: 28px;" color="#605bff"><ShoppingTrolley /></el-icon>
                                </div> -->
                                <div class="item" @click="toLogoutPage">
                                    <div>退出登录</div>
                                    <el-icon style="font-size: 28px;" color="#605bff"><MaterialSymbolsLightExitToAppIcon /></el-icon>
                                </div>
                            </div>
                        </el-popover>
                    </div>
                </div>
                <div style="height: 88%; display: flex; margin-top: 2%;">
                    <div style="width: 70%;">
                        <el-card style="height: 30%; border-radius: 15px;">
                            <div style="height: 100%; display: flex; justify-content: space-around;">
                                <div style="height: 100%; width: 40%; display: flex; flex-direction: column; justify-content: center;">
                                    <div style="width: 100%; text-align: left; font-size: 2em; color: #000; font-weight: 700">
                                        早上好，<span style="color: #605bff; font-weight: 700;">{{ userName }}医生</span>
                                    </div>
                                    <div style="height: 50%; width: 100%; text-align: left; font-size: 1vw; display: flex; align-items: center;">
                                        开始今天的工作吧！
                                        <el-button circle @click="toProgress" type="primary" style="width: 6vw; min-width: 100px; height: 20%; min-height: 40px; border-radius: 25px;">开始接诊</el-button>
                                    </div>
                                </div>
                                <el-image style="width: 30%;" :src="HealthcareIcon" fit="contain" />
                            </div>
                        </el-card>
                        <div style="height: 30%; display: flex; display: flex; justify-content: space-between; margin: 2% 1% 2% 2%">
                                <el-card style="height: 100%; width: 12vw; margin-right: 10px; cursor: pointer; border-radius: 15px;" 
                                    @click="switchActive(0)"
                                    :style="{ 
                                        'box-shadow': active == 0 ? 'rgba(3, 102, 214, 0.3) 0px 0px 0px 3px' : ''
                                    }"
                                >
                                    <div style="height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: space-around;">
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #6a5acd;" v-if="active == 0"><StethoscopeIcon2 /></el-icon>
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #f3f2f7" v-else><StethoscopeIcon /></el-icon>
                                        <el-text class="mx-1" :type="active == 0 ? 'primary' : 'info'" style="font-size: 1.3vw; font-weight: 700;">待接诊</el-text>
                                        <el-text class="mx-1" :type="active == 0 ? 'primary' : ''" style="font-size: 2.5vw;">{{ admissionLog.length }}</el-text>
                                    </div>
                                </el-card>
                                <el-card style="height: 100%; width: 12vw; margin-right: 10px; cursor: pointer; border-radius: 15px;" 
                                    @click="switchActive(1)"
                                    :style="{ 
                                        'box-shadow': active == 1 ? 'rgba(3, 102, 214, 0.3) 0px 0px 0px 3px' : ''
                                    }"
                                >
                                    <div style="height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: space-around;">
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #6a5acd;" v-if="active == 1" color="#fff"><User /></el-icon>
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #f3f2f7" v-else><UserIcon /></el-icon>
                                        <el-text class="mx-1" :type="active == 1 ? 'primary' : 'info'" style="font-size: 1.3vw; font-weight: 700;">今日接诊</el-text>
                                        <el-text class="mx-1" :type="active == 1 ? 'primary' : ''" style="font-size: 2.5vw;">{{ currentDayAdmissionLogData.length }}</el-text>
                                    </div>
                                </el-card>
                                <el-card style="height: 100%; width: 12vw; margin-right: 10px; cursor: pointer; border-radius: 15px;" 
                                    @click="switchActive(2)"
                                    :style="{ 
                                        'box-shadow': active == 2 ? 'rgba(3, 102, 214, 0.3) 0px 0px 0px 3px' : ''
                                    }"
                                >
                                    <div style="height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: space-around;">
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #6a5acd;" v-if="active == 2"><PayIcon2 /></el-icon>
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #f3f2f7" v-else><PayIcon /></el-icon>
                                        <el-text class="mx-1" :type="active == 2 ? 'primary' : 'info'" style="font-size: 1.3vw; font-weight: 700;">今日收益</el-text>
                                        <el-text class="mx-1" :type="active == 2 ? 'primary' : ''" style="font-size: 2.5vw;">{{ countFreeVal }}</el-text>
                                    </div>
                                </el-card>
                                <el-card style="height: 100%; width: 12vw; margin-right: 10px; cursor: pointer; border-radius: 15px;" 
                                    @click="switchActive(3)"
                                    :style="{ 
                                        'box-shadow': active == 3 ? 'rgba(3, 102, 214, 0.3) 0px 0px 0px 3px' : ''
                                    }"
                                >
                                    <div style="height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: space-around;">
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #6a5acd;" v-if="active == 3"><DrugIcon /></el-icon>
                                        <el-icon size="3vh" style="width: 10vh; height: 40%; border-radius: 15px; background-color: #f3f2f7" v-else><MedicineBottleIcon /></el-icon>
                                        <el-text class="mx-1" :type="active == 3 ? 'primary' : 'info'" style="font-size: 1.3vw; font-weight: 700;">库存不足</el-text>
                                        <el-text class="mx-1" :type="active == 3 ? 'primary' : ''" style="font-size: 2.5vw;">{{ stockShortageNumber }}</el-text>
                                    </div>
                                </el-card>
                        </div>
                        <el-card style="height: 80%; width: 100%; padding: 2%; border-radius: 15px;">
                            <el-carousel ref="carouselRef" style="height: 100%;" indicator-position="none" :autoplay="false">
                                <el-carousel-item  style="height: 100%;">
                                    <el-table :data="admissionLog" style="width: 100%; height: 100%; font-size: 18px;" :row-style="{height: '60px'}" 
                                        @cell-mouse-enter="row => { row.color = '#605bff'; row.iconColor = '#fff'; }"
                                        @cell-mouse-leave="row => { row.color = ''; row.iconColor = '#605bff'; }"
                                    >
                                        <el-table-column prop="createTime" label="日期" :formatter="convertDate" />
                                        <el-table-column prop="name" label="姓名" :formatter="convertName" />
                                        <el-table-column prop="sex" label="性别" :formatter="convertSex" />
                                        <el-table-column prop="age" label="年龄" :formatter="convertAge" />
                                        <el-table-column fixed="right" width="100">
                                            <template #default="{ row }">
                                                <el-button 
                                                    @click="toCure(row.id)"
                                                    :color="row.color == undefined ? '' : row.color" 
                                                    @mouseenter="row.color = '#605bff'; row.iconColor = '#fff';" 
                                                    @mouseleave="row.color = ''; row.iconColor = '#605bff';"
                                                    circle
                                                    style="border: 2px solid #605bff; font-size: 20px; width: 36px; height: 36px;"
                                                >
                                                    <el-icon 
                                                        :color="row.iconColor == undefined ? '#605bff' : row.iconColor"
                                                    >
                                                        <ArrowRightBold />
                                                    </el-icon>
                                                </el-button>
                                            </template>
                                        </el-table-column>
                                        <template #empty>
                                            <el-empty>
                                                <template #description>
                                                    <div>没有待接诊的患者</div>
                                                    <el-button type="success" @click="routerInstance.push({ path: '/clinic/doctor/patient/add' })">开始接诊</el-button>
                                                </template>
                                            </el-empty>
                                        </template>
                                    </el-table>
                                </el-carousel-item>
                                <el-carousel-item  style="height: 100%;">
                                    <el-table :data="currentDayAdmissionLogData" style="height: 80%; width: 100%; font-size: 18px;" :row-style="{height: '80px'}">
                                        <el-table-column prop="createTime" label="日期" :formatter="convertDate" />
                                        <el-table-column prop="name" label="姓名" />
                                        <el-table-column prop="sex" label="性别" :formatter="convertSex" />
                                        <el-table-column label="状态">
                                            <template #default="{ row }">
                                                <div style="font-size: 15px; width: 100%;">
                                                    <el-tag 
                                                        :type="row.state == 0 ? '' : (row.state == 1 ? 'success' : 'info')" 
                                                        effect="light" 
                                                        style="width: 100px; height: 30px; font-weight: 700; font-size: 1.2em; margin-right: 20px;"
                                                    >
                                                        {{state(row)}}
                                                    </el-tag>
                                                    <el-tag 
                                                        :type="row.pay && row.pay.state == 1 ? 'info' : 'warning'" 
                                                        effect="light" 
                                                        style="width: 100px; height: 30px; font-weight: 700; font-size: 1.2em;"
                                                    >
                                                        {{payState(row)}}
                                                    </el-tag>
                                                </div>
                                            </template>
                                        </el-table-column>
                                        <el-table-column align="right" fixed="right" width="200">
                                            <template #default="{ $index, row }">
                                                <el-button type="primary" @click="admissionsEdit($index, row)" style="width: 100px; height: 50px; font-size: 15px" plain>查看</el-button>
                                            </template>
                                        </el-table-column>
                                        <template #empty>
                                            <el-empty>
                                                <template #description>
                                                    <div>没有待接诊的患者</div>
                                                    <el-button type="success" @click="routerInstance.push({ path: '/clinic/doctor/patient/add' })">开始接诊</el-button>
                                                </template>
                                            </el-empty>
                                        </template>
                                    </el-table>
                                </el-carousel-item>
                                <el-carousel-item  style="height: 100%;">
                                    <el-table :data="currentDayProceedsData" stripe style="width: 100%; height: 80%; font-size: 18px;" :row-style="{height: '80px'}">
                                        <el-table-column prop="dossierTime" label="日期" sortable>
                                            <template #default="{ row }">
                                                <span style="margin-left: 10px">{{ row.dossierTime ? formatTimeToHMS(new Date(row.dossierTime)) : '' }}</span>
                                            </template>
                                        </el-table-column>
                                        <el-table-column prop="name" label="姓名" />
                                        <el-table-column prop="sex" label="性别"  :filter-method="filterSexHandler" :filters="[
                                            { text: '男', value: '1' },
                                            { text: '女', value: '0' },
                                        ]">
                                            <template #default="{ row }">
                                                {{ row.sex == 1 ? '男' : '女' }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column prop="phone" label="手机" />
                                        <el-table-column prop="fee" label="费用" sortable>
                                            <template #default="{ row }">
                                                <el-text class="mx-1" type="success" style="font-size: 1.2em">{{ row.fee }}</el-text>
                                            </template>
                                        </el-table-column>
                                        <template #empty>
                                            <el-empty>
                                                <template #description>
                                                    <div>没有支付记录</div>
                                                </template>
                                            </el-empty>
                                        </template>
                                    </el-table>
                                </el-carousel-item>
                                <el-carousel-item  style="height: 100%;">
                                    <el-table :data="stockShortageList" stripe style="width: 100%; height: 80%; font-size: 18px;" :row-style="{height: '80px'}">
                                        <el-table-column prop="expiryDate" label="过期" sortable>
                                            <template #default="{ row }">
                                                <span style="margin-left: 10px">{{ row.expiryDate ? formatDateToYMD(new Date(row.expiryDate)) : '' }}</span>
                                            </template>
                                        </el-table-column>
                                        <el-table-column prop="name" label="药品" />
                                        <el-table-column prop="manufacturer" label="厂商" />
                                        <el-table-column prop="batchNumber" label="批次" />
                                        <el-table-column prop="dosageForm" label="剂型" />
                                        <el-table-column prop="spec" label="规格" />
                                    </el-table>
                                </el-carousel-item>
                            </el-carousel>
                        </el-card>
                    </div>
                    <div style="width: 25%; margin-left: 2%;">
                        <el-card style="border-radius: 15px; height: 80%;" ref="operationLogRef">
                            <div style="height: 5vh;">
                                <div style="font-size: 2vh; font-weight: 700;">
                                    门诊日志
                                </div>
                            </div>
                            <div class="custom-scrollbar">
                                <el-scrollbar ref="scrollbarRef" height="100%">
                                    <el-timeline style="width: 100%" ref="timelineRef">
                                        <el-timeline-item center placement="top" :hide-timestamp="true" type="primary" v-for="(operationLog, index) in operationLogs" :key="index">
                                            <div v-if="operationLog.isFill" style="display: flex; align-items: center; margin: 40px 0;"></div>
                                            <div v-else style="display: flex; align-items: center; margin: 5% 0;">
                                                <div style="width: 50%; display: flex; flex-direction: column; ">
                                                    <span style="font-size: 1.2vw; margin-right: 5px;">{{ operationLog.createHMS }}</span>
                                                    <div style="display: flex; align-items: center; justify-content: space-between;">
                                                        <el-text v-if="operationLog.isCurrentDay" class="mx-1" type="success" style="font-size: 1vw;">今天</el-text>
                                                        <el-text v-else class="mx-1" type="info" style="font-size: 1vw;">{{ operationLog.createYMD }}</el-text>
                                                    </div>
                                                </div>
                                                <div style="width: 50%; display: flex; flex-direction: column;">
                                                    <span style="font-size: 1.2vw; text-align: left;">{{ operationLog.about }}</span>
                                                    <el-text v-if="operationLog.patient" class="mx-1" type="info" style="font-size: 1vw; width: 100%; text-align: left;">{{ operationLog.patient.name }}</el-text>
                                                </div>
                                                <!-- <el-divider v-if="operationLog.patient" direction="vertical" style="margin: 0 40px; height: 40px;" />
                                                <div v-if="operationLog.patient" style="display: flex; flex-direction: column;">
                                                    <span style="font-size: 1.5em;">{{ operationLog.patient.name }}</span>
                                                    <el-text class="mx-1" type="info">{{ operationLog.patient.sex && operationLog.patient.sex == 1 ? '男' : '女' }} {{ operationLog.patient.age }}岁</el-text>
                                                </div>
                                                <el-divider direction="vertical" style="margin: 0 20px; height: 0;" />
                                                <el-button v-if="operationLog.serviceCode == 3" @click="toCure(operationLog.param)" link>
                                                    <el-icon color="#605bff"><Document /></el-icon>
                                                    <span style="color: #605bff;; text-align: center; font-size: 1.2em;">查看病例</span>
                                                </el-button> -->
                                            </div>
                                        </el-timeline-item>
                                    </el-timeline>
                                </el-scrollbar>
                            </div>
                        </el-card>
                        <el-card style="border-radius: 15px; height: 30%; margin: 2% 0;">
                            <div ref="receptionPeopleNumberChartContainer" style="height: 100%;"></div>
                        </el-card>
                        <el-card style="border-radius: 15px; height: 30%;">
                            <div ref="salesChartContainer" style="height: 100%;"></div>
                        </el-card>
                    </div>
                </div>
            </el-scrollbar>
        </div>

        <el-dialog v-model="dialogSearchVisible" @close="searchVal = ''" width="60vw" style="background-color: #eceef2;">
            <GloableSearch ref="searchRef" />
        </el-dialog>
    </el-scrollbar>



    <div>
        <el-drawer v-model="informDrawer" >
            <template #header>
                <h4 class="titleClass">系统通知</h4>
            </template>
            <template #default>
                <el-scrollbar>
                    <el-card style="width: 100%;" shadow="always" v-for="(inform, index) in informList" :key="index" >
                        <p class="text item"  >
                            <el-badge :is-dot="inform.isNotRead" class="item">{{ inform.content }}{{ formatDateToYMD(new Date(inform.time)) }}</el-badge>
                        </p>
                    </el-card>
                </el-scrollbar>
            </template>
            <template #footer>
                <el-button>一键已读</el-button>
            </template>
        </el-drawer>
    </div>

<!-- 
    <div>
        <el-dialog v-model="inviteDialogVisible" :show-close="false" :close-on-click-modal="false" width="60%" style="height: 60vh; border-radius: 5px;">
            <div v-if="inviteDialogStep == 0" style="height: 100%; display: flex; background-color: #fff;">
                <div style="height: 100%; width: 60%; display: flex; flex-direction: column; justify-content: space-around; align-items: center;">
                    <div style="font-size: 1.2vw;">
                        限时活动，领取免费使用卷
                    </div>
                    <div style="display: flex; flex-direction: column; align-items: center;">
                        <div style="height: 6vh; display: flex; width: 100%; justify-content: center;">
                            <div style="height: 100%; display: flex; align-items: center;">
                                <el-icon color="#67c23a" size="1vw" style="margin-right: 0.5vw;"><CircleCheckFilled /></el-icon>
                                <span style="font-size: 0.8vw;">邀请 1 家诊所使用</span>
                            </div>
                            <div style="display: flex; align-items: center; margin-left: 2vw;">
                                <el-text class="mx-1" type="success" style="font-size: 0.8vw; margin-right: 0.5vw;">1个月</el-text>
                                <el-text class="mx-1" style="font-size: 0.8vw;">免费使用</el-text>
                            </div>
                        </div>
                        <div style="height: 6vh; display: flex; width: 100%; justify-content: center;">
                            <div style="height: 100%; display: flex; align-items: center;">
                                <el-icon color="#67c23a" size="1vw" style="margin-right: 0.5vw;"><CircleCheckFilled /></el-icon>
                                <span style="font-size: 0.8vw;">邀请 5 家诊所使用</span>
                            </div>
                            <div style="display: flex; align-items: center; margin-left: 2vw;">
                                <el-text class="mx-1" type="warning" style="font-size: 1.2vw; margin-right: 0.5vw;">6个月</el-text>
                                <el-text class="mx-1" style="font-size: 0.8vw;">免费使用</el-text>
                            </div>
                        </div>
                        <div style="height: 6vh; display: flex; width: 100%; justify-content: center;">
                            <div style="height: 100%; display: flex; align-items: center;">
                                <el-icon color="#67c23a" size="1vw" style="margin-right: 0.5vw;"><CircleCheckFilled /></el-icon>
                                <span style="font-size: 0.8vw;">邀请 10 家诊所使用</span>
                            </div>
                            <div style="display: flex; align-items: center; margin-left: 2vw;">
                                <el-text class="mx-1" type="danger" style="font-size: 1.5vw; margin-right: 0.5vw;">1年</el-text>
                                <el-text class="mx-1" style="font-size: 0.8vw;">免费使用</el-text>
                            </div>
                        </div>
                    </div>
                    <div style="display: flex; flex-direction: column; align-items: center;">
                        <el-button @click="inviteDialogStep = 1" type="primary" style="width: 6vw; height: 4vh; margin-bottom: 1vh;">下一步</el-button>
                        <el-text class="mx-1" type="info">稍后可以在右上角【会员中心】查看</el-text>
                    </div>
                </div>
                <el-image style="width: 20%;" :src="DoctorIMG" fit="contain" />
            </div>
            <div v-else-if="inviteDialogStep == 1" style="height: 100%; display: flex; background-color: #fff;">
                <div style="height: 100%; width: 60%; display: flex; flex-direction: column; align-items: center;">
                    <div style="height: 80%; display: flex; flex-direction: column; justify-content: center;  align-items: center;">
                        <div style="width: 20vw; font-size: 0.8vw; margin-bottom: 1vh;"><span>输入您的邀请码，领取新人奖励</span></div>
                        <el-input v-model="invitationCode" style="width: 20vw; height: 5vh;" placeholder="">
                            <template #append>
                                <el-button @click="() => getInvitationReward()" type="success" style="font-size: 0.8vw; height: 5vh; width: 5vw;">领取</el-button>
                            </template>
                        </el-input>
                    </div>
                    <el-button @click="closeInviteDialog" type="primary" style="width: 6vw; height: 4vh;">跳过</el-button>
                </div>
                <el-image style="width: 20%;" :src="DoctorIMG" fit="contain" />
            </div>
        </el-dialog>
    </div> -->
</template>
<script setup>
import { FirstAidKit, Coin, ShoppingCart, Timer, Brush, Stamp, Search, ArrowRightBold, Tools, FullScreen, ChatLineRound, Bell, User, Document, MoreFilled, ShoppingTrolley  } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import UserIcon from './userIcon.vue'
import PayIcon from './payIcon.vue'
import PayIcon2 from './payIcon2.vue'
import AgentiaIcon from './agentialIcon.vue'
import StethoscopeIcon from './stethoscopeIcon.vue'
import StethoscopeIcon2 from './stethoscopeIcon2.vue'
import BacteriaIcon from './bacteriaIcon.vue'
import LaboratoryIcon from './laboratoryIcon.vue'
import DelIcon from './delIcon.vue'
import MaterialSymbolsLightExitToAppIcon from '@/components/header/MaterialSymbolsLightExitToApp.vue'
import HealthcareIcon from '@/static/healthcare.png'
import DrugIcon from './drugIcon.vue'
import MedicineBottleIcon from './medicineBottleIcon.vue'
import DoctorIMG from '@/static/doctor.png'

import GloableSearch from './gloableSearch.vue'


import { ref, inject, onMounted, reactive, watch, nextTick } from 'vue';
import { useRouter } from 'vue-router'
import { ElNotification, } from 'element-plus';

import { getTimeStr, } from '@/utils/dateUtils.js';

import { loginUserStore } from '@/stores/UserStore'
const routerInstance = useRouter()

import { searchBusiness as getBusAPI, } from '@/api/clinic/home.api.js';
import countService from '@/api/clinic/count.api.js'
import { admissionService, payService } from '@/api/clinic/index.js'
import { search as searchAPI, searchUnderStock as searchUnderStockAPI, searchExpiredDrug as searchExpiredDrugAPI } from '@/api/clinic/search.api.js'
import logService from '@/api/clinic/log.api.js'
import { settingService } from '@/api/clinic/index.js'
import { fullScreenStore } from '@/stores/fullScreen'
import { informService } from '@/api/clinic/index.js'
import { 
    toProgress, toPayList, toAddRetail, toStockList, toAddDisinfection, toAddSterilize, toAddProof,
    toLogoutPage, toMePageByWork, toRenew,
    convertDate, convertName, convertSex, convertAge, filterSexHandler, formatDateToYMD, formatTimeToHMS, 
    getStartOfDay, getEndOfDay 
} from './home.js'
import { size } from 'lodash'

const Echarts = inject('echarts');

const layout = ref(1)

const active = ref(0)
const searchVal = ref('')
const carouselRef = ref()
const stockShortageNumber = ref(0)
const stockShortageList = ref([])

const informDrawer = ref(false)

const operationLogRef = ref()

watch(
    () => active.value,
    () => {
        if (active.value == 0) {
            carouselRef.value.setActiveItem(0)
        } else if (active.value == 1) {
            carouselRef.value.setActiveItem(1)
        } else if (active.value == 2) {
            carouselRef.value.setActiveItem(2)
        } else if (active.value == 3) {
            carouselRef.value.setActiveItem(3)
        }
    }
)
const isNotRead = ref(false)
const informList = ref([])

function getInformList() {
    informService.search({}, res => {
        informList.value = res.list
        isNotRead.value = res.hasNoRead
    })
}


const searchRef = ref()

const dialogSearchVisible = ref(false)

const data = reactive({
    queueNumber: 0,
    currentDayTotalReceptionNumber: 0,
    currentDayEarnings: 0,
    existCriticalDrug: false,
    existCriticalDrugNumber: 0,
    existUnderStockDrug: false,
    existUnderStockDrugNumber: 0,
    isDisinfection: false,
    isSterilize: false,
})

const tableData = ref([])


const receptionPeopleNumberChartContainer = ref();
const receptionPeopleNumberChartDateList = ref()
const receptionPeopleNumberChartDataList = ref()
const receptionPeopleNumberChartMaxIndex = ref()
var receptionPeopleNumberChart = null
function initReceptionPeopleNumberChart() {
    const option = {
        title: { text: '当月接诊人数' },
        xAxis: {
            type: 'category',
            data: receptionPeopleNumberChartDateList.value,
        },
        yAxis: {
            type: 'value',
            max: receptionPeopleNumberChartMaxIndex.value,
        },
        series: [
            {
                data: receptionPeopleNumberChartDataList.value,
                type: 'line'
            }
        ]
    };
    receptionPeopleNumberChart.setOption(option);
    // 根据页面大小自动响应图表大小
    window.addEventListener("resize", function () {
        receptionPeopleNumberChart.resize();
    });
}


const salesChartContainer = ref();
const salesChartDateList = ref()
const salesChartDataList = ref()
const salesChartMaxIndex = ref()
var salesChart = null
function initSalesChart() {
    const option = {
        title: { text: '当月销售额' },
        xAxis: {
            type: 'category',
            data: salesChartDateList.value,
        },
        yAxis: {
            type: 'value',
            max: salesChartMaxIndex.value,
        },
        series: [
            {
                data: salesChartDataList.value,
                type: 'bar',
                showBackground: true,
                backgroundStyle: {
                    color: 'rgba(180, 180, 180, 0.2)'
                }
            }
        ]
    };
    salesChart.setOption(option);
    // 根据页面大小自动响应图表大小
    window.addEventListener("resize", function () {
        salesChart.resize();
    });
}

const userName = ref('')
const setting = ref(null)
const countFreeVal = ref(0)
const circleUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')


const operationLogs = ref([])

const timerUpdateActive = ref()

const scrollbarRef = ref()
const timelineRef = ref() // 计数器内部实例

// 邀请相关推送弹窗
import { search as searchDisposableFinishStatusAPI, finished as finishedDisposableAPI } from '@/api/clinic/disposable.api.js'
import { getInvitationReward as getInvitationRewardAPI } from '@/api/clinic/reward.api.js'
const inviteDialogVisible = ref(false)
const inviteDialogStep = ref(0)
const invitationCode = ref()
function getInvitationReward() {
    let code = invitationCode.value
    if(code && code != "")
    getInvitationRewardAPI(code, () => {
        ElMessage({ message: '奖励已发送', type: 'success', })
        closeInviteDialog()
    })
}
function closeInviteDialog() {
    inviteDialogVisible.value = false
    finishedDisposableAPI("invite_hint")
}
function openForumLink(){
    window.open('https://forum.maliang.work/', '_blank')
}

const isBusiness = ref(false);
const businessStr = ref("停止营业");
onMounted(async () => {
    receptionPeopleNumberChart = Echarts.init(receptionPeopleNumberChartContainer.value);
    salesChart = Echarts.init(salesChartContainer.value);
    if(layout == 0) {
        countService.search({}, res => {
            Object.assign(data, res)
            receptionPeopleNumberChartDateList.value = res.receptionPeopleNumberChartData.dateList
            receptionPeopleNumberChartDataList.value = res.receptionPeopleNumberChartData.numberList
            receptionPeopleNumberChartMaxIndex.value = res.receptionPeopleNumberChartData.max * 1.5
            initReceptionPeopleNumberChart()
            salesChartDateList.value = res.singularMonthSalesChartData.dateList
            salesChartDataList.value = res.singularMonthSalesChartData.numberList
            salesChartMaxIndex.value = res.singularMonthSalesChartData.max * 1.5
            initSalesChart()
        })
    } else {
        method.searchAdmissionLog()
        method.searchCuurentDayAdmissionLog()
        method.searchCuurentDayProceeds()
        countService.searchCountTow(({ admissionLog, currentDayAdmissionLog, currentDayProceeds, countFree }) => {
            //正在接诊
            admissionLog.value = admissionLog.records
            let firstRow = tableData.value[0]
            if(firstRow) {
                firstRow.color = '#605bff'; 
                firstRow.iconColor = '#fff';
            }
            currentDayAdmissionLogData.value = currentDayAdmissionLog.records
            currentDayProceedsData.value = currentDayProceeds.records
            countFreeVal.value = countFree
        })
    }

    countService.search({}, res => {
            Object.assign(data, res)
            receptionPeopleNumberChartDateList.value = res.receptionPeopleNumberChartData.dateList
            receptionPeopleNumberChartDataList.value = res.receptionPeopleNumberChartData.numberList
            receptionPeopleNumberChartMaxIndex.value = res.receptionPeopleNumberChartData.max * 1.5
            initReceptionPeopleNumberChart()
            salesChartDateList.value = res.singularMonthSalesChartData.dateList
            salesChartDataList.value = res.singularMonthSalesChartData.numberList
            salesChartMaxIndex.value = res.singularMonthSalesChartData.max * 1.5
            initSalesChart()

            // 库存不足
            if(res.drugExpiryGroup.stockShortage) {
                let stockShortage = res.drugExpiryGroup.stockShortage
                stockShortageList.value = stockShortage
                stockShortageNumber.value = stockShortage.length
            }
        })
    
    getBusAPI((res) => {
        if (res) {
            userName.value = res.physician;

            if (isWork(res.isWork))
                return;

            const dayArr = res.businessDayList;
            const timesArr = res.businessTimeList;

            if (!hasBusiness(dayArr.length, timesArr.length))
                return;

            if (!isValidDay(dayArr))
                return;

            initBusiness(timesArr);
        }
    })

    logService.searchOperationList({}, async (res) => {
        if (res.length < 1)
            return;

        res[res.length -1].isLast = true
        operationLogs.value = [{ isFill: true }, ...res, { isFill: true }, ]
        await nextTick(() => {
            if(scrollbarRef.value) scrollbarRef.value.setScrollTop(timelineRef.value.$el.clientHeight)
        })
    })
    timerUpdateActive.value = setInterval(function () {
        if(active.value < 3) {
            active.value++
        } else {
            active.value = 0
        }
    }, 2000)
    getInformList()
    
    // 邀请弹窗
    // searchDisposableFinishStatusAPI("invite_hint", res => {
    //     if(res == false) {
    //         inviteDialogVisible.value = true
    //     }
    // })
})
const state = ({ state, payId,pay, prescriptionId, dossierId }) => {
    let result = '未接诊'
    if(state == 1) {
        result = '正在接诊'
    } else if(state == 2) {
        result = '已结束'
    }
    return result
}
const payState = ({ state, payId,pay, prescriptionId, dossierId }) => {
    let result = '未支付'
    if(pay) {
        if(pay.state == 1) {
            result = '已支付'
        }
    }
    return result
}
const admissionLog = ref([])
const currentDayAdmissionLogData = ref([])
const currentDayProceedsData = ref([])
const method = {
    search() {
        admissionService.search({ current: 1, size: 10, createTime: formatDateToYMD(new Date()) }, (response) => {
            tableData.value = response.records
            let firstRow = tableData.value[0]
            if(firstRow) {
                firstRow.color = '#605bff'; 
                firstRow.iconColor = '#fff';
            }
        })
    },
    searchAdmissionLog() {
        admissionService.search({ current: 1, size: 10, state: 1, createTime: formatDateToYMD(new Date()) }, (response) => {
            admissionLog.value = response.records
            let firstRow = tableData.value[0]
            if(firstRow) {
                firstRow.color = '#605bff'; 
                firstRow.iconColor = '#fff';
            }
        })
    },
    searchCuurentDayAdmissionLog() {
        admissionService.search({ current: 1, size: 10, createTime: formatDateToYMD(new Date()) }, (response) => {
            currentDayAdmissionLogData.value = response.records
        })
    },
    searchCuurentDayProceeds() {
        let now = new Date();
        let startOfDay = getStartOfDay(now);
        let endOfDay = getEndOfDay(now);
        let param = {
            current: 1,
            size: 10,
            startDate: startOfDay.getTime(),
            endDate: endOfDay.getTime(),
        }
        payService.searchIsPay(param, (response) => {
            currentDayProceedsData.value = response.records
        })
    },
}
function admissionsEdit($index, row) {
    let routeData = routerInstance.resolve({
        path: `/clinic/cure/` + row.id,
        query: { 
            admissionLogId: row.id,
            title: row.name,
            index: row.id
        }
    });
    window.open(routeData.href, "_blank");
}
function toCure(admissionLogId) {
    let routeData = routerInstance.resolve({
        path: `/clinic/cure/` + admissionLogId,
    });
    window.open(routeData.href, "_blank");
}
function switchActive(val) {
    clearInterval(timerUpdateActive.value)
    active.value = val
}

function isWork(isWork) {
    if (isWork != null || isWork != undefined) {
        if (isWork) {
            businessStr.value = "正在营业";
            isBusiness.value = true;
        }

        return true;
    }
    return false;
}

/**
 * 是否有效营业信息
 * @param {营业天数数组长度} dayArrSize
 * @param {营业时间段数组长度} timesArrSize 
 */
function hasBusiness(dayArrSize, timesArrSize) {
    if (dayArrSize < 1 || timesArrSize < 1) {
        ElNotification({
            title: '营业显示异常', message: '请到[管理]下的[设置]，填写营业相关信息并点击保存', type: 'error',
        })
        return false;
    }
    return true;
}

/**
 * 今天是否营业
 * @param {营业天数数组} dayArr 
 */
function isValidDay(dayArr) {
    let nowDay = new Date().getDay();
    for (const key in dayArr) {
        const theDay = dayArr[key];
        if (parseInt(theDay) == nowDay)
            return true;
    }
    return false;
}

/**
 * 初始化营业信息
 * @param {所有时间段的营业时间数组} timesArr 
 */
function initBusiness(timesArr) {
    let nowTimeLite = parseInt(getTimeStr(new Date()));
    if (timesArr.length < 1) {
        businessStr.value = "营业时间未设置,请在管理进入设置";
        isBusiness.value = false;
        return;
    }
     
    for (const key in timesArr) {
        const theTimeArr = timesArr[key];
        let leftTimeLite = parseInt(theTimeArr[0]);
        let rightTimeLite = parseInt(theTimeArr[1]);
        if (leftTimeLite <= nowTimeLite && nowTimeLite <= rightTimeLite) {
            businessStr.value = "正在营业";
            isBusiness.value = true;
            return;
        }
    }
}
</script>
<style scoped>
:deep(.el-timeline) {
    padding-left: 3% !important;
}
:deep(.no_search_result > .el-card__body) {
    width: 100%;
}
.card-header {
    color: #fff;
}
.index_card {
    border-radius: 15px;
    width: 32% !important;
}
.index_card_2 {
    border-radius: 15px;
    width: 24% !important;
}
:deep(.el-card__body) {
    height: 100%;
}
:deep(.search_input>.el-input__wrapper) {
    border-radius: 25px;
}
:deep(.el-carousel__container) {
    height: 100%;
}
:deep(.el-carousel__arrow) {
    display: none !important;
}
.item {
    width: 100%; height: 60px; line-height: 60px; display: flex; justify-content: space-between; align-items: center;
}
.item:hover {
    color: #438ee4;
    cursor: pointer;
}
:deep(.el-scrollbar__view) {
    height: 100%;
}
:deep(.el-card__header) {
    background-color: #6a5acd;
}
.blue_border {
    border: 2px solid #605bff;;
}
.status-point {
    display: inline-block;
    width: 12px;
    height: 12px;
    border-radius: 50%;
}

.custom-scrollbar {
    height: 90%; /* 设置滚动区域的高度 */
    position: relative; /* 使伪元素相对于容器定位 */
    overflow: hidden; /* 隐藏溢出的内容 */
}

/* 顶部渐变 */
.custom-scrollbar::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 25%; /* 渐变高度 */
    background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
    pointer-events: none; /* 使元素不响应鼠标事件 */
    z-index: 999;
}

/* 底部渐变 */
.custom-scrollbar::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 25%; /* 渐变高度 */
    background: linear-gradient(to top, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
    pointer-events: none; /* 使元素不响应鼠标事件 */
    z-index: 999;
}
:deep(.el-card__body) {
    width: 100%;
}
:deep(.el-input-group__append) {
    background-color: #67c23a;
    color: #fff;
}
:deep(.el-drawer__body) {
    padding: 0 !important;
}
:deep(.el-dialog__body) {
    height: 100%;
}
</style>
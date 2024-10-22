import { createRouter, createWebHistory } from 'vue-router'

import { leftMenuStore, globalLoading } from '@/stores/app.js'

import OfficialWebsite from '@/views/official/index.vue'
// import OfficialPrice from '@/views/official/price.vue'

import LoadingView from '@/views/loading.vue'

//后台
import BackCompanyView from '@/views/system/back/index.vue'
import BackCreateCompanyView from '@/views/system/back/CreateCompany.vue'
import BackSystemView from '@/views/system/back/system/index.vue'
import BackRouterView from '@/views/system/back/router/index.vue'


import ClinicLoginView from '@/views/system/clinic/login.vue'
import MemberCenterView from '@/views/system/clinic/member/index.vue'
import RenewView from '@/views/system/clinic/renew/index.vue'
import DrugView from '@/views/system/clinic/admission/drug.vue';
import OpenDrugView from '@/views/system/clinic/drug/openDrug.vue';

import PatientHistoryPrescriptionView from '@/views/system/clinic/phone/record/prescription.vue'
import PatientPhoneHistoryClinicView from '@/views/system/clinic/phone/record/hclinic.vue'
import PatientPhoneDoctorView from '@/views/system/clinic/phone/record/doctor.vue'
import PatientPhoneAdmissionView from '@/views/system/clinic/phone/record/admission.vue'

import PatientPhoneClinicView from '@/views/system/clinic/phone/clinic.vue'
import PatientPhoneIndexView from '@/views/system/clinic/phone/index.vue'

import BackMeInviteView from '@/views/system/back/invite/me/index.vue'
import BackMerchantView from '@/views/system/back/merchant/index.vue'
import BackMerchantShelfView from '@/views/system/back/merchant/shelf.vue'

//用户条款
import UserTerms from '@/views/system/clinic/clause/UserTerms.vue'
// 隐私协议
import PrivacyPolicy from '@/views/system/clinic/clause/PrivacyPolicy.vue'

//个人信息
import MeView from '@/views/system/me/index.vue'


import { loginUserStore } from '@/stores/UserStore'

var routerInstance = createRouter({
  mode: 'hash',
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'official',
      component: OfficialWebsite,
      meta: { 
        title: '首页 | 诊所 | 诊所管理 | 码良云诊所 | 在线医疗服务 | 医疗 | 乡村医疗 | 乡村诊所 | 医疗点',
        isHideLeftMenu: true, //是否隐藏左侧菜单
      },
    },
    // {
    //   path: '/price',
    //   name: 'officialPrice',
    //   component: OfficialPrice,
    //   meta: {
    //     title: '首页 | 诊所 | 诊所管理 | 码良云诊所 | 在线医疗服务 | 医疗 | 乡村医疗 | 乡村诊所 | 医疗点',
    //     isHideLeftMenu: true, //是否隐藏左侧菜单
    //   },
    // },
    {
      path: '/clinic/drug',
      name: 'drug',
      component: DrugView,
      meta: {
        title: '处方药服用指南',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/open/drug',
      name: 'openDrug',
      component: OpenDrugView,
      meta: {
        title: '开药',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/404',
      name: 'NotFound',
      component: LoadingView
    },
    {
      path: '/me',
      name: 'me',
      component: MeView,
      meta: { 
        title: '我的',
      },
    },
    {
      path: '/me/:isWork',
      name: 'meByWork',
      component: MeView,
      meta: {
        title: '我的',
      },
    },
    {
      path: '/clinic/login',
      name: 'clinicLogin',
      component: ClinicLoginView,
      meta: { 
        title: '登录诊所系统',
        isHideLeftMenu: true,
      },
      beforeEnter: (to, from) => {
        if(from.path != '/' && from.path != '/clinic/login' ) {
          loginUserStore.logout()
        }
        return true
      },
    },
    {
      path: '/clinic/login/:code',
      name: 'clinicLoginByInv',
      component: ClinicLoginView,
      meta: {
        title: '登录诊所系统',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/back/company',
      name: 'backCompany',
      component: BackCompanyView
    },
    {
      path: '/back/company/create',
      name: 'backCreateCompany',
      component: BackCreateCompanyView
    },
    {
      path: '/back/system',
      name: 'backSystem',
      component: BackSystemView
    },
    {
      path: '/back/router',
      name: 'backRouter',
      component: BackRouterView
    },
    {
      name: '404',
      path: '/:catchAll(.*)',
      component: OfficialWebsite,
      beforeEnter: (to, from) => {
        leftMenuStore.hideLeftMenu()
        globalLoading.closeGlobalLoading()
      },
    },
    {
      path: '/clinic/member',
      name: 'memberCenter',
      component: MemberCenterView,
      meta: { 
        title: '会员中心',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/renew',
      name: 'renew',
      component: RenewView,
      meta: { 
        title: '续费',
        isHideLeftMenu: true,
      },
    },

    {
      path: '/clinic/phone/patient/admission',
      name: 'patientPhoneAdmission',
      component: PatientPhoneAdmissionView,
      meta: {
        title: '门诊日志',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/phone/patient/history/prescription',
      name: 'patientHistoryPrescription',
      component: PatientHistoryPrescriptionView,
      meta: {
        title: '历史处方',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/phone/patient/history/clinic',
      name: 'patientPhoneHistoryClinic',
      component: PatientPhoneHistoryClinicView,
      meta: {
        title: '去过的诊所',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/phone',
      name: 'patientPhoneDoctor',
      component: PatientPhoneDoctorView,
      meta: {
        title: '联系医生',
        isHideLeftMenu: true,
      },
    },

    {
      path: '/clinic/phone/patient/clinic',
      name: 'patientPhoneClinic',
      component: PatientPhoneClinicView,
      meta: {
        title: '找诊所',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/phone/patient/index',
      name: 'patientPhoneIndex',
      component: PatientPhoneIndexView,
      meta: {
        title: '个人中心',
        isHideLeftMenu: true,
      },
    },

    {
      path: '/clinic/back/invite/me',
      name: 'backMeInvite',
      component: BackMeInviteView,
      meta: { 
        title: '我的邀请（营销）',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/back/merchant',
      name: 'backMerchant',
      component: BackMerchantView,  
      meta: { 
        title: '商家后台',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/back/merchant/shelf',
      name: 'backMerchantShelf',
      component: BackMerchantShelfView,  
      meta: { 
        title: '上架商品',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/user/userterms',
      name: 'UserTerms',
      component: UserTerms,
      meta: {
        title: '用户条款',
        isHideLeftMenu: true,
      },
    },
    {
      path: '/clinic/user/privacy',
      name: 'PrivacyPolicy',
      component: PrivacyPolicy,
      meta: {
        title: '隐私协议',
        isHideLeftMenu: true,
      },
    },
  ]
})

export default routerInstance


// 除官网页面外，不需要【登录检测】的页面
export const notCheckLoginPath = [
  '/clinic/login',
  '/clinic/phone/patient/clinic',
  '/clinic/phone/patient/index',
  '/clinic/phone/patient/admission',
  '/clinic/phone/patient/history/prescription',
  '/clinic/phone/patient/history/clinic',
  '/clinic/phone',
  '/price',
  '/clinic/drug',
  '/clinic/open/drug',
  '/clinic/user/userterms',
  '/clinic/user/privacy'
]

// 不显示【联系客服】按钮的页面
export const notContactServicePath = [
  '/',
  '/clinic/login',
  '/clinic/phone/patient/clinic',
  '/clinic/phone/patient/index',
  '/clinic/phone/patient/history/prescription',
  '/clinic/phone/patient/history/clinic',
  '/clinic/phone',
  '/clinic/phone/patient/admission',
  '/price',
  '/clinic/drug',
  '/clinic/open/drug',
    '/clinic/user/userterms',
    '/clinic/user/privacy'
]

import { systemRouterStore } from '@/stores/app'
import { searchMyRouter } from '@/api/user.api.js'
import { system } from '@/stores/system.js'
import { invite } from '@/stores/clinic/invite.js'
import { checkLogin } from '@/stores/UserStore.js'

routerInstance.beforeEach(async (to, from) => {

  if (from.fullPath === "/" && to.fullPath.startsWith("/inviteCode="))
    invite.setInvite(to.fullPath.split("=")[1]);
  if(to.fullPath == '/') {          
    // 访问官网，直接放过
    document.title = '诊所系统 | 便宜的诊所系统 | 更实用 | 易上手 | 诊所管理 | 码良云诊所 | 医疗 | 乡村医疗 | 乡村诊所 | 医疗点'
    leftMenuStore.hideLeftMenu()
    globalLoading.closeGlobalLoading()
  } else if(notCheckLoginPath.includes(to.path)) {  
    // 访问非官网其他无需登录的页面，直接放过
    leftMenuStore.hideLeftMenu()
    globalLoading.closeGlobalLoading()
  } else {
    // 如果是进入系统，则开启全局 loading（小车快跑）
    if(from.fullPath == '/' || from.fullPath == '/clinic/login') {
      globalLoading.openGlobalLoading()
    }
    if(checkLogin()) {
      // 获取缓存中的路由信息
      let routers = systemRouterStore.getRouters()

      // 命名当前页面标题
      document.title = to.meta.title ? to.meta.title : '诊所系统 | 便宜的诊所系统 | 更实用 | 易上手 | 诊所管理 | 码良云诊所 | 医疗 | 乡村医疗 | 乡村诊所 | 医疗点'

      // if 排除已经加载【系统 & 路由】，而且未指定【必须重新加载】的情况
      if(routers != null && system.isReloadSystem) {
          switchLeftMenuState(to)
          return
      } else {
        let systemName
        let fullpath = to.fullPath
        if(fullpath.includes("clinic")) {
          systemName = "clinic"
        } else {
          switchLeftMenuState(to)
          globalLoading.closeGlobalLoading()
          return '/'
        }
        let routers = await searchMyRouter(systemName, new Date().getTime());
        if(routers && routers.data.data) {
          systemRouterStore.setRouters(routers.data.data) //保存路由信息
          system.isReloadSystem = true
        }
        switchLeftMenuState(to)
        globalLoading.closeGlobalLoading()
        return { path: to.fullPath }
      }
    } else {
      leftMenuStore.hideLeftMenu()
      globalLoading.closeGlobalLoading()
      return { path: '/clinic/login' }
    }
  }
})

function switchLeftMenuState(to) {
  if(to.meta.isHideLeftMenu) {
    leftMenuStore.hideLeftMenu()
  } else {
    leftMenuStore.showLeftMenu()
  }
}

routerInstance.onError(error => {
  const fetchResourcesErrors = ['Failed to fetch dynamically imported module', 'Importing a module script failed']
   if (fetchResourcesErrors.some((item) => error?.message && error.message?.includes(item))) {
     window.location.reload()
   }
 
});

routerInstance.afterEach((to, from) => {
  globalLoading.closeGlobalLoading()
})
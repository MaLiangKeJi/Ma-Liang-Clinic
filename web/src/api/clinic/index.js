/** 
 * api 接口统一出口管理
 */

import patientService from '@/api/clinic/patient.api.js'; // 病人
import dossierService from '@/api/clinic/dossier.api.js'; // 病历
import tagService from '@/api/clinic/tag.api.js'; // 标签
import manufacturerService from '@/api/clinic/manufacturer.api.js'; // 药企
import userService from '@/api/clinic/user.api.js'; // 用户
import groupService from '@/api/clinic/group.api.js'; // 组
import roleService from '@/api/clinic/role.api.js'; // 角色
import permissionService from '@/api/clinic/permission.api.js'; //权限
import stockService from '@/api/clinic/stock.api.js' //库存
import drugService from '@/api/clinic/drug.api.js' //药品
import prescriptionService from '@/api/clinic/prescription.api.js' //处方
import payService from '@/api/clinic/pay.api.js' //支付
import admissionService from '@/api/clinic/admission.api.js' //接诊日志
import sterilizeService from '@/api/clinic/sterilizeLog.api.js' //消毒日志
import disinfectionService from '@/api/clinic/disinfectionLog.api.js' //消杀日志
import retailService from '@/api/clinic/retail.api.js' //零售
import settingService from '@/api/clinic/setting.api.js' //设置
import proofService from '@/api/clinic/proof.api.js' //设置
import resourceService from '@/api/clinic/resource.api.js' //设置
import logService from '@/api/clinic/log.api.js' //日志
import informService from '@/api/clinic/inform.api.js' //通知

export {
    patientService,
    dossierService,
    tagService,
    manufacturerService,
    userService,
    groupService,
    roleService,
    permissionService,
    stockService,
    drugService,
    prescriptionService,
    payService,
    admissionService,
    sterilizeService,
    disinfectionService,
    retailService,
    settingService,
    proofService,
    resourceService,
    logService,
    informService,
}
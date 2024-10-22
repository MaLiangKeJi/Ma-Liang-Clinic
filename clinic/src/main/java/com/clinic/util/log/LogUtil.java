package com.clinic.util.log;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.clinic.entity.OperationLog;
import org.slf4j.event.Level;

import java.math.BigDecimal;

public class LogUtil {

    /**
     * 操作日志
     * <p>
     *     注意：操作需要视为事务的一部分！！！！！（如果一个重要操作的执行，没有痕迹，则无法溯源可能发生的问题）
     * </p>
     */
    public static class Operation {

        /**
         * 记录
         * @param service   业务
         * @param operation 操作
         * @param level     级别
         */
        public static void record(Integer serviceCode, String service, String about, String operation, Level level, Long patientId, String param) {
            Db.save(new OperationLog(getExecLocation(), serviceCode, service, about, operation, level, patientId, param));
        }

        public static void record(ServiceLogEnums serviceLogEnum, Level level, Long patientId, String param, CharSequence template, Object... params) {
            record(serviceLogEnum.getServiceCode(), serviceLogEnum.getTitle(), serviceLogEnum.getLabel(), StrUtil.format(template, params), level, patientId, param);
        }

        public static void record(ServiceLogEnums serviceLogEnum, Level level, Long patientId, Long param, CharSequence template, Object... params) {
            record(serviceLogEnum.getServiceCode(), serviceLogEnum.getTitle(), serviceLogEnum.getLabel(), StrUtil.format(template, params), level, patientId, param.toString());
        }

        public static void record(ServiceLogEnums serviceLogEnum, Level level, Long patientId, CharSequence template, Object... params) {
            record(serviceLogEnum.getServiceCode(), serviceLogEnum.getTitle(), serviceLogEnum.getLabel(), StrUtil.format(template, params), level, patientId, null);
        }

        public static void record(ServiceLogEnums serviceLogEnum, Level level, Long patientId, BigDecimal param, CharSequence template, Object... params) {
            record(serviceLogEnum.getServiceCode(), serviceLogEnum.getTitle(), serviceLogEnum.getLabel(), StrUtil.format(template, params), level, patientId, param.toString());
        }

        /**
         * 新增库存
         */
        public static void addStock(String stockNo, CharSequence template, Object... params) {
            record(ServiceLogEnums.STOCK_ADD, Level.INFO, null, stockNo, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 零售药品
         */
        public static void retailDrug(Long patientId, BigDecimal totalPrice, CharSequence template, Object... params) {
            record(ServiceLogEnums.RETAIL, Level.INFO, patientId, totalPrice, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 接诊
         */
        public static void reception(Long patientId, Long logId, CharSequence template, Object... params) {
            record(ServiceLogEnums.ADMISSION, Level.INFO, patientId, logId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增支付项
         */
        public static void newPayItem(Long payId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PAY_ADD_ITEM, Level.INFO, null, payId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 修改支付项
         */
        public static void updatePayItem(Long payId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PAY_UPDATE_ITEM, Level.INFO, null, payId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 支付 - 结束就诊
         */
        public static void pay(Long patientId, Long admissionId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PAY, Level.INFO, patientId, admissionId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增处方
         */
        public static void addPrescription(Long patientId, Long prescriptionId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PRESCRIPTION_ADD, Level.INFO, patientId, prescriptionId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 修改处方
         */
        public static void updatePrescription(Long patientId, Long prescriptionId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PRESCRIPTION_UPDATE, Level.INFO, patientId, prescriptionId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 下载处方
         */
        public static void downloadPrescription(Long prescriptionId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PRESCRIPTION_DOWNLOAD, Level.INFO, null, prescriptionId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增诊断证明
         */
        public static void addDiagnosisProof(Long patientId, Long diagnosisProofId, CharSequence template, Object... params) {
            record(ServiceLogEnums.DIAGNOSIS_PROOF_ADD, Level.INFO, patientId, diagnosisProofId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增消杀
         */
        public static void addDisinfection(Long diagnosisProofId, CharSequence template, Object... params) {
            record(ServiceLogEnums.DISINFECTION_ADD, Level.INFO, null, diagnosisProofId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增病例
         */
        public static void addDossier(Long patientId, Long dossierId, CharSequence template, Object... params) {
            record(ServiceLogEnums.DOSSIER_ADD, Level.INFO, patientId, dossierId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 修改病例
         */
        public static void updateDossier(Long patientId, Long dossierId, CharSequence template, Object... params) {
            record(ServiceLogEnums.DOSSIER_UPDATE, Level.INFO, patientId, dossierId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 添加病人信息
         */
        public static void addPatient(Long patientId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PATIENT_ADD, Level.INFO, patientId, StrUtil.format(template, params), Level.INFO);
        }
        /**
         * 修改病人信息
         */
        public static void updatePatient(Long patientId, CharSequence template, Object... params) {
            record(ServiceLogEnums.PATIENT_UPDATE, Level.INFO, patientId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增诊所设置
         */
        public static void addClinicSetting(CharSequence template, Object... params) {
            record(ServiceLogEnums.USER_SETTING_UPDATE, Level.INFO, null, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 修改诊所设置
         */
        public static void updateClinicSetting(CharSequence template, Object... params) {
            record(ServiceLogEnums.USER_SETTING_UPDATE, Level.INFO, null, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 新增消毒
         */
        public static void addSterilize(Long sterilizeId, CharSequence template, Object... params) {
            record(ServiceLogEnums.STERILIZE_ADD, Level.INFO, null, sterilizeId, StrUtil.format(template, params), Level.INFO);
        }

        /**
         * 获取调用地址
         * @return 调用地址（格式：packagePath::methodName）
         */
        private static String getExecLocation() {
            StackTraceElement prevMethod = Thread.currentThread().getStackTrace()[3];
            return prevMethod.getClassName() + "::" + prevMethod.getMethodName();
        }
    }
}

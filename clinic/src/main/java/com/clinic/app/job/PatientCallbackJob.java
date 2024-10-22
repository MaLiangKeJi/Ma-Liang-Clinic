package com.clinic.app.job;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.clinic.entity.PatientCallback;
import com.clinic.service.PatientCallbackService;
import com.clinic.util.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 任务：患者回访
 */
@Slf4j
@Component
public class PatientCallbackJob {

    @Resource
    private PatientCallbackService patientCallbackService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


//    @Scheduled(cron = "${scheduled.cron.expression}")
    @Scheduled(cron = "0 0 8 * * ?")
    public void startDayCallbackJob() {
        List<PatientCallback> needExecJobs = patientCallbackService.lambdaQuery()
                .eq(PatientCallback::getType, PatientCallbackTypeEnum.ADMISSION.getCode())
                .eq(PatientCallback::getCallbackDate, new Date())
                .list();
        for (PatientCallback job : needExecJobs) {
            try {
                // 解析 JSON 格式的参数
                List<Object> params = JSONObject.parseArray(job.getParams(), Object.class);

                // 获取方法引用
                String methodReference = job.getClassMethodName();

                // 解析方法引用
                String[] parts = methodReference.split("::");
                Class<?> clazz = Class.forName(parts[0]);
                Method method = clazz.getMethod(parts[1], parts.getClass());

                // 获取 Spring Bean
                Object bean = SpringUtil.getBean(clazz);

                // 调用方法
                method.invoke(bean, params);
            } catch (Exception e) {
                log.error("回访任务执行失败: {}", e.getMessage(), e);
            }
        }
    }

    private void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Map<String, Object> parseParameters(String parametersJson) throws Exception {
        if (isNotBlank(parametersJson)) {
            return new HashMap<>();
        }
        return objectMapper.readValue(parametersJson, Map.class);
    }

    private Method getMethod(Class<?> clazz, String methodName, Map<String, Object> parameters) throws Exception {
        Class<?>[] parameterTypes = parameters.values().stream()
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

        return clazz.getDeclaredMethod(methodName, parameterTypes);
    }

    private Object[] getParameterValues(Map<String, Object> parameters) {
        return parameters.values().toArray();
    }

    /**
     * 创建回访任务
     */
    public void setCallbackJon(Long patient, PatientCallbackTypeEnum callbackType, Date callbackDate, Object methodReference, Object... args) {
        Long loginUserId = LoginUser.getId();
        /// 转换方法引用为全限定名
        String methodReferenceStr = MethodReferenceUtils.convertMethodReferenceToQualifiedName(methodReference);
        patientCallbackService.save(new PatientCallback(loginUserId, patient, callbackType.getCode(), methodReferenceStr, JSONObject.toJSONString(args), callbackDate));
    }
}

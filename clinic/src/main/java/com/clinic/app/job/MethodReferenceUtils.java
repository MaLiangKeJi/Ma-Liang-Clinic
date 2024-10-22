package com.clinic.app.job;

import java.lang.reflect.Method;

public class MethodReferenceUtils {

    public static String convertMethodReferenceToQualifiedName(Object methodReference) {
        // 获取方法引用的目标类型
        Method method = getTargetMethod(methodReference);

        if (method == null) {
            throw new IllegalArgumentException("Invalid method reference");
        }

        // 获取方法的类和名称
        Class<?> declaringClass = method.getDeclaringClass();
        String methodName = method.getName();

        // 构造全限定名
        return declaringClass.getName() + "::" + methodName;
    }

    private static Method getTargetMethod(Object methodReference) {
        try {
            // 获取方法引用的目标方法
            Method method = methodReference.getClass().getMethod("writeReplace");
            Object serializedForm = method.invoke(methodReference);
            Method serializedMethod = serializedForm.getClass().getMethod("get串联MethodName");
            return (Method) serializedMethod.invoke(serializedForm);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get target method from method reference", e);
        }
    }
}

package net.jlxxw.apicenter.facade.scanner;

import net.jlxxw.apicenter.facade.annotation.RemoteRegister;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方法扫描
 * @author zhanxiumei
 */
@Component
public class MethodScanner {

    private static  final Logger logger = LoggerFactory.getLogger(MethodScanner.class);
    /**
     * 方法本地注册表
     */
    private static final Map<String,MethodInfo> REGISTRY_TABLE = new ConcurrentHashMap<>(16);

    /**
     * 将扫描到到方法注册到注册表中
     * @param object 实例对象
     * @param method 调用的方法
     * @param serviceCode 方法唯一识别码
     * @param parameterTypes 方法参数类型列表
     * @param hasReturn 是否具有返回值
     */
    private void registry(Object object, Method method,String serviceCode,Class[] parameterTypes,boolean hasReturn){
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setParameterTypes(parameterTypes);
        methodInfo.setMethod(method);
        methodInfo.setObject(object);
        methodInfo.setHasReturn(hasReturn);
        REGISTRY_TABLE.put(serviceCode,methodInfo);
        logger.info("registry method "+method);
    }

    /**
     * 扫描方法，并检测是否合规
     * @param bean spring bean
     */
    public void scanMethod(Object bean){
        Class clazz;
        if(AopUtils.isAopProxy(bean)){
            clazz = AopUtils.getTargetClass(bean);
        }else{
            clazz = bean.getClass();
        }
        // 获取全部声明的方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if(Objects.nonNull(declaredMethods)){
            for (Method declaredMethod : declaredMethods) {
                // 如果方法包含指定的注解，则进行相关解析
                if(declaredMethod.isAnnotationPresent(RemoteRegister.class)){
                    RemoteRegister annotation = declaredMethod.getAnnotation(RemoteRegister.class);
                    String serviceCode = annotation.serviceCode();
                    if(StringUtils.isBlank(serviceCode)){
                        // 注解中的 service code 不能为空
                        throw new IllegalArgumentException("method:" + declaredMethod +" serviceCode is not null");
                    }
                    if(REGISTRY_TABLE.containsKey(serviceCode)){
                        // 注解中的 service code 不能重复
                        MethodInfo methodInfo = REGISTRY_TABLE.get(serviceCode);
                        throw new IllegalArgumentException("method:" + declaredMethod + " serviceCode exists,please check "+methodInfo.getMethod().getName());
                    }

                    // 获取返回值类型
                    Class<?> returnType = declaredMethod.getReturnType();
                    // 获取参数列表
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if(parameterTypes.length >0){
                        for (Class<?> parameterType : parameterTypes) {
                            if(parameterType.isArray() || parameterType.isEnum()){
                                throw new IllegalArgumentException("method: "+declaredMethod + "param is not support,not support type:array,enum");
                            }
                        }
                    }
                    registry(bean,declaredMethod,serviceCode,parameterTypes,"void".equals(returnType.getName()));
                }
            }
        }
    }

    /**
     * 根据方法注解编码，获取相关执行的方法
     * @param serviceCode
     * @return
     */
    public MethodInfo getMethod(String serviceCode){
        return REGISTRY_TABLE.get(serviceCode);
    }
}




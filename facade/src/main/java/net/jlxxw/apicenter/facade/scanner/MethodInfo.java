package net.jlxxw.apicenter.facade.scanner;

import java.lang.reflect.Method;

/**
 * 注册方法基本信息映射实体
 * @author zhanxiumei
 */
public class MethodInfo {
    /**
     * 实例对象
     */
    private Object object;

    /**
     * 注册的方法
     */
    private Method method;

    /**
     * 参数类型
     */
    private Class[] parameterTypes;

    /**
     * 是否具有返回值
     */
    private boolean hasReturn;

    /**
     * 方法参数名字
     */
    private String[] methodParamNames;
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public boolean isHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public String[] getMethodParamNames() {
        return methodParamNames;
    }

    public void setMethodParamNames(String[] methodParamNames) {
        this.methodParamNames = methodParamNames;
    }
}

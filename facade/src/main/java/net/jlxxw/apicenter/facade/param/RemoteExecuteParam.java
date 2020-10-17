package net.jlxxw.apicenter.facade.param;

import java.io.Serializable;

/**
 * @author zhanxiumei
 */
public class RemoteExecuteParam implements Serializable {

    /**
     * 方法serviceCode
     */
    private String serviceCode;

    /**
     * 方法的参数数据
     */
    private String methodParamJson;

    /**
     * 方法执行标识
     */
    private String methodFlag;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getMethodParamJson() {
        return methodParamJson;
    }

    public void setMethodParamJson(String methodParamJson) {
        this.methodParamJson = methodParamJson;
    }

    public String getMethodFlag() {
        return methodFlag;
    }

    public void setMethodFlag(String methodFlag) {
        this.methodFlag = methodFlag;
    }
}

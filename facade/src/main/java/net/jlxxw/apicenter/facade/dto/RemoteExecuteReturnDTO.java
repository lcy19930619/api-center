package net.jlxxw.apicenter.facade.dto;

import java.io.Serializable;

/**
 * 远程执行方法返回值
 * @author zhanxiumei
 */
public class RemoteExecuteReturnDTO implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String message;
    /**
     * 方法注解上的service code
     */
    private String serviceCode;

    /**
     * 方法执行完毕的返回值
     */
    private String returnData;

    /**
     * 方法是否具有返回值
     */
    private boolean hasReturn;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public boolean isHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

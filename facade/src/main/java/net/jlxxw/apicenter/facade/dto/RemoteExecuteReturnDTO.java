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
    private Object returnData;

    /**
     * 方法是否具有返回值
     */
    private boolean hasReturn;


    /**
     * netty 唯一编号
     */
    private String channelId;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}

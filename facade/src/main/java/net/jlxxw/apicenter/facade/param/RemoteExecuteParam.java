package net.jlxxw.apicenter.facade.param;

import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;

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

    /**
     * 目标服务的ip
     */
    private String ip;

    /**
     * 目标服务的端口
     */
    private Integer port;

    /**
     * 执行结果
     */
    private RemoteExecuteReturnDTO result;

    /**
     * 用户的基本信息
     */
    private RemoteUserInfo remoteUserInfo;

    /**
     * netty 唯一编号
     */
    private String channelId;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

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

    public RemoteExecuteReturnDTO getResult() {
        return result;
    }

    public void setResult(RemoteExecuteReturnDTO result) {
        this.result = result;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public RemoteUserInfo getRemoteUserInfo() {
        return remoteUserInfo;
    }

    public void setRemoteUserInfo(RemoteUserInfo remoteUserInfo) {
        this.remoteUserInfo = remoteUserInfo;
    }
}

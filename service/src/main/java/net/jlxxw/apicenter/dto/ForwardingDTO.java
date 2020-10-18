package net.jlxxw.apicenter.dto;

import java.util.Map;

/**
 * 2020-10-18 11:36
 *
 * @author LCY
 */
public class ForwardingDTO {

    /**
     * 服务code
     */
    private String serviceCode;

    /**
     * 请求参数
     */
    private Map<Object,Object> requestParam;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Map<Object, Object> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(Map<Object, Object> requestParam) {
        this.requestParam = requestParam;
    }
}

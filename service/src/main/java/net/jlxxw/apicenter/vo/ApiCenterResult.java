package net.jlxxw.apicenter.vo;

import net.jlxxw.apicenter.constant.ResultCodeInterface;

/**
 * 网关执行结果
 * 2020-10-18 11:55
 *
 * @author LCY
 */
public class ApiCenterResult<T> {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误码
     */
    private String code;

    /**
     * 数据载体
     */
    private T data;

    private ApiCenterResult(Boolean success, String message, String code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ApiCenterResult success(T data){
        return new ApiCenterResult<>(true,null,"success",data);
    }

    public static ApiCenterResult failed(String code, String message){
        return new ApiCenterResult<>(false,message,code,null);
    }

    public static ApiCenterResult failed(ResultCodeInterface resultCodeInterface){
        return new ApiCenterResult<>(false,resultCodeInterface.getMessage(),resultCodeInterface.getCode(),null);
    }
}

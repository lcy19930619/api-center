package net.jlxxw.apicenter.constant;

/**
 * 2020-10-18 12:24
 *
 * @author LCY
 */
public enum ResultCodeEnum implements ResultCodeInterface {

    /**
     * 错误的serviceCode
     */
    SERVICE_CODE_IS_NOT_EXISTS("SERVICE_CODE_IS_NOT_EXISTS","错误的serviceCode"),

    /**
     * 对应的服务已下线
     */
    SERVER_IS_OFFLINE("SERVER_IS_OFFLINE","对应的服务已下线"),
    /**
     * 远程调用执行失败
     */
    REMOTE_EXECUTE_FAILED("","远程调用执行失败"),

    /**
     * 没有访问权限
     */
    AUTH_ERROR("AUTH_ERROR","没有访问权限"),
    ;

    private String code;

    private String message;


    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取错误信息
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }
}

package net.jlxxw.apicenter.intergration.buc;

/**
 * 2020-10-18 11:21
 *
 * @author LCY
 */
public interface BucClient {

    /**
     * 检查用户是否具有指定接口权限
     * @param token 使用者SSO token
     * @param serviceCode 服务code
     * @return  true 可以访问，false 无权限，跳转403页面
     */
    Boolean auth(String token,String serviceCode);

    /**
     * 获取用户的基本信息
     * @param token SSO token
     * @param <T> 用户基本信息实体类
     * @return 用户的基本信息
     */
    <T> T selectUserByToken(String token);
}

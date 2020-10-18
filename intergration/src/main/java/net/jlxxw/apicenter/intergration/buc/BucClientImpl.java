package net.jlxxw.apicenter.intergration.buc;

import org.springframework.stereotype.Service;

/**
 * 2020-10-18 12:55
 *
 * @author LCY
 */
@Service
public class BucClientImpl implements BucClient {

    /**
     * 检查用户是否具有指定接口权限
     *
     * @param token       使用者SSO token
     * @param serviceCode 服务code
     *
     * @return true 可以访问，false 无权限，跳转403页面
     */
    @Override
    public Boolean auth(String token, String serviceCode) {
        return true;
    }

    /**
     * 获取用户的基本信息
     *
     * @param token SSO token
     *
     * @return 用户的基本信息
     */
    @Override
    public <T> T selectUserByToken(String token) {
        return null;
    }
}

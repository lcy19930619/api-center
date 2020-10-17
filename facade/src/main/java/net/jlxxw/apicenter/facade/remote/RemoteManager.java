package net.jlxxw.apicenter.facade.remote;

import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class RemoteManager extends AbstractRemoteManager {

    /**
     * 创建远程执行代理对象
     *
     * @param apiCenterClientProperties 自定义的配置文件
     * @throws ApiCenterException 创建失败则抛出相关异常
     */
    @Override
    public void createExecuteProxy(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {

    }

    /**
     * 向注册中心注册
     *
     * @param apiCenterClientProperties
     * @throws ApiCenterException
     */
    @Override
    protected void registryCenter(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {

    }

    /**
     * 初始化通信框架
     *
     * @param apiCenterClientProperties
     */
    @Override
    protected void initNetty(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {

    }

    /**
     * 关闭代理对象
     */
    @Override
    public void closeProxy() {

    }
}

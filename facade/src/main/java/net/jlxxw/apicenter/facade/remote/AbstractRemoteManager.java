package net.jlxxw.apicenter.facade.remote;

import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanxiumei
 */
public abstract class AbstractRemoteManager {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRemoteManager.class);

    /**
     * 向注册中心注册
     * @param apiCenterClientProperties 自定义的配置文件
     * @throws ApiCenterException 创建失败则抛出相关异常
     */
    protected abstract void registryCenter(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException;

    /**
     * 初始化通信框架
     * @param apiCenterClientProperties 自定义的配置文件
     * @throws ApiCenterException 创建失败则抛出相关异常
     */
    protected abstract void initNetty(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException;
    /**
     * 关闭代理对象
     */
    public abstract void closeProxy();


    public void init(ApiCenterClientProperties apiCenterClientProperties){
        logger.info("init netty");
        initNetty(apiCenterClientProperties);
        logger.info("init netty done");

        logger.info("join registryCenter");
        registryCenter(apiCenterClientProperties);
        logger.info("join registryCenter done");

    }
}

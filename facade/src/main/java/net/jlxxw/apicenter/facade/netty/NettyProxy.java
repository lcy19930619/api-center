package net.jlxxw.apicenter.facade.netty;

import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;

/**
 * @author zhanxiumei
 */
public interface NettyProxy {

    /**
     * 初始化netty代理对象
     */
    void initProxy(ApiCenterClientProperties apiCenterClientProperties);


}

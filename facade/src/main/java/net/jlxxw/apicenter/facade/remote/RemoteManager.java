package net.jlxxw.apicenter.facade.remote;

import net.jlxxw.apicenter.facade.constant.ApiCenterConstant;
import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.utils.IPAddressUtils;
import net.jlxxw.apicenter.facade.utils.ZookeeperUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class RemoteManager extends AbstractRemoteManager {

    @Autowired
    private ZookeeperUtils zookeeperUtils;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 向注册中心注册
     *
     * @param apiCenterClientProperties
     * @throws ApiCenterException
     */
    @Override
    protected void registryCenter(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {
        if (StringUtils.isBlank(applicationName)) {
            throw new IllegalArgumentException("application name is not null");
        }
        String path = ApiCenterConstant.PARENT_NODE + "/" + applicationName + "/" + IPAddressUtils.getIpAddress() + ":" +
                apiCenterClientProperties.getPort();
        if (!zookeeperUtils.existsNode(path)) {
            // 如果节点不存在，创建节点
            zookeeperUtils.createOpenACLEphemeralNode(path, "".getBytes());
        }

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

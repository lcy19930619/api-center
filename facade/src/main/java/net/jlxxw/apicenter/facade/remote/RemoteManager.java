package net.jlxxw.apicenter.facade.remote;

import net.jlxxw.apicenter.facade.constant.ApiCenterConstant;
import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.netty.NettyProxy;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.utils.IPAddressUtils;
import net.jlxxw.apicenter.facade.utils.ZookeeperUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class RemoteManager extends AbstractRemoteManager {

    private static final Logger logger = LoggerFactory.getLogger(RemoteManager.class);
    @Autowired
    private ZookeeperUtils zookeeperUtils;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private NettyProxy nettyProxy;
    /**
     * 向注册中心注册
     *
     * @param apiCenterClientProperties
     *
     * @throws ApiCenterException
     */
    @Override
    protected void registryCenter(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {
        if (StringUtils.isBlank( applicationName )) {
            throw new IllegalArgumentException( "application name is not null" );
        }
        if (!zookeeperUtils.existsNode(ApiCenterConstant.PARENT_NODE)) {
            // 如果api center主节点不存在，创建节点
            zookeeperUtils.createOpenACLPersistentNode(ApiCenterConstant.PARENT_NODE, "".getBytes());
        }

        String parentPath = ApiCenterConstant.PARENT_NODE + "/" + applicationName ;
        if (!zookeeperUtils.existsNode( parentPath )) {
            // 如果节点不存在，创建节点
            zookeeperUtils.createOpenACLPersistentNode( parentPath, "".getBytes() );
        }

        String serverIp = apiCenterClientProperties.getServerIp();


        if(StringUtils.isBlank(serverIp)){
            String ipAddress = IPAddressUtils.getIpAddress();
            logger.info("server ip not found,enable automatic acquisition,ip address :"+ipAddress);
        }


        parentPath = parentPath + "/" + serverIp + ":" + apiCenterClientProperties.getPort();
        if (!zookeeperUtils.existsNode( parentPath )) {
            // 如果节点不存在，创建节点
            zookeeperUtils.createOpenACLEphemeralNode( parentPath, "".getBytes() );
        }

    }

    /**
     * 初始化通信框架
     *
     * @param apiCenterClientProperties
     */
    @Override
    protected void initNetty(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {
        nettyProxy.initProxy( apiCenterClientProperties );
    }

    /**
     * 关闭代理对象
     */
    @Override
    public void closeProxy() {

    }
}

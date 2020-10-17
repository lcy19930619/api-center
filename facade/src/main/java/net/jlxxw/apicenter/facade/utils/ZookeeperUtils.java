package net.jlxxw.apicenter.facade.utils;

import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.watcher.ZookeeperWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author zhanxiumei
 * @Description:
 * @date: 2020/10/16 13:51
 */
@Component
public class ZookeeperUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);
    private ZooKeeper zooKeeper = null;
    private ZookeeperUtils(ApiCenterClientProperties apiCenterClientProperties) throws IOException {
       zooKeeper = new ZooKeeper(apiCenterClientProperties.getZookeeperHosts(), apiCenterClientProperties.getTimeout(), new ZookeeperWatcher());
    }

    /**
     * 创建一个开放权限的永久节点
     * @param node 节点
     * @param data 数据
     */
    public void createOpenACLPersistentNode(String node, byte[] data) throws ApiCenterException {
        try {
            zooKeeper.create(node, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }catch (Exception e){
            logger.error("zookeeper error :",e);
            throw new ApiCenterException(e.getMessage());
        }
    }

    /**
     * 判断节点是否存在
     * @param node 节点
     * @return true 存在，false 不存在
     * @throws ApiCenterException 自定义异常
     */
    public Boolean existsNode(String node) throws ApiCenterException  {
        try {
             return Objects.nonNull(zooKeeper.exists(node, new ZookeeperWatcher()));
        }catch (Exception e){
            logger.error("zookeeper error :",e);
            throw new ApiCenterException(e.getMessage());
        }
    }


    /**
     * 创建一个开放权限的临时节点
     * @param node 节点
     * @param data 数据
     * @throws ApiCenterException 自定义异常
     */
    public void createOpenACLEphemeralNode(String node, byte[] data) throws ApiCenterException {
        try {
            zooKeeper.create(node,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }catch (Exception e){
            logger.error("zookeeper error :",e);
            throw new ApiCenterException(e.getMessage());
        }
    }

    /**
     * 查询指定节点下的全部子节点
     * @param parentNode 父节点
     * @throws ApiCenterException 自定义异常
     */
    public List<String> listChildrenNodes(String parentNode) throws ApiCenterException  {
        try {
            return zooKeeper.getChildren(parentNode,null);
        }catch (Exception e){
            logger.error("zookeeper error :",e);
            throw new ApiCenterException(e.getMessage());
        }
    }

    /**
     * 获取指定节点的数据
     * @param node 节点(如果是zookeeper,则需要完整路径，例如：/parentNode/childNode)
     * @throws ApiCenterException 自定义异常
     * @return
     */
    public byte[] getNodeData(String node)  throws ApiCenterException {
        try {
            return zooKeeper.getData(node, false, new Stat());
        }catch (Exception e){
            logger.error("zookeeper error :",e);
            throw new ApiCenterException(e.getMessage());
        }
    }


}

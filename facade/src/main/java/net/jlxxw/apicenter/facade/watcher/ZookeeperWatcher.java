package net.jlxxw.apicenter.facade.watcher;

import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.utils.ZookeeperUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Description:
 * @author: lengchunyang
 * @date: 2020/10/16 14:09
 */
public class ZookeeperWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatcher.class);


    private ApiCenterClientProperties apiCenterClientProperties;

    private ZookeeperUtils zookeeperUtils;

    public ZookeeperWatcher(ApiCenterClientProperties apiCenterClientProperties,ZookeeperUtils zookeeperUtils) {
        this.apiCenterClientProperties = apiCenterClientProperties;
        this.zookeeperUtils = zookeeperUtils;
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            logger.info("zookeeper通知：会话连接成功");
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                logger.info("zookeeper进入会话初始状态");
            }
        } else if (Event.KeeperState.Disconnected == event.getState()) {
            logger.error("zookeeper通知：会话连接失败");
        } else if (Event.KeeperState.AuthFailed == event.getState()) {
            logger.warn("zookeeper通知：会话认证失败");
        } else if (Event.KeeperState.Expired == event.getState()) {
            logger.warn("zookeeper通知：会话过期");
            try {
                logger.info("try reconnection zookeeper .....");
                zookeeperUtils.resetConnection(apiCenterClientProperties);
                logger.info("try reconnection zookeeper success");
            } catch (IOException e) {
                logger.error("zookeeper reconnection failed!!!",e);
            }
        } else {
            logger.error("未知的通知状态：" + event.getState());
        }
    }
}

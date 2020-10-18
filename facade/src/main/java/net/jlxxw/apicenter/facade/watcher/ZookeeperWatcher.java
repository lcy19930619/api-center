package net.jlxxw.apicenter.facade.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @author: lengchunyang
 * @date: 2020/10/16 14:09
 */
public class ZookeeperWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatcher.class);
    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            logger.info("通知：会话连接成功");
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                logger.info("进入会话初始状态");
            }
        } else if (Event.KeeperState.Disconnected == event.getState()) {
            logger.error("通知：会话连接失败");
        } else if (Event.KeeperState.AuthFailed == event.getState()) {
            logger.warn("通知：会话认证失败");
        } else if (Event.KeeperState.Expired == event.getState()) {
            logger.warn("通知：会话过期");
        } else {
            logger.error("未知的通知状态：" + event.getState());
        }

    }
}

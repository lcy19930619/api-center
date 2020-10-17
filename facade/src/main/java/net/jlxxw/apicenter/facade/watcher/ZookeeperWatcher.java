package net.jlxxw.apicenter.facade.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Description:
 * @author: lengchunyang
 * @date: 2020/10/16 14:09
 */
public class ZookeeperWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive WatchedEvent：" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            System.out.println("通知：会话连接成功");
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                System.out.println("进入会话初始状态");
            } else if (event.getType() == Event.EventType.NodeCreated) {
                System.out.println("节点创建通知：" + event.getPath());
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("节点的数据变更通知：");
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("子节点的数据变更通知：");
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("节点删除通知：" + event.getPath());
            } else {
                System.out.println("未知事件通知类型：" + event.getType());
            }
        } else if (Event.KeeperState.Disconnected == event.getState()) {
            System.out.println("通知：会话连接失败");
        } else if (Event.KeeperState.AuthFailed == event.getState()) {
            System.out.println("通知：会话认证失败");
        } else if (Event.KeeperState.Expired == event.getState()) {
            System.out.println("通知：会话过期");
        } else {
            System.out.println("未知的通知状态：" + event.getState());
        }

    }
}

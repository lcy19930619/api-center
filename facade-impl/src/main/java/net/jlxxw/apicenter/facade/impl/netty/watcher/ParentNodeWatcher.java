package net.jlxxw.apicenter.facade.impl.netty.watcher;

import net.jlxxw.apicenter.facade.constant.ApiCenterConstant;
import net.jlxxw.apicenter.facade.impl.netty.NettyClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 2020-10-18 20:38
 *
 * @author LCY
 */
@Component
public class ParentNodeWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger( ParentNodeWatcher.class );
    @Autowired
    private NettyClient nettyClient;

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                String path = event.getPath();
                int index = path.lastIndexOf( "/" );
                String address = path.substring( index + 1 );
                String[] split = address.split( ":" );
                String ip = split[0];
                int port = Integer.valueOf( split[1] );
                if (event.getType() == Event.EventType.NodeCreated) {
                    // 节点创建通知，如果创建了一个节点，则会自动回调此函数
                    if (path.startsWith( ApiCenterConstant.PARENT_NODE )) {
                        // 创建一个新的netty客户端
                    }
                    logger.info( "listener create node ：" + path );
                }
                if (event.getType() == Event.EventType.NodeDeleted) {
                    nettyClient.removeChannel( ip, port );
                    logger.info( "listener delete node ：" + path );
                }
            }
        } catch (Exception e) {
            logger.error("zookeeper found exception:",e);
        }
    }
}

package net.jlxxw.apicenter.facade.impl.netty.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.impl.netty.ClientHandler;
import net.jlxxw.apicenter.facade.impl.netty.NettyClient;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhanxiumei
 */
@Service
public class NettyClientImpl  implements NettyClient  {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientImpl.class);
    //管理以ip:端口号为key的连接池   FixedChannelPool继承SimpleChannelPool，有大小限制的连接池实现
    private static AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;

    /**
     * key channel ID，value 请求参数
     */
    private static Map<String,RemoteExecuteParam> map = new ConcurrentHashMap<>();
    //启动辅助类 用于配置各种参数
    private  Bootstrap bootstrap =new Bootstrap();

    public NettyClientImpl(){
        ClientHandler clientHandler = new ClientHandler( this );
        bootstrap.group(new NioEventLoopGroup())
                .channel( NioSocketChannel.class)
                .option( ChannelOption.TCP_NODELAY,true);
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress inetSocketAddress) {
                ChannelPoolHandler handler = new ChannelPoolHandler() {
                    //使用完channel需要释放才能放入连接池
                    @Override
                    public void channelReleased(Channel ch) throws Exception {

                    }
                    //当链接创建的时候添加channel handler，只有当channel不足时会创建，但不会超过限制的最大channel数
                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        logger.info("channelCreated. Channel ID: " + ch.id());
                        ch.pipeline().addLast(new ObjectEncoder());
                        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,  ClassResolvers.weakCachingConcurrentResolver(null)));
                        ch.pipeline().addLast(clientHandler);//添加相应回调处理
                    }
                    //获取连接池中的channel
                    @Override
                    public void channelAcquired(Channel ch) throws Exception {

                    }
                };
                return new FixedChannelPool(bootstrap.remoteAddress(inetSocketAddress), handler, 5); //单个服务端连接池大小
            }
        };
    }
    /**
     * 向远程服务发送相关数据
     *
     * @param param
     * @return
     */
    @Override
    public RemoteExecuteReturnDTO send(RemoteExecuteParam param) throws InterruptedException {
        String ip = param.getIp();
        Integer port = param.getPort();
        InetSocketAddress address = new InetSocketAddress(ip, port);
        final SimpleChannelPool pool = poolMap.get(address);
        final Future<Channel> future = pool.acquire();
        future.addListener( (FutureListener<Channel>) arg0 -> {
            if (future.isSuccess()) {
                Channel ch = future.getNow();
                ChannelId id = ch.id();
                String tempId = id.toString();
                param.setChannelId( tempId );
                map.put( tempId,param );
                ch.writeAndFlush(param);
                //放回去
                pool.release(ch);
            }
        } );
        synchronized (param) {
            //因为异步 所以不阻塞的话 该线程获取不到返回值
            //放弃对象锁 并阻塞等待notify
            try {
                // 超时时间十秒，到时自动释放
                param.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return param.getResult();
    }



    /**
     * 根据channelId 获取执行参数
     *
     * @param channelId
     * @return
     */
    @Override
    public RemoteExecuteParam getRemoteExecuteParam(String channelId) {
        return map.get(channelId);
    }

    /**
     * 当指定的服务下线后，移除此通道
     *
     * @param ip
     * @param port
     */
    @Override
    public void removeChannel(String ip, Integer port) {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        poolMap.remove( address );
    }


    public static void main(String[] args) throws InterruptedException {
        RemoteExecuteParam remoteExecuteParam = new RemoteExecuteParam();
        remoteExecuteParam.setIp( "192.168.1.31" );
        remoteExecuteParam.setPort( 31697 );
        RemoteExecuteReturnDTO send = new NettyClientImpl().send( remoteExecuteParam );

    }
}

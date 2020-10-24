package net.jlxxw.apicenter.facade.netty.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.netty.NettyProxy;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.remote.AbstractRemoteExecuteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class NettyProxyImpl implements NettyProxy {

    private static final Logger logger = LoggerFactory.getLogger( NettyProxyImpl.class );
    @Autowired
    private AbstractRemoteExecuteProxy abstractRemoteExecuteProxy;

    /**
     * 初始化netty代理对象
     */
    @Override
    public void initProxy(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {
        int port = apiCenterClientProperties.getPort();

        //netty主从线程模型(建立2个线程组) 一个用于网络读写   一个用于和客户的进行连接
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            //启动辅助类 用于配置各种参数
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)//最大排列队数
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
                            socketChannel.pipeline().addLast(new ServerHandler( abstractRemoteExecuteProxy ));//处理类
                        }
                    });
            //绑定端口 同步等待成功
            ChannelFuture future=b.bind(port).sync();
            logger.info("api center client listener on port:"+port);
            //同步等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error( "api center start error :"+e );
        } finally {
            //释放资源退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

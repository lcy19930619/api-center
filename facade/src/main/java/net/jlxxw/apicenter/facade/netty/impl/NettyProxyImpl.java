package net.jlxxw.apicenter.facade.netty.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.jlxxw.apicenter.facade.exception.ApiCenterException;
import net.jlxxw.apicenter.facade.netty.NettyProxy;
import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class NettyProxyImpl implements NettyProxy {

    private static final Logger logger = LoggerFactory.getLogger(NettyProxyImpl.class);
    @Autowired
    private ServerHandler serverHandler;
    /**
     * 初始化netty代理对象
     */
    @Override
    public void initProxy(ApiCenterClientProperties apiCenterClientProperties) throws ApiCenterException {
        // 用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 用来处理已经被接收的连接，一旦bossGroup接收到连接，就会把连接信息注册到workerGroup上
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // nio服务的启动类
            ServerBootstrap sbs = new ServerBootstrap();
            // 配置nio服务参数
            sbs.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 说明一个新的Channel如何接收进来的连接
                    .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
                    .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 处理接收到的请求
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers
                                    .weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast("encoder", new ObjectEncoder());

                            pipeline.addLast(serverHandler);
                        }
                    });
            logger.info("netty starter listener:" + apiCenterClientProperties.getPort());
            // 绑定端口，开始接受链接
            ChannelFuture cf = sbs.bind(apiCenterClientProperties.getPort()).sync();
            // 等待服务端口的关闭；在这个例子中不会发生，但你可以优雅实现；关闭你的服务
            if (cf != null) {
                cf.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            logger.error("netty starter failed",e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

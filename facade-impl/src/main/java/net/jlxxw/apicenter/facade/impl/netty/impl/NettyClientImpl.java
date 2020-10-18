package net.jlxxw.apicenter.facade.impl.netty.impl;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.impl.netty.ClientHandler;
import net.jlxxw.apicenter.facade.impl.netty.NettyClient;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhanxiumei
 */
@Service
public class NettyClientImpl  implements NettyClient  {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientImpl.class);
    /**
     * netty的客户信息连接池
     */
    private static Map<String, ChannelFuture> map = new ConcurrentHashMap<>();

    private RemoteExecuteReturnDTO result = null;

    private volatile Boolean done = false;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private static final int RETRY_MAX = 5;
    /**
     * 向远程服务发送相关数据
     *
     * @param param
     * @return
     */
    @Override
    public RemoteExecuteReturnDTO send(RemoteExecuteParam param) throws InterruptedException {
        ChannelFuture channelFuture = map.get(param.getIp() + ":" + param.getPort());
        Channel channel = channelFuture.channel();
        if (channel.isOpen()) {
            // 发送客户端的请求
            channel.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(param).getBytes(StandardCharsets.UTF_8)));

            while(true){
                if(atomicInteger.incrementAndGet()<=RETRY_MAX){
                    if(done){
                        atomicInteger.set(0);
                        done = false;
                        RemoteExecuteReturnDTO result = this.result;
                        this.result = null;
                        return result;
                    }else{
                        Thread.sleep(300);
                    }
                }else{
                    RemoteExecuteReturnDTO dto = new RemoteExecuteReturnDTO();
                    dto.setSuccess(false);
                    dto.setMessage("timeout");
                    atomicInteger.set(0);
                    done = false;
                    return dto;
                }
            }

        } else {
            channel.connect(new SocketAddress() {
                // todo
            });
        }

        return null;
    }

    /**
     * 创建客户端
     */
    @Override
    public void createClient(String ip, int port) throws InterruptedException {
        ClientHandler clientHandler = new ClientHandler( this );
        String key = ip + ":" + port;
        if (!map.containsKey(key)) {
            EventLoopGroup bossGroup = new NioEventLoopGroup();

            Bootstrap bs = new Bootstrap();

            bs.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers
                                    .weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast("encoder", new ObjectEncoder());

                            // 处理来自服务端的响应信息
                            pipeline.addLast(clientHandler);
                        }
                    });

            // 客户端开启
            ChannelFuture cf = bs.connect(ip, port).sync();
            map.put(key, cf);

            new Thread( () -> {
                // 等待直到连接中断
                try {
                    cf.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    logger.error( "create netty client error!!!" );
                    e.printStackTrace();
                }
            } ).start();

        }
    }

    @Override
    public void removeClient(String ip, int port) {
        String key = ip + ":" + port;
        if (!map.containsKey(key)) {
            ChannelFuture channelFuture = map.get(key);
            Channel channel = channelFuture.channel();
            channel.close();
            map.remove( key );
            logger.info( "remove netty client " + key);
        }
    }

    @Override
    public void done(RemoteExecuteReturnDTO result){
        done = true;
        this.result = result;
    }
}

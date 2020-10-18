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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhanxiumei
 */
@Component
public class NettyClientImpl  implements NettyClient  {


    private static Map<String, ChannelFuture> map = new ConcurrentHashMap<>();

    @Autowired
    private NettyClient nettyClient;

    private RemoteExecuteReturnDTO result = null;

    private volatile Boolean done;

    private AtomicInteger atomicInteger = new AtomicInteger();

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
                if(atomicInteger.get()<=RETRY_MAX){
                    if(done){
                        atomicInteger.set(0);
                        done = false;
                        return this.result;
                    }else{
                        Thread.sleep(300);
                    }
                }else{
                    RemoteExecuteReturnDTO dto = new RemoteExecuteReturnDTO();
                    dto.setSuccess(false);
                    dto.setMessage("timeout");
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
                            pipeline.addLast(new ClientHandler(nettyClient));
                        }
                    });

            // 客户端开启
            ChannelFuture cf = bs.connect(ip, port).sync();
            map.put(key, cf);

            // 等待直到连接中断
            cf.channel().closeFuture().sync();
        }
    }

    @Override
    public void done(RemoteExecuteReturnDTO result){
        done = true;
        this.result = result;
    }
}

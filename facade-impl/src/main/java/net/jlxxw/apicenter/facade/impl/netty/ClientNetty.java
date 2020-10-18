package net.jlxxw.apicenter.facade.impl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.jlxxw.apicenter.facade.exception.ApiCenterException;

import java.nio.charset.StandardCharsets;


/**
 * 客户端发送请求
 * @author zhanxiumei
 *
 */
public class ClientNetty {

    // 要请求的服务器的ip地址
    private String ip;
    // 服务器的端口
    private int port;

    public ClientNetty(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    // 请求端主题
    private void action() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        Bootstrap bs = new Bootstrap();

        bs.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 处理来自服务端的响应信息
                        socketChannel.pipeline().addLast((ChannelHandler) new ApiCenterException("这里我不会写了不知道"));
                    }
                });

        // 客户端开启
        ChannelFuture cf = bs.connect(ip, port).sync();

        String reqStr = "我是客户端请求1$_";

        // 发送客户端的请求
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes(StandardCharsets.UTF_8)));
//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes(Constant.charset)));
//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes(Constant.charset)));

//      Student student = new Student();
//      student.setId(3);
//      student.setName("张三");
//      cf.channel().writeAndFlush(student);

        // 等待直到连接中断
        cf.channel().closeFuture().sync();
    }

}

package net.jlxxw.apicenter.facade.netty.impl;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.remote.AbstractRemoteExecuteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * 处理某个客户端的请求
 *
 * @author zhanxiumei
 */
@Component
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private AbstractRemoteExecuteProxy abstractRemoteExecuteProxy;

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // 普通的处理 及过滤器不多
        simpleRead(ctx, msg);
    }

    /**
     * 最简单的处理
     *
     * @param ctx
     * @param msg
     * @throws UnsupportedEncodingException
     */
    public void simpleRead(ChannelHandlerContext ctx, Object msg)  {

        ByteBuf bb = (ByteBuf) msg;
        // 创建一个和buf同等长度的字节数组
        byte[] reqByte = new byte[bb.readableBytes()];
        // 将buf中的数据读取到数组中
        bb.readBytes(reqByte);
        String reqStr = new String(reqByte, StandardCharsets.UTF_8);
        logger.info(" A request is received from the client： " + reqStr);
        //远程调用的执行请求的返回值
        RemoteExecuteReturnDTO execute = abstractRemoteExecuteProxy.execute(reqStr);
        //将执行结果转换成json格式
        String result = JSON.toJSONString(execute);
        // 返回给客户端响应                                                                                                                                                       和客户端链接中断即短连接，当信息返回给客户端后中断
        ctx.writeAndFlush(Unpooled.copiedBuffer(result.getBytes()));
    }

    /**
     * 数据读取完毕的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //服务端读取数据完毕
        logger.info("The server has finished reading the data");
    }

    /**
     * 出现异常的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //读取数据出现异常
        logger.error(" There was an exception reading the data");
        ctx.close();
    }


}


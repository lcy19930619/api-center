package net.jlxxw.apicenter.facade.netty.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.remote.AbstractRemoteExecuteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理某个客户端的请求
 *
 * @author zhanxiumei
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private AbstractRemoteExecuteProxy abstractRemoteExecuteProxy;

    public ServerHandler(AbstractRemoteExecuteProxy abstractRemoteExecuteProxy) {
        this.abstractRemoteExecuteProxy = abstractRemoteExecuteProxy;
    }

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        RemoteExecuteParam bb = (RemoteExecuteParam) msg;
        String reqStr = JSON.toJSONString( bb );
        logger.info(" A request is received from the client： " + reqStr);
        //远程调用的执行请求的返回值
        RemoteExecuteReturnDTO execute = abstractRemoteExecuteProxy.execute(reqStr);
        // 返回给客户端响应                                                                                                                                                       和客户端链接中断即短连接，当信息返回给客户端后中断
        ctx.writeAndFlush(execute);
    }


    /**
     * 数据读取完毕的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //服务端读取数据完毕
        logger.info("Read data over");
        ctx.flush();
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
        logger.error("Read data error :",cause);
        ctx.close();
    }


}


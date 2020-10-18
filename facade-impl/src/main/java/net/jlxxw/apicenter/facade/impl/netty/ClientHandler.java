package net.jlxxw.apicenter.facade.impl.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 读取服务器返回的响应信息
 * @author zhanxiumei
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private NettyClient nettyClient;

    public ClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            ByteBuf bb = (ByteBuf)msg;
            byte[] respByte = new byte[bb.readableBytes()];
            bb.readBytes(respByte);
            String resultJson = new String(respByte, StandardCharsets.UTF_8);
            logger.info("client--收到响应：" + resultJson);

            RemoteExecuteReturnDTO result = getResult(resultJson);
            nettyClient.done(result);

        } finally{
            // 必须释放msg数据
            ReferenceCountUtil.release(msg);

        }

    }




    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println("客户端读取数据完毕");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("client 读取数据出现异常");
        ctx.close();
    }

    private RemoteExecuteReturnDTO getResult(String json){
        return JSONObject.parseObject(json,RemoteExecuteReturnDTO.class);
    }

}


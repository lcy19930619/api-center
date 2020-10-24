package net.jlxxw.apicenter.facade.impl.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {

        try {
            RemoteExecuteReturnDTO result = (RemoteExecuteReturnDTO)msg;
            String channelId = result.getChannelId();
            RemoteExecuteParam param = new RemoteExecuteParam();
            param.setChannelId(channelId);
            param.setResult(result);
            nettyClient.done( param );
        } catch (Exception e){
            RemoteExecuteParam result = new RemoteExecuteParam();
            RemoteExecuteReturnDTO obj = new RemoteExecuteReturnDTO();
            obj.setSuccess( false );
            obj.setMessage( "远程执行产生位置异常！" );
            logger.error( "远程执行产生位置异常！",e );
            nettyClient.done( result );
        }
    }

    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端读取数据完毕");
        ctx.flush();
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client 读取数据出现异常");
        ctx.close();
    }

}


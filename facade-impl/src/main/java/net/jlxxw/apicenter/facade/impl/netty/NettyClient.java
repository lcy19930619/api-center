package net.jlxxw.apicenter.facade.impl.netty;

import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;

/**
 * 2020-10-18 11:10
 *
 * @author LCY
 */
public interface NettyClient {

    /**
     * 向远程服务发送相关数据
     * @param param
     * @return
     */
    RemoteExecuteReturnDTO send(RemoteExecuteParam param) throws InterruptedException;

    /**
     * 根据channelId 获取执行参数
     * @param channelId
     * @return
     */
    RemoteExecuteParam getRemoteExecuteParam(String channelId);

    /**
     * 当指定的服务下线后，移除此通道
     * @param ip
     * @param port
     */
    void removeChannel(String ip,Integer port);
}

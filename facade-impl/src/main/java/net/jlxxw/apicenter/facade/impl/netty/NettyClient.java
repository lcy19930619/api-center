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
     * netty方法执行完毕回调此方法
     * @param param
     */
    void done(RemoteExecuteParam param);

    /**
     * 当指定的服务下线后，移除此通道
     * @param ip
     * @param port
     */
    void removeChannel(String ip,Integer port);
}

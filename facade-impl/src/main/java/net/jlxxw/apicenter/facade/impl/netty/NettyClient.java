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
    RemoteExecuteReturnDTO send(RemoteExecuteParam param);
}

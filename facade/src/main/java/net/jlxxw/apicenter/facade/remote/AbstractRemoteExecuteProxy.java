package net.jlxxw.apicenter.facade.remote;

import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.param.RemoteMultipartFileParam;

/**
 * @author zhanxiumei
 */
public abstract class AbstractRemoteExecuteProxy {

    /**
     * 远程执行方法
     * @param param 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    protected abstract RemoteExecuteReturnDTO remoteExecute(RemoteExecuteParam param);

    /**
     * 远程执行上传文件
     * @param remoteMultipartFileParam
     * @return
     */
    protected abstract RemoteExecuteReturnDTO remoteUpload(RemoteMultipartFileParam remoteMultipartFileParam);

    /**
     * 远程执行下载
     * @param param
     * @return
     */
    protected abstract RemoteExecuteReturnDTO remoteDownLoad(RemoteExecuteParam param);

    /**
     * 执行远程方法
     * @param remoteExecuteParam
     * @return
     */
    public abstract RemoteExecuteReturnDTO execute(RemoteExecuteParam remoteExecuteParam);
}

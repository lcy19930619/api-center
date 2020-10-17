package net.jlxxw.apicenter.facade.remote;

/**
 * @author zhanxiumei
 */

import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.enums.MethodFlagEnum;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.param.RemoteFileDownLoadParam;
import net.jlxxw.apicenter.facade.param.RemoteMultipartFileParam;
import net.jlxxw.apicenter.facade.scanner.MethodInfo;
import net.jlxxw.apicenter.facade.scanner.MethodScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class RemoteExecuteProxy extends AbstractRemoteExecuteProxy {

    @Autowired
    private  MethodScanner methodScanner;

    /**
     * 执行远程方法
     *
     * @param remoteExecuteParam 执行参数
     * @return 执行结果
     */
    @Override
    public RemoteExecuteReturnDTO execute(RemoteExecuteParam remoteExecuteParam) {
        RemoteExecuteReturnDTO executeReturn = new RemoteExecuteReturnDTO();
        String serviceCode = remoteExecuteParam.getServiceCode();
        MethodInfo methodInfo = methodScanner.getMethod(serviceCode);
        if(Objects.isNull(methodInfo)){
            executeReturn.setSuccess(false);
            executeReturn.setMessage(serviceCode + "未注册为远程方法");
            return executeReturn;
        }

        String methodFlag = remoteExecuteParam.getMethodFlag();
        if(MethodFlagEnum.NORMAL.name().equals(methodFlag)){
            // 执行普通方法
           return remoteExecute(remoteExecuteParam);
        }
        if(MethodFlagEnum.UPLOAD.name().equals(methodFlag)){
            // 执行上传方法
            return remoteUpload((RemoteMultipartFileParam)remoteExecuteParam);
        }
        if(MethodFlagEnum.DOWNLOAD.name().equals(methodFlag)){
            // 执行下载方法
            return remoteDownLoad(remoteExecuteParam);
        }
        RemoteExecuteReturnDTO dto = new RemoteExecuteReturnDTO();
        dto.setSuccess(false);
        dto.setMessage("api center method flag error");
        return dto;
    }

    /**
     * 远程执行方法
     *
     * @param param 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    @Override
    protected RemoteExecuteReturnDTO remoteExecute(RemoteExecuteParam param) {
        RemoteExecuteReturnDTO executeReturn = new RemoteExecuteReturnDTO();
        String serviceCode = param.getServiceCode();
        MethodInfo methodInfo = methodScanner.getMethod(serviceCode);


        Method method = methodInfo.getMethod();
        Class[] parameterTypes = methodInfo.getParameterTypes();
        Object object = methodInfo.getObject();


        return null;
    }

    /**
     * 远程执行上传文件
     *
     * @param remoteMultipartFileParam 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    @Override
    protected RemoteExecuteReturnDTO remoteUpload(RemoteMultipartFileParam remoteMultipartFileParam) {
        return null;
    }

    /**
     * 远程执行下载
     *
     * @param param 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    @Override
    protected RemoteFileDownLoadParam remoteDownLoad(RemoteExecuteParam param) {
        return null;
    }
}

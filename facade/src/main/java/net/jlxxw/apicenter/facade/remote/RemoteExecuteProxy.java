package net.jlxxw.apicenter.facade.remote;

/**
 * @author zhanxiumei
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.enums.MethodFlagEnum;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.param.RemoteFileDownLoadParam;
import net.jlxxw.apicenter.facade.param.RemoteMultipartFileParam;
import net.jlxxw.apicenter.facade.scanner.MethodInfo;
import net.jlxxw.apicenter.facade.scanner.MethodScanner;
import net.jlxxw.apicenter.facade.utils.RemoteParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class RemoteExecuteProxy extends AbstractRemoteExecuteProxy {

    private static final Logger logger = LoggerFactory.getLogger(RemoteExecuteProxy.class);

    @Autowired
    private  MethodScanner methodScanner;

    /**
     * 执行远程方法
     *
     * @param json 执行参数
     * @return 执行结果
     */
    @Override
    public RemoteExecuteReturnDTO execute(String json) {
        JSONObject jsonObject = JSON.parseObject(json);

        String methodFlag = jsonObject.getString("methodFlag");
        String serviceCode = jsonObject.getString("serviceCode");


        RemoteExecuteReturnDTO executeReturn = new RemoteExecuteReturnDTO();

        MethodInfo methodInfo = methodScanner.getMethod(serviceCode);
        if(Objects.isNull(methodInfo)){
            executeReturn.setSuccess(false);
            executeReturn.setMessage(serviceCode + "未注册为远程方法");
            return executeReturn;
        }


        if(MethodFlagEnum.NORMAL.name().equals(methodFlag)){
            RemoteExecuteParam remoteExecuteParam = jsonObject.toJavaObject(RemoteExecuteParam.class);
            // 执行普通方法
           return remoteExecute(remoteExecuteParam);
        }
        if(MethodFlagEnum.UPLOAD.name().equals(methodFlag)){
            // 执行上传方法
            RemoteMultipartFileParam remoteExecuteParam = jsonObject.toJavaObject(RemoteMultipartFileParam.class);
            return remoteUpload(remoteExecuteParam);
        }
        if(MethodFlagEnum.DOWNLOAD.name().equals(methodFlag)){
            RemoteExecuteParam remoteExecuteParam = jsonObject.toJavaObject(RemoteExecuteParam.class);
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
        // 从本地方法注册表寻找需要执行的方法
        MethodInfo methodInfo = methodScanner.getMethod(serviceCode);
        // 需要执行的方法
        Method method = methodInfo.getMethod();
        // 方法参数类型
        Class[] parameterTypes = methodInfo.getParameterTypes();
        // 执行的真实对象
        Object object = methodInfo.getObject();
        // 方法的具体参数
        Object[] args = RemoteParamUtils.decode(param.getMethodParamJson(), parameterTypes);
        try{
            method.invoke(object, args);
        }catch (Exception e){
            logger.error("api center reflect execute filed:",e);
            executeReturn.setMessage(e.getMessage());
            executeReturn.setSuccess(false);
            return executeReturn;
        }
        executeReturn.setSuccess(true);
        executeReturn.setReturnData(JSON.toJSONString(object));
        return executeReturn;
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

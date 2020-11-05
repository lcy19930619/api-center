package net.jlxxw.apicenter.facade.remote;

/**
 * @author zhanxiumei
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.enums.MethodFlagEnum;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.param.RemoteMultipartFileParam;
import net.jlxxw.apicenter.facade.param.RemoteUserInfo;
import net.jlxxw.apicenter.facade.scanner.MethodInfo;
import net.jlxxw.apicenter.facade.scanner.MethodScanner;
import net.jlxxw.apicenter.facade.utils.RemoteParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        logger.info( "api center send param:"+json );
        JSONObject jsonObject = JSON.parseObject(json);

        String methodFlag = jsonObject.getString("methodFlag");
        String serviceCode = jsonObject.getString("serviceCode");

        RemoteExecuteReturnDTO executeReturn = new RemoteExecuteReturnDTO();
        executeReturn.setChannelId(jsonObject.getString("channelId"));
        MethodInfo methodInfo = methodScanner.getMethod(serviceCode);
        if(Objects.isNull(methodInfo)){
            executeReturn.setSuccess(false);
            executeReturn.setMessage(serviceCode + "未注册为远程方法");
            return executeReturn;
        }

        RemoteExecuteReturnDTO dto = new RemoteExecuteReturnDTO();
        dto.setSuccess(false);
        dto.setMessage("api center method flag error");
        if(MethodFlagEnum.NORMAL.name().equals(methodFlag)){
            RemoteExecuteParam remoteExecuteParam = jsonObject.toJavaObject(RemoteExecuteParam.class);
            // 执行普通方法
            dto =  remoteExecute(remoteExecuteParam);
        }
        if(MethodFlagEnum.UPLOAD.name().equals(methodFlag)){
            // 执行上传方法
            RemoteMultipartFileParam remoteExecuteParam = jsonObject.toJavaObject(RemoteMultipartFileParam.class);
            dto =   remoteUpload(remoteExecuteParam);
        }
        if(MethodFlagEnum.DOWNLOAD.name().equals(methodFlag)){
            RemoteExecuteParam remoteExecuteParam = jsonObject.toJavaObject(RemoteExecuteParam.class);
            // 执行下载方法
            dto =   remoteDownLoad(remoteExecuteParam);
        }
        dto.setChannelId(jsonObject.getString("channelId"));

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
        return invoke(param);
    }

    /**
     * 远程执行上传文件
     *
     * @param remoteMultipartFileParam 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    @Override
    protected RemoteExecuteReturnDTO remoteUpload(RemoteMultipartFileParam remoteMultipartFileParam) {
        return invoke(remoteMultipartFileParam);
    }

    /**
     * 远程执行下载
     *
     * @param param 远程执行的输入参数
     * @return 本地执行完毕的返回参数
     */
    @Override
    protected RemoteExecuteReturnDTO remoteDownLoad(RemoteExecuteParam param) {
        return invoke(param);
    }


    /**
     * 反射执行
     * @param param
     * @return
     */
    private RemoteExecuteReturnDTO invoke(RemoteExecuteParam param) {
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
        // 全部参数名称列表
        String[] methodParamNames = methodInfo.getMethodParamNames();
        // 方法的具体参数
        Object[] args = RemoteParamUtils.decode(param.getMethodParamJson(), methodParamNames);
        Object result = null;
        // 处理全部参数类型
        if(Objects.nonNull(parameterTypes)){
            for (int i = 0; i < parameterTypes.length; i++) {
                if(parameterTypes[i].equals(RemoteUserInfo.class)){
                    // 准备用户基本信息
                    args[i] = param.getRemoteUserInfo();
                }
                if(parameterTypes[i].equals(MultipartFile.class)){
                    // 如果是上传文件类型的参数，需要单独处理这个参数
                    args[i] = param;
                }
            }
        }

        try{
            method.setAccessible(true);
            result = method.invoke(object, args); ;
        }catch (Exception e){
            logger.error("api center reflect execute filed:",e);
            executeReturn.setMessage(e.getMessage());
            executeReturn.setSuccess(false);
            return executeReturn;
        }
        executeReturn.setSuccess(true);
        executeReturn.setReturnData(JSON.toJSONString(result));
        return executeReturn;
    }

}

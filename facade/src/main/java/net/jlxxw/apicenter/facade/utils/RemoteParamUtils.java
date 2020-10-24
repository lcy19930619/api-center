package net.jlxxw.apicenter.facade.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @author zhanxiumei
 */
public class RemoteParamUtils {


    /**
     * 本地解析远程参数解码
     * @return
     */
    public static Object[] decode(String json,String[] paramNames){
        Object[] result = null;
        if(Objects.nonNull(paramNames)){
            result = new Object[paramNames.length];
            JSONObject jsonObject = JSON.parseObject(json);
            if(paramNames.length == 1){
                // 如果方法只有一个参数
                result[0] = JSON.parse(json);
            }else{
                for (int i = 0; i < paramNames.length; i++) {
                    result[i] = jsonObject.get(paramNames[i]);
                }
            }
        }
        return result;
    }
}

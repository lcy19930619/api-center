package net.jlxxw.apicenter.facade.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhanxiumei
 */
public class RemoteParamUtils {

    /**
     * 远程调用参数编码
     * @return
     */
    public static String encode(Object... params){
        String result = null;
        if(Objects.nonNull(params)){
            Map<Integer,Object> map = new HashMap<>();
            for (int i = 0; i < params.length; i++) {
                map.put(i,params[i]);
            }
            result = JSON.toJSONString(map);
        }
        return result;
    }

    /**
     * 本地解析远程参数解码
     * @return
     */
    public static Object[] decode(String json,Class... paramTypes){
        Object[] result = null;
        if(Objects.nonNull(paramTypes)){
            result = new Object[paramTypes.length];
            JSONObject jsonObject = JSON.parseObject(json);
            for (int i = 0; i < paramTypes.length; i++) {
                result[i] = jsonObject.get(i);
            }
        }
        return result;
    }
}

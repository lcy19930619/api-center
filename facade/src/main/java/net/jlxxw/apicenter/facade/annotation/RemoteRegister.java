package net.jlxxw.apicenter.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 远程注册注解，用于标注此方法可以被远程调用
 * @author zhanxiumei
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteRegister {

    /**
     * 远程调用唯一识别码，全系统唯一
     * @return
     */
    String serviceCode();
}

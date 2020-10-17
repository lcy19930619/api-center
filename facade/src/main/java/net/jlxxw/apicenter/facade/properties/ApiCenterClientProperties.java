package net.jlxxw.apicenter.facade.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * api center 的自定义装配的属性
 * @author zhanxiumei
 */
@Configuration
@ConfigurationProperties("api-center")
public class ApiCenterClientProperties {

    /**
     * 远程注册时的端口号，默认值 31697
     */
    private int port = 31697;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

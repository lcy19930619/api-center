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

    /**
     * zookeeper host地址，多个地址用逗号做分割
     * 例如 ：127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
     */
    private String zookeeperHosts = "localhost:2181";

    /**
     * zookeeper 超时时间
     */
    private int timeout = 5000;

    /**
     * 自定义的当前服务ip，如果不指定，则由程序自行获取
     */
    private String serverIp;


    public String getZookeeperHosts() {
        return zookeeperHosts;
    }

    public void setZookeeperHosts(String zookeeperHosts) {
        this.zookeeperHosts = zookeeperHosts;
    }

    public  int getTimeout() {
        return timeout;
    }

    public  void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

}

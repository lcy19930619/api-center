package net.jlxxw.apicenter.facade.impl.netty.config;

import net.jlxxw.apicenter.facade.impl.netty.NettyClient;
import net.jlxxw.apicenter.facade.impl.netty.impl.NettyClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 2020-10-18 20:58
 *
 * @author LCY
 */
@Configuration
public class NettyClientConfig {

    @Bean
    @Scope(value = "prototype")
    public NettyClient nettyClient(){
        return new NettyClientImpl();
    }
}

package net.jlxxw.apicenter.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

/**
 * 持久成相关配置类
 * @author zhanxiumei
 */
public class RepositoryConfig {
    /**
     * 响应式事务控制器
     * @param connectionFactory
     * @return
     */
    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}

package net.jlxxw.apicenter.facade.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author zhanxiumei
 */
@ComponentScan("net.jlxxw.apicenter.facade")
public class SpringMvcSupport implements ApplicationContextAware,ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SpringMvcSupport.class);

    private ApplicationContext applicationContext;

    @Autowired
    private ApiCenterRunner apiCenterRunner;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Class.forName("org.springframework.boot.autoconfigure.SpringBootApplication");
            logger.info("environment is spring boot");
        } catch (ClassNotFoundException e) {
            // 如果加载失败，则说明非boot环境，需要启动mvc支持
            apiCenterRunner.setApplicationContext(applicationContext);
            // 执行启动网关内容
            logger.info("environment is spring mvc ,enable spring mvc support");
            apiCenterRunner.run(null);
        }
    }
}

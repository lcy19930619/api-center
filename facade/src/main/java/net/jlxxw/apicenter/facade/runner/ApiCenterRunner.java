package net.jlxxw.apicenter.facade.runner;

import net.jlxxw.apicenter.facade.properties.ApiCenterClientProperties;
import net.jlxxw.apicenter.facade.remote.AbstractRemoteManager;
import net.jlxxw.apicenter.facade.scanner.MethodScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhanxiumei
 */
@Component
public class ApiCenterRunner implements ApplicationContextAware, ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApiCenterRunner.class);
    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    @Autowired
    private MethodScanner methodScanner;

    @Autowired
    private ApiCenterClientProperties apiCenterClientProperties;

    @Autowired
    private AbstractRemoteManager remoteManager;
    /**
     * boot启动完毕后会自动回调这个方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化远程执行相关全部内容
        remoteManager.init(apiCenterClientProperties);

        // 扫描全部bean
        logger.info("begin scan method");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            methodScanner.scanMethod(bean);
        }
        logger.info("method registry done");
    }

    /**
     * 获取Spring上下文
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

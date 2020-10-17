package net.jlxxw.apicenter.dao;

import net.jlxxw.apicenter.domain.ServiceInfoDO;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author zhanxiumei
 */
public interface ServiceInfoDAO extends ReactiveCrudRepository<ServiceInfoDO,Long> {
}

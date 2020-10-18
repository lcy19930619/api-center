package net.jlxxw.apicenter.service;

import net.jlxxw.apicenter.dto.ForwardingDTO;
import net.jlxxw.apicenter.vo.ApiCenterResult;
import reactor.core.publisher.Mono;

/**
 * 网关转发服务
 * 2020-10-18 11:35
 *
 * @author LCY
 */
public interface ForwardingService {

    /**
     * 处理网关转发服务
     * @param dto 前端页面入参对象
     * @return 网关处理结果
     */
    Mono<ApiCenterResult> forward(ForwardingDTO dto);
}

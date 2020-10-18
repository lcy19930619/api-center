package net.jlxxw.apicenter.service;

import net.jlxxw.apicenter.dto.ForwardingDTO;
import net.jlxxw.apicenter.vo.ApiCenterResult;

/**
 * 网关转发服务
 * 2020-10-18 11:35
 *
 * @author LCY
 */
public interface ForwardingService {

    ApiCenterResult forward(ForwardingDTO dto);
}

package net.jlxxw.apicenter.controller;

import net.jlxxw.apicenter.dto.ForwardingDTO;
import net.jlxxw.apicenter.service.ForwardingService;
import net.jlxxw.apicenter.vo.ApiCenterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 2020-10-18 12:43
 *
 * @author LCY
 */
@RestController
@RequestMapping("api-center")
public class ForwardingController {
    @Autowired
    private ForwardingService forwardingService;

    @PostMapping("forward")
    public Mono<ApiCenterResult> forward(@RequestBody ForwardingDTO dto){
        return forwardingService.forward( dto );
    }
}

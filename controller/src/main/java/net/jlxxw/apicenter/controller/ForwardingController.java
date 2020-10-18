package net.jlxxw.apicenter.controller;

import net.jlxxw.apicenter.dto.ForwardingDTO;
import net.jlxxw.apicenter.service.ForwardingService;
import net.jlxxw.apicenter.vo.ApiCenterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * 2020-10-18 12:43
 *
 * @author LCY
 */
@Controller
@RequestMapping("apiCenter")
public class ForwardingController {
    @Autowired
    private ForwardingService forwardingService;

    @RequestMapping("execute")
    @ResponseBody
    public Mono<ApiCenterResult> execute(@RequestBody ForwardingDTO dto){
        return forwardingService.forward( dto );
    }
}

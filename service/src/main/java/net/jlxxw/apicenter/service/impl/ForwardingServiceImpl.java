package net.jlxxw.apicenter.service.impl;

import com.alibaba.fastjson.JSON;
import net.jlxxw.apicenter.constant.ResultCodeEnum;
import net.jlxxw.apicenter.dao.ServiceInfoDAO;
import net.jlxxw.apicenter.domain.ServiceInfoDO;
import net.jlxxw.apicenter.dto.ForwardingDTO;
import net.jlxxw.apicenter.facade.constant.ApiCenterConstant;
import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;
import net.jlxxw.apicenter.facade.enums.MethodFlagEnum;
import net.jlxxw.apicenter.facade.impl.netty.NettyClient;
import net.jlxxw.apicenter.facade.param.RemoteExecuteParam;
import net.jlxxw.apicenter.facade.utils.ZookeeperUtils;
import net.jlxxw.apicenter.intergration.buc.BucClient;
import net.jlxxw.apicenter.service.ForwardingService;
import net.jlxxw.apicenter.vo.ApiCenterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * 2020-10-18 12:08
 *
 * @author LCY
 */
@Service
public class ForwardingServiceImpl implements ForwardingService {

    @Resource
    private ServiceInfoDAO serviceInfoDAO;
    @Autowired
    private ZookeeperUtils zookeeperUtils;
    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private BucClient bucClient;

    /**
     * 处理网关转发服务
     *
     * @param dto 前端页面入参对象
     *
     * @return 网关处理结果
     */
    @Override
    public Mono<ApiCenterResult> forward(ForwardingDTO dto) {

        /*
            判断service code 是否正确
         */
        ServiceInfoDO serviceInfoDO = serviceInfoDAO.findByServiceCode( dto.getServiceCode() );
        if (Objects.isNull( serviceInfoDO )) {
            return Mono.just( ApiCenterResult.failed( ResultCodeEnum.SERVICE_CODE_IS_NOT_EXISTS ) );
        }

        /*
            检测服务是否在线
         */
        String appName = serviceInfoDO.getAppName();
        List<String> nodes = zookeeperUtils.listChildrenNodes( ApiCenterConstant.PARENT_NODE + "/" + appName );
        if (CollectionUtils.isEmpty( nodes )) {
            return Mono.just( ApiCenterResult.failed( ResultCodeEnum.SERVER_IS_OFFLINE ) );
        }

        /*
            网关接口鉴权
         */
        if (!bucClient.auth( "", dto.getServiceCode() )) {
            return Mono.just( ApiCenterResult.failed( ResultCodeEnum.SERVER_IS_OFFLINE ) );
        }

        /*
            随机获取一个服务节点
         */
        Random random = new Random();
        int index = random.nextInt( nodes.size() );
        String address = nodes.get( index );
        String[] split = address.split( ":" );

        /*
            执行远程方法
         */
        RemoteExecuteParam remoteExecuteParam = new RemoteExecuteParam();
        remoteExecuteParam.setServiceCode( dto.getServiceCode() );
        remoteExecuteParam.setMethodParamJson( JSON.toJSONString( dto.getRequestParam() ) );
        remoteExecuteParam.setMethodFlag( MethodFlagEnum.NORMAL.name() );
        try {
            remoteExecuteParam.setIp( split[0] );
            remoteExecuteParam.setPort( Integer.valueOf( split[1] ) );
            RemoteExecuteReturnDTO result = nettyClient.send( remoteExecuteParam );
            return Mono.just( ApiCenterResult.success( result ) );
        } catch (Exception e) {
            return Mono.just( ApiCenterResult.failed( ResultCodeEnum.REMOTE_EXECUTE_FAILED ) );
        }
    }
}

package net.jlxxw.apicenter.dao;

import net.jlxxw.apicenter.domain.ServiceInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhanxiumei
 */
@Mapper
public interface ServiceInfoDAO {

    @Select( "select " +
            "id,service_code as serviceCode,app_name as appName,app_id as appId, permission_code as permissionCode,`desc` " +
            "from service_info  " +
            "where service_code = #{serviceCode}" )
    ServiceInfoDO findByServiceCode(@Param( "serviceCode" ) String serviceCode);
}

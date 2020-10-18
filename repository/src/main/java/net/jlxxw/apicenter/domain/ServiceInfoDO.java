package net.jlxxw.apicenter.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author zhanxiumei
 */
@Setter
@Getter
@Table("service_info")
public class ServiceInfoDO {

    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 接口code
     */
    private String serviceCode;

    /**
     * 所属应用名称
     */
    private String appName;

    /**
     * 所属应用ID
     */
    private Long appId;

    /**
     * 接口信息描述
     */
    private String desc;

    /**
     * 接口权限码
     */
    private String permissionCode;

}

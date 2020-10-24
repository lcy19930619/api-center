package net.jlxxw.apicenter.facade.param;

/**
 * 用户的基本信息
 * @author zhanxiumei
 */
public class RemoteUserInfo {

    /**
     * 用户表中的id
     */
    private Long id;

    /**
     * 用户工号
     */
    private String empId;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 真实名称
     */
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

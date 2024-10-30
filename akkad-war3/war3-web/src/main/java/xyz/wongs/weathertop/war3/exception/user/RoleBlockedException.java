package xyz.wongs.weathertop.war3.exception.user;

/**
 * @ClassName RoleBlockedException
 * @Description 角色锁定异常类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 17:11
 * @Version 1.0.0
*/
public class RoleBlockedException extends UserException {
    private static final long serialVersionUID = 1L;

    public RoleBlockedException() {
        super("role.blocked", null);
    }
}

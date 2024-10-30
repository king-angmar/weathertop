package xyz.wongs.weathertop.war3.exception.user;



import xyz.wongs.weathertop.war3.exception.base.BaseException;

/**
 * @ClassName UserException
 * @Description 用户信息异常类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 17:14
 * @Version 1.0.0
*/
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
